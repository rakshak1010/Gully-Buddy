package com.example.vmac.WatBot;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.lang.*;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

public class Login extends AppCompatActivity {

    public String sname;
    public String friendName;
    public String snumber;
    public String friendNumber;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private EditText editTextName , editTestEmail,editTestpnumber , editTextPassword ,
            editTextFname , editTextFnumber ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //added by siddharth
        firebaseAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_login);

        editTextName = findViewById(R.id.input_name);
        editTestEmail = findViewById(R.id.input_email);
        editTestpnumber = findViewById(R.id.input_pnumber);
        editTextPassword = findViewById(R.id.input_password);
        editTextFname = findViewById(R.id.friends_name);
        editTextFnumber = findViewById(R.id.friends_pnumber);


        final Button button = (Button) findViewById(R.id.btn_signup);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                final Intent activityChangeIntent = new Intent(Login.this, Assistant_select.class);

                sname = editTextName.getText().toString().trim();
                String email = editTestEmail.getText().toString().trim();
                snumber = editTestpnumber.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                friendName = editTextFname.getText().toString().trim();
                friendNumber = editTextFnumber.getText().toString().trim();

                //added by Siddharth
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(Login.this,"Please enter email",Toast.LENGTH_LONG).show();
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    Toast.makeText(Login.this,"Please enter password",Toast.LENGTH_LONG).show();
                    return;
                }

                if(TextUtils.isEmpty(sname)){
                    Toast.makeText(Login.this,"Please enter Name",Toast.LENGTH_LONG).show();
                    return;
                }

                if(TextUtils.isEmpty(snumber)){
                    Toast.makeText(Login.this,"Please enter Number",Toast.LENGTH_LONG).show();
                    return;
                }

                if(TextUtils.isEmpty(friendName)){
                    Toast.makeText(Login.this,"Please enter Friend Name",Toast.LENGTH_LONG).show();
                    return;
                }

                if(TextUtils.isEmpty(friendNumber)){
                    Toast.makeText(Login.this,"Please enter Friend Number",Toast.LENGTH_LONG).show();
                    return;
                }
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //checking if success
                            if(task.isSuccessful()){
                                Toast.makeText(Login.this,"Successfully registered",Toast.LENGTH_LONG).show();
                                Log.d("rishi" , friendName +" " + friendNumber);
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                updateUI(user);
                            }else{
                                Toast.makeText(Login.this,"Registration Error",Toast.LENGTH_LONG).show();
                                updateUI(null);
                            }
                            progressDialog = new ProgressDialog(Login.this);
                            progressDialog.dismiss();
                        }
                    });
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        if(currentUser!=null){
            final Intent activityChangeIntent = new Intent(Login.this, Assistant_select.class);
            JSONObject obj = new JSONObject();
            obj.put("friendName", friendName);
            obj.put("friendNumber", friendNumber);

            try {
                FileWriter file = new FileWriter("/data/data/" + getApplicationContext().getPackageName() + "/" + "text1.txt");
                //file.write(obj.toJSONString());
                file.write(friendNumber + " " + friendName + " " + sname);
                file.flush();
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.d("rishi" , "obj "+ obj);

            //currentContext.startActivity(activityChangeIntent);

            Login.this.startActivity(activityChangeIntent);
        }else{
            Toast.makeText(Login.this,"Try Again",Toast.LENGTH_LONG).show();
        }
    }

    private void registerUser(){

        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(editTestEmail.getText().toString().trim(),
                editTextPassword.getText().toString().trim())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        //checking if success
                        if(task.isSuccessful()){
                            Log.d("sid","Login Success");
                        }else{
                            Log.d("sid","Login Failed");
                        }

                    }
                });

    }
}