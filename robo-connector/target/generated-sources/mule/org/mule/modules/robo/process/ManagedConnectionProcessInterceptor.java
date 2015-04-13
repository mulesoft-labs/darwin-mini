
package org.mule.modules.robo.process;

import java.util.List;
import javax.annotation.Generated;
import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.MuleMessage;
import org.mule.api.devkit.ProcessInterceptor;
import org.mule.api.processor.MessageProcessor;
import org.mule.api.routing.filter.Filter;
import org.mule.devkit.processor.ExpressionEvaluatorSupport;
import org.mule.modules.robo.adapters.RoboConnectorConnectionIdentifierAdapter;
import org.mule.modules.robo.connection.ConnectionManager;
import org.mule.modules.robo.connection.UnableToAcquireConnectionException;
import org.mule.modules.robo.connection.UnableToReleaseConnectionException;
import org.mule.modules.robo.connectivity.RoboConnectorConnectionKey;
import org.mule.modules.robo.processors.ConnectivityProcessor;
import org.mule.security.oauth.callback.ProcessCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Generated(value = "Mule DevKit Version 3.5.1", date = "2014-11-06T04:06:21-03:00", comments = "Build UNNAMED.1967.45d0eb0")
public class ManagedConnectionProcessInterceptor<T >
    extends ExpressionEvaluatorSupport
    implements ProcessInterceptor<T, RoboConnectorConnectionIdentifierAdapter>
{

    private static Logger logger = LoggerFactory.getLogger(ManagedConnectionProcessInterceptor.class);
    private final ConnectionManager<RoboConnectorConnectionKey, RoboConnectorConnectionIdentifierAdapter> connectionManager;
    private final MuleContext muleContext;
    private final ProcessInterceptor<T, RoboConnectorConnectionIdentifierAdapter> next;

    public ManagedConnectionProcessInterceptor(ProcessInterceptor<T, RoboConnectorConnectionIdentifierAdapter> next, ConnectionManager<RoboConnectorConnectionKey, RoboConnectorConnectionIdentifierAdapter> connectionManager, MuleContext muleContext) {
        this.next = next;
        this.connectionManager = connectionManager;
        this.muleContext = muleContext;
    }

    @Override
    public T execute(ProcessCallback<T, RoboConnectorConnectionIdentifierAdapter> processCallback, RoboConnectorConnectionIdentifierAdapter object, MessageProcessor messageProcessor, MuleEvent event)
        throws Exception
    {
        RoboConnectorConnectionIdentifierAdapter connection = null;
        RoboConnectorConnectionKey key = null;
        if (hasConnectionKeysOverride(messageProcessor)) {
            ConnectivityProcessor connectivityProcessor = ((ConnectivityProcessor) messageProcessor);
            final String _transformedRobotAddr = ((String) evaluateAndTransform(muleContext, event, connectivityProcessor.typeFor("_robotAddrType"), null, connectivityProcessor.getRobotAddr()));
            if (_transformedRobotAddr == null) {
                throw new UnableToAcquireConnectionException("Parameter robotAddr in method connect can't be null because is not @Optional");
            }
            key = new RoboConnectorConnectionKey(_transformedRobotAddr);
        } else {
            key = connectionManager.getEvaluatedConnectionKey(event);
        }
        try {
            if (logger.isDebugEnabled()) {
                logger.debug(("Attempting to acquire connection using "+ key.toString()));
            }
            connection = connectionManager.acquireConnection(key);
            if (connection == null) {
                throw new UnableToAcquireConnectionException();
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug((("Connection has been acquired with [id="+ connection.getConnectionIdentifier())+"]"));
                }
            }
            return next.execute(processCallback, connection, messageProcessor, event);
        } catch (Exception e) {
            if (processCallback.getManagedExceptions()!= null) {
                for (Class exceptionClass: ((List<Class<? extends Exception>> ) processCallback.getManagedExceptions())) {
                    if (exceptionClass.isInstance(e)) {
                        if (logger.isDebugEnabled()) {
                            logger.debug((((("An exception ( "+ exceptionClass.getName())+") has been thrown. Destroying the connection with [id=")+ connection.getConnectionIdentifier())+"]"));
                        }
                        try {
                            if (connection!= null) {
                                connectionManager.destroyConnection(key, connection);
                                connection = null;
                            }
                        } catch (Exception innerException) {
                            logger.error(innerException.getMessage(), innerException);
                        }
                    }
                }
            }
            throw e;
        } finally {
            try {
                if (connection!= null) {
                    if (logger.isDebugEnabled()) {
                        logger.debug((("Releasing the connection back into the pool [id="+ connection.getConnectionIdentifier())+"]"));
                    }
                    connectionManager.releaseConnection(key, connection);
                }
            } catch (Exception e) {
                throw new UnableToReleaseConnectionException(e);
            }
        }
    }

    /**
     * Validates that the current message processor has changed any of its connection parameters at processor level. If so, a new RoboConnectorConnectionKey must be generated
     * 
     * @param messageProcessor
     *     the message processor to test against the keys
     * @return
     *     true if any of the parameters in @Connect method annotated with @ConnectionKey was override in the XML, false otherwise  
     */
    private Boolean hasConnectionKeysOverride(MessageProcessor messageProcessor) {
        if ((messageProcessor == null)||(!(messageProcessor instanceof ConnectivityProcessor))) {
            return false;
        }
        ConnectivityProcessor connectivityProcessor = ((ConnectivityProcessor) messageProcessor);
        if (connectivityProcessor.getRobotAddr()!= null) {
            return true;
        }
        return false;
    }

    public T execute(ProcessCallback<T, RoboConnectorConnectionIdentifierAdapter> processCallback, RoboConnectorConnectionIdentifierAdapter object, Filter filter, MuleMessage message)
        throws Exception
    {
        throw new UnsupportedOperationException();
    }

}
