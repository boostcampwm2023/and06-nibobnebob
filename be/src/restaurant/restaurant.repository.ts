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

  async updateRestaurantsFromKakao(data: RestaurantInfoEntity[]) {
    await this.save(data);
    return {};
  }
}
