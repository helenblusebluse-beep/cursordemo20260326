package com.zhongzhou.modules.iot.vo;

public class IotTslServiceInvocationRowVO {
    private String invokeTime;
    private String serviceIdentifier;
    private String serviceName;
    private String inputParams;
    private String outputParams;

    public String getInvokeTime() { return invokeTime; }
    public void setInvokeTime(String invokeTime) { this.invokeTime = invokeTime; }
    public String getServiceIdentifier() { return serviceIdentifier; }
    public void setServiceIdentifier(String serviceIdentifier) { this.serviceIdentifier = serviceIdentifier; }
    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    public String getInputParams() { return inputParams; }
    public void setInputParams(String inputParams) { this.inputParams = inputParams; }
    public String getOutputParams() { return outputParams; }
    public void setOutputParams(String outputParams) { this.outputParams = outputParams; }
}
