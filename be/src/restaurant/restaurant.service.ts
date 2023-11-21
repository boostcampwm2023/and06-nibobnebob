import { Injectable } from "@nestjs/common";
import { RestaurantRepository } from "./restaurant.repository";
import { SearchInfoDto } from "./dto/seachInfo.dto";

@Injectable()
export class RestaurantService {
  constructor(private restaurantRepository: RestaurantRepository) {}

  async searchRestaurant(searchInfoDto: SearchInfoDto) {
    console.log(searchInfoDto);
    return this.restaurantRepository.searchRestarant(searchInfoDto);
  }
}
