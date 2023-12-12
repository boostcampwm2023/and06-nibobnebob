import { BadRequestException, Injectable } from '@nestjs/common';
import { ReviewRepository } from './review.repository';
import { TokenInfo } from '../user/user.decorator';
import { ReviewLikeRepository } from './review.like.repository';
import { SortInfoDto } from '../utils/sortInfo.dto';
import { AwsService } from '../aws/aws.service';

@Injectable()
export class ReviewService {
    constructor(
        private reviewRepository: ReviewRepository,
        private reviewLikeRepository: ReviewLikeRepository,
        private awsService: AwsService
    ) { }

    async getSortedReviews(tokenInfo: TokenInfo, restaurantId: number, getSortedReviewsDto: SortInfoDto) {
        let sortedReviewIds;
        if (getSortedReviewsDto.sort === "REVIEW_ASC") {
            const getReviewIdsWithLikes = await this.reviewRepository.getReviewIdsWithLikes("ASC");
            sortedReviewIds = getReviewIdsWithLikes.map(rl => rl.reviewId);
        }
        else {
            const getReviewIdsWithLikes = await this.reviewRepository.getReviewIdsWithLikes("DESC");
            sortedReviewIds = getReviewIdsWithLikes.map(rl => rl.reviewId);
        }

        const reviews = await this.reviewRepository.getSortedReviews(getSortedReviewsDto, restaurantId, tokenInfo.id, sortedReviewIds);
        for (const review of reviews.items) {
            const likeCounts = await this.reviewLikeRepository.createQueryBuilder("reviewLike")
                .select("reviewLike.isLike", "status")
                .addSelect("COUNT(*)", "count")
                .where("reviewLike.reviewId = :reviewId", { reviewId: review.review_id })
                .groupBy("reviewLike.isLike")
                .getRawMany();
            if (review.user_profileImage) review.user_profileImage = this.awsService.getImageURL(review.user_profileImage);
            if (review.review_reviewImage && review.review_reviewImage != "review/images/defaultImage.png") review.review_reviewImage = this.awsService.getImageURL(review.review_reviewImage);
            else { review.review_reviewImage = "" }
            review.likeCount = Number(likeCounts.find(lc => lc.status === true)?.count) || 0;
            review.dislikeCount = Number(likeCounts.find(lc => lc.status === false)?.count) || 0;
        }

        return reviews;
    }

    async reviewLike(tokenInfo: TokenInfo, reviewId: number) {
        const existingLike = await this.reviewLikeRepository.findOne({
            where: { userId: tokenInfo.id, reviewId: reviewId }
        });

        if (existingLike && existingLike.isLike) {
            await this.reviewLikeRepository.remove(existingLike);
        } else {
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
    }

    async reviewUnLike(tokenInfo: TokenInfo, reviewId: number) {
        const existingLike = await this.reviewLikeRepository.findOne({
            where: { userId: tokenInfo.id, reviewId: reviewId }
        });

        if (existingLike && !existingLike.isLike) {
            await this.reviewLikeRepository.remove(existingLike);
        } else {
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

}
