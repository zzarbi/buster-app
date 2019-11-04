import { sequelize } from '../lib/sequelize';

import Transaction, { TransactionStatus } from './transaction';
import { NotFoundError } from '../lib/error';

const models = {
  Transaction,
};

// Necessary to call db operations directly via models
sequelize.addModels(Object.values(models));

type TransactionCreate = {
  referenceId: string,
  externalId: string,
  status: TransactionStatus,
}

export async function createTransaction(transaction: TransactionCreate) {
  const result = await Transaction.create(transaction);

  return result.serialize();
}

export async function updateTransaction(id: number, updates: Partial<TransactionCreate>) {
  const transaction = await Transaction.findByPk(id);

  if (!transaction) {
    throw new NotFoundError(`Transaction not found with id ${id}`);
  }

  await transaction.update(updates);

  return transaction.serialize();
}

export async function getTransaction(id: number) {
  const result = await Transaction.findByPk(id);

  return result!.serialize();
}

export async function getTransactionByReferenceId(referenceId: string) {
  const result = await Transaction.findOne({
    where: { referenceId }
  });

  return result!.serialize();
}

export async function getAllTransactions() {
  const result = await Transaction.findAll();

  return result.map(t => t.serialize());
}
