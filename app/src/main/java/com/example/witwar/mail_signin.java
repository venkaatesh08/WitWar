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

public class mail_signin extends AppCompatActivity {

    TextView newsignup;
    Button login;
    EditText mail;
    EditText password;


    ProgressDialog PD;
    FirebaseAuth mFirebaseAuth;
    FirebaseAuth.AuthStateListener mAuthStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_signin);

        newsignup=(TextView)findViewById(R.id.signup_btn);
        mail=(EditText)findViewById(R.id.email_id);
        password=(EditText)findViewById(R.id.pass_id);
        login=(Button)findViewById(R.id.btn_signin);


        mFirebaseAuth=FirebaseAuth.getInstance();
        mAuthStateListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                     if(firebaseAuth.getCurrentUser()!=null){
                         startActivity(new Intent(mail_signin.this,Dashboard.class));
                         finish();
                     }
            }
        };

        PD = new ProgressDialog(this);
        PD.setMessage("Loading...");
        PD.setCancelable(true);
        PD.setCanceledOnTouchOutside(false);



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=mail.getText().toString();
                String pass=password.getText().toString();

                SignInMethod(email,pass);

            }
        });




        newsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signup=new Intent(mail_signin.this,MailSignUp.class);
                startActivity(signup);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
       mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    public void SignInMethod(String email, String pass) {

        if (!isValidEmail(email)) {
            mail.setError("Invalid Email");
        } else if (!isValidPassword(pass)) {
            password.setError("Invalid Password");
        } else {
            PD.show();
            mFirebaseAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()){
                        Toast.makeText(mail_signin.this,"Signin Failed",Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(mail_signin.this,"Signin Sucess",Toast.LENGTH_LONG).show();
                    }
                    PD.dismiss();
                }
            });

        }


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
