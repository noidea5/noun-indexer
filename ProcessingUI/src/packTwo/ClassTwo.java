package packTwo;

import java.io.File;

import processing.core.PApplet;

public class ClassTwo extends PApplet
{
	private String errorMessage;
	public static void main(String[] args)
	{
        PApplet.main("packTwo.ClassTwo");
	}

    public void settings()
	{
        size(800,450);
    }

    public void setup()
	{
		errorMessage = "Program loaded.";
    }

    public void draw()
	{
		background(255, 255, 255);
		
		stroke(255, 0, 0);
		noFill();
		line((float)(0.1 * width), (float)(0.1 * height), (float)(0.4 * width), (float)(0.1 * height));
		line((float)(0.1 * width), (float)(0.4 * height), (float)(0.4 * width), (float)(0.4 * height));
		line((float)(0.4 * width), (float)(0.1 * height), (float)(0.4 * width), (float)(0.4 * height));
		line((float)(0.1 * width), (float)(0.1 * height), (float)(0.1 * width), (float)(0.4 * height));
		
		noStroke();
		fill(0, 0, 0);
		textSize((float)(height * 0.1));
		textAlign(CENTER, CENTER);
		text(errorMessage, (float)(width * 0.5), (float)(height * 0.5));
    }

    public void mousePressed()
	{
		if(0.1 * width < mouseX && mouseX < 0.4 * width && 0.1 * height < mouseY && mouseY < 0.4 * height)
		{
			selectInput("Select the fully tagged text file", "processFile");
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
			//TODO: add code that processes the selected file
		}
    }
}
