package com.wotlab.sch.rtmp;

public class YuvToRGB {

    static public byte[] decodeYUV420SP(byte[] yuv420sp, int width, int height) {

        final int frameSize = width * height;
        byte[] rgbBuf = new byte[frameSize * 3];

        if (rgbBuf == null)
            throw new NullPointerException("buffer 'rgbBuf' is null");
        if (rgbBuf.length < frameSize * 3)
            throw new IllegalArgumentException("buffer 'rgbBuf' size "
                    + rgbBuf.length + " < minimum " + frameSize * 3);

        if (yuv420sp == null)
            throw new NullPointerException("buffer 'yuv420sp' is null");

        if (yuv420sp.length < frameSize * 3 / 2)
            throw new IllegalArgumentException("buffer 'yuv420sp' size " + yuv420sp.length
                    + " < minimum " + frameSize * 3 / 2);

        int i = 0, y = 0;
        int uvp = 0, u = 0, v = 0;
        int y1192 = 0, r = 0, g = 0, b = 0;

        for (int j = 0, yp = 0; j < height; j++) {
            uvp = frameSize + (j >> 1) * width;
            u = 0;
            v = 0;
            for (i = 0; i < width; i++, yp++) {
                y = (0xff & ((int) yuv420sp[yp])) - 16;
                if (y < 0) y = 0;
                if ((i & 1) == 0) {
                    v = (0xff & yuv420sp[uvp++]) - 128;
                    u = (0xff & yuv420sp[uvp++]) - 128;
                }

                y1192 = 1192 * y;
                r = (y1192 + 1634 * v);
                g = (y1192 - 833 * v - 400 * u);
                b = (y1192 + 2066 * u);

                if (r < 0) r = 0; else if (r > 262143) r = 262143;
                if (g < 0) g = 0; else if (g > 262143) g = 262143;
                if (b < 0) b = 0; else if (b > 262143) b = 262143;

                rgbBuf[yp * 3] = (byte)(r >> 10);
                rgbBuf[yp * 3 + 1] = (byte)(g >> 10);
                rgbBuf[yp * 3 + 2] = (byte)(b >> 10);
            }
        }

        return rgbBuf;
    }

    private static int R = 0;
	private static int G = 1;
	private static int B = 2;

	// I420是yuv420格式，是3个plane，排列方式为(Y)(U)(V)
	public static int[] I420ToRGB(byte[] src, int width, int height) {
		int numOfPixel = width * height;
		int positionOfV = numOfPixel;
		int positionOfU = numOfPixel / 4 + numOfPixel;
		int[] rgb = new int[numOfPixel * 3];

		for (int i = 0; i < height; i++) {
			int startY = i * width;
			int step = (i / 2) * (width / 2);
			int startU = positionOfV + step;
			int startV = positionOfU + step;
			for (int j = 0; j < width; j++) {
				int Y = startY + j;
				int U = startU + j / 2;
				int V = startV + j / 2;
				int index = Y * 3;
				RGB tmp = yuvTorgb(src[Y], src[U], src[V]);
				rgb[index + R] = tmp.r;
				rgb[index + G] = tmp.g;
				rgb[index + B] = tmp.b;
			}
		}

		return rgb;
	}

	private static class RGB {
		public int r, g, b;
	}

	private static RGB yuvTorgb(byte Y, byte U, byte V) {
		RGB rgb = new RGB();
		rgb.r = (int) ((Y & 0xff) + 1.4075 * ((V & 0xff) - 128));
		rgb.g = (int) ((Y & 0xff) - 0.3455 * ((U & 0xff) - 128) - 0.7169 * ((V & 0xff) - 128));
		rgb.b = (int) ((Y & 0xff) + 1.779 * ((U & 0xff) - 128));
		rgb.r = (rgb.r < 0 ? 0 : rgb.r > 255 ? 255 : rgb.r);
		rgb.g = (rgb.g < 0 ? 0 : rgb.g > 255 ? 255 : rgb.g);
		rgb.b = (rgb.b < 0 ? 0 : rgb.b > 255 ? 255 : rgb.b);
		return rgb;
	}

	// YV16是yuv422格式，是三个plane，(Y)(U)(V)
	public static int[] YV16ToRGB(byte[] src, int width, int height) {
		int numOfPixel = width * height;
		int positionOfU = numOfPixel;
		int positionOfV = numOfPixel / 2 + numOfPixel;
		int[] rgb = new int[numOfPixel * 3];
		for (int i = 0; i < height; i++) {
			int startY = i * width;
			int step = i * width / 2;
			int startU = positionOfU + step;
			int startV = positionOfV + step;
			for (int j = 0; j < width; j++) {
				int Y = startY + j;
				int U = startU + j / 2;
				int V = startV + j / 2;
				int index = Y * 3;
				// rgb[index+R] = (int)((src[Y]&0xff) + 1.4075 *
				// ((src[V]&0xff)-128));
				// rgb[index+G] = (int)((src[Y]&0xff) - 0.3455 *
				// ((src[U]&0xff)-128) - 0.7169*((src[V]&0xff)-128));
				// rgb[index+B] = (int)((src[Y]&0xff) + 1.779 *
				// ((src[U]&0xff)-128));
				RGB tmp = yuvTorgb(src[Y], src[U], src[V]);
				rgb[index + R] = tmp.r;
				rgb[index + G] = tmp.g;
				rgb[index + B] = tmp.b;
			}
		}
		return rgb;
	}

