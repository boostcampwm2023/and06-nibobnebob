import { Injectable } from "@nestjs/common";
import * as AWS from "aws-sdk";
import { awsConfig } from "objectStorage.config";
import { v4 } from "uuid";

@Injectable()
export class AwsService {
  private s3: AWS.S3;

  constructor() {
    this.s3 = new AWS.S3({
      endpoint: awsConfig.endpoint,
      region: awsConfig.region,
      credentials: {
        accessKeyId: awsConfig.accessKey,
        secretAccessKey: awsConfig.secretKey,
      },
    });
  }

  async uploadToS3(path: string, data: Buffer){
    await this.s3.putObject({
      Bucket: awsConfig.bucket,
      Key: path,
      Body: data,
    }).promise();
  }

  getImageURL(path: string) {
    const signedUrl = this.s3.getSignedUrl("getObject", {
      Bucket: awsConfig.bucket,
      Key: path,
      Expires: 60,
    });

    return signedUrl;
  }
}
