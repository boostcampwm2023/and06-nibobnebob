import {
  Entity,
  PrimaryGeneratedColumn,
  Column,
  CreateDateColumn,
  ManyToOne,
  JoinColumn,
  OneToOne,
} from "typeorm";
import { User } from "src/user/entities/user.entity";
import { RestaurantInfoEntity } from "src/restaurant/entities/restaurant.entity";
import { UserRestaurantListEntity } from "src/user/entities/user.restaurantlist.entity";

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

  @CreateDateColumn({ name: "created_at" })
  createdAt: Date;

  @ManyToOne(() => User)
  @JoinColumn({ name: "user_id" })
  user: User;

  @ManyToOne(() => RestaurantInfoEntity)
  @JoinColumn({ name: "restaurant_id" })
  restaurant: RestaurantInfoEntity;

  @OneToOne(() => UserRestaurantListEntity)
  userRestaurant: UserRestaurantListEntity
}
