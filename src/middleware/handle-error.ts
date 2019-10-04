import { Request, Response, NextFunction } from 'express';
import { ApiError } from '../lib/error';

export async function handleError(error: Error, req: Request, res: Response, next: NextFunction) {
  console.log(error.message);
  if (error instanceof ApiError) {
    return res.status(error.statusCode).send(error.message);
  }

  return res.status(500).send(error.message);
}
