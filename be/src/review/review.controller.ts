import { Controller } from '@nestjs/common';
import { ReviewService } from './review.service';

@Controller('review')
export class ReviewController {
    constructor(private reviewService: ReviewService) { }
}
