package com.zhongzhou.modules.iot.vo;

public class IotTslRuntimeRowVO {
    private Long id;
    private String propIdentifier;
    private String functionName;
    private String updateTime;
    private String dataValue;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPropIdentifier() { return propIdentifier; }
    public void setPropIdentifier(String propIdentifier) { this.propIdentifier = propIdentifier; }
    public String getFunctionName() { return functionName; }
    public void setFunctionName(String functionName) { this.functionName = functionName; }
    public String getUpdateTime() { return updateTime; }
    public void setUpdateTime(String updateTime) { this.updateTime = updateTime; }
    public String getDataValue() { return dataValue; }
    public void setDataValue(String dataValue) { this.dataValue = dataValue; }
}
