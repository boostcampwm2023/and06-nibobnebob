import { Body, Controller, Post, Req, UseGuards, ValidationPipe } from '@nestjs/common';
import { SocialLoginDto } from './dto/socialLogin.dto';
import { AuthService } from './auth.service';
import { AuthGuard } from '@nestjs/passport';
import { Request } from 'express';

@Controller('auth')
export class AuthController {
    constructor(private authService: AuthService){}

    @Post('social-login')
    @UseGuards(AuthGuard('naver'))
    signin(@Req() req: Request){
        const user = req.user;
        return this.authService.signin(user);
    }
}
