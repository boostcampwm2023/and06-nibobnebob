import { Injectable } from '@nestjs/common';
import { UserInfoDto } from './dto/userInfo.dto';
@Injectable()
export class UserService {
    signup( userInfoDto : UserInfoDto){
        const { email, password, provider, nickName, age, gender } = userInfoDto;

        // DB에 저장
        
        return userInfoDto;
        
    }
}
