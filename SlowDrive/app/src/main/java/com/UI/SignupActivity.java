package com.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.Logic.Manager.SignupManager;
import com.R;

public class SignupActivity extends AppCompatActivity {


    private Button signup_submit;
    private ImageButton back_submit;
    private TextView fn_signup, ln_signup, username_signup, emailadd_signup, password_signup, repassword_signup, message;
    private String[] invalidUsernameChar = {" ", ">", "<", "+", ",", ";", ":", };

    private final int USERNAME_MAX_LENGTH = 3;
    private final int USERNAME_MIN_LENGTH = 20;

    public static String requirements = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_page);


        back_submit = findViewById(R.id.back_submit);
        signup_submit = findViewById(R.id.signup_submit);
        fn_signup = findViewById(R.id.firstname_su);
        ln_signup = findViewById(R.id.lastname_su);
        username_signup = findViewById(R.id.username_su);
        emailadd_signup = findViewById(R.id.emailaddress_su);
        password_signup = findViewById(R.id.password_su);
        repassword_signup = findViewById(R.id.repassword_su);
        message = findViewById(R.id.message_su);



        //==============================
        // Event Listeners
        //==============================
        back_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToThisScreen(SplashScreenActivity.class);
            }
        });

        signup_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                requirements = "Please review your form:";

                String username = username_signup.getText().toString();
                String password = password_signup.getText().toString();
                String repassword = repassword_signup.getText().toString();
                String firstname = fn_signup.getText().toString();
                String lastname = ln_signup.getText().toString();
                String emailaddress = emailadd_signup.getText().toString();

                SignupManager newSignupManager = new SignupManager();


                if(newSignupManager.startSignup(username, password, firstname, lastname, emailaddress, repassword)){
                    requirements = "";
                    goToThisScreen(MainActivity.class);
                } else {
                    message.setText(requirements);
                }

            }
        });

    }


    private void goToThisScreen(Class c){
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }





}
