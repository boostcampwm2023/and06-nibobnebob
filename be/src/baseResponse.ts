export class BaseResponse<T> {
  constructor(
    public data: T,
    public message: string,
    public statusCode: number
  ) {}
}
