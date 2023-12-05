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

@Entity("userreview")
export class ReviewInfoEntity {
    @PrimaryGeneratedColumn("increment")
    id: number;

    @Column({ name: "is_like" })
    isLike: boolean;

    @ManyToOne(() => User)
    @JoinColumn({ name: "user_id" })
    user_id: User;

    @CreateDateColumn({ name: "created_at" })
    createdAt: Date;

    @DeleteDateColumn({ name: "deleted_at", nullable: true, type: "timestamp" })
    deletedAt: Date | null;
}
