import { Module } from "@nestjs/common";
import { ReviewRepository } from "./review.repository";

@Module({
  providers: [ReviewRepository],
  exports: [ReviewRepository]
})
export class ReviewModule { }
