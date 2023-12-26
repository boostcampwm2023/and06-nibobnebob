import { Injectable, OnModuleInit } from "@nestjs/common";
import { Client } from "@elastic/elasticsearch";
import { RestaurantRepository } from "./restaurant.repository";
import { SearchInfoDto } from "./dto/seachInfo.dto";

@Injectable()
export class ElasticsearchService implements OnModuleInit {
  private client: Client;

  constructor(private restaurantRepository: RestaurantRepository) {
    this.client = new Client({ node: "http://localhost:9200" });
  }

  async onModuleInit() {
    const indexExists = await this.client.indices.exists({
      index: "restaurants",
    });
    // await this.client.indices.delete({ index: 'restaurants' });
    await this.createRestaurantIndex();
    await this.indexRestaurantData();
  }

  async search(query: any) {
    return this.client.search(query);
  }

  async createRestaurantIndex() {
    return this.client.indices.create({
      index: "restaurants",
      body: {
        settings: {
          analysis: {
            analyzer: {
              ngram_analyzer: {
                type: "custom",
                tokenizer: "ngram",
                filter: ["lowercase", "edge_ngram"]
              }
            },
            filter: {
              edge_ngram: {
                type: "edge_ngram",
                min_gram: 1,
                max_gram: 20
              }
            }
          }
        },
        mappings: {
          properties: {
            restaurant_name: {
              type: "text",
              analyzer: "ngram_analyzer",
              search_analyzer: "standard"
            },
            location: {type: "geo_point"},
          },
        },
      },
    });
  }

  async indexRestaurantData() {
    const restaurants = await this.restaurantRepository.find();
    const batchSize = 500;
    const totalBatches = Math.ceil(restaurants.length / batchSize);
    for (let i = 0; i < totalBatches; i++) {
      console.log(i * batchSize);
      const currentBatch = restaurants.slice(
        i * batchSize,
        (i + 1) * batchSize
      );
      const bulkBody = currentBatch.flatMap((restaurant) => [
        { index: { _index: "restaurants", _id: restaurant.id } },
        {
          restaurant_id: restaurant.id,
          restaurant_name: restaurant.name,
          location: {
            lat: restaurant.location.coordinates[1],
            lon: restaurant.location.coordinates[0]
          },
          restaurant_phoneNumber: restaurant.phoneNumber,
          restaurant_address: restaurant.address,
          restaurant_category: restaurant.category,
        },
      ]);
      await this.client.bulk({ body: bulkBody });
    }
  }

  async getSuggestions(searchInfoDto: SearchInfoDto) {
    const response = await this.client.search({
      index: 'restaurants',
      body: {
        query: {
          match: {
            restaurant_name: {
              query: searchInfoDto.partialName
            }
          }
        },
        _source: ["restaurant_name", "restaurant_address", "restaurant_phoneNumber", "restaurant_category"],
        size:15,
        sort: [
          {
            _geo_distance: {
              location: { lat: searchInfoDto.latitude, lon: searchInfoDto.longitude }, // 사용자 위치
              order: 'asc', // 가까운 순으로 정렬
              unit: 'km' // 거리 단위
            }
          }
        ],
        script_fields: {
          distance: {
            script: {
              source: "doc['location'].arcDistance(params.lat,params.lon)", // 거리 계산 스크립트
              params: {
                lat: searchInfoDto.latitude,
                lon: searchInfoDto.longitude
              }
            }
          }
        },
      }
    });

    console.log(JSON.stringify(response,null,2))
    // console.log(JSON.stringify(response.suggest.restaurantSuggestions[0].options,null,2));
    // const options = response.suggest.restaurantSuggestions[0].options;
    // const result = Array.isArray(options)
    //   ? options.map((item) => item["_source"])
    //   : [];
    // return result;
  }
}
