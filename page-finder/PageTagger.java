package ner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class PageTagger {
	
	
	public static void main(String[] args) {
		
		//inputFilePath is the path for the .txt file that is to be processed
		String inputFileAbsPath;
//		inputFileAbsPath = "/Users/dannyshrestha/Desktop/inputFile.txt";
		inputFileAbsPath = "/Users/dannyshrestha/Desktop/testStory.txt";
		String inputFileContents = usingBufferedReader(inputFileAbsPath);
//		System.out.println(inputFileContents);
		
		String regex = "\\d";//using regular expression to set what to identify, in this case numerical digit
		ArrayList<Integer> page = new ArrayList<Integer>();//array for each number
		ArrayList<Integer> location = new ArrayList<Integer>();//corresponding array for the number's location
		
		/* for loop runs through the entire .txt file to identify every number
		 * first finds the initial digit, then checks to see if there are following digits
		 * once the entire number has been identified, it is added to a 2D array
		 * the location of the number is also recorded */
		for (int i = 0; i < inputFileContents.length(); i++) {
			if (inputFileContents.substring(i,i+1).matches(regex)) {
				int counter = i+1;
				String foundDigits = inputFileContents.substring(i, i+1);
				while (inputFileContents.substring(counter, counter+1).matches(regex)) {
					foundDigits = foundDigits + inputFileContents.substring(counter, counter+1);
					counter++;
				}
				page.add(Integer.parseInt(foundDigits));
				location.add(i);
				
				i = counter;
			}
		}
		for(int i = 0; i < page.size(); i++) {
			System.out.println(page.get(i) + ", " + location.get(i));
		}
	}
	
	//reads the .txt file (method made by Nathan Ho, so I don't know how it works)
	private static String usingBufferedReader(String filePath)
	{
	    StringBuilder contentBuilder = new StringBuilder();
	    try (BufferedReader br = new BufferedReader(new FileReader(filePath)))
	    {
	 
	        String sCurrentLine;
	        while ((sCurrentLine = br.readLine()) != null)
	        {
	            contentBuilder.append(sCurrentLine).append("\n");
	        }
	    }
	    catch (IOException e)
	    {
	        e.printStackTrace();
	    }
	    return contentBuilder.toString();
	}
	
	//method to get rid of all of the out of order numbers
	public static void orderPages(ArrayList<Integer> page, ArrayList<Integer> location, ArrayList<Integer> pageXML, ArrayList<Integer> locationXML) {
		ArrayList<Integer> outputPage = new ArrayList<Integer>();
		ArrayList<Integer> outputLocation = new ArrayList<Integer>();
		
		HashMap<Integer,ArrayList<Integer>> hm = new HashMap<Integer, ArrayList<Integer>>();
		int counter = 0;
		for (int i = 0; i < pageXML.size(); i++) {
			ArrayList<Integer> cons = new ArrayList<Integer>();
			
			while (location.get(counter) < locationXML.get(i)) {
				cons.add(page.get(counter)-i);
				
				counter++;
			}
			//TODO: remove duplicates from cons
			//TODO: store cons in the output data structure
		}
	}
}
