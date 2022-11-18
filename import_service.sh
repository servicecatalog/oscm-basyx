#!/bin/bash

source testvars.conf

# Create a service payload for the creation of
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
    echo "The Marketable Service was created successfully."
    MS_KEY=$(cat /tmp/response.txt | sed -n 's|.*"createdObjectId":"\([^"]*\)".*|\1|p')
    echo "The created service key is : ${MS_KEY}."
    printf "MS_KEY=$MS_KEY"$'\n' >> testvars.conf
fi

rm -f /tmp/response.txt
rm -f /tmp/new_marketable_service.json


