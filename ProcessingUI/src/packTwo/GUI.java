// TODO: Rename the Java Project (ProcessingUI) and the package (packTwo) to
// something more relevant

package packTwo;

import java.io.File;

import processing.core.PApplet;

public class GUI extends PApplet
{
	private String errorMessage;
	private BookIndexer bookProcessor;
	private boolean[] categoriesToFind;

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
		categoriesToFind = new boolean[7];
		for(int i = 0; i < categoriesToFind.length; i++)
		{
			categoriesToFind[i] = true;
		}
	}

	@Override
	public void draw()
	{
		background(255, 255, 255);

		stroke(255, 0, 0);
		noFill();
		rect((float) (0.1 * width), (float) (0.1 * height), (float) (0.3 * width), (float) (0.2 * height));
		rect((float) (0.6 * width), (float) (0.1 * height), (float) (0.3 * width), (float) (0.2 * height));

		noStroke();
		for(int i = 0; i < 4; i++)
		{
			if(categoriesToFind[i])
			{
				fill(0, 255, 0);
			}
			else
			{
				fill(255, 0, 0);
			}
			rect((float) (0.1 * width), (float) ((0.52 + 0.12 * i) * height), (float) (0.08 * height), (float) (0.08 * height));
		}
		for(int i = 0; i < 3; i++)
		{
			if(categoriesToFind[i + 4])
			{
				fill(0, 255, 0);
			}
			else
			{
				fill(255, 0, 0);
			}
			rect((float) (0.6 * width), (float) ((0.52 + 0.12 * i) * height), (float) (0.08 * height), (float) (0.08 * height));
		}

		noStroke();
		fill(0, 0, 0);
		textSize((float) (height * 0.05));
		textAlign(RIGHT, BOTTOM);
		text(errorMessage, (float) (width - 5), (float) (height - 5));

		textSize((float) (height * 0.08));
		textAlign(LEFT, BOTTOM);
		text("Select", (float) (width * 0.1), (float) (height * 0.2));
		textAlign(LEFT, TOP);
		text("Book", (float) (width * 0.1), (float) ((height * 0.2)));

		textAlign(LEFT, BOTTOM);
		text("Generate", (float) (width * 0.6), (float) (height * 0.2));
		textAlign(LEFT, TOP);
		text("Index", (float) (width * 0.6), (float) ((height * 0.2)));

		textAlign(LEFT, CENTER);
		text("Organization", (float) (width * 0.10 + 0.08 * height + 5), (float) ((height * 0.56)));
		text("Person", (float) (width * 0.1 + 0.08 * height + 5), (float) ((height * 0.68)));
		text("Location", (float) (width * 0.1 + 0.08 * height + 5), (float) ((height * 0.80)));
		text("Percent", (float) (width * 0.1 + 0.08 * height + 5), (float) ((height * 0.92)));
		text("Date", (float) (width * 0.6 + 0.08 * height + 5), (float) ((height * 0.56)));
		text("Time", (float) (width * 0.6 + 0.08 * height + 5), (float) ((height * 0.68)));
		text("Money", (float) (width * 0.6 + 0.08 * height + 5), (float) ((height * 0.80)));
	}

	@Override
	public void mousePressed()
	{
		if(((0.1 * width) < mouseX) && (mouseX < (0.4 * width)) && ((0.1 * height) < mouseY) && (mouseY < (0.3 * height)))
		{
			selectInput("Select the book file", "inputSelected");
		}
		else if(((0.6 * width) < mouseX) && (mouseX < (0.8 * width)) && ((0.1 * height) < mouseY) && (mouseY < (0.3 * height)))
		{
			processBook();
		}
		else if(((0.1 * width) < mouseX) && (mouseX < (0.1 * width + 0.08 * height)) && ((0.52 * height) < mouseY) && (mouseY < (0.60 * height)))
		{
			categoriesToFind[0] = !categoriesToFind[0];
		}
		else if(((0.1 * width) < mouseX) && (mouseX < (0.1 * width + 0.08 * height)) && ((0.64 * height) < mouseY) && (mouseY < (0.72 * height)))
		{
			categoriesToFind[1] = !categoriesToFind[1];
		}
		else if(((0.1 * width) < mouseX) && (mouseX < (0.1 * width + 0.08 * height)) && ((0.76 * height) < mouseY) && (mouseY < (0.84 * height)))
		{
			categoriesToFind[2] = !categoriesToFind[2];
		}
		else if(((0.1 * width) < mouseX) && (mouseX < (0.1 * width + 0.08 * height)) && ((0.88 * height) < mouseY) && (mouseY < (0.96 * height)))
		{
			categoriesToFind[3] = !categoriesToFind[3];
		}
		else if(((0.6 * width) < mouseX) && (mouseX < (0.6 * width + 0.08 * height)) && ((0.52 * height) < mouseY) && (mouseY < (0.60 * height)))
		{
			categoriesToFind[4] = !categoriesToFind[4];
		}
		else if(((0.6 * width) < mouseX) && (mouseX < (0.6 * width + 0.08 * height)) && ((0.64 * height) < mouseY) && (mouseY < (0.72 * height)))
		{
			categoriesToFind[5] = !categoriesToFind[5];
		}
		else if(((0.6 * width) < mouseX) && (mouseX < (0.6 * width + 0.08 * height)) && ((0.76 * height) < mouseY) && (mouseY < (0.84 * height)))
		{
			categoriesToFind[6] = !categoriesToFind[6];
		}
	}

	public void inputSelected(File selection)
	{
		if(selection == null)
		{
			errorMessage = "Input file selection error.";
		}
		else
		{
			bookProcessor.selectInputFile(selection);
			errorMessage = "Selecting an input file succeeded.";
		}
	}

	public void processBook()
	{
		errorMessage = "Starting generating an index.";
		processing.data.StringList acceptableCategories = new processing.data.StringList();
		final String[] categories = new String[]{	"Organization",
													"Person",
													"Location",
													"Percent",
													"Date",
													"Time",
													"Money"};
		for(int i = 0; i < 7; i++)
		{
			if(categoriesToFind[i])
			{
				acceptableCategories.append(categories[i]);
			}
		}
		bookProcessor.setAcceptableCategories(acceptableCategories);
		bookProcessor.processFile();
		errorMessage = "Done generating an index.";
	}
}
