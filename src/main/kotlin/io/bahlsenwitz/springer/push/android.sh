#!/bin/bash
#
# prefix: "chmod +rx "
#
# RESOURCES:
#
# https://icons8.com/icon/set/notification/material
# https://console.firebase.google.com/u/0/project/bahlsenwitz/notification
# https://console.firebase.google.com/u/0/project/bahlsenwitz/settings/cloudmessaging
#

NOTE_KEY=$1

curl -X POST \
	--header "authorization: key=AAAAf2p1_k8:APA91bFFpyIHjZSDaE1ziE9fdWrxmtMuAHJWse1mSfHMz_sl1OucVpxhy9h7HPXi9HVrXpn0J4MxAvhsnf8XdlAn7hAZaRfz1NtXjab0Vi71o-MKWaRPon23cJuejW7NQ67N47_FH3D2" \
    --header "content-type: application/json" \
    https://fcm.googleapis.com/fcm/send \
    -d "{\"to\":\"${NOTE_KEY}\",\"data\":{\"body\":\"Your move.\"},\"priority\":1}"
