import { Module } from "@nestjs/common";
import { UserController } from "./user.controller";
import { UserService } from "./user.service";
import { UserRepository } from "./user.repository";
import { AuthModule } from "../auth/auth.module";
import { forwardRef } from "@nestjs/common";
import { UserRestaurantListRepository } from "./user.restaurantList.repository";

@Module({
  imports: [forwardRef(() => AuthModule)],
  controllers: [UserController],
  providers: [UserService, UserRepository, UserRestaurantListRepository],
  exports: [UserRepository],
})
export class UserModule { }
