
package org.mule.modules.robo.connectivity;

import javax.annotation.Generated;
import org.apache.commons.pool.KeyedPoolableObjectFactory;
import org.mule.api.context.MuleContextAware;
import org.mule.api.lifecycle.Disposable;
import org.mule.api.lifecycle.Initialisable;
import org.mule.api.lifecycle.Startable;
import org.mule.api.lifecycle.Stoppable;
import org.mule.modules.robo.adapters.RoboConnectorConnectionIdentifierAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Generated(value = "Mule DevKit Version 3.5.1", date = "2014-11-06T04:06:21-03:00", comments = "Build UNNAMED.1967.45d0eb0")
public class RoboConnectorConnectionFactory implements KeyedPoolableObjectFactory
{

    private static Logger logger = LoggerFactory.getLogger(RoboConnectorConnectionFactory.class);
    private RoboConnectorConnectionManager connectionManager;

    public RoboConnectorConnectionFactory(RoboConnectorConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public Object makeObject(Object key)
        throws Exception
    {
        if (!(key instanceof RoboConnectorConnectionKey)) {
            if (key == null) {
                logger.warn("Connection key is null");
            } else {
                logger.warn("Cannot cast key of type ".concat(key.getClass().getName().concat(" to ").concat("org.mule.modules.robo.connectivity.RoboConnectorConnectionKey")));
            }
            throw new RuntimeException("Invalid key type ".concat(key.getClass().getName()));
        }
        RoboConnectorConnectionIdentifierAdapter connector = new RoboConnectorConnectionIdentifierAdapter();
        if (connector instanceof MuleContextAware) {
            ((MuleContextAware) connector).setMuleContext(connectionManager.getMuleContext());
        }
        if (connector instanceof Initialisable) {
            ((Initialisable) connector).initialise();
        }
        if (connector instanceof Startable) {
            ((Startable) connector).start();
        }
        if (!connector.isConnected()) {
            connector.connect(((RoboConnectorConnectionKey) key).getRobotAddr());
        }
        return connector;
    }

    public void destroyObject(Object key, Object obj)
        throws Exception
    {
        if (!(key instanceof RoboConnectorConnectionKey)) {
            if (key == null) {
                logger.warn("Connection key is null");
            } else {
                logger.warn("Cannot cast key of type ".concat(key.getClass().getName().concat(" to ").concat("org.mule.modules.robo.connectivity.RoboConnectorConnectionKey")));
            }
            throw new RuntimeException("Invalid key type ".concat(key.getClass().getName()));
        }
        if (!(obj instanceof RoboConnectorConnectionIdentifierAdapter)) {
            if (obj == null) {
                logger.warn("Connector is null");
            } else {
                logger.warn("Cannot cast connector of type ".concat(obj.getClass().getName().concat(" to ").concat("org.mule.modules.robo.adapters.RoboConnectorConnectionIdentifierAdapter")));
            }
            throw new RuntimeException("Invalid connector type ".concat(obj.getClass().getName()));
        }
        try {
            ((RoboConnectorConnectionIdentifierAdapter) obj).disconnect();
        } catch (Exception e) {
            throw e;
        } finally {
            if (((RoboConnectorConnectionIdentifierAdapter) obj) instanceof Stoppable) {
                ((Stoppable) obj).stop();
            }
            if (((RoboConnectorConnectionIdentifierAdapter) obj) instanceof Disposable) {
                ((Disposable) obj).dispose();
            }
        }
    }

    public boolean validateObject(Object key, Object obj) {
        if (!(obj instanceof RoboConnectorConnectionIdentifierAdapter)) {
            if (obj == null) {
                logger.warn("Connector is null");
            } else {
                logger.warn("Cannot cast connector of type ".concat(obj.getClass().getName().concat(" to ").concat("org.mule.modules.robo.adapters.RoboConnectorConnectionIdentifierAdapter")));
            }
            throw new RuntimeException("Invalid connector type ".concat(obj.getClass().getName()));
        }
        try {
            return ((RoboConnectorConnectionIdentifierAdapter) obj).isConnected();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    public void activateObject(Object key, Object obj)
        throws Exception
    {
        if (!(key instanceof RoboConnectorConnectionKey)) {
            throw new RuntimeException("Invalid key type");
        }
        if (!(obj instanceof RoboConnectorConnectionIdentifierAdapter)) {
            throw new RuntimeException("Invalid connector type");
        }
        try {
            if (!((RoboConnectorConnectionIdentifierAdapter) obj).isConnected()) {
                ((RoboConnectorConnectionIdentifierAdapter) obj).connect(((RoboConnectorConnectionKey) key).getRobotAddr());
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public void passivateObject(Object key, Object obj)
        throws Exception
    {
    }

}
