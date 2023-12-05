import { IsString, IsInt, IsOptional, Min, IsNotEmpty } from 'class-validator';
import { Type } from 'class-transformer';

export class SortInfoDto {
    @IsString()
    @IsOptional()
    sort?: string;

    @IsInt()
    @Min(1)
    @Type(() => Number)
    @IsOptional()
    page?: number;

    @IsInt()
    @Min(1)
    @Type(() => Number)
    @IsOptional()
    limit?: number;
}