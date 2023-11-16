import { Body, Controller, Post, ValidationPipe } from '@nestjs/common';
import { SocialLoginDto } from './dto/socialLogin.dto';
import { AuthService } from './auth.service';

@Controller('auth')
export class AuthController {
    constructor(private authService: AuthService){}

    @Post('social-login')
    signin(@Body(ValidationPipe) socialLoginDto: SocialLoginDto){
        return this.authService.signin(socialLoginDto);
    }
}
