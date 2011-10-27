/*******************************************************************************
* Copyright (c) {2009,2011} {Software Design and Collaboration Laboratory (SDCL)
*				, University of California, Irvine}.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*    {Software Design and Collaboration Laboratory (SDCL)
*	, University of California, Irvine}
*			- initial API and implementation and/or initial documentation
*******************************************************************************/
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



			// hard coded number of images to split into
			int row = 5;
			int column = 10;
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
					splitImages[num] = new SplitImage(picture,column,row,j,i,num);
					threads[num] = new Thread(splitImages[num]);
					num++;
				}
			}
			//run threads
			for (int i=0; i < row*column; i++)
			{
				threads[i].start();
				//collect image data
				

			}

			//give time for threads to finish
			//Thread.sleep(5000);

			//collect image data


			System.out.println(images.length);

			// ImageFigure figure = new ImageFigure(picture);

			//ImageLoader loader = new ImageLoader();



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
