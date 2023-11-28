import {
    Entity,
    Column,
    CreateDateColumn,
    ManyToOne,
    JoinColumn,
    PrimaryColumn,
} from "typeorm";
import { User } from "./user.entity";
import { RestaurantInfoEntity } from "src/restaurant/entities/restaurant.entity";

@Entity("user_wishrestaurant_lists")
export class UserWishRestaurantListEntity {
    @PrimaryColumn({ name: "user_id" })
    userId: number;

    @PrimaryColumn({ name: "restaurant_id" })
    restaurantId: number;

    @CreateDateColumn({ name: "created_at", type: "timestamp" })
    createdAt: Date;

    @Column({ name: "deleted_at", type: "timestamp", nullable: true })
    deletedAt: Date | null;

    @ManyToOne(() => User)
    @JoinColumn({ name: "user_id" })
    user: User;

    @ManyToOne(() => RestaurantInfoEntity)
    @JoinColumn({ name: "restaurant_id" })
    restaurant: RestaurantInfoEntity;

}


