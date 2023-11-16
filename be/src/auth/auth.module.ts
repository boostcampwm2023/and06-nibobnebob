import { Module } from '@nestjs/common';
import { AuthController } from './auth.controller';
import { AuthService } from './auth.service';
import { JwtModule } from '@nestjs/jwt';
import { PassportModule } from '@nestjs/passport';
import { UserModule } from 'src/user/user.module';

@Module({
  imports:[
    PassportModule.register({defaultStrategy: 'jwt'}),
    JwtModule.register({
      secret: "nibobnebob",
      signOptions: {
        expiresIn: 3600,
      }
    }),
    UserModule
  ],
  controllers: [AuthController],
  providers: [AuthService],
  exports: [PassportModule]
})
export class AuthModule {}
