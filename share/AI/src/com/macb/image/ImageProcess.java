package com.macb.image;



import org.eclipse.swt.graphics.ImageData;

import com.macb.utils.Utils;

public class ImageProcess {

	public ImageData rotate(ImageData srcData) {

		int bytesPerPixel = srcData.bytesPerLine / srcData.width;
		int destBytesPerLine = srcData.height * bytesPerPixel;
		byte[] newData = new byte[srcData.data.length];
		int width = 0, height = 0;

		for (int srcY = 0; srcY < srcData.height; srcY++) {
			for (int srcX = 0; srcX < srcData.width; srcX++) {
				int destX = 0, destY = 0, destIndex = 0, srcIndex = 0;
				destX = srcY;
				destY = srcData.width - srcX - 1;
				width = srcData.height;
				height = srcData.width;
				destIndex = (destY * destBytesPerLine)
						+ (destX * bytesPerPixel);
				srcIndex = (srcY * srcData.bytesPerLine)
						+ (srcX * bytesPerPixel);
				System.arraycopy(srcData.data, srcIndex, newData, destIndex,
						bytesPerPixel);
			}
		}

		return new ImageData(width, height, srcData.depth, srcData.palette,
				destBytesPerLine, newData);
	}

	public ImageData toQuantifiedImage(ImageData srcData) {

		byte[] sourceData = srcData.data;

		for (int srcY = 0; srcY < srcData.height; srcY++) {
			for (int srcX = 0; srcX < srcData.width; srcX++) {

				int[] rgb = new int[3];

				rgb[0] = sourceData[srcY * srcData.bytesPerLine + srcX * 3 + 2] & 0xff;
				rgb[1] = sourceData[srcY * srcData.bytesPerLine + srcX * 3 + 1] & 0xff;
				rgb[2] = sourceData[srcY * srcData.bytesPerLine + srcX * 3 + 0] & 0xff;

				int[] hsv = rgb2HSV(rgb);
				int Qs = 3, Qv = 3;
				int l = Qs * Qv * hsv[0] + Qv * hsv[1] + hsv[2];

				for (int k = 0; k < 3; k++) {
					sourceData[srcY * srcData.bytesPerLine + srcX * 3 + k] = (byte) l;
				}
			}
		}
		srcData.data = sourceData;
		return srcData;
	}

	public ImageData toGrayImage(ImageData srcData) {

		byte[] sourceData = srcData.data;

		for (int srcY = 0; srcY < srcData.height; srcY++) {
			for (int srcX = 0; srcX < srcData.width; srcX++) {
				// Gray=0.299R+0.587G+0.114B
				int b = sourceData[srcY * srcData.bytesPerLine + srcX * 3 + 0] & 0xff;
				int g = sourceData[srcY * srcData.bytesPerLine + srcX * 3 + 1] & 0xff;
				int r = sourceData[srcY * srcData.bytesPerLine + srcX * 3 + 2] & 0xff;
				int gray = (int) (0.299 * r + 0.587 * g + 0.114 * b);

				for (int k = 0; k < 3; k++) {
					sourceData[srcY * srcData.bytesPerLine + srcX * 3 + k] = (byte) gray;
				}
			}
		}

		srcData.data = sourceData;

		return srcData;
	}

	public ImageData threshold(int threshold, ImageData srcData) {

		byte[] sourceData = srcData.data;

		for (int i = 0; i < sourceData.length; i++) {
			if (sourceData[i] <= threshold) {
				sourceData[i] = 0;
			} else {
				sourceData[i] = (byte) 255;
			}
		}

		srcData.data = sourceData;

		return srcData;
	}

	private double getMean(ImageData srcData, int sx, int sy, int ex, int ey) {
		// System.out.println(sx + " " + sy + " " + ex + " " + ey);
		double mean = 0;
		for (int srcY = sy; srcY < ey; srcY++) {
			for (int srcX = sx; srcX < ex; srcX++) {
				mean += srcData.data[srcY * srcData.bytesPerLine + srcX * 3] & 0xff;
			}
		}
		return mean / ((ey - sy) * (ex - sx) * 1.0);
	}

	private double getVariance(ImageData srcData, int sx, int sy, int ex, int ey) {

		double mean = getMean(srcData, sx, sy, ex, ey);
		double variance = 0;
		for (int srcY = sy; srcY < ey; srcY++) {
			for (int srcX = sx; srcX < ex; srcX++) {
				variance += (mean - (double) (srcData.data[srcY
						* srcData.bytesPerLine + srcX * 3] & 0xff))
						* (mean - (double) (srcData.data[srcY
								* srcData.bytesPerLine + srcX * 3] & 0xff));
			}
		}
		return Math.sqrt(variance / ((ey - sy) * (ex - sx) * 1.0));
	}

