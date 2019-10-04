'use strict';

var dbm;
var type;
var seed;

/**
 * We receive the dbmigrate dependency from dbmigrate initially.
 * This enables us to not have to rely on NODE_PATH.
 */
exports.setup = function(options, seedLink) {
  dbm = options.dbmigrate;
  type = dbm.dataType;
  seed = seedLink;
};

exports.up = function(db) {
  return db.createTable('transaction', {
    id: {
      type: type.STRING,
      length: 256,
      primaryKey: true,
    },
    reference_id: {
      type: type.STRING,
      length: 64,
      unique: true,
    },
    external_id: {
      type: type.STRING,
      length: 64,
      unique: true,
    },
    status: {
      type: type.STRING,
      length: 32,
    },
    created: {
      type: type.DATE_TIME,
      defaultValue: 'CURRENT_TIMESTAMP',
      notNull: true,
    },
  });
};

exports.down = function(db) {
  return db.dropTable('transaction');
};

exports._meta = {
  version: 1,
};
