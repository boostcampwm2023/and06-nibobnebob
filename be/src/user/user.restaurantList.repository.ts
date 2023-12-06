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
          "user_restaurant_lists.created_at",
          `CASE WHEN user_wish_list.userId IS NOT NULL THEN TRUE ELSE FALSE END AS "isWish"`,
        ])
        .where(
          "user_restaurant_lists.user_id = :userId  and user_restaurant_lists.deleted_at IS NULL",
          { userId: id }
        )
        .getRawMany();
    }
  }
  async getMyFavoriteFoodCategory(id: TokenInfo['id'], region) {
    const categoryCounts = await this.createQueryBuilder("userRestaurantList")
      .select("restaurant.category", "category")
      .addSelect("COUNT(restaurant.category)", "count")
      .innerJoin("userRestaurantList.restaurant", "restaurant")
      .where("userRestaurantList.userId = :id", { id })
      .groupBy("restaurant.category")
      .getRawMany();

    if (categoryCounts.length) {
      const favoriteCategory = categoryCounts.reduce((a, b) => a.count > b.count ? a : b).category;

      const subQuery = await this.createQueryBuilder()
        .select("DISTINCT(userRestaurantListSub.restaurantId)", "restaurantId")
        .from(UserRestaurantListEntity, "userRestaurantListSub")
        .where("userRestaurantListSub.userId = :id", { id })
        .getRawMany();

      const restaurantIds = subQuery.map(item => item.restaurantId);

      const result = await this
        .createQueryBuilder("userRestaurantList")
        .leftJoinAndSelect("userRestaurantList.restaurant", "restaurant")
        .select(["restaurant.id", "restaurant.name", "restaurant.category"])
        .where("restaurant.category = :category", { category: favoriteCategory })
        .andWhere("restaurant.address LIKE :region", { region: `%${region.region}%` })
        .andWhere("userRestaurantList.restaurantId NOT IN (:...restaurantIds)", { restaurantIds: restaurantIds })
        .getRawMany();

      if (result.length > 0) {
        const recommendedRestaurant = result[Math.floor(Math.random() * result.length)];
        return recommendedRestaurant;
      }
    }
    else {
      const result = await this
        .createQueryBuilder("userRestaurantList")
        .leftJoinAndSelect("userRestaurantList.restaurant", "restaurant")
        .select(["restaurant.id", "restaurant.name", "restaurant.category"])
        .andWhere("restaurant.address LIKE :region", { region: `%${region.region}%` })
        .getRawMany();

      if (result.length > 0) {
        let recommendedRestaurants = [];
        let usedIndexes = new Set();

        for (let i = 0; i < Math.min(3, result.length); i++) {
          let randomIndex;
          do {
            randomIndex = Math.floor(Math.random() * result.length);
          } while (usedIndexes.has(randomIndex));

          usedIndexes.add(randomIndex);
          recommendedRestaurants.push(result[randomIndex]);
        }
        return recommendedRestaurants;
      }
    }
  }
}
