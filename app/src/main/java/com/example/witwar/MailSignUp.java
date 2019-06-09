package com.example.witwar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MailSignUp extends AppCompatActivity {

    TextView login;
    Button signup;
    EditText usernam;
    EditText mail;
    EditText passwd;

    private FirebaseAuth mAuth;
    ProgressDialog PD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_sign_up);

        login=(TextView)findViewById(R.id.signin);
        usernam=(EditText)findViewById(R.id.person_id);
        mail=(EditText)findViewById(R.id.email_id);
        passwd=(EditText)findViewById(R.id.pass_id);
        signup=(Button)findViewById(R.id.btn_signup);


        mAuth = FirebaseAuth.getInstance();
        PD = new ProgressDialog(this);
        PD.setMessage("Processing...");
        PD.setCancelable(true);
        PD.setCanceledOnTouchOutside(false);

        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(MailSignUp.this, Dashboard.class));
            finish();
        }


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String usern=usernam.getText().toString();
                String email=mail.getText().toString();
                String password=passwd.getText().toString();

                if (!isValidName(usern)) {
                    usernam.setError("Invalid username");
                }

                else  if (!isValidEmail(email)) {
                    mail.setError("Invalid Email");
                }


                else if (!isValidPassword(password)) {
                    passwd.setError("Invalid Password");
                }
                else {
                    try{
                        PD.show();
                        mAuth.createUserWithEmailAndPassword(email,password)
                                .addOnCompleteListener(MailSignUp.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful()){
                                            Intent intent = new Intent(MailSignUp.this, Dashboard.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                        else
                                        {
                                            Toast.makeText(
                                                    MailSignUp.this,
                                                    "Authentication Failed",
                                                    Toast.LENGTH_LONG).show();
                                        }
                                        PD.dismiss();
                                    }
                                });

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent log=new Intent(MailSignUp.this,mail_signin.class);
                startActivity(log);
            }
        });


    }



    private boolean isValidName(String user) {
        if (user != null && user.length() > 0) {
            return true;
        }
        return false;
    }


    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isValidPassword(String pass) {
        if (pass != null && pass.length() > 6) {
            return true;
        }
        return false;
    }



}
