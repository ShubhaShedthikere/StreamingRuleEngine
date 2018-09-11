package com.rule_engine;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;


public class EventMessage implements Serializable {

    @JsonProperty("signal")
    private String signal;
    @JsonProperty("value_type")
    private String valueType;
    @JsonProperty("value")
    private String value;

    @JsonProperty("signal")
    public String getSignal() {
        return signal;
    }

    @JsonProperty("signal")
    public void setSignal(String signal) {
        this.signal = signal;
    }

    @JsonProperty("value_type")
    public String getValueType() {
        return valueType;
    }

    @JsonProperty("value_type")
    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    @JsonProperty("value")
    public String getValue() {
        return value;
    }

    @JsonProperty("value")
    public void setValue(String value) {
        this.value = value;
    }



}


