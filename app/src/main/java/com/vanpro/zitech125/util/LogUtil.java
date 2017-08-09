package com.vanpro.zitech125.util;

import android.util.Log;

import com.vanpro.zitech125.BuildConfig;

import java.io.File;
import java.io.RandomAccessFile;


public class LogUtil {

	public static final boolean isPrintLog = BuildConfig.DEBUG;

	public static void println(String msg) {
		if (isPrintLog) {
			System.out.println(msg == null ? "" : msg);
		}
	}

	public static void i(String tag, String msg) {
		if (isPrintLog) {
			android.util.Log.i(tag, msg == null ? "" : msg);
		}
	}

	public static void i(String tag, String msg, Throwable tr) {
		if (isPrintLog) {
			android.util.Log.i(tag, msg == null ? "" : msg, tr);
		}
	}

	public static void d(String msg){
		if (isPrintLog){
			android.util.Log.d("BASETAG", msg == null ? "" : msg);
		}
	}

	public static void d(String tag, String msg) {
		if (isPrintLog) {
			android.util.Log.d(tag, msg == null ? "" : msg);
		}
	}

	public static void d(String tag, String msg, Throwable tr) {
		if (isPrintLog) {
			android.util.Log.d(tag, msg == null ? "" : msg, tr);
		}
	}

	public static void e(String tag, String msg) {
		if (isPrintLog) {
			android.util.Log.e(tag, msg == null ? "" : msg);
		}
	}

	public static void e(String tag, String msg, Throwable tr) {
		if (isPrintLog) {
			android.util.Log.e(tag, msg == null ? "" : msg, tr);
		}
	}

	public static void v(String tag, String msg) {
		if (isPrintLog) {
			android.util.Log.v(tag, msg == null ? "" : msg);
		}
	}

	public static void v(String tag, String msg, Throwable tr) {
		if (isPrintLog) {
			android.util.Log.v(tag, msg == null ? "" : msg, tr);
		}
	}

	public static void w(String tag, String msg) {
		if (isPrintLog) {
			android.util.Log.w(tag, msg == null ? "" : msg);
		}
	}

	public static void w(String tag, String msg, Throwable tr) {
		if (isPrintLog) {
			android.util.Log.w(tag, msg == null ? "" : msg, tr);
		}
	}

	// 将字符串写入到文本文件中
	public static void writeTxtToFile(String strcontent) {
		//生成文件夹之后，再生成文件，不然会出错
		String appPath = Config.getAppDataPath();

		String strFilePath = appPath+"log.txt";
		// 每次写入时，都换行写
		String strContent = strcontent + "\r\n";
		try {
			File file = new File(strFilePath);
			if (!file.exists()) {
				Log.d("TestFile", "Create the file:" + strFilePath);
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
			RandomAccessFile raf = new RandomAccessFile(file, "rwd");
			raf.seek(file.length());
			raf.write(strContent.getBytes());
			raf.close();
		} catch (Exception e) {
			Log.e("TestFile", "Error on write File:" + e);
		}
	}

}
