import {
  Injectable,
  NotFoundException,
  HttpException,
  HttpStatus,
  BadRequestException,
  UnauthorizedException,
} from "@nestjs/common";
import { UserRepository } from "../user/user.repository";
import { JwtService } from "@nestjs/jwt";
import axios from "axios";
import { LoginInfoDto } from "./dto/loginInfo.dto";
import { comparePasswords } from "src/utils/encryption.utils";
import { AuthRepository } from "./auth.repository";

@Injectable()
export class AuthService {
  constructor(
    private userRepository: UserRepository,
    private jwtService: JwtService,
    private authRepository: AuthRepository
  ) { }
  async login(loginInfoDto: LoginInfoDto) {
    const data = await this.userRepository.findOne({ select: ["password"], where: { email: loginInfoDto.email, provider: "site" } })
    try {
      const result = await comparePasswords(loginInfoDto.password, data["password"]);
      if (result) return this.signin(loginInfoDto);
      else throw new UnauthorizedException();
    } catch (err) {
      throw new UnauthorizedException();
    }
  }

  async logout(id: number) {
    await this.authRepository.delete({ id: id });
  }

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
      const payload = { id: user.id };
      const accessToken = this.jwtService.sign(payload);
      const refreshToken = this.jwtService.sign(payload, {
        secret: "nibobnebob",
        expiresIn: "7d",
      });
      await this.authRepository.upsert({ id: user.id, accessToken: accessToken, refreshToken: refreshToken }, ["id"]);
      return { accessToken, refreshToken };
    } else {
      throw new NotFoundException(
        "사용자가 등록되어 있지 않습니다. 회원가입을 진행해주세요"
      );
    }
  }

  async checkRefreshToken(refreshToken: string) {
    try {
      const decoded = this.jwtService.verify(refreshToken, {
        secret: "nibobnebob",
      });
      if (await this.authRepository.findOne({ where: { id: decoded.id, refreshToken: refreshToken } })) {
        const payload = { id: decoded.id };
        const accessToken = this.jwtService.sign(payload);
        await this.authRepository.update(decoded.id, { accessToken: accessToken });
        return { accessToken };
      }
      throw new HttpException("Invalid refresh token", HttpStatus.UNAUTHORIZED);
    } catch (err) {
      throw new HttpException("Invalid refresh token", HttpStatus.UNAUTHORIZED);
    }
  }
}
