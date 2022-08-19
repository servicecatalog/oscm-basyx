#!/bin/bash
echo "Starting OSCM ..."
process_id=$!
docker-compose -f docker-compose-oscm-basyx.yml up -d
wait $process_id

echo "Starting Proxy..."
docker-compose -f ./proxy/docker-compose-proxy.yml up -d
wait $!
echo "OSCM is up."
