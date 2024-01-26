import { Module } from "@nestjs/common";
import { ReviewRepository } from "./review.repository";
import { ReviewController } from "./review.controller";
import { ReviewService } from "./review.service";
import { ReviewLikeRepository } from "./review.like.repository";

@Module({
  controllers: [ReviewController],
  providers: [ReviewRepository, ReviewLikeRepository, ReviewService],
  exports: [ReviewRepository],
})
export class ReviewModule { }
