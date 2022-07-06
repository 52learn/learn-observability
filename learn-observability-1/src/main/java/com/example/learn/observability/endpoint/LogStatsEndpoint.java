package com.example.learn.observability.endpoint;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;

import java.util.HashMap;
import java.util.Map;

@Endpoint(id = "logStats")
public class LogStatsEndpoint {
    Map<String, Object> map = new HashMap<>(16);
    public LogStatsEndpoint(){
        map.put("records", 100);
        Map<String, Object> debugLog = new HashMap<>(16);
        debugLog.put("records",20);
        map.put("debug",debugLog);
        Map<String, Object> infoLog = new HashMap<>(16);
        infoLog.put("records",70);
        map.put("info",infoLog);
        Map<String, Object> errorLog = new HashMap<>(16);
        errorLog.put("records",10);
        map.put("error",errorLog);
    }

    @ReadOperation
    public Map<String, Object> logInfo() {
        return map;
    }

    @ReadOperation
    public Map<String, Object> logInfoWithLevel(@Selector String logLevel) {
        return (Map<String, Object>)map.get(logLevel);
    }


    @WriteOperation
    public void changeLogRecords(String logLevel,Integer records){
        Map<String, Object> logLevelMap  = (Map<String, Object>)map.get(logLevel);
        if(logLevelMap!=null){
            logLevelMap.put("records",records);
        }
    }

    /*
    未实现
    @WriteOperation
    public void changeLogRecords2(LogLevelInfo logLevelInfo){
        Map<String, Object> logLevelMap  = (Map<String, Object>)map.get(logLevelInfo.getLogLevel());
        if(logLevelMap!=null){
            logLevelMap.put(logLevelInfo.getLogLevel(),logLevelInfo);
        }
    }*/
}