import { DataSource, IsNull, Repository, Not } from "typeorm";
import { ConflictException, Injectable } from "@nestjs/common";
import { ReviewLikeEntity } from "./entities/review.like.entity";

@Injectable()
export class ReviewLikeRepository extends Repository<ReviewLikeEntity> {
    constructor(private dataSource: DataSource) {
        super(ReviewLikeEntity, dataSource.createEntityManager());
    }
}
