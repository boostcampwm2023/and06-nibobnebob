// logging.interceptor.ts
import {
  Injectable,
  NestInterceptor,
  ExecutionContext,
  CallHandler,
  HttpStatus,
} from "@nestjs/common";
import { Observable, throwError } from "rxjs";
import { tap, catchError } from "rxjs/operators";
import { CustomLoggerService } from "./custom.logger";

@Injectable()
export class LoggingInterceptor implements NestInterceptor {
  constructor(private logger: CustomLoggerService) {}

  intercept(context: ExecutionContext, next: CallHandler): Observable<any> {
    const now = Date.now();
    const request = context.switchToHttp().getRequest();
    const response = context.switchToHttp().getResponse();
    const { method, url } = request;
    const clientIp = request.ip || request.headers["x-forwarded-for"];

    return next.handle().pipe(
      tap(() => {
        const { statusCode } = response;
        const delay = Date.now() - now;
        this.logger.log(
          `[Success]  ${clientIp} ${method} ${url} ${statusCode} ${delay}ms`
        );
      }),
      catchError((error) => {
        const status = error.status || HttpStatus.INTERNAL_SERVER_ERROR;
        this.logger.error(
          `[Error]  ${clientIp} ${method} ${url} ${status} ${error.message}`,
          error.stack
        );

        return throwError(error);
      })
    );
  }
}
