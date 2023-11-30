import {
  Entity,
  PrimaryColumn,
  CreateDateColumn,
  DeleteDateColumn,
  ManyToOne,
  JoinColumn,
} from "typeorm";
import { User } from "./user.entity";

@Entity("follow")
export class FollowEntity {
  @PrimaryColumn({ name: "following_user_id" })
  followingUserId: number;

  @PrimaryColumn({ name: "followed_user_id" })
  followedUserId: number;

  @CreateDateColumn({ name: "created_at" })
  createdAt: Date;

  @DeleteDateColumn({ name: "deleted_at", nullable: true, type: "timestamp" })
  deletedAt: Date | null;

  @ManyToOne(() => User)
  @JoinColumn({ name: "following_user_id" })
  followingUser: User;

  @ManyToOne(() => User)
  @JoinColumn({ name: "followed_user_id" })
  followedUser: User;
}
