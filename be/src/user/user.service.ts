import { Injectable, UploadedFile } from "@nestjs/common";
import { UserInfoDto } from "./dto/userInfo.dto";
import { InjectRepository } from "@nestjs/typeorm";
import { UserRepository } from "./user.repository";
import { TokenInfo } from "./user.decorator";
import { hashPassword } from "../utils/encryption.utils";
import { SearchInfoDto } from "../restaurant/dto/seachInfo.dto";
import { UserRestaurantListRepository } from "./user.restaurantList.repository";
import { UserFollowListRepository } from "./user.followList.repository";
import { Equal, In, Like, Not } from "typeorm";
import { BadRequestException, ConflictException } from "@nestjs/common/exceptions";
import { ReviewInfoDto } from "src/review/dto/reviewInfo.dto";
import { ReviewRepository } from "src/review/review.repository";
import { UserWishRestaurantListRepository } from "./user.wishrestaurantList.repository";
import { AwsService } from "src/aws/aws.service";
import { v4 } from "uuid";
import { User } from "./entities/user.entity";
import { RestaurantInfoEntity } from "src/restaurant/entities/restaurant.entity";
import { AuthService } from "src/auth/auth.service";
import { SortInfoDto } from "src/utils/sortInfo.dto";

@Injectable()
export class UserService {
  constructor(
    @InjectRepository(UserRepository)
    private usersRepository: UserRepository,
    private userRestaurantListRepository: UserRestaurantListRepository,
    private userFollowListRepositoy: UserFollowListRepository,
    private reviewRepository: ReviewRepository,
    private userWishRestaurantListRepository: UserWishRestaurantListRepository,
    private awsService: AwsService,
    private authService: AuthService
  ) { }
  async signup(@UploadedFile() file: Express.Multer.File, userInfoDto: UserInfoDto) {
    if (userInfoDto.password) userInfoDto.password = await hashPassword(userInfoDto.password);
    let profileImage;

    if (file) {
      const uuid = v4();
      profileImage = `profile/images/${uuid}.png`;
    } else {
      profileImage = "profile/images/defaultprofile.png";
    }

    const user = {
      ...userInfoDto,
      profileImage: profileImage
    };

    try {
      const newUser = this.usersRepository.create(user);
      await this.usersRepository.createUser(newUser);
      if (file) {
        await this.awsService.uploadToS3(profileImage, file.buffer);
      }
      return;
    } catch (error) {
      if (error.code === "23505") {
        throw new ConflictException("Duplicated Value");
      }
    }
  }
  async getNickNameAvailability(nickName: UserInfoDto["nickName"]) {
    return await this.usersRepository.getNickNameAvailability(nickName);
  }
  async getEmailAvailability(email: UserInfoDto["email"]) {
    return await this.usersRepository.getEmailAvailability(email);
  }
  async getMypageUserInfo(tokenInfo: TokenInfo) {
    const result = await this.usersRepository.getMypageUserInfo(tokenInfo.id);
    result.userInfo.profileImage = this.awsService.getImageURL(result.userInfo.profileImage);
    return result;
  }
  async getMypageTargetUserInfo(tokenInfo: TokenInfo, nickName: string) {
    const targetInfo = await this.usersRepository.findOne({
      select: ["id"],
      where: { nickName: nickName },
    });
    try {
      const result = await this.usersRepository.getMypageTargetUserInfo(
        targetInfo.id
      );
      result["isFollow"] =
        (await this.userFollowListRepositoy.getFollowState(
          tokenInfo.id,
          targetInfo.id
        ))
          ? true
          : false;
      const restaurantList =
        await this.userRestaurantListRepository.getTargetRestaurantListInfo(
          targetInfo.id,
          tokenInfo.id
        );
      if (restaurantList) result["restaurants"] = restaurantList;
      result.profileImage = this.awsService.getImageURL(result.profileImage);
      return result;
    } catch (err) {
      throw new BadRequestException();
    }
  }
  async getMypageUserDetailInfo(tokenInfo: TokenInfo) {
    const result = await this.usersRepository.getMypageUserDetailInfo(tokenInfo.id);
    result.userInfo.profileImage = this.awsService.getImageURL(result.userInfo.profileImage);
    return result;
  }
  async getMyRestaurantListInfo(
    searchInfoDto: SearchInfoDto,
    sortInfoDto: SortInfoDto,
    tokenInfo: TokenInfo
  ) {
    const results =
      await this.userRestaurantListRepository.getMyRestaurantListInfo(
        searchInfoDto,
        sortInfoDto,
        tokenInfo.id
      );

    let list
    if ('items' in results) list = results.items;
    else list = results;

    for (const restaurant of list) {
      const reviewCount = await this.reviewRepository
        .createQueryBuilder("review")
        .where("review.restaurant_id = :restaurantId", {
          restaurantId: restaurant.restaurant_id,
        })
        .getCount();

      restaurant.isMy = true;
      restaurant.restaurant_reviewCnt = reviewCount;
    }

    return results;
  }
  async getMyWishRestaurantListInfo(tokenInfo: TokenInfo, sortInfoDto: SortInfoDto) {
    const result =
      await this.userWishRestaurantListRepository.getMyWishRestaurantListInfo(
        tokenInfo.id,
        sortInfoDto
      );
    return result;
  }
  async getStateIsWish(tokenInfo: TokenInfo, restaurantId: number) {
    const result = await this.userWishRestaurantListRepository.findOne({ where: { restaurantId: restaurantId, userId: tokenInfo["id"] } });
    if (result) return { isWish: true };
    else return { isWish: false };
  }
  async getMyFollowListInfo(tokenInfo: TokenInfo) {
    const userIds = await this.userFollowListRepositoy.getMyFollowListInfo(
      tokenInfo.id
    );
    const userIdValues = userIds.map((user) => user.followingUserId);
    const result = await this.usersRepository.find({
      select: ["nickName", "region"],
      where: { id: In(userIdValues) },
    });
    return result.map((user) => ({
      ...user,
      isFollow: true,
    }));
  }
  async getMyFollowerListInfo(tokenInfo: TokenInfo) {
    const followerUserIds =
      await this.userFollowListRepositoy.getMyFollowerListInfo(tokenInfo.id);
    const followUserIds =
      await this.userFollowListRepositoy.getMyFollowListInfo(tokenInfo.id);
    const followerUserIdValues = followerUserIds.map(
      (user) => user.followedUserId
    );
    const followUserIdValues = followUserIds.map(
      (user) => user.followingUserId
    );
    const result = await this.usersRepository.find({
      select: ["id", "nickName", "region"],
      where: { id: In(followerUserIdValues) },
    });

    return result.map((user) => {
      const { id, ...userInfo } = user;
      return {
        ...userInfo,
        isFollow: followUserIdValues.includes(id) ? true : false,
      };
    });
  }
  async getRecommendUserListInfo(tokenInfo: TokenInfo) {
    const userIds = await this.userFollowListRepositoy.getMyFollowListInfo(
      tokenInfo.id
    );
    const userIdValues = userIds.map((user) => user.followingUserId);
    userIdValues.push(tokenInfo.id);
    const result =
      await this.usersRepository.getRecommendUserListInfo(userIdValues);
    return result.map((user) => ({
      ...user,
      isFollow: false,
    }));
  }
  async getRecommendFood(tokenInfo: TokenInfo) {
    const region = await this.usersRepository.findOne({ select: ["region"], where: { id: tokenInfo.id } });
    const restaurants = await this.userRestaurantListRepository.getMyFavoriteFoodCategory(tokenInfo.id, region);

    for (const restaurant of restaurants) {
      const reviewInfo = await this.reviewRepository
        .createQueryBuilder("review")
        .leftJoin("review.reviewLikes", "reviewLike")
        .select(["review.id", "review.reviewImage"],)
        .groupBy("review.id")
        .where("review.restaurant_id = :restaurantId and review.reviewImage is NOT NULL", { restaurantId: restaurant.restaurant_id })
        .orderBy("COUNT(CASE WHEN reviewLike.isLike = true THEN 1 ELSE NULL END)", "DESC")
        .getRawOne();
      if (reviewInfo) {
        restaurant.restaurant_reviewImage = this.awsService.getImageURL(reviewInfo.review_reviewImage);
      }
      else {
        restaurant.restaurant_reviewImage = this.awsService.getImageURL("review/images/defaultImage.png");
      }
    }
    return restaurants;
  }
  async searchTargetUser(tokenInfo: TokenInfo, nickName: string, region: string[]) {
    const whereCondition: any = {
      nickName: Like(`%${nickName}%`),
      id: Not(Equal(tokenInfo.id)),
    };
    if (region) {
      whereCondition.region = In(region);
    }
    const users = await this.usersRepository.find({
      select: ["id"],
      where: whereCondition,
      take: 20,
    });
    if (users.length) {
      const userIds = users.map((user) => user.id);
      const result = await this.usersRepository.getUsersInfo(userIds);
      for (let i in result) {
        result[i]["isFollow"] =
          (await this.userFollowListRepositoy.getFollowState(
            tokenInfo.id,
            userIds[i]
          ))
            ? true
            : false;
      }
      return result;
    }
    return [];
  }

