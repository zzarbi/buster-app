from flask import jsonify, request, abort
from setup import app
from buster_api import create_api_key, create_transaction, get_transaction_by_reference_id


@app.route('/')
def get_root():
    return jsonify({"ok": True})


@app.route('/transaction')
def get_transactions():
    if request.method == 'GET':
        # get all transactions
        key = create_api_key("ASdfasdadsffasd")
        trans = create_transaction('a12sdfasdfasdzzfzadsf', key['key'])

        t2 = get_transaction_by_reference_id(trans['referenceId'], key['key'])

        return jsonify(t2)

    elif request.method == 'POST':
        # nothing
        return jsonify({ "ok": True })

    else:
        abort(500)


@app.route('/webhooks')
def get_webhooks():
    if request.method == 'POST':
        # handle webhooks
        return jsonify({ "ok": True })

    else:
        abort(500)


if __name__ == '__main__':
    app.run(debug=True, port=8082, host='0.0.0.0')
