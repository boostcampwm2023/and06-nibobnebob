import {
  Entity,
  PrimaryGeneratedColumn,
  Column,
  CreateDateColumn,
  ManyToOne,
  JoinColumn,
} from "typeorm";
import { User } from "src/user/entities/user.entity";
import { RestaurantInfoEntity } from "src/restaurant/entities/restaurant.entity";

@Entity("review")
export class ReviewInfoEntity {
  @PrimaryGeneratedColumn("increment")
  id: number;

  @ManyToOne(() => User)
  @JoinColumn({ name: "user_id" })
  user: User;

  @ManyToOne(() => RestaurantInfoEntity)
  @JoinColumn({ name: "restaurant_id" })
  restaurant: RestaurantInfoEntity;

  @Column({ type: "boolean" })
  visitMethod: boolean;

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
}
