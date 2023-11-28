import {
  Controller,
  Get,
  Param,
  Query,
  UseGuards,
  UsePipes,
  ValidationPipe,
} from "@nestjs/common";
import { AuthGuard } from "@nestjs/passport";
import {
  ApiBearerAuth,
  ApiOperation,
  ApiParam,
  ApiQuery,
  ApiResponse,
} from "@nestjs/swagger";
import { RestaurantService } from "./restaurant.service";
import { SearchInfoDto } from "./dto/seachInfo.dto";
import { FilterInfoDto } from "./dto/filterInfo.dto";
import { GetUser, TokenInfo } from "src/user/user.decorator";
import { LocationDto } from "./dto/location.dto";

@Controller("restaurant")
export class RestaurantController {
  constructor(private restaurantService: RestaurantService) {}
  @Get("autocomplete/:partialRestaurantName")
  @UseGuards(AuthGuard("jwt"))
  @ApiBearerAuth()
  @ApiOperation({ summary: "음식점 검색 자동완성" })
  @ApiParam({
    name: "partialRestaurantName",
    required: true,
    type: String,
    description: "부분 음식점 이름",
  })
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
  @ApiResponse({
    status: 200,
    description: "음식점 검색 성공",
  })
  @ApiResponse({ status: 401, description: "인증 실패" })
  @ApiResponse({ status: 404, description: "존재하지 않는 음식점" })
  @UsePipes(new ValidationPipe())
  searchRestaurant(
    @GetUser() tokenInfo: TokenInfo,
    @Query() locationDto: LocationDto,
    @Param("partialRestaurantName") partialName: string
  ) {
    const searchInfoDto = new SearchInfoDto(partialName, locationDto);
    return this.restaurantService.searchRestaurant(searchInfoDto, tokenInfo);
  }

  @Get(":restaurantId/details")
  @UseGuards(AuthGuard("jwt"))
  @ApiBearerAuth()
  @ApiOperation({ summary: "음식점 상세 페이지" })
  @ApiResponse({
    status: 200,
    description: "음식점 상세 페이지 요청 성공",
  })
  @ApiResponse({ status: 401, description: "인증 실패" })
  @ApiResponse({ status: 404, description: "존재하지 않는 음식점" })
  detailInfo(
    @GetUser() tokenInfo: TokenInfo,
    @Param("restaurantId") restaurantId: string
  ) {
    return this.restaurantService.detailInfo(parseInt(restaurantId), tokenInfo);
  }

  @Get()
  @UseGuards(AuthGuard("jwt"))
  @ApiBearerAuth()
  @ApiOperation({ summary: "필터링된 음식점 리스트 응답" })
  @ApiQuery({
    name: "filter",
    required: true,
    type: String,
    description: "팔로우한 유저의 nickName",
  })
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
  @ApiResponse({
    status: 200,
    description: "필터링된 음식점 리스트 요청 성공",
  })
  @ApiResponse({ status: 401, description: "인증 실패" })
  @ApiResponse({
    status: 400,
    description: "쿼리파라미터 형식이 올바르지 않음",
  })
  @ApiResponse({
    status: 404,
    description: "존재하지 않는 필터(유저 닉네임)를 요청",
  })
  @UsePipes(new ValidationPipe())
  filteredRestaurantList(
    @GetUser() tokenInfo: TokenInfo,
    @Query() locationDto: LocationDto,
    @Query("filter") filter: string
  ) {
    const filterInfoDto = new FilterInfoDto(filter, locationDto);
    return this.restaurantService.filteredRestaurantList(
      filterInfoDto,
      tokenInfo
    );
  }
  @Get("all")
  @UseGuards(AuthGuard("jwt"))
  @ApiBearerAuth()
  @ApiOperation({ summary: "현위치 기반 반경 내의 전체 음식점 리스트 응답" })
  @ApiQuery({
    name: "latitude",
    required: true,
    type: String,
    description: "위도",
  })
  @ApiQuery({
    name: "longitude",
    required: true,
    type: String,
    description: "경도",
  })
  @ApiQuery({
    name: "radius",
    required: true,
    type: String,
    description: "검색 반경",
  })
  @ApiResponse({
    status: 200,
    description: "전체 음식점 리스트 요청 성공",
  })
  @ApiResponse({ status: 401, description: "인증 실패" })
  @ApiResponse({
    status: 400,
    description: "쿼리파라미터 형식이 올바르지 않음",
  })
  @UsePipes(new ValidationPipe())
  entireRestaurantList(
    @GetUser() tokenInfo: TokenInfo,
    @Query() locationDto: LocationDto
  ) {
    return this.restaurantService.entireRestaurantList(locationDto, tokenInfo);
  }
}
