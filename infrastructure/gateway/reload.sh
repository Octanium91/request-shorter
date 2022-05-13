#!/bin/bash
###########

while true
do
  sleep 6h
  echo "Executing: nginx -s reload"
  nginx -s reload
done