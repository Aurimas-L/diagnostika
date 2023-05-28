package com.diagnostika;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.diagnostika.automobiliuValdymas.MasinaFragment;
import com.diagnostika.koduTikrinimas.PaieskaFragment;
import com.diagnostika.paskyrosValdymas.PaskyraFragment;
import com.diagnostika.paskyrosValdymas.Prisijungimas;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    PaieskaFragment paieskaFragment= new PaieskaFragment();
    MasinaFragment masinaFragment = new MasinaFragment();
    PaskyraFragment paskyraFragment = new PaskyraFragment();


    private static final String SHARED_PREFS = "sharedPrefs";
    private static final  String VARTOTOJAS = "vartotojas";
    private int vartotojasId;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadData();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        getSupportFragmentManager().beginTransaction().replace(R.id.flFrangment, paieskaFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                switch (item.getItemId()){
                    case R.id.navPaieska:
                          getSupportFragmentManager().beginTransaction().replace(R.id.flFrangment,paieskaFragment).commit();
                        return true;
                    case R.id.navMasina:
                        if(vartotojasId != 0) getSupportFragmentManager().beginTransaction().replace(R.id.flFrangment, masinaFragment).commit();
                        else {
                            Intent intent = new Intent(getApplicationContext(), Prisijungimas.class);
                            startActivity(intent);
                        }
                        return true;
                    case R.id.navPaskyra:
                        if(vartotojasId != 0)   getSupportFragmentManager().beginTransaction().replace(R.id.flFrangment,paskyraFragment).commit();
                        else {
                            Intent intent = new Intent(getApplicationContext(), Prisijungimas.class);
                            startActivity(intent);
                        }
                        return true;
                }

                return false;
            }
        });
    }
    public void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        vartotojasId = sharedPreferences.getInt(VARTOTOJAS,0);
    }

    }