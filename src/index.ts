import * as express from 'express';
import { router } from './router';
import { handleError } from './middleware/handle-error';
import * as bodyParser from 'body-parser';
import { NextFunction, Request, Response } from 'express';

const app = express();

app.use(bodyParser.json({ limit: '10mb' }));

app.use((req: Request, res: Response, next: NextFunction) => {
  console.info({
    ip: req.ip,
    method: req.method,
    url: req.url,
    query: req.query,
    payload: req.body,
    headers: req.headers,
    endpoint: req.route ? req.route.path : req.originalUrl,
    logType: 'requestStart',
  });
  next();
});

app.get('/', async (req, res) => {
  return res.send({ ok: true });
});

app.use('/', router);

app.use(handleError);

app.listen(4242, () => {
  console.log('listening');
});
