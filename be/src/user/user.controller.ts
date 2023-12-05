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
  Query,
  UseInterceptors,
  UploadedFile,
  BadRequestException,
} from "@nestjs/common";
import {
  ApiBearerAuth,
  ApiBody,
  ApiConsumes,
  ApiOperation,
  ApiParam,
  ApiQuery,
  ApiResponse,
  ApiTags,
} from "@nestjs/swagger";
import { UserInfoDto } from "./dto/userInfo.dto";
import { UserService } from "./user.service";
import { GetUser, TokenInfo } from "./user.decorator";
import { AuthGuard } from "@nestjs/passport";
import { SearchInfoDto } from "../restaurant/dto/seachInfo.dto";
import { LocationDto } from "src/restaurant/dto/location.dto";
import { ReviewInfoDto } from "src/review/dto/reviewInfo.dto";
import { ParseArrayPipe } from "../utils/parsearraypipe";
import { FileInterceptor } from "@nestjs/platform-express";
import { memoryStorage } from 'multer';
import { plainToClass } from "class-transformer";
import { validate } from "class-validator";
import { SortInfoDto } from "src/utils/sortInfo.dto";

const multerOptions = {
  storage: memoryStorage(),
};


@Controller("user")
export class UserController {
  constructor(private userService: UserService) { }

  @ApiTags("Mypage")
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

  @ApiTags("Mypage")
  @Get("/details")
  @UseGuards(AuthGuard("jwt"))
  @ApiBearerAuth()
  @ApiOperation({ summary: "마이페이지 유저 정보 가져오기" })
  @ApiResponse({ status: 200, description: "마이페이지 정보 요청 성공" })
  @ApiResponse({ status: 401, description: "인증 실패" })
  @ApiResponse({ status: 400, description: "부적절한 요청" })
  async getMypageUserInfo(@GetUser() tokenInfo: TokenInfo) {
    return await this.userService.getMypageUserInfo(tokenInfo);
  }

  @ApiTags("Follow/Following")
  @Get(":nickName/details")
  @UseGuards(AuthGuard("jwt"))
  @ApiBearerAuth()
  @ApiOperation({ summary: "다른 유저 메인 마이페이지 유저 정보 가져오기" })
  @ApiResponse({
    status: 200,
    description: "다른 유저 메인 마이페이지 정보 요청 성공",
  })
  @ApiResponse({ status: 401, description: "인증 실패" })
  @ApiResponse({ status: 400, description: "부적절한 요청" })
  async getMypageTargetUserInfo(
    @GetUser() tokenInfo: TokenInfo,
    @Param("nickName") nickName: string
  ) {
    return await this.userService.getMypageTargetUserInfo(tokenInfo, nickName);
  }

  @ApiTags("Follow/Following")
  @Get("/autocomplete/:partialUsername")
  @UseGuards(AuthGuard("jwt"))
  @ApiQuery({
    name: 'region',
    required: false,
    type: String,
    isArray: true,
    description: '필터링할 지역 목록을 쉼표(,)로 구분하여 제공'
  })
  @ApiBearerAuth()
  @ApiOperation({ summary: "다른 유저 검색 자동완성" })
  @ApiResponse({ status: 200, description: "다른 유저 검색 자동완성 완성" })
  @ApiResponse({ status: 401, description: "인증 실패" })
  @ApiResponse({ status: 400, description: "부적절한 요청" })
  async searchTargetUser(
    @GetUser() tokenInfo: TokenInfo,
    @Param("partialUsername") partialUsername: string,
    @Query("region", ParseArrayPipe) region: string[]
  ) {
    return await this.userService.searchTargetUser(tokenInfo, partialUsername, region);
  }

  @ApiTags("Signup", "Mypage")
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

  @ApiTags("Signup", "Mypage")
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
  async getEmailAvailability(@Param("email") email: UserInfoDto["email"]) {
    return await this.userService.getEmailAvailability(email);
  }

