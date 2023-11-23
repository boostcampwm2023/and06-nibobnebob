import { Controller, Get, Param, Query, UseGuards } from "@nestjs/common";
import { AuthGuard } from "@nestjs/passport";
import { ApiBearerAuth, ApiOperation, ApiResponse } from "@nestjs/swagger";
import { RestaurantService } from "./restaurant.service";
import { SearchInfoDto } from "./dto/seachInfo.dto";

@Controller("restaurant")
export class RestaurantController {
  constructor(private restaurantService: RestaurantService) {}
  @Get("autocomplete/:partialRestaurantName")
  @UseGuards(AuthGuard("jwt"))
  @ApiBearerAuth()
  @ApiOperation({ summary: "음식점 검색 자동완성" })
  @ApiResponse({
    status: 200,
    description: "음식점 검색 성공",
  })
  @ApiResponse({ status: 401, description: "인증 실패" })
  @ApiResponse({ status: 404, description: "존재하지 않는 음식점" })
  searchRestaurant(
    @Param("partialRestaurantName") partialName: string,
    @Query("location") location: string,
    @Query("radius") radius: string
  ) {
    const searchInfoDto = new SearchInfoDto(partialName, location, radius);
    return this.restaurantService.searchRestaurant(searchInfoDto);
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
    @Param("restaurantId") restaurantId: string,
  ) {
    return this.restaurantService.detailInfo(parseInt(restaurantId));
  }
}
