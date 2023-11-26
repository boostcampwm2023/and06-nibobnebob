import { createParamDecorator, ExecutionContext } from '@nestjs/common';
import { FilterInfoDto } from './dto/filterInfo.dto';

export const filterInfo = createParamDecorator(
  (data: unknown, ctx: ExecutionContext): FilterInfoDto => {
    const request = ctx.switchToHttp().getRequest();
    const filter = request.query.filter || '';
    const latitude = request.query.latitude || null;
    const longitude = request.query.longitude || null;
    const radius = request.query.radius || 500000;
    
    return new FilterInfoDto(filter, latitude, longitude, radius);
  },
);