  @ApiTags("Mypage", "Home")
  @Get("/restaurant")
  @UseGuards(AuthGuard("jwt"))
  @ApiBearerAuth()
  @ApiOperation({ summary: "내 맛집 리스트 정보 가져오기" })
  @ApiQuery({
    name: "latitude",
    required: false,
    type: String,
    description: "위도",
  })
  @ApiQuery({
    name: "longitude",
    required: false,
    type: String,
    description: "경도",
  })
  @ApiQuery({
    name: "radius",
    required: false,
    type: String,
    description: "검색 반경",
  })
  @ApiQuery({
    name: "sort",
    required: false,
    type: String,
    description: "선택된 필터",
  })
  @ApiQuery({ name: 'page', required: false, description: '페이지 번호' })
  @ApiQuery({ name: 'limit', required: false, description: '페이지 당 항목 수' })
  @ApiResponse({ status: 200, description: "내 맛집 리스트 정보 요청 성공" })
  @ApiResponse({ status: 401, description: "인증 실패" })
  @ApiResponse({ status: 400, description: "부적절한 요청" })
  @UsePipes(new ValidationPipe())
  async getMyRestaurantListInfo(
    @Query() locationDto: LocationDto,
    @Query() sortInfoDto: SortInfoDto,
    @GetUser() tokenInfo: TokenInfo
  ) {
    const searchInfoDto = new SearchInfoDto("", locationDto);
    return await this.userService.getMyRestaurantListInfo(
      searchInfoDto,
      sortInfoDto,
      tokenInfo
    );
  }

  @ApiTags("Mypage")
  @Get("/wish-restaurant")
  @UseGuards(AuthGuard("jwt"))
  @ApiBearerAuth()
  @ApiOperation({ summary: "내 위시 맛집 리스트 정보 가져오기" })
  @ApiQuery({
    name: "sort",
    required: false,
    type: String,
    description: "선택된 필터",
  })
  @ApiQuery({ name: 'page', required: false, description: '페이지 번호' })
  @ApiQuery({ name: 'limit', required: false, description: '페이지 당 항목 수' })
  @ApiResponse({ status: 200, description: "내 맛집 리스트 정보 요청 성공" })
  @ApiResponse({ status: 401, description: "인증 실패" })
  async getMyWishRestaurantListInfo(@GetUser() tokenInfo: TokenInfo,  @Query() sortInfoDto: SortInfoDto) {
    return await this.userService.getMyWishRestaurantListInfo(tokenInfo,sortInfoDto);
  }

  @ApiTags("Home")
  @Get("/state/wish-restaurant")
  @UseGuards(AuthGuard("jwt"))
  @ApiBearerAuth()
  @ApiOperation({ summary: "위시 맛집리스트 포함 여부 정보 가져오기" })
  @ApiResponse({ status: 200, description: "위시 맛집리스트 포함 여부 정보 요청 성공" })
  @ApiResponse({ status: 401, description: "인증 실패" })
  async getStateIsWish(@GetUser() tokenInfo: TokenInfo, @Query("restaurantid") restaurantid: number) {
    return await this.userService.getStateIsWish(tokenInfo, restaurantid);
  }

  @ApiTags("Follow/Following", "Home")
  @Get("follow-list")
  @UseGuards(AuthGuard("jwt"))
  @ApiBearerAuth()
  @ApiOperation({ summary: "내 팔로우 리스트 정보 가져오기" })
  @ApiResponse({ status: 200, description: "내 팔로우 리스트 정보 요청 성공" })
  @ApiResponse({ status: 401, description: "인증 실패" })
  @ApiResponse({ status: 400, description: "부적절한 요청" })
  async getMyFollowListInfo(@GetUser() tokenInfo: TokenInfo) {
    return await this.userService.getMyFollowListInfo(tokenInfo);
  }

