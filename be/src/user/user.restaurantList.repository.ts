import { DataSource, IsNull, Repository, Not } from "typeorm";
import { ConflictException, Injectable } from "@nestjs/common";
import { UserRestaurantListEntity } from "./entities/user.restaurantlist.entity";
import { TokenInfo } from "./user.decorator";
import { SearchInfoDto } from "src/restaurant/dto/seachInfo.dto";
import { ReviewInfoEntity } from "src/review/entities/review.entity";
import { UserWishRestaurantListEntity } from "./entities/user.wishrestaurantlist.entity";

@Injectable()
export class UserRestaurantListRepository extends Repository<UserRestaurantListEntity> {
  constructor(private dataSource: DataSource) {
    super(UserRestaurantListEntity, dataSource.createEntityManager());
  }
  async addRestaurantToNebob(
    id: TokenInfo["id"],
    restaurantId: number,
    reviewEntity: ReviewInfoEntity
  ) {
    const userRestaurantList = new UserRestaurantListEntity();
    userRestaurantList.userId = id;
    userRestaurantList.restaurantId = restaurantId;
    userRestaurantList.review = reviewEntity;
    userRestaurantList.deletedAt = null;
    userRestaurantList.createdAt = new Date();
    await this.upsert(userRestaurantList, ["userId", "restaurantId"]);
    return null;
  }
  async deleteRestaurantFromNebob(id: TokenInfo["id"], restaurantId: number) {
    await this.update(
      { userId: id, restaurantId: restaurantId },
      { deletedAt: new Date() }
    );
    return null;
  }
  async getMyRestaurantListInfo(
    searchInfoDto: SearchInfoDto,
    id: TokenInfo["id"]
  ) {
    if (searchInfoDto.latitude && searchInfoDto.longitude) {
      return await this.createQueryBuilder("user_restaurant_lists")
        .leftJoinAndSelect("user_restaurant_lists.restaurant", "restaurant")
        .leftJoin(
          UserWishRestaurantListEntity,
          "user_wish_list",
          "user_wish_list.restaurantId = restaurant.id AND user_wish_list.userId = :userId",
          { userId: id }
        )
        .select([
          "user_restaurant_lists.restaurantId AS restaurant_id",
          "restaurant.name",
          "restaurant.location",
          "restaurant.address",
          "restaurant.category",
          "restaurant.phoneNumber",
          "restaurant.reviewCnt",
          `CASE WHEN user_wish_list.userId IS NOT NULL THEN TRUE ELSE FALSE END AS "isWish"`,
        ])
        .where(
          `user_restaurant_lists.user_id = :userId and ST_DistanceSphere(
                location, 
                ST_GeomFromText('POINT(${searchInfoDto.longitude} ${searchInfoDto.latitude})', 4326)
            )<  ${searchInfoDto.radius} and user_restaurant_lists.deleted_at IS NULL`,
          { userId: id }
        )
        .getRawMany();
    } else {
      return await this.createQueryBuilder("user_restaurant_lists")
        .leftJoinAndSelect("user_restaurant_lists.restaurant", "restaurant")
        .leftJoin(
          UserWishRestaurantListEntity,
          "user_wish_list",
          "user_wish_list.restaurantId = restaurant.id AND user_wish_list.userId = :userId",
          { userId: id }
        )
        .select([
          "user_restaurant_lists.restaurantId AS restaurant_id",
          "restaurant.name",
          "restaurant.location",
          "restaurant.address",
          "restaurant.category",
          "restaurant.phoneNumber",
          "restaurant.reviewCnt",
          `CASE WHEN user_wish_list.userId IS NOT NULL THEN TRUE ELSE FALSE END AS "isWish"`,
        ])
        .where(
          "user_restaurant_lists.user_id = :userId  and user_restaurant_lists.deleted_at IS NULL",
          { userId: id }
        )
        .getRawMany();
    }
  }
}
