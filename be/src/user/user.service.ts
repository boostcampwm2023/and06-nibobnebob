import { Injectable } from "@nestjs/common";
import { UserInfoDto } from "./dto/userInfo.dto";
import { InjectRepository } from "@nestjs/typeorm";
import { UserRepository } from "./user.repository";
import { TokenInfo } from "./user.decorator";
import { hashPassword } from "../utils/encryption.utils";
import { SearchInfoDto } from "../restaurant/dto/seachInfo.dto";
import { UserRestaurantListRepository } from "./user.restaurantList.repository";
import { UserFollowListRepository } from "./user.followList.repository";
import { Equal, In, Like, Not } from "typeorm";
import { BadRequestException } from "@nestjs/common/exceptions";
import { ReviewInfoDto } from "src/review/dto/reviewInfo.dto";
import { ReviewRepository } from "src/review/review.repository";
import { UserRestaurantListEntity } from "./entities/user.restaurantlist.entity";

@Injectable()
export class UserService {
  constructor(
    @InjectRepository(UserRepository)
    private usersRepository: UserRepository,
    private userRestaurantListRepository: UserRestaurantListRepository,
    private userFollowListRepositoy: UserFollowListRepository,
    private reviewRepository: ReviewRepository
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
  async getMypageTargetUserInfo(tokenInfo: TokenInfo, nickName: string) {
    const targetInfo = await this.usersRepository.findOne({ select: ["id"], where: { nickName: nickName } });
    try {
      const result = await this.usersRepository.getMypageTargetUserInfo(targetInfo.id);
      result.userInfo["isFollow"] = await this.userFollowListRepositoy.getFollowState(tokenInfo.id, targetInfo.id);
      return result;
    }
    catch (err) {
      throw new BadRequestException();
    }
  }
  async getMypageUserDetailInfo(tokenInfo: TokenInfo) {
    return await this.usersRepository.getMypageUserDetailInfo(tokenInfo.id);
  }
  async getMyRestaurantListInfo(searchInfoDto: SearchInfoDto, tokenInfo: TokenInfo) {
    const results = await this.userRestaurantListRepository.getMyRestaurantListInfo(searchInfoDto, tokenInfo.id);
    return results.map(result => ({
      ...result,
      "isMy": true
    }));
  }
  async getMyFollowListInfo(tokenInfo: TokenInfo) {
    const userIds = await this.userFollowListRepositoy.getMyFollowListInfo(tokenInfo.id);
    const userIdValues = userIds.map(user => user.followingUserId);
    const result = await this.usersRepository.find({ select: ["nickName", "region"], where: { 'id': In(userIdValues) } });
    return result.map(user => ({
      ...user,
      isFollow: 1
    }));
  }
  async getMyFollowerListInfo(tokenInfo: TokenInfo) {
    const followerUserIds = await this.userFollowListRepositoy.getMyFollowerListInfo(tokenInfo.id);
    const followUserIds = await this.userFollowListRepositoy.getMyFollowListInfo(tokenInfo.id);
    const followerUserIdValues = followerUserIds.map(user => user.followedUserId);
    const followUserIdValues = followUserIds.map(user => user.followingUserId);
    const result = await this.usersRepository.find({
      select: ["id", "nickName", "region"],
      where: { 'id': In(followerUserIdValues) }
    });

    return result.map(user => {
      const { id, ...userInfo } = user;
      return {
        ...userInfo,
        isFollow: followUserIdValues.includes(id) ? 1 : 0
      };
    });
  }
  async getRecommendUserListInfo(tokenInfo: TokenInfo) {
    const userIds = await this.userFollowListRepositoy.getMyFollowListInfo(tokenInfo.id);
    const userIdValues = userIds.map(user => user.followingUserId);
    userIdValues.push(tokenInfo.id);
    return await this.usersRepository.getRecommendUserListInfo(userIdValues);
  }
  async searchTargetUser(tokenInfo: TokenInfo, nickName: string) {
    const users = await this.usersRepository.find({
      select: ["id"],
      where: {
        "nickName": Like(`%${nickName}%`),
        id: Not(Equal(tokenInfo.id))
      },
      take: 20,
    });
    if (users.length) {
      const userIds = users.map(user => user.id);
      const result = await this.usersRepository.getUsersInfo(userIds);
      for (let i in result.userInfo) {
        result.userInfo[i]["isFollow"] = await this.userFollowListRepositoy.getFollowState(tokenInfo.id, userIds[i]);
      }
      return result;
    }
    return null;
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
  async unfollowUser(tokenInfo: TokenInfo, nickName: string) {
    const targetId = await this.usersRepository.findOne({ select: ["id"], where: { "nickName": nickName } })
    try {
      await this.userFollowListRepositoy.unfollowUser(tokenInfo.id, targetId["id"]);
      return null;
    }
    catch (err) {
      throw new BadRequestException();
    }
  }

  async addRestaurantToNebob(reviewInfoDto: ReviewInfoDto, tokenInfo: TokenInfo, restaurantId: number) {
    const reviewEntity = this.reviewRepository.create(reviewInfoDto);
    try {
      await this.reviewRepository.save(reviewEntity);
      await this.userRestaurantListRepository.addRestaurantToNebob(tokenInfo.id, restaurantId, reviewEntity);
    } catch (err) {
      throw new BadRequestException();
    }
    return null;
  }

  async deleteRestaurantFromNebob(tokenInfo: TokenInfo, restaurantId: number) {
    await this.userRestaurantListRepository.deleteRestaurantFromNebob(tokenInfo.id, restaurantId);
    return null;
  }

  async logout(tokenInfo: TokenInfo) {
    return await this.usersRepository.logout(tokenInfo.id);
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
