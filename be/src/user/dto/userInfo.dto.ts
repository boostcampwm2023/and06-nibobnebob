import {
  IsBoolean,
  IsString,
  IsNotEmpty,
  IsEmail,
  IsInt,
  MaxLength,
} from "class-validator";

export class UserInfoDto {
  @IsEmail()
  @IsNotEmpty()
  @MaxLength(50)
  email: string;

  @IsString()
  @IsNotEmpty()
  @MaxLength(50)
  password: string;

  @IsString()
  @IsNotEmpty()
  @MaxLength(20)
  provider: string;

  @IsString()
  @IsNotEmpty()
  @MaxLength(20)
  nickName: string;

  @IsInt()
  @IsNotEmpty()
  age: number;

  @IsBoolean()
  @IsNotEmpty()
  gender: boolean;
}
