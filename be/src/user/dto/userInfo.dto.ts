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
import { Transform } from 'class-transformer';

export class UserInfoDto {
  @ApiProperty({
    example: "user@example.com",
    description: "The email of the user",
  })
  @IsEmail()
  @IsNotEmpty()
  @MaxLength(50)
  email: string;

  @ApiProperty({ example: "1234", description: "The password of the user", required: false })
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
  @Transform(({ value }) => value === true)
  @IsBoolean()
  @IsNotEmpty()
  isMale: boolean;
}
