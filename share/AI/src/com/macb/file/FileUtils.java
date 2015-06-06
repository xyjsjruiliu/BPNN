package com.macb.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.macb.utils.AI;
import com.macb.utils.Utils;

public class FileUtils {

	public List<String> getImageList(String path, int type) {

		List<String> imagelist = new ArrayList<String>();
		File file = new File(path);
		String[] filenames = file.list(new ImageFileFilter(type));

		for (String filename : filenames) {
			imagelist.add(path + filename);
		}
		return imagelist;
	}

	public double[] getFeatureFromFile(String path) {
		try {
			FileReader reader = new FileReader(AI.IMAGE_DB_PATH + "feature.txt");
			BufferedReader br = new BufferedReader(reader);

			String str = null;
			while ((str = br.readLine()) != null) {
				if (str.startsWith(path)) {
					return new Utils().String2Double(str);
				}
			}

			br.close();
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
