# spark-xml-parse
Demonstration of XML parsing using the StackOverflow data dump.

## Overview
This is a simple Spark app that reads a `Posts.xml` input file from
one of the 
[StackExchange data dumps](https://archive.org/details/stackexchange); 
the XML schema description can be found 
[here](https://ia600500.us.archive.org/22/items/stackexchange/readme.txt).

The code attempts to parse one `row` XML element in each line; if a
row is parsed, its `Body`, `CreationDate`, and `ViewCount` attributes
are queried. For each successful parse, a compact JSON record is
written onto a single line in the files of the output directory.