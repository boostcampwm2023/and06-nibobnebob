import { ExecutionContext, createParamDecorator } from "@nestjs/common";

export interface TokenInfo {
  id: number;
}

export const GetUser = createParamDecorator(
  (data, ctx: ExecutionContext): TokenInfo => {
    const req = ctx.switchToHttp().getRequest();
    return req.user;
  }
);
