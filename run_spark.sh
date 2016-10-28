#!/bin/bash

if [ $# -ne 0 ]; then
  echo $0: "usage: ./$0" 
  exit 1
fi

echo Using Spark from $SPARK_HOME

#input=resources/Posts.xml
input=stack/productivity/Posts.xml
output=tmp/posts

$SPARK_HOME/bin/spark-submit \
    --class XMLParseApp \
    --master yarn \
    target/scala-2.11/spark-xml-parse_2.11-1.0.jar $input $output 
