import { DataSource, IsNull, Repository, Not, In } from "typeorm";
import { User } from "./entities/user.entity";
import { UserInfoDto } from "./dto/userInfo.dto";
import {
  ConflictException,
  Injectable,
  BadRequestException,
} from "@nestjs/common";

@Injectable()
export class UserRepository extends Repository<User> {
  constructor(private dataSource: DataSource) {
    super(User, dataSource.createEntityManager());
  }
  async createUser(userentity: User) {
    await this.save(userentity);
  }
  async getNickNameAvailability(nickName: UserInfoDto["nickName"]) {
    const user = await this.findOne({
      select: ["nickName"],
      where: { nickName: nickName },
    });
    return { isexist: user !== null };
  }
  async getEmailAvailability(email: UserInfoDto["email"]) {
    const user = await this.findOne({
      select: ["email"],
      where: { email: email },
    });
    return { isexist: user !== null };
  }
  async getMypageUserInfo(id: number) {
    const userInfo = await this.findOne({
      select: ["nickName", "birthdate", "isMale", "region", "profileImage"],
      where: { id: id },
    });
    return { userInfo: userInfo };
  }
  async getMypageTargetUserInfo(targetInfoId: number) {
    const userInfo = await this.findOne({
      select: ["nickName", "birthdate", "isMale", "region", "profileImage"],
      where: { id: targetInfoId },
    });
    return userInfo;
  }
  async getUsersInfo(targetInfoIds: number[]) {
    const userInfo = await this.find({
      select: ["nickName", "region"],
      where: { id: In(targetInfoIds) },
    });
    return userInfo;
  }
  async getMypageUserDetailInfo(id: number) {
    const userInfo = await this.findOne({
      select: [
        "nickName",
        "birthdate",
        "isMale",
        "region",
        "provider",
        "email",
        "profileImage",
      ],
      where: { id: id },
    });
    return { userInfo: userInfo };
  }

  async getRecommendUserListInfo(idList: number[], id: number) {
    const curUser = await this.findOne({
      select: ["id", "region"],
      where: { id: id },
    });

    const myRestaurants = await this.createQueryBuilder("user")
      .leftJoinAndSelect("user.restaurant", "userRestaurant")
      .where("user.id = :id", { id })
      .select("userRestaurant.restaurantId")
      .getRawMany();

    const myFavRestaurants = myRestaurants.map(
      (r) => r.userRestaurant_restaurant_id
    );

    const userInfo = await this.createQueryBuilder("user")
      .leftJoin("user.restaurant", "userRestaurant")
      .select([
        "user.nickName",
        "user.region",
        'SUM(CASE WHEN userRestaurant.restaurantId IN (:...myFavRestaurants) THEN 1 ELSE 0 END) AS "commonRestaurant"',
      ])
      .setParameter("myFavRestaurants", myFavRestaurants)
      .where("user.id NOT IN (:...idList)", { idList })
      .andWhere("user.region = :yourRegion", { yourRegion: curUser.region })
      .groupBy("user.id")
      .orderBy("\"commonRestaurant\"","DESC")
      .limit(10)
      .getRawMany();

    return userInfo;
  }

  async logout(id: number) {
    return {};
  }
  async deleteUserAccount(id: number) {
    const userInfo = await this.findOne({
      select: ["id"],
      where: { id: id },
    });
    if (userInfo) {
      await this.update(userInfo.id, { deleted_at: new Date() });
    } else {
      throw new ConflictException("Already Deleted");
    }
    return {};
  }
  async updateMypageUserInfo(id: number, userEntity: User) {
    const user = await this.findOne({ select: ["id"], where: { id: id } });
    const [emailUser, nickNameUser] = await Promise.all([
      this.findOne({ select: ["id"], where: { email: userEntity["email"] } }),
      this.findOne({
        select: ["id"],
        where: { nickName: userEntity["nickName"] },
      }),
    ]);

    const isEmailDuplicate = !!emailUser;
    const isNickNameDuplicate = !!nickNameUser;

    let updateObject = {
      birthdate: userEntity["birthdate"],
      isMale: userEntity["isMale"],
      region: userEntity["region"],
      provider: userEntity["provider"],
      password: userEntity["password"],
      profileImage: userEntity["profileImage"],
    };

    if (!isEmailDuplicate) {
      updateObject["email"] = userEntity["email"];
    }
    if (!isNickNameDuplicate) {
      updateObject["nickName"] = userEntity["nickName"];
    }
    await this.update(user.id, updateObject);
    return {
      isEmailDuplicate: isEmailDuplicate,
      isNickNameDuplicate: isNickNameDuplicate,
    };
  }
}
