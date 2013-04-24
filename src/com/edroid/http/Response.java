package com.edroid.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.CharArrayBuffer;

public class Response {
	private final HttpResponse mResponse;

	public Response(HttpResponse res) {
		if (res == null) {
			throw new IllegalArgumentException("HttpResponse may not be null");
		}
		mResponse = res;
	}

	/**
	 * Convert Response to inputStream
	 * 
	 * @return InputStream or null
	 * @throws IOException 
	 * @throws IllegalStateException 
	 * @throws ResponseException
	 */
	public InputStream asStream() throws IllegalStateException, IOException {
		final HttpEntity entity = mResponse.getEntity();
		if (entity != null) {
			return entity.getContent();
		}
		return null;
	}

	/**
	 * Convert Response to Context String
	 * 
	 * @return response context string or null
	 * @throws IOException 
	 * @throws IllegalStateException 
	 * @throws ResponseException
	 */
	public String asString() throws IOException{
		return entityToString(mResponse.getEntity());
	}

	/**
	 * EntityUtils.toString(entity, "UTF-8");
	 * 
	 * @param entity
	 * @return
	 * @throws IllegalStateException 
	 * @throws IOException
	 * @throws ResponseException
	 */
	public static String entityToString(final HttpEntity entity) throws IOException {
		if (null == entity) {
			//throw new IllegalArgumentException("HTTP entity may not be null");
		    return "";
		}
		InputStream instream = entity.getContent();
		if (instream == null) {
			return "";
		}
		/*
		if (entity.getContentLength() > Integer.MAX_VALUE) {
			throw new IllegalArgumentException(
					"HTTP entity too large to be buffered in memory");
		}
		*/

		int i = (int) entity.getContentLength();
		if (i < 0) {
			i = 4096;
		}
		//Log.i("LDS", i + " content length");

		Reader reader = new BufferedReader(new InputStreamReader(instream,
				"UTF-8"));
		CharArrayBuffer buffer = new CharArrayBuffer(i);
		try {
			char[] tmp = new char[1024];
			int l;
			while ((l = reader.read(tmp)) != -1) {
				buffer.append(tmp, 0, l);
			}
		} finally {
			reader.close();
		}

		return buffer.toString();
	}

}
