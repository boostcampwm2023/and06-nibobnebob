export class SearchInfoDto {
    partialName: string;
    latitude: number;
    longitude: number;
    radius: number;

    constructor(
        partialName: string,
        location: string = null,
        radius: string = null,
    ) {
        this.partialName = partialName;
        if (radius) this.radius = parseInt(radius, 10);

        if (location) {
            const [latitude, longitude] = location.split(' ').map(Number);
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }
}