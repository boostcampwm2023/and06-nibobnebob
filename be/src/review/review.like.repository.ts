import { DataSource, IsNull, Repository, Not } from "typeorm";
import { ConflictException, Injectable } from "@nestjs/common";
import { ReviewInfoEntity } from "./entities/review.entity";
import { ReviewLikeEntity } from "./entities/review.like.entity";
import { String } from "aws-sdk/clients/cloudhsm";

@Injectable()
export class ReviewLikeRepository extends Repository<ReviewLikeEntity> {
    constructor(private dataSource: DataSource) {
        super(ReviewLikeEntity, dataSource.createEntityManager());
    }

    async getReviewIdsWithLikes(sort: string) {
        if (sort === "ASC") {
            return await this
                .createQueryBuilder("reviewLike")
                .select("reviewLike.reviewId", "reviewId")
                .addSelect("COUNT(reviewLike.isLike)", "likeCount")
                .where("reviewLike.isLike = :isLike", { isLike: false })
                .groupBy("reviewLike.reviewId")
                .orderBy("COUNT(reviewLike.isLike)", "DESC")
                .getRawMany();
        }
        else {
            return await this
                .createQueryBuilder("reviewLike")
                .select("reviewLike.reviewId", "reviewId")
                .addSelect("COUNT(reviewLike.isLike)", "likeCount")
                .where("reviewLike.isLike = :isLike", { isLike: true })
                .groupBy("reviewLike.reviewId")
                .orderBy("COUNT(reviewLike.isLike)", "DESC")
                .getRawMany();
        }
    }


}
