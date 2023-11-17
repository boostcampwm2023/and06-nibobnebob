import { ExecutionContext, createParamDecorator } from "@nestjs/common";

export interface TokenInfo {
  nickName: string;
}

export const GetUser = createParamDecorator(
  (data, ctx: ExecutionContext): TokenInfo => {
    const req = ctx.switchToHttp().getRequest();
    return req.user;
  }
);
