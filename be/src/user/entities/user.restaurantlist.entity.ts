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
import { ReviewInfoEntity } from "src/review/entities/review.entity";

@Entity("user_restaurant_lists")
export class UserRestaurantListEntity {
  @ManyToOne(() => User)
  @JoinColumn({ name: "user_id" })
  user: User;

  @PrimaryColumn({ name: "user_id" })
  userId: number;

  @ManyToOne(() => RestaurantInfoEntity)
  @JoinColumn({ name: "restaurant_id" })
  restaurant: RestaurantInfoEntity;

  @PrimaryColumn({ name: "restaurant_id" })
  restaurantId: number;

  @ManyToOne(() => ReviewInfoEntity)
  @JoinColumn({ name: "review_id" })
  reviewId: ReviewInfoEntity;

  @CreateDateColumn({ name: "created_at", type: "timestamp" })
  createdAt: Date;

  @Column({ name: "deleted_at", type: "timestamp", nullable: true })
  deletedAt: Date | null;
}
