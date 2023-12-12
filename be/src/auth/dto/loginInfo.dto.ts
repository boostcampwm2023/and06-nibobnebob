import { ApiProperty } from "@nestjs/swagger";
import { IsEmail, IsNotEmpty, IsString, MaxLength } from "class-validator";

export class LoginInfoDto {
    @ApiProperty({
        example: "user@example.com",
        description: "The email of the user",
    })
    @IsEmail()
    @IsNotEmpty()
    @MaxLength(50)
    email: string;

    @ApiProperty({
        description: "The password of the user",
    })
    @IsString()
    @IsNotEmpty()
    @MaxLength(50)
    password: string;
}
