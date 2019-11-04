from flask import jsonify, request, abort
from setup import app
from buster_api import create_api_key, create_transaction, get_transaction_by_reference_id
import sys


##
# Regular print will not work you must use something like this to log
##
def log(item):
    print(item, file=sys.stdout)


@app.route('/')
def get_root():
    return jsonify({"ok": True})


@app.route('/transaction', methods=['POST', 'GET'])
def get_transactions():
    if request.method == 'GET':
        # get all transactions
        return jsonify([])

    elif request.method == 'POST':
        # nothing
        return jsonify({ "ok": True })

    else:
        abort(500)


@app.route('/webhooks', methods=['POST'])
def get_webhooks():
    if request.method == 'POST':
        # handle webhooks
        return jsonify({ "ok": True })

    else:
        abort(500)


if __name__ == '__main__':
    app.run(debug=True, port=8082, host='0.0.0.0')
