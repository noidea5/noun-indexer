package packTwo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;

public class PageTagger {
	
	private ArrayList<Integer> pageNumbers;
	private ArrayList<Integer> startLocations;
	
	public void findPageNumbers(String filePath,ArrayList<Integer> pageXML, ArrayList<Integer> locationXML)
	{
		//inputFilePath is the path for the .txt file that is to be processed
		String inputFileContents = usingBufferedReader(filePath);
//		System.out.println(inputFileContents);
		
		String regex = "\\d";//using regular expression to set what to identify, in this case numerical digit
		ArrayList<Integer> page = new ArrayList<Integer>();//array for each number
		ArrayList<Integer> location = new ArrayList<Integer>();//corresponding array for the number's location
		
		/* for loop runs through the entire .txt file to identify every number
		 * first finds the initial digit, then checks to see if there are following digits
		 * once the entire number has been identified, it is added to an array list
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
//			System.out.println(page.get(i) + ", " + location.get(i));
		}
		orderPages(page, location, pageXML, locationXML);
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
	
	//method to figure out which numbers are page numbers
	public void orderPages(ArrayList<Integer> page, ArrayList<Integer> location, ArrayList<Integer> pageXML, ArrayList<Integer> locationXML) {
		
		HashMap<Integer, Integer> hm = new HashMap<Integer, Integer>();

		int counter = 0;
		for (int i = 0; i < pageXML.size(); i++) {
			ArrayList<Integer> dif = new ArrayList<Integer>();
			while (location.get(counter) < locationXML.get(i)) {
				dif.add(page.get(counter)-pageXML.get(i));
				counter++;
			}
			
			Collections.sort(dif);
			
			int counter2 = 0;
			while (counter2+1 < dif.size()) {
				if (dif.get(counter2) == dif.get(counter2+1)) {
					dif.remove(counter2);
				}
			}
			
			for (int o = 0; o < dif.size(); o++) {
				hm.put(dif.get(o), hm.get(dif.get(o))+1);
			}
		}
		
		Integer maxOffsetFreq = 0;
		Integer offset = 0;
		
		Integer[] keys = hm.keySet().toArray(new Integer[0]);
		for(int i = 0; i < keys.length; i++) {
			Integer offsetFreq = hm.get(keys[i]);
			
			if (offsetFreq > maxOffsetFreq) {
				maxOffsetFreq = offsetFreq;
				offset = keys[i];
			}
		}
		
		ArrayList<Integer> outputPage = new ArrayList<Integer>();
		
		for (int i = 0; i < pageXML.size(); i++) {
			outputPage.add(pageXML.get(i)+offset);
		}
		//TODO: return output page and locationXML
		pageNumbers = outputPage;
		startLocations = locationXML;
	}
	
	public ArrayList<Integer> getPageNumbers()
	{
		return pageNumbers;
	}
	
	public ArrayList<Integer> getStartLocations()
	{
		return startLocations;
	}
}
