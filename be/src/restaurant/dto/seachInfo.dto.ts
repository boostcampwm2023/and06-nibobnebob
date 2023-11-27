import { LocationDto } from "./location.dto";

export class SearchInfoDto {
    partialName: string;

    latitude: number;

    longitude: number;

    radius: number;

    constructor(
        partialName: string,
        location: LocationDto
    ) {
        this.partialName = partialName;
        this.latitude = location.latitude ? parseFloat(location.latitude) : null;
        this.longitude = location.longitude ? parseFloat(location.longitude) : null;
        this.radius = location.radius ? parseInt(location.radius) : 500000;
    }
}