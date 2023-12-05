import { DataSource, IsNull, Repository, Not } from "typeorm";
import { ConflictException, Injectable } from "@nestjs/common";
import { UserWishRestaurantListEntity } from "./entities/user.wishrestaurantlist.entity";
import { TokenInfo } from "./user.decorator";
import { RestaurantInfoEntity } from "src/restaurant/entities/restaurant.entity";
import { UserRestaurantListEntity } from "./entities/user.restaurantlist.entity";
import { SortInfoDto } from "src/utils/sortInfo.dto";
@Injectable()
export class UserWishRestaurantListRepository extends Repository<UserWishRestaurantListEntity> {
  constructor(private dataSource: DataSource) {
    super(UserWishRestaurantListEntity, dataSource.createEntityManager());
  }
  async addRestaurantToWishNebob(
    id: TokenInfo["id"],
    restaurantId: RestaurantInfoEntity["id"]
  ) {
    const userWishRestaurantList = new UserWishRestaurantListEntity();
    userWishRestaurantList.userId = id;
    userWishRestaurantList.restaurantId = restaurantId;
    userWishRestaurantList.deletedAt = null;
    userWishRestaurantList.createdAt = new Date();

    await this.upsert(userWishRestaurantList, ["userId", "restaurantId"]);
    return null;
  }
  async deleteRestaurantFromWishNebob(
    id: TokenInfo["id"],
    restaurantId: number
  ) {
    await this.update(
      { userId: id, restaurantId: restaurantId },
      { deletedAt: new Date() }
    );
    return null;
  }
  async getMyWishRestaurantListInfo(id: TokenInfo["id"], sortInfoDto: SortInfoDto) {
    let query = this.createQueryBuilder("user_wishrestaurant_lists")
      .leftJoinAndSelect("user_wishrestaurant_lists.restaurant", "restaurant")
      .leftJoin(
        UserRestaurantListEntity,
        "current_url",
        "current_url.restaurantId = restaurant.id AND current_url.userId = :currentUserId",
        { currentUserId: id }
      )
      .select([
        "user_wishrestaurant_lists.restaurantId AS restaurant_id",
        "restaurant.name",
        "restaurant.location",
        "restaurant.address",
        "restaurant.category",
        "restaurant.phoneNumber",
        "user_wishrestaurant_lists.created_at",
        'CASE WHEN current_url.user_id IS NOT NULL THEN true ELSE false END AS "isMy"',
        'CASE WHEN user_wishrestaurant_lists.user_id IS NOT NULL THEN true ELSE false END AS "isWish"',
      ])
      .where(
        `user_wishrestaurant_lists.user_id = :userId and user_wishrestaurant_lists.deleted_at IS NULL`,
        { userId: id }
      )

    if (sortInfoDto.sort === 'TIME_ASC') {
      query = query.orderBy("user_wishrestaurant_lists.created_at", "ASC");
    } else {
      query = query.orderBy("user_wishrestaurant_lists.created_at", "DESC");
    }

    sortInfoDto.page = sortInfoDto.page || 1;
    sortInfoDto.limit = sortInfoDto.limit || 10;

    const offset = (sortInfoDto.page - 1) * sortInfoDto.limit;
    query = query.skip(offset).take(sortInfoDto.limit + 1);
  
    const items = await query.getRawMany();
  
    const hasNext = items.length > sortInfoDto.limit;
    const resultItems = hasNext ? items.slice(0, -1) : items;
  
    return {
      hasNext,
      items : resultItems,
    }
  }
}
