import { DataSource, IsNull, Repository, Not } from "typeorm";
import { ConflictException, Injectable } from "@nestjs/common";
import { UserRestaurantListEntity } from "./entities/user.restaurantlist.entity";
import { TokenInfo } from "./user.decorator";

@Injectable()
export class UserRestaurantListRepository extends Repository<UserRestaurantListEntity> {
    constructor(private dataSource: DataSource) {
        super(UserRestaurantListEntity, dataSource.createEntityManager());
    }
    async deleteRestaurantFromNebob(id: TokenInfo["id"], restaurantId: number) {
        await this.update({ userId: id, restaurantId: restaurantId }, { deletedAt: new Date() });

        return null;
    }
}