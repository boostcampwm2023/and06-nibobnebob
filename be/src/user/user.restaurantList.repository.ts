import { DataSource, IsNull, Repository, Not } from "typeorm";
import { ConflictException, Injectable } from "@nestjs/common";
import { UserRestaurantListEntity } from "./entities/user.restaurantlist.entity";
import { TokenInfo } from "./user.decorator";
import { SearchInfoDto } from "src/restaurant/dto/seachInfo.dto";
import { ReviewInfoEntity } from "src/review/entities/review.entity";
import { UserWishRestaurantListEntity } from "./entities/user.wishrestaurantlist.entity";
import { SortInfoDto } from "src/utils/sortInfo.dto";

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

  async getTargetRestaurantListInfo(targetId: number, id: TokenInfo["id"]) {
    const ids = await this.find({
      select: ["restaurantId"],
      where: { userId: targetId },
      order: { createdAt: "DESC" },
      take: 3,
    });

    if (ids.length) {
      const restaurantIds = ids.map((entity) => entity.restaurantId);
      return await this.createQueryBuilder("user_restaurant_lists")
        .leftJoinAndSelect("user_restaurant_lists.restaurant", "restaurant")
        .leftJoin(
          "user_restaurant_lists",
          "user_restaurant_lists_other",
          "user_restaurant_lists.restaurantId = user_restaurant_lists_other.restaurantId AND user_restaurant_lists_other.userId = :currentUserId"
        )
        .select([
          "user_restaurant_lists.restaurantId as restaurant_id",
          "restaurant.name",
          "restaurant.location",
          "restaurant.address",
          "restaurant.category",
          "restaurant.phoneNumber",
          `CASE WHEN user_restaurant_lists_other.userId IS NOT NULL THEN TRUE ELSE FALSE END AS "isMy"`,
        ])
        .where(
          "user_restaurant_lists.restaurantId IN (:...restaurantIds) and user_restaurant_lists.userId = :targetId",
          {
            restaurantIds: restaurantIds,
            targetId: targetId,
            currentUserId: id,
          }
        )
        .getRawMany();
    }
    return;
  }

  async getMyRestaurantListInfo(
    searchInfoDto: SearchInfoDto,
    sortInfoDto: SortInfoDto,
    id: TokenInfo["id"]
  ) {
    if (searchInfoDto.latitude && searchInfoDto.longitude) {
      const items = await this.createQueryBuilder("user_restaurant_lists")
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

      return {
        items
      }
    } else {
      let query = this.createQueryBuilder("user_restaurant_lists")
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
        "user_restaurant_lists.created_at",
        `CASE WHEN user_wish_list.userId IS NOT NULL THEN TRUE ELSE FALSE END AS "isWish"`,
      ])
      .where(
        "user_restaurant_lists.user_id = :userId and user_restaurant_lists.deleted_at IS NULL",
        { userId: id }
      );
    
    if (sortInfoDto.sort === '등록순') {
      query = query.orderBy("user_restaurant_lists.created_at", "ASC");
    } else {
      query = query.orderBy("user_restaurant_lists.created_at", "DESC");
    }
    sortInfoDto.page = sortInfoDto.page || 1;
    sortInfoDto.limit = sortInfoDto.limit || 10;

    const offset = (sortInfoDto.page - 1) * sortInfoDto.limit;
    query = query.skip(offset).take(sortInfoDto.limit + 1);
  
    const items = await query.getRawMany();
  
    const hasNext = items.length > sortInfoDto.limit;
    const resultItems = hasNext ? items.slice(0, -1) : items;
  
    return {
      hasNext,
      items : resultItems,
    }
  
    }
  }
}
