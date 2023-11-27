import { Module } from "@nestjs/common";
import { UserController } from "./user.controller";
import { UserService } from "./user.service";
import { UserRepository } from "./user.repository";
import { AuthModule } from "../auth/auth.module";
import { forwardRef } from "@nestjs/common";
import { UserRestaurantListRepository } from "./user.restaurantList.repository";
import { UserFollowListRepository } from "./user.followList.repository";
import { ReviewModule } from "../review/review.module";


@Module({
  imports: [forwardRef(() => AuthModule), ReviewModule],
  controllers: [UserController],
  providers: [UserService, UserRepository, UserRestaurantListRepository, UserFollowListRepository],
  exports: [UserRepository, UserRestaurantListRepository],
})
export class UserModule { }
