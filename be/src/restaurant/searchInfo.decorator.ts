import { createParamDecorator, ExecutionContext } from '@nestjs/common';
import { SearchInfoDto } from './dto/seachInfo.dto';

export const SearchInfo = createParamDecorator(
  (data: unknown, ctx: ExecutionContext): SearchInfoDto => {
    const request = ctx.switchToHttp().getRequest();
    const partialName = request.params.partialRestaurantName || '';
    const latitude = request.query.latitude || null;
    const longitude = request.query.longitude || null;
    const radius = request.query.radius || 500000;
    
    return new SearchInfoDto(partialName, latitude, longitude, radius);
  },
);
