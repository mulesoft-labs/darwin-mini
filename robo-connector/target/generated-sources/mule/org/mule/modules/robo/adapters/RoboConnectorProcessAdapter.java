
package org.mule.modules.robo.adapters;

import javax.annotation.Generated;
import org.mule.api.MuleEvent;
import org.mule.api.MuleMessage;
import org.mule.api.devkit.ProcessAdapter;
import org.mule.api.devkit.ProcessTemplate;
import org.mule.api.devkit.ProcessTemplate;
import org.mule.api.processor.MessageProcessor;
import org.mule.api.routing.filter.Filter;
import org.mule.modules.robo.RoboConnector;
import org.mule.security.oauth.callback.ProcessCallback;


/**
 * A <code>RoboConnectorProcessAdapter</code> is a wrapper around {@link RoboConnector } that enables custom processing strategies.
 * 
 */
@Generated(value = "Mule DevKit Version 3.5.1", date = "2014-11-06T04:06:21-03:00", comments = "Build UNNAMED.1967.45d0eb0")
public class RoboConnectorProcessAdapter
    extends RoboConnectorLifecycleAdapter
    implements ProcessAdapter<RoboConnectorCapabilitiesAdapter>
{


    public<P >ProcessTemplate<P, RoboConnectorCapabilitiesAdapter> getProcessTemplate() {
        final RoboConnectorCapabilitiesAdapter object = this;
        return new ProcessTemplate<P,RoboConnectorCapabilitiesAdapter>() {


            @Override
            public P execute(ProcessCallback<P, RoboConnectorCapabilitiesAdapter> processCallback, MessageProcessor messageProcessor, MuleEvent event)
                throws Exception
            {
                return processCallback.process(object);
            }

            @Override
            public P execute(ProcessCallback<P, RoboConnectorCapabilitiesAdapter> processCallback, Filter filter, MuleMessage message)
                throws Exception
            {
                return processCallback.process(object);
            }

        }
        ;
    }

}
