package com.example.yuanjiayi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yuanjiayi.api.Log;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditor, passwordEditor;
    private Button login, logout;
    public Activity self;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        self = this;

        usernameEditor = findViewById(R.id.et_loginactivity_username);
        passwordEditor = findViewById(R.id.et_loginactivity_password);
        login = findViewById(R.id.bt_loginactivity_login);
        logout = findViewById(R.id.bt_loginactivity_logout);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditor.getText().toString();
                String password = passwordEditor.getText().toString();
                if(username.equals("")){
                    Toast.makeText(self, "请输入用户名", Toast.LENGTH_SHORT).show();
                    return;
                }
                boolean succeed = false;
                switch (Log.login(username, password, getApplicationContext())){
                    case 0:
                        succeed = true;
                        break;
                    case 1:
                        Log.signin(username, password, getApplicationContext());
                        succeed = true;
                        break;
                    case 2:
                        Toast.makeText(LoginActivity.this, "请输入正确密码", Toast.LENGTH_SHORT).show();
                        break;
                }
                if(succeed){
                    Intent intent = new Intent();
                    intent.putExtra("username", username);
                    setResult(3, intent);
                    finish();
                }
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("username", "guest");
                setResult(3, intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("username", "");
        setResult(3, intent);
        super.onBackPressed();
    }
}
