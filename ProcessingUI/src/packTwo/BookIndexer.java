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
	private File inputFile;		//the file that was last selected to be the input to the program

	//TODO: TEXT_OUTPUT_LOCATION is a temporary file that never gets deleted from the file system
	private File textOutputLocation = new File("testdata/xmlToText.txt"); // the temporary file that xmlParser will write the book plaintext to

	private processing.data.StringList acceptableCategories = new processing.data.StringList();

	public void selectInputFile(File selection)
	{
		inputFile = selection;
	}

	public void setAcceptableCategories(processing.data.StringList input)
	{
		acceptableCategories.clear();
		for(int i = 0; i < input.size(); i++)
		{
			acceptableCategories.append(input.get(i).toLowerCase());
		}
	}

	public void processFile()
	{
		if(inputFile != null)
		{
			String inputFileName = inputFile.getName();
			int dotIndex = inputFileName.lastIndexOf(".");
			String inputFileExtension = "";
			if(dotIndex != -1)
			{
				inputFileExtension = inputFileName.substring(dotIndex + 1).toLowerCase();
			}

			//initialize a NERProcessor
			NERProcessor ner = new NERProcessor();
			ner.initializeClassifier(classifierLocation);

			//these variables must be initialized for the code at the end of the gigantic if-else section
			String bookPlaintext = "";
			ArrayList<Integer> truePageNums = new ArrayList<Integer>();
			ArrayList<Integer> truePageLocs = new ArrayList<Integer>();

			//set bookPlaintext and truePageNums and truePageLocs based on the input file
			if(inputFileExtension.equals("xml"))
			{
				XMLParser xmlParser = new XMLParser();
				File xmlOutputFile = textOutputLocation;
				xmlParser.parseBookXML(inputFile, xmlOutputFile);
				ArrayList<Integer> pageLocations = xmlParser.getPageLocations();
				bookPlaintext = readFileIntoString(xmlOutputFile.getAbsolutePath());

				PageTagger pageFinder = new PageTagger();
				ArrayList<Integer> countingUp = new ArrayList<Integer>();
				for(int i = 0; i < pageLocations.size(); i++)
				{
					countingUp.add(i);
				}
				pageFinder.findPageNumbersFromFile(xmlOutputFile.getAbsolutePath(), countingUp, pageLocations);
				truePageNums = pageFinder.getPageNumbers();
				truePageLocs = pageFinder.getStartLocations();
			}
			else if(inputFileExtension.equals("pdf"))
			{
				PDFParser coolName = new PDFParser();
				try
				{
					coolName.parse(inputFile);
				}
				catch (IOException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				bookPlaintext = coolName.getText();
				truePageNums = coolName.getPageLocations();
				truePageLocs = coolName.getTrueNumbers();
			}

			ner.processText(bookPlaintext);
			ArrayList<String> termList = ner.getTermList();
			ArrayList<String> categoryList = ner.getCategoryList();
			ArrayList<Integer> termStartingIndices = ner.getTermStartingIndices();

			HashMap<String, ArrayList<Integer>> termsIndex = new HashMap<String, ArrayList<Integer>>();
			int currentDocPage = 0;
			for(int i = 0; i < termList.size(); i++)
			{
				if(acceptableCategories.hasValue(categoryList.get(i).toLowerCase()))
				{
					String term = termList.get(i);
					String termNoNewLine = term.replace("\n", " ");		//there should be no line separators in index entries

					// add term to termsIndex
					while(currentDocPage + 1 < truePageLocs.size() && truePageLocs.get(currentDocPage + 1) <= termStartingIndices.get(i))
					{
						currentDocPage++;
					}
					int numberToAdd = truePageNums.get(currentDocPage);

					// initialize termsIndex.get(termNoNewLine) to avoid a NullPointerException
					if(termsIndex.get(termNoNewLine) == null)
					{
						termsIndex.put(termNoNewLine, new ArrayList<Integer>());
					}

					//check for duplicate index entry
					final boolean RECORD_DUPLICATE_TERMS_ON_SAME_PAGE = false;
					if(RECORD_DUPLICATE_TERMS_ON_SAME_PAGE || termsIndex.get(termNoNewLine).contains(numberToAdd) == false)
					{
						termsIndex.get(termNoNewLine).add(numberToAdd);
					}
				}
			}

			// write termsIndex to a file
			try
			{
				String inputNameWithoutExtension = inputFile.getName().substring(0, inputFile.getName().lastIndexOf("."));
				PrintWriter pw = new PrintWriter("testdata/" +inputNameWithoutExtension +"BookIndex.txt");
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
