package com.rule_engine;

import com.fasterxml.jackson.annotation.JsonProperty;



public class RuleFormat {

    @JsonProperty("rule_signal")
    private String ruleSignal;
    @JsonProperty("rule_type")
    private String ruleType;
    @JsonProperty("rule_value")
    private String ruleValue;
    @JsonProperty("rule_operation")
    private String ruleOperation;

    @JsonProperty("rule_signal")
    public String getRuleSignal() {
        return ruleSignal;
    }

    @JsonProperty("rule_signal")
    public void setRuleSignal(String ruleSignal) {
        this.ruleSignal = ruleSignal;
    }

    @JsonProperty("rule_type")
    public String getRuleType() {
        return ruleType;
    }

    @JsonProperty("rule_type")
    public void setRuleType(String ruleType) {
        this.ruleType = ruleType;
    }

    @JsonProperty("rule_value")
    public String getRuleValue() {
        return ruleValue;
    }

    @JsonProperty("rule_value")
    public void setRuleValue(String ruleValue) {
        this.ruleValue = ruleValue;
    }

    @JsonProperty("rule_operation")
    public String getRuleOperation() {
        return ruleOperation;
    }

    @JsonProperty("rule_operation")
    public void setRuleOperation(String ruleOperation) {
        this.ruleOperation = ruleOperation;
    }

}
