import { Test, TestingModule } from "@nestjs/testing";
import { AuthService } from "./auth.service";
import { DataSource } from "typeorm";
import { User } from "src/user/entities/user.entity";
import { TypeOrmModule } from "@nestjs/typeorm";
import { FollowEntity } from "src/user/entities/user.followList.entity";
import { RestaurantInfoEntity } from "src/restaurant/entities/restaurant.entity";
import { UserRestaurantListEntity } from "src/user/entities/user.restaurantlist.entity";
import { ReviewInfoEntity } from "src/review/entities/review.entity";
import { JwtService } from "@nestjs/jwt";
import { UserModule } from "src/user/user.module";
import { RestaurantModule } from "src/restaurant/restaurant.module";
import { ReviewModule } from "src/review/review.module";
import { HttpException } from '@nestjs/common';
import { UserInfoDto } from "src/user/dto/userInfo.dto";
import { UserService } from "src/user/user.service";
import { AuthModule } from "./auth.module";

describe("AuthService", () => {

  let authService: AuthService;
  let dataSource: DataSource;
  let jwtService: JwtService;
  let userService: UserService;

  beforeAll(async () => {
    const testModule = await Test.createTestingModule({
      imports: [
        AuthModule,
        UserModule,
        RestaurantModule, 
        ReviewModule,
        TypeOrmModule.forRoot({
          type: 'postgres',
          host: 'localhost',
          port: 5433,
          username: 'user',
          password: 'password',
          database: 'testdb',
          entities: [User, FollowEntity, RestaurantInfoEntity, UserRestaurantListEntity, ReviewInfoEntity],
          synchronize: true,
        }),
      ],
    }).compile();

    authService = testModule.get<AuthService>(AuthService);
    jwtService = testModule.get<JwtService>(JwtService);
    dataSource = testModule.get<DataSource>(DataSource);
    userService = testModule.get<UserService>(UserService);
  });



  beforeEach(async () => {
    const repository = dataSource.getRepository(User);
    await repository.query('DELETE FROM public.user');
  });

  it("회원가입 된 유저가 로그인 요청을 한 경우", async () => {

    const userInfoDto: UserInfoDto = {
      email: "test@naver.com",
      password: "",
      provider: "naver",
      nickName: "test",
      region: "인천",
      birthdate: "1999/10/13",
      isMale: true,
    };

    await userService.signup(userInfoDto);

    const loginRequestUser = {
      email: "test@naver.com"
    }

    const result = await authService.signin(loginRequestUser);
    expect(result).toBeDefined();
    expect(jwtService.verify(result.accessToken)).toBeTruthy();
    expect(jwtService.verify(result.refreshToken)).toBeTruthy();
  })

  it("회원가입이 안된 유저가 로그인 요청을 한 경우",async () => {
    const loginRequestUser = {
      email: "test@naver.com"
    }

    await expect(authService.signin(loginRequestUser))
    .rejects
    .toThrow(HttpException);
  })

  it("정상적인 리프레쉬 토큰을 받은 경우", async () => {
    const refreshToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0IiwiaWQiOi0xLCJpYXQiOjIwMTYyMzkwMjJ9.zOd1UtNxKjPqNUYGapfDgqY78M5iBEj2Ike386HTDOA"

    const result = await authService.checkRefreshToken(refreshToken);

    expect(result).toBeDefined();
    expect(jwtService.verify(result.accessToken)).toBeTruthy();
  })

  it("비정상적인 리프레쉬 토큰을 받은 경우", async () => {
    const refreshToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0IiwiaWQiOjEsImlhdCI6MjAxNjIzOTAyMn0.3nRp6Qdxf5xj2C0M7-0lOURWx-dAKEjl9eS9dRoQTsA"

    await expect(authService.checkRefreshToken(refreshToken))
    .rejects
    .toThrow(HttpException);
  })
});