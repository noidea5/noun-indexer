//TODO: Rename the Java Project (ProcessingUI), the package (packTwo), and the class (ClassTwo) to something more relevant

package packTwo;

import java.io.File;

import processing.core.PApplet;

public class GUI extends PApplet
{
	private String errorMessage;

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
	}

	@Override
	public void draw()
	{
		background(255, 255, 255);

		stroke(255, 0, 0);
		noFill();
		line((float) (0.1 * width), (float) (0.1 * height), (float) (0.4 * width), (float) (0.1 * height));
		line((float) (0.1 * width), (float) (0.4 * height), (float) (0.4 * width), (float) (0.4 * height));
		line((float) (0.4 * width), (float) (0.1 * height), (float) (0.4 * width), (float) (0.4 * height));
		line((float) (0.1 * width), (float) (0.1 * height), (float) (0.1 * width), (float) (0.4 * height));

		noStroke();
		fill(0, 0, 0);
		textSize((float) (height * 0.05));
		textAlign(CENTER, CENTER);
		text(errorMessage, (float) (width * 0.5), (float) (height * 0.5));

		textSize((float) (height * 0.08));
		textAlign(LEFT, TOP);
		text("Click to", (float) (width * 0.1), (float) (height * 0.1));
		text("Select File", (float) (width * 0.1), (float) ((height * 0.1) + textAscent()));
	}

	@Override
	public void mousePressed()
	{
		if(((0.1 * width) < mouseX) && (mouseX < (0.4 * width)) && ((0.1 * height) < mouseY)
				&& (mouseY < (0.4 * height)))
		{
			selectInput("Select the downloaded XML file", "processFile");
		}
	}

	public void processFile(File selection)
	{
		if(selection == null)
		{
			println("You didn't select an acceptable file.");
			errorMessage = "File selection error.";
		}
		else
		{
			println("You selected the file " + selection.getAbsolutePath());
			errorMessage = "File selection suceeded.";
			BookIndexer.processFile(selection);
		}
	}
}
