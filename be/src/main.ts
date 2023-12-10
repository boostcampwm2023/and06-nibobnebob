import { NestFactory } from "@nestjs/core";
import { SwaggerModule, DocumentBuilder } from "@nestjs/swagger";
import { AppModule } from "./app.module";
import { TransformInterceptor } from "./response.interceptor";
import { HttpExceptionFilter } from "./error.filter";
import * as fs from 'fs';

async function bootstrap() {

  const app = await NestFactory.create(AppModule);

  app.setGlobalPrefix("api");
  app.useGlobalFilters(new HttpExceptionFilter());
  app.useGlobalInterceptors(new TransformInterceptor());

  const config = new DocumentBuilder()
    .setTitle("Example API")
    .setDescription("The example API description")
    .setVersion("1.0")
    .addBearerAuth()
    .build();
  const document = SwaggerModule.createDocument(app, config);
  SwaggerModule.setup("api", app, document);
  await app.listen(8000);
}
bootstrap();