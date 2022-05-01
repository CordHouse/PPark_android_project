package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity_Floor extends AppCompatActivity {

    private Button btn_1F;                                                                                           //1층 버튼 변수 선언
    private Button btn_2F;
    private ImageButton btn_reflush; //2층 버튼 변수 선언
    private TextView tv_1F;                                                                                          //1층 주차 차량 tv 선언
    private TextView tv_2F;

    public String userID = "";
    String userPassword = "";
    int userReserve;
    int userReserve1_1;
    int userReserve2_1;
    int userReserve3_1;
    int userReserve4_2;
    int userReserve5_2;
    int userReserve6_2;
    String state1, state2, state3, state4, state5, state6;
    String state_RGB1, state_RGB2, state_RGB3, state_RGB4, state_RGB5, state_RGB6;
    String time = "";
    int penalty;
    String penaltyTime = "";
    String safeLock = "";
    String state ="";

    //테스트
    private RequestQueue queueCount, queueLogin;
    countRequest countRequest;
    LoginRequest loginRequest;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_floor);

        btn_1F = (Button) findViewById(R.id.btn_1F);                                                                 //1층 버튼 아이디 입력
        btn_2F = (Button) findViewById(R.id.btn_2F);                                                                  //TEST 버튼 아이디 입력
        tv_1F = (TextView) findViewById(R.id.tv_1F_current);                                                        //1층 주차장 개수 아이디 입력
        tv_2F = (TextView) findViewById(R.id.tv_2F_current);
        btn_reflush = (ImageButton) findViewById(R.id.btn_reflush);

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        userPassword = intent.getStringExtra("userPassword");

        renewal();
        reserveRenewal();

        // 1초마다 파일갱신
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(!Thread.interrupted())
                    try {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                renewal();
                                reserveRenewal();
                            }
                        });
                        Thread.sleep(1000);
                    }catch (InterruptedException e){
                        Toast.makeText(getApplicationContext(), "에러", Toast.LENGTH_SHORT).show();
                    }
            }
        }).start();

        btn_1F.setOnClickListener(new View.OnClickListener() {                                                                //1층 버튼 클릭했을때
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                renewal();
                reserveRenewal();
                if(tv_1F.getText().equals("3") && (userReserve == 1 || userReserve == 2 || userReserve == 3)){
                    Intent intent = new Intent(MainActivity_Floor.this, MainActivity1F_Parking.class);             //현재 엑티비티와 이동할 엑티비티(1층 주차장)
                    intent.putExtra("userID",userID);
                    intent.putExtra("state",state);
                    intent.putExtra("userReserve",userReserve);
                    intent.putExtra("userReserve1_1",userReserve1_1);
                    intent.putExtra("userReserve2_1",userReserve2_1);
                    intent.putExtra("userReserve3_1",userReserve3_1);
                    intent.putExtra("state1",state1);
                    intent.putExtra("state2",state2);
                    intent.putExtra("state3",state3);
                    intent.putExtra("state_RGB1",state_RGB1);
                    intent.putExtra("state_RGB2",state_RGB2);
                    intent.putExtra("state_RGB3",state_RGB3);
                    intent.putExtra("time", time);
                    intent.putExtra("userPassword", userPassword);
                    intent.putExtra("penalty", penalty);
                    intent.putExtra("penaltyTime", penaltyTime);
                    intent.putExtra("safeLock", safeLock);
                    startActivity(intent);
                }
                else if(tv_1F.getText().equals("3")) {                                                                                                 //1층 현재 차량이 3대일 때
                    Toast.makeText(getApplicationContext(), "남은 자리가 없습니다.", Toast.LENGTH_SHORT).show();                  //Toast 메시지 출력
                } else {                                                                                                        //아닌 경우(자리가 남았을 때)
                    Intent intent = new Intent(MainActivity_Floor.this, MainActivity1F_Parking.class);             //현재 엑티비티와 이동할 엑티비티(1층 주차장)
                    intent.putExtra("userID",userID);
                    intent.putExtra("state",state);
                    intent.putExtra("userReserve",userReserve);
                    intent.putExtra("userReserve1_1",userReserve1_1);
                    intent.putExtra("userReserve2_1",userReserve2_1);
                    intent.putExtra("userReserve3_1",userReserve3_1);
                    intent.putExtra("state1",state1);
                    intent.putExtra("state2",state2);
                    intent.putExtra("state3",state3);
                    intent.putExtra("state_RGB1",state_RGB1);
                    intent.putExtra("state_RGB2",state_RGB2);
                    intent.putExtra("state_RGB3",state_RGB3);
                    intent.putExtra("time", time);
                    intent.putExtra("userPassword", userPassword);
                    intent.putExtra("penalty", penalty);
                    intent.putExtra("penaltyTime", penaltyTime);
                    intent.putExtra("safeLock", safeLock);
                    startActivity(intent);                                                                                       //엑티비티 이동
                }
            }
        });

        btn_2F.setOnClickListener(new View.OnClickListener() {                                                                    //2층 버튼 클릭했을때
            @Override
            public void onClick(View view) {
                renewal();
                reserveRenewal();
                if(tv_2F.getText().equals("3") && (userReserve == 4 || userReserve == 5 || userReserve == 6)){
                    Intent intent = new Intent(MainActivity_Floor.this, MainActivity2F_Parking.class);               //현재 엑티비티와 이동할 엑티비티(2층 주차장)
                    intent.putExtra("userID",userID);
                    intent.putExtra("state",state);
                    intent.putExtra("userReserve",userReserve);
                    intent.putExtra("userReserve4_2",userReserve4_2);
                    intent.putExtra("userReserve5_2",userReserve5_2);
                    intent.putExtra("userReserve6_2",userReserve6_2);
                    intent.putExtra("state4",state4);
                    intent.putExtra("state5",state5);
                    intent.putExtra("state6",state6);
                    intent.putExtra("state_RGB4",state_RGB4);
                    intent.putExtra("state_RGB5",state_RGB5);
                    intent.putExtra("state_RGB6",state_RGB6);
                    intent.putExtra("time", time);
                    intent.putExtra("userPassword", userPassword);
                    intent.putExtra("penalty", penalty);
                    intent.putExtra("penaltyTime", penaltyTime);
                    intent.putExtra("safeLock", safeLock);
                    startActivity(intent);
                }
                else if (tv_2F.getText().equals("3")) {                                                                                                //2층 현재 차량이 3대일 때
                    Toast.makeText(getApplicationContext(), "남은 자리가 없습니다.", Toast.LENGTH_SHORT).show();                  //Toast 메시지 출력
                } else {                                                                                                            //아닌 경우(자리가 남았을 때)
                    Intent intent = new Intent(MainActivity_Floor.this, MainActivity2F_Parking.class);               //현재 엑티비티와 이동할 엑티비티(2층 주차장)
                    intent.putExtra("userID",userID);
                    intent.putExtra("state",state);
                    intent.putExtra("userReserve",userReserve);
                    intent.putExtra("userReserve4_2",userReserve4_2);
                    intent.putExtra("userReserve5_2",userReserve5_2);
                    intent.putExtra("userReserve6_2",userReserve6_2);
                    intent.putExtra("state4",state4);
                    intent.putExtra("state5",state5);
                    intent.putExtra("state6",state6);
                    intent.putExtra("state_RGB4",state_RGB4);
                    intent.putExtra("state_RGB5",state_RGB5);
                    intent.putExtra("state_RGB6",state_RGB6);
                    intent.putExtra("time", time);
                    intent.putExtra("userPassword", userPassword);
                    intent.putExtra("penalty", penalty);
                    intent.putExtra("penaltyTime", penaltyTime);
                    intent.putExtra("safeLock", safeLock);
                    startActivity(intent);                                                                                      //엑티비티 이동
                }
            }
        });

        btn_reflush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                renewal();
                reserveRenewal();
                Toast.makeText(getApplicationContext(), "새로고침", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void renewal(){
        Intent intent = getIntent();
        String userID_Re = intent.getStringExtra("userID");

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        String count_Re = jsonObject.getString("count");
                        String count2_Re = jsonObject.getString("count2");
                        int userReserve1_1_Re = jsonObject.getInt("userReserve1_1");
                        int userReserve2_1_Re = jsonObject.getInt("userReserve2_1");
                        int userReserve3_1_Re = jsonObject.getInt("userReserve3_1");
                        int userReserve4_2_Re = jsonObject.getInt("userReserve4_2");
                        int userReserve5_2_Re = jsonObject.getInt("userReserve5_2");
                        int userReserve6_2_Re = jsonObject.getInt("userReserve6_2");
                        String state1_Re = jsonObject.getString("state1");
                        String state2_Re = jsonObject.getString("state2");
                        String state3_Re = jsonObject.getString("state3");
                        String state4_Re = jsonObject.getString("state4");
                        String state5_Re = jsonObject.getString("state5");
                        String state6_Re = jsonObject.getString("state6");
                        String state_RGB1_Re = jsonObject.getString("state_RGB1");
                        String state_RGB2_Re = jsonObject.getString("state_RGB2");
                        String state_RGB3_Re = jsonObject.getString("state_RGB3");
                        String state_RGB4_Re = jsonObject.getString("state_RGB4");
                        String state_RGB5_Re = jsonObject.getString("state_RGB5");
                        String state_RGB6_Re = jsonObject.getString("state_RGB6");
                        userID = userID_Re;
                        tv_1F.setText(count_Re);
                        tv_2F.setText(count2_Re);
                        userReserve1_1 = userReserve1_1_Re;
                        userReserve2_1 = userReserve2_1_Re;
                        userReserve3_1 = userReserve3_1_Re;
                        userReserve4_2 = userReserve4_2_Re;
                        userReserve5_2 = userReserve5_2_Re;
                        userReserve6_2 = userReserve6_2_Re;
                        state1 = state1_Re;
                        state2 = state2_Re;
                        state3 = state3_Re;
                        state4 = state4_Re;
                        state5 = state5_Re;
                        state6 = state6_Re;
                        state_RGB1 = state_RGB1_Re;
                        state_RGB2 = state_RGB2_Re;
                        state_RGB3 = state_RGB3_Re;
                        state_RGB4 = state_RGB4_Re;
                        state_RGB5 = state_RGB5_Re;
                        state_RGB6 = state_RGB6_Re;
                    } else {
                        Toast.makeText(getApplicationContext(), "에러", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "에러", Toast.LENGTH_SHORT).show();
                }
            }
        };
        countRequest = new countRequest(userID, responseListener);
        if(queueCount == null) {
            queueCount = Volley.newRequestQueue(MainActivity_Floor.this);
        }
        queueCount.add(countRequest);
    }

    private void reserveRenewal(){
        Intent intent = getIntent();
        String userID_Re = intent.getStringExtra("userID");

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        String state_Re = jsonObject.getString("state");
                        int userReserve_Re = jsonObject.getInt("userReserve");
                        String time_Re = jsonObject.getString("time");
                        int penalty_Re = jsonObject.getInt("penalty");
                        String penaltyTime_Re = jsonObject.getString("penaltyTime");
                        String safeLock_Re = jsonObject.getString("safeLock");
                        userID = userID_Re;
                        state = state_Re;
                        userReserve = userReserve_Re;
                        time = time_Re;
                        penalty = penalty_Re;
                        penaltyTime = penaltyTime_Re;
                        safeLock = safeLock_Re;
                    } else {
                        Toast.makeText(getApplicationContext(), "에러", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "에러", Toast.LENGTH_SHORT).show();
                }
            }
        };
        loginRequest = new LoginRequest(userID, userPassword, responseListener);
        if(queueLogin == null) {
            queueLogin = Volley.newRequestQueue(MainActivity_Floor.this);
        }
        queueLogin.add(loginRequest);
    }

}

