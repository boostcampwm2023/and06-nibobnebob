import { Module } from "@nestjs/common";
import { RestaurantController } from "./restaurant.controller";
import { AuthModule } from "src/auth/auth.module";
import { RestaurantService } from "./restaurant.service";
import { RestaurantRepository } from "./restaurant.repository";
import { UserModule } from "src/user/user.module";
import { ReviewModule } from "src/review/review.module";
import { ScheduleModule } from '@nestjs/schedule';

@Module({
  imports: [AuthModule, UserModule, ReviewModule, ScheduleModule.forRoot(),],
  controllers: [RestaurantController],
  providers: [RestaurantService, RestaurantRepository],
})
export class RestaurantModule { }
