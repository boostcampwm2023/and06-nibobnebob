import { Controller, Get, Post, Body, UsePipes } from "@nestjs/common";
import { UserInfoDto } from "./dto/userInfo.dto";
import { UserService } from "./user.service";
import { validateHeaderName } from "http";

@Controller("user")
export class UserController {
  constructor(private userService:UserService) {}

  @Post()
  singup(@Body() userInfoDto : UserInfoDto) {
    try {
      const result = this.userService.signup(userInfoDto);
      return { message: 'User created successfully', data: result };
    } catch (error) {
      return { message: 'User creation failed', error: error.message };
    }
  }
}
