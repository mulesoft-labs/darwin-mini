
package org.mule.modules.robo.processors;

import java.lang.reflect.Type;
import javax.annotation.Generated;
import org.mule.streaming.processor.AbstractDevkitBasedPageableMessageProcessor;

@Generated(value = "Mule DevKit Version 3.5.1", date = "2014-11-06T04:06:21-03:00", comments = "Build UNNAMED.1967.45d0eb0")
public abstract class AbstractPagedConnectedProcessor
    extends AbstractDevkitBasedPageableMessageProcessor
    implements ConnectivityProcessor
{

    protected Object robotAddr;
    protected String _robotAddrType;

    public AbstractPagedConnectedProcessor(String operationName) {
        super(operationName);
    }

    /**
     * Sets robotAddr
     * 
     * @param value Value to set
     */
    public void setRobotAddr(Object value) {
        this.robotAddr = value;
    }

    /**
     * Retrieves robotAddr
     * 
     */
    @Override
    public Object getRobotAddr() {
        return this.robotAddr;
    }

    /**
     * {@inheritDoc}
     * 
     */
    @Override
    public Type typeFor(String fieldName)
        throws NoSuchFieldException
    {
        return AbstractPagedConnectedProcessor.class.getDeclaredField(fieldName).getGenericType();
    }

}
