import { ExecutionContext, createParamDecorator } from "@nestjs/common";

interface TokenInfo {
    nickname: string;
}

export const GetUser = createParamDecorator((data, ctx: ExecutionContext): TokenInfo => {
    const req = ctx.switchToHttp().getRequest();
    return req.user;
})