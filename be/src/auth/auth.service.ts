import {
  Injectable,
  NotFoundException,
  HttpException,
  HttpStatus,
} from "@nestjs/common";
import { UserRepository } from "src/user/user.repository";
import { JwtService } from "@nestjs/jwt";
import axios from "axios";

@Injectable()
export class AuthService {
  constructor(
    private userRepository: UserRepository,
    private jwtService: JwtService
  ) {}
  async NaverAuth(authorization: string) {
    if (!authorization) {
      throw new HttpException(
        "Authorization header is missing",
        HttpStatus.UNAUTHORIZED
      );
    }
    const accessToken = authorization.split(" ")[1];
    const headers = {
      Authorization: `Bearer ${accessToken}`,
    };

    try {
      const response = await axios.get("https://openapi.naver.com/v1/nid/me", {
        headers,
      });
      return this.signin(response.data.response);
    } catch (err) {
      throw new HttpException("Invalid access token", HttpStatus.UNAUTHORIZED);
    }
  }

  async signin(loginRequestUser: any) {
    const user = await this.userRepository.findOneBy({
      email: loginRequestUser.email,
    });

    if (user) {
      const payload = { nickName: user.nickName };
      const accessToken = this.jwtService.sign(payload);
      
      const refreshToken = this.jwtService.sign(payload, {
        secret: "nibobnebob", 
        expiresIn: '7d', 
      });

      return { accessToken, refreshToken };

    } else {
      throw new NotFoundException(
        "사용자가 등록되어 있지 않습니다. 회원가입을 진행해주세요"
      );
    }
  }
}
