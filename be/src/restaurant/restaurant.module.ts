import { Module } from "@nestjs/common";
import { RestaurantController } from "./restaurant.controller";
import { AuthModule } from "../auth/auth.module";
import { RestaurantService } from "./restaurant.service";
import { RestaurantRepository } from "./restaurant.repository";
import { UserModule } from "../user/user.module";
import { ReviewModule } from "../review/review.module";
import { ScheduleModule } from '@nestjs/schedule';
//import { ElasticsearchService } from "./elasticSearch.service";

@Module({
  imports: [AuthModule, UserModule, ReviewModule, ScheduleModule.forRoot(),],
  controllers: [RestaurantController],
  providers: [RestaurantService, RestaurantRepository],
})
export class RestaurantModule { }
