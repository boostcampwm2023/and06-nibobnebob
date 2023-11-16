import {
  Body,
  Controller,
  Get,
  Post,
  UsePipes,
  ValidationPipe,
} from "@nestjs/common";
import { ApiOperation, ApiResponse } from "@nestjs/swagger";
import { UserInfoDto } from "./dto/userInfo.dto";
import { UserService } from "./user.service";

@Controller("user")
export class UserController {
  constructor(private userService: UserService) { }

  @Post()
  @ApiOperation({ summary: "유저 회원가입" })
  @ApiResponse({ status: 200, description: "회원가입 성공" })
  @ApiResponse({ status: 400, description: "부적절한 요청" })
  @UsePipes(new ValidationPipe())
  singup(@Body() userInfoDto: UserInfoDto) {
    return this.userService.signup(userInfoDto);
  }
}
