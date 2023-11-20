import {
  Entity,
  Column,
  CreateDateColumn,
  ManyToOne,
  JoinColumn,
  PrimaryColumn,
} from 'typeorm';
import { User } from './user.entity';
import { Restaurant } from 'src/restaurant/entities/restaurant.entity';
import { Review } from 'src/review/entities/review.entity';

@Entity('user_restaurant_lists')
export class UserRestaurantListEntity {
  @ManyToOne(() => User)
  @PrimaryColumn({ name: 'user_id' })
  userId: User;

  @ManyToOne(() => Restaurant)
  @PrimaryColumn({ name: 'restaurant_id' })
  restaurantId: Restaurant;

  @ManyToOne(() => Review)
  @JoinColumn({ name: 'review_id' })
  reviewId: Review;

  @CreateDateColumn({ name: 'created_at', type: 'timestamp' })
  createdAt: Date;

  @Column({ name: 'deleted_at', type: 'timestamp', nullable: true })
  deletedAt: Date | null;
}
