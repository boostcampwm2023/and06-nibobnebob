import {
  Body,
  Controller,
  Get,
  Param,
  Post,
  UsePipes,
  ValidationPipe,
} from "@nestjs/common";
import { ApiOperation, ApiParam, ApiResponse } from "@nestjs/swagger";
import { UserInfoDto } from "./dto/userInfo.dto";
import { UserService } from "./user.service";

@Controller("user")
export class UserController {
  constructor(private userService: UserService) {}

  @Get("nickname/:nickname/exists")
  @ApiParam({ name: 'nickname', required: true, description: '확인하고자 하는 닉네임', type: String })
  @ApiOperation({ summary: "닉네임 중복확인" })
  @ApiResponse({ status: 200, description: "닉네임 중복확인 요청 성공" })
  @ApiResponse({ status: 400, description: "부적절한 요청" })
  async getNickNameAvailability(@Param('nickname') nickname: UserInfoDto["nickName"]) {
    return await this.userService.getNickNameAvailability(nickname);
  }

  @Post()
  @ApiOperation({ summary: "유저 회원가입" })
  @ApiResponse({ status: 200, description: "회원가입 성공" })
  @ApiResponse({ status: 400, description: "부적절한 요청" })
  @UsePipes(new ValidationPipe())
  async singup(@Body() userInfoDto: UserInfoDto) {
    return await this.userService.signup(userInfoDto);
  }
}
