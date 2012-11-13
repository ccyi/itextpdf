package com.itextpdf.text.io;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.channels.FileChannel;

import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.pdf.BaseFont;

/**
 * Factory to create {@link RandomAccessSource} objects based on various types of sources
 * @since 5.3.5
 *
 */

public final class RandomAccessSourceFactory {

	/**
	 * whether the full content of the source should be read into memory at construction
	 */
	private boolean forceRead = false;
	
	/**
	 * Whether {@link RandomAccessFile} should be used instead of a {@link FileChannel}, where applicable
	 */
	private boolean usePlainRandomAccess = false;
	
	/**
	 * Creates a factory that will give preference to accessing the underling data source using memory mapped files
	 */
	public RandomAccessSourceFactory() {
	}
	
	/**
	 * Determines whether the full content of the source will be read into memory
	 * @param forceRead true if the full content will be read, false otherwise
	 * @return this object (this allows chaining of method calls)
	 */
	public RandomAccessSourceFactory setForceRead(boolean forceRead){
		this.forceRead = forceRead;
		return this;
	}
	
	/**
	 * Determines whether {@link RandomAccessFile} should be used as the primary data access mechanism
	 * @param usePlainRandomAccess whether {@link RandomAccessFile} should be used as the primary data access mechanism
	 * @return this object (this allows chaining of method calls)
	 */
	public RandomAccessSourceFactory setUsePlainRandomAccess(boolean usePlainRandomAccess){
		this.usePlainRandomAccess = usePlainRandomAccess;
		return this;
	}
	
	/**
	 * Creates a {@link RandomAccessSource} based on a byte array
	 * @param data the byte array
	 * @return the newly created {@link RandomAccessSource}
	 */
	public RandomAccessSource createSource(byte[] data){
		return new ArrayRandomAccessSource(data); 
	}
	
	/**
	 * Creates a {@link RandomAccessSource} based on a URL.  The data available at the URL is read into memory and used
	 * as the source for the {@link RandomAccessSource}
	 * @param url the url to read from
	 * @return the newly created {@link RandomAccessSource}
	 */
	public RandomAccessSource createSource(URL url) throws IOException{
        InputStream is = url.openStream();
        try {
        	return createSource(is);
        }
        finally {
            try {is.close();}catch(IOException ioe){}
        }
	}
	
	/**
	 * Creates a {@link RandomAccessSource} based on an {@link InputStream}.  The full content of the InputStream is read into memory and used
	 * as the source for the {@link RandomAccessSource}
	 * @param is the stream to read from
	 * @return the newly created {@link RandomAccessSource}
	 */
	public RandomAccessSource createSource(InputStream is) throws IOException{
       try {
        	return createSource(StreamUtil.inputStreamToArray(is));
        }
        finally {
            try {is.close();}catch(IOException ioe){}
        }		
	}
	
	/**
	 * Creates a {@link RandomAccessSource} based on a filename string.
	 * If the filename describes a URL, a URL based source is created
	 * If the filename describes a file on disk, the contents may be read into memory (if forceRead is true), opened using memory mapped file channel (if usePlainRandomAccess is false), or opened using {@link RandomAccessFile} access (if usePlainRandomAccess is true)
	 * This call will automatically failover to using {@link RandomAccessFile} if the memory map operation fails
	 * @param filename the name of the file or resource to create the {@link RandomAccessSource} for
	 * @return the newly created {@link RandomAccessSource}
	 */
	public RandomAccessSource createBestSource(String filename) throws IOException{
		File file = new File(filename);
		if (!file.canRead()){
	        if (filename.startsWith("file:/")
	        		|| filename.startsWith("http://") 
	                || filename.startsWith("https://")
	                || filename.startsWith("jar:")
	                || filename.startsWith("wsjar:")
	                || filename.startsWith("wsjar:")
	                || filename.startsWith("vfszip:")) {
	        	return createSource(new URL(filename));
	        } else {
	        	return createByReadingToMemory(filename);
	        }
		}
	        
        if (forceRead){
        	return createByReadingToMemory(filename);
        }
		
        if (usePlainRandomAccess){
        	return new RAFRandomAccessSource(new RandomAccessFile(file, "r"));
        }
        
		try{
			return new FileChannelRandomAccessSource(new FileInputStream(file).getChannel());
		} catch (IOException e){
			if (exceptionIsMapFailureException(e)){
				return new RAFRandomAccessSource(new RandomAccessFile(file, "r"));
			}
			throw e;
		}
		
	}
	
	/**
	 * Creates a new {@link RandomAccessSource} by reading the specified file/resource into memory
	 * @param filename the name of the resource to read
	 * @return the newly created {@link RandomAccessSource}
	 * @throws IOException if reading the underling file or stream fails
	 */
	private RandomAccessSource createByReadingToMemory(String filename) throws IOException {
    	//TODO: seems odd that we are using BaseFont here...
        InputStream is = BaseFont.getResourceStream(filename);
        if (is == null)
            throw new IOException(MessageLocalization.getComposedMessage("1.not.found.as.file.or.resource", filename));
        try {
        	return new ArrayRandomAccessSource(StreamUtil.inputStreamToArray(is));
        }
        finally {
            try {is.close();}catch(IOException ioe){}
        }
	}
	
    /**
     * Utility method that determines whether a given IOException is the result
     * of a failure to map a memory mapped file.  It would be better if the runtime
     * provided a special exception for this case, but it doesn't, so we have to rely
     * on parsing the exception message.
     * @param e the exception to check
     * @return true if the exception was the result of a failure to map a memory mapped file
     */
    private static boolean exceptionIsMapFailureException(IOException e){
        if (e.getMessage() != null && e.getMessage().indexOf("Map failed") >= 0)
            return true;

        return false;
    }
    

}