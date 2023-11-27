import { DataSource, Repository, Like } from "typeorm";
import { Injectable } from "@nestjs/common";
import { RestaurantInfoEntity } from "./entities/restaurant.entity";
import { SearchInfoDto } from "./dto/seachInfo.dto";
import { TokenInfo } from "src/user/user.decorator";
import { UserRestaurantListEntity } from "src/user/entities/user.restaurantlist.entity";

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
          userId: tokenInfo.id, // 현재 사용자 ID
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
        .select([
          "restaurant.id",
          "restaurant.name",
          "restaurant.location",
          "restaurant.address",
          "restaurant.phoneNumber",
          "restaurant.reviewCnt",
          "restaurant.category",
          `CASE WHEN user_restaurant_list.userId IS NOT NULL THEN TRUE ELSE FALSE END AS "isMy"`,
        ])
        .where("restaurant.name LIKE :partialName")
        .setParameters({
          partialName: `%${searchInfoDto.partialName}%`,
          userId: tokenInfo.id, // 현재 사용자 ID
        })
        .limit(15)
        .getRawMany();
    }
  }

  async detailInfo(restaurantId: number) {
    return this.findOne({
      select: [
        "id",
        "name",
        "location",
        "address",
        "phoneNumber",
        "reviewCnt",
        "category",
      ],
      where: { id: restaurantId },
    });
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
