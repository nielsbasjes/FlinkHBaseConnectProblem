#!/usr/bin/env bash

mvn clean package

flink \
    run \
    -m yarn-cluster \
    --yarncontainer 1 \
    --yarnname "Flink on Yarn HBase problem" \
    --yarnslots                     1     \
    --yarnjobManagerMemory          4000  \
    --yarntaskManagerMemory         4000  \
    --yarnstreaming                       \
    target/flink-hbase-connect-1.0-SNAPSHOT.jar

