package edu.uci.lighthouse.ui.swt.util;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class ImageFactory {
	private static Map<String,Image> imageCache = new HashMap<String,Image>();

	public static Image getImage(String path) {
		Image result = imageCache.get(path);
		if (result == null) {
			result = new Image(Display.getDefault(), ImageFactory.class
					.getResourceAsStream(path));
			/*try {
				File f = new File(".");
				result = new Image(Display.getDefault(), new FileInputStream(f.getAbsolutePath()+path));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			imageCache.put(path, result);
		}
		return result;
	}
}
