package com.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

//import com.Logic.Implementation.Login;
import com.Logic.Manager.LoginManager;
import com.R;


public class LoginActivity extends AppCompatActivity {

    private Button login_submit;
    private ImageButton back_login;
    private TextView username_login, password_login, message_login;

    private String message = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        login_submit = findViewById(R.id.login_submit);
        back_login = findViewById(R.id.back_login);
        username_login = findViewById(R.id.username_li);
        password_login = findViewById(R.id.password_li);
        message_login = findViewById(R.id.message_login);

        message_login.setText(message);


        //==============================
        // Event Listeners
        //==============================

        login_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = username_login.getText().toString();
                String password = password_login.getText().toString();


                LoginManager newLogin = new LoginManager();

                if(newLogin.startLogin(username, password)){
                    message = "";
                    message_login.setText(message);
                    message_login.setText(message);
                    goToThisScreen(MainActivity.class);
                } else {
                    message = "Incorrect username or password.";
                    message_login.setText(message);
                }
            }
        });

        back_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToThisScreen(SplashScreenActivity.class);
            }
        });

    }

    private void goToThisScreen(Class c){
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }



}
