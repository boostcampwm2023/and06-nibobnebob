import { Injectable, OnModuleInit } from "@nestjs/common";
import { RestaurantRepository } from "./restaurant.repository";
import { SearchInfoDto } from "./dto/seachInfo.dto";
import * as proj4 from 'proj4';
import axios from 'axios';

const key = 'api키 입력하세요';

@Injectable()
export class RestaurantService implements OnModuleInit {
  onModuleInit() {
    this.updateRestaurantsFromSeoulData()
    setInterval(() => {
      this.updateRestaurantsFromSeoulData();
    }, 1000 * 60 * 60 * 24 * 3);
  }

  constructor(private restaurantRepository: RestaurantRepository) { }

  async searchRestaurant(searchInfoDto: SearchInfoDto) {
    return this.restaurantRepository.searchRestarant(searchInfoDto);
  }

  async getRestaurantsListFromSeoulData(startPage) {
    const tm2097 = "+proj=tmerc +lat_0=38 +lon_0=127.0028902777778 +k=1 +x_0=200000 +y_0=500000 +ellps=bessel +units=m +no_defs +towgs84=-115.80,474.99,674.11,1.16,-2.31,-1.63,6.43";
    const wgs84 = "EPSG:4326";

    const apiUrl = `http://openapi.seoul.go.kr:8088/${key}/json/LOCALDATA_072404/${startPage}/${startPage + 999}/`;

    const response = axios.get(apiUrl);

    return response.then(response => {
      const result = { data: [], lastPage: false };
      if (response.data.RESULT && response.data.RESULT.CODE === "INFO-200") {
        result.lastPage = true;
      } else {
        response.data.LOCALDATA_072404.row.forEach(element => {
          const tmX = parseFloat(element.X);
          const tmY = parseFloat(element.Y);
          if (!isNaN(tmX) && !isNaN(tmY) && element.DTLSTATENM === "영업") {
            const [lon, lat] = proj4(tm2097, wgs84, [tmX, tmY]);
            result.data.push({
              name: element.BPLCNM,
              location: { type: 'Point', coordinates: [lon, lat] },
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
    const allData = [];
    let lastPageReached = false;
    while (!lastPageReached) {
      const promise = this.getRestaurantsListFromSeoulData(pageElementNum);
      promises.push(promise);
      pageElementNum += 1000;

      if (promises.length >= 15) {
        const data = [];
        const results = await Promise.all(promises);
        results.forEach(result => {
          if (result.lastPage) lastPageReached = true;
          data.push(...result.data);
        });
        await this.restaurantRepository.updateRestaurantsFromSeoulData(data);
        promises.length = 0;
      }
    }
    const finalResults = await Promise.all(promises);
    finalResults.forEach(result => allData.push(...result.data));
    await this.restaurantRepository.updateRestaurantsFromSeoulData(allData);
  }
}



