#!/bin/bash

SCRIPTPATH="$( cd -- "$(dirname "$0")" >/dev/null 2>&1 ; pwd -P )"
echo ${SCRIPTPATH}

echo "Stopping Proxy..."
process_id=$!
docker-compose -f ./proxy/docker-compose-proxy.yml down
wait $process_id
echo "Stopping OSCM..."
docker-compose -f ./docker-compose-oscm-basyx.yml down
wait $!
echo "OSCM stopped."

