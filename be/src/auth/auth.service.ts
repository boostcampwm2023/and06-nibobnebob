import { Injectable, NotFoundException } from '@nestjs/common';
import { SocialLoginDto } from './dto/socialLogin.dto';
import { UserRepository } from 'src/user/user.repository';
import { JwtService } from '@nestjs/jwt';

@Injectable()
export class AuthService {
    constructor(
        private userRepository: UserRepository,
        private jwtService: JwtService
    ){}
    async signin(loginRequestUser: any){

        const user = await this.userRepository.findOneBy({ nickName: loginRequestUser.email })

        if(user){
            const payload = {nickName:user.nickName};
            const accessToken = this.jwtService.sign(payload);

            return accessToken;
        } else{
            throw new NotFoundException("사용자가 등록되어 있지 않습니다. 회원가입을 진행해주세요")
        }

    }
}