	private int[] rgb2HSV(int[] rgb) {

		double h, s, v;
		int H;
		int S;
		int V;
		int r = rgb[0];
		int g = rgb[1];
		int b = rgb[2];

		int[] maxmin = new Utils().getMaxMin(rgb);
		int max = maxmin[0];
		int min = maxmin[1];
		int imax = maxmin[2];
		int imin = maxmin[3];
		if (max == min) {
			h = s = 0;
			v = r / 255.0;
		} else {
			int _v = max;

			double _r, _g, _b, _h = 0;
			_r = (_v - r) * 1.0 / (_v - min);
			_g = (_v - g) * 1.0 / (_v - min);
			_b = (_v - b) * 1.0 / (_v - min);

			v = max / 255.0;
			s = (_v - min) * 1.0 / _v;

			if (imax == 0) {
				if (imin == 1) {
					_h = 5 + _b;
				} else {
					_h = 1 - _g;
				}
			} else if (imax == 1) {
				if (imin == 2) {
					_h = 1 + _r;
				} else {
					_h = 3 - _b;
				}
			} else if (imax == 2) {
				if (imin == 0) {
					_h = 3 + _g;
				} else {
					_h = 5 - _r;
				}
			}
			h = _h * 60;
		}
		if (0 <= v && v < 0.2) {
			V = 0;
		} else if (0.2 <= v && v < 0.7) {
			V = 1;
		} else {
			V = 2;
		}

		if (0 <= s && s < 0.2) {
			S = 0;
		} else if (0.2 <= s && s < 0.7) {
			S = 1;
		} else {
			S = 2;
		}
		if (21 <= h && h <= 40) {
			H = 1;
		} else if (41 <= h && h <= 75) {
			H = 2;
		} else if (76 <= h && h <= 155) {
			H = 3;
		} else if (156 <= h && h <= 190) {
			H = 4;
		} else if (191 <= h && h <= 270) {
			H = 5;
		} else if (271 <= h && h <= 295) {
			H = 6;
		} else if (296 <= h && h <= 315) {
			H = 7;
		} else {
			H = 0;
		}
		return new int[] { H, S, V };
	}

	private double[] getImageColorFeatures(ImageData srcData) {

		double[] cfv = new double[10];
		int y = srcData.height / 4;
		int x = srcData.width / 4;
		ImageData quantifiedData = toQuantifiedImage(srcData);
		// color
		cfv[0] = getMean(quantifiedData, 0, 0, 2 * x, 2 * y);
		cfv[1] = getMean(quantifiedData, 2 * x, 0, 4 * x, 2 * y);
		cfv[2] = getMean(quantifiedData, 0, 2 * y, 2 * x, 4 * y);
		cfv[3] = getMean(quantifiedData, 2 * x, 2 * y, 4 * x, 4 * y);
		cfv[4] = getMean(quantifiedData, x, y, 3 * x, 3 * y);

		cfv[5 + 0] = getVariance(quantifiedData, 0, 0, 2 * x, 2 * y);
		cfv[5 + 1] = getVariance(quantifiedData, 2 * x, 0, 4 * x, 2 * y);
		cfv[5 + 2] = getVariance(quantifiedData, 0, 2 * y, 2 * x, 4 * y);
		cfv[5 + 3] = getVariance(quantifiedData, 2 * x, 2 * y, 4 * x, 4 * y);
		cfv[5 + 4] = getVariance(quantifiedData, x, y, 3 * x, 3 * y);

		return cfv;

	}

	private double[] getImageTextureFeatures(ImageData srcData) {

		double[] tfv = new double[10];
		int y = srcData.height / 4;
		int x = srcData.width / 4;
		ImageData grayData = toGrayImage(srcData);

		// texture
		tfv[0] = getMean(grayData, 0, 0, 2 * x, 2 * y);
		tfv[1] = getMean(grayData, 2 * x, 0, 4 * x, 2 * y);
		tfv[2] = getMean(grayData, 0, 2 * y, 2 * x, 4 * y);
		tfv[3] = getMean(grayData, 2 * x, 2 * y, 4 * x, 4 * y);
		tfv[4] = getMean(grayData, x, y, 3 * x, 3 * y);

		tfv[5 + 0] = getVariance(grayData, 0, 0, 2 * x, 2 * y);
		tfv[5 + 1] = getVariance(grayData, 2 * x, 0, 4 * x, 2 * y);
		tfv[5 + 2] = getVariance(grayData, 0, 2 * y, 2 * x, 4 * y);
		tfv[5 + 3] = getVariance(grayData, 2 * x, 2 * y, 4 * x, 4 * y);
		tfv[5 + 4] = getVariance(grayData, x, y, 3 * x, 3 * y);

		return tfv;

	}

	public double[] getUnitFeature(ImageData srcData) {

		double[] cv = getImageColorFeatures(srcData);
		double[] tv = getImageTextureFeatures(srcData);

		double[] ufv = new double[cv.length + tv.length];
		System.arraycopy(cv, 0, ufv, 0, cv.length);
		System.arraycopy(tv, 0, ufv, cv.length, tv.length);
		double mean = new Utils().getArrayMean(ufv);
		double variance = new Utils().getArrayVariance(ufv);
		// for (double d : ufv) {
		// System.out.println(d);
		// }
		// System.out.println("###########################");
		if (variance == 0) {
			for (int i = 0; i < ufv.length; i++) {
				ufv[i] = ufv[i] - mean;
				if (ufv[i] > 1) {
					ufv[i] = 1;
				}
				if (ufv[i] < -1) {
					ufv[i] = -1;
				}
			}
		} else {
			for (int i = 0; i < ufv.length; i++) {
				ufv[i] = (ufv[i] - mean) / (3.0 * variance);
				if (ufv[i] > 1) {
					ufv[i] = 1;
				}
				if (ufv[i] < -1) {
					ufv[i] = -1;
				}
			}
		}
		// for (double d : ufv) {
		// System.out.println(d);
		// }
		// System.out.println("###########################");
		for (int i = 0; i < ufv.length; i++) {
			ufv[i] = (ufv[i] + 1) / 2.0;
		}
		return ufv;
	}
}
