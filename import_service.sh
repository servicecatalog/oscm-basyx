#!/bin/bash

if [[ -z $OSCM_HOST ]]; then
  echo "***OSCM_HOST is not set. OSCM_HOST=<fully qualified name of your OSCM server>."
  exit 1
fi

TSID=$1
USER=${2:-$API_USER_KEY}
PASS=${3:-$API_PASS}

if [[ -z $USER || -z $PASS || -z $1 ]]; then
  echo "***Missing arguments. Call $0 <AAS_Id> [<User> <Password>]."
  exit 1
fi

# Export the json for a MarketableService to a file: new_marketable_service.json and use this as the payload
curl -ku "$USER:$PASS" -X GET https://$OSCM_HOST:8881/discovery/mservice/json/$TSID -H "accept: application/problem+json" > /tmp/new_marketable_service.json
SERVICE_PAYLOAD=$(cat /tmp/new_marketable_service.json)
echo $SERVICE_PAYLOAD

HTTP_CODE=$(curl -o /tmp/response.txt -w "%{http_code}" -ku "$USER:$PASS" -X POST "https://$OSCM_HOST:8081/oscm-rest-api/v1/services" -H  "accept: */*" -H  "Content-Type: application/json" -d "$SERVICE_PAYLOAD")

if [[ ${HTTP_CODE} -ne 201 ]];then
    echo "***Creation of Marketable Service failed with HTTP error code: ${HTTP_CODE}."
    ERROR_DETAILS=$(cat /tmp/response.txt | sed -n 's|.*"errorDetails":"\([^"]*\)".*|\1|p')
    echo $ERROR_DETAILS
    exit 1
else
    echo "The Marketable Service Creation was successfull.\n"
    MS_KEY=$(cat /tmp/response.txt | sed -n 's|.*"createdObjectId":"\([^"]*\)".*|\1|p')
    echo "The created service key is : ${MS_KEY}."
fi

rm -f /tmp/response.txt
rm -f /tmp/new_marketable_service.json


