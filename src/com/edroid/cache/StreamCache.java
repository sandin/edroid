package com.edroid.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.edroid.utils.IOUtils;

import android.util.Log;

/**
 * Cache for InputStream
 * like HTTP response, input file
 */
public class StreamCache extends DiskCache<String, InputStream> {
    private static final String TAG = StreamCache.class.getSimpleName();
    
    /**
     * 
     * @param cacheBase
     * @param cachePrefix
     * @param cacheSuffix
     */
	public StreamCache(File cacheBase, String cachePrefix, String cacheSuffix) {
        super(cacheBase, cachePrefix, cacheSuffix);
        // TODO Auto-generated constructor stub
    }

	@Override
	protected void toDisk(String key, InputStream in, OutputStream out) {
		Log.d(TAG, "Cache InputStream to disk, use key " + key);
		try {
			int count = IOUtils.copy(in, out);
			Log.d(TAG, "Cache InputStream bytes: " + count);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected InputStream fromDisk(String key, InputStream in) {
		return null; // just get me a original inputStream
	}
	
	/**
     * Reads the value from disk using {@link #fromDisk(Object, InputStream)}.
     *
     * @param key
     * @return The value for key or null if the key doesn't map to any existing entries.
     */
	@Override
    public synchronized InputStream get(String key) throws IOException {
        final File readFrom = getFile(key);

        if (!readFrom.exists()){
            return null;
        }

        return new FileInputStream(readFrom);
    }

}
