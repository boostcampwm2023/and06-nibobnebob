import { Injectable } from "@nestjs/common";
import { UserInfoDto } from "./dto/userInfo.dto";
import { InjectRepository } from "@nestjs/typeorm";
import { UserRepository } from "./user.repository";

@Injectable()
export class UserService {
  constructor(
    @InjectRepository(UserRepository)
    private usersRepository: UserRepository
  ) { }
  signup(userInfoDto: UserInfoDto) {
    return this.usersRepository.createUser(userInfoDto);
  }
}
