import { DataSource, IsNull, Repository, Not } from "typeorm";
import { ConflictException, Injectable } from "@nestjs/common";
import { ReviewInfoEntity } from "./entities/review.entity";
import { TokenInfo } from "../user/user.decorator";
import { SortInfoDto } from "../utils/sortInfo.dto";

@Injectable()
export class ReviewRepository extends Repository<ReviewInfoEntity> {
  constructor(private dataSource: DataSource) {
    super(ReviewInfoEntity, dataSource.createEntityManager());
  }

  async getReviewIdsWithLikes(sort: string) {
    if (sort === "ASC") {
      return await this
        .createQueryBuilder("review")
        .leftJoin("review.reviewLikes", "reviewLike")
        .select("review.id", "reviewId")
        .addSelect("COUNT(reviewLike.isLike)", "likeCount")
        .groupBy("review.id")
        .orderBy("COUNT(CASE WHEN reviewLike.isLike = false THEN 1 ELSE NULL END)", "DESC")
        .getRawMany();
    }
    else {
      return await this
        .createQueryBuilder("review")
        .leftJoin("review.reviewLikes", "reviewLike")
        .select("review.id", "reviewId")
        .addSelect("COUNT(reviewLike.isLike)", "likeCount")
        .groupBy("review.id")
        .orderBy("COUNT(CASE WHEN reviewLike.isLike = true THEN 1 ELSE NULL END)", "DESC")
        .getRawMany();
    }
  }
  async getSortedReviews(getSortedReviewsDto: SortInfoDto, restaurantId: number, id: TokenInfo["id"], sortedReviewIds: number[]) {
    const pageNumber = parseInt(getSortedReviewsDto.page as unknown as string) || 1;
    const limitNumber = parseInt(getSortedReviewsDto.limit as unknown as string) || 10;
    const skipNumber = (pageNumber - 1) * limitNumber;
    if (getSortedReviewsDto && getSortedReviewsDto.sort === "TIME_DESC") {
      const items = await this.createQueryBuilder("review")
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
          "user.profileImage",
          "review.createdAt",
          "review.reviewImage",
          "reviewLike.isLike as isLike",
        ])
        .where("review.restaurant_id = :restaurantId", {
          restaurantId: restaurantId,
        })
        .orderBy("review.createdAt", "DESC")
        .offset(skipNumber)
        .limit(limitNumber + 1)
        .getRawMany();

      const hasNext = items.length > limitNumber;
      const resultItems = hasNext ? items.slice(0, -1) : items;

      return { hasNext, items: resultItems };
    }
    else if (getSortedReviewsDto && getSortedReviewsDto.sort === "TIME_ASC") {
      const items = await this.createQueryBuilder("review")
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
          "user.profileImage",
          "review.createdAt",
          "review.reviewImage",
          "reviewLike.isLike as isLike",
        ])
        .where("review.restaurant_id = :restaurantId", {
          restaurantId: restaurantId,
        })
        .orderBy("review.createdAt", "ASC")
        .offset(skipNumber)
        .limit(limitNumber + 1)
        .getRawMany();

      const hasNext = items.length > limitNumber;
      const resultItems = hasNext ? items.slice(0, -1) : items;

      return { hasNext, items: resultItems };
    }
    else {
      if (sortedReviewIds.length) {
        const items = await this.createQueryBuilder("review")
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
            "user.profileImage",
            "review.createdAt",
            "review.reviewImage",
            "reviewLike.isLike as isLike",
          ])
          .where("review.id IN (:...sortedReviewIds)", { sortedReviewIds })
          .andWhere("review.restaurant_id = :restaurantId", { restaurantId: restaurantId })
          .offset(skipNumber)
          .limit(limitNumber + 1)
          .getRawMany();
        const sortedItems = sortedReviewIds
          .map(id => items.find(item => item.review_id === id))
          .filter(item => item !== undefined);

        const hasNext = sortedItems.length > limitNumber;
        const resultItems = hasNext ? sortedItems.slice(0, -1) : sortedItems;

        return { hasNext, items: resultItems };
      }
    }
  }
}
