import { Test, TestingModule } from "@nestjs/testing";
import { INestApplication } from "@nestjs/common";
import * as request from "supertest";
import { AppModule } from "../src/app.module";

describe("AppController (e2e)", () => {
  let app: INestApplication;

  beforeEach(async () => {
    const moduleFixture: TestingModule = await Test.createTestingModule({
      imports: [AppModule],
    }).compile();

    app = moduleFixture.createNestApplication();
    await app.init();
  });

  it('/api/user', async () => {
    const userData = {
      email: "test@example.com",
      password: "1234",
      provider: "site",
      nickName: "test",
      region: "강남구",
      birthdate: "1234/56/78",
      isMale: true
    };

    const response = await request(app.getHttpServer())
      .post('/user')
      .send(userData)
      .expect(201);

    expect(response.statusCode).toBe(201);
  });

  afterAll(async () => {
    await app.close();
  });
});
