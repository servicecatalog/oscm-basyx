#!/bin/bash

source testvars.conf

if [[ -z $MS_KEY ]]; then
  echo "***The key of the Service for publishing the service is not set."
  exit 1
fi

MPID=11000
curl -ku "$USER:$PASS" -X PUT "https://$OSCM_HOST:8081/oscm-rest-api/v1/marketplaces/$MPID/entries/$MS_KEY" -H  "accept: */*" -H  "Content-Type: application/json" -d "{\"anonymousVisible\":true,\"visibleInCatalog\":true,\"etag\":1}"