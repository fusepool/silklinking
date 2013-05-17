/**
 * 
 */
package eu.fusepool.java.silk.client;

import java.io.File;
import java.io.InputStream;

/**
 * @author giorgio
 *
 */
public interface SilkClient {

	/**
	 * Executes loading a default configuration
	 * 
	 * @throws Exception
	 */
	void excute() throws Exception ;
	/**
	 * 
	 * @param config 
	 * @param linkSpecId
	 * @param numThreads
	 * @param reload
	 * @throws Exception
	 */
	void executeStream(InputStream config, String linkSpecId, int numThreads, boolean reload) throws Exception ;
	/**
	 * 
	 * @param config
	 * @param linkSpecId
	 * @param numThreads
	 * @param reload
	 * @throws Exception
	 */
	void executeFile(File config, String linkSpecId, int numThreads, boolean reload) throws Exception ;
	/**
	 * 
	 * @throws Exception
	 */
	void ExecuteConfig() throws Exception ;
}
