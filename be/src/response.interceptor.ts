import {
  Injectable,
  NestInterceptor,
  ExecutionContext,
  CallHandler,
} from "@nestjs/common";
import { Observable } from "rxjs";
import { map } from "rxjs/operators";
import { BaseResponse } from "./baseResponse";

@Injectable()
export class TransformInterceptor<T>
  implements NestInterceptor<T, BaseResponse<T>>
{
  intercept(
    context: ExecutionContext,
    next: CallHandler<T>
  ): Observable<BaseResponse<T>> {
    return next
      .handle()
      .pipe(
        map((data) => ({ data: data, message: "Success", statusCode: 200 }))
      );
  }
}
