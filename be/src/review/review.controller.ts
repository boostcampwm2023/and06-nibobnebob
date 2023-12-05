import { Controller, Param, Post, UseGuards } from '@nestjs/common';
import { ReviewService } from './review.service';
import { ApiBearerAuth, ApiOperation, ApiParam, ApiResponse, ApiTags } from '@nestjs/swagger';
import { AuthGuard } from '@nestjs/passport';
import { GetUser, TokenInfo } from 'src/user/user.decorator';

@Controller('review')
export class ReviewController {
    constructor(private reviewService: ReviewService) { }

    @ApiTags("Review")
    @Post("/:reviewId/like")
    @UseGuards(AuthGuard("jwt"))
    @ApiBearerAuth()
    @ApiOperation({ summary: "리뷰 좋아요 요청" })
    @ApiResponse({ status: 200, description: "위시 맛집리스트 등록 성공" })
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
}
