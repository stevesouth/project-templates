#!/bin/bash
#
# Start the PricingExample Java DataSource.
#
# $1 - Path to java executable
# $2 - Path to datasource XML file
# $3 - Path to fields XML file
# $4 - Java definitions ( optional )
#
# Returns the process id of the Java process.
#

if [ "$1" = "CONFREADER" ]; then
   shift
   confreading=1
   jar=`ls "$BINARY_ROOT"/lib/datasource-java*.jar|head -1`
else
   confreading=0
   jar="${BINARY_ROOT}/lib*"

   echo "Classpath: $jar"
fi

if [ $confreading = 1 ]; then
   java -jar "$jar" "$@"
   exit $?
else
   java -cp "$jar" "$@" > "$LOGDIR"/java$BLADENAME.log 2>&1 &
   echo $!
fi
