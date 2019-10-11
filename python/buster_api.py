import requests
import os
import sys

BUSTER_API_URL = os.getenv('BUSTER_API_URL', None)


def create_api_key(webhook_url):
    result = requests.post('{}/v1/api_key'.format(BUSTER_API_URL),
                           json={"webhookUrl": webhook_url})

    if result.status_code != 200:
        raise Exception(result.text)

    return result.json()


def create_transaction(reference_id, api_key):
    result = requests.post('{}/v1/transaction'.format(BUSTER_API_URL),
                           json={"referenceId": reference_id},
                           headers={'X-API-KEY': api_key})

    if result.status_code != 200:
        raise Exception(result.text)

    return result.json()


def get_transaction(external_id, api_key):
    result = requests.get('{}/v1/transaction/{}'.format(BUSTER_API_URL, external_id),
                          headers={'X-API-KEY': api_key})

    if result.status_code != 200:
        raise Exception(result.text)

    return result.json()


def get_transaction_by_reference_id(reference_id, api_key):
    result = requests.get('{}/v1/transaction?referenceId={}'.format(BUSTER_API_URL, reference_id),
                          headers={'X-API-KEY': api_key})

    if result.status_code != 200:
        raise Exception(result.text)

    return result.json()
