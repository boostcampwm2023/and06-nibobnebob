import { Injectable } from "@nestjs/common";
import { RestaurantRepository } from "./restaurant.repository";
import { SearchInfoDto } from "./dto/seachInfo.dto";
import axios from "axios";
import * as proj4 from 'proj4';

@Injectable()
export class RestaurantService {
  constructor(private restaurantRepository: RestaurantRepository) { }

  async searchRestaurant(searchInfoDto: SearchInfoDto) {
    return this.restaurantRepository.searchRestarant(searchInfoDto);
  }

  async updateRestaurantsFromKakao() {
    const data = [];

    const tm2097 = "+proj=tmerc +lat_0=38 +lon_0=127.0028902777778 +k=1 +x_0=200000 +y_0=500000 +ellps=bessel +units=m +no_defs +towgs84=-115.80,474.99,674.11,1.16,-2.31,-1.63,6.43";
    const wgs84 = "EPSG:4326";

    const response = await axios.get(`http://openapi.seoul.go.kr:8088/인증키/json/LOCALDATA_072404/1/1000/`, {
    });
    response.data.LOCALDATA_072404.row.forEach(element => {
      const tmX = parseFloat(element.X);
      const tmY = parseFloat(element.Y);


      if (!isNaN(tmX) && !isNaN(tmY)) {
        const [lon, lat] = proj4(tm2097, wgs84, [tmX, tmY]);
        if (element.DTLSTATENM === "영업")
          data.push({
            name: element.BPLCNM,
            location: { type: 'Point', coordinates: [lon, lat] },
            address: element.SITEWHLADDR,
            category: element.UPTAENM,
            phoneNumber: element.SITETEL,
          });
      } else {
        console.error(`유효하지 않은 좌표: x=${element.x}, y=${element.y}`);
      }

    });

    return await this.restaurantRepository.updateRestaurantsFromKakao(data);

  }
}




