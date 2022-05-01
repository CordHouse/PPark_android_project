package com.example.login;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity1F_Parking extends AppCompatActivity {

    private Button btn_1_1F;                                                                             //1번 주자장 버튼선언
    private Button btn_2_1F;                                                                             //2번 주자장 버튼선언
    private Button btn_3_1F;                                                                             //3번 주자장 버튼선언
    private Button btn_safeLock;
    TextView tv_hour, tv_min, tv_sec, tv_split1, tv_split2;
    int hour, minute, second;
    TimerTask timerTask;
    Timer timer = new Timer();;
    String btn1 = "Green",btn2 = "Green",btn3 = "Green";
    int userReserve1 = 0,userReserve2 = 0,userReserve3 = 0;
    int userReserve;
    int userReserve1_1 = 0, userReserve2_1 = 0, userReserve3_1 = 0;
    String state1, state2, state3; // userID 앱 미사용자
    String state_RGB1, state_RGB2, state_RGB3; // 다른계정 state
    int penalty = 0;
    int dd; // day * 24
    String userID ="";
    String userPassword = "";
    String penaltyTime="";
    String time="";
    String btn = "";
    int safeLockState = 0;
    String safeLock = "OFF";
    String state; // 현재 주차 상태 ( 한 계정당 상태임 )

    //테스트
    private RequestQueue queueCount, queueLogin;
    LoginRequest loginRequest;
    countRequest countRequest;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity1_fparking);

        btn_1_1F = (Button) findViewById(R.id.btn_1_1F);                                                    //1번 주차장 id입력
        btn_2_1F = (Button) findViewById(R.id.btn_2_1F);                                                    //2번 주차장 id입력
        btn_3_1F = (Button) findViewById(R.id.btn_3_1F);                                                    //3번 주차장 id입력
        btn_safeLock = (Button) findViewById(R.id.btn_safeLock);
        tv_hour = findViewById(R.id.tv_hour);
        tv_min = findViewById(R.id.tv_min);
        tv_sec = findViewById(R.id.tv_sec);
        tv_split1 = findViewById(R.id.tv_split1);
        tv_split2 = findViewById(R.id.tv_split2);

        Intent intent = getIntent(); // 1
        userID = intent.getStringExtra("userID"); // 2
        userPassword = intent.getStringExtra("userPassword");
        userReserve = intent.getIntExtra("userReserve", 0);
        userReserve1_1 = intent.getIntExtra("userReserve1_1", 0);
        userReserve2_1 = intent.getIntExtra("userReserve2_1", 0);
        userReserve3_1 = intent.getIntExtra("userReserve3_1", 0);
        state = intent.getStringExtra("state");
        state1 = intent.getStringExtra("state1");
        state2 = intent.getStringExtra("state2");
        state3 = intent.getStringExtra("state3");
        state_RGB1 = intent.getStringExtra("state_RGB1");
        state_RGB2 = intent.getStringExtra("state_RGB2");
        state_RGB3 = intent.getStringExtra("state_RGB3");
        btn = intent.getStringExtra("state");
        time = intent.getStringExtra("time");// 저장된 시간
        penalty = intent.getIntExtra("penalty", 0);
        penaltyTime = intent.getStringExtra("penaltyTime");// 저장된 시간
        safeLock = intent.getStringExtra("safeLock");
        // 예약한 상황이라면 버튼 상태 받아오기

        renewal(userReserve, penalty);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(!Thread.interrupted())
                    try {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateLogin();
                                updateCount();
                                if (userReserve != 1 && userReserve1_1 == 1) {
                                    btn_1_1F.setText("예약된 자리입니다.");
                                    btn_1_1F.setBackgroundColor(Color.YELLOW);
                                    btn_1_1F.setTextColor(Color.BLACK);
                                    btn1 = "Yellow";
                                }
                                else if (userReserve != 1) {
                                    btn_1_1F.setText("#1");
                                    btn_1_1F.setBackgroundColor(Color.GREEN);
                                    btn_1_1F.setTextColor(Color.WHITE);
                                    btn1 = "Green";
                                }
                                if (userReserve != 2 && userReserve2_1 == 2) {
                                    btn_2_1F.setText("예약된 자리입니다.");
                                    btn_2_1F.setBackgroundColor(Color.YELLOW);
                                    btn_2_1F.setTextColor(Color.BLACK);
                                    btn2 = "Yellow";
                                }
                                else if (userReserve != 2) {
                                    btn_2_1F.setText("#2");
                                    btn_2_1F.setBackgroundColor(Color.GREEN);
                                    btn_2_1F.setTextColor(Color.WHITE);
                                    btn2 = "Green";
                                }
                                if (userReserve != 3 && userReserve3_1 == 3) {
                                    btn_3_1F.setText("예약된 자리입니다.");
                                    btn_3_1F.setBackgroundColor(Color.YELLOW);
                                    btn_3_1F.setTextColor(Color.BLACK);
                                    btn3 = "Yellow";
                                }
                                else if (userReserve != 3) {
                                    btn_3_1F.setText("#3");
                                    btn_3_1F.setBackgroundColor(Color.GREEN);
                                    btn_3_1F.setTextColor(Color.WHITE);
                                    btn3 = "Green";
                                }
                                if (userReserve == 1 && userReserve1_1 == 1) {
                                    btn_1_1F.setText(userID + "님이 예약하신\n 자리입니다.");
                                    btn_1_1F.setBackgroundColor(Color.YELLOW);
                                    btn_1_1F.setTextColor(Color.BLACK);
                                    btn_safeLock.setText("#" + userReserve + " SAFELOCK " + safeLock);
                                    if (safeLock.equals("ON")) {
                                        btn_safeLock.setBackgroundColor(Color.parseColor("#FF9800"));
                                        btn1 = "Yellow";
                                    } else if(safeLock.equals("OFF")) {
                                        btn_safeLock.setBackgroundColor(Color.BLUE);
                                        btn1 = "Red";
                                    }

                                } else if (userReserve == 2 && userReserve2_1 == 2) {
                                    btn_2_1F.setText(userID + "님이 예약하신\n 자리입니다.");
                                    btn_2_1F.setBackgroundColor(Color.YELLOW);
                                    btn_2_1F.setTextColor(Color.BLACK);
                                    btn_safeLock.setText("#" + userReserve + " SAFELOCK " + safeLock);
                                    if (safeLock.equals("ON")) {
                                        btn_safeLock.setBackgroundColor(Color.parseColor("#FF9800"));
                                        btn2 = "Yellow";
                                    } else if(safeLock.equals("OFF")) {
                                        btn_safeLock.setBackgroundColor(Color.BLUE);
                                        btn2 = "Red";
                                    }
                                } else if (userReserve == 3 && userReserve3_1 == 3) {
                                    btn_3_1F.setText(userID + "님이 예약하신\n 자리입니다.");
                                    btn_3_1F.setBackgroundColor(Color.YELLOW);
                                    btn_3_1F.setTextColor(Color.BLACK);
                                    btn_safeLock.setText("#" + userReserve + " SAFELOCK " + safeLock);
                                    if (safeLock.equals("ON")) {
                                        btn_safeLock.setBackgroundColor(Color.parseColor("#FF9800"));
                                        btn3 = "Yellow";
                                    } else if(safeLock.equals("OFF")){
                                        btn_safeLock.setBackgroundColor(Color.BLUE);
                                        btn3 = "Red";
                                    }
                                }
                                if(time.equals("0")) {
                                    if (userReserve == 1 && state.equals("Red") && safeLock.equals("OFF")) { // 예약한 상태 + 세이프락 해제 ( 주차하기 전 )
                                        btn_1_1F.setBackgroundColor(Color.RED);
                                        btn_1_1F.setTextColor(Color.WHITE);
                                        btn_1_1F.setText(userID + "님 주차");
                                        tv_hour.setVisibility(View.INVISIBLE);
                                        tv_min.setVisibility(View.INVISIBLE);
                                        tv_sec.setVisibility(View.INVISIBLE);
                                        tv_split1.setVisibility(View.INVISIBLE);
                                        tv_split2.setVisibility(View.INVISIBLE);
                                        btn_safeLock.setVisibility(View.INVISIBLE);
                                    } else if (userReserve == 2 && state.equals("Red") && safeLock.equals("OFF")) { // 예약한 상태 + 세이프락 해제 ( 주차하기 전 )
                                        btn_2_1F.setBackgroundColor(Color.RED);
                                        btn_2_1F.setTextColor(Color.WHITE);
                                        btn_2_1F.setText(userID + "님 주차");
                                        tv_hour.setVisibility(View.INVISIBLE);
                                        tv_min.setVisibility(View.INVISIBLE);
                                        tv_sec.setVisibility(View.INVISIBLE);
                                        tv_split1.setVisibility(View.INVISIBLE);
                                        tv_split2.setVisibility(View.INVISIBLE);
                                        btn_safeLock.setVisibility(View.INVISIBLE);
                                    } else if (userReserve == 3 && state.equals("Red") && safeLock.equals("OFF")) { // 예약한 상태 + 세이프락 해제 ( 주차하기 전 )
                                        btn_3_1F.setBackgroundColor(Color.RED);
                                        btn_3_1F.setTextColor(Color.WHITE);
                                        btn_3_1F.setText(userID + "님 주차");
                                        tv_hour.setVisibility(View.INVISIBLE);
                                        tv_min.setVisibility(View.INVISIBLE);
                                        tv_sec.setVisibility(View.INVISIBLE);
                                        tv_split1.setVisibility(View.INVISIBLE);
                                        tv_split2.setVisibility(View.INVISIBLE);
                                        btn_safeLock.setVisibility(View.INVISIBLE);
                                    }
                                }
                                if (state1.equals("1") && userReserve1_1 == 1){ // 앱 미사용자 주차
                                    btn_1_1F.setBackgroundColor(Color.RED);
                                    btn_1_1F.setTextColor(Color.WHITE);
                                    btn_1_1F.setText("#"+userReserve1_1+"\r\n앱 미사용자 주차");
                                }
                                if (state2.equals("2") && userReserve2_1 == 2){ // 앱 미사용자 주차
                                    btn_2_1F.setBackgroundColor(Color.RED);
                                    btn_2_1F.setTextColor(Color.WHITE);
                                    btn_2_1F.setText("#"+userReserve2_1+"\r\n앱 미사용자 주차");
                                }
                                if (state3.equals("3") && userReserve3_1 == 3){ // 앱 미사용자 주차
                                    btn_3_1F.setBackgroundColor(Color.RED);
                                    btn_3_1F.setTextColor(Color.WHITE);
                                    btn_3_1F.setText("#"+userReserve3_1+"\r\n앱 미사용자 주차");
                                }
                                if (userReserve != 1 && userReserve1_1 == 1 && state_RGB1.equals("Red") && !state1.equals("1")) {
                                    btn_1_1F.setText("주차된 자리입니다.");
                                    btn_1_1F.setBackgroundColor(Color.RED);
                                    btn_1_1F.setTextColor(Color.WHITE);
                                    btn1 = "Red";
                                }
                                if (userReserve != 2 && userReserve2_1 == 2 && state_RGB2.equals("Red") && !state2.equals("2")) {
                                    btn_2_1F.setText("주차된 자리입니다.");
                                    btn_2_1F.setBackgroundColor(Color.RED);
                                    btn_2_1F.setTextColor(Color.WHITE);
                                    btn2 = "Red";
                                }
                                if (userReserve != 3 && userReserve3_1 == 3 && state_RGB3.equals("Red") && !state3.equals("3")) {
                                    btn_3_1F.setText("주차된 자리입니다.");
                                    btn_3_1F.setBackgroundColor(Color.RED);
                                    btn_3_1F.setTextColor(Color.WHITE);
                                    btn3 = "Red";
                                }
                            }

                        });
                        Thread.sleep(1000);
                    }catch(InterruptedException e){
                        Toast.makeText(getApplicationContext(), "에러", Toast.LENGTH_SHORT).show();
                    }
            }
        }).start();

        if (state1.equals("1") && userReserve1_1 == 1){ // 앱 미사용자 주차
            btn_1_1F.setBackgroundColor(Color.RED);
            btn_1_1F.setTextColor(Color.WHITE);
            btn_1_1F.setText("#"+userReserve1_1+"\r\n앱 미사용자 주차");
        }
        if (userReserve != 1 && userReserve1_1 == 1 && state_RGB1.equals("Red") && !state1.equals("1")) {
            btn_1_1F.setText("주차된 자리입니다.");
            btn_1_1F.setBackgroundColor(Color.RED);
            btn_1_1F.setTextColor(Color.WHITE);
            btn1 = "Red";
        }
        else if (userReserve == 1 && state.equals("Red") && safeLock.equals("OFF") && time.equals("0")) { // 예약한 상태 + 세이프락 해제 ( 주차하기 전 )
            btn_1_1F.setBackgroundColor(Color.RED);
            btn_1_1F.setTextColor(Color.WHITE);
            btn_1_1F.setText(userID + "님 주차");
        }
        else if (userReserve1_1 == 1) {
            btn_1_1F.setText("예약된 자리입니다.");
            btn_1_1F.setBackgroundColor(Color.YELLOW);
            btn_1_1F.setTextColor(Color.BLACK);
            btn1 = "Yellow";
        }
        else if(userReserve1_1 == 0){
            btn_1_1F.setText("#1");
            btn_1_1F.setBackgroundColor(Color.GREEN);
            btn_1_1F.setTextColor(Color.WHITE);
            btn1 = "Green";
        }
        if (state2.equals("2") && userReserve2_1 == 2){ // 앱 미사용자 주차
            btn_2_1F.setBackgroundColor(Color.RED);
            btn_2_1F.setTextColor(Color.WHITE);
            btn_2_1F.setText("#"+userReserve2_1+"\r\n앱 미사용자 주차");
        }
        if (userReserve != 2 && userReserve2_1 == 2 && state_RGB2.equals("Red") && !state2.equals("2")) {
            btn_2_1F.setText("주차된 자리입니다.");
            btn_2_1F.setBackgroundColor(Color.RED);
            btn_2_1F.setTextColor(Color.WHITE);
            btn2 = "Red";
        }
        else if (userReserve == 2 && state.equals("Red") && safeLock.equals("OFF") && time.equals("0")) { // 예약한 상태 + 세이프락 해제 ( 주차하기 전 )
            btn_2_1F.setBackgroundColor(Color.RED);
            btn_2_1F.setTextColor(Color.WHITE);
            btn_2_1F.setText(userID + "님 주차");
        }
        else if (userReserve2_1 == 2) {
            btn_2_1F.setText("예약된 자리입니다.");
            btn_2_1F.setBackgroundColor(Color.YELLOW);
            btn_2_1F.setTextColor(Color.BLACK);
            btn2 = "Yellow";
        }
        else if(userReserve2_1 == 0){
            btn_2_1F.setText("#2");
            btn_2_1F.setBackgroundColor(Color.GREEN);
            btn_2_1F.setTextColor(Color.WHITE);
            btn2 = "Green";
        }
        if (state3.equals("3") && userReserve3_1 == 3){ // 앱 미사용자 주차
            btn_3_1F.setBackgroundColor(Color.RED);
            btn_3_1F.setTextColor(Color.WHITE);
            btn_3_1F.setText("#"+userReserve3_1+"\r\n앱 미사용자 주차");
        }
        if (userReserve != 3 && userReserve3_1 == 3 && state_RGB3.equals("Red") && !state3.equals("3")) {
            btn_3_1F.setText("주차된 자리입니다.");
            btn_3_1F.setBackgroundColor(Color.RED);
            btn_3_1F.setTextColor(Color.WHITE);
            btn3 = "Red";
        }
        else if (userReserve == 3 && state.equals("Red") && safeLock.equals("OFF") && time.equals("0")) { // 예약한 상태 + 세이프락 해제 ( 주차하기 전 )
            btn_3_1F.setBackgroundColor(Color.RED);
            btn_3_1F.setTextColor(Color.WHITE);
            btn_3_1F.setText(userID + "님 주차");
        }
        else if (userReserve3_1 == 3) {
            btn_3_1F.setText("예약된 자리입니다.");
            btn_3_1F.setBackgroundColor(Color.YELLOW);
            btn_3_1F.setTextColor(Color.BLACK);
            btn3 = "Yellow";
        }
        else if(userReserve3_1 == 0){
            btn_3_1F.setText("#3");
            btn_3_1F.setBackgroundColor(Color.GREEN);
            btn_3_1F.setTextColor(Color.WHITE);
            btn3 = "Green";
        }

        if (userReserve == 1 && !time.equals("0")) {
            counterTimer(time);
        } else if (userReserve == 2 && !time.equals("0")) {
            counterTimer(time);
        } else if (userReserve == 3 && !time.equals("0")) {
            counterTimer(time);
        }

        if (penalty > 0 && !penaltyTime.equals("0")) {
            counterTimer(penaltyTime);
        } else if (hour < 0 || minute < 0 || second < 0) {
            overTime();
        }

        btn_1_1F.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (penaltyTime.equals("0")) {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                boolean success = jsonObject.getBoolean("success");
                                if (success) {
                                } else {
                                    Toast.makeText(getApplicationContext(), "예약실패.", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    };
                    if (btn1.equals("Green")) {
                        if (userReserve == 0) {                                                                               //1번 자리 상태 확인
                            AlertDialog.Builder Check = new AlertDialog.Builder(MainActivity1F_Parking.this);   //다이얼로그 띄우기
                            Check.setTitle("자리 예약");                                                                   //다이얼로그 제목
                            Check.setMessage("#1 자리를 선택하시겠습니까?");                                               //다이얼로그 내용
                            Check.setPositiveButton("예", new DialogInterface.OnClickListener() {                    //다이얼로그 '예' 버튼 눌렀을 때
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {
                                    btn_1_1F.setBackgroundColor(Color.YELLOW);
                                    btn_1_1F.setTextColor(Color.BLACK);//1번 버튼 색 빨강으로 변경
                                    btn_1_1F.setText(userID + "님이 예약하신\n 자리입니다.");
                                    dialogInterface.dismiss();                                                          //다이얼로그 창 사라짐
                                    userReserve1 = 1;                                                                   //자리 상태 변경(빈자리 -> 예약)
                                    userReserve1_1 = 1;
                                    btn1 = "Yellow";
                                    String setPenaltyTime = "0";
                                    safeLock = "ON";
                                    btn_safeLock.setText("#"+ userReserve1 + " SAFELOCK ON");
                                    btn_safeLock.setBackgroundColor(Color.parseColor("#FF9800"));
                                    long nowLater = System.currentTimeMillis() + 600000;
                                    long now = System.currentTimeMillis();
                                    Date date = new Date(nowLater);
                                    @SuppressLint("SimpleDateFormat")
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
                                    String getTime = dateFormat.format(date);
                                    Date dateNow = new Date(nowLater - now - 32400000);
                                    @SuppressLint("SimpleDateFormat")
                                    SimpleDateFormat dateHour = new SimpleDateFormat("HH", Locale.KOREA);
                                    tv_hour.setText(dateHour.format(dateNow));
                                    @SuppressLint("SimpleDateFormat")
                                    SimpleDateFormat dateMin = new SimpleDateFormat("mm", Locale.KOREA);
                                    tv_min.setText(dateMin.format(dateNow));
                                    @SuppressLint("SimpleDateFormat")
                                    SimpleDateFormat dateSec = new SimpleDateFormat("ss", Locale.KOREA);
                                    tv_sec.setText(dateSec.format(dateNow));
                                    time = getTime;
                                    ReserveRequest reserveRequest = new ReserveRequest(userID, userReserve1, btn1, getTime, penalty, setPenaltyTime,safeLock, responseListener);
                                    RequestQueue queue = Volley.newRequestQueue(MainActivity1F_Parking.this);
                                    queue.add(reserveRequest);
                                    tv_hour.setVisibility(View.VISIBLE);
                                    tv_min.setVisibility(View.VISIBLE);
                                    tv_sec.setVisibility(View.VISIBLE);
                                    tv_split1.setVisibility(View.VISIBLE);
                                    tv_split2.setVisibility(View.VISIBLE);
                                    btn_safeLock.setVisibility(View.VISIBLE);
                                    startTimerTask();
                                    renewal(userReserve1, penalty);
                                    Toast.makeText(getApplicationContext(), "예약완료.", Toast.LENGTH_SHORT).show();
                                }
                            });
                            Check.setNegativeButton("취소", new DialogInterface.OnClickListener() {                   //다이얼로그 '취소' 버튼 눌렀을 때
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {
                                    dialogInterface.dismiss();                                                          //다이얼로그 창 사라짐
                                }
                            });
                            Check.show();
                        } else
                            Toast.makeText(getApplicationContext(), "이미 예약하신 자리가 있습니다.", Toast.LENGTH_SHORT).show();
                    } else if (btn1.equals("Red") && time.equals("0")) {
                        Toast.makeText(getApplicationContext(), "이미 주차된 자리입니다.", Toast.LENGTH_SHORT).show();
                    } else if (userReserve == 1 || userReserve1 == 1) {
                        AlertDialog.Builder Check = new AlertDialog.Builder(MainActivity1F_Parking.this);   //다이얼로그 띄우기
                        Check.setTitle("예약 취소");                                                                   //다이얼로그 제목
                        Check.setMessage("예약을 취소 하시겠습니까?");                                               //다이얼로그 내용
                        Check.setPositiveButton("예", new DialogInterface.OnClickListener() {                    //다이얼로그 '예' 버튼 눌렀을 때
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                btn_1_1F.setBackgroundColor(Color.GREEN);
                                btn_1_1F.setTextColor(Color.WHITE);//1번 버튼 색 빨강으로 변경
                                btn_1_1F.setText("#1"); // 3
                                dialogInterface.dismiss();
                                Toast.makeText(getApplicationContext(), "예약을 취소하셨습니다.", Toast.LENGTH_SHORT).show();
                                userReserve1 = 0;
                                userReserve1_1 = 0; // 예약 취소시 두개가 동시에 작동해야 두번 동작하지 않음
                                btn1 = "Green";
                                String getTime = "0";
                                String setPenaltyTime = "0";
                                safeLock = "OFF";
                                ReserveRequest reserveRequest = new ReserveRequest(userID, userReserve1, btn1, getTime, penalty, setPenaltyTime,safeLock, responseListener);
                                RequestQueue queue = Volley.newRequestQueue(MainActivity1F_Parking.this);
                                queue.add(reserveRequest);
                                tv_hour.setVisibility(View.INVISIBLE);
                                tv_min.setVisibility(View.INVISIBLE);
                                tv_sec.setVisibility(View.INVISIBLE);
                                tv_split1.setVisibility(View.INVISIBLE);
                                tv_split2.setVisibility(View.INVISIBLE);
                                btn_safeLock.setVisibility(View.INVISIBLE);
                                timer.cancel();
                                renewal(userReserve1, penalty);
                            }
                        });
                        Check.setNegativeButton("취소", new DialogInterface.OnClickListener() {                   //다이얼로그 '취소' 버튼 눌렀을 때
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                dialogInterface.dismiss();                                                          //다이얼로그 창 사라짐
                            }
                        });
                        Check.show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "페널티 시간이 남았습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_2_1F.setOnClickListener(new View.OnClickListener() {
            //2번 자리 눌렀을 때
            @Override
            public void onClick(View view) {
                if (penaltyTime.equals("0")) {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                boolean success = jsonObject.getBoolean("success");
                                if (success) {
                                } else {
                                    Toast.makeText(getApplicationContext(), "예약실패", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    };
                    if (btn2.equals("Green")) {
                        if (userReserve == 0) {                                                                              //2번 자리 상태 확인
                            AlertDialog.Builder Check = new AlertDialog.Builder(MainActivity1F_Parking.this);   //다이얼로그 띄우기
                            Check.setTitle("자리 예약");                                                                   //다이얼로그 제목
                            Check.setMessage("#2 자리를 선택하시겠습니까?");                                               //다이얼로그 내용
                            Check.setPositiveButton("예", new DialogInterface.OnClickListener() {                    //다이얼로그 '예' 버튼 눌렀을 때
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {
                                    btn_2_1F.setBackgroundColor(Color.YELLOW);
                                    btn_2_1F.setTextColor(Color.BLACK);//2번 버튼 색 빨강으로 변경
                                    btn_2_1F.setText(userID + "님이 예약하신\n 자리입니다.");
                                    dialogInterface.dismiss();                                                          //다이얼로그 창 사라짐
                                    userReserve2 = 2;
                                    userReserve2_1 = 2;
                                    btn2 = "Yellow";
                                    String setPenaltyTime = "0";
                                    safeLock = "ON";
                                    btn_safeLock.setText("#"+ userReserve2 + " SAFELOCK ON");
                                    btn_safeLock.setBackgroundColor(Color.parseColor("#FF9800"));
                                    long nowLater = System.currentTimeMillis() + 600000;
                                    long now = System.currentTimeMillis();
                                    Date date = new Date(nowLater);
                                    @SuppressLint("SimpleDateFormat")
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
                                    String getTime = dateFormat.format(date);
                                    Date dateNow = new Date(nowLater - now - 32400000);
                                    @SuppressLint("SimpleDateFormat")
                                    SimpleDateFormat dateHour = new SimpleDateFormat("HH", Locale.KOREA);
                                    tv_hour.setText(dateHour.format(dateNow));
                                    @SuppressLint("SimpleDateFormat")
                                    SimpleDateFormat dateMin = new SimpleDateFormat("mm", Locale.KOREA);
                                    tv_min.setText(dateMin.format(dateNow));
                                    @SuppressLint("SimpleDateFormat")
                                    SimpleDateFormat dateSec = new SimpleDateFormat("ss", Locale.KOREA);
                                    tv_sec.setText(dateSec.format(dateNow));
                                    time = getTime;
                                    ReserveRequest reserveRequest = new ReserveRequest(userID, userReserve2, btn2, getTime, penalty, setPenaltyTime,safeLock, responseListener);
                                    RequestQueue queue = Volley.newRequestQueue(MainActivity1F_Parking.this);
                                    queue.add(reserveRequest);
                                    tv_hour.setVisibility(View.VISIBLE);
                                    tv_min.setVisibility(View.VISIBLE);
                                    tv_sec.setVisibility(View.VISIBLE);
                                    tv_split1.setVisibility(View.VISIBLE);
                                    tv_split2.setVisibility(View.VISIBLE);
                                    btn_safeLock.setVisibility(View.VISIBLE);
                                    startTimerTask();
                                    renewal(userReserve2, penalty);
                                    Toast.makeText(getApplicationContext(), "예약완료.", Toast.LENGTH_SHORT).show();
                                }
                            });
                            Check.setNegativeButton("취소", new DialogInterface.OnClickListener() {                   //다이얼로그 '취소' 버튼 눌렀을 때
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {
                                    dialogInterface.dismiss();                                                          //다이얼로그 창 사라짐
                                }
                            });
                            Check.show();
                        } else
                            Toast.makeText(getApplicationContext(), "이미 예약하신 자리가 있습니다.", Toast.LENGTH_SHORT).show();
                    } else if (btn2.equals("Red") && time.equals("0")) {
                        Toast.makeText(getApplicationContext(), "이미 주차된 자리입니다.", Toast.LENGTH_SHORT).show();
                    } else if (userReserve == 2 || userReserve2 == 2) {
                        AlertDialog.Builder Check = new AlertDialog.Builder(MainActivity1F_Parking.this);   //다이얼로그 띄우기
                        Check.setTitle("예약 취소");                                                                   //다이얼로그 제목
                        Check.setMessage("예약을 취소 하시겠습니까?");                                               //다이얼로그 내용
                        Check.setPositiveButton("예", new DialogInterface.OnClickListener() {                    //다이얼로그 '예' 버튼 눌렀을 때
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                btn_2_1F.setBackgroundColor(Color.GREEN);
                                btn_2_1F.setTextColor(Color.WHITE);//1번 버튼 색 빨강으로 변경
                                btn_2_1F.setText("#2"); // 3
                                dialogInterface.dismiss();
                                Toast.makeText(getApplicationContext(), "예약을 취소하셨습니다.", Toast.LENGTH_SHORT).show();
                                userReserve2 = 0;
                                userReserve2_1 = 0;
                                btn2 = "Green";
                                String getTime = "0";
                                String setPenaltyTime = "0";
                                safeLock = "OFF";
                                ReserveRequest reserveRequest = new ReserveRequest(userID, userReserve2, btn2, getTime, penalty, setPenaltyTime,safeLock, responseListener);
                                RequestQueue queue = Volley.newRequestQueue(MainActivity1F_Parking.this);
                                queue.add(reserveRequest);
                                tv_hour.setVisibility(View.INVISIBLE);
                                tv_min.setVisibility(View.INVISIBLE);
                                tv_sec.setVisibility(View.INVISIBLE);
                                tv_split1.setVisibility(View.INVISIBLE);
                                tv_split2.setVisibility(View.INVISIBLE);
                                btn_safeLock.setVisibility(View.INVISIBLE);
                                timer.cancel();
                                renewal(userReserve2, penalty);
                            }
                        });
                        Check.setNegativeButton("취소", new DialogInterface.OnClickListener() {                   //다이얼로그 '취소' 버튼 눌렀을 때
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                dialogInterface.dismiss();                                                          //다이얼로그 창 사라짐
                            }
                        });
                        Check.show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "페널티 시간이 남았습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_3_1F.setOnClickListener(new View.OnClickListener() {                                            //3번 자리 눌렀을 때
            @Override
            public void onClick(View view) {
                if (penaltyTime.equals("0")) {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                boolean success = jsonObject.getBoolean("success");
                                if (success) {
                                } else {
                                    Toast.makeText(getApplicationContext(), "예약실패", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    };
                    if (btn3.equals("Green")) {
                        if (userReserve == 0) {                                                                              //2번 자리 상태 확인
                            AlertDialog.Builder Check = new AlertDialog.Builder(MainActivity1F_Parking.this);   //다이얼로그 띄우기
                            Check.setTitle("자리 예약");                                                                   //다이얼로그 제목
                            Check.setMessage("#3 자리를 선택하시겠습니까?");                                               //다이얼로그 내용
                            Check.setPositiveButton("예", new DialogInterface.OnClickListener() {                    //다이얼로그 '예' 버튼 눌렀을 때
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {
                                    btn_3_1F.setBackgroundColor(Color.YELLOW);                                             //2번 버튼 색 빨강으로 변경
                                    btn_3_1F.setTextColor(Color.BLACK);
                                    btn_3_1F.setText(userID + "님이 예약하신\n 자리입니다.");
                                    dialogInterface.dismiss();                                                          //다이얼로그 창 사라짐
                                    userReserve3 = 3;
                                    userReserve3_1 = 3;
                                    btn3 = "Yellow";
                                    String setPenaltyTime = "0";
                                    safeLock = "ON";
                                    btn_safeLock.setText("#"+ userReserve3 + " SAFELOCK ON");
                                    btn_safeLock.setBackgroundColor(Color.parseColor("#FF9800"));
                                    long nowLater = System.currentTimeMillis() + 600000;
                                    long now = System.currentTimeMillis();
                                    Date date = new Date(nowLater);
                                    @SuppressLint("SimpleDateFormat")
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
                                    String getTime = dateFormat.format(date);
                                    Date dateNow = new Date(nowLater - now - 32400000);
                                    @SuppressLint("SimpleDateFormat")
                                    SimpleDateFormat dateHour = new SimpleDateFormat("HH", Locale.KOREA);
                                    tv_hour.setText(dateHour.format(dateNow));
                                    @SuppressLint("SimpleDateFormat")
                                    SimpleDateFormat dateMin = new SimpleDateFormat("mm", Locale.KOREA);
                                    tv_min.setText(dateMin.format(dateNow));
                                    @SuppressLint("SimpleDateFormat")
                                    SimpleDateFormat dateSec = new SimpleDateFormat("ss", Locale.KOREA);
                                    tv_sec.setText(dateSec.format(dateNow));
                                    time = getTime;
                                    ReserveRequest reserveRequest = new ReserveRequest(userID, userReserve3, btn3, getTime, penalty, setPenaltyTime,safeLock, responseListener);
                                    RequestQueue queue = Volley.newRequestQueue(MainActivity1F_Parking.this);
                                    queue.add(reserveRequest);
                                    tv_hour.setVisibility(View.VISIBLE);
                                    tv_min.setVisibility(View.VISIBLE);
                                    tv_sec.setVisibility(View.VISIBLE);
                                    tv_split1.setVisibility(View.VISIBLE);
                                    tv_split2.setVisibility(View.VISIBLE);
                                    btn_safeLock.setVisibility(View.VISIBLE);
                                    startTimerTask();
                                    renewal(userReserve3, penalty);
                                    Toast.makeText(getApplicationContext(), "예약완료.", Toast.LENGTH_SHORT).show();
                                }
                            });
                            Check.setNegativeButton("취소", new DialogInterface.OnClickListener() {                   //다이얼로그 '취소' 버튼 눌렀을 때
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {
                                    dialogInterface.dismiss();                                                          //다이얼로그 창 사라짐
                                }
                            });
                            Check.show();
                        } else
                            Toast.makeText(getApplicationContext(), "이미 예약하신 자리가 있습니다.", Toast.LENGTH_SHORT).show();
                    } else if (btn3.equals("Red") && time.equals("0")) {
                        Toast.makeText(getApplicationContext(), "이미 주차된 자리입니다.", Toast.LENGTH_SHORT).show();
                    } else if (userReserve == 3 || userReserve3 == 3) {
                        AlertDialog.Builder Check = new AlertDialog.Builder(MainActivity1F_Parking.this);   //다이얼로그 띄우기
                        Check.setTitle("예약 취소");                                                                   //다이얼로그 제목
                        Check.setMessage("예약을 취소 하시겠습니까?");                                               //다이얼로그 내용
                        Check.setPositiveButton("예", new DialogInterface.OnClickListener() {                    //다이얼로그 '예' 버튼 눌렀을 때
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                btn_3_1F.setBackgroundColor(Color.GREEN);
                                btn_3_1F.setTextColor(Color.WHITE);//1번 버튼 색 빨강으로 변경
                                btn_3_1F.setText("#3"); // 3
                                dialogInterface.dismiss();
                                Toast.makeText(getApplicationContext(), "예약을 취소하셨습니다.", Toast.LENGTH_SHORT).show();
                                userReserve3 = 0;
                                userReserve3_1 = 0;
                                btn3 = "Green";
                                String getTime = "0";
                                String setPenaltyTime = "0";
                                safeLock = "OFF";
                                ReserveRequest reserveRequest = new ReserveRequest(userID, userReserve3, btn3, getTime, penalty, setPenaltyTime,safeLock, responseListener);
                                RequestQueue queue = Volley.newRequestQueue(MainActivity1F_Parking.this);
                                queue.add(reserveRequest);
                                tv_hour.setVisibility(View.INVISIBLE);
                                tv_min.setVisibility(View.INVISIBLE);
                                tv_sec.setVisibility(View.INVISIBLE);
                                tv_split1.setVisibility(View.INVISIBLE);
                                tv_split2.setVisibility(View.INVISIBLE);
                                btn_safeLock.setVisibility(View.INVISIBLE);
                                timer.cancel();
                                renewal(userReserve3, penalty);
                            }
                        });
                        Check.setNegativeButton("취소", new DialogInterface.OnClickListener() {                   //다이얼로그 '취소' 버튼 눌렀을 때
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                dialogInterface.dismiss();                                                          //다이얼로그 창 사라짐
                            }
                        });
                        Check.show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "페널티 시간이 남았습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_safeLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) {
                            } else {
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };
                if (safeLock.equals("ON")) {
                    if (userReserve1 != 0 || userReserve2 != 0 || userReserve3 != 0) {
                        safeLockState = userReserve1 + userReserve2 + userReserve3;
                    } else if (userReserve != 0) {
                        safeLockState = userReserve;
                    }
                    AlertDialog.Builder Check = new AlertDialog.Builder(MainActivity1F_Parking.this);   //다이얼로그 띄우기
                    Check.setTitle("SAFELOCK 해제");                                                                   //다이얼로그 제목
                    Check.setMessage("#" + safeLockState + " SAFELOCK OFF하시겠습니까?");                                               //다이얼로그 내용
                    Check.setPositiveButton("예", new DialogInterface.OnClickListener() {                    //다이얼로그 '예' 버튼 눌렀을 때
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            //해당 자리 색상 받아오기
                            safeLock = "OFF";
                            btn_safeLock.setText("#" + safeLockState + " SAFELOCK OFF");
                            btn_safeLock.setBackgroundColor(Color.BLUE);
                            dialogInterface.dismiss();                                                          //다이얼로그 창 사라짐
                            String setPenaltyTime = "0";
                            btn = "Yellow";
                            ReserveRequest reserveRequest = new ReserveRequest(userID, safeLockState, btn, time, penalty, setPenaltyTime, safeLock, responseListener);
                            RequestQueue queue = Volley.newRequestQueue(MainActivity1F_Parking.this);
                            queue.add(reserveRequest);
                            tv_hour.setVisibility(View.VISIBLE);
                            tv_min.setVisibility(View.VISIBLE);
                            tv_sec.setVisibility(View.VISIBLE);
                            tv_split1.setVisibility(View.VISIBLE);
                            tv_split2.setVisibility(View.VISIBLE);
                            btn_safeLock.setVisibility(View.VISIBLE);
                            Toast.makeText(getApplicationContext(), "SAFELOCK 해제", Toast.LENGTH_SHORT).show();
                        }
                    });
                    Check.setNegativeButton("취소", new DialogInterface.OnClickListener() {                   //다이얼로그 '취소' 버튼 눌렀을 때
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            dialogInterface.dismiss();                                                          //다이얼로그 창 사라짐
                        }
                    });
                    Check.show();
                }
            }
        });
    }
    private void startTimerTask()
    {
        hour = Integer.parseInt(tv_hour.getText().toString());
        minute = Integer.parseInt(tv_min.getText().toString());
        second = Integer.parseInt(tv_sec.getText().toString());
        timerTask = new TimerTask()
        {
            @SuppressLint("SetTextI18n")
            @Override
            public void run()
            {
                if(second != 0)
                    second--;
                else if(minute != 0){
                    second = 60;
                    second--;
                    minute--;
                }
                else if(hour !=0){
                    second = 60;
                    minute = 60;
                    second--;
                    minute--;
                    hour--;
                }
                if(second <= 9){
                    tv_sec.post(new Runnable() {
                        @Override
                        public void run() {
                            tv_sec.setText("0"+second);
                        }
                    });
                } else {
                    tv_sec.post(new Runnable() {
                        @Override
                        public void run() {
                            tv_sec.setText(second + "");
                        }
                    });
                }

                if(minute <= 9){
                    tv_min.post(new Runnable() {
                        @Override
                        public void run() {
                            tv_min.setText("0"+minute);
                        }
                    });
                } else {
                    tv_min.post(new Runnable() {
                        @Override
                        public void run() {
                            tv_min.setText(minute + "");
                        }
                    });
                }

                if(hour <= 9){
                    tv_hour.post(new Runnable() {
                        @Override
                        public void run() {
                            tv_hour.setText("0"+hour);
                        }
                    });
                } else {
                    tv_hour.post(new Runnable() {
                        @Override
                        public void run() {
                            tv_hour.setText(hour + "");
                        }
                    });
                }

                if(state.equals("Red") && safeLock.equals("OFF") && time.equals("0")){ //추가한부분
                    timer.cancel();
                }
                else if((hour <= 0 && minute <= 0 && second <= 0) && !penaltyTime.equals("0") || ((hour < 0 || minute < 0 || second < 0) && !penaltyTime.equals("0"))) {
                    timer.cancel();//타이머 종료
                    penaltyOverTime();
                }
                else if((hour <= 0 && minute <= 0 && second <= 0) || (hour < 0 || minute < 0 || second < 0)) {
                    timer.cancel();//타이머 종료
                    overTime();
                }
            }
        };
        timer = new Timer();
        timer.schedule(timerTask,0 ,1000);
    }

    @SuppressLint("SetTextI18n")
    private void counterTimer(String time){

        long nowSec = System.currentTimeMillis();
        Date nowDate = new Date(nowSec);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
        String now = dateFormat.format(nowDate); // 현재 시간을 dateFormat으로 저장
        try {
            Date date = dateFormat.parse(time); // 저장 시간
            Date date1 = dateFormat.parse(now); // 현재 시간
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat dateDay = new SimpleDateFormat("dd",Locale.KOREA);
            if(penalty >= 0 && calculrateTime(dateDay.format(date), dateDay.format(date1)) == 0 && !penaltyTime.equals("0"))
                dd = 9;
            else if (penalty >= 0 && calculrateTime(dateDay.format(date), dateDay.format(date1)) < 0 && !penaltyTime.equals("0")){
                timer.cancel();
                penaltyOverTime();
            }
            else if(penalty >= 1 && calculrateTime(dateDay.format(date), dateDay.format(date1)) == 1 && !penaltyTime.equals("0"))
                dd = 33;
            else if (penalty >= 1 && calculrateTime(dateDay.format(date), dateDay.format(date1)) < 0 && !penaltyTime.equals("0")){
                timer.cancel();
                penaltyOverTime();
            }
            else if(penalty >= 2 && calculrateTime(dateDay.format(date), dateDay.format(date1)) == 2 && !penaltyTime.equals("0"))
                dd = 57;
            else if (penalty >= 2 && calculrateTime(dateDay.format(date), dateDay.format(date1)) < 0 && !penaltyTime.equals("0")){
                timer.cancel();
                penaltyOverTime();
            }
            else if(penalty >= 3 && calculrateTime(dateDay.format(date), dateDay.format(date1)) == 3 && !penaltyTime.equals("0"))
                dd = 81;
            else if (penalty >= 3 && calculrateTime(dateDay.format(date), dateDay.format(date1)) < 0 && !penaltyTime.equals("0")){
                timer.cancel();
                penaltyOverTime();
            }
            else
                dd = -1;

            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat dateHour = new SimpleDateFormat("HH",Locale.KOREA);
            if(dd > 9){
                if(calculrateTime(dateHour.format(date),dateHour.format(date1))>=0)
                    tv_hour.setText(calculrateTime(dateHour.format(date), dateHour.format(date1))+dd-1 + "");
                else if(calculrateTime(dateHour.format(date),dateHour.format(date1))>=0)
                    tv_hour.setText(calculrateTime(dateHour.format(date), dateHour.format(date1))+dd-1 + "");

                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat dateMin = new SimpleDateFormat("mm",Locale.KOREA);
                tv_min.setText(calculrateTime(dateMin.format(date),dateMin.format(date1))+"");
                if(calculrateTime(dateMin.format(date),dateMin.format(date1))>=0)
                    tv_min.setText(calculrateTime(dateMin.format(date), dateMin.format(date1)) + "");
                else if(calculrateTime(dateMin.format(date),dateMin.format(date1))<0){
                    tv_hour.setText(calculrateTime(dateHour.format(date), dateHour.format(date1))-1+dd + "");
                    tv_min.setText(calculrateTime(dateMin.format(date), dateMin.format(date1))+60 + "");
                }

                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat dateSec = new SimpleDateFormat("ss",Locale.KOREA);
                if(calculrateTime(dateSec.format(date),dateSec.format(date1))>=0)
                    tv_sec.setText(calculrateTime(dateSec.format(date),dateSec.format(date1))+"");
                else if(calculrateTime(dateMin.format(date),dateMin.format(date1))>=0 && calculrateTime(dateSec.format(date),dateSec.format(date1))<0){
                    tv_min.setText(calculrateTime(dateMin.format(date), dateMin.format(date1))-1 + "");
                    tv_sec.setText(calculrateTime(dateSec.format(date),dateSec.format(date1))+60 +"");
                }
                else if(calculrateTime(dateMin.format(date),dateMin.format(date1))<0 && calculrateTime(dateSec.format(date),dateSec.format(date1))<0){
                    tv_min.setText(calculrateTime(dateMin.format(date), dateMin.format(date1))+60-1 + "");
                    tv_sec.setText(calculrateTime(dateSec.format(date),dateSec.format(date1))+60 +"");
                }
            }

            else{
                if(calculrateTime(dateHour.format(date),dateHour.format(date1))>=0)
                    tv_hour.setText(calculrateTime(dateHour.format(date), dateHour.format(date1)) + "");

                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat dateMin = new SimpleDateFormat("mm",Locale.KOREA);
                tv_min.setText(calculrateTime(dateMin.format(date),dateMin.format(date1))+"");
                if(calculrateTime(dateMin.format(date),dateMin.format(date1))>=0)
                    tv_min.setText(calculrateTime(dateMin.format(date), dateMin.format(date1)) + "");
                else if(calculrateTime(dateMin.format(date),dateMin.format(date1))<0){
                    tv_hour.setText(calculrateTime(dateHour.format(date), dateHour.format(date1))-1 + "");
                    tv_min.setText(calculrateTime(dateMin.format(date), dateMin.format(date1))+60 + "");
                }

                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat dateSec = new SimpleDateFormat("ss",Locale.KOREA);
                if(calculrateTime(dateSec.format(date),dateSec.format(date1))>=0)
                    tv_sec.setText(calculrateTime(dateSec.format(date),dateSec.format(date1))+"");
                else if(calculrateTime(dateMin.format(date),dateMin.format(date1))>=0 && calculrateTime(dateSec.format(date),dateSec.format(date1))<0){
                    tv_min.setText(calculrateTime(dateMin.format(date), dateMin.format(date1))-1 + "");
                    tv_sec.setText(calculrateTime(dateSec.format(date),dateSec.format(date1))+60 +"");
                }
                else if(calculrateTime(dateMin.format(date),dateMin.format(date1))<0 && calculrateTime(dateSec.format(date),dateSec.format(date1))<0){
                    tv_min.setText(calculrateTime(dateMin.format(date), dateMin.format(date1))+60-1 + "");
                    tv_sec.setText(calculrateTime(dateSec.format(date),dateSec.format(date1))+60 +"");
                }
            }
            if(calculrateTime(dateDay.format(date), dateDay.format(date1)) >= 0) {
                tv_hour.setVisibility(View.VISIBLE);
                tv_min.setVisibility(View.VISIBLE);
                tv_sec.setVisibility(View.VISIBLE);
                tv_split1.setVisibility(View.VISIBLE);
                tv_split2.setVisibility(View.VISIBLE);
                if (!time.equals("0")) {
                    btn_safeLock.setVisibility(View.VISIBLE);
                    tv_hour.setTextColor(Color.WHITE);
                    tv_min.setTextColor(Color.WHITE);
                    tv_sec.setTextColor(Color.WHITE);
                } else {
                    btn_safeLock.setVisibility(View.INVISIBLE);
                    tv_hour.setTextColor(Color.RED);
                    tv_min.setTextColor(Color.RED);
                    tv_sec.setTextColor(Color.RED);
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        startTimerTask();
    }

    private int calculrateTime(String saveTime, String nowTime){
        int a = Integer.parseInt(saveTime);
        int b = Integer.parseInt(nowTime);
        int sum = a-b;
        return sum;
    }

    @SuppressLint("SetTextI18n")
    private void overTime(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
        Date date = null;
        String getTime = "0";
        Response.Listener<String> responseListener = new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if(success){
                        Toast.makeText(getApplicationContext(), "예약시간초과", Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), "페널티 지급", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "에러.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        if(userReserve == 1){
            btn_1_1F.setBackgroundColor(Color.GREEN);                                             //1번 버튼 색 빨강으로 변경
            btn_1_1F.setText("#1"); // 3
            btn_1_1F.setTextColor(Color.WHITE);                                                       //다이얼로그 창 사라짐
            userReserve = 0;
            userReserve1_1 = 0;
        }
        else if(userReserve == 2){
            btn_2_1F.setBackgroundColor(Color.GREEN);
            btn_2_1F.setTextColor(Color.WHITE);//2번 버튼 색 빨강으로 변경
            btn_2_1F.setText("#2");
            userReserve = 0;
            userReserve2_1 = 0;
        }
        else if(userReserve == 3){
            btn_3_1F.setBackgroundColor(Color.GREEN);
            btn_3_1F.setTextColor(Color.WHITE);//2번 버튼 색 빨강으로 변경
            btn_3_1F.setText("#3");
            userReserve = 0;
            userReserve3_1 = 0;
        }                                                           //자리 상태 변경(빈자리 -> 예약)
        btn1 = "Green";
        penalty += 1;
        // 예약 초과시 페널티
        if(penalty == 1){
            long nowLater = System.currentTimeMillis() + 54000000; // 하루
            date = new Date(nowLater);
        }
        else if(penalty == 2){
            long nowLater = System.currentTimeMillis() + 140400000; // 이틀
            date = new Date(nowLater);
        }
        else if(penalty >= 3){
            long nowLater = System.currentTimeMillis() + 226800000; // 삼일
            date = new Date(nowLater);
        }
        penaltyTime = dateFormat.format(date);
        safeLock = "OFF";
        ReserveRequest reserveRequest = new ReserveRequest(userID, userReserve, btn1, getTime, penalty, penaltyTime,safeLock, responseListener);
        RequestQueue queue = Volley.newRequestQueue(MainActivity1F_Parking.this);
        queue.add(reserveRequest);
        tv_hour.setVisibility(View.INVISIBLE);
        tv_min.setVisibility(View.INVISIBLE);
        tv_sec.setVisibility(View.INVISIBLE);
        tv_split1.setVisibility(View.INVISIBLE);
        tv_split2.setVisibility(View.INVISIBLE);
        btn_safeLock.setVisibility(View.INVISIBLE);
    }

    private void penaltyOverTime(){
        Intent intent = getIntent(); // 1
        String userID = intent.getStringExtra("userID"); // 2
        penalty = intent.getIntExtra("penalty",0);

        Response.Listener<String> responseListener = new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if(success){
                        Toast.makeText(getApplicationContext(), "페널티 시간 종료", Toast.LENGTH_SHORT).show();
                    }
                    else{
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        String btn = "Green";
        int userReserve = 0;
        String getTime = "0";
        penaltyTime = "0";
        ReserveRequest reserveRequest = new ReserveRequest(userID, userReserve, btn, getTime, penalty, penaltyTime,safeLock, responseListener);
        RequestQueue queue = Volley.newRequestQueue(MainActivity1F_Parking.this);
        queue.add(reserveRequest);

        tv_hour.setVisibility(View.INVISIBLE);
        tv_min.setVisibility(View.INVISIBLE);
        tv_sec.setVisibility(View.INVISIBLE);
        tv_split1.setVisibility(View.INVISIBLE);
        tv_split2.setVisibility(View.INVISIBLE);
        btn_safeLock.setVisibility(View.INVISIBLE);
    }

    private void renewal(int userReserve_Re, int penalty_Re){
        userReserve = userReserve_Re;
        penalty = penalty_Re;
    }


    private void updateLogin(){
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
                        state = state_Re;
                        userReserve = userReserve_Re;
                        time = time_Re;
                        penalty = penalty_Re;
                        penaltyTime = penaltyTime_Re;
                        safeLock = safeLock_Re;
                    } else {
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        loginRequest = new LoginRequest(userID, userPassword, responseListener);
        if(queueLogin == null) {
            queueLogin = Volley.newRequestQueue(MainActivity1F_Parking.this);
        }
        queueLogin.add(loginRequest);
    }

    private void updateCount(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        int userReserve1_1_Re = jsonObject.getInt("userReserve1_1");
                        int userReserve2_1_Re = jsonObject.getInt("userReserve2_1");
                        int userReserve3_1_Re = jsonObject.getInt("userReserve3_1");
                        String state1_Re = jsonObject.getString("state1");
                        String state2_Re = jsonObject.getString("state2");
                        String state3_Re = jsonObject.getString("state3");
                        String state_RGB1_Re = jsonObject.getString("state_RGB1");
                        String state_RGB2_Re = jsonObject.getString("state_RGB2");
                        String state_RGB3_Re = jsonObject.getString("state_RGB3");
                        userReserve1_1 = userReserve1_1_Re;
                        userReserve2_1 = userReserve2_1_Re;
                        userReserve3_1 = userReserve3_1_Re;
                        state1 = state1_Re;
                        state2 = state2_Re;
                        state3 = state3_Re;
                        state_RGB1 = state_RGB1_Re;
                        state_RGB2 = state_RGB2_Re;
                        state_RGB3 = state_RGB3_Re;
                    } else {
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        countRequest = new countRequest(userID, responseListener);
        if(queueCount == null) {
            queueCount = Volley.newRequestQueue(MainActivity1F_Parking.this);
        }
        queueCount.add(countRequest);
    }
}