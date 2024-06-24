#!/usr/bin/python

"""
This tool has not been written by me nor I claim ownership of any of the code below. It has been extracted
from the link provided below and used unmodified:

    https://gist.github.com/jerrygb/c4d786a8f7dc8385e3cb4808d592aece
"""

import os
import sys

import requests

schema_registry_url = sys.argv[1]
topic = sys.argv[2]
schema_file = sys.argv[3]

aboslute_path_to_schema = os.path.join(os.getcwd(), schema_file)

print("Schema Registry URL: " + schema_registry_url)
print("Topic: " + topic)
print("Schema file: " + schema_file)
print

with open(aboslute_path_to_schema, 'r') as content_file:
    schema = content_file.read()

payload = "{ \"schema\": \"" \
          + schema.replace("\"", "\\\"").replace("\t", "").replace("\n", "") \
          + "\" }"

url = schema_registry_url + "/subjects/" + topic + "-value/versions"
headers = {"Content-Type": "application/vnd.schemaregistry.v1+json"}

r = requests.post(url, headers=headers, data=payload)
if r.status_code == requests.codes.ok:
    print("Success")
else:
    r.raise_for_status()
