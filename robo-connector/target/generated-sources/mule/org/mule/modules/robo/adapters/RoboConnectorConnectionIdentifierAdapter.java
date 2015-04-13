
package org.mule.modules.robo.adapters;

import javax.annotation.Generated;
import org.mule.modules.robo.RoboConnector;
import org.mule.modules.robo.connection.Connection;


/**
 * A <code>RoboConnectorConnectionIdentifierAdapter</code> is a wrapper around {@link RoboConnector } that implements {@link org.mule.devkit.dynamic.api.helper.Connection} interface.
 * 
 */
@Generated(value = "Mule DevKit Version 3.5.1", date = "2014-11-06T04:06:21-03:00", comments = "Build UNNAMED.1967.45d0eb0")
public class RoboConnectorConnectionIdentifierAdapter
    extends RoboConnectorProcessAdapter
    implements Connection
{


    public String getConnectionIdentifier() {
        return super.connectionId();
    }

}
