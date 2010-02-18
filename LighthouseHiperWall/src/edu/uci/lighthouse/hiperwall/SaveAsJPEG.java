package edu.uci.lighthouse.hiperwall;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;


/**
 * Author: Alex Taubman
 * */
public class SaveAsJPEG {
	public static void main(String[] args) {

		try {
			

			File loadFile = new File("C:\\Users\\Imryel\\Desktop\\hiper.jpg");

			//BufferedImage picture2 = splitter.convertToAWT(picture
			//		.getImageData());

			//load file
			BufferedImage picture = ImageIO.read(loadFile);

			System.out.println("1");
			
			BufferedImage picture2 = ImageIO.read(loadFile);

			System.out.println("2");
			
			BufferedImage picture3 = ImageIO.read(loadFile);
			
			System.out.println("3");
			
			// hard coded number of images to split into
			int row = 1;
			int column = 1;
			BufferedImage[] images = new BufferedImage[row*column];
			
			//create row*column Threads
			Thread[] threads = new Thread[row*column];
			
			//save access to the Split Images
			SplitImage[] splitImages = new SplitImage[row*column];
			
			//create threads
			int num = 0;
			for (int i = 0; i < row; i++)
			{
				for (int j = 0; j < column; j++)
				{
					splitImages[num] = new SplitImage(picture,column,row,j,i);
					threads[num] = new Thread(splitImages[num]);
					num++;
				}
			}
			//run threads
			for (int i=0; i < row*column; i++)
			{
				threads[i].start();
			}
			
			//give time for threads to finish
			Thread.sleep(5000);
			
			//collect image data
			int num1 = 0;
			for (int i=0; i < row*column; i++)
			{
				images[num1] = splitImages[num1].getImage();
				num1++;
			}
						
			System.out.println(images.length);

			// ImageFigure figure = new ImageFigure(picture);

			//ImageLoader loader = new ImageLoader();
			
			//save images
			String fileName = "C:\\Users\\Imryel\\Desktop\\PicturesHere\\picture";
			for (int i = 0; i < row * column; i++) {
				String finalFileName = fileName + "" + i + ".jpg";
				System.out.println(finalFileName);
				File saveFile = new File(finalFileName);
				saveFile.createNewFile();
				ImageIO.write(images[i],"jpg",saveFile);
				//FileOutputStream fos = new FileOutputStream(file);
				//loader.save(fos, SWT.IMAGE_JPEG);
				fileName = "C:\\Users\\Imryel\\Desktop\\PicturesHere\\picture";
			}

			// this code is intended to create an image out of an ImageFigure.
			// it may or may not be needed, and may or may not work.

			// Dimension size = figure.getSize();
			// System.out.println(size);
			// Image image = new Image(Display.getDefault(), size.width,
			// size.height);
			// GC gc = new GC(picture);
			// SWTGraphics graphics = new SWTGraphics(gc);
			// figure.paint(graphics);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