	// YV12是yuv420格式，是3个plane，排列方式为(Y)(V)(U)
	public static int[] YV12ToRGB(byte[] src, int width, int height) {
		int numOfPixel = width * height;
		int positionOfV = numOfPixel;
		int positionOfU = numOfPixel / 4 + numOfPixel;
		int[] rgb = new int[numOfPixel * 3];

		for (int i = 0; i < height; i++) {
			int startY = i * width;
			int step = (i / 2) * (width / 2);
			int startV = positionOfV + step;
			int startU = positionOfU + step;
			for (int j = 0; j < width; j++) {
				int Y = startY + j;
				int V = startV + j / 2;
				int U = startU + j / 2;
				int index = Y * 3;

				// rgb[index+R] = (int)((src[Y]&0xff) + 1.4075 *
				// ((src[V]&0xff)-128));
				// rgb[index+G] = (int)((src[Y]&0xff) - 0.3455 *
				// ((src[U]&0xff)-128) - 0.7169*((src[V]&0xff)-128));
				// rgb[index+B] = (int)((src[Y]&0xff) + 1.779 *
				// ((src[U]&0xff)-128));
				RGB tmp = yuvTorgb(src[Y], src[U], src[V]);
				rgb[index + R] = tmp.r;
				rgb[index + G] = tmp.g;
				rgb[index + B] = tmp.b;
			}
		}
		return rgb;
	}

	// YUY2是YUV422格式，排列是(YUYV)，是1 plane
	public static int[] YUY2ToRGB(byte[] src, int width, int height) {
		int numOfPixel = width * height;
		int[] rgb = new int[numOfPixel * 3];
		int lineWidth = 2 * width;
		for (int i = 0; i < height; i++) {
			int startY = i * lineWidth;
			for (int j = 0; j < lineWidth; j += 4) {
				int Y1 = j + startY;
				int Y2 = Y1 + 2;
				int U = Y1 + 1;
				int V = Y1 + 3;
				int index = (Y1 >> 1) * 3;
				RGB tmp = yuvTorgb(src[Y1], src[U], src[V]);
				rgb[index + R] = tmp.r;
				rgb[index + G] = tmp.g;
				rgb[index + B] = tmp.b;
				index += 3;
				tmp = yuvTorgb(src[Y2], src[U], src[V]);
				rgb[index + R] = tmp.r;
				rgb[index + G] = tmp.g;
				rgb[index + B] = tmp.b;
			}
		}
		return rgb;
	}

	// UYVY是YUV422格式，排列是(UYVY)，是1 plane
	public static int[] UYVYToRGB(byte[] src, int width, int height) {
		int numOfPixel = width * height;
		int[] rgb = new int[numOfPixel * 3];
		int lineWidth = 2 * width;
		for (int i = 0; i < height; i++) {
			int startU = i * lineWidth;
			for (int j = 0; j < lineWidth; j += 4) {
				int U = j + startU;
				int Y1 = U + 1;
				int Y2 = U + 3;
				int V = U + 2;
				int index = (U >> 1) * 3;
				RGB tmp = yuvTorgb(src[Y1], src[U], src[V]);
				rgb[index + R] = tmp.r;
				rgb[index + G] = tmp.g;
				rgb[index + B] = tmp.b;
				index += 3;
				tmp = yuvTorgb(src[Y2], src[U], src[V]);
				rgb[index + R] = tmp.r;
				rgb[index + G] = tmp.g;
				rgb[index + B] = tmp.b;
			}
		}
		return rgb;
	}

	// NV21是YUV420格式，排列是(Y), (VU)，是2 plane
	public static int[] NV21ToRGB(byte[] src, int width, int height) {
		int numOfPixel = width * height;
		int positionOfV = numOfPixel;
		int[] rgb = new int[numOfPixel * 3];

		for (int i = 0; i < height; i++) {
			int startY = i * width;
			int step = i / 2 * width;
			int startV = positionOfV + step;
			for (int j = 0; j < width; j++) {
				int Y = startY + j;
				int V = startV + j / 2;
				int U = V + 1;
				int index = Y * 3;
				RGB tmp = yuvTorgb(src[Y], src[U], src[V]);
				rgb[index + R] = tmp.r;
				rgb[index + G] = tmp.g;
				rgb[index + B] = tmp.b;
			}
		}
		return rgb;
	}

