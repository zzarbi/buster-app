import * as config from 'config';
import { Sequelize } from 'sequelize-typescript';
import { isProdEnv, isStagingEnv } from './utils';

export function typeCast(field: any, next: any) {
  if (field.type === 'NEWDECIMAL') {
    const result = field.string();
    return result ? parseFloat(result) : null;
  }
  if (field.type === 'TINY' && field.length === 1) {
    return field.string() === '1'; // 1 = true, 0 = false
  }
  return next();
}

export const sequelize = new Sequelize({
  database: config.get('db.name'),
  username: config.get('db.username'),
  password: config.get('db.password'),
  dialect: 'mysql',
  logging: config.get('db.logQueries') ? console.log : false,
  host: config.get('db.host'),
  port: config.get('db.port'),
  dialectOptions: {
    socketPath: (isProdEnv() || isStagingEnv()) && config.get('db.socketpath'),
    multipleStatements: !isProdEnv() && !isStagingEnv(),
    charset: 'utf8mb4',
    typeCast,
  },
  define: {
    charset: 'utf8mb4',
    timestamps: true,
    createdAt: 'created',
    updatedAt: 'updated',
    freezeTableName: true,
  },
  pool: {
    max: parseInt(config.get('db.connectionLimit'), 10) || 5,
  },
});
