import { DataSource, IsNull, Repository, Not } from "typeorm";
import { ConflictException, Injectable } from "@nestjs/common";
import { UserWishRestaurantListEntity } from "./entities/user.wishrestaurantlist.entity";
import { TokenInfo } from "./user.decorator";
import { RestaurantInfoEntity } from "src/restaurant/entities/restaurant.entity";
@Injectable()
export class UserWishRestaurantListRepository extends Repository<UserWishRestaurantListEntity> {
    constructor(private dataSource: DataSource) {
        super(UserWishRestaurantListEntity, dataSource.createEntityManager());
    }
    async addRestaurantToWishNebob(id: TokenInfo["id"], restaurantId: RestaurantInfoEntity["id"]) {
        const userWishRestaurantList = new UserWishRestaurantListEntity();
        userWishRestaurantList.userId = id;
        userWishRestaurantList.restaurantId = restaurantId;
        userWishRestaurantList.deletedAt = null;
        userWishRestaurantList.createdAt = new Date();

        await this.upsert(userWishRestaurantList, ["userId", "restaurantId"]);
        return null;
    }
    async deleteRestaurantFromWishNebob(id: TokenInfo["id"], restaurantId: number) {
        await this.update({ userId: id, restaurantId: restaurantId }, { deletedAt: new Date() });
        return null;
    }
}