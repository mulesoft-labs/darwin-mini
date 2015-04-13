
package org.mule.modules.robo.adapters;

import javax.annotation.Generated;
import org.mule.api.MuleException;
import org.mule.api.lifecycle.Disposable;
import org.mule.api.lifecycle.Initialisable;
import org.mule.api.lifecycle.InitialisationException;
import org.mule.api.lifecycle.Startable;
import org.mule.api.lifecycle.Stoppable;
import org.mule.common.MuleVersion;
import org.mule.config.MuleManifest;
import org.mule.config.i18n.CoreMessages;
import org.mule.modules.robo.RoboConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * A <code>RoboConnectorLifecycleAdapter</code> is a wrapper around {@link RoboConnector } that adds lifecycle methods to the pojo.
 * 
 */
@Generated(value = "Mule DevKit Version 3.5.1", date = "2014-11-06T04:06:21-03:00", comments = "Build UNNAMED.1967.45d0eb0")
public class RoboConnectorLifecycleAdapter
    extends RoboConnectorMetadataAdapater
    implements Disposable, Initialisable, Startable, Stoppable
{


    @Override
    public void start()
        throws MuleException
    {
    }

    @Override
    public void stop()
        throws MuleException
    {
    }

    @Override
    public void initialise()
        throws InitialisationException
    {
        Logger log = LoggerFactory.getLogger(RoboConnectorLifecycleAdapter.class);
        MuleVersion connectorVersion = new MuleVersion("3.5.0");
        MuleVersion muleVersion = new MuleVersion(MuleManifest.getProductVersion());
        if (!muleVersion.atLeastBase(connectorVersion)) {
            throw new InitialisationException(CoreMessages.minMuleVersionNotMet(this.getMinMuleVersion()), this);
        }
    }

    @Override
    public void dispose() {
    }

}
