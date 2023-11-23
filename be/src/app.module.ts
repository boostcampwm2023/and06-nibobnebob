import { Module } from "@nestjs/common";
import { UserModule } from "./user/user.module";
import { TypeOrmModule } from "@nestjs/typeorm";
import { typeORMConfig } from "./configs/typeorm.config";
import { AuthModule } from "./auth/auth.module";
import { RestaurantModule } from "./restaurant/restaurant.module";
import { ReviewModule } from "./review/review.module";
import { CustomLoggerService } from "./custom.logger";
import { APP_INTERCEPTOR } from "@nestjs/core";
import { LoggingInterceptor } from "./logger.interceptor";

@Module({
  imports: [
    UserModule,
    TypeOrmModule.forRoot(typeORMConfig),
    AuthModule,
    RestaurantModule,
    ReviewModule,
  ],
  providers: [
    CustomLoggerService,
    {
      provide: APP_INTERCEPTOR,
      useClass: LoggingInterceptor,
    },
  ],
})
export class AppModule {}
