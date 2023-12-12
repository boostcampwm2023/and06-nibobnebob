import { ApiProperty } from "@nestjs/swagger";
import {
  IsBoolean,
  IsString,
  IsNotEmpty,
  IsInt,
  MaxLength,
  IsOptional,
  MinLength,
  Max,
  Min,
} from "class-validator";
import { Transform } from 'class-transformer';

export class ReviewInfoDto {
  @ApiProperty({
    example: "true",
    description: "The transportation for visiting",
  })
  @Transform(({ value }) => value === 'true')
  @IsBoolean()
  @IsNotEmpty()
  isCarVisit: boolean;

  @ApiProperty({
    example: "0",
    description: "transportation Accessibility for visiting",
  })
  @IsOptional()
  @Transform(({ value }) => {
    return !value ? null : parseInt(value);
  })
  @IsInt()
  @Min(0)
  @Max(4)
  transportationAccessibility: number | null;

  @ApiProperty({
    example: "0",
    description: "condition of the restaurant's parking area",
  })
  @IsOptional()
  @Transform(({ value }) => {
    return !value ? null : parseInt(value);
  })
  @IsInt()
  @Min(0)
  @Max(4)
  parkingArea: number | null;

  @ApiProperty({ example: "0", description: "The taste of the food" })
  @Transform(({ value }) => parseInt(value))
  @IsInt()
  @IsNotEmpty()
  @Min(0)
  @Max(4)
  taste: number;

  @ApiProperty({ example: "0", description: "The service of the restaurant" })
  @Transform(({ value }) => parseInt(value))
  @IsInt()
  @IsNotEmpty()
  @Min(0)
  @Max(4)
  service: number;

  @ApiProperty({
    example: "0",
    description: "The condition of the restaurant's restroom",
  })
  @Transform(({ value }) => parseInt(value))
  @IsInt()
  @IsNotEmpty()
  @Min(0)
  @Max(4)
  restroomCleanliness: number;

  @ApiProperty({
    example: "좋았음",
    description: "The overallExperience about the restaurant",
  })
  @IsString()
  @IsNotEmpty()
  @MinLength(20)
  overallExperience: string;
}
