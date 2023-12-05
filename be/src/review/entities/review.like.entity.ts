import {
    Entity,
    PrimaryGeneratedColumn,
    Column,
    CreateDateColumn,
    ManyToOne,
    JoinColumn,
    DeleteDateColumn,
} from "typeorm";
import { User } from "src/user/entities/user.entity";
import { ReviewInfoEntity } from "./review.entity";

@Entity("reviewLike")
export class ReviewLikeEntity {
    @PrimaryGeneratedColumn("increment")
    id: number;

    @Column({ name: "is_like" })
    isLike: boolean;

    @CreateDateColumn({ name: "created_at" })
    createdAt: Date;

    @DeleteDateColumn({ name: "deleted_at", nullable: true, type: "timestamp" })
    deletedAt: Date | null;

    @ManyToOne(() => ReviewInfoEntity)
    @JoinColumn({ name: "review_id" })
    reviewId: ReviewInfoEntity;

    @ManyToOne(() => User)
    @JoinColumn({ name: "user_id" })
    userId: User;
}
