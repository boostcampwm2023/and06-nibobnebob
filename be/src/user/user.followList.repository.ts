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
    async getMyFollowerListInfo(id: TokenInfo['id']) {
        return await this.find({ select: ["followedUserId"], where: { 'followingUserId': id } });
    }
    async followUser(id: TokenInfo['id'], targetId: number) {
        const followEntity = new FollowEntity();
        followEntity.followedUserId = id;
        followEntity.followingUserId = targetId;
        followEntity.createdAt = new Date();
        followEntity.deletedAt = null;
        return await this.upsert(followEntity, ["followedUserId", "followingUserId"]);
    }
    async unfollowUser(id: TokenInfo['id'], targetId: number) {
        const followEntity = new FollowEntity();
        followEntity.followedUserId = id;
        followEntity.followingUserId = targetId;
        followEntity.deletedAt = new Date();
        return await this.upsert(followEntity, ["followedUserId", "followingUserId"]);
    }
}
