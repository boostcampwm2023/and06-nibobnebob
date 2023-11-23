import { DataSource, IsNull, Repository, Not } from "typeorm";
import { ConflictException, Injectable } from "@nestjs/common";
import { UserRestaurantListEntity } from "./entities/user.restaurantlist.entity";

@Injectable()
export class UserRestaurantListRepository extends Repository<UserRestaurantListEntity> {
    constructor(private dataSource: DataSource) {
        super(UserRestaurantListEntity, dataSource.createEntityManager());
    }
}