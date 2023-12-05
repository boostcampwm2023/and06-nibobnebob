import { BadRequestException, Injectable } from '@nestjs/common';
import { ReviewRepository } from './review.repository';
import { TokenInfo } from 'src/user/user.decorator';
import { ReviewLikeRepository } from './review.like.repository';
import { SortInfoDto } from 'src/utils/sortInfo.dto';

@Injectable()
export class ReviewService {
    constructor(
        private reviewRepository: ReviewRepository,
        private reviewLikeRepository: ReviewLikeRepository
    ) { }

    async getSortedReviews(tokenInfo: TokenInfo, restaurantId: number, getSortedReviewsDto: SortInfoDto) {
        let sortedReviewIds;
        if (getSortedReviewsDto.sort === "REVIEW ASC") {
            const getReviewIdsWithLikes = await this.reviewLikeRepository.getReviewIdsWithLikes("ASC");
            sortedReviewIds = getReviewIdsWithLikes.map(rl => rl.reviewId);
        }
        else {
            const getReviewIdsWithLikes = await this.reviewLikeRepository.getReviewIdsWithLikes("DESC");
            sortedReviewIds = getReviewIdsWithLikes.map(rl => rl.reviewId);
        }

        const reviews = await this.reviewRepository.getSortedReviews(getSortedReviewsDto, restaurantId, tokenInfo.id, sortedReviewIds);
        for (const review of reviews) {
            const likeCounts = await this.reviewLikeRepository.createQueryBuilder("reviewLike")
                .select("reviewLike.isLike", "status")
                .addSelect("COUNT(*)", "count")
                .where("reviewLike.reviewId = :reviewId", { reviewId: review.review_id })
                .groupBy("reviewLike.isLike")
                .getRawMany();

            review.likeCount = Number(likeCounts.find(lc => lc.status === true)?.count) || 0;
            review.dislikeCount = Number(likeCounts.find(lc => lc.status === false)?.count) || 0;
        }

        return reviews;
    }

    async reviewLike(tokenInfo: TokenInfo, reviewId: number) {
        const entity = this.reviewLikeRepository.create({
            userId: tokenInfo.id,
            reviewId: reviewId,
            isLike: true,
        });
        try {
            await this.reviewLikeRepository.upsert(entity, ['userId', 'reviewId']);
        } catch (err) {
            throw new BadRequestException();
        }
    }

    async reviewUnLike(tokenInfo: TokenInfo, reviewId: number) {
        const entity = this.reviewLikeRepository.create({
            userId: tokenInfo.id,
            reviewId: reviewId,
            isLike: false,
        });
        try {
            await this.reviewLikeRepository.upsert(entity, ['userId', 'reviewId']);
        } catch (err) {
            throw new BadRequestException();
        }
    }
}
