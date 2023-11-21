import { ApiProperty } from "@nestjs/swagger";
import {
    IsNotEmpty,
    IsInt,
} from "class-validator";

export class RestaurantInfoDto {
    @ApiProperty({
        example: "음식점 id",
        description: "The id of the restaurant",
    })
    @IsInt()
    @IsNotEmpty()
    id: number;
}
