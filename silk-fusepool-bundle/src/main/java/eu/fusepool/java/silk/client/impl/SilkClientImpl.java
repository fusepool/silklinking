/**
 *
 */
package eu.fusepool.java.silk.client.impl;

import java.io.File;
import java.io.InputStream;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;

import eu.fusepool.java.silk.client.SilkClient;
import eu.fusepool.scala.silk.Silk;

/**
 * @author giorgio
 *
 */
@Component(immediate = true, metatype = true)
@Service(SilkClient.class)
public class SilkClientImpl implements SilkClient {

    protected BundleContext ctx;
    protected ComponentContext componentContext;

    /* (non-Javadoc)
     * @see eu.fusepool.java.silk.client.SilkClient#executeStream(java.io.InputStream, java.lang.String, int, boolean)
     */
    public void executeStream(InputStream config, String linkSpecId,
            int numThreads, boolean reload) {
        Silk.executeStream(config, linkSpecId, numThreads, reload);
    }

    /* (non-Javadoc)
     * @see eu.fusepool.java.silk.client.SilkClient#executeFile(java.io.File, java.lang.String, int, boolean)
     */
    public void executeFile(File config, String linkSpecId, int numThreads,
            boolean reload) {
        Silk.executeFile(config, linkSpecId, numThreads, reload);
    }


    @Activate
    protected void activate(final ComponentContext componentContext) {
        this.componentContext = componentContext;
        //this.excute() ;

    }

}
