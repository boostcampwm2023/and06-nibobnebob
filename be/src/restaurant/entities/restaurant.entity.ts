import {
  Entity,
  PrimaryGeneratedColumn,
  Column,
  CreateDateColumn,
  UpdateDateColumn,
  Unique,
  OneToMany,
} from "typeorm";
import { Point } from "geojson";
import { ReviewInfoEntity } from "src/review/entities/review.entity";
import { User } from "src/user/entities/user.entity";
import { UserRestaurantListEntity } from "src/user/entities/user.restaurantlist.entity";

@Unique("unique_name_location", ["name", "location"])
@Entity("restaurant")
export class RestaurantInfoEntity {
  @PrimaryGeneratedColumn("increment")
  id: number;

  @Column({ type: "varchar", length: 100 })
  name: string;

  @Column({
    type: "geometry",
    spatialFeatureType: "Point",
    srid: 4326,
    nullable: true,
  })
  location: Point;

  @Column({ type: "text", nullable: true })
  address: string | null;

  @Column({ type: "varchar", length: 50, nullable: true })
  category: string | null;

  @Column({ type: "int", default: 0 })
  reviewCnt: number;

  @Column({ type: "varchar", length: 20, nullable: true })
  phoneNumber: string | null;

  @CreateDateColumn({ name: "created_at" })
  createdAt: Date;

  @Column({ name: "deleted_at", type: "timestamp", nullable: true })
  deletedAt: Date | null;

  @UpdateDateColumn({ name: "updated_at" })
  updatedAt: Date;

  @OneToMany(() => UserRestaurantListEntity, userRestaurant => userRestaurant.restaurant)
  userRestaurant: UserRestaurantListEntity

  @OneToMany(() => ReviewInfoEntity, review => review.restaurant)
  review: ReviewInfoEntity
}
