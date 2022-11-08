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

TS_KEY=$(curl -ku "$USER:$PASS" -X GET "https://$OSCM_HOST:8881/discovery/techservice/id/$TSID" -H "accept: application/problem+json")
if [[ $TS_KEY == *"errorMsg"* ]]; then
  echo "Not found!"
  unset TS_KEY
else
  curl -ku "$USER:$PASS" -X DELETE "https://$OSCM_HOST:8081/oscm-rest-api/v1/technicalservices/$TS_KEY" -H  "accept: */*"
  echo "Deleted Technical Service $TS_KEY."
fi

curl -ku "$USER:$PASS" -X GET https://$OSCM_HOST:8881/discovery/techservice/json/$TSID -H "accept: application/problem+json"  > /tmp/new_service.json

sed -i s/"\\\u003d"/"="/g  /tmp/new_service.json
sed -i s/"\\\u003c"/"<"/g  /tmp/new_service.json
sed -i s/"\\\u003e"/">"/g  /tmp/new_service.json
sed -Ei "s/.*\[(.*)\].*/\1/1" /tmp/new_service.json

XML_BODY=$(cat /tmp/new_service.json) 

curl -ku "$USER:$PASS" -X PUT "https://$OSCM_HOST:8081/oscm-rest-api/v1/technicalservices/import" -H "accept: */*" -H  "Content-Type: application/json" -d "$XML_BODY"

TS_KEY=$(curl -ku "$USER:$PASS" -X GET "https://$OSCM_HOST:8881/discovery/techservice/id/$TSID" -H "accept: application/problem+json")

rm -f /tmp/new_service.json
echo "Imported $TS_KEY."