  async followUser(tokenInfo: TokenInfo, nickName: string) {
    const targetId = await this.usersRepository.findOne({
      select: ["id"],
      where: { nickName: nickName },
    });
    try {
      await this.userFollowListRepositoy.followUser(
        tokenInfo.id,
        targetId["id"]
      );
      return null;
    } catch (err) {
      throw new BadRequestException();
    }
  }
  async unfollowUser(tokenInfo: TokenInfo, nickName: string) {
    const targetId = await this.usersRepository.findOne({
      select: ["id"],
      where: { nickName: nickName },
    });
    try {
      await this.userFollowListRepositoy.unfollowUser(
        tokenInfo.id,
        targetId["id"]
      );
      return null;
    } catch (err) {
      throw new BadRequestException();
    }
  }

  async addRestaurantToNebob(
    reviewInfoDto: ReviewInfoDto,
    tokenInfo: TokenInfo,
    restaurantId: number,
    file: Express.Multer.File
  ) {
    const reviewEntity = this.reviewRepository.create(reviewInfoDto);
    let reviewImage;
    if (file) {
      const uuid = v4();
      reviewImage = `review/images/${uuid}.png`;
      reviewEntity.reviewImage = reviewImage;
    }
    const userEntity = new User();
    userEntity.id = tokenInfo["id"];
    reviewEntity.user = userEntity;

    const restaurantEntity = new RestaurantInfoEntity();
    restaurantEntity.id = restaurantId;
    reviewEntity.restaurant = restaurantEntity;
    try {
      await this.reviewRepository.save(reviewEntity);
      await this.userRestaurantListRepository.addRestaurantToNebob(
        tokenInfo.id,
        restaurantId,
        reviewEntity
      );
      if (file) await this.awsService.uploadToS3(reviewImage, file.buffer);
    } catch (err) {
      throw new BadRequestException();
    }
    return null;
  }

