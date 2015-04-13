
package org.mule.modules.robo.config;

import javax.annotation.Generated;
import org.mule.config.MuleManifest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;


/**
 * Registers bean definitions parsers for handling elements in <code>http://www.mulesoft.org/schema/mule/robo</code>.
 * 
 */
@Generated(value = "Mule DevKit Version 3.5.1", date = "2014-11-06T04:06:21-03:00", comments = "Build UNNAMED.1967.45d0eb0")
public class RoboNamespaceHandler
    extends NamespaceHandlerSupport
{

    private static Logger logger = LoggerFactory.getLogger(RoboNamespaceHandler.class);

    private void handleException(String beanName, String beanScope, NoClassDefFoundError noClassDefFoundError) {
        String muleVersion = "";
        try {
            muleVersion = MuleManifest.getProductVersion();
        } catch (Exception _x) {
            logger.error("Problem while reading mule version");
        }
        logger.error(((((("Cannot launch the mule app, the  "+ beanScope)+" [")+ beanName)+"] within the connector [robo] is not supported in mule ")+ muleVersion));
        throw new FatalBeanException(((((("Cannot launch the mule app, the  "+ beanScope)+" [")+ beanName)+"] within the connector [robo] is not supported in mule ")+ muleVersion), noClassDefFoundError);
    }

    /**
     * Invoked by the {@link DefaultBeanDefinitionDocumentReader} after construction but before any custom elements are parsed. 
     * @see NamespaceHandlerSupport#registerBeanDefinitionParser(String, BeanDefinitionParser)
     * 
     */
    public void init() {
        try {
            this.registerBeanDefinitionParser("config", new RoboConnectorConfigDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("config", "@Config", ex);
        }
        try {
            this.registerBeanDefinitionParser("send-message", new SendMessageDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("send-message", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("send-command-name", new SendCommandNameDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("send-command-name", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("send-command-to-parse", new SendCommandToParseDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("send-command-to-parse", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("get-help", new GetHelpDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("get-help", "@Processor", ex);
        }
    }

}
