import { DATE, STRING, INTEGER } from 'sequelize';
import { Column, Model, Table } from 'sequelize-typescript';
import { ISerializable } from '../typings/serialization';

export type TransactionResponse = {
  id: number;
  created: string;
  referenceId: string;
  status: TransactionStatus;
};

export enum TransactionStatus {
  PENDING = 'PENDING',
  CANCELED = 'CANCELED',
  COMPLETED = 'COMPLETED',
  CREATED = 'CREATED',
}

@Table({
  tableName: 'transaction',
  updatedAt: false,
})
export default class Transaction extends Model<Transaction>
  implements ISerializable<TransactionResponse> {
  @Column({
    type: INTEGER,
    field: 'id',
    primaryKey: true,
  })
  public id!: number;

  @Column({
    type: STRING(32),
    field: 'reference_id',
  })
  public referenceId!: string;

  @Column({
    type: STRING(32),
    field: 'external_id',
  })
  public externalId!: string;

  @Column({
    type: STRING(32),
    field: 'status',
  })
  public status!: TransactionStatus;

  @Column({
    type: DATE,
  })
  public created!: Date;

  public serialize() {
    return {
      id: this.id,
      created: this.created.toISOString(),
      status: this.status,
      referenceId: this.referenceId,
    };
  }
}
