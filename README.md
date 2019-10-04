The Buster API. Here at dave we work a lot with third party data providers who aren't always the most consistent and reliable. For this test we have created an third party api that mimics the things we do regurarly here. 


THE BUSTER API
The buster api lives here: https://5b2f73d1.ngrok.io
If you make a GET request to that url you should get back something like { ok: true }

Your available endpoints are:

POST /v1/api_key  -  creates an api key

	POST BODY PARAMS
		webhookUrl - the url to send transaction status updates to

	RESPONSE BODY
      webhookUrl: the webhook url we will send webhooks to,
      key: The registered api key for this user ( must be included in all transaction request headers )

After you have created an API key, all future requests need to be authenticated by adding the header 
	X-API-KEY = your key


POST /v1/transaction
	AUTH
		uses header X-API-KEY

	POST BODY PARAMS
		referenceId - A unique alpha numeric string between 1 and 32 characters

	RESPONSE BODY
      	id - the id used to retrieve this transaction
      	created - timestamp for the transaction
      	status - the status of the transaction
      	referenceId - your provided reference id

POST /v1/transaction
	AUTH
		uses header X-API-KEY

	POST BODY PARAMS
		referenceId - A unique alpha numeric string between 1 and 32 characters

	RESPONSE BODY
      	id - the id used to retrieve this transaction
      	created - timestamp for the transaction
      	status - the status of the transaction
      	referenceId - your provided reference id

GET /v1/transaction/<transaction_id>
	AUTH
		uses header X-API-KEY
	
	URL PARAMS
		transaction_id - the id of the transaction returned from POST /v1/transaction

	RESPONSE BODY
		- see POST /v1/transaction RESPONSE BODY

GET /v1/transaction?referenceId=<reference_id>
	AUTH
		uses header X-API-KEY
	
	URL QUERY PARAMS
		reference_id - the supplied reference id for the transaction

	RESPONSE BODY
		- see POST /v1/transaction RESPONSE BODY

WEBHOOKS
	Webhooks will make a post request to the url provided in the request to create an api key

	WEBHOOK BODY
     	type - 'TRANSACTION_UPDATE' is the only type right now
      	data
      		id - the id used to retrieve this transaction
      		created - timestamp for the transaction
      		status - the status of the transaction
      		referenceId - your provided reference id

    EXAMPLE
    	{
			"type": "TRANSACTION_UPDATE",
				"data": {
				"id": "2138056f-72bf-45c4-8c27-395101a1c468",
				"created": "2019-10-03T22:18:52.000Z",
				"status": "PENDING",
				"referenceId": "im_a_reference"
			}
		}


Your job is to create an api with 2 endpoints.

1. Create a transaction, this endpoint should respond with the created transaction, or an appropriate error.

2. Fetch Transactions, I should be able to retrieve all the transactions i have created
