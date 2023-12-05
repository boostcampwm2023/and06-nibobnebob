import { BadRequestException, Injectable } from '@nestjs/common';
import { ReviewRepository } from './review.repository';
import { TokenInfo } from 'src/user/user.decorator';
import { ReviewLikeEntity } from 'src/review/entities/review.like.entity';
import { ReviewLikeRepository } from './review.like.repository';

@Injectable()
export class ReviewService {
    constructor(
        private reviewRepository: ReviewRepository,
        private reviewLikeRepository: ReviewLikeRepository
    ) { }

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
