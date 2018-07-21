<?xml version="1.0" encoding="utf-8" ?>
<Configuration status="info" monitorInterval="1800">
    <properties>
        <property name="LOG_HOME">logs</property>
        <property name="ERROR_LOG_FILE_NAME">runlog</property>
    </properties>

    <Appenders>
        <Console name="Console" target="SYSTEM_OUT">
            <PatternLayout pattern="%d %-5p (%F:%L) - %m%n" />
        </Console>
        <RollingRandomAccessFile name="FileLog"
                                 fileName="${LOG_HOME}/${ERROR_LOG_FILE_NAME}.log"
                                 filePattern="${LOG_HOME}/${ERROR_LOG_FILE_NAME}.log.%d{yyyy-MM-dd}.gz">
            <PatternLayout pattern="%d %-5p (%F:%L) - %m%n"/>
            <Policies>
                <TimeBasedTriggeringPolicy/>
                <SizeBasedTriggeringPolicy size="20MB"/>
            </Policies>
            <DefaultRolloverStrategy max="20"/>
        </RollingRandomAccessFile>
    </Appenders>

    <Loggers>
        <!-- 将业务dao接口填写进去,并用控制台输出即可 -->  
        <logger name="cd.jason.log.LogFactory" level="trace" additivity="false"> 
            <appender-ref ref="Console"/>  
        </logger>  
         <root level="info" includeLocation="true">
            <appender-ref ref="Console"/>
        </root>
    </Loggers>
</Configuration>