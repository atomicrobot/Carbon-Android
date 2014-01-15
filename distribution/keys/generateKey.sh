#!/bin/sh
#
# Generates a key suitable for signing an Android app.  You need to provide
# a name and will be prompted with a bunch of certificate questions.  You'll
# get a keystore and properties file with the passwords IN PLAIN TEXT.
#
# Usage: generateKey.sh name
#
function generateRandomKey()
{
	local randomKey=`env LC_CTYPE=C tr -dc "a-zA-Z0-9-_\$\?" < /dev/urandom | head -c 64`
	echo "$randomKey"
}

function generateKey()
{
	local name=$1
	local keystorePassword=$2
	local aliasPassword=$3
	keytool -genkey -v -keystore ${name}/${name}.keystore -alias ${name} -keyalg RSA -keysize 2048 -validity 10000 -storepass ${keystorePassword} -keypass ${aliasPassword}
}

name=${1}
keystorePassword=$(generateRandomKey)
aliasPassword=$(generateRandomKey)
propertiesFile=${name}/${name}.properties

mkdir ${name}
touch ${propertiesFile}
generateKey ${name} ${keystorePassword} ${aliasPassword}
echo "keystorePassword=$keystorePassword" >> ${propertiesFile}
echo "aliasPassword=$aliasPassword" >> ${propertiesFile}