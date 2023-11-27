import { DataSource, IsNull, Repository, Not } from "typeorm";
import { ConflictException, Injectable } from "@nestjs/common";
import { UserRestaurantListEntity } from "./entities/user.restaurantlist.entity";
import { TokenInfo } from "./user.decorator";
import { SearchInfoDto } from "src/restaurant/dto/seachInfo.dto";

@Injectable()
export class UserRestaurantListRepository extends Repository<UserRestaurantListEntity> {
    constructor(private dataSource: DataSource) {
        super(UserRestaurantListEntity, dataSource.createEntityManager());
    }
    async deleteRestaurantFromNebob(id: TokenInfo["id"], restaurantId: number) {
        await this.update({ userId: id, restaurantId: restaurantId }, { deletedAt: new Date() });
        return null;
    }
    async getMyRestaurantListInfo(searchInfoDto: SearchInfoDto, id: TokenInfo["id"]) {
        if (searchInfoDto.radius) {
            return await this
                .createQueryBuilder('user_restaurant_lists')
                .leftJoinAndSelect('user_restaurant_lists.restaurant', 'restaurant')
                .select([
                    'user_restaurant_lists.restaurantId',
                    'restaurant.name',
                    'restaurant.location',
                    'restaurant.address',
                    'restaurant.category',
                    "restaurant.phoneNumber",
                    "restaurant.reviewCnt"
                ])
                .where(`user_restaurant_lists.user_id = :userId and ST_DistanceSphere(
                location, 
                ST_GeomFromText('POINT(${searchInfoDto.longitude} ${searchInfoDto.latitude})', 4326)
            )<  ${searchInfoDto.radius} and user_restaurant_lists.deleted_at IS NULL`, { userId: id })
                .getMany();
        }
        else {
            return await this
                .createQueryBuilder('user_restaurant_lists')
                .leftJoinAndSelect('user_restaurant_lists.restaurant', 'restaurant')
                .select([
                    'user_restaurant_lists.restaurantId AS restaurant_id',
                    'restaurant.name',
                    'restaurant.location',
                    'restaurant.address',
                    'restaurant.category',
                    "restaurant.phoneNumber",
                    "restaurant.reviewCnt"
                ])
                .where('user_restaurant_lists.user_id = :userId  and user_restaurant_lists.deleted_at IS NULL', { userId: id })
                .getRawMany();
        }
    }
}