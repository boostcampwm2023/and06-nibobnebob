import { Module } from '@nestjs/common';
import { RestaurantController } from './restaurant.controller';
import { AuthModule } from 'src/auth/auth.module';
import { RestaurantService } from './restaurant.service';
import { RestaurantRepository } from './restaurant.repository';

@Module({
  imports: [AuthModule],
  controllers: [RestaurantController],
  providers: [RestaurantService, RestaurantRepository]
})
export class RestaurantModule {}
