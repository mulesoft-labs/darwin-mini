
package org.mule.modules.robo.config;

import javax.annotation.Generated;
import org.mule.config.MuleManifest;
import org.mule.config.spring.parsers.generic.ChildDefinitionParser;
import org.mule.modules.robo.connectivity.RoboConnectorConnectionManager;
import org.mule.modules.robo.pooling.CacheConfig;
import org.mule.security.oauth.config.AbstractDevkitBasedDefinitionParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.parsing.BeanDefinitionParsingException;
import org.springframework.beans.factory.parsing.Location;
import org.springframework.beans.factory.parsing.Problem;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

@Generated(value = "Mule DevKit Version 3.5.1", date = "2014-11-06T04:06:21-03:00", comments = "Build UNNAMED.1967.45d0eb0")
public class RoboConnectorConfigDefinitionParser
    extends AbstractDevkitBasedDefinitionParser
{

    private static Logger logger = LoggerFactory.getLogger(RoboConnectorConfigDefinitionParser.class);

    public BeanDefinition parse(Element element, ParserContext parserContext) {
        parseConfigName(element);
        BeanDefinitionBuilder builder = getBeanDefinitionBuilder(parserContext);
        builder.setScope(BeanDefinition.SCOPE_SINGLETON);
        setInitMethodIfNeeded(builder, RoboConnectorConnectionManager.class);
        setDestroyMethodIfNeeded(builder, RoboConnectorConnectionManager.class);
        parseProperty(builder, element, "robotAddr", "robotAddr");
        BeanDefinitionBuilder connectionStrategyConfigBuilder = BeanDefinitionBuilder.rootBeanDefinition(CacheConfig.class.getName());
        Element connectionStrategyConfigElement = DomUtils.getChildElementByTagName(element, "cache-config");
        if (connectionStrategyConfigElement!= null) {
            ChildDefinitionParser child = new ChildDefinitionParser("cacheConfig", CacheConfig.class);
            child.forceParent(builder.getBeanDefinition());
            child.parse(connectionStrategyConfigElement, parserContext);
        }
        BeanDefinition definition = builder.getBeanDefinition();
        setNoRecurseOnDefinition(definition);
        parseRetryPolicyTemplate("reconnect", element, parserContext, builder, definition);
        parseRetryPolicyTemplate("reconnect-forever", element, parserContext, builder, definition);
        parseRetryPolicyTemplate("reconnect-custom-strategy", element, parserContext, builder, definition);
        return definition;
    }

    private BeanDefinitionBuilder getBeanDefinitionBuilder(ParserContext parserContext) {
        try {
            return BeanDefinitionBuilder.rootBeanDefinition(RoboConnectorConnectionManager.class.getName());
        } catch (NoClassDefFoundError noClassDefFoundError) {
            String muleVersion = "";
            try {
                muleVersion = MuleManifest.getProductVersion();
            } catch (Exception _x) {
                logger.error("Problem while reading mule version");
            }
            logger.error(("Cannot launch the mule app, the configuration [config] within the connector [robo] is not supported in mule "+ muleVersion));
            throw new BeanDefinitionParsingException(new Problem(("Cannot launch the mule app, the configuration [config] within the connector [robo] is not supported in mule "+ muleVersion), new Location(parserContext.getReaderContext().getResource()), null, noClassDefFoundError));
        }
    }

}
