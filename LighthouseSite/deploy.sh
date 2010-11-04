#!/bin/sh 

RSYNC=/usr/bin/rsync 
RUSER=lighthouse 
RHOST=calico.ics.uci.edu 
RPATH=/home/lighthouse/public_html/updates 
LPATH=. 

$RSYNC --exclude=deploy.sh -az $LPATH -e ssh $RUSER@$RHOST:$RPATH 
