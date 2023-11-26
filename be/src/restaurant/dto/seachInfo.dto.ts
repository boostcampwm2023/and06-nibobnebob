import { IsString, IsNumberString, IsOptional } from 'class-validator';

export class SearchInfoDto {
    @IsString()
    partialName: string;

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
        partialName: string,
        latitude: string = null,
        longitude: string = null,
        radius: string = null
    ) {
        this.partialName = partialName;
        this.latitude = latitude ? parseFloat(latitude) : null;
        this.longitude = longitude ? parseFloat(longitude) : null;
        this.radius = radius ? parseInt(radius) : null;
    }
}