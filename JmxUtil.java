package util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

import javax.management.*;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import java.io.IOException;
import java.net.MalformedURLException;


@Slf4j
public class JmxUtil {
    private static String PARAMETER_START = "start";
    private static String PARAMETER_HOST = "host";
    private static String PARAMETER_PORT = "port";
    private static String PARAMETER_BEAN_NAME = "bean";
    private static String PARAMETER_BEAN_ATTRIBUTE_NAME = "attribute";


    public static void main(String[] args){
        String value = getBeanAttributeValue(args);
        System.out.println(value);
    }

    public static String getBeanAttributeValue(String[] args){
        CommandLine commandLine = buildCommandLine(args);
        ObjectName objectName = getObjectName(commandLine);
        MBeanServerConnection mBeanServerConnection = buildConnection(commandLine);
        MBeanAttributeInfo mBeanAttributeInfo = getMBeanAttributeInfo(mBeanServerConnection, objectName, commandLine);
        String value = getMbeanAttributeValue(mBeanAttributeInfo, objectName, mBeanServerConnection);
        return value;
    }

    public static String getMbeanAttributeValue(MBeanAttributeInfo mBeanAttributeInfo, ObjectName objectName,
                                                MBeanServerConnection mBeanServerConnection){
        if ( null == mBeanAttributeInfo){
            log.warn("null");
            return null;
        }
        String value = null;
        try {
            value = mBeanServerConnection.getAttribute(objectName, mBeanAttributeInfo.getName()).toString();
        } catch (MBeanException e) {
            log.error("AttributeNotFoundException:{}", LogExceptionStackUtil.logExceptionStack(e));
        } catch (AttributeNotFoundException e) {
            log.error("AttributeNotFoundException:{}", LogExceptionStackUtil.logExceptionStack(e));
        } catch (InstanceNotFoundException e) {
            log.error("InstanceNotFoundException:{}", LogExceptionStackUtil.logExceptionStack(e));
        } catch (ReflectionException e) {
            log.error("ReflectionException:{}", LogExceptionStackUtil.logExceptionStack(e));
        } catch (IOException e) {
            log.error("IOException:{}", LogExceptionStackUtil.logExceptionStack(e));
        }
        return value;
    }


    public static ObjectName getObjectName(CommandLine commandLine){
        String beanName = commandLine.getOptionValue(PARAMETER_BEAN_NAME);
        ObjectName objectName = null;
        try {
            objectName = new ObjectName(beanName);
        } catch (MalformedObjectNameException e) {
            log.error(LogExceptionStackUtil.logExceptionStack(e));
            return null;
        }
        return objectName;
    }

    public static MBeanAttributeInfo getMBeanAttributeInfo(MBeanServerConnection mBeanServerConnection, ObjectName objectName,
                                                           CommandLine commandLine) {

        MBeanInfo mBeanInfo = null;
        try {
            mBeanInfo = mBeanServerConnection.getMBeanInfo(objectName);
        } catch (InstanceNotFoundException e) {
            log.error("InstanceNotFoundException:{}", LogExceptionStackUtil.logExceptionStack(e));
        } catch (IntrospectionException e) {
            log.error("IntrospectionException:{}", LogExceptionStackUtil.logExceptionStack(e));
        } catch (ReflectionException e) {
            log.error("ReflectionException:{}", LogExceptionStackUtil.logExceptionStack(e));
        } catch (IOException e) {
            log.error("IOException:{}", LogExceptionStackUtil.logExceptionStack(e));
        }

        MBeanAttributeInfo[] mBeanAttributeInfos = mBeanInfo.getAttributes();
        String attributeName = null;
        for (MBeanAttributeInfo mBeanAttributeInfo : mBeanAttributeInfos) {
            attributeName = mBeanAttributeInfo.getName();
            if (attributeName.startsWith(commandLine.getOptionValue(PARAMETER_BEAN_ATTRIBUTE_NAME))) {
                return mBeanAttributeInfo;
            }
        }
        return null;
    }

    public static MBeanServerConnection buildConnection(CommandLine commandLine) {
        String host = commandLine.getOptionValue(PARAMETER_HOST);
        String port = commandLine.getOptionValue(PARAMETER_PORT);
        String url = "service:jmx:rmi:///jndi/rmi://" + host + ":" + port + "/jmxrmi";

        MBeanServerConnection mBeanServerConnection = null;
        JMXServiceURL serviceURL = null;
        try {
            serviceURL = new JMXServiceURL(url);
        } catch (MalformedURLException e) {
            log.error(LogExceptionStackUtil.logExceptionStack(e));
            return null;
        }
        final JMXConnector connector;
        try {
            connector = JMXConnectorFactory.connect(serviceURL);
        } catch (IOException e) {
            log.error(LogExceptionStackUtil.logExceptionStack(e));
            return null;
        }
        log.info("done.....");
        try {
            mBeanServerConnection = connector.getMBeanServerConnection();
        } catch (IOException e) {
            log.error(LogExceptionStackUtil.logExceptionStack(e));
            return null;
        }
        return mBeanServerConnection;
    }


    public static CommandLine buildCommandLine(String[] args){
        CommandLine commandLine = null;
        Options options = new Options();
        options.addOption(PARAMETER_START, false, "The command start flag");
        options.addOption(PARAMETER_HOST, true, "The host address");
        options.addOption(PARAMETER_PORT, true, "The host port");
        options.addOption(PARAMETER_BEAN_NAME, true, "The Bean Name ");
        options.addOption(PARAMETER_BEAN_ATTRIBUTE_NAME, true, "The Bean attribute Name ");

        BasicParser parser = new BasicParser();
        try {
            commandLine = parser.parse( options, args );
        } catch( Exception e ) {
            log.error("input exception is:{}", LogExceptionStackUtil.logExceptionStack(e));
        }
        if (!commandLine.hasOption(PARAMETER_START)) {
            log.error("have no PARAMETER_START:{}. Then exit....", PARAMETER_START);
            System.exit(2);
        }
        return commandLine;
    }
}