  async deleteRestaurantFromNebob(tokenInfo: TokenInfo, restaurantId: number) {
    await this.userRestaurantListRepository.deleteRestaurantFromNebob(
      tokenInfo.id,
      restaurantId
    );
    return null;
  }

  async addRestaurantToWishNebob(tokenInfo: TokenInfo, restaurantId: number) {
    try {
      await this.userWishRestaurantListRepository.addRestaurantToWishNebob(
        tokenInfo.id,
        restaurantId
      );
    } catch (err) {
      throw new BadRequestException();
    }
    return null;
  }

  async deleteRestaurantFromWishNebob(
    tokenInfo: TokenInfo,
    restaurantId: number
  ) {
    await this.userWishRestaurantListRepository.deleteRestaurantFromWishNebob(
      tokenInfo.id,
      restaurantId
    );
    return null;
  }

  async logout(tokenInfo: TokenInfo) {
    return await this.authService.logout(tokenInfo.id);
  }

  async deleteUserAccount(tokenInfo: TokenInfo) {
    return await this.usersRepository.deleteUserAccount(tokenInfo.id);
  }
  async updateMypageUserInfo(file: Express.Multer.File, tokenInfo: TokenInfo, userInfoDto: UserInfoDto) {
    userInfoDto.password = await hashPassword(userInfoDto.password);
    let profileImage;
    if (file) {
      const uuid = v4();
      profileImage = `profile/images/${uuid}.png`;
    } else {
      profileImage = "profile/images/defaultprofile.png";
    }
    const user = {
      ...userInfoDto,
      profileImage: profileImage
    };
    const newUser = this.usersRepository.create(user);
    const updatedUser = await this.usersRepository.updateMypageUserInfo(tokenInfo.id, newUser);
    if (file) {
      this.awsService.uploadToS3(profileImage, file.buffer);
    }
    return updatedUser;
  }
}
