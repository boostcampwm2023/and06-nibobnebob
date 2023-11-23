import { ApiProperty } from "@nestjs/swagger";
import {
  IsBoolean,
  IsString,
  IsNotEmpty,
  IsInt,
  MaxLength,
  IsOptional,
  MinLength,
} from "class-validator";

export class ReviewInfoDto {
  @ApiProperty({
    example: "true",
    description: "The transportation for visiting",
  })
  @IsBoolean()
  @IsNotEmpty()
  visitMethod: boolean;

  @ApiProperty({
    example: "0",
    description: "transportation Accessibility for visiting",
  })
  @IsInt()
  @IsOptional()
  @MaxLength(1)
  transportationAccessibility: number | null;

  @ApiProperty({
    example: "0",
    description: "condition of the restaurant's parking area",
  })
  @IsInt()
  @IsOptional()
  @MaxLength(1)
  parkingArea: number | null;

  @ApiProperty({ example: "0", description: "The taste of the food" })
  @IsInt()
  @IsNotEmpty()
  @MaxLength(1)
  taste: number;

  @ApiProperty({ example: "0", description: "The service of the restaurant" })
  @IsInt()
  @IsNotEmpty()
  @MaxLength(1)
  service: number;

  @ApiProperty({
    example: "0",
    description: "The condition of the restaurant's restroom",
  })
  @IsInt()
  @IsNotEmpty()
  @MaxLength(1)
  restroomtCleanliness: number;

  @ApiProperty({
    example: "좋았음",
    description: "The overallExperience about the restaurant",
  })
  @IsString()
  @IsNotEmpty()
  @MinLength(20)
  overallExperience: string;
}
