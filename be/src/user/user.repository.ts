import { DataSource, Repository } from 'typeorm';
import { User } from './entities/user.entity';
import { UserInfoDto } from './dto/userInfo.dto';
import { Injectable } from '@nestjs/common';

@Injectable()
export class UserRepository extends Repository<User> {
    constructor(private dataSource: DataSource){
        super(User, dataSource.createEntityManager());
    }
  async createUser(userinfoDto: UserInfoDto): Promise<User> {
    const newUser = this.create(userinfoDto);
    await this.save(newUser);
    return;
  }
}