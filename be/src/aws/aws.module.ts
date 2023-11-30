import { Global, Module } from '@nestjs/common';
import { AwsService } from './aws.service';

@Global()
@Module({
  providers: [AwsService],
  exports: [AwsService],
})
export class AwsModule {}
