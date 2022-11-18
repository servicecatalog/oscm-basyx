#!/bin/bash

source import_service.sh
#SID="12002"
if [[ -z $MS_KEY ]]; then
  echo "***The key of the Marketplace Service is not set."
  exit 1
fi
PRICEMODEL_PAYLOAD=$(cat ./src/main/resources/pm_payload.json)

curl -ku "$USER:$PASS" -X PUT "https://$OSCM_HOST:8081/oscm-rest-api/v1/services/$MS_KEY/pricemodel" -H  "accept: */*" -H  "Content-Type: application/json" -d "$PRICEMODEL_PAYLOAD"

#TODO handle successful code 204: Price model created successfully.
# TODO Handle error 500 (for example SID does not exist).