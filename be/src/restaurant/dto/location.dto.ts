import { IsString, IsNumberString, IsOptional } from 'class-validator';

export class LocationDto {
    @IsNumberString({}, { message: 'Latitude must be a valid number.' })
    @IsOptional()
    latitude: string;

    @IsNumberString({}, { message: 'Longitude must be a valid number.' })
    @IsOptional()
    longitude: string;

    @IsNumberString({}, { message: 'Radius must be a valid number.' })
    @IsOptional()
    radius: string;
}