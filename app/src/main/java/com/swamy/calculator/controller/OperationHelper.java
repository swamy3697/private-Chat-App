package com.swamy.calculator.controller;


import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlExpression;
import org.apache.commons.jexl3.MapContext;

public class OperationHelper {
    public OperationHelper()
    {
        this.lastchar="";
        this.inputValue="";
    }
    public String getLastchar() {
        return lastchar;
    }

    public void setLastchar(String lastchar) {
        this.lastchar = lastchar;
    }

    String lastchar;
    public void setFirstOperator(boolean firstOperator) {
        isFirstOperator = firstOperator;
    }

    boolean isFirstOperator;
    private String inputValue;
    public String getInputValue() {
        return this.inputValue;
    }

    public void setInputValue(String iValue) {
        this.inputValue =this.inputValue+iValue;
    }

    public String evaluate(String value)
    {

        try
        {
            JexlEngine jexl = new JexlBuilder().create();
            JexlExpression jexlExpression=jexl.createExpression(value);
            JexlContext jexlContext=new MapContext();
            Object result=jexlExpression.evaluate(jexlContext);
            return result.toString();
        }
        catch (Exception e)
        {
            return "Invalid";
        }
    }
    public void clr()
    {
        this.inputValue="";
    }
    public void setInput(String v)
    {
        this.inputValue=v;
    }


}
