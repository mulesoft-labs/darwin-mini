
package org.mule.modules.robo.connectivity;

import javax.annotation.Generated;
import org.apache.commons.pool.KeyedObjectPool;
import org.mule.api.ConnectionException;
import org.mule.api.ConnectionExceptionCode;
import org.mule.api.MetadataAware;
import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.config.MuleProperties;
import org.mule.api.construct.FlowConstruct;
import org.mule.api.context.MuleContextAware;
import org.mule.api.devkit.ProcessAdapter;
import org.mule.api.devkit.ProcessTemplate;
import org.mule.api.devkit.capability.Capabilities;
import org.mule.api.devkit.capability.ModuleCapability;
import org.mule.api.lifecycle.Disposable;
import org.mule.api.lifecycle.Initialisable;
import org.mule.api.retry.RetryPolicyTemplate;
import org.mule.common.DefaultResult;
import org.mule.common.DefaultTestResult;
import org.mule.common.FailureType;
import org.mule.common.TestResult;
import org.mule.common.Testable;
import org.mule.devkit.processor.ExpressionEvaluatorSupport;
import org.mule.modules.robo.RoboConnector;
import org.mule.modules.robo.adapters.RoboConnectorConnectionIdentifierAdapter;
import org.mule.modules.robo.connection.ConnectionManager;
import org.mule.modules.robo.connection.UnableToAcquireConnectionException;
import org.mule.modules.robo.pooling.CacheConfig;
import org.mule.modules.robo.pooling.CachedObjectPoolAdapter;


/**
 * A {@code RoboConnectorConnectionManager} is a wrapper around {@link RoboConnector } that adds connection management capabilities to the pojo.
 * 
 */
