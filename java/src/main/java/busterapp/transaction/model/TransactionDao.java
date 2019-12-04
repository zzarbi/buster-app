package busterapp.transaction.model;

import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public class TransactionDao {

    private Sql2o sql2o;

    public TransactionDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    public boolean create(String referenceId, String externalId, String status) {
        try (Connection conn = sql2o.beginTransaction()) {
            conn.createQuery("insert into transaction (reference_id, external_id, status) VALUES (:reference_id, :external_id, :status)")
                    .addParameter("reference_id", referenceId)
                    .addParameter("external_id", externalId)
                    .addParameter("status", status)
                    .executeUpdate();
            conn.commit();
            return true;
        }
    }

    public List<Transaction> getAllReversed(int limit, int offset) {
        try (Connection conn = sql2o.open()) {
            List<Transaction> transactions = conn.createQuery("select * from transaction order by id desc limit " + offset + ", " + limit)
                    .executeAndFetch(Transaction.class);
            return transactions;
        }
    }

    public Optional<Transaction> getByReferenceId(String referenceId) {
        try (Connection conn = sql2o.open()) {
            List<Transaction> transactions = conn.createQuery("select * from transaction where reference_id=:reference_id")
                    .addParameter("reference_id", referenceId)
                    .executeAndFetch(Transaction.class);
            if (transactions.size() == 0) {
                return Optional.empty();
            } else if (transactions.size() == 1) {
                return Optional.of(transactions.get(0));
            } else {
                throw new RuntimeException();
            }
        }
    }

    public void updateStatusByReferenceId(String referenceId, String status) {
        try (Connection conn = sql2o.open()) {
            conn.createQuery("update transaction set status=:status where reference_id=:reference_id")
                    .addParameter("reference_id", referenceId)
                    .addParameter("status", status)
                    .executeUpdate();
        }
    }

}