import { DataSource, Repository, Like } from "typeorm";
import { Injectable } from "@nestjs/common";
import { RestaurantInfoEntity } from "./entities/restaurant.entity";
import { SearchInfoDto } from "./dto/seachInfo.dto";

@Injectable()
export class RestaurantRepository extends Repository<RestaurantInfoEntity> {
  constructor(private dataSource: DataSource) {
    super(RestaurantInfoEntity, dataSource.createEntityManager());
  }

  async searchRestarant(searchInfoDto: SearchInfoDto) {
    const rawQuery = `
    SELECT id, name, location, address, "phoneNumber", "reviewCnt", category, distance FROM (
      SELECT *, 
      ST_DistanceSphere(
          location, 
          ST_GeomFromText('POINT(${searchInfoDto.longitude} ${searchInfoDto.latitude})', 4326)
      ) AS distance
      FROM restaurant
  ) AS subquery
  WHERE name LIKE '%${searchInfoDto.partialName}%' AND distance < ${searchInfoDto.radius}
  ORDER BY location <-> ST_GeomFromText('POINT(${searchInfoDto.longitude} ${searchInfoDto.latitude})', 4326)
  
    `
    return this.query(rawQuery)
  }

  async detailInfo(restaurantId: number){
    return this.findOne({
      select: ['id', 'name', 'location', 'address', 'phoneNumber', 'reviewCnt', 'category'],
      where: {id: restaurantId}
    })
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
