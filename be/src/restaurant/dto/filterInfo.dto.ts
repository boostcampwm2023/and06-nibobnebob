import { LocationDto } from "./location.dto";

export class FilterInfoDto {
  filter: string;

  latitude: number;

  longitude: number;

  radius: number;

  constructor(filter: string, location: LocationDto) {
    this.filter = filter;
    this.latitude = location.latitude ? parseFloat(location.latitude) : null;
    this.longitude = location.longitude ? parseFloat(location.longitude) : null;
    this.radius = location.radius ? parseInt(location.radius) : 500000;
  }
}
