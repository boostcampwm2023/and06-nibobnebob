import { DataSource, IsNull, Repository, Not } from "typeorm";
import { ConflictException, Injectable } from "@nestjs/common";
import { FollowEntity } from "./entities/user.followList.entity";


@Injectable()
export class UserFollowListRepository extends Repository<FollowEntity> {
    constructor(private dataSource: DataSource) {
        super(FollowEntity, dataSource.createEntityManager());
    }
}
