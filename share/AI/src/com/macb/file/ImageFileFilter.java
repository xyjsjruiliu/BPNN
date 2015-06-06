package com.macb.file;

import java.io.File;
import java.io.FilenameFilter;

import com.macb.utils.AI;

public class ImageFileFilter implements FilenameFilter {

	private int type;

	public ImageFileFilter(int type) {
		// TODO Auto-generated constructor stub
		this.type = type;
	}

	@Override
	public boolean accept(File dir, String name) {

		if (this.type == AI.ALL)
			return name.endsWith(".jpg");
		else if (this.type == AI.INDOOR) {
			return name.endsWith(".jpg") && name.startsWith("Indoor");
		} else {
			return name.endsWith(".jpg") && name.startsWith("Outdoor");
		}

	}
}