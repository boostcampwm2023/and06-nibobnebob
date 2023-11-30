import { ApiProperty } from "@nestjs/swagger";
import {
  IsBoolean,
  IsString,
  IsNotEmpty,
  IsEmail,
  IsInt,
  MaxLength,
  IsOptional,
  IsInstance,
} from "class-validator";

export class UserInfoDto {
  @ApiProperty({
    example: "user@example.com",
    description: "The email of the user",
  })
  @IsEmail()
  @IsNotEmpty()
  @MaxLength(50)
  email: string;

  @ApiProperty({ example: "1234", description: "The password of the user" })
  @IsString()
  @IsOptional()
  @MaxLength(50)
  password: string;

  @ApiProperty({ example: "naver", description: "The provider of the user" })
  @IsString()
  @IsNotEmpty()
  @MaxLength(20)
  provider: string;

  @ApiProperty({ example: "test", description: "The nickname of the user" })
  @IsString()
  @IsNotEmpty()
  @MaxLength(20)
  nickName: string;

  @ApiProperty({ example: "강동구", description: "The region of the user" })
  @IsString()
  @IsNotEmpty()
  @MaxLength(20)
  region: string;

  @ApiProperty({ example: "1234/56/78", description: "The birth of the user" })
  @IsString()
  @MaxLength(11)
  @IsNotEmpty()
  birthdate: string;

  @ApiProperty({
    example: true,
    description: "The gender of the user. true is male, false is female",
  })
  @IsBoolean()
  @IsNotEmpty()
  isMale: boolean;

  @ApiProperty({
    example: "<Buffer 89 50 4e 47 0d 0a 1a 0a 00 00 00 0d 49 48 44 52 00 00 02 d0 00 00 01 95 08 02 00 00 00 58 48 51 89 00 00 00 01 73 52 47 42 00 ae ce 1c e9 00 00 00 78 ... 134080 more bytes>",
    description: "The profile image of the user",
  })
  @IsInstance(Buffer)
  @IsOptional()
  profileImage: Buffer;
}
