#!/usr/bin/env python3

# This script reads news-word relationship data and generates
# csv files to be imported to the graph database

import csv
import sys
import re
from tqdm import tqdm
import numpy as np

# data paths
datafile_path = "../data/"
wnp_path = datafile_path + "article_word_ne_pos.csv" #
an_path = datafile_path + "articles.csv" #
ne_path = datafile_path + "ner_tags.csv" #
ps_path = datafile_path + "pos_tags.csv" #
wn_path = datafile_path + "words.csv" #
awr_path = datafile_path + "awr.csv" 
wwr_path = datafile_path + "wwr.csv"#
newr_path = datafile_path + "newr.csv"
pswr_path = datafile_path + "pswr.csv"

# constants: defined for memory optimisation
aid_ID = "aid:ID" 
from_START_ID = "from:START_ID"
to_END_ID = "to:END_ID"
LABEL = ":LABEL"
TYPE = ":TYPE"

# fields
wnp_fields = [aid_ID, "word", "NamedEntity", "PartOfSpeech"]
ar_fields = [aid_ID, LABEL, "label"]
ne_fields = ["neid:ID", LABEL, "name"]
ps_fields = ["psid:ID", LABEL, "name"]
wn_fields = ["wid:ID", LABEL, "word"]
awr_fields = [from_START_ID, to_END_ID, TYPE]
wwr_fields = [from_START_ID, to_END_ID, TYPE, "contained_in:INT[]"]
newr_fields = [from_START_ID, to_END_ID, TYPE]
pswr_fields = [from_START_ID, to_END_ID, TYPE]

# Names of all the named entities
named_entities = ["ORGANIZATION",
                  "LOCATION",
                  "PERSON",
                  "PERCENT",
                  "MONEY",
                  "DATE",
                  "TIME",
                  'NATIONALITY',
                  'MISC',
                  'CITY',
                  'SET',
                  'URL',
                  'RELIGION',
                  'ORDINAL',
                  "CAUSE_OF_DEATH",
                  'NUMBER',
                  'CRIMINAL_CHARGE',
                  'STATE_OR_PROVINCE',
                  'DURATION',
                  'COUNTRY',
                  'TITLE',
                  'IDEOLOGY',
                  'O']

part_of_speech = ['ADD',
                  'AFX',
                  'CC',
                  'CD',
                  'DT',
                  'EX',
                  'FW',
                  'GW',
                  'HYPH',
                  'IN',
                  'JJ',
                  'JJR',
                  'JJS',
                  'LS',
                  'MD',
                  'NFP',
                  'NN',
                  'NNS',
                  'NNP',
                  'NNPS',
                  'PDT',
                  'POS',
                  'PRP',
                  'PRP$',
                  'RB',
                  'RBR',
                  'RBS',
                  'RP',
                  'SYM',
                  'TO',
                  'UH',
                  'VB',
                  'VBD',
                  'VBG',
                  'VBN',
                  'VBP',
                  'VBZ',
                  'WDT',
                  'WP',
                  'WP$',
                  'WRB',
                  '.',
                  '$',
                  ':',
                  '-LRB-',
                  '-RRB-',
                  "''",
                  '']

words = {}              # word : wid
named_entity_set = {}   # neid : set(wid)
part_of_speech_set = {}  # psid : set(wid)
article_words = {}      # aid  : set(wid)
words_order = {}        # aid  : list(wid)
news = []               # all news info after DictReader
dictWordRelations = {}  # (startid, endid) : set(aid)

_id = 0         # each node has unique id
dictArticleIds = {}

