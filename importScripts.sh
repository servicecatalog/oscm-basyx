#!/bin/bash

./import.sh $1 $2 $3
./import_service.sh $1 $2 $3
./create_pricemodel.sh $1 $2 $3
./publish_service.sh $1 $2 $3