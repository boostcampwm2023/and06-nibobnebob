import { Module } from "@nestjs/common";
import { RestaurantController } from "./restaurant.controller";
import { AuthModule } from "src/auth/auth.module";
import { RestaurantService } from "./restaurant.service";
import { RestaurantRepository } from "./restaurant.repository";
import { UserModule } from "src/user/user.module";

@Module({
  imports: [AuthModule, UserModule],
  controllers: [RestaurantController],
  providers: [RestaurantService, RestaurantRepository],
})
export class RestaurantModule {}
