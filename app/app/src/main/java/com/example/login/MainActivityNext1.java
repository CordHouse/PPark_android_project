package com.example.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivityNext1 extends AppCompatActivity {

    private TextView tv_id;
    Button btn_reserve, btn_color;

    @SuppressLint("SetTextI18n")
    protected void onCreate (Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_main_next1);

        tv_id = findViewById(R.id.tv_login_id);
        btn_reserve = findViewById(R.id.btn_reserve);
        btn_color = findViewById(R.id.btn_color);

        Intent intent = getIntent();
        String userID = intent.getStringExtra("userID");

        tv_id.setText(userID + "회원님! 반갑습니다!");

        btn_reserve.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onClick(View view) {
                /*
                switch (view.getId()) {
                    case R.id.btn_reserve:
                        btn_color.setBackgroundColor(Color.RED);
                        break;
                }
                 */
                Intent intent = new Intent(MainActivityNext1.this, sildingActivity.class);
                startActivity(intent);
            }
        });

        btn_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivityNext1.this, mapViewActivity.class);
                startActivity(intent);
            }
        });
    }
}
