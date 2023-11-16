import { Injectable } from '@nestjs/common';
import { SocialLoginDto } from './dto/socialLogin.dto';

@Injectable()
export class AuthService {
    signin(socialLoginDto: SocialLoginDto){
        return;
    }
}
