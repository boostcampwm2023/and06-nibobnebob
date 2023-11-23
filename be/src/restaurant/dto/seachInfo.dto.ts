export class SearchInfoDto {
    partialName: string;
    latitude: number;
    longitude: number;
    radius: number;

    constructor(
        partialName: string,
        location: string,
        radius: string,
    ){
        this.partialName = partialName;
        this.radius = parseInt(radius, 10);

        const [latitude, longitude] = location.split(' ').map(Number);
        this.latitude = latitude;
        this.longitude = longitude; 
    }
}