import { Test } from "@nestjs/testing";
import { UserRepository } from "./user.repository";
import { UserService } from "./user.service";
import { AuthModule } from "../auth/auth.module";
import { UserInfoDto } from "./dto/userInfo.dto";
import { newDb } from "pg-mem";
import { DataSource, TypeORMError } from "typeorm";
import { User } from "./entities/user.entity";
import { TypeOrmModule } from "@nestjs/typeorm";
import { FollowEntity } from "./entities/user.followList.entity";
import { RestaurantInfoEntity } from "../restaurant/entities/restaurant.entity";
import { UserRestaurantListEntity } from "./entities/user.restaurantlist.entity";
import { UserWishRestaurantListEntity } from "./entities/user.wishrestaurantlist.entity";
import { ReviewInfoEntity } from "../review/entities/review.entity";
import { UserFollowListRepository } from "./user.followList.repository";
import { UserRestaurantListRepository } from "./user.restaurantList.repository";
import { UserWishRestaurantListRepository } from "./user.wishrestaurantList.repository";
import { RestaurantRepository } from "../restaurant/restaurant.repository";
import { ReviewRepository } from "../review/review.repository";

describe("UserService", () => {
  let userService: UserService;
  let dataSource: DataSource;

  beforeEach(async () => {
    const userRepository = dataSource.getRepository(User);
    await userRepository.query(`DELETE FROM public.user;`);
  });
  beforeAll(async () => {
    const testModule = await Test.createTestingModule({
      imports: [
        AuthModule,
        TypeOrmModule.forRoot({
          type: 'postgres',
          host: 'localhost',
          port: 5433,
          username: 'user',
          password: 'password',
          database: 'testdb',
          entities: [User, FollowEntity, RestaurantInfoEntity, UserRestaurantListEntity, UserWishRestaurantListEntity, ReviewInfoEntity],
          synchronize: true,
        }),
        TypeOrmModule.forFeature([User, RestaurantRepository, UserRepository, UserRestaurantListRepository, UserFollowListRepository, UserWishRestaurantListRepository, ReviewRepository]),
      ],
      providers: [UserService],
    }).compile();

    userService = testModule.get<UserService>(UserService);
    dataSource = testModule.get<DataSource>(DataSource);
  });

  afterAll(async () => {
    await dataSource.destroy();
  });

  it("회원가입", async () => {
    const userInfoDto: UserInfoDto = {
      email: "test@email.com",
      password: "1234",
      provider: " ",
      nickName: "hi",
      region: "인천",
      birthdate: "1999/10/13",
      isMale: true,
    };

    await userService.signup(userInfoDto);

    const userRepository = dataSource.getRepository(User);

    const foundUser = await userRepository.findOne({
      select: ["nickName", "birthdate", "isMale", "region"],
      where: { email: "test@email.com" },
    });

    expect(foundUser).toEqual(await
      expect.objectContaining({
        nickName: "hi",
        region: "인천",
        birthdate: "1999/10/13",
        isMale: true,
      })
    );
  });
});

describe("마이페이지", () => {
  let userService: UserService;
  let dataSource: DataSource;
  beforeEach(async () => {
    const userRepository = dataSource.getRepository(User);
    await userRepository.query(`DELETE FROM public.user;`);
  });


  beforeAll(async () => {
    const testModule = await Test.createTestingModule({
      imports: [
        AuthModule,
        TypeOrmModule.forRoot({
          type: 'postgres',
          host: 'localhost',
          port: 5433,
          username: 'user',
          password: 'password',
          database: 'testdb',
          entities: [User, FollowEntity, RestaurantInfoEntity, UserRestaurantListEntity, UserWishRestaurantListEntity, ReviewInfoEntity],
          synchronize: true,
        }),
        TypeOrmModule.forFeature([User, RestaurantRepository, UserRepository, UserRestaurantListRepository, UserFollowListRepository, UserWishRestaurantListRepository, ReviewRepository]),
      ],
      providers: [UserService],
    }).compile();

    userService = testModule.get<UserService>(UserService);
    dataSource = testModule.get<DataSource>(DataSource);
  });

  afterAll(async () => {
    await dataSource.destroy();
  });

  it("마이페이지 정보 요청", async () => {
    const userInfoDto: UserInfoDto = {
      email: "test@email.com",
      password: "1234",
      provider: " ",
      nickName: "hi",
      region: "인천",
      birthdate: "1999/10/13",
      isMale: true
    };
    const userRepository = dataSource.getRepository(User);
    const id = await userRepository.save(userInfoDto);

    const mypageInfo = await userService.getMypageUserInfo(id)

    expect(mypageInfo).toEqual(await
      expect.objectContaining({
        "userInfo": {
          nickName: "hi",
          region: "인천",
          birthdate: "1999/10/13",
          isMale: true,
        },
      })
    );
  });
  it("마이페이지 수정 페이지 정보 요청", async () => {
    const userInfoDto: UserInfoDto = {
      email: "test@email.com",
      password: "1234",
      provider: " ",
      nickName: "hi",
      region: "인천",
      birthdate: "1999/10/13",
      isMale: true
    };
    const userRepository = dataSource.getRepository(User);
    const id = await userRepository.save(userInfoDto);

    const mypageInfo = await userService.getMypageUserDetailInfo(id)

    expect(mypageInfo).toEqual(await
      expect.objectContaining({
        "userInfo": {
          email: "test@email.com",
          nickName: "hi",
          provider: " ",
          region: "인천",
          birthdate: "1999/10/13",
          isMale: true,
        },
      })
    );
  });

  it("유저 회원탈퇴", async () => {
    const userInfoDto: UserInfoDto = {
      email: "test@email.com",
      password: "1234",
      provider: " ",
      nickName: "hi",
      region: "인천",
      birthdate: "1999/10/13",
      isMale: true
    };
    const userRepository = dataSource.getRepository(User);
    const id = await userRepository.save(userInfoDto);

    await userService.deleteUserAccount(id)

    const result = await userRepository.findOne({ where: { id: id["id"] } });

    expect(result).toEqual(await
      expect.objectContaining({

      })
    );
  });
  it("유저 정보 업데이트", async () => {
    const userInfoDto: UserInfoDto = {
      email: "test@email.com",
      password: "1234",
      provider: " ",
      nickName: "hi",
      region: "인천",
      birthdate: "1999/10/13",
      isMale: true
    };
    const userRepository = dataSource.getRepository(User);
    const id = await userRepository.save(userInfoDto);

    const userUpdateInfoDto: UserInfoDto = {
      email: "testSuccess@email.com",
      password: "4321",
      provider: " ",
      nickName: "Good",
      region: "서울",
      birthdate: "2000/01/03",
      isMale: false
    }

    await userService.updateMypageUserInfo(id, userUpdateInfoDto)

    const result = await userRepository.findOne({ where: { id: id["id"] } });
    expect(result).toEqual(await
      expect.objectContaining({
        email: "testSuccess@email.com",
        password: "4321",
        provider: " ",
        nickName: "Good",
        region: "서울",
        birthdate: "2000/01/03",
        isMale: false
      })
    );
  });
});
