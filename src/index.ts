import * as express from 'express';
import { router } from './router';
import { handleError } from './middleware/handle-error';
import * as bodyParser from 'body-parser';

const app = express();

app.use(bodyParser.json({ limit: '10mb' }));

app.get('/', (req, res) => {
  return res.send({ ok: true });
});

app.use('/', router);

app.use(handleError);

app.listen(4242, () => {
  console.log('listening');
});
