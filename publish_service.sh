#!/bin/bash

MPID="10001"
curl -ku "$USER:$PASS" -X PUT "https://$OSCM_HOST:8081/oscm-rest-api/v1/marketplaces/$MPID/entries/$SID" -H  "accept: */*" -H  "Content-Type: application/json" -d "{\"anonymousVisible\":true,\"visibleInCatalog\":true,\"etag\":1}"