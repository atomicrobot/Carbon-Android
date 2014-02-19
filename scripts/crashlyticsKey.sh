#!/bin/sh

echo "Crashlytics username (e-mail):"
read email

echo "Crashlytics password:"
read -s password

data="{\"email\":\"${email}\",\"password\":\"${password}\"}"

curl \
  --header "Content-Type: application/json" \
  --header "X-CRASHLYTICS-DEVELOPER-TOKEN: ed8fc3dc68a7475cc970eb1e9c0cb6603b0a3ea2" \
  --data ${data} \
  https://api.crashlytics.com/api/v2/session.json | python -mjson.tool
