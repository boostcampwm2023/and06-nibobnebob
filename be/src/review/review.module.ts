import { Module } from "@nestjs/common";
import { ReviewRepository } from "./review.repository";
import { ReviewController } from "./review.controller";
import { ReviewService } from "./review.service";

@Module({
  controllers: [ReviewController],
  providers: [ReviewRepository, ReviewService],
  exports: [ReviewRepository],
})
export class ReviewModule { }
