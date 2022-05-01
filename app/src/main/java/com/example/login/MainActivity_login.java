package com.example.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.login.databinding.ActivityMainLoginBinding;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity_login extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainLoginBinding binding;
    TextView tv_login_id;

    @SuppressLint({"SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_header_login);

        Intent intent = getIntent();
        String userID = intent.getStringExtra("userID");
        String userPassword = intent.getStringExtra("userPassword");
        String state = intent.getStringExtra("state");
        int userReserve = intent.getIntExtra("userReserve",0);
        String time = intent.getStringExtra("time");

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(!Thread.interrupted())
                    try {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv_login_id = (TextView) findViewById(R.id.tv_login_id);
                                tv_login_id.setText(userID + "회원님!\r\n반갑습니다!");
                            }
                        });
                    }catch (InterruptedException e){

                    }
            }
        }).start();

        binding = ActivityMainLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMainLogin.toolbarLogin);

        DrawerLayout drawer = binding.drawerLayoutLogin;
        NavigationView navigationView = binding.navViewLogin;

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_reserve)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main_login);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.nav_home:
                        Toast.makeText(getApplicationContext(), "홈", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.nav_reserve:
                        if(userReserve != 0) {
                            Response.Listener<String> responseListener = new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        boolean success = jsonObject.getBoolean("success");
                                        if (success) {
                                            String count = jsonObject.getString("count");
                                            String count2 = jsonObject.getString("count2");
                                            int userReserve1_1 = jsonObject.getInt("userReserve1_1");
                                            int userReserve2_1 = jsonObject.getInt("userReserve2_1");
                                            int userReserve3_1 = jsonObject.getInt("userReserve3_1");
                                            int userReserve4_2 = jsonObject.getInt("userReserve4_2");
                                            int userReserve5_2 = jsonObject.getInt("userReserve5_2");
                                            int userReserve6_2 = jsonObject.getInt("userReserve6_2");
                                            Intent intent_reserve = new Intent(MainActivity_login.this, MainActivity_Floor.class);
                                            intent_reserve.putExtra("userID", userID); // 3
                                            intent_reserve.putExtra("count", count);
                                            intent_reserve.putExtra("count2", count2);
                                            intent_reserve.putExtra("userReserve", userReserve);
                                            intent_reserve.putExtra("state", state);
                                            intent_reserve.putExtra("userReserve1_1", userReserve1_1);
                                            intent_reserve.putExtra("userReserve2_1", userReserve2_1);
                                            intent_reserve.putExtra("userReserve3_1", userReserve3_1);
                                            intent_reserve.putExtra("userReserve4_2", userReserve4_2);
                                            intent_reserve.putExtra("userReserve5_2", userReserve5_2);
                                            intent_reserve.putExtra("userReserve6_2", userReserve6_2);
                                            intent_reserve.putExtra("time", time);
                                            intent_reserve.putExtra("userPassword",userPassword);
                                            startActivity(intent_reserve);
                                            Toast.makeText(getApplicationContext(), "예약현황", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getApplicationContext(), jsonObject.getString("userReserve"), Toast.LENGTH_SHORT).show();

                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            };
                            countRequest countRequest = new countRequest(userID, responseListener);
                            RequestQueue queue = Volley.newRequestQueue(MainActivity_login.this);
                            queue.add(countRequest);
                            break;
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "예약정보없음", Toast.LENGTH_SHORT).show();
                            break;
                        }

                    case R.id.nav_map:
                        Intent intent_map = new Intent(MainActivity_login.this, mapViewActivity.class);
                        intent_map.putExtra("userID",userID);
                        intent_map.putExtra("userReserve", userReserve);
                        intent_map.putExtra("state",state);
                        intent_map.putExtra("time",time);
                        intent_map.putExtra("userPassword",userPassword);
                        startActivity(intent_map);
                        Toast.makeText(getApplicationContext(), "지도", Toast.LENGTH_SHORT).show();
                        break;

                }

                DrawerLayout drawer = findViewById(R.id.drawer_layout_login);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main_login);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}