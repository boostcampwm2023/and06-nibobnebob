import {
  Entity,
  Column,
  CreateDateColumn,
  ManyToOne,
  JoinColumn,
  PrimaryColumn,
  DeleteDateColumn,
} from "typeorm";
import { User } from "./user.entity";
import { RestaurantInfoEntity } from "../../restaurant/entities/restaurant.entity";
import { ReviewInfoEntity } from "../../review/entities/review.entity";

@Entity("user_restaurant_lists")
export class UserRestaurantListEntity {
  @PrimaryColumn({ name: "user_id" })
  userId: number;

  @PrimaryColumn({ name: "restaurant_id" })
  restaurantId: number;

  @CreateDateColumn({ name: "created_at", type: "timestamp" })
  createdAt: Date;

  @DeleteDateColumn({ name: "deleted_at", type: "timestamp", nullable: true })
  deletedAt: Date | null;

  @ManyToOne(() => User)
  @JoinColumn({ name: "user_id" })
  user: User;

  @ManyToOne(() => RestaurantInfoEntity)
  @JoinColumn({ name: "id" })
  restaurant: RestaurantInfoEntity;

  @ManyToOne(() => ReviewInfoEntity)
  @JoinColumn({ name: "review_id" })
  review: ReviewInfoEntity;
}
