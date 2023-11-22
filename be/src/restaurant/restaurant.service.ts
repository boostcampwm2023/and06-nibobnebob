import { Injectable } from "@nestjs/common";
import { RestaurantRepository } from "./restaurant.repository";
import { SearchInfoDto } from "./dto/seachInfo.dto";
const { Worker } = require('worker_threads');


@Injectable()
export class RestaurantService {
  constructor(private restaurantRepository: RestaurantRepository) { }

  async searchRestaurant(searchInfoDto: SearchInfoDto) {
    console.log(searchInfoDto);
    return this.restaurantRepository.searchRestarant(searchInfoDto);
  }

  async updateRestaurantsFromKakao() {
    const numThreads = 4;
    let pageElementNum = 1;
    let lastPageReached = false;

    while (!lastPageReached) {
      const workers = [];
      for (let i = 0; i < numThreads; i++) {
        pageElementNum += 1000;
        const workerData = { pageElementNum: pageElementNum };
        const worker = new Worker('./src/restaurant/worker/restaurant.worker.js', { workerData });
        workers.push(
          new Promise((resolve, reject) => {
            worker.on('message', resolve);
            worker.on('error', reject);
          })
        );
      }

      const results = await Promise.all(workers);
      const dbUpdates = results.map(result => {
        if (result.lastPage) {
          lastPageReached = true;
        }
        return this.restaurantRepository.updateRestaurantsFromKakao(result.data);
      });

      await Promise.all(dbUpdates);
    }
  }
}



