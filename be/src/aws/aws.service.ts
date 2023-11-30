import { Injectable } from '@nestjs/common';
import * as AWS from 'aws-sdk';
import { awsConfig } from 'objectStorage.config';

@Injectable()
export class AwsService {
    private s3: AWS.S3;

    constructor() {
        this.s3 = new AWS.S3({
            endpoint: awsConfig.endpoint,
            region: awsConfig.region,
            credentials: {
                accessKeyId: awsConfig.accessKey,
                secretAccessKey: awsConfig.secretKey
            },
        });
    }
}