  @ApiTags("Follow/Following")
  @Get("followed-list")
  @UseGuards(AuthGuard("jwt"))
  @ApiBearerAuth()
  @ApiOperation({ summary: "내 팔로워 리스트 정보 가져오기" })
  @ApiResponse({ status: 200, description: "내 팔로워 리스트 정보 요청 성공" })
  @ApiResponse({ status: 401, description: "인증 실패" })
  @ApiResponse({ status: 400, description: "부적절한 요청" })
  async getMyFollowerListInfo(@GetUser() tokenInfo: TokenInfo) {
    return await this.userService.getMyFollowerListInfo(tokenInfo);
  }

  @ApiTags("Follow/Following")
  @Get("recommended")
  @UseGuards(AuthGuard("jwt"))
  @ApiBearerAuth()
  @ApiOperation({ summary: "추천 사용자 정보 가져오기" })
  @ApiResponse({ status: 200, description: "추천 사용자 정보 요청 성공" })
  @ApiResponse({ status: 401, description: "인증 실패" })
  async getRecommendUserListInfo(@GetUser() tokenInfo: TokenInfo) {
    return await this.userService.getRecommendUserListInfo(tokenInfo);
  }

  @ApiTags("Signup")
  @Post()
  @UseInterceptors(FileInterceptor('profileImage', multerOptions))
  @ApiOperation({ summary: "유저 회원가입" })
  @ApiResponse({ status: 200, description: "회원가입 성공" })
  @ApiResponse({ status: 400, description: "부적절한 요청" })
  @ApiBody({
    schema: {
      type: 'object',
      description: "회원가입",
      required: ['email', 'provider', 'nickName', 'region', 'birthdate', 'isMale'],
      properties: {
        email: { type: 'string', example: 'user@example.com', description: 'The email of the user' },
        password: { type: 'string', example: '1234', description: 'The password of the user' },
        provider: { type: 'string', example: 'naver', description: 'The provider of the user' },
        nickName: { type: 'string', example: 'test', description: 'The nickname of the user' },
        region: { type: 'string', example: '강동구', description: 'The region of the user' },
        birthdate: { type: 'string', example: '1234/56/78', description: 'The birth of the user' },
        isMale: { type: 'boolean', example: true, description: 'The gender of the user. true is male, false is female' },
        profileImage: { type: 'string', format: 'binary', description: 'The profile image of the user' },
      },
    },
  })
  @ApiConsumes('multipart/form-data')
  async singup(@Body() body, @UploadedFile() file: Express.Multer.File) {
    const userInfoDto = plainToClass(UserInfoDto, body);
    const errors = await validate(userInfoDto);
    if (errors.length > 0) {
      throw new BadRequestException(errors);
    }
    return await this.userService.signup(file, userInfoDto);
  }

  @ApiTags("Follow/Following")
  @Post("follow-list/:nickName")
  @ApiParam({
    name: "nickName",
    required: true,
    description: "팔로우 할 유저의 닉네임",
    type: String,
  })
  @UseGuards(AuthGuard("jwt"))
  @ApiBearerAuth()
  @ApiOperation({ summary: "유저 팔로우 하기" })
  @ApiResponse({ status: 200, description: "유저 팔로우 성공" })
  @ApiResponse({ status: 400, description: "부적절한 요청" })
  @ApiResponse({ status: 401, description: "인증 실패" })
  @UsePipes(new ValidationPipe())
  async followUser(
    @GetUser() tokenInfo: TokenInfo,
    @Param("nickName") nickName: string
  ) {
    return await this.userService.followUser(tokenInfo, nickName);
  }

