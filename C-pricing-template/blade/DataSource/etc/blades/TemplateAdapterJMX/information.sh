#!/bin/bash

DIRNAME=`dirname "$0"`

if [ "$datasource_rmi_registry_port" = "" -a "$1" != "noget" ]; then
   OLDDIR=`pwd`
   DIRNAME=`dirname "$0"`
   cd "$DIRNAME"/../..
   PROGROOT=`pwd`
   cd "$OLDDIR"

   if [ `uname | grep -n CYGWIN` ]; then
      iscygwin=1
   else
      iscygwin=0
   fi

   . "$PROGROOT"/tools/information.sh
   get_core_info
fi
if [ "$datasource_rmi_registry_port" != "" ]; then
   echo "   To monitor the TemplateAdapter connect the JMX monitoring tool to RMI port $datasource_rmi_registry_port on $datasource_host"
   echo
fi
