// TODO: Add documentation of the class LOL

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
	private String classifierLocation = "classifiers/english.muc.7class.distsim.crf.ser"; // the location of the classifier to be used by ner
	private File indexOutputLocation = new File("testData/indexOutput.txt"); // the file that the index will be written to
	private File inputFile;		//the file that was last selected to be the input to the program
	
	//TODO: TEXT_OUTPUT_LOCATION is a temporary file that never gets deleted from the file system
	private File textOutputLocation = new File("testdata/xmlToText.txt"); // the temporary file that xmlParser will write the book plaintext to
	
	public void selectInputFile(File selection)
	{
		inputFile = selection;
	}
	
	public void setClassifier(File selection)
	{
		classifierLocation = selection.getName();
	}
	
	public void setIndexOutputLocation(File selection)
	{
		indexOutputLocation = selection;
	}
	
	public void setPlaintextLocation(File selection)
	{
		textOutputLocation = selection;
	}
	
	// TODO: handle case where inputFile is null
	public void processFile()
	{
		if(inputFile != null)
		{
			
			// initialize an XMLParser and use it to parse an input file
			XMLParser xmlParser = new XMLParser();
			File xmlOutputFile = textOutputLocation;
			xmlParser.parseBookXML(inputFile, xmlOutputFile);
			ArrayList<Integer> pageLocations = xmlParser.getPageLocations();
			
			// initialize a NERProcessor and use it to parse the text output of the XMLParser (the text of the book)
			NERProcessor ner = new NERProcessor();
			ner.initializeClassifier(classifierLocation);
			String bookPlaintext = readFileIntoString(xmlOutputFile.getAbsolutePath());
			ner.processText(bookPlaintext);
			
			// store output of ner in the HashMap termsIndex
			ArrayList<String> termList = ner.getTermList();
			ArrayList<String> categoryList = ner.getCategoryList();
			ArrayList<Integer> termStartingIndices = ner.getTermStartingIndices();
			
			PageTagger pageFinder = new PageTagger();
			ArrayList<Integer> countingUp = new ArrayList<Integer>();
			for(int i = 0; i < pageLocations.size(); i++)
			{
				countingUp.add(i);
			}
			pageFinder.findPageNumbers(xmlOutputFile.getAbsolutePath(), countingUp, pageLocations);
			
			ArrayList<Integer> truePageNums = pageFinder.getPageNumbers();
			ArrayList<Integer> truePageLocs = pageFinder.getStartLocations();
			HashMap<String, ArrayList<Integer>> termsIndex = new HashMap<String, ArrayList<Integer>>();
			int currentXMLPage = 0;
			for(int i = 0; i < termList.size(); i++)
			{
				String term = termList.get(i);
				String termNoNewLine = term.replace("\n", " ");
				
				// add term to termsIndex
				while(currentXMLPage + 1 < truePageLocs.size() && truePageLocs.get(currentXMLPage + 1) <= termStartingIndices.get(i))
				{
					currentXMLPage++;
				}
				int numberToAdd = truePageNums.get(currentXMLPage);
				
				// initialize termsIndex.get(termNoNewLine) to avoid a NullPointerException
				if(termsIndex.get(termNoNewLine) == null)
				{
					termsIndex.put(termNoNewLine, new ArrayList<Integer>());
				}
				
				//check for duplicate index entry
				boolean RECORD_DUPLICATE_TERMS_ON_SAME_PAGE = false;
				if(RECORD_DUPLICATE_TERMS_ON_SAME_PAGE || termsIndex.get(termNoNewLine).contains(numberToAdd) == false)
				{
					termsIndex.get(termNoNewLine).add(numberToAdd);
				}
			}
			
			// write termsIndex to a file
			try
			{
				PrintWriter pw = new PrintWriter(indexOutputLocation);
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
