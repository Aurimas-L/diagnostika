package com.diagnostika.paskyrosValdymas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.diagnostika.R;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class Registracija extends AppCompatActivity {

    EditText editTextVardas, editTextPrisijungimas,editTextEmail,editTextSlaptazodis;
    Button buttonRegistruotis;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registracija);

        editTextVardas= findViewById(R.id.registracijaVardas);
        editTextPrisijungimas=findViewById(R.id.registracijaPrisijungimas);
        editTextEmail= findViewById(R.id.registracijaEmail);
        editTextSlaptazodis=findViewById(R.id.registracijaSlaptazodis);
        buttonRegistruotis = findViewById(R.id.buttonRegistracija);
        progressBar = findViewById(R.id.registracijaProgressBar);

        buttonRegistruotis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String vardas,prisijungimas,slaptazodis,email;
                vardas = String.valueOf(editTextVardas.getText());
                prisijungimas = String.valueOf(editTextPrisijungimas.getText());
                email = String.valueOf(editTextEmail.getText());
                slaptazodis = String.valueOf(editTextSlaptazodis.getText());

                if(!vardas.equals("") && !prisijungimas.equals("") && !email.equals("") && !slaptazodis.equals("")) {
                   progressBar.setVisibility(View.VISIBLE);
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            String[] field = new String[4];
                            field[0] = "fullname";
                            field[1] = "username";
                            field[2] = "email";
                            field[3] = "password";

                            String[] data = new String[4];
                            data[0] = vardas;
                            data[1] = prisijungimas;
                            data[2] = email;
                            data[3] = slaptazodis;

                            PutData putData = new PutData("http://192.168.0.109/DbOperations/insertVartotojas.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    progressBar.setVisibility(View.GONE);
                                    String result = putData.getResult();
                                    if(result.equals("Registracija pavyko")){
                                        Toast.makeText(getApplicationContext(), "Registracija sėkminga", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), Prisijungimas.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(getApplicationContext(),"Visi laukai privalomi",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

}