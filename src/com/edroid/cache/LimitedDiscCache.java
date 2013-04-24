package com.edroid.cache;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

/**
 * Limited Disc Cache
 * 
 * @param <V>
 */
public abstract class LimitedDiscCache<V> extends DiskCache<String, V> {
    
    private final Map<File, Long> lastUsageDates = Collections.synchronizedMap(new HashMap<File, Long>());
    private int cacheSize = 0;
    private int sizeLimit = 0; // bytes 

    /**
     * @param cacheBase cache base path
     * @param cachePrefix cache prefix
     * @param cacheSuffix cache suffix 
     * @param sizeLimit max caches size, in bytes
     */
    public LimitedDiscCache(File cacheBase, String cachePrefix,
            String cacheSuffix, int sizeLimit) {
        super(cacheBase, cachePrefix, cacheSuffix);
        this.sizeLimit = sizeLimit;
        calculateCacheSizeAndFillUsageMap();
    }

    private void calculateCacheSizeAndFillUsageMap() {
        int size = 0;
        File[] cachedFiles = mCacheBase.listFiles();
        for (File cachedFile : cachedFiles) {
            size += getCacheFileSize(cachedFile);
            lastUsageDates.put(cachedFile, cachedFile.lastModified());
        }
        cacheSize = size;
    }
    
    private int getCacheFileSize(File file) {
        return (int) file.length();
    };
    
    /**
     * cache it
     * 
     * @param cacke key
     * @param cache value
     */
    @Override
    public void put(String key, V value) throws IOException, FileNotFoundException {
        super.put(key, value); // cache it into file
        File saveAt = getFile(key); // which file
        
        int valueSize = getCacheFileSize(saveAt);
        while (cacheSize + valueSize > sizeLimit) {
            int freedSize = removeNext();
            if (freedSize == 0) break; // cache is empty (have nothing to delete)
            cacheSize -= freedSize;
        }
        cacheSize += valueSize;

        Long currentTime = System.currentTimeMillis();
        saveAt.setLastModified(currentTime);
        lastUsageDates.put(saveAt, currentTime);
    }
    
    /**
     * get cache by key
     * 
     * @param key
     */
    @Override
    public V get(String key) throws IOException  {
        V obj = super.get(key);
        
        final File saveAt = getFile(key);
        Long currentTime = System.currentTimeMillis();
        saveAt.setLastModified(currentTime);
        lastUsageDates.put(saveAt, currentTime);
        
        return obj;
    }
    
    /**
     * clear all cache 
     */
    @Override
    public boolean clear() {
        lastUsageDates.clear();
        cacheSize = 0;
        return super.clear();
    }
    
    /** Remove next file and returns it's size */
    private int removeNext() {
        if (lastUsageDates.isEmpty()) {
            return 0;
        }

        Long oldestUsage = null;
        File mostLongUsedFile = null;
        Set<Entry<File, Long>> entries = lastUsageDates.entrySet();
        synchronized (lastUsageDates) {
            for (Entry<File, Long> entry : entries) {
                if (mostLongUsedFile == null) {
                    mostLongUsedFile = entry.getKey();
                    oldestUsage = entry.getValue();
                } else {
                    Long lastValueUsage = entry.getValue();
                    if (lastValueUsage < oldestUsage) {
                        oldestUsage = lastValueUsage;
                        mostLongUsedFile = entry.getKey();
                    }
                }
            }
        }

        int fileSize = getCacheFileSize(mostLongUsedFile);
        if (mostLongUsedFile.delete()) {
            lastUsageDates.remove(mostLongUsedFile);
        }
        return fileSize;
    }
    
    /**
     * Get Total Cache size
     */
    @Override
    public int getCacheSize() {
        return cacheSize;
    }
    
}
