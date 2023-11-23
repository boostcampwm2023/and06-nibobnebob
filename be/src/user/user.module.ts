import { Module } from "@nestjs/common";
import { UserController } from "./user.controller";
import { UserService } from "./user.service";
import { UserRepository } from "./user.repository";
import { AuthModule } from "../auth/auth.module";
import { forwardRef } from "@nestjs/common";
import { UserRestaurantListRepository } from "./user.restaurantList.repository";
import { UserFollowListRepository } from "./user.followList.repository";


@Module({
  imports: [forwardRef(() => AuthModule)],
  controllers: [UserController],
   providers: [UserService, UserRepository, UserRestaurantListRepository, UserFollowListRepository],
  exports: [UserRepository, UserRestaurantListRepository],
})
export class UserModule { }
