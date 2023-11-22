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
    return this.find({
      select: ["id", "name", "location"],
      where: {
        name: Like(`%${searchInfoDto.partitalName}%`),
      },
    });
  }

  async updateRestaurantsFromSeoulData(data: RestaurantInfoEntity[]) {
    const uniqueData = Array.from(new Map(data.map(item => [item['name'] + JSON.stringify(item['location']), item])).values());

    await this.upsert(uniqueData, ['name', 'location']);
    return;
  }

}
