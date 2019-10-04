export abstract class ApiError extends Error {
  public abstract  statusCode: number;
}

export class InvalidParametersError extends ApiError {
  public statusCode = 400;
}
export class UnauthorizedError extends ApiError {
  public statusCode = 401;
}
export class InternalServerError extends ApiError {
  public statusCode = 500;
}
export class GatewayTimeoutError extends ApiError {
  public statusCode = 503;
}
export class NotFoundError extends ApiError {
  public statusCode = 404;
}
