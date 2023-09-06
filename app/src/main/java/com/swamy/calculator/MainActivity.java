package com.swamy.calculator;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.swamy.calculator.databinding.ActivityMainBinding;
import com.swamy.calculator.view.Viewer;

public class MainActivity extends AppCompatActivity{

    public ActivityMainBinding binding;
    Viewer viewer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewer =new Viewer(binding);

        binding.i1.setOnClickListener(view -> viewer.setnumberExpression("1"));
        binding.i2.setOnClickListener(view -> viewer.setnumberExpression("2"));
        binding.i3.setOnClickListener(view -> viewer.setnumberExpression("3"));
        binding.i4.setOnClickListener(view -> viewer.setnumberExpression("4"));
        binding.i5.setOnClickListener(view -> viewer.setnumberExpression("5"));
        binding.i6.setOnClickListener(view -> viewer.setnumberExpression("6"));
        binding.i7.setOnClickListener(view -> viewer.setnumberExpression("7"));
        binding.i8.setOnClickListener(view -> viewer.setnumberExpression("8"));
        binding.i9.setOnClickListener(view -> viewer.setnumberExpression("9"));
        binding.i0.setOnClickListener(view -> viewer.setnumberExpression("0"));
        binding.idot.setOnClickListener(view -> viewer.setnumberExpression("."));
        binding.addition.setOnClickListener(view -> viewer.setnumberExpression("+"));
        binding.subtraction.setOnClickListener(view -> viewer.setnumberExpression("-"));
        binding.multiply.setOnClickListener(view -> viewer.setnumberExpression("*"));
        binding.devidedby.setOnClickListener(view -> viewer.setnumberExpression("/"));
        binding.module.setOnClickListener(view -> viewer.setnumberExpression("%"));
        binding.AC.setOnClickListener(view->viewer.clear());
        binding.clr.setOnClickListener(view ->viewer.clrearOne());
        binding.equalto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.result.getText().toString().equals("")) {
                return;
                }
                viewer.calculate();
            }
        });




    }



    }
