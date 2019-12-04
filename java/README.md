# BUSTER Java APP

This app can create transaction trought the Buster-API and stored them in MySQL.
It keeps each transaction updated trought a webhook (http)


## The Java App API
The buster api lives here: http://localhost:4567
If you make a GET request to http://localhost:4567/transaction you should:

```json
{
    "code": 200,
    "transactions": []
}
```

Endpoints are:

POST /transaction  -  creates a transaction

	POST BODY PARAMS
		None

	RESPONSE BODY Sample

```json
{
    "code": 201,
    "transaction": {
        "id": 1,
        "reference_id": "a73d24d640344b2cb2dbc980bc519c92",
        "external_id": "fd99cd3c-105d-4cd2-80d9-c246cb36574a",
        "created": "2019-12-04 02:42:27.0",
        "status": "CREATED"
    }
}
```
    It will try to create one transaction and in case of error from the source it will attempt a few more time for a certain amount of time.
      id: An integer of the Primary Key of the DB
      reference_id: A unique random id
      external_id: A unique id of the transaction from Buster-API
      created: Date of the creation of the record in the database
      status: 



GET /transaction - retrieves a list of transactions 

	QUERY PARAMS
		Limit - A positive value, the default is set at 100
        Offset - A positive value, the default is set at 0

	RESPONSE BODY Sample
```json
{
    "code": 200,
    "transactions": [
        {
            "id": 1,
            "reference_id": "a73d24d640344b2cb2dbc980bc519c92",
            "external_id": "fd99cd3c-105d-4cd2-80d9-c246cb36574a",
            "created": "2019-12-04 02:42:27.0",
            "status": "CANCELED"
        }
    ]
}
```
      	transactions - A list of the las transactions stored

POST /webhooks

	POST BODY DATA Sample

```json
{
    "type": "TRANSACTION_UPDATE",
    "data": {
        "id": "91ff5bfb-c9f3-42d4-a5af-e142f6e6e63e",
        "created": "2019-12-03T23:49:56.000Z",
        "status": "PENDING",
        "referenceId": "8de1a13281d14272ac492708d98dbe16"
    }
}
```

	RESPONSE BODY

```json
{
    "code": 200
}
```

GET /healthcheck

	RESPONSE BODY Sample
```json
{
    "code": 200,
    "date": "2019-12-04T21:09Z"
}
```

## Wishlist/Todo
- Handle generating a new api key if the current one is failing
- Using caching technic, keep the last transaction in memory until status is CANCELED or COMPLETED (would avoid multiple db lookupand concurrency issue)
- Add updated_at field in the database, for tracking when a row was updated
- Make sure that Buster-API returns an updated_at datetime in order to make sure we only update the transaction with the latest information
- Update `util.Ngrok` to parse XML properly (instead of regex shortcut)
- Create an abstract contoller for basic operation like returning JSON and reading query/body params (alternatively find a plugin to do that)
- Figure out concurrency: Java/Jetty can be setup to have multiple thread so one docker instance could handle multiple request
- Add unitest, right now the only thing that can be really unitested is the one DAO/BO object. Everything else would better benefit of a BDD test suite

## Known Bugs
- Java has issue accessing docker instance via their name. For instance while the the image of `buster-java-app` can ping `buster-java-app` (`ping buster-app-mysql`). For some reason Java cannot connect to `buster-app-mysql`. Had to use `host.docker.internal`, which mean we need to expose the ports of Ngrok and Mysql