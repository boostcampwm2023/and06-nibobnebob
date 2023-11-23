import { DataSource, IsNull, Repository, Not } from "typeorm";
import { ConflictException, Injectable } from "@nestjs/common";
import { FollowEntity } from "./entities/user.followList.entity";
import { TokenInfo } from "./user.decorator";


@Injectable()
export class UserFollowListRepository extends Repository<FollowEntity> {
    constructor(private dataSource: DataSource) {
        super(FollowEntity, dataSource.createEntityManager());
    }
    async getMyFollowListInfo(id: TokenInfo['id']) {
        return await this.find({ select: ["followingUserId"], where: { 'followedUserId': id } });
    }
}
