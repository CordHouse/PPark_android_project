package com.example.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.login.databinding.ActivityMainLoginBinding;
import com.google.android.material.navigation.NavigationView;

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
                        tv_login_id = (TextView) findViewById(R.id.tv_login_id);
                        tv_login_id.setText(userID + "회원님! 반갑습니다!");
                        Toast.makeText(getApplicationContext(), "홈", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_reserve:
                        Intent intent_reserve = new Intent(MainActivity_login.this, sildingActivity.class);
                        startActivity(intent_reserve);
                        tv_login_id = (TextView) findViewById(R.id.tv_login_id);
                        tv_login_id.setText(userID + "회원님! 반갑습니다!");
                        Toast.makeText(getApplicationContext(), "예약현황", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_map:
                        Intent intent_map = new Intent(MainActivity_login.this, mapViewActivity.class);
                        startActivity(intent_map);
                        tv_login_id = (TextView) findViewById(R.id.tv_login_id);
                        tv_login_id.setText(userID + "회원님! 반갑습니다!");
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