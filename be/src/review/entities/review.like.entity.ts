import {
    Entity,
    PrimaryColumn,
    Column,
    CreateDateColumn,
    ManyToOne,
    JoinColumn,
    DeleteDateColumn,
} from "typeorm";
import { User } from "../../user/entities/user.entity";
import { ReviewInfoEntity } from "./review.entity";

@Entity("reviewLike")
export class ReviewLikeEntity {
    @PrimaryColumn({ name: "review_id" })
    reviewId: number;

    @PrimaryColumn({ name: "user_id" })
    userId: number;

    @Column({ name: "is_like" })
    isLike: boolean;

    @CreateDateColumn({ name: "created_at" })
    createdAt: Date;

    @DeleteDateColumn({ name: "deleted_at", nullable: true, type: "timestamp" })
    deletedAt: Date | null;

    @ManyToOne(() => ReviewInfoEntity)
    @JoinColumn({ name: "review_id", referencedColumnName: "id" })
    review: ReviewInfoEntity;

    @ManyToOne(() => User)
    @JoinColumn({ name: "user_id", referencedColumnName: "id" })
    user: User;
}