	// NV12是YUV420格式，排列是(Y), (UV)，是2 plane
	public static int[] NV12ToRGB(byte[] src, int width, int height) {
		int numOfPixel = width * height;
		int positionOfU = numOfPixel;
		int[] rgb = new int[numOfPixel * 3];

		for (int i = 0; i < height; i++) {
			int startY = i * width;
			int step = i / 2 * width;
			int startU = positionOfU + step;
			for (int j = 0; j < width; j++) {
				int Y = startY + j;
				int U = startU + j / 2;
				int V = U + 1;
				int index = Y * 3;
				RGB tmp = yuvTorgb(src[Y], src[U], src[V]);
				rgb[index + R] = tmp.r;
				rgb[index + G] = tmp.g;
				rgb[index + B] = tmp.b;
			}
		}
		return rgb;
	}

	// NV16是YUV422格式，排列是(Y), (UV)，是2 plane
	public static int[] NV16ToRGB(byte[] src, int width, int height) {
		int numOfPixel = width * height;
		int positionOfU = numOfPixel;
		int[] rgb = new int[numOfPixel * 3];

		for (int i = 0; i < height; i++) {
			int startY = i * width;
			int step = i * width;
			int startU = positionOfU + step;
			for (int j = 0; j < width; j++) {
				int Y = startY + j;
				int U = startU + j / 2;
				int V = U + 1;
				int index = Y * 3;
				RGB tmp = yuvTorgb(src[Y], src[U], src[V]);
				rgb[index + R] = tmp.r;
				rgb[index + G] = tmp.g;
				rgb[index + B] = tmp.b;
			}
		}
		return rgb;
	}

	// NV61是YUV422格式，排列是(Y), (VU)，是2 plane
	public static int[] NV61ToRGB(byte[] src, int width, int height) {
		int numOfPixel = width * height;
		int positionOfV = numOfPixel;
		int[] rgb = new int[numOfPixel * 3];

		for (int i = 0; i < height; i++) {
			int startY = i * width;
			int step = i * width;
			int startV = positionOfV + step;
			for (int j = 0; j < width; j++) {
				int Y = startY + j;
				int V = startV + j / 2;
				int U = V + 1;
				int index = Y * 3;
				RGB tmp = yuvTorgb(src[Y], src[U], src[V]);
				rgb[index + R] = tmp.r;
				rgb[index + G] = tmp.g;
				rgb[index + B] = tmp.b;
			}
		}
		return rgb;
	}

	// YVYU是YUV422格式，排列是(YVYU)，是1 plane
	public static int[] YVYUToRGB(byte[] src, int width, int height) {
		int numOfPixel = width * height;
		int[] rgb = new int[numOfPixel * 3];
		int lineWidth = 2 * width;
		for (int i = 0; i < height; i++) {
			int startY = i * lineWidth;
			for (int j = 0; j < lineWidth; j += 4) {
				int Y1 = j + startY;
				int Y2 = Y1 + 2;
				int V = Y1 + 1;
				int U = Y1 + 3;
				int index = (Y1 >> 1) * 3;
				RGB tmp = yuvTorgb(src[Y1], src[U], src[V]);
				rgb[index + R] = tmp.r;
				rgb[index + G] = tmp.g;
				rgb[index + B] = tmp.b;
				index += 3;
				tmp = yuvTorgb(src[Y2], src[U], src[V]);
				rgb[index + R] = tmp.r;
				rgb[index + G] = tmp.g;
				rgb[index + B] = tmp.b;
			}
		}
		return rgb;
	}

	// VYUY是YUV422格式，排列是(VYUY)，是1 plane
	public static int[] VYUYToRGB(byte[] src, int width, int height) {
		int numOfPixel = width * height;
		int[] rgb = new int[numOfPixel * 3];
		int lineWidth = 2 * width;
		for (int i = 0; i < height; i++) {
			int startV = i * lineWidth;
			for (int j = 0; j < lineWidth; j += 4) {
				int V = j + startV;
				int Y1 = V + 1;
				int Y2 = V + 3;
				int U = V + 2;
				int index = (U >> 1) * 3;
				RGB tmp = yuvTorgb(src[Y1], src[U], src[V]);
				rgb[index + R] = tmp.r;
				rgb[index + G] = tmp.g;
				rgb[index + B] = tmp.b;
				index += 3;
				tmp = yuvTorgb(src[Y2], src[U], src[V]);
				rgb[index + R] = tmp.r;
				rgb[index + G] = tmp.g;
				rgb[index + B] = tmp.b;
			}
		}
		return rgb;
	}
}
