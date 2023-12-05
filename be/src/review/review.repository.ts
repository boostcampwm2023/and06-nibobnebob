import { DataSource, IsNull, Repository, Not } from "typeorm";
import { ConflictException, Injectable } from "@nestjs/common";
import { ReviewInfoEntity } from "./entities/review.entity";
import { TokenInfo } from "src/user/user.decorator";
import { SortInfoDto } from "src/utils/sortInfo.dto";

@Injectable()
export class ReviewRepository extends Repository<ReviewInfoEntity> {
  constructor(private dataSource: DataSource) {
    super(ReviewInfoEntity, dataSource.createEntityManager());
  }
  async getSortedReviews(getSortedReviewsDto: SortInfoDto, restaurantId: number, id: TokenInfo["id"], sortedReviewIds: number[]) {
    const pageNumber = getSortedReviewsDto.page || 1;
    const limitNumber = getSortedReviewsDto.limit || 10;
    const skipNumber = (pageNumber - 1) * limitNumber;
    if (getSortedReviewsDto && getSortedReviewsDto.sort === "TIME DESC") {
      return await this.createQueryBuilder("review")
        .leftJoinAndSelect("review.user", "user")
        .leftJoin("review.reviewLikes", "reviewLike", "reviewLike.userId = :userId", { userId: id })
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
          "review.createdAt",
          "review.reviewImage",
          "reviewLike.isLike",
        ])
        .where("review.restaurant_id = :restaurantId", {
          restaurantId: restaurantId,
        })
        .orderBy("review.createdAt", "DESC")
        .skip(skipNumber)
        .take(limitNumber)
        .getRawMany();
    }
    else if (getSortedReviewsDto && getSortedReviewsDto.sort === "TIME ASC") {
      return await this.createQueryBuilder("review")
        .leftJoinAndSelect("review.user", "user")
        .leftJoin("review.reviewLikes", "reviewLike", "reviewLike.userId = :userId", { userId: id })
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
          "review.createdAt",
          "review.reviewImage",
          "reviewLike.isLike",
        ])
        .where("review.restaurant_id = :restaurantId", {
          restaurantId: restaurantId,
        })
        .orderBy("review.createdAt", "ASC")
        .skip(skipNumber)
        .take(limitNumber)
        .getRawMany();
    }
    else {
      if (sortedReviewIds.length) {
        return await this.createQueryBuilder("review")
          .leftJoinAndSelect("review.user", "user")
          .leftJoin("review.reviewLikes", "reviewLike", "reviewLike.userId = :userId", { userId: id })
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
            "review.createdAt",
            "review.reviewImage",
            "reviewLike.isLike",
          ])
          .where("review.id IN (:...sortedReviewIds)", { sortedReviewIds })
          .andWhere("review.restaurant_id = :restaurantId", { restaurantId: restaurantId })
          .skip(skipNumber)
          .take(limitNumber)
          .getRawMany();
      }
      return [];
    }
  }
}
