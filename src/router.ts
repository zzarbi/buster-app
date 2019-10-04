import * as PromiseRouter from 'express-promise-router';

export const router = PromiseRouter();

router.post('/transaction', (req, res) => {
  // endpoint for creating a transaction
});

router.get('/transaction', (req, res) => {
  // endpoint for retrieving all transactions
});

router.get('/webhooks', (req, res) => {
  // endpoint for receiving webhooks
});
