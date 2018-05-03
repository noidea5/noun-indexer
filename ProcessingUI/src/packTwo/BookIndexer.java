package packTwo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class BookIndexer
{
	public static void processFile(File selection)
	{
		if(selection != null)
		{
			// TODO: initialize these final variables as necessary before running the
			// program
			final File TEXT_OUTPUT_LOCATION = new File("testdata/xmlToText.txt"); // the file that xmlParser will write
																					// the book plaintext to
			final String CLASSIFIER_LOCATION = "classifiers/english.muc.7class.distsim.crf.ser"; // the location of the
																									// classifier to be
																									// used by ner
			final File INDEX_OUTPUT_LOCATION = new File("testData/indexOutput.txt"); // the file that the index will be
																						// written to

			// initialize an XMLParser and use it to parse an input file
			XMLParser xmlParser = new XMLParser();
			File xmlOutputFile = TEXT_OUTPUT_LOCATION;
			xmlParser.parseBookXML(selection, xmlOutputFile);
			ArrayList<Integer> pageLocations = xmlParser.getPageLocations();		//TODO: change pageLocations to the number of characters on previous pages of the book

			// initialize a NERProcessor and use it to parse the text output of the
			// XMLParser
			NERProcessor ner = new NERProcessor();
			ner.initializeClassifier(CLASSIFIER_LOCATION);
			String bookPlaintext = readFileIntoString(xmlOutputFile.getAbsolutePath());
			ner.processText(bookPlaintext);

			// store output of ner in the HashMap termsIndex and also print output of ner
			ArrayList<String> termList = ner.getTermList();
			ArrayList<String> categoryList = ner.getCategoryList();
			ArrayList<Integer> termStartingIndices = ner.getTermStartingIndices();
			// System.out.println();
			// System.out.printf("%-18s[%7s, %7s)\t%s\n", "CATEGORY", "START", "END",
			// "TERM");
			// System.out.println();
			
			PageTagger pageFinder = new PageTagger();
			ArrayList<Integer> countingUp = new ArrayList<Integer>();
			
			for (int i = 0; i < pageLocations.size(); i++) {
				countingUp.add(i);
			}
			
			pageFinder.findPageNumbers(xmlOutputFile.getAbsolutePath(), countingUp, pageLocations);
			ArrayList<Integer> truePageNums = pageFinder.getPageNumbers();
			ArrayList<Integer> truePageLocs = pageFinder.getStartLocations();
			
			
			
			HashMap<String, ArrayList<Integer>> termsIndex = new HashMap<String, ArrayList<Integer>>();
			int currentPage = 0;
			for(int i = 0; i < termList.size(); i++)
			{
				String term = termList.get(i);
				String termNoNewLine = term.replace("\n", " ");

				// System.out.printf("%-18s[%7d, %7d)\t%s\n", categoryList.get(i),
				// termStartingIndices.get(i), (termStartingIndices.get(i) + term.length()),
				// termNoNewLine);

				// add term to termsIndex
				boolean RECORD_DUPLICATE_TERMS_ON_SAME_PAGE = false;
				while(currentPage + 1 < truePageLocs.size()
						&& truePageLocs.get(currentPage + 1) <= termStartingIndices.get(i))
				{
					currentPage++;
				}
				int numberToAdd = truePageNums.get(currentPage);

				// initialize termsIndex.get(termNoNewLine) to avoid a NullPointerException
				if(termsIndex.get(termNoNewLine) == null)
				{
					termsIndex.put(termNoNewLine, new ArrayList<Integer>());
				}
				if(RECORD_DUPLICATE_TERMS_ON_SAME_PAGE || termsIndex.get(termNoNewLine).contains(numberToAdd) == false)
				{
					termsIndex.get(termNoNewLine).add(numberToAdd);
				}
			}

			// TODO: add code that processes the output of ner
			/*
			 * // print xmlParser.getPageLocations() for (int i = 0; i <
			 * pageLocations.size(); i++) { System.out.println("[" + i + "]: " +
			 * pageLocations.get(i)); }
			 */

			// write termsIndex to a file

			try
			{
				PrintWriter pw = new PrintWriter(INDEX_OUTPUT_LOCATION);
				String[] keys = termsIndex.keySet().toArray(new String[0]);
				Arrays.sort(keys);
				for(int i = 0; i < keys.length; i++)
				{
					ArrayList<Integer> pageNumbers = termsIndex.get(keys[i]);
					pw.println(keys[i]);

					for(int j = 0; j < pageNumbers.size(); j++)
					{
						pw.println("\t" + pageNumbers.get(j));
					}
				}
				pw.flush();
				pw.close();
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}

		}
	}

	// returns the text of the file
	// code comes from
	// https://howtodoinjava.com/core-java/io/java-read-file-to-string-examples/
	private static String readFileIntoString(String filePath)
	{
		String content = "";
		try
		{
			content = new String(Files.readAllBytes(Paths.get(filePath)));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return content;
	}
}
