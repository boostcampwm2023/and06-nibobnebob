import { Injectable } from '@nestjs/common';
import { ReviewRepository } from './review.repository';

@Injectable()
export class ReviewService {
    constructor(
        private reviewRepository: ReviewRepository
    ) { }


}
