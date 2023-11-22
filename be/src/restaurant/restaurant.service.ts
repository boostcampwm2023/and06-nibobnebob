import { Injectable, OnModuleInit } from "@nestjs/common";
import { RestaurantRepository } from "./restaurant.repository";
import { SearchInfoDto } from "./dto/seachInfo.dto";
const { Worker } = require('worker_threads');


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
    console.log(searchInfoDto);
    return this.restaurantRepository.searchRestarant(searchInfoDto);
  }

  async updateRestaurantsFromSeoulData() {
    const numThreads = 6;
    let pageElementNum = 1;
    let lastPageReached = false;

    while (!lastPageReached) {
      const workers = [];
      for (let i = 0; i < numThreads; i++) {
        const workerData = { pageElementNum: pageElementNum };
        pageElementNum += 1000;
        const worker = new Worker('./src/restaurant/worker/restaurant.worker.js', { workerData });
        workers.push(
          new Promise((resolve, reject) => {
            worker.on('message', resolve);
            worker.on('error', reject);
          })
        );
      }

      const results = await Promise.all(workers);
      const allData = results.flatMap(result => result.data);
      if (allData.length > 0) {
        await this.restaurantRepository.updateRestaurantsFromSeoulData(allData);
      }

      lastPageReached = results.some(result => result.lastPage);
    }
  }
}



