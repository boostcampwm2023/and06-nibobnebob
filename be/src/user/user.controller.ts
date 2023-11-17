import {
  Body,
  Controller,
  Get,
  Param,
  Post,
  Put,
  Delete,
  UsePipes,
  ValidationPipe,
  UseGuards,
} from "@nestjs/common";
import {
  ApiBearerAuth,
  ApiOperation,
  ApiParam,
  ApiResponse,
} from "@nestjs/swagger";
import { UserInfoDto } from "./dto/userInfo.dto";
import { UserService } from "./user.service";
import { GetUser, TokenInfo } from "./user.decorator";
import { AuthGuard } from "@nestjs/passport";

@Controller("user")
export class UserController {
  constructor(private userService: UserService) { }

  @Get(":nickname/details")
  @UseGuards(AuthGuard("jwt"))
  @ApiBearerAuth()
  @ApiParam({
    name: "nickname",
    required: true,
    description: "요청하고자 하는 유저의 닉네임",
    type: String,
  })
  @ApiOperation({ summary: "유저 정보 가져오기" })
  @ApiResponse({ status: 200, description: "정보 요청 성공" })
  @ApiResponse({ status: 401, description: "인증 실패" })
  @ApiResponse({ status: 400, description: "부적절한 요청" })
  async getUserInfo(@Param("nickname") nickname: UserInfoDto["nickName"]) {
    return await this.userService.getUserInfo(nickname);
  }

  @Get()
  @UseGuards(AuthGuard("jwt"))
  @ApiBearerAuth()
  @ApiOperation({ summary: "마이페이지 유저 수정페이지 정보 가져오기" })
  @ApiResponse({
    status: 200,
    description: "마이페이지 수정페이지 정보 요청 성공",
  })
  @ApiResponse({ status: 401, description: "인증 실패" })
  @ApiResponse({ status: 400, description: "부적절한 요청" })
  async getMypageUserDetailInfo(@GetUser() tokenInfo: TokenInfo) {
    return await this.userService.getMypageUserDetailInfo(tokenInfo);
  }

  @Get("nickname/:nickname/exists")
  @ApiParam({
    name: "nickname",
    required: true,
    description: "확인하고자 하는 닉네임",
    type: String,
  })
  @ApiOperation({ summary: "닉네임 중복확인" })
  @ApiResponse({ status: 200, description: "닉네임 중복확인 요청 성공" })
  @ApiResponse({ status: 400, description: "부적절한 요청" })
  async getNickNameAvailability(
    @Param("nickname") nickname: UserInfoDto["nickName"]
  ) {
    return await this.userService.getNickNameAvailability(nickname);
  }

  @Get("email/:email/exists")
  @ApiParam({
    name: "email",
    required: true,
    description: "확인하고자 하는 이메일",
    type: String,
  })
  @ApiOperation({ summary: "이메일 중복확인" })
  @ApiResponse({ status: 200, description: "이메일 중복확인 요청 성공" })
  @ApiResponse({ status: 400, description: "부적절한 요청" })
  async getEmailAvailability(
    @Param("email") email: UserInfoDto["email"]
  ) {
    return await this.userService.getEmailAvailability(email);
  }

  @Post()
  @ApiOperation({ summary: "유저 회원가입" })
  @ApiResponse({ status: 200, description: "회원가입 성공" })
  @ApiResponse({ status: 400, description: "부적절한 요청" })
  @UsePipes(new ValidationPipe())
  async singup(@Body() userInfoDto: UserInfoDto) {
    return await this.userService.signup(userInfoDto);
  }

  @Delete()
  @UseGuards(AuthGuard("jwt"))
  @ApiBearerAuth()
  @ApiOperation({ summary: "유저 회원탈퇴" })
  @ApiResponse({ status: 200, description: "회원탈퇴 성공" })
  @ApiResponse({ status: 401, description: "인증 실패" })
  @ApiResponse({ status: 400, description: "부적절한 요청" })
  @UsePipes(new ValidationPipe())
  async deleteUserAccount(@GetUser() tokenInfo: TokenInfo) {
    return await this.userService.deleteUserAccount(tokenInfo);
  }

  @Put()
  @UseGuards(AuthGuard("jwt"))
  @ApiBearerAuth()
  @ApiOperation({ summary: "유저 회원정보 수정" })
  @ApiResponse({ status: 200, description: "회원정보 수정 성공" })
  @ApiResponse({ status: 401, description: "인증 실패" })
  @ApiResponse({ status: 400, description: "부적절한 요청" })
  @UsePipes(new ValidationPipe())
  async updateMypageUserInfo(
    @GetUser() tokenInfo: TokenInfo,
    @Body() userInfoDto: UserInfoDto
  ) {
    return await this.userService.updateMypageUserInfo(tokenInfo, userInfoDto);
  }
}
