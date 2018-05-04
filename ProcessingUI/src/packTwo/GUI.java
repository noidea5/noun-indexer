// TODO: Rename the Java Project (ProcessingUI) and the package (packTwo) to
// something more relevant

package packTwo;

import java.io.File;

import processing.core.PApplet;

public class GUI extends PApplet
{
	private String errorMessage;
	private BookIndexer bookProcessor;

	public static void main(String[] args)
	{
		PApplet.main("packTwo.GUI");
	}

	@Override
	public void settings()
	{
		size(800, 450);
	}

	@Override
	public void setup()
	{
		errorMessage = "Program loaded.";
		bookProcessor = new BookIndexer();
	}

	@Override
	public void draw()
	{
		background(255, 255, 255);

		stroke(255, 0, 0);
		noFill();
		rect((float) (0.1 * width), (float) (0.1 * height), (float) (0.3 * width), (float) (0.2 * height));
		rect((float) (0.1 * width), (float) (0.4 * height), (float) (0.3 * width), (float) (0.2 * height));
		rect((float) (0.1 * width), (float) (0.7 * height), (float) (0.3 * width), (float) (0.2 * height));
		rect((float) (0.6 * width), (float) (0.1 * height), (float) (0.3 * width), (float) (0.2 * height));
		rect((float) (0.6 * width), (float) (0.4 * height), (float) (0.3 * width), (float) (0.2 * height));

		noStroke();
		fill(0, 0, 0);
		textSize((float) (height * 0.05));
		textAlign(RIGHT, BOTTOM);
		text(errorMessage, (float) (width - 5), (float) (height - 5));

		textSize((float) (height * 0.08));
		textAlign(LEFT, BOTTOM);
		text("Select", (float) (width * 0.1), (float) (height * 0.2));
		textAlign(LEFT, TOP);
		text("XML File", (float) (width * 0.1), (float) ((height * 0.2)));

		textAlign(LEFT, BOTTOM);
		text("Select", (float) (width * 0.1), (float) (height * 0.5));
		textAlign(LEFT, TOP);
		text("Classifier", (float) (width * 0.1), (float) ((height * 0.5)));

		textAlign(LEFT, BOTTOM);
		text("Select temp", (float) (width * 0.1), (float) (height * 0.8));
		textAlign(LEFT, TOP);
		text("text location", (float) (width * 0.1), (float) ((height * 0.8)));

		textAlign(LEFT, BOTTOM);
		text("Select index", (float) (width * 0.6), (float) (height * 0.2));
		textAlign(LEFT, TOP);
		text("output location", (float) (width * 0.6), (float) ((height * 0.2)));

		textAlign(LEFT, BOTTOM);
		text("Generate", (float) (width * 0.6), (float) (height * 0.5));
		textAlign(LEFT, TOP);
		text("Index", (float) (width * 0.6), (float) ((height * 0.5)));
	}

	@Override
	public void mousePressed()
	{
		if(((0.1 * width) < mouseX) && (mouseX < (0.4 * width)) && ((0.1 * height) < mouseY) && (mouseY < (0.3 * height)))
		{
			selectInput("Select the downloaded XML file", "selectXML");
		}
		else if(((0.1 * width) < mouseX) && (mouseX < (0.4 * width)) && ((0.4 * height) < mouseY) && (mouseY < (0.6 * height)))
		{
			selectInput("Select the classifier that the NER will use", "selectClassifier");
		}
		else if(((0.1 * width) < mouseX) && (mouseX < (0.4 * width)) && ((0.7 * height) < mouseY) && (mouseY < (0.9 * height)))
		{
			selectInput("Select where the program will write the book's plaintext to", "selectTextLocation");
		}
		else if(((0.6 * width) < mouseX) && (mouseX < (0.8 * width)) && ((0.1 * height) < mouseY) && (mouseY < (0.3 * height)))
		{
			selectInput("Select where the program will write the generated index to", "selectOutputLocation");
		}
		else if(((0.6 * width) < mouseX) && (mouseX < (0.8 * width)) && ((0.4 * height) < mouseY) && (mouseY < (0.6 * height)))
		{
			processBook();
		}
	}

	public void selectXML(File selection)
	{
		if(selection == null)
		{
			println("You didn't select an acceptable xml file.");
			errorMessage = "File selection error (XML).";
		}
		else
		{
			println("You selected the file " + selection.getAbsolutePath());
			bookProcessor.selectInputFile(selection);
			errorMessage = "Selecting a XML File succeeded.";
		}
	}

	public void selectClassifier(File selection)
	{
		if(selection == null)
		{
			println("You didn't select an acceptable classifier file.");
			errorMessage = "File selection error (classifier).";
		}
		else
		{
			bookProcessor.setClassifier(selection);
			errorMessage = "Selecting a classifier succeeded.";
		}
	}

	public void selectTextLocation(File selection)
	{
		if(selection == null)
		{
			println("You didn't select an acceptable temporary file location.");
			errorMessage = "File selection error (text).";
		}
		else
		{
			bookProcessor.setPlaintextLocation(selection);
			errorMessage = "Selecting temp text location succeeded.";
		}
	}

	public void selectOutputLocation(File selection)
	{
		if(selection == null)
		{
			println("You didn't select an acceptable index output location.");
			errorMessage = "File selection error (index).";
		}
		else
		{
			bookProcessor.setIndexOutputLocation(selection);
			errorMessage = "Selecting index location succeeded.";
		}
	}

	public void processBook()
	{
		errorMessage = "Starting generating an index.";
		bookProcessor.processFile();
	}
}
