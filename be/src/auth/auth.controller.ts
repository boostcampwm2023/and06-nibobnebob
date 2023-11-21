import {
  Body,
  Controller,
  Headers,
  Post,
  UseGuards,
  ValidationPipe,
} from "@nestjs/common";
import { AuthService } from "./auth.service";
import {
  ApiTags,
  ApiOperation,
  ApiResponse,
  ApiBearerAuth,
  ApiBody,
} from "@nestjs/swagger";
import { RefreshTokenDto } from "./dto/refreshToken.dto";

@Controller("auth")
export class AuthController {
  constructor(private authService: AuthService) {}

  @Post("social-login")
  @ApiOperation({ summary: "네이버 소셜 로그인" })
  @ApiResponse({ status: 200, description: "성공적으로 로그인됨." })
  @ApiResponse({ status: 401, description: "잘못된 access token." })
  @ApiResponse({
    status: 404,
    description: "회원정보 없음. 회원가입이 필요함.",
  })
  @ApiBearerAuth()
  signin(@Headers("authorization") authorization: string) {
    return this.authService.NaverAuth(authorization);
  }

  @Post("refresh-token")
  @ApiOperation({ summary: "accessToken 재발급" })
  @ApiResponse({ status: 200, description: "성공적으로 재발급됨." })
  @ApiResponse({ status: 401, description: "잘못된 refresh token." })
  @ApiBody({ type: RefreshTokenDto })
  checkRefreshToken(@Body() refreshTokenDto: RefreshTokenDto) {
    return this.authService.checkRefreshToken(refreshTokenDto.refreshToken);
  }
}
