#!/bin/sh

sudo neo4j-admin import --database=neo4j --id-type=INTEGER \
  --nodes=../data/articles.csv \
  --nodes=../data/words.csv \
  --nodes=../data/ner_tags.csv \
  --nodes=../data/pos_tags.csv \
  --relationships=../data/wwr.csv \
  --relationships=../data/awr.csv \
  --relationships=../data/newr.csv \
  --relationships=../data/pswr.csv \
  --multiline-fields=true 
