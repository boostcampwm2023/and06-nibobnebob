import { Injectable } from "@nestjs/common";
import * as AWS from "aws-sdk";
import { awsConfig } from "objectStorage.config";
import * as sharp from "sharp";

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

  async uploadToS3(path: string, data: Buffer) {

    try {
      const resizedBuffer = await sharp(data)
        .resize(256, 256)
        .toBuffer();

      await this.s3.putObject({
        Bucket: awsConfig.bucket,
        Key: path,
        Body: resizedBuffer,
      }).promise();
    } catch (error) {
      throw error;
    }
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
