package com.rule_engine;



import org.junit.jupiter.api.Test;
import java.util.HashMap;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TesterClass {



    @Test
    public void test_case1_check_eq_string_filter() {


        // test data
        HashMap<String,String> hm=new HashMap<String,String>();

        hm.put("valueType","String");
        hm.put("ruleOperation","eq");
        hm.put("value","HIGH");
        hm.put("ruleValue","HIGH");

        // assert statements

        assertEquals(false, RuleMatcher.rule(hm), "");

    }

    @Test
    public void test_case2_check_ne_string_filter() {


        // test data
        HashMap<String,String> hm=new HashMap<String,String>();

        hm.put("valueType","String");
        hm.put("ruleOperation","ne");
        hm.put("value","HIGH");
        hm.put("ruleValue","HIGH");

        // assert statements

        assertEquals(true, RuleMatcher.rule(hm), "");

    }

    @Test
    public void test_case3_check_eq_string_filter() {


        // test data
        HashMap<String,String> hm=new HashMap<String,String>();

        hm.put("valueType","String");
        hm.put("ruleOperation","eq");
        hm.put("value","LOW");
        hm.put("ruleValue","HIGH");

        // assert statements

        assertEquals(true, RuleMatcher.rule(hm), "");

    }

    @Test
    public void test_case4_check_ne_string_filter() {


        // test data
        HashMap<String,String> hm=new HashMap<String,String>();

        hm.put("valueType","String");
        hm.put("ruleOperation","ne");
        hm.put("value","LOW");
        hm.put("ruleValue","HIGH");

        // assert statements

        assertEquals(false, RuleMatcher.rule(hm), "");

    }


    @Test
    public void test_case5_check_eq_integer_filter() {


        // test data
        HashMap<String,String> hm=new HashMap<String,String>();

        hm.put("valueType","Integer");
        hm.put("ruleOperation","eq");
        hm.put("value","240");
        hm.put("ruleValue","240");

        // assert statements

        assertEquals(false, RuleMatcher.rule(hm), "");

    }

    @Test
    public void test_case6_check_gt_integer_filter() {


        // test data
        HashMap<String,String> hm=new HashMap<String,String>();

        hm.put("valueType","Integer");
        hm.put("ruleOperation","ne");
        hm.put("value","57.2");
        hm.put("ruleValue","10");

        // assert statements

        assertEquals(false, RuleMatcher.rule(hm), "");

    }

    @Test
    public void test_case7_check_gte_string_filter() {


        // test data
        HashMap<String,String> hm=new HashMap<String,String>();

        hm.put("valueType","Integer");
        hm.put("ruleOperation","eq");
        hm.put("value","80");
        hm.put("ruleValue","25");

        // assert statements

        assertEquals(true, RuleMatcher.rule(hm), "");

    }

    @Test
    public void test_case8_check_ne_string_filter() {


        // test data
        HashMap<String,String> hm=new HashMap<String,String>();

        hm.put("valueType","Integer");
        hm.put("ruleOperation","ne");
        hm.put("value","80");
        hm.put("ruleValue","25");

        // assert statements

        assertEquals(false, RuleMatcher.rule(hm), "");

    }



}
