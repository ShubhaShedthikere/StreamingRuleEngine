package com.rule_engine;

import java.util.*;


public class RuleMatcher {


    public static ArrayList<String> getRequiredFieldNames(){

        ArrayList<String> requiredFieldNames =  new ArrayList<String>();
        Collections.addAll(requiredFieldNames, "valueType","ruleOperation","value","ruleValue");
        return requiredFieldNames;

    }

    public static Boolean rule(Map<String, String> input_data)
    {
        if (input_data.get("valueType").equals("String"))
        {
            if (input_data.get("ruleOperation").equals("eq"))
            {
                return !(input_data.get("value").equals(input_data.get("ruleValue")));
            }
            else{
                return (input_data.get("value").equals(input_data.get("ruleValue")));
            }
        }

        if (input_data.get("valueType").equals("Integer"))
        {   float value = Float.parseFloat(input_data.get("value"));
            int rule_value = Integer.parseInt(input_data.get("ruleValue"));
            switch(input_data.get("ruleOperation")){
                case "eq":return !(value == rule_value);
                case "ne":return (value == rule_value );
                case "gt":return !(value > rule_value );
                case "lt":return !(value < rule_value );
                case "gte":return !(value >= rule_value );
                case "lte":return !(value <= rule_value );
            }
        }
        return Boolean.TRUE;

    }


}
