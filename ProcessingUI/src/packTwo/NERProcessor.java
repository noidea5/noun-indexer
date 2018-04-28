// TODO: change getters so that private variables cannot be manipulated through the getters

package packTwo;

import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.util.Triple;

public class NERProcessor
{

	private ArrayList<String> termList;
	private ArrayList<String> categoryList;
	private ArrayList<Integer> termStartingIndices;
	private AbstractSequenceClassifier<CoreLabel> classifier;
	private String classifierFileRelativePath = "";

	public NERProcessor()
	{
		termList = new ArrayList<String>();
		categoryList = new ArrayList<String>();
		termStartingIndices = new ArrayList<Integer>();
		initializeClassifier("classifiers/english.muc.7class.distsim.crf.ser");
	}

	// this method is a setter
	public void initializeClassifier(String classifierFile)
	{
		if(!classifierFileRelativePath.equals(classifierFile))
		{
			try
			{
				classifier = CRFClassifier.getClassifier(classifierFile);
				classifierFileRelativePath = classifierFile;
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public void processText(String inputText)
	{
		List<Triple<String, Integer, Integer>> list = classifier.classifyToCharacterOffsets(inputText);
		for(Triple<String, Integer, Integer> item : list)
		{
			String term = inputText.substring(item.second(), item.third());
			termList.add(term);
			categoryList.add(item.first());
			termStartingIndices.add(item.second());
		}
	}

	public ArrayList<String> getTermList()
	{
		return termList;
	}

	public ArrayList<String> getCategoryList()
	{
		return categoryList;
	}

	public ArrayList<Integer> getTermStartingIndices()
	{
		return termStartingIndices;
	}
}