  @ApiTags("RestaurantList")
  @Post("/restaurant/:restaurantid")
  @UseInterceptors(FileInterceptor('reviewImage', multerOptions))
  @UseGuards(AuthGuard("jwt"))
  @ApiBearerAuth()
  @ApiOperation({ summary: "내 맛집 리스트에 등록하기" })
  @ApiParam({
    name: "restaurantid",
    required: true,
    description: "음식점 id",
    type: Number,
  })
  @ApiBody({
    schema: {
      type: 'object',
      description: "리뷰 등록하기",
      required: ['isCarVisit', 'taste', 'service', 'restroomCleanliness', 'overallExperience'],
      properties: {
        isCarVisit: { type: 'boolean', example: true, description: 'The transportation for visiting' },
        transportationAccessibility: {
          type: 'integer',
          example: 0,
          description: 'Transportation accessibility for visiting',
          minimum: 0,
          maximum: 4
        },
        parkingArea: {
          type: 'integer',
          example: 0,
          description: "Condition of the restaurant's parking area",
          minimum: 0,
          maximum: 4
        },
        taste: {
          type: 'integer',
          example: 0,
          description: 'The taste of the food',
          minimum: 0,
          maximum: 4
        },
        service: {
          type: 'integer',
          example: 0,
          description: 'The service of the restaurant',
          minimum: 0,
          maximum: 4
        },
        restroomCleanliness: {
          type: 'integer',
          example: 0,
          description: "The condition of the restaurant's restroom",
          minimum: 0,
          maximum: 4
        },
        overallExperience: {
          type: 'string',
          example: '20자 이상 작성하기',
          description: 'The overall experience about the restaurant',
          minLength: 20
        },
        reviewImage: { type: 'string', format: 'binary', description: 'The image of food' },
      },
    },
  })
  @ApiResponse({ status: 200, description: "맛집리스트 등록 성공" })
  @ApiResponse({ status: 401, description: "인증 실패" })
  @ApiResponse({ status: 400, description: "부적절한 요청" })
  @ApiConsumes('multipart/form-data')
  async addRestaurantToNebob(
    @Body() body,
    @GetUser() tokenInfo: TokenInfo,
    @Param("restaurantid") restaurantid: number,
    @UploadedFile() file: Express.Multer.File
  ) {
    const reviewInfoDto = plainToClass(ReviewInfoDto, body);
    const errors = await validate(reviewInfoDto);
    if (errors.length > 0) throw new BadRequestException(errors);
    return await this.userService.addRestaurantToNebob(
      reviewInfoDto,
      tokenInfo,
      restaurantid,
      file
    );
  }

  @ApiTags("RestaurantList")
  @Delete("/restaurant/:restaurantid")
  @UseGuards(AuthGuard("jwt"))
  @ApiBearerAuth()
  @ApiOperation({ summary: "내 맛집 리스트에서 삭제하기" })
  @ApiResponse({ status: 200, description: "맛집리스트 삭제 성공" })
  @ApiResponse({ status: 401, description: "인증 실패" })
  @ApiResponse({ status: 400, description: "부적절한 요청" })
  async deleteRestaurantFromNebob(
    @GetUser() tokenInfo: TokenInfo,
    @Param("restaurantid") restaurantid: number
  ) {
    return await this.userService.deleteRestaurantFromNebob(
      tokenInfo,
      restaurantid
    );
  }

  @ApiTags("WishRestaurantList")
  @Post("/wish-restaurant/:restaurantid")
  @UseGuards(AuthGuard("jwt"))
  @ApiBearerAuth()
  @ApiOperation({ summary: "내 위시 맛집 리스트에 등록하기" })
  @ApiParam({
    name: "restaurantid",
    required: true,
    description: "음식점 id",
    type: Number,
  })
  @ApiResponse({ status: 200, description: "위시 맛집리스트 등록 성공" })
  @ApiResponse({ status: 401, description: "인증 실패" })
  @ApiResponse({ status: 400, description: "부적절한 요청" })
  async addRestaurantToWishNebob(
    @GetUser() tokenInfo: TokenInfo,
    @Param("restaurantid") restaurantid: number
  ) {
    return await this.userService.addRestaurantToWishNebob(
      tokenInfo,
      restaurantid
    );
  }

