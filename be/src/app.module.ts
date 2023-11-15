import { Module } from "@nestjs/common";
import { UserModule } from "./user/user.module";
import { TypeOrmModule } from "@nestjs/typeorm";
import { typeORMConfig } from "./configs/typeorm.config";

@Module({
  imports: [UserModule, TypeOrmModule.forRoot(typeORMConfig)],
})
export class AppModule {}
