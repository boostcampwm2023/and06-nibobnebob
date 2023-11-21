import {
    Entity,
    PrimaryColumn,
    CreateDateColumn,
    DeleteDateColumn,
    ManyToOne,
    JoinColumn
} from 'typeorm';
import { User } from './user.entity';

@Entity('follow')
export class FollowEntity {
    @ManyToOne(() => User)
    @PrimaryColumn({ name: 'following_user_id' })
    followingUserId: number;

    @ManyToOne(() => User)
    @PrimaryColumn({ name: 'followed_user_id' })
    followedUserId: number;

    @CreateDateColumn({ name: 'created_at' })
    createdAt: Date;

    @DeleteDateColumn({ name: 'deleted_at', nullable: true, type: 'timestamp' })
    deletedAt: Date | null;

}
