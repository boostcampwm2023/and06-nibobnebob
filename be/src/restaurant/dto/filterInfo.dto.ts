import { IsString, IsNumberString, IsOptional } from 'class-validator';

export class FilterInfoDto {
    @IsString()
    filter: string;

    @IsNumberString({}, { message: 'Latitude must be a valid number.' })
    @IsOptional()
    latitude: number;

    @IsNumberString({}, { message: 'Longitude must be a valid number.' })
    @IsOptional()
    longitude: number;

    @IsNumberString({}, { message: 'Radius must be a valid number.' })
    @IsOptional()
    radius: number;

    constructor(
        filter: string,
        latitude: string = null,
        longitude: string = null,
        radius: string = null
    ) {
        this.filter = filter;
        this.latitude = latitude ? parseFloat(latitude) : null;
        this.longitude = longitude ? parseFloat(longitude) : null;
        this.radius = radius ? parseInt(radius) : null;
    }
}