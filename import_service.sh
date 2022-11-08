#!/bin/bash

source import.sh
# global variables section
# SID="12002"

# Export the json for a MarketableService to a file: new_marketable_service.json and use this as the payload
curl -ku "$USER:$PASS" -X GET https://$OSCM_HOST:8881/discovery/mservice/json/$TSID -H "accept: application/problem+json" > /tmp/new_marketable_service.json

SERVICE_PAYLOAD=$(cat /tmp/new_marketable_service.json)
CREATED_SERVICE_JSON=$(curl -ku "$USER:$PASS" -X POST "https://$OSCM_HOST:8081/oscm-rest-api/v1/services" -H  "accept: */*" -H  "Content-Type: application/json" -d "$SERVICE_PAYLOAD")
# TODO handle error 400 (Invalid technicalServiceId,
  Invalid JSON request body: "Invalid field parameter: PROVISIONING_SCRIPT is required and must not be empty",
  Object of class 'SERVICE' already exists with unique business key value'Festo_3S7PM0CP4BD_FROM_REST'"
}
# TODO handle error 201 (Successful ID created)
# TODO add parameters for Nameplate.

Example JSON Response:
{
  "createdObjectName": "Festo_3S7PM0CP4BD_FROM_REST",
  "createdObjectId": "12000"
}
# TODO handle successful service_json (Extract createdObjectId, or empty json).


SERVICE_JSON=$(curl -ku "$USER:$PASS -X GET "https://$OSCM_HOST:8081/oscm-rest-api/v1/services/$SID" -H  "accept: application/json")

rm -f /tmp/new_marketable_service.json
echo "Imported Marketable Service $SID."