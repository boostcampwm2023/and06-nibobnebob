import { Injectable, OnModuleInit } from "@nestjs/common";
import { RestaurantRepository } from "./restaurant.repository";
import { SearchInfoDto } from "./dto/seachInfo.dto";
import * as proj4 from "proj4";
import axios from "axios";
import { FilterInfoDto } from "./dto/filterInfo.dto";
import { TokenInfo } from "../user/user.decorator";
import { UserRepository } from "../user/user.repository";
import { ReviewRepository } from "../review/review.repository";
import { LocationDto } from "./dto/location.dto";
import { AwsService } from "../aws/aws.service";
import { Cron } from "@nestjs/schedule";
import { ElasticsearchService } from "./elasticSearch.service";
import { UserRestaurantListEntity } from "src/user/entities/user.restaurantlist.entity";
import { UserWishRestaurantListEntity } from "src/user/entities/user.wishrestaurantlist.entity";

const key = process.env.API_KEY;

@Injectable()
export class RestaurantService {

  @Cron('0 0 2 * * *')
  handleCron() {
    this.updateRestaurantsFromSeoulData();
  }

  constructor(
    private restaurantRepository: RestaurantRepository,
    private userRepository: UserRepository,
    private reviewRepository: ReviewRepository,
    private awsService: AwsService,
    private elasticSearchService: ElasticsearchService
  ) { }

  async searchRestaurant(searchInfoDto: SearchInfoDto, tokenInfo: TokenInfo) {
    const restaurants = await this.elasticSearchService.getSuggestions(searchInfoDto);

    for (const restaurant of restaurants) {
      const info = await this.restaurantRepository
        .createQueryBuilder("restaurant")
        .leftJoin(
          UserRestaurantListEntity,
          "user_restaurant_list",
          "user_restaurant_list.restaurantId = restaurant.id AND user_restaurant_list.userId = :userId",
          { userId: tokenInfo.id }
        )
        .leftJoin(
          UserWishRestaurantListEntity,
          "user_wish_list",
          "user_wish_list.restaurantId = restaurant.id AND user_wish_list.userId = :userId",
          { userId: tokenInfo.id }
        )
        .select([
          `CASE WHEN user_restaurant_list.userId IS NOT NULL THEN TRUE ELSE FALSE END AS "isMy"`,
          `CASE WHEN user_wish_list.userId IS NOT NULL THEN TRUE ELSE FALSE END AS "isWish"`,
        ])
        .where(`restaurant.id = ${restaurant.restaurant_id}`)
        .getRawOne();

      const reviewCount = await this.reviewRepository
        .createQueryBuilder("review")
        .where("review.restaurant_id = :restaurantId", {
          restaurantId: restaurant['restaurant_id'],
        })
        .getCount();

      const reviewInfo = await this.reviewRepository
        .createQueryBuilder("review")
        .leftJoin("review.reviewLikes", "reviewLike")
        .select(["review.id", "review.reviewImage"],)
        .groupBy("review.id")
        .where("review.restaurant_id = :restaurantId and review.reviewImage is NOT NULL", { restaurantId: restaurant['restaurant_id'] })
        .orderBy("COUNT(CASE WHEN reviewLike.isLike = true THEN 1 ELSE NULL END)", "DESC")
        .getRawOne();
      if (reviewInfo) {
        restaurant['restaurant_reviewImage'] = this.awsService.getImageURL(reviewInfo.review_reviewImage);
      }
      else {
        restaurant['restaurant_reviewImage'] = this.awsService.getImageURL("review/images/defaultImage.png");
      }


      restaurant['restaurant_reviewCnt'] = reviewCount;
      restaurant['isMy'] = info.isMy;
      restaurant['isWish'] = info.isWish;
    }

    return restaurants;
  }

