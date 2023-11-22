import {
  Entity,
  PrimaryGeneratedColumn,
  Column,
  CreateDateColumn,
  UpdateDateColumn,
  Unique,
} from "typeorm";
import { Point } from "geojson";

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
}
