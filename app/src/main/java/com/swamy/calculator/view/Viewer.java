package com.swamy.calculator.view;

import android.util.Log;
import android.widget.Toast;

import com.swamy.calculator.MainActivity;
import com.swamy.calculator.controller.OperationHelper;
import com.swamy.calculator.databinding.ActivityMainBinding;

import java.util.Locale;

import com.swamy.calculator.databinding.ActivityMainBinding;
public class Viewer {
    OperationHelper operationHelper;
    ActivityMainBinding binding;
    int valuelen;


    public Viewer(ActivityMainBinding binding)
    {
        operationHelper=new OperationHelper();
        this.binding=binding;
        this.valuelen=0;
    }
    public void setnumberExpression(String val)

    {
        Log.d("len", "setnumberExpression: "+valuelen);
        if(this.valuelen>15)
        {
            clear();
            this.valuelen=0;
            return;
        }
        this.valuelen+=1;
        Log.d("len", "setnumberExpression: "+valuelen);
        if(val.equals(operationHelper.getLastchar()))
        {
         return;
        }
        if(val.equals("+") || val.equals("%") || val.equals("/") || val.equals("*") || val.equals("-"))
        {

            operationHelper.setLastchar(val);
        }
        else
        {
            operationHelper.setLastchar("");
        }

        if(operationHelper.getInputValue().equals("Invalid"))
        {
            clear();
        }
        operationHelper.setInputValue(val);
        binding.result.setText(operationHelper.getInputValue());
    }

    public void calculate()
    {
        String ans=operationHelper.evaluate(operationHelper.getInputValue());
        binding.result.setText(ans);
        operationHelper.setInput(ans);
        //operationHelper.setInputValue(ans);
    }
    public void clear()
    {
        Log.d("main", "clear: "+operationHelper.getInputValue());
        operationHelper.setInputValue("");
        binding.result.setText("");
        operationHelper.clr();
        Log.d("main", "clear: "+operationHelper.getInputValue());
    }
    public void clrearOne()
    {
        if(binding.result.getText().toString().equals(""))
        {
            return;
        }
        if(operationHelper.getInputValue().equals("Invalid"))
        {
            clear();
            return;
        }
        String tempValue=operationHelper.getInputValue();
        int len=tempValue.length();
        String newval=tempValue.substring(0,len-1);
        operationHelper.setInput(newval);
        binding.result.setText(operationHelper.getInputValue());
    }
}
