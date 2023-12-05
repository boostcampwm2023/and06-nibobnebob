import { Controller, Get, Param, Post, Query, UseGuards, UsePipes, ValidationPipe } from '@nestjs/common';
import { ReviewService } from './review.service';
import { ApiBearerAuth, ApiOperation, ApiParam, ApiQuery, ApiResponse, ApiTags } from '@nestjs/swagger';
import { AuthGuard } from '@nestjs/passport';
import { GetUser, TokenInfo } from 'src/user/user.decorator';
import { SortInfoDto } from 'src/utils/sortInfo.dto';

@ApiTags("Review")
@Controller('review')
export class ReviewController {
    constructor(private reviewService: ReviewService) { }

    @Get("/:restaurantId")
    @UseGuards(AuthGuard("jwt"))
    @ApiBearerAuth()
    @ApiQuery({ name: 'sort', required: false, description: '정렬 기준' })
    @ApiQuery({ name: 'page', required: false, description: '페이지 번호' })
    @ApiQuery({ name: 'limit', required: false, description: '페이지 당 항목 수' })

    @ApiOperation({ summary: "리뷰 정렬 요청" })
    @ApiResponse({ status: 200, description: "리뷰 정렬 요청 성공" })
    @ApiResponse({ status: 401, description: "인증 실패" })
    @ApiResponse({ status: 400, description: "부적절한 요청" })
    @UsePipes(new ValidationPipe())
    async getSortedReviews(
        @GetUser() tokenInfo: TokenInfo,
        @Param('restaurantId') restaurantId: string,
        @Query() getSortedReviewsDto: SortInfoDto
    ) {
        const restaurantNumber = parseInt(restaurantId, 10);
        return await this.reviewService.getSortedReviews(tokenInfo, restaurantNumber, getSortedReviewsDto);
    }

    @Post("/:reviewId/like")
    @UseGuards(AuthGuard("jwt"))
    @ApiBearerAuth()
    @ApiOperation({ summary: "리뷰 좋아요 요청" })
    @ApiResponse({ status: 200, description: "리뷰 좋아요 요청 성공" })
    @ApiResponse({ status: 401, description: "인증 실패" })
    @ApiResponse({ status: 400, description: "부적절한 요청" })
    async reviewLike(
        @GetUser() tokenInfo: TokenInfo,
        @Param("reviewId") reviewid: number
    ) {
        return await this.reviewService.reviewLike(
            tokenInfo,
            reviewid
        );
    }

    @Post("/:reviewId/unlike")
    @UseGuards(AuthGuard("jwt"))
    @ApiBearerAuth()
    @ApiOperation({ summary: "리뷰 싫어요 요청" })
    @ApiResponse({ status: 200, description: "리뷰 싫어요 요청 성공" })
    @ApiResponse({ status: 401, description: "인증 실패" })
    @ApiResponse({ status: 400, description: "부적절한 요청" })
    async reviewUnLike(
        @GetUser() tokenInfo: TokenInfo,
        @Param("reviewId") reviewid: number
    ) {
        return await this.reviewService.reviewUnLike(
            tokenInfo,
            reviewid
        );
    }
}
