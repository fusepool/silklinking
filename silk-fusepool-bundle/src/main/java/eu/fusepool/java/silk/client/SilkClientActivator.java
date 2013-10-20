/**
 * 
 */
package eu.fusepool.java.silk.client;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import eu.fusepool.java.silk.client.impl.SilkClientImpl;

/**
 * @author giorgio
 *
 */
public class SilkClientActivator implements BundleActivator {

	
	BundleContext ctx ;
	
	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		this.ctx = context ;
		System.out.println("######################\nAvvio SilkClient Bundle\n\n\n");
		SilkClientImpl client = new SilkClientImpl() ;
		ctx.registerService(SilkClient.class.getName(), client , null) ;
		client.setCtx(context) ;
		//client.excute() ; 
		

	}

	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		// TODO Auto-generated method stub

	}

}
