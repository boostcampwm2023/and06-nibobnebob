import { Injectable, OnModuleInit } from "@nestjs/common";
import { RestaurantRepository } from "./restaurant.repository";
import { SearchInfoDto } from "./dto/seachInfo.dto";
import * as proj4 from "proj4";
import axios from "axios";
import { FilterInfoDto } from "./dto/filterInfo.dto";
import { TokenInfo } from "src/user/user.decorator";
import { UserRestaurantListRepository } from "src/user/user.restaurantList.repository";
import { UserRestaurantListEntity } from "src/user/entities/user.restaurantlist.entity";
import { UserRepository } from "src/user/user.repository";

const key = "api키 입력하세요";

@Injectable()
export class RestaurantService implements OnModuleInit {
  onModuleInit() {
    //this.updateRestaurantsFromSeoulData();
    setInterval(
      () => {
        this.updateRestaurantsFromSeoulData();
      },
      1000 * 60 * 60 * 24 * 3
    );
  }

  constructor(
    private restaurantRepository: RestaurantRepository,
    private userRepository: UserRepository,
    private userRestaurantListRepository: UserRestaurantListRepository
  ) {}


  async searchRestaurant(searchInfoDto: SearchInfoDto) {
    return this.restaurantRepository.searchRestarant(searchInfoDto);
  }

  async detailInfo(restaurantId: number) {
    return this.restaurantRepository.detailInfo(restaurantId);
  }

  async filteredRestaurantList(
    filterInfoDto: FilterInfoDto,
    tokenInfo: TokenInfo
  ) {
    const target = await this.userRepository.findOne({
      select: ["id"],
      where: { nickName: filterInfoDto.filter },
    });

    const results = await this.userRestaurantListRepository
      .createQueryBuilder("user_restaurant_lists")
      .leftJoinAndSelect("user_restaurant_lists.restaurant", "restaurant")
      .leftJoin(
        "user_restaurant_lists",
        "current_url",
        "current_url.restaurantId = restaurant.id AND current_url.userId = :currentUserId",
        { currentUserId: tokenInfo.id }
      )
      .select([
        "user_restaurant_lists.restaurantId AS restaurant_id",
        "restaurant.name",
        "restaurant.location",
        "restaurant.address",
        "restaurant.category",
        "restaurant.phoneNumber",
        "CASE WHEN current_url.user_id IS NOT NULL THEN true ELSE false END AS isMy",
      ])
      .where(
        `ST_DistanceSphere(
          location, 
          ST_GeomFromText('POINT(${filterInfoDto.longitude} ${filterInfoDto.latitude})', 4326)) < ${filterInfoDto.radius} and user_restaurant_lists.user_id = :targetId`,
        { targetId: target.id }
      )
      .getRawMany();

    return results;
  }

  async getRestaurantsListFromSeoulData(startPage) {
    const tm2097 =
      "+proj=tmerc +lat_0=38 +lon_0=127.0028902777778 +k=1 +x_0=200000 +y_0=500000 +ellps=bessel +units=m +no_defs +towgs84=-115.80,474.99,674.11,1.16,-2.31,-1.63,6.43";
    const wgs84 = "EPSG:4326";

    const apiUrl = `http://openapi.seoul.go.kr:8088/${key}/json/LOCALDATA_072404/${startPage}/${startPage + 999
      }/`;

    const response = axios.get(apiUrl);

    return response.then((response) => {
      const result = { data: [], lastPage: false };
      if (response.data.RESULT && response.data.RESULT.CODE === "INFO-200") {
        result.lastPage = true;
      } else {
        response.data.LOCALDATA_072404.row.forEach((element) => {
          const tmX = parseFloat(element.X);
          const tmY = parseFloat(element.Y);
          if (!isNaN(tmX) && !isNaN(tmY) && element.DTLSTATENM === "영업") {
            const [lon, lat] = proj4(tm2097, wgs84, [tmX, tmY]);
            result.data.push({
              name: element.BPLCNM,
              location: { type: "Point", coordinates: [lon, lat] },
              address: element.SITEWHLADDR,
              category: element.UPTAENM,
              phoneNumber: element.SITETEL,
            });
          }
        });
      }
      return result;
    });
  }

  async updateRestaurantsFromSeoulData() {
    let pageElementNum = 1;
    const promises = [];
    let lastPageReached = false;
    while (!lastPageReached) {
      const promise = this.getRestaurantsListFromSeoulData(pageElementNum);
      promises.push(promise);
      pageElementNum += 1000;

      if (promises.length >= 15) {
        const data = [];
        const results = await Promise.all(promises);
        results.forEach((result) => {
          if (result.lastPage) lastPageReached = true;
          data.push(...result.data);
        });
        await this.restaurantRepository.updateRestaurantsFromSeoulData(data);
        promises.length = 0;
      }
    }
  }
}
