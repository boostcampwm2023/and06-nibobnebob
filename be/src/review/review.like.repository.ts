import { DataSource, IsNull, Repository, Not } from "typeorm";
import { ConflictException, Injectable } from "@nestjs/common";
import { ReviewInfoEntity } from "./entities/review.entity";
import { ReviewLikeEntity } from "./entities/review.like.entity";
import { String } from "aws-sdk/clients/cloudhsm";

@Injectable()
export class ReviewLikeRepository extends Repository<ReviewLikeEntity> {
    constructor(private dataSource: DataSource) {
        super(ReviewLikeEntity, dataSource.createEntityManager());
    }
}
