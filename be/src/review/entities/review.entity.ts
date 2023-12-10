import {
  Entity,
  PrimaryGeneratedColumn,
  Column,
  CreateDateColumn,
  ManyToOne,
  JoinColumn,
  OneToOne,
  OneToMany,
} from "typeorm";
import { User } from "../../user/entities/user.entity";
import { RestaurantInfoEntity } from "../../restaurant/entities/restaurant.entity";
import { UserRestaurantListEntity } from "../../user/entities/user.restaurantlist.entity";
import { ReviewLikeEntity } from "./review.like.entity";

@Entity("review")
export class ReviewInfoEntity {
  @PrimaryGeneratedColumn("increment")
  id: number;

  @Column({ type: "boolean" })
  isCarVisit: boolean;

  @Column({ type: "smallint", nullable: true })
  transportationAccessibility: number | null;

  @Column({ type: "smallint", nullable: true })
  parkingArea: number | null;

  @Column({ type: "smallint" })
  taste: number;

  @Column({ type: "smallint" })
  service: number;

  @Column({ type: "smallint" })
  restroomCleanliness: number;

  @Column({ type: "text" })
  overallExperience: string;

  @Column({ type: "text", nullable: true, default: null })
  reviewImage: string;

  @CreateDateColumn({ name: "created_at" })
  createdAt: Date;

  @OneToMany(() => ReviewLikeEntity, reviewLike => reviewLike.review)
  reviewLikes: ReviewLikeEntity[];

  @ManyToOne(() => User)
  @JoinColumn({ name: "user_id" })
  user: User;

  @ManyToOne(() => RestaurantInfoEntity)
  @JoinColumn({ name: "restaurant_id" })
  restaurant: RestaurantInfoEntity;

  @OneToOne(() => UserRestaurantListEntity)
  userRestaurant: UserRestaurantListEntity;
}
