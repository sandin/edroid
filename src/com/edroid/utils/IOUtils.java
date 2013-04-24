package com.edroid.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import android.util.Log;

public final class IOUtils {
	private static final String TAG = IOUtils.class.getSimpleName();

	/**
	 * Convert InputStream into String, line by line
	 * 
	 * @param is
	 * @param charset
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String streamToString(InputStream is, String charset)
			throws UnsupportedEncodingException {
		if (is == null) {
		    Log.e(TAG, "Cann't convert inputStream to String, inputStream == null");
			return ""; // just return a empty string to ignore
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(is,
				charset));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return sb.toString();
	}

	private static final int BUFFER_SIZE = 1024 * 2;
	
	/**
	 * Copy a InputStream into OutputStream
	 * 
	 * @param input
	 * @param output
	 * @return
	 * @throws IOException
	 */
	public static int copy(InputStream input, OutputStream output)
			throws IOException {
		byte[] buffer = new byte[BUFFER_SIZE];

		BufferedInputStream in = new BufferedInputStream(input, BUFFER_SIZE);
		BufferedOutputStream out = new BufferedOutputStream(output, BUFFER_SIZE);
		int count = 0, n = 0;
		try {
			while ((n = in.read(buffer, 0, BUFFER_SIZE)) != -1) {
				out.write(buffer, 0, n);
				count += n;
			}
			out.flush();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				Log.e(TAG, e.getMessage());
			}
			try {
				in.close();
			} catch (IOException e) {
				Log.e(TAG, e.getMessage());
			}
		}
		return count;
	}
	
	
	/**
	 * 将一个InputStream写入到文件
	 * 
	 * @param is
	 * @param filename
	 * @return
	 */
	public static boolean writeInputStreamToFile(InputStream is, String filename) {
		Log.d("LDS", "new write to file");
		boolean result = false;
		BufferedInputStream in = null;
		BufferedOutputStream out = null;
		try {
			in = new BufferedInputStream(is);
			out = new BufferedOutputStream(new FileOutputStream(filename));
			byte[] buffer = new byte[1024];
			int l;
			while ((l = in.read(buffer)) != -1) {
				out.write(buffer, 0, l);
			}
			result = true;
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				if (in != null)
					in.close();
				if (out != null) {
					Log.d("LDS", "new write to file to -> " + filename);
					out.flush();
					out.close();
				}
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		return result;
	}
}
