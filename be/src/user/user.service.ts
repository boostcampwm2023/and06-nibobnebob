import { Injectable } from "@nestjs/common";
import { UserInfoDto } from "./dto/userInfo.dto";
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { User } from './entities/user.entity';
import { UserRepository } from "./user.repository";

@Injectable()
export class UserService {
    constructor(
        @InjectRepository(UserRepository)
        private usersRepository: UserRepository,
      ) {}
    signup(userInfoDto: UserInfoDto) {
        this.usersRepository.createUser(userInfoDto);
        return;
    }
}
