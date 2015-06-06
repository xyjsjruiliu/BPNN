package com.macb.file;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import com.macb.image.ImageProcess;
import com.macb.utils.AI;

public class CalFeatureThread extends Thread {

	private Display display;
	private List<String> imagelist;

	public CalFeatureThread(Display display, List<String> imagelist) {

		this.display = display;
		this.imagelist = imagelist;
	}

	public void run() {

		try {
			FileWriter fw = new FileWriter(AI.IMAGE_DB_PATH + "feature.txt",
					false);
			BufferedWriter bw = new BufferedWriter(fw);
			for (int i = 0; i < imagelist.size(); i++) {
				Image image = new Image(display, imagelist.get(i));
				double fv[] = new ImageProcess().getUnitFeature(image
						.getImageData());
				StringBuffer sb = new StringBuffer("");
				sb.append(imagelist.get(i) + " ");
				for (double d : fv) {
					sb.append(d + " ");
				}
				bw.write(sb.toString());
				bw.newLine();
			}
			bw.flush();
			bw.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