if __name__ == "__main__":
    # open files
    wnpfile = open(wnp_path, "r")
    anfile = open(an_path, "w+")
    nefile = open(ne_path, "w+")
    psfile = open(ps_path, "w+")
    wnfile = open(wn_path, "w+")
    awrfile = open(awr_path, "w+")
    wwrfile = open(wwr_path, "w+")
    newrfile = open(newr_path, "w+")
    pswrfile = open(pswr_path, "w+")

    # dict read/writes
    wnp = csv.DictReader(wnpfile, fieldnames=wnp_fields)
    an = csv.DictWriter(anfile, fieldnames=ar_fields)
    ne = csv.DictWriter(nefile, fieldnames=ne_fields)
    ps = csv.DictWriter(psfile, fieldnames=ps_fields)
    wn = csv.DictWriter(wnfile, fieldnames=wn_fields)
    awr = csv.DictWriter(awrfile, fieldnames=awr_fields)
    wwr = csv.DictWriter(wwrfile, fieldnames=wwr_fields)
    newr = csv.DictWriter(newrfile, fieldnames=newr_fields)
    pswr = csv.DictWriter(pswrfile, fieldnames=pswr_fields)

    # Write Headers
    an.writeheader()
    ne.writeheader()
    ps.writeheader()
    wn.writeheader()
    awr.writeheader()
    wwr.writeheader()
    newr.writeheader()
    pswr.writeheader()

    next(wnp)       # discard header

    for i in range(len(named_entities)):
        named_entity_set[i] = set()
        nedata = [_id, "NamedEntity", named_entities[i]]
        ne.writerow(dict(zip(ne_fields, nedata)))
        _id += 1

    for i in range(len(part_of_speech)):
        part_of_speech_set[i] = set()
        psdata = [_id, "PartOfSpeech", part_of_speech[i]]
        ps.writerow(dict(zip(ps_fields, psdata)))
        _id += 1

    for row in tqdm(wnp):
        if not row[aid_ID] in dictArticleIds:
            # Sampling, uncomment following 2 lines to do so
            # if int(row[aid_ID]) == 15000:
            #        break

            aid = _id
            dictArticleIds[row[aid_ID]] = aid
            articledata = [aid, "Article", 'article']
            an.writerow(dict(zip(ar_fields, articledata)))
            article_words[aid] = set()
            words_order[aid] = list()
            _id += 1

        word = row["word"]
        if word not in words:
            words[word] = _id
            wndata = [_id, "Word", word]
            wn.writerow(dict(zip(wn_fields, wndata)))
            _id += 1
            aid = dictArticleIds[row[aid_ID]]
        words_order[aid].append(words[word])
        article_words[aid].add(words[word])

        # assign the word to its named entity
        neidx = named_entities.index(row['NamedEntity'])
        if not words[word] in named_entity_set[neidx]:
            named_entity_set[neidx].add(words[word])

        # assign the word to its part of speech
        psidx = part_of_speech.index(row['PartOfSpeech'])
        if not words[word] in part_of_speech_set[psidx]:
            part_of_speech_set[psidx].add(words[word])

    # write news - words relationship
    i = 0
    for aid, word_ids in article_words.items():
        i += 1
        for word_id in word_ids:
            awrdata = [aid, word_id, "CONTAINS"]
            awr.writerow(dict(zip(awr_fields, awrdata)))

    for k, aid in dictArticleIds.items():
        for i in range(len(words_order[aid])-1):
            if (words_order[aid][i], words_order[aid][i+1]) in dictWordRelations:
                dictWordRelations[(words_order[aid][i],
                                   words_order[aid][i+1])].add(aid)
            else:
                dictWordRelations[(words_order[aid][i],
                                   words_order[aid][i+1])] = set([aid])

    for psid, new in part_of_speech_set.items():
        for word_id in new:
            newrdata = [word_id, psid, "IS"]
            newr.writerow(dict(zip(newr_fields, newrdata)))

    for neid, new in named_entity_set.items():
        for word_id in new:
            pswrdata = [word_id, neid, "IS"]
            pswr.writerow(dict(zip(pswr_fields, pswrdata)))

    for k, v in dictWordRelations.items():
        varray = ";".join(list(map(str, v)))
        wwrdata = [k[0], k[1], "FOLLOWED_BY", varray]
        wwr.writerow(dict(zip(wwr_fields, wwrdata)))

    # close files
    wnpfile.close()
    anfile.close()
    nefile.close()
    psfile.close()
    wnfile.close()
    awrfile.close()
    wwrfile.close()
    newrfile.close()
    pswrfile.close()
