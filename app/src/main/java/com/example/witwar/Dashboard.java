package com.example.witwar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    final Context context=this;


    GoogleSignInClient mGoogleSignInClient;

    TextView nameTV;
    TextView emailTV;
    ImageView photoIV;


    BottomNavigationView bottomNavigationView;
    TextView textView;

    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer);

        nameTV = findViewById(R.id.name);
        emailTV = findViewById(R.id.email);
        photoIV = findViewById(R.id.photo);

        bottomNavigationView=(BottomNavigationView)findViewById(R.id.navigation);
        textView=(TextView)findViewById(R.id.text) ;

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        drawerLayout=(DrawerLayout)findViewById(R.id.drawerlayout);
        navigationView=(NavigationView)findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,myToolbar,R.string.open_drawer,R.string.close_drawer);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();


        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(Dashboard.this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            Uri personPhoto = acct.getPhotoUrl();

            nameTV.setText("Name: " + personName);
            emailTV.setText("Email: " + personEmail);
            Glide.with(this).load(personPhoto).into(photoIV);
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.home:
                       textView.setText("Home Activity");
                        break;
                    case R.id.edit:
                        textView.setText("Post your Activity");
                        break;
                    case R.id.profile:
                        textView.setText("Profile Activity");
                        break;
                }
                return true;
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);



    }





    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.home_id:
                break;

            case R.id.inbox_id:
                Toast.makeText(Dashboard.this, "5 star Rating has provided.", Toast.LENGTH_SHORT).show();
                break;

            case R.id.share_id:
                shareIt();
                break;

            case R.id.signout_id:
                SignOut();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }

    private void shareIt(){
        Intent shareIntent=new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT,"WitWar Battle");
        shareIntent.putExtra(Intent.EXTRA_TEXT,"Compitition of different Talents https://play.google.com/store/apps/details?id=com.edutech.notified");
        shareIntent.setType("text/plain");
        startActivity(shareIntent);
    }

    private void SignOut(){
        AlertDialog.Builder alertDialogBuilder =new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Are you sure You Want to SignOut from app..");
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog,int id){
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(Dashboard.this, MainActivity.class));
                        finish();
                        mGoogleSignInClient.signOut()
                                .addOnCompleteListener(Dashboard.this, new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(Dashboard.this, "Successfully signed out", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(Dashboard.this, MainActivity.class));
                                        finish();
                                    }
                                });
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog,int id){
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog=alertDialogBuilder.create();
        alertDialog.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_create_order:
                //Code to run when the Create Order item is clicked
                Intent intent = new Intent(this, competition.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
