#!/bin/bash
echo "Start reload script..."
while true
do
  sleep 1m
  echo "Executing: nginx -s reload"
  nginx -s reload
done