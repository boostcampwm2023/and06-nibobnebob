import { DataSource, Repository, Like } from "typeorm";
import { Injectable } from "@nestjs/common";
import { RestaurantInfoEntity } from "./entities/restaurant.entity";
import { SearchInfoDto } from "./dto/seachInfo.dto";
import { TokenInfo } from "../user/user.decorator";
import { UserRestaurantListEntity } from "../user/entities/user.restaurantlist.entity";
import { FilterInfoDto } from "./dto/filterInfo.dto";
import { User } from "../user/entities/user.entity";
import { LocationDto } from "./dto/location.dto";
import { UserWishRestaurantListEntity } from "../user/entities/user.wishrestaurantlist.entity";

@Injectable()
export class RestaurantRepository extends Repository<RestaurantInfoEntity> {
  constructor(private dataSource: DataSource) {
    super(RestaurantInfoEntity, dataSource.createEntityManager());
  }

  async searchRestarant(searchInfoDto: SearchInfoDto, tokenInfo: TokenInfo) {
    if (searchInfoDto.latitude && searchInfoDto.longitude) {
      return this.createQueryBuilder("restaurant")
        .leftJoin(
          UserRestaurantListEntity,
          "user_restaurant_list",
          "user_restaurant_list.restaurantId = restaurant.id AND user_restaurant_list.userId = :userId",
          { userId: tokenInfo.id }
        )
        .leftJoin(
          UserWishRestaurantListEntity,
          "user_wish_list",
          "user_wish_list.restaurantId = restaurant.id AND user_wish_list.userId = :userId",
          { userId: tokenInfo.id }
        )
        .select([
          "restaurant.id",
          "restaurant.name",
          "restaurant.location",
          "restaurant.address",
          "restaurant.phoneNumber",
          "restaurant.reviewCnt",
          "restaurant.category",
          `ST_DistanceSphere(
        restaurant.location, 
        ST_GeomFromText(:point, 4326)
      ) AS distance`,
          `CASE WHEN user_restaurant_list.userId IS NOT NULL THEN TRUE ELSE FALSE END AS "isMy"`,
          `CASE WHEN user_wish_list.userId IS NOT NULL THEN TRUE ELSE FALSE END AS "isWish"`,
        ])
        .where("restaurant.name LIKE :partialName")
        .andWhere(
          `ST_DistanceSphere(
        restaurant.location, 
        ST_GeomFromText(:point, 4326)
      ) < :radius`
        )
        .orderBy(
          `ST_DistanceSphere(
        restaurant.location, 
        ST_GeomFromText(:point, 4326)
      )`
        )
        .setParameters({
          point: `POINT(${searchInfoDto.longitude} ${searchInfoDto.latitude})`,
          partialName: `%${searchInfoDto.partialName}%`,
          radius: searchInfoDto.radius,
          userId: tokenInfo.id,
        })
        .limit(15)
        .getRawMany();
    } else {
      return this.createQueryBuilder("restaurant")
        .leftJoin(
          UserRestaurantListEntity,
          "user_restaurant_list",
          "user_restaurant_list.restaurantId = restaurant.id AND user_restaurant_list.userId = :userId",
          { userId: tokenInfo.id }
        )
        .leftJoin(
          UserWishRestaurantListEntity,
          "user_wish_list",
          "user_wish_list.restaurantId = restaurant.id AND user_wish_list.userId = :userId",
          { userId: tokenInfo.id }
        )
        .select([
          "restaurant.id",
          "restaurant.name",
          "restaurant.location",
          "restaurant.address",
          "restaurant.phoneNumber",
          "restaurant.reviewCnt",
          "restaurant.category",
          `CASE WHEN user_restaurant_list.userId IS NOT NULL THEN TRUE ELSE FALSE END AS "isMy"`,
          `CASE WHEN user_wish_list.userId IS NOT NULL THEN TRUE ELSE FALSE END AS "isWish"`,
        ])
        .where("restaurant.name LIKE :partialName")
        .setParameters({
          partialName: `%${searchInfoDto.partialName}%`,
          userId: tokenInfo.id,
        })
        .limit(15)
        .getRawMany();
    }
  }

