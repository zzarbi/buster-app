import * as request from 'superagent';

const BUSTER_API_URL = process.env.BUSTER_API_URL;

export function createApiKey(webhookUrl: string) {
  return request
    .post(`${BUSTER_API_URL}/v1/api_key`)
    .send({ webhookUrl })
    .then(res => res.body);
}

export function createTransaction(referenceId: string, apiKey: string) {
  return request
    .post(`${BUSTER_API_URL}/v1/transaction`)
    .set({ ['X-API-KEY']: apiKey })
    .send({ referenceId })
    .then(res => res.body);
}

export function getTransaction(externalId: string, apiKey: string) {
  return request
    .get(`${BUSTER_API_URL}/v1/transaction/${externalId}`)
    .set({ ['X-API-KEY']: apiKey })
    .then(res => res.body);
}

export function getTransactionByReferenceId(referenceId: string, apiKey: string) {
  return request
    .get(`${BUSTER_API_URL}/v1/transaction`)
    .set({ ['X-API-KEY']: apiKey })
    .query({ referenceId })
    .then(res => res.body);
}
