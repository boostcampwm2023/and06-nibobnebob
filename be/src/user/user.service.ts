import { Injectable } from "@nestjs/common";
import { UserInfoDto } from "./dto/userInfo.dto";
import { InjectRepository } from "@nestjs/typeorm";
import { UserRepository } from "./user.repository";
import { TokenInfo } from "./user.decorator";
import { hashPassword } from "../utils/encryption.utils";
import { SearchInfoDto } from "../restaurant/dto/seachInfo.dto";
import { UserRestaurantListRepository } from "./user.restaurantList.repository";
import { UserFollowListRepository } from "./user.followList.repository";
import { In } from "typeorm";
import { BadRequestException } from "@nestjs/common/exceptions";

@Injectable()
export class UserService {
  constructor(
    @InjectRepository(UserRepository)
    private usersRepository: UserRepository,
    private userRestaurantListRepository: UserRestaurantListRepository,
    private userFollowListRepositoy: UserFollowListRepository
  ) { }
  async signup(userInfoDto: UserInfoDto) {
    userInfoDto.password = await hashPassword(userInfoDto.password);
    return await this.usersRepository.createUser(userInfoDto);
  }
  async getNickNameAvailability(nickName: UserInfoDto["nickName"]) {
    return await this.usersRepository.getNickNameAvailability(nickName);
  }
  async getEmailAvailability(email: UserInfoDto["email"]) {
    return await this.usersRepository.getEmailAvailability(email);
  }
  async getMypageUserInfo(tokenInfo: TokenInfo) {
    return await this.usersRepository.getMypageUserInfo(tokenInfo.id);
  }
  async getUserInfo(nickName: UserInfoDto["nickName"]) {
    return await this.usersRepository.getUserInfo(nickName);
  }
  async getMypageUserDetailInfo(tokenInfo: TokenInfo) {
    return await this.usersRepository.getMypageUserDetailInfo(tokenInfo.id);
  }
  async getMyRestaurantListInfo(searchInfoDto: SearchInfoDto, tokenInfo: TokenInfo) {
    let results;
    if (searchInfoDto.radius) {
      results = await this.userRestaurantListRepository
        .createQueryBuilder('user_restaurant_lists')
        .leftJoinAndSelect('user_restaurant_lists.restaurant', 'restaurant')
        .select([
          'user_restaurant_lists.restaurantId',
          'restaurant.name',
          'restaurant.location',
          'restaurant.address',
          'restaurant.category',
          "restaurant.phoneNumber",
          "restaurant.reviewCnt"
        ])
        .where(`user_restaurant_lists.user_id = :userId and ST_DistanceSphere(
          location, 
          ST_GeomFromText('POINT(${searchInfoDto.longitude} ${searchInfoDto.latitude})', 4326)
      )<  ${searchInfoDto.radius}`, { userId: tokenInfo.id })
        .getMany();
    }
    else {
      results = await this.userRestaurantListRepository
        .createQueryBuilder('user_restaurant_lists')
        .leftJoinAndSelect('user_restaurant_lists.restaurant', 'restaurant')
        .select([
          'user_restaurant_lists.restaurantId AS restaurant_id',
          'restaurant.name',
          'restaurant.location',
          'restaurant.address',
          'restaurant.category',
          "restaurant.phoneNumber",
          "restaurant.reviewCnt"
        ])
        .where('user_restaurant_lists.user_id = :userId', { userId: tokenInfo.id })
        .getRawMany();
    }
    return results.map(result => ({
      ...result,
      isMy: true
    }));
  }
  async getMyFollowListInfo(tokenInfo: TokenInfo) {
    const userIds = await this.userFollowListRepositoy.getMyFollowListInfo(tokenInfo.id);
    const userIdValues = userIds.map(user => user.followingUserId);
    const result = await this.usersRepository.find({ select: ["nickName"], where: { 'id': In(userIdValues) } });
    return result.map(result => result.nickName);
  }
  async getMyFollowerListInfo(tokenInfo: TokenInfo) {
    const userIds = await this.userFollowListRepositoy.getMyFollowerListInfo(tokenInfo.id);
    const userIdValues = userIds.map(user => user.followedUserId);
    const result = await this.usersRepository.find({ select: ["nickName"], where: { 'id': In(userIdValues) } });
    return result.map(result => result.nickName);
  }
  async followUser(tokenInfo: TokenInfo, nickName: string) {
    const targetId = await this.usersRepository.findOne({ select: ["id"], where: { "nickName": nickName } })
    try {
      await this.userFollowListRepositoy.followUser(tokenInfo.id, targetId["id"]);
      return null;
    }
    catch (err) {
      throw new BadRequestException();
    }
  }

  async deleteUserAccount(tokenInfo: TokenInfo) {
    return await this.usersRepository.deleteUserAccount(tokenInfo.id);
  }
  async updateMypageUserInfo(tokenInfo: TokenInfo, userInfoDto: UserInfoDto) {
    return await this.usersRepository.updateMypageUserInfo(
      tokenInfo.id,
      userInfoDto
    );
  }
}
