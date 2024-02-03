package com.swamy.calculator;


import static android.app.PendingIntent.getActivity;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

        SharedPreferences sharedPreferences=this.getPreferences(Context.MODE_PRIVATE);
        //SharedPreferences.Editor sharedp= sharedPreferences.edit();

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
        binding.equalto.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {
               // Toast.makeText(MainActivity.this, "long clicked", Toast.LENGTH_SHORT).show();

                boolean isPassSet=sharedPreferences.getBoolean("isSet",false);
                if(isPassSet){
                    ConstraintLayout forgotLayout=binding.dialogView2;
                    forgotLayout.setVisibility(View.VISIBLE);
                    binding.change.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String secondaryPassword=binding.secondarypassword.getText().toString();
                            String tempPass=sharedPreferences.getString("Secondary_Password","3697");
                            //Toast.makeText(MainActivity.this, tempPass, Toast.LENGTH_SHORT).show();

                            if(!tempPass.equals(secondaryPassword)){
                                //binding.secondarypassword.setBackground(ContextCompat.getDrawable(MainActivity.this,R.drawable.bg_for_edit_text_red));
                                binding.wSp.setVisibility(View.VISIBLE);
                                binding.secondarypassword.setText("");
                                return;
                            }
                            binding.wSp.setVisibility(View.GONE);
                            String password=binding.changepassword2.getText().toString();
                            if(!binding.changepassword1.getText().toString().equals(password))
                            {
                                binding.wp.setVisibility(View.VISIBLE);
                                return;
                            }
                            SharedPreferences.Editor sharedp=sharedPreferences.edit();
                            sharedp.putString("password",password);
                            sharedp.putBoolean("isSet",true);
                            Toast.makeText(MainActivity.this, "Password Updated", Toast.LENGTH_SHORT).show();

                            forgotLayout.setVisibility(View.GONE);
                        }
                    });
                    //clears everything like error msgs and text in edittext fields
                    allclear();
                    return false;
                }
                // new password setting
                ConstraintLayout setLayout=binding.dialogView;
                setLayout.setVisibility(View.VISIBLE);
                binding.set.setOnClickListener(view1 -> {
                    if(!binding.password1.getText().toString().equals(binding.password2.getText().toString())){
                        binding.wpom.setVisibility(View.VISIBLE);
                        return;
                    }
                    if(!binding.secondarypassword1.getText().toString().equals(binding.secondarypassword2.getText().toString())){
                        binding.wspom.setVisibility(View.VISIBLE);
                        return;
                    }
                    String password=binding.password1.getText().toString();
                    String secondaryPass=binding.password2.getText().toString();
                    setPassword(password,secondaryPass);
                    setLayout.setVisibility(View.GONE);
                });
                return false;
            }
        });
        binding.equalto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass=sharedPreferences.getString("password","3697");
                String enteredPassword=binding.result.getText().toString();
                //Toast.makeText(MainActivity.this, "in it", Toast.LENGTH_SHORT).show();
                //Toast.makeText(MainActivity.this, pass+" "+enteredPassword, Toast.LENGTH_SHORT).show();
                if(pass.equals(enteredPassword)){
                    //Toast.makeText(MainActivity.this, "in if", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(MainActivity.this,ChatActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    return;
                }

                if(binding.result.getText().toString().equals("")) {
                return;
                }
                viewer.calculate();
            }
        });

        OnBackPressedCallback onBackPressedCallback=new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                boolean ispasswordSetDialogShowed=binding.dialogView.isShown();
                if(ispasswordSetDialogShowed){
                    binding.dialogView.setVisibility(View.GONE);
                    return;
                }
                boolean isChangeDialogShowed=binding.dialogView2.isShown();
                if(isChangeDialogShowed){
                    binding.dialogView2.setVisibility(View.GONE);
                    return;
                }
                finish();

            }

        };
        getOnBackPressedDispatcher().addCallback(this,onBackPressedCallback);


    }




    private void allclear() {
        //secondary password  updating dialog
        binding.secondarypassword.setText("");

        //new secondary password  setting dialog
        binding.secondarypassword1.setText("");
        binding.secondarypassword2.setText("");

        //new password  updating dialog

        binding.changepassword1.setText("");
        binding.changepassword2.setText("");

        //new password  setting dialog
        binding.password1.setText("");
        binding.password2.setText("");

        //error msgs
        binding.wp.setVisibility(View.GONE);
        binding.wSp.setVisibility(View.GONE);
        binding.wpom.setVisibility(View.GONE);
        binding.wspom.setVisibility(View.GONE);
    }

    private void setPassword(String password, String secondaryPass) {
        Toast.makeText(this, "password set", Toast.LENGTH_SHORT).show();
        SharedPreferences sharedPreferences=MainActivity.this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedp= sharedPreferences.edit();
        sharedp.putString("password",password);
        sharedp.putString("Secondary_Password",secondaryPass);
        sharedp.putBoolean("isSet",true);
        sharedp.apply();
        //Toast.makeText(this, sharedPreferences.getString("password","default"), Toast.LENGTH_SHORT).show();
    }


}
