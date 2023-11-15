import {
  Body,
  Controller,
  Get,
  Post,
  UsePipes,
  ValidationPipe,
} from "@nestjs/common";
import { UserInfoDto } from "./dto/userInfo.dto";
import { UserService } from "./user.service";

@Controller("user")
export class UserController {
  constructor(private userService: UserService) {}

  @Post()
  @UsePipes(new ValidationPipe())
  singup(@Body() userInfoDto: UserInfoDto) {
    try {
      const result = this.userService.signup(userInfoDto);
      return { message: "User created successfully", data: result };
    } catch (error) {
      return { message: "User creation failed", error: error.message };
    }
  }
}