  async detailInfo(restaurantId: number, tokenInfo: TokenInfo) {
    const restaurant = await this.restaurantRepository.detailInfo(
      restaurantId,
      tokenInfo
    );

    const reviews = await this.reviewRepository
      .createQueryBuilder("review")
      .leftJoinAndSelect("review.user", "user")
      .leftJoin("review.reviewLikes", "reviewLike", "reviewLike.userId = :userId", { userId: tokenInfo.id })
      .select([
        "review.id",
        "review.isCarVisit",
        "review.transportationAccessibility",
        "review.parkingArea",
        "review.taste",
        "review.service",
        "review.restroomCleanliness",
        "review.overallExperience",
        "user.nickName as reviewer",
        "user.profileImage",
        "review.createdAt",
        "review.reviewImage",
        "reviewLike.isLike as isLike"
      ])
      .addSelect("COUNT(CASE WHEN reviewLike.isLike = true THEN 1 ELSE NULL END)", "likeCount")
      .addSelect("COUNT(CASE WHEN reviewLike.isLike = false THEN 1 ELSE NULL END)", "dislikeCount")
      .groupBy("review.id, user.nickName, user.profileImage, review.isCarVisit, review.transportationAccessibility, review.parkingArea, review.taste, review.service, review.restroomCleanliness, review.overallExperience, review.createdAt, review.reviewImage, reviewLike.isLike")
      .where("review.restaurant_id = :restaurantId", {
        restaurantId: restaurant.restaurant_id,
      })
      .orderBy("COUNT(CASE WHEN reviewLike.isLike = true THEN 1 ELSE NULL END)", "DESC")
      .getRawMany();


    restaurant.restaurant_reviewCnt = reviews.length;
    const reviewList = reviews.slice(0, 3);
    reviewList.forEach((element) => {
      if (element.review_reviewImage && element.review_reviewImage != "review/images/defaultImage.png") element.review_reviewImage = this.awsService.getImageURL(element.review_reviewImage);
      else { element.review_reviewImage = "" }
      if (element.user_profileImage) element.user_profileImage = this.awsService.getImageURL(element.user_profileImage);

    })
    restaurant.reviews = reviewList;
    return restaurant;
  }

  async filteredRestaurantList(
    filterInfoDto: FilterInfoDto,
    tokenInfo: TokenInfo
  ) {
    const target = await this.userRepository.findOne({
      select: ["id"],
      where: { nickName: filterInfoDto.filter },
    });

    const restaurants = await this.restaurantRepository.filteredRestaurantList(
      filterInfoDto,
      tokenInfo,
      target
    );

    for (const restaurant of restaurants) {
      const reviewCount = await this.reviewRepository
        .createQueryBuilder("review")
        .where("review.restaurant_id = :restaurantId", {
          restaurantId: restaurant.restaurant_id,
        })
        .getCount();

      const reviewInfo = await this.reviewRepository
        .createQueryBuilder("review")
        .leftJoin("review.reviewLikes", "reviewLike")
        .select(["review.id", "review.reviewImage"],)
        .groupBy("review.id")
        .where("review.restaurant_id = :restaurantId and review.reviewImage is NOT NULL", { restaurantId: restaurant.restaurant_id })
        .orderBy("COUNT(CASE WHEN reviewLike.isLike = true THEN 1 ELSE NULL END)", "DESC")
        .getRawOne();
      if (reviewInfo) {
        restaurant.restaurant_reviewImage = this.awsService.getImageURL(reviewInfo.review_reviewImage);
      }
      else {
        restaurant.restaurant_reviewImage = this.awsService.getImageURL("review/images/defaultImage.png");
      }


      restaurant.restaurant_reviewCnt = reviewCount;
    }

    return restaurants;
  }

  async entireRestaurantList(locationDto: LocationDto, tokenInfo: TokenInfo, limit: string) {
    const restaurants = await this.restaurantRepository.entireRestaurantList(
      locationDto,
      tokenInfo,
      limit
    );

    for (const restaurant of restaurants) {
      const reviewCount = await this.reviewRepository
        .createQueryBuilder("review")
        .where("review.restaurant_id = :restaurantId", {
          restaurantId: restaurant.restaurant_id,
        })
        .getCount();
      const reviewInfo = await this.reviewRepository
        .createQueryBuilder("review")
        .leftJoin("review.reviewLikes", "reviewLike")
        .select(["review.id", "review.reviewImage"],)
        .groupBy("review.id")
        .where("review.restaurant_id = :restaurantId and review.reviewImage is NOT NULL", { restaurantId: restaurant.restaurant_id })
        .orderBy("COUNT(CASE WHEN reviewLike.isLike = true THEN 1 ELSE NULL END)", "DESC")
        .getRawOne();
      if (reviewInfo) {
        restaurant.restaurant_reviewImage = this.awsService.getImageURL(reviewInfo.review_reviewImage);
      }
      else {
        restaurant.restaurant_reviewImage = this.awsService.getImageURL("review/images/defaultImage.png");
      }
      restaurant.restaurant_reviewCnt = reviewCount;
    }

    return restaurants;
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
