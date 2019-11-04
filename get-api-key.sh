#!/bin/bash
curl -XPOST -H "Content-Type: application/json" http://34.102.239.194/v1/api_key -d '{"webhookUrl": "'"$1"'" }'