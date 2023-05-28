package com.diagnostika.paskyrosValdymas;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.diagnostika.MainActivity;
import com.diagnostika.R;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class Prisijungimas extends AppCompatActivity {

    private static final String SHARED_PREFS = "sharedPrefs";
    private static final  String VARTOTOJAS = "vartotojas";
    private int vartotojasId;
    EditText editTextPrisijungimas,editTextSlaptazodis;
    Button buttonPrisijungti, buttonRegistracija;
    ProgressBar progressBar;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prisijungimas);

        editTextPrisijungimas = findViewById(R.id.prisijungimasPrisijungimas);
        editTextSlaptazodis = findViewById(R.id.prisijungimasSlaptazodis);
        buttonPrisijungti = findViewById(R.id.mygtukasPrisijungti);
        progressBar = findViewById(R.id.prisijungimasProgressBar);
        buttonRegistracija = findViewById(R.id.buttonRegistruotis);

        buttonPrisijungti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prisijungti();
            }

        });

        buttonRegistracija.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Registracija.class);
                startActivity(intent);
            }
        });
    }

    private void prisijungti() {

                String prisijungimas,slaptazodis;
                prisijungimas = String.valueOf(editTextPrisijungimas.getText());
                slaptazodis = String.valueOf(editTextSlaptazodis.getText());

                if(!prisijungimas.equals("") && !slaptazodis.equals("")) {
                    progressBar.setVisibility(View.VISIBLE);
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            String[] field = new String[2];
                            field[0] = "username";
                            field[1] = "password";
                            String[] data = new String[2];
                            data[0] = prisijungimas;
                            data[1] = slaptazodis;
                            PutData putData = new PutData("http://192.168.0.109/DbOperations/login.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    progressBar.setVisibility(View.GONE);
                                    String result = putData.getResult();
                                    if(!result.equals("Neteisingi duomenys")) {
                                        if (!result.equals("tinklo klaida")) {
                                            if (!result.equals("Visi laukai privalomi")) {
                                                vartotojasId = Integer.parseInt(result);
                                                saveData();
                                                Toast.makeText(getApplicationContext(), "Prisijungta", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }else
                                                Toast.makeText(Prisijungimas.this, result, Toast.LENGTH_SHORT).show();
                                        }else
                                            Toast.makeText(Prisijungimas.this, result, Toast.LENGTH_SHORT).show();
                                    }else
                                        Toast.makeText(Prisijungimas.this, result, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(getApplicationContext(),"Visi laukai privalomi",Toast.LENGTH_SHORT).show();
                }
            }


    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(VARTOTOJAS, vartotojasId);
        editor.apply();
    }

    }



