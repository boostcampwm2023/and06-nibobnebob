import { DataSource, Repository } from "typeorm";
import { User } from "./entities/user.entity";
import { UserInfoDto } from "./dto/userInfo.dto";
import { ConflictException, Injectable } from "@nestjs/common";
import { userInfo } from "os";

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
  async getMypageUserInfo(nickName: UserInfoDto["nickName"]) {
    const userInfo = await this.findOne({
      select: ["nickName", "birthdate", "isMale", "region"],
      where: { nickName: nickName },
    });
    return { userInfo: userInfo };
  }
  async getMypageUserDetailInfo(nickName: UserInfoDto["nickName"]) {
    const userInfo = await this.findOne({
      select: [
        "nickName",
        "birthdate",
        "isMale",
        "region",
        "provider",
        "email",
      ],
      where: { nickName: nickName },
    });
    return { userInfo: userInfo };
  }
  async deleteUserAccount(nickName: UserInfoDto["nickName"]) {
    const userInfo = await this.findOne({
      select: ["id"],
      where: { nickName: nickName },
    });
    if (userInfo) {
      await this.update(userInfo.id, { deleted_at: new Date() });
    } else {
      throw new ConflictException("Already Deleted");
    }
    return {};
  }
  async updateMypageUserInfo(nickName: UserInfoDto["nickName"], userInfoDto: UserInfoDto) {
    const userInfo = await this.findOne({
      select: ["id"],
      where: { nickName: nickName },
    });
    if (userInfo) {
      await this.update(userInfo.id, {
        nickName: userInfoDto["nickName"],
        birthdate: userInfoDto["birthdate"],
        isMale: userInfoDto["isMale"],
        region: userInfoDto["region"],
        provider: userInfoDto["provider"],
        email: userInfoDto["email"],
        password: userInfoDto["password"],
      });
    } else {
      throw new ConflictException("Already Deleted");
    }
    return {};
  }
}
