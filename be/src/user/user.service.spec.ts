import { Test } from '@nestjs/testing';
import { UserRepository } from './user.repository';
import { UserService } from './user.service';
import { AuthModule } from '../auth/auth.module';
import { UserInfoDto } from './dto/userInfo.dto';
import { newDb } from 'pg-mem';
import { DataSource, TypeORMError } from 'typeorm';
import { User } from './entities/user.entity';
import { TypeOrmModule } from '@nestjs/typeorm';

describe('UserService', () => {

  let userService: UserService;
  let dataSource: DataSource;
  beforeAll(async () => {
    const db = newDb({ autoCreateForeignKeyIndices: true });

    db.public.registerFunction({
      name: 'current_database',
      implementation: () => 'test_database',
    });

    db.public.registerFunction({
      name: 'version',
      implementation: () => 'PostgreSQL 12.16, compiled by Visual C++ build 1914, 64-bit',
    });

    dataSource = (await db.adapters.createTypeormDataSource({
      type: 'postgres',
      entities: [User],
    }))

    await dataSource.initialize();
    await dataSource.synchronize();

    const testModule = await Test.createTestingModule({
      imports: [AuthModule, TypeOrmModule.forRoot(), TypeOrmModule.forFeature([User])],
      providers: [UserService, UserRepository],
    }).overrideProvider(DataSource).useValue(dataSource).compile();

    userService = testModule.get<UserService>(UserService);
  });


  it('should create a new user and return the user data', async () => {
    const userInfoDto: UserInfoDto = {
      email: 'test@email.com',
      password: "1234",
      provider: " ",
      nickName: "hi",
      region: "인천",
      birthdate: "1999/10/13",
      isMale: true
    };

    await userService.signup(userInfoDto);

    const userRepository = dataSource.getRepository(User);

    const foundUser = await userRepository.findOne({
      select: ["nickName", "birthdate", "isMale", "region"],
      where: { email: 'test@email.com' },
    });

    expect(foundUser).toEqual(expect.objectContaining({
      nickName: "hi",
      region: "인천",
      birthdate: "1999/10/13",
      isMale: true
    }));
  });
});
