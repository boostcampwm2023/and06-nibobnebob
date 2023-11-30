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
  async createUser(userentity: User): Promise<User> {
    try {
      await this.save(userentity);
    } catch (err) {
      if (err.code === "23505") {
        throw new ConflictException("Duplicated Value");
      }
    }
    return;
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
    return { userInfo: userInfo };
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
  async getRecommendUserListInfo(idList: number[]) {
    const userInfo = await this.createQueryBuilder("user")
      .select(["user.nickName", "user.region"])
      .where("user.id NOT IN (:...idList)", { idList })
      .orderBy("RANDOM()")
      .limit(2)
      .getMany();
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
      profileImage : userEntity["profileImage"]
    };

    if (!isEmailDuplicate) {
      updateObject["email"] =userEntity["email"];
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
