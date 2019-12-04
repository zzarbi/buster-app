package busterapp.util;

import org.sql2o.Sql2o;
import busterapp.transaction.model.TransactionDao;

public class DaoHelper {
    private static TransactionDao transactionModel = null;
    private static Sql2o db = null;

    /**
     * Create and return a unique DB connection
     * @todo read params from environement
     * 
     * @return Sql2o
     */
    private static Sql2o getDbConnection() {
        if (DaoHelper.db == null) {
            DaoHelper.db = new Sql2o("jdbc:mysql://" + EnvHelper.getMysqlHost() + ":" + EnvHelper.getMysqlPort() + "/" + EnvHelper.getMysqlDB(),
                EnvHelper.getMysqlUsername(),
                EnvHelper.getMysqlPassword());
        }

        return DaoHelper.db;
    }

    /**
     * Instanzialize a Transaction model
     * @return TransactionDao
     */
    public static TransactionDao getTransactionDao() {
        if (DaoHelper.transactionModel == null) {
            transactionModel = new TransactionDao(DaoHelper.getDbConnection());
        }

        return DaoHelper.transactionModel;
    }
}