  async filteredRestaurantList(
    filterInfoDto: FilterInfoDto,
    tokenInfo: TokenInfo,
    target: User
  ) {
    if (filterInfoDto.longitude && filterInfoDto.latitude) {
      return this.createQueryBuilder("restaurant")
        .innerJoin(
          UserRestaurantListEntity,
          "user_restaurant_list",
          "user_restaurant_list.restaurantId = restaurant.id AND user_restaurant_list.userId = :targetId",
          { targetId: target.id }
        )
        .leftJoin(
          UserRestaurantListEntity,
          "current_url",
          "current_url.restaurantId = restaurant.id AND current_url.userId = :currentUserId",
          { currentUserId: tokenInfo.id }
        )
        .leftJoin(
          UserWishRestaurantListEntity,
          "user_wish_list",
          "user_wish_list.restaurantId = restaurant.id AND user_wish_list.userId = :userId",
          { userId: tokenInfo.id }
        )
        .select([
          "user_restaurant_list.restaurantId AS restaurant_id",
          "restaurant.name",
          "restaurant.location",
          "restaurant.address",
          "restaurant.category",
          "restaurant.phoneNumber",
          'CASE WHEN current_url.user_id IS NOT NULL THEN true ELSE false END AS "isMy"',
          `CASE WHEN user_wish_list.userId IS NOT NULL THEN TRUE ELSE FALSE END AS "isWish"`,
          "restaurant.reviewCnt",
        ])
        .where(
          `ST_DistanceSphere(
          location, 
          ST_GeomFromText('POINT(${filterInfoDto.longitude} ${filterInfoDto.latitude})', 4326)) < ${filterInfoDto.radius}`
        )
        .getRawMany();
    } else {
      return this.createQueryBuilder("restaurant")
        .innerJoin(
          UserRestaurantListEntity,
          "user_restaurant_list",
          "user_restaurant_list.restaurantId = restaurant.id AND user_restaurant_list.userId = :targetId",
          { targetId: target.id }
        )
        .leftJoin(
          UserRestaurantListEntity,
          "current_url",
          "current_url.restaurantId = restaurant.id AND current_url.userId = :currentUserId",
          { currentUserId: tokenInfo.id }
        )
        .leftJoin(
          UserWishRestaurantListEntity,
          "user_wish_list",
          "user_wish_list.restaurantId = restaurant.id AND user_wish_list.userId = :userId",
          { userId: tokenInfo.id }
        )
        .select([
          "restaurant.id AS restaurant_id",
          "restaurant.name",
          "restaurant.location",
          "restaurant.address",
          "restaurant.category",
          "restaurant.phoneNumber",
          'CASE WHEN current_url.userId IS NOT NULL THEN true ELSE false END AS "isMy"',
          `CASE WHEN user_wish_list.userId IS NOT NULL THEN TRUE ELSE FALSE END AS "isWish"`,
          "restaurant.reviewCnt",
        ])
        .getRawMany();
    }
  }

  async entireRestaurantList(locationDto: LocationDto, tokenInfo: TokenInfo, limit: string = "40") {
    const limitNum = parseInt(limit);

    return this.createQueryBuilder("restaurant")
      .leftJoin(
        UserRestaurantListEntity,
        "current_url",
        "current_url.restaurantId = restaurant.id AND current_url.userId = :currentUserId",
        { currentUserId: tokenInfo.id }
      )
      .leftJoin(
        UserWishRestaurantListEntity,
        "user_wish_list",
        "user_wish_list.restaurantId = restaurant.id AND user_wish_list.userId = :userId",
        { userId: tokenInfo.id }
      )
      .select([
        "restaurant.id",
        "restaurant.name",
        "restaurant.location",
        "restaurant.address",
        "restaurant.category",
        "restaurant.phoneNumber",
        'CASE WHEN current_url.user_id IS NOT NULL THEN true ELSE false END AS "isMy"',
        `CASE WHEN user_wish_list.userId IS NOT NULL THEN TRUE ELSE FALSE END AS "isWish"`,
        "restaurant.reviewCnt",
      ])
      .where(
        `ST_DistanceSphere(
        location, 
        ST_GeomFromText('POINT(${locationDto.longitude} ${locationDto.latitude})', 4326)) < ${locationDto.radius}`
      )
      .limit(limitNum)
      .getRawMany();
  }

  async detailInfo(restaurantId: number, tokenInfo: TokenInfo) {
    return this.createQueryBuilder("restaurant")
      .leftJoin(
        UserRestaurantListEntity,
        "user_restaurant_list",
        "user_restaurant_list.restaurantId = restaurant.id AND user_restaurant_list.userId = :userId",
        { userId: tokenInfo.id }
      )
      .leftJoin(
        UserWishRestaurantListEntity,
        "user_wish_list",
        "user_wish_list.restaurantId = restaurant.id AND user_wish_list.userId = :userId",
        { userId: tokenInfo.id }
      )
      .select([
        "restaurant.id",
        "restaurant.name",
        "restaurant.location",
        "restaurant.address",
        "restaurant.category",
        "restaurant.phoneNumber",
        'CASE WHEN user_restaurant_list.userId IS NOT NULL THEN TRUE ELSE FALSE END AS "isMy"',
        `CASE WHEN user_wish_list.userId IS NOT NULL THEN TRUE ELSE FALSE END AS "isWish"`,
        "restaurant.reviewCnt",
      ])
      .where("restaurant.id = :restaurantId", { restaurantId })
      .getRawOne();
  }

  async updateRestaurantsFromSeoulData(data: RestaurantInfoEntity[]) {
    const uniqueData = Array.from(
      new Map(
        data.map((item) => [
          item["name"] + JSON.stringify(item["location"]),
          item,
        ])
      ).values()
    );

    await this.upsert(uniqueData, ["name", "location"]);
    return;
  }
}
