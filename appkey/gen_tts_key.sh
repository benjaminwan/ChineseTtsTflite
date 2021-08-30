#!/bin/bash

export ALIAS="chttstf"
export FILENAME="$ALIAS.keystore"
export PASS="chttstf"

echo "gen android key"
keytool -genkey -alias $ALIAS -storetype PKCS12 -keyalg RSA -validity 10000 -keystore $FILENAME -keypass $PASS -storepass $PASS -dname "CN=benjaminwan, OU=dev, O=work, L=Quanzhou, ST=Fujian, C=86"