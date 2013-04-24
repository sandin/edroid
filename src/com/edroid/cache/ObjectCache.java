package com.edroid.cache;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.StreamCorruptedException;

/**
 * Object Cache 
 * cache any object you want
 */
public class ObjectCache extends LimitedDiscCache<Object>  {

    /**
     * @param cacheBase cache base path
     * @param cachePrefix cache prefix
     * @param cacheSuffix cache suffix 
     * @param sizeLimit max cache size, in bytes
     */
    public ObjectCache(File cacheBase, String cachePrefix, String cacheSuffix, int sizeLimit) {
        super(cacheBase, cachePrefix, cacheSuffix, sizeLimit);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void toDisk(String key, Object in, OutputStream out) {
        // TODO Auto-generated method stub
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(out);
            oos.writeObject(in);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (oos != null) oos.close();
            } catch (IOException e) {
            }
        }
    }

    @Override
    protected Object fromDisk(String key, InputStream in) {
        Object obj = null;
        ObjectInputStream ooi = null;
        try {
            ooi = new ObjectInputStream(in);
            obj = ooi.readObject();
        } catch (StreamCorruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (ooi != null) ooi.close();
            } catch (IOException e) {
            }
        }
        return obj;
    }
    
}
