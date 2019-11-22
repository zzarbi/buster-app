from datetime import datetime
from setup import db


class TransactionStatus:
    PENDING = 'PEDNING'
    CANCELED = 'CANCELED'
    COMPLETED = 'COMPLETED'
    CREATED = 'CREATED'


class Transaction(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    reference_id = db.Column(db.String(256), unique=True, nullable=False)
    external_id = db.Column(db.String(256), unique=True, nullable=True)
    status = db.Column(db.String(256), unique=False, nullable=False)
    created = db.Column(db.DateTime, unique=False, nullable=False, default=datetime.now())

    def __repr__(self):
        return '<Transaction %r>' % self.id

    def serialize(self):
        return {
            "id": self.id,
            "referenceId": self.reference_id,
            "externalId": self.external_id,
            "status": self.status,
            "created": self.created
        }


def create_transaction(reference_id,
                       status=TransactionStatus.PENDING,
                       external_id=None):
    trans = Transaction(reference_id=reference_id,
                        external_id=external_id,
                        status=status)
    db.session.add(trans)
    db.session.commit()

    return trans.serialize()


def update_transaction(trans_id, updates):
    trans = Transaction.query.filter_by(id=trans_id).first()
    if trans is None:
        raise Exception('Transaction not found with id: {}'.format(id))

    for key, value in updates.items():
        setattr(trans, key, value)

    db.session.commit()

    return trans.serialize()

def update_transaction_by_reference_id(reference_id, updates):
    trans = Transaction.query.filter_by(reference_id=reference_id).first()
    if trans is None:
        raise Exception('Transaction not found with reference_id: {}'.format(reference_id))

    for key, value in updates.items():
        setattr(trans, key, value)

    db.session.commit()

    return trans.serialize()


def get_transaction(trans_id):
    trans = Transaction.query.filter_by(id=trans_id).first()

    return trans.serialize()

def get_transaction_by_reference_id(reference_id):
    trans = Transaction.query.filter_by(reference_id=reference_id).first()

    return trans.serialize()


def get_all_transactions():
    trans = Transaction.query.all()

    result = []
    for transaction in trans:
        result.append(transaction.serialize())

    return result




