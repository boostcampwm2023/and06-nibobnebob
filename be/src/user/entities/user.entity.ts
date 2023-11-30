import {
  Entity,
  PrimaryGeneratedColumn,
  Column,
  CreateDateColumn,
  UpdateDateColumn,
  DeleteDateColumn,
  OneToMany,
} from "typeorm";
import { FollowEntity } from "./user.followList.entity";
import { UserRestaurantListEntity } from "./user.restaurantlist.entity";
import { ReviewInfoEntity } from "src/review/entities/review.entity";

@Entity()
export class User {
  @PrimaryGeneratedColumn("increment")
  id: number;

  @Column({ type: "varchar", length: 20, unique: true })
  nickName: string;

  @Column({ type: "varchar", length: 50, unique: true })
  email: string;

  @Column({ type: "varchar", length: 11 })
  birthdate: string;

  @Column({ type: "varchar", length: 20 })
  region: string;

  @Column({ type: "boolean" })
  isMale: boolean;

  @Column({ type: "varchar", length: 62, nullable: true })
  password: string | null;

  @Column({ type: "varchar", length: 20, nullable: true })
  provider: string | null;

  @Column({ type: "string", nullable: true })
  profileImage: string | null;

  @CreateDateColumn({ type: "timestamp" })
  created_at: Date;

  @DeleteDateColumn({ type: "timestamp", nullable: true })
  deleted_at: Date | null;

  @UpdateDateColumn({ type: "timestamp" })
  updated_at: Date;

  @OneToMany(() => FollowEntity, (follow) => follow.followingUserId)
  following: FollowEntity[];

  @OneToMany(() => FollowEntity, (follow) => follow.followedUserId)
  follower: FollowEntity[];

  @OneToMany(() => UserRestaurantListEntity, (list) => list.userId)
  restaurant: UserRestaurantListEntity[];

  @OneToMany(() => ReviewInfoEntity, (review) => review.user)
  review: UserRestaurantListEntity[];
}
