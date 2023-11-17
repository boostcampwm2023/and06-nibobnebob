import { DataSource, Repository } from "typeorm";
import { User } from "./entities/user.entity";
import { UserInfoDto } from "./dto/userInfo.dto";
import { ConflictException, Injectable } from "@nestjs/common";

@Injectable()
export class UserRepository extends Repository<User> {
  constructor(private dataSource: DataSource) {
    super(User, dataSource.createEntityManager());
  }
  async createUser(userinfoDto: UserInfoDto): Promise<User> {
    const newUser = this.create(userinfoDto);
    try {
      await this.save(newUser);
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
  async getUserInfo(nickName: UserInfoDto["nickName"]) {
    const userInfo = await this.findOne({
      select: ["nickName", "birthdate", "isMale", "region"],
      where: { nickName: nickName },
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
      ],
      where: { id: id },
    });
    return { userInfo: userInfo };
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
  async updateMypageUserInfo(id: number, userInfoDto: UserInfoDto) {
    const user = await this.findOne({ select: ["id"], where: { id: id } });
    const [emailUser, nickNameUser] = await Promise.all([
      this.findOne({ select: ["id"], where: { email: userInfoDto["email"] } }),
      this.findOne({
        select: ["id"], where: { nickName: userInfoDto["nickName"] },
      }),
    ]);

    const isEmailDuplicate = !!emailUser;
    const isNickNameDuplicate = !!nickNameUser;

    let updateObject = {
      birthdate: userInfoDto["birthdate"],
      isMale: userInfoDto["isMale"],
      region: userInfoDto["region"],
      provider: userInfoDto["provider"],
      password: userInfoDto["password"],
    };

    if (!isEmailDuplicate) {
      updateObject["email"] = userInfoDto["email"];
    }
    if (!isNickNameDuplicate) {
      updateObject["nickName"] = userInfoDto["nickName"];
    }
    await this.update(user.id, updateObject);
    return {
      isEmailDuplicate: isEmailDuplicate,
      isNickNameDuplicate: isNickNameDuplicate,
    };
  }
}
