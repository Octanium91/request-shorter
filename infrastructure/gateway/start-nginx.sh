#!/usr/bin/env bash

echo "## START NGINX APP"
envsubst "`printf '${%s} ' $(bash -c "compgen -A variable")`" < /etc/nginx/conf.d/conf.nginx > /etc/nginx/conf.d/default.conf
#exec /etc/nginx/reload.sh &
#exec nginx
#echo "## START WHILE"
#while :; do sleep 6h & wait $${!}; nginx -s reload; done & nginx -g \"daemon off;\"
#echo "## SCRIPT END"
while :; do sleep 1m && nginx -s reload; done & nginx
