package packOne;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ClassOne {

	public static void main(String[] args)
	{
		String inputFileAbsPath;
		inputFileAbsPath = "C:\\inputFile.txt";
		String inputFileContents = usingBufferedReader(inputFileAbsPath);
		//System.out.println(inputFileContents);
		//System.out.println("EOM");
		
		ArrayList<String> TERM_TAGS = new ArrayList();
		TERM_TAGS.add("LOCATION");
		TERM_TAGS.add("ORGANIZATION");
		TERM_TAGS.add("DATE");
		TERM_TAGS.add("MONEY");
		TERM_TAGS.add("PERSON");
		TERM_TAGS.add("PERCENT");
		TERM_TAGS.add("TIME");
		
		HashMap<String, ArrayList<Integer>> termsIndex = new HashMap<String, ArrayList<Integer>>();
		int LBracketIndex = -1;
		while(inputFileContents.indexOf("<", LBracketIndex + 1) != -1)
		{
			//System.out.println(LBracketIndex + " versus " + inputFileContents.length());
			int RBracketIndex = inputFileContents.indexOf(">", LBracketIndex);
			String termCategory = inputFileContents.substring(LBracketIndex + 1, RBracketIndex);
			
			if(TERM_TAGS.contains(termCategory))
			{
				String termCategoryEnd = "</" + termCategory + ">";
				String termInside = inputFileContents.substring(RBracketIndex + 1, inputFileContents.indexOf(termCategoryEnd, RBracketIndex + 1));
				int pageNumber = -1;
				int pageRBracketIndex = inputFileContents.lastIndexOf(">", LBracketIndex);
				boolean searchingForPageTag = true;
				while(searchingForPageTag)
				{
					int pageLBracketIndex = inputFileContents.lastIndexOf("<p", pageRBracketIndex);
					String pageNumberAsString = inputFileContents.substring(pageLBracketIndex + 2, pageRBracketIndex);
					try
					{
						pageNumber = Integer.parseInt(pageNumberAsString);
						searchingForPageTag = false;
					}
					catch(NumberFormatException e)
					{
						pageRBracketIndex = inputFileContents.lastIndexOf(">", pageRBracketIndex - 1);
					}
				}
				if(searchingForPageTag || pageNumber == -1)
				{
					//System.out.println();
					//System.out.println("Searching for page tag error");
					//System.out.println("inside is " + inside);
					//System.out.println("pageNumber is " + pageNumber);
					//System.out.println("searchingForPageTag is " + searchingForPageTag);
				}
				
				final boolean RECORD_DUPLICATE_TERMS_ON_SAME_PAGE = true;
				if(RECORD_DUPLICATE_TERMS_ON_SAME_PAGE || termsIndex.get(termInside).contains(pageNumber) == false)
				{
					if(termsIndex.get(termInside) == null)
					{
						termsIndex.put(termInside, new ArrayList());
					}
					termsIndex.get(termInside).add(pageNumber);
				}
			}
			LBracketIndex = inputFileContents.indexOf("<", LBracketIndex + 1);
		}
		//System.out.println("Done Searching");
		
		//print output
		String[] keys = termsIndex.keySet().toArray(new String[0]);
		Arrays.sort(keys);
		for(int i = 0; i < keys.length; i++)
		{
			ArrayList<Integer> pageNumbers = termsIndex.get(keys[i]);
			System.out.println(keys[i]);

			for(int j = 0; j < pageNumbers.size(); j++)
			{
				System.out.println("\t" + pageNumbers.get(j));
			}
		}
	}

	//https://howtodoinjava.com/core-java/io/java-read-file-to-string-examples/
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
}