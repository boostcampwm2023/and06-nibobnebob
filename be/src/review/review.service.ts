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
            userId: { id: tokenInfo.id },
            reviewId: { id: reviewId },
            isLike: true,
        });
        try {
            await this.reviewLikeRepository.save(entity);
        } catch (err) {
            throw new BadRequestException();
        }
    }
}
