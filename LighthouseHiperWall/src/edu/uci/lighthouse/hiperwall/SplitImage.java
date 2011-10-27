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

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;

/**
 * Author: Alex Taubman
 * */
public class SplitImage implements Runnable{

	BufferedImage image;
	int columns;
	int rows;
	int thisX;
	int thisY;
	int number;
	BufferedImage smallImage;

	public SplitImage (BufferedImage img, int cols, int ros, int x, int y, int num)
	{
		image = img;
		columns = cols;
		rows = ros;
		thisX = x;
		thisY = y;
		number = num;
	}

	public BufferedImage getImage()
	{
		return smallImage;
	}

	// source: http://www.javalobby.org/articles/ultimate-image/#13
	public void run(){

		columns = image.getWidth()/columns;  
		rows= image.getHeight()/rows;  
		smallImage = new BufferedImage(columns, rows, image.getType());
		// Tell the graphics to draw only one block of the image
		Graphics2D g = smallImage.createGraphics();
		g.drawImage(image, 0, 0, columns, rows, columns * thisX, rows * thisY, columns * thisX + columns,
				rows * thisY + rows, null);
		g.dispose();

		saveImage();


	}

	public void saveImage()
	{
		BufferedImage image = getImage();

		//save image
		String fileName = "C:\\Users\\Imryel\\Desktop\\PicturesHere\\picture";

		String finalFileName = fileName + "" + number + ".jpg";
		System.out.println(finalFileName);
		File saveFile = new File(finalFileName);
		try {
			saveFile.createNewFile();

			ImageIO.write(image,"jpg",saveFile);
		} catch (IOException e) {

			e.printStackTrace();
		}
		//FileOutputStream fos = new FileOutputStream(file);
		//loader.save(fos, SWT.IMAGE_JPEG);
		fileName = "C:\\Users\\Imryel\\Desktop\\PicturesHere\\picture";
		
		//kill thread
		Thread.yield();
	}

	//ImageData[] images = new ImageData[w * h];
	//for (int i = 0; i < rows * cols; i++) {
	//	images[i] = convertToSWT(imgs[i]);

	//}


	//}

	// source:
	// http://www.java2s.com/Code/Java/SWT-JFace-Eclipse/ConvertbetweenSWTImageandAWTBufferedImage.htm
	public BufferedImage convertToAWT(ImageData data) {
		ColorModel colorModel = null;
		PaletteData palette = data.palette;
		if (palette.isDirect) {
			colorModel = new DirectColorModel(data.depth, palette.redMask,
					palette.greenMask, palette.blueMask);
			BufferedImage bufferedImage = new BufferedImage(colorModel,
					colorModel.createCompatibleWritableRaster(data.width,
							data.height), false, null);
			WritableRaster raster = bufferedImage.getRaster();
			int[] pixelArray = new int[3];
			for (int y = 0; y < data.height; y++) {
				for (int x = 0; x < data.width; x++) {
					int pixel = data.getPixel(x, y);
					RGB rgb = palette.getRGB(pixel);
					pixelArray[0] = rgb.red;
					pixelArray[1] = rgb.green;
					pixelArray[2] = rgb.blue;
					raster.setPixels(x, y, 1, 1, pixelArray);
				}
			}
			return bufferedImage;
		} else {
			RGB[] rgbs = palette.getRGBs();
			byte[] red = new byte[rgbs.length];
			byte[] green = new byte[rgbs.length];
			byte[] blue = new byte[rgbs.length];
			for (int i = 0; i < rgbs.length; i++) {
				RGB rgb = rgbs[i];
				red[i] = (byte) rgb.red;
				green[i] = (byte) rgb.green;
				blue[i] = (byte) rgb.blue;
			}
			if (data.transparentPixel != -1) {
				colorModel = new IndexColorModel(data.depth, rgbs.length, red,
						green, blue, data.transparentPixel);
			} else {
				colorModel = new IndexColorModel(data.depth, rgbs.length, red,
						green, blue);
			}
			BufferedImage bufferedImage = new BufferedImage(colorModel,
					colorModel.createCompatibleWritableRaster(data.width,
							data.height), false, null);
			WritableRaster raster = bufferedImage.getRaster();
			int[] pixelArray = new int[1];
			for (int y = 0; y < data.height; y++) {
				for (int x = 0; x < data.width; x++) {
					int pixel = data.getPixel(x, y);
					pixelArray[0] = pixel;
					raster.setPixel(x, y, pixelArray);
				}
			}
			return bufferedImage;
		}
	}

	// source:
	// http://www.java2s.com/Code/Java/SWT-JFace-Eclipse/ConvertbetweenSWTImageandAWTBufferedImage.htm
	public ImageData convertToSWT(BufferedImage bufferedImage) {
		if (bufferedImage.getColorModel() instanceof DirectColorModel) {
			DirectColorModel colorModel = (DirectColorModel) bufferedImage
			.getColorModel();
			PaletteData palette = new PaletteData(colorModel.getRedMask(),
					colorModel.getGreenMask(), colorModel.getBlueMask());
			ImageData data = new ImageData(bufferedImage.getWidth(),
					bufferedImage.getHeight(), colorModel.getPixelSize(),
					palette);
			WritableRaster raster = bufferedImage.getRaster();
			int[] pixelArray = new int[3];
			for (int y = 0; y < data.height; y++) {
				for (int x = 0; x < data.width; x++) {
					raster.getPixel(x, y, pixelArray);
					int pixel = palette.getPixel(new RGB(pixelArray[0],
							pixelArray[1], pixelArray[2]));
					data.setPixel(x, y, pixel);
				}
			}
			return data;
		} else if (bufferedImage.getColorModel() instanceof IndexColorModel) {
			IndexColorModel colorModel = (IndexColorModel) bufferedImage
			.getColorModel();
			int size = colorModel.getMapSize();
			byte[] reds = new byte[size];
			byte[] greens = new byte[size];
			byte[] blues = new byte[size];
			colorModel.getReds(reds);
			colorModel.getGreens(greens);
			colorModel.getBlues(blues);
			RGB[] rgbs = new RGB[size];
			for (int i = 0; i < rgbs.length; i++) {
				rgbs[i] = new RGB(reds[i] & 0xFF, greens[i] & 0xFF,
						blues[i] & 0xFF);
			}
			PaletteData palette = new PaletteData(rgbs);
			ImageData data = new ImageData(bufferedImage.getWidth(),
					bufferedImage.getHeight(), colorModel.getPixelSize(),
					palette);
			data.transparentPixel = colorModel.getTransparentPixel();
			WritableRaster raster = bufferedImage.getRaster();
			int[] pixelArray = new int[1];
			for (int y = 0; y < data.height; y++) {
				for (int x = 0; x < data.width; x++) {
					raster.getPixel(x, y, pixelArray);
					data.setPixel(x, y, pixelArray[0]);
				}
			}
			return data;
		}
		return null;
	}
}
