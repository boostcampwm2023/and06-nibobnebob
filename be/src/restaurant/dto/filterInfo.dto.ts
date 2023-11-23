export class FilterInfoDto {
    filter: string;
    latitude: number;
    longitude: number;
    radius: number;

    constructor(
        filter: string,
        location: string,
        radius: string,
    ){
        this.filter = filter;
        this.radius = parseInt(radius, 10);

        const [latitude, longitude] = location.split(' ').map(Number);
        this.latitude = latitude;
        this.longitude = longitude; 
    }
}