# noun-indexer
This Java program will parse a book in the form of an XML or PDF file and create an index of important terms and the pages on which each term occurs. Categories of terms, like organizations, people, dates, times, etc. can be excluded from the index. This program uses the [Processing core library](https://github.com/processing/processing) and the [Stanford Named Entity Recognizer](https://github.com/dat/stanford-ner).

# XML Input format
The ABBYY GZ download option from https://archive.org/details/midsummernightsd05shak is an acceptable XML input file.

# Repository Structure
The /ProcessingUI folder is a folder containing an Eclipse Java project, with a few additional github files like README files.

# To run a compiled version
1. Navigate to the releases page of this github repo.
2. Download and unzip the ReleaseUser.zip download.
3. Follow the instructions in the README.md file contained in the download.

# Notes for running from source code
All of the dependencies are .jar files stored in the /ProcessingUI/lib folder. There are files missing from the github repo because said files are too large for storage on github. They are the english.muc.7class.distsim.crf.ser and english.muc.7class.distsim.prop files of the /ProcessingUI/classifiers folder. They are in the classifiers folder of the ReleaseUser.zip download in the releases page of this github repo. Most file paths are hard-coded, so it's recommended to keep the original file structure when first running the code. The /ProcessingUI/testdata folder is deceptively named testdata. Input files need not be located in that folder, and the output files are created in that folder, despite not being test data.