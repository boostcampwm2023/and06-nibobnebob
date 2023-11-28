import { DataSource, IsNull, Repository, Not } from "typeorm";
import { ConflictException, Injectable } from "@nestjs/common";
import { ReviewInfoEntity } from "./entities/review.entity";

@Injectable()
export class ReviewRepository extends Repository<ReviewInfoEntity> {
  constructor(private dataSource: DataSource) {
    super(ReviewInfoEntity, dataSource.createEntityManager());
  }
}