@Generated(value = "Mule DevKit Version 3.5.1", date = "2014-11-06T04:06:21-03:00", comments = "Build UNNAMED.1967.45d0eb0")
public class RoboConnectorConnectionManager
    extends ExpressionEvaluatorSupport
    implements MetadataAware, MuleContextAware, ProcessAdapter<RoboConnectorConnectionIdentifierAdapter> , Capabilities, Disposable, Initialisable, Testable, ConnectionManager<RoboConnectorConnectionKey, RoboConnectorConnectionIdentifierAdapter>
{

    /**
     * 
     */
    private String robotAddr;
    /**
     * Mule Context
     * 
     */
    protected MuleContext muleContext;
    /**
     * Flow Construct
     * 
     */
    protected FlowConstruct flowConstruct;
    /**
     * Connector Pool
     * 
     */
    private KeyedObjectPool connectionPool;
    protected CacheConfig cacheConfig;
    protected RetryPolicyTemplate retryPolicyTemplate;
    private final static String MODULE_NAME = "Robo";
    private final static String MODULE_VERSION = "1.0-SNAPSHOT";
    private final static String DEVKIT_VERSION = "3.5.1";
    private final static String DEVKIT_BUILD = "UNNAMED.1967.45d0eb0";
    private final static String MIN_MULE_VERSION = "3.5.0";

    /**
     * Sets muleContext
     * 
     * @param value Value to set
     */
    public void setMuleContext(MuleContext value) {
        this.muleContext = value;
    }

    /**
     * Retrieves muleContext
     * 
     */
    public MuleContext getMuleContext() {
        return this.muleContext;
    }

    /**
     * Sets flowConstruct
     * 
     * @param value Value to set
     */
    public void setFlowConstruct(FlowConstruct value) {
        this.flowConstruct = value;
    }

    /**
     * Retrieves flowConstruct
     * 
     */
    public FlowConstruct getFlowConstruct() {
        return this.flowConstruct;
    }

    /**
     * Sets cacheConfig
     * 
     * @param value Value to set
     */
    public void setCacheConfig(CacheConfig value) {
        this.cacheConfig = value;
    }

    /**
     * Retrieves cacheConfig
     * 
     */
    public CacheConfig getCacheConfig() {
        return this.cacheConfig;
    }

    /**
     * Sets retryPolicyTemplate
     * 
     * @param value Value to set
     */
    public void setRetryPolicyTemplate(RetryPolicyTemplate value) {
        this.retryPolicyTemplate = value;
    }

    /**
     * Retrieves retryPolicyTemplate
     * 
     */
    public RetryPolicyTemplate getRetryPolicyTemplate() {
        return this.retryPolicyTemplate;
    }

    /**
     * Sets robotAddr
     * 
     * @param value Value to set
     */
    public void setRobotAddr(String value) {
        this.robotAddr = value;
    }

    /**
     * Retrieves robotAddr
     * 
     */
    public String getRobotAddr() {
        return this.robotAddr;
    }

    public void initialise() {
        connectionPool = new CachedObjectPoolAdapter(new RoboConnectorConnectionFactory(this), cacheConfig);
        if (retryPolicyTemplate == null) {
            retryPolicyTemplate = muleContext.getRegistry().lookupObject(MuleProperties.OBJECT_DEFAULT_RETRY_POLICY_TEMPLATE);
        }
    }

    @Override
    public void dispose() {
        try {
            connectionPool.close();
        } catch (Exception e) {
        }
    }

    public RoboConnectorConnectionIdentifierAdapter acquireConnection(RoboConnectorConnectionKey key)
        throws Exception
    {
        return ((RoboConnectorConnectionIdentifierAdapter) connectionPool.borrowObject(key));
    }

    public void releaseConnection(RoboConnectorConnectionKey key, RoboConnectorConnectionIdentifierAdapter connection)
        throws Exception
    {
        connectionPool.returnObject(key, connection);
    }

    public void destroyConnection(RoboConnectorConnectionKey key, RoboConnectorConnectionIdentifierAdapter connection)
        throws Exception
    {
        connectionPool.invalidateObject(key, connection);
    }

    /**
     * Returns true if this module implements such capability
     * 
     */
    public boolean isCapableOf(ModuleCapability capability) {
        if (capability == ModuleCapability.LIFECYCLE_CAPABLE) {
            return true;
        }
        if (capability == ModuleCapability.CONNECTION_MANAGEMENT_CAPABLE) {
            return true;
        }
        return false;
    }

    @Override
    public<P >ProcessTemplate<P, RoboConnectorConnectionIdentifierAdapter> getProcessTemplate() {
        return new ManagedConnectionProcessTemplate(this, muleContext);
    }

    @Override
    public RoboConnectorConnectionKey getDefaultConnectionKey() {
        return new RoboConnectorConnectionKey(getRobotAddr());
    }

    @Override
    public RoboConnectorConnectionKey getEvaluatedConnectionKey(MuleEvent event)
        throws Exception
    {
        if (event!= null) {
            final String _transformedRobotAddr = ((String) evaluateAndTransform(muleContext, event, this.getClass().getDeclaredField("robotAddr").getGenericType(), null, getRobotAddr()));
            if (_transformedRobotAddr == null) {
                throw new UnableToAcquireConnectionException("Parameter robotAddr in method connect can't be null because is not @Optional");
            }
            return new RoboConnectorConnectionKey(_transformedRobotAddr);
        }
        return getDefaultConnectionKey();
    }

    public String getModuleName() {
        return MODULE_NAME;
    }

    public String getModuleVersion() {
        return MODULE_VERSION;
    }

    public String getDevkitVersion() {
        return DEVKIT_VERSION;
    }

    public String getDevkitBuild() {
        return DEVKIT_BUILD;
    }

    public String getMinMuleVersion() {
        return MIN_MULE_VERSION;
    }

    public TestResult test() {
        RoboConnectorConnectionIdentifierAdapter connection = null;
        DefaultTestResult result;
        RoboConnectorConnectionKey key = getDefaultConnectionKey();
        try {
            connection = acquireConnection(key);
            result = new DefaultTestResult(org.mule.common.Result.Status.SUCCESS);
        } catch (Exception e) {
            try {
                destroyConnection(key, connection);
            } catch (Exception ie) {
            }
            result = ((DefaultTestResult) buildFailureTestResult(e));
        } finally {
            if (connection!= null) {
                try {
                    releaseConnection(key, connection);
                } catch (Exception ie) {
                }
            }
        }
        return result;
    }

    public DefaultResult buildFailureTestResult(Exception exception) {
        DefaultTestResult result;
        if (exception instanceof ConnectionException) {
            ConnectionExceptionCode code = ((ConnectionException) exception).getCode();
            if (code == ConnectionExceptionCode.UNKNOWN_HOST) {
                result = new DefaultTestResult(org.mule.common.Result.Status.FAILURE, exception.getMessage(), FailureType.UNKNOWN_HOST, exception);
            } else {
                if (code == ConnectionExceptionCode.CANNOT_REACH) {
                    result = new DefaultTestResult(org.mule.common.Result.Status.FAILURE, exception.getMessage(), FailureType.RESOURCE_UNAVAILABLE, exception);
                } else {
                    if (code == ConnectionExceptionCode.INCORRECT_CREDENTIALS) {
                        result = new DefaultTestResult(org.mule.common.Result.Status.FAILURE, exception.getMessage(), FailureType.INVALID_CREDENTIALS, exception);
                    } else {
                        if (code == ConnectionExceptionCode.CREDENTIALS_EXPIRED) {
                            result = new DefaultTestResult(org.mule.common.Result.Status.FAILURE, exception.getMessage(), FailureType.INVALID_CREDENTIALS, exception);
                        } else {
                            if (code == ConnectionExceptionCode.UNKNOWN) {
                                result = new DefaultTestResult(org.mule.common.Result.Status.FAILURE, exception.getMessage(), FailureType.UNSPECIFIED, exception);
                            } else {
                                result = new DefaultTestResult(org.mule.common.Result.Status.FAILURE, exception.getMessage(), FailureType.UNSPECIFIED, exception);
                            }
                        }
                    }
                }
            }
        } else {
            result = new DefaultTestResult(org.mule.common.Result.Status.FAILURE, exception.getMessage(), FailureType.UNSPECIFIED, exception);
        }
        return result;
    }

}
