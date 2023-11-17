import { Injectable } from "@nestjs/common";
import { UserInfoDto } from "./dto/userInfo.dto";
import { InjectRepository } from "@nestjs/typeorm";
import { UserRepository } from "./user.repository";
import { TokenInfo } from "./user.decorator";

@Injectable()
export class UserService {
  constructor(
    @InjectRepository(UserRepository)
    private usersRepository: UserRepository
  ) {}
  async signup(userInfoDto: UserInfoDto) {
    return await this.usersRepository.createUser(userInfoDto);
  }
  async getNickNameAvailability(nickName: UserInfoDto["nickName"]) {
    return await this.usersRepository.getNickNameAvailability(nickName);
  }
  async getUserInfo(tokenInfo: TokenInfo) {
    return await this.usersRepository.getMypageUserInfo(tokenInfo.nickName);
  }
  async getMypageUserDetailInfo(tokenInfo: TokenInfo) {
    return await this.usersRepository.getMypageUserDetailInfo(
      tokenInfo.nickName
    );
  }
}
