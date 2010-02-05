package edu.uci.lighthouse.hiperwall;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;

/**
 * Author: Alex Taubman
 * */
public class SaveAsJPEG {
	public static void main(String[] args) {

		try {
			ImageData[] images;

			Image picture = new Image(null,
					"C:\\Users\\Imryel\\Pictures\\turtle.jpg");

			SplitImage splitter = new SplitImage();
			BufferedImage picture2 = splitter.convertToAWT(picture
					.getImageData());

			// hard coded number of images to split into
			int row = 5;
			int column = 10;
			images = splitter.split(picture2, row, column);
			System.out.println(images.length);

			// ImageFigure figure = new ImageFigure(picture);

			ImageLoader loader = new ImageLoader();
			String fileName = "C:\\Users\\Imryel\\Desktop\\PicturesHere\\picture";
			for (int i = 0; i < row * column; i++) {
				loader.data = new ImageData[] { images[i] };
				String finalFileName = fileName + "" + i + ".jpg";
				System.out.println(finalFileName);
				File file = new File(finalFileName);
				file.createNewFile();
				FileOutputStream fos = new FileOutputStream(file);
				loader.save(fos, SWT.IMAGE_JPEG);
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
