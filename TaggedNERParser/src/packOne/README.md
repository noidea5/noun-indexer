#TaggedNERParser

ClassOne reads a file from the hard drive and prints to the console an index of terms tagged by the NER and page numbers where the terms appear.

Before running the source code, change line 14 so that inputFileAbsPath stores the absolute file path of the tagged text file.

#Input Formatting

The input text file must include <p#> tags where # is the page number recorded for any terms that follow the tag and precede other page tags. The input text must include tags according to the output of the NER File -> Save Tagged File As option.