  @ApiTags("WishRestaurantList")
  @Delete("/wish-restaurant/:restaurantid")
  @UseGuards(AuthGuard("jwt"))
  @ApiBearerAuth()
  @ApiOperation({ summary: "내 위시 맛집 리스트에서 삭제하기" })
  @ApiResponse({ status: 200, description: "위시 맛집리스트 삭제 성공" })
  @ApiResponse({ status: 401, description: "인증 실패" })
  @ApiResponse({ status: 400, description: "부적절한 요청" })
  async deleteRestaurantFromWishNebob(
    @GetUser() tokenInfo: TokenInfo,
    @Param("restaurantid") restaurantid: number
  ) {
    return await this.userService.deleteRestaurantFromWishNebob(
      tokenInfo,
      restaurantid
    );
  }

  @ApiTags("Mypage")
  @Post("logout")
  @UseGuards(AuthGuard("jwt"))
  @ApiBearerAuth()
  @ApiOperation({ summary: "유저 로그아웃" })
  @ApiResponse({ status: 200, description: "로그아웃 성공" })
  @ApiResponse({ status: 401, description: "인증 실패" })
  async logout(@GetUser() tokenInfo: TokenInfo) {
    return await this.userService.logout(tokenInfo);
  }

  @ApiTags("Mypage")
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

  @ApiTags("Follow/Following")
  @Delete("follow-list/:nickName")
  @ApiParam({
    name: "nickName",
    required: true,
    description: "언팔로우 할 유저의 닉네임",
    type: String,
  })
  @UseGuards(AuthGuard("jwt"))
  @ApiBearerAuth()
  @ApiOperation({ summary: "유저 언팔로우 하기" })
  @ApiResponse({ status: 200, description: "유저 언팔로우 성공" })
  @ApiResponse({ status: 400, description: "부적절한 요청" })
  @ApiResponse({ status: 401, description: "인증 실패" })
  @UsePipes(new ValidationPipe())
  async unfollowUser(
    @GetUser() tokenInfo: TokenInfo,
    @Param("nickName") nickName: string
  ) {
    return await this.userService.unfollowUser(tokenInfo, nickName);
  }

  @ApiTags("Mypage")
  @Put()
  @UseInterceptors(FileInterceptor('profileImage', multerOptions))
  @UseGuards(AuthGuard("jwt"))
  @ApiBearerAuth()
  @ApiBody({
    schema: {
      type: 'object',
      description: "회원정보 수정",
      required: ['email', 'provider', 'nickName', 'region', 'birthdate', 'isMale'],
      properties: {
        email: { type: 'string', example: 'user@example.com', description: 'The email of the user' },
        password: { type: 'string', example: '1234', description: 'The password of the user' },
        provider: { type: 'string', example: 'naver', description: 'The provider of the user' },
        nickName: { type: 'string', example: 'test', description: 'The nickname of the user' },
        region: { type: 'string', example: '강동구', description: 'The region of the user' },
        birthdate: { type: 'string', example: '1234/56/78', description: 'The birth of the user' },
        isMale: { type: 'boolean', example: true, description: 'The gender of the user. true is male, false is female' },
        profileImage: { type: 'string', format: 'binary', description: 'The profile image of the user' },
      },
    },
  })
  @ApiOperation({ summary: "유저 회원정보 수정" })
  @ApiResponse({ status: 200, description: "회원정보 수정 성공" })
  @ApiResponse({ status: 401, description: "인증 실패" })
  @ApiResponse({ status: 400, description: "부적절한 요청" })
  @ApiConsumes('multipart/form-data')
  async updateMypageUserInfo(
    @UploadedFile() file: Express.Multer.File,
    @GetUser() tokenInfo: TokenInfo,
    @Body() body
  ) {
    const userInfoDto = plainToClass(UserInfoDto, body);
    const errors = await validate(userInfoDto);
    if (errors.length > 0) throw new BadRequestException(errors);
    return await this.userService.updateMypageUserInfo(file, tokenInfo, userInfoDto);
  }
}
