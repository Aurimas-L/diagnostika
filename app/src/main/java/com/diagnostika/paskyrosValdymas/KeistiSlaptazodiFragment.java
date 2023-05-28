package com.diagnostika.paskyrosValdymas;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.diagnostika.R;
import com.vishnusivadas.advanced_httpurlconnection.PutData;


public class KeistiSlaptazodiFragment extends Fragment {

    EditText editTextDabartinisSlaptazodis, editTextNaujasSlaptazodis, editTextNaujasSlaptazodis2;
    Button buttonKeitimasPakeistiSlaptazodi;

    int vartotojasId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_keisti_slaptazodi, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editTextDabartinisSlaptazodis = view.findViewById(R.id.editTextDabartinisSlaptazodis);
        editTextNaujasSlaptazodis = view.findViewById(R.id.editTextnaujasSlaptazodis);
        editTextNaujasSlaptazodis2 = view.findViewById(R.id.editTextNaujasSlaptazodis2);
        buttonKeitimasPakeistiSlaptazodi = view.findViewById(R.id.buttonKeitimasPakeistiSlaptazodi);

        Bundle bundle = this.getArguments();
        vartotojasId = bundle.getInt("vartotojasId");
        String prisijungimas = bundle.getString("prisijungimas");

        buttonKeitimasPakeistiSlaptazodi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String senas, naujas, naujas2;
                senas = String.valueOf(editTextDabartinisSlaptazodis.getText());
                naujas = String.valueOf(editTextNaujasSlaptazodis.getText());
                naujas2 = String.valueOf(editTextNaujasSlaptazodis2.getText());
                if (!senas.equals("") && !naujas.equals("") && !naujas2.equals("")) {
                    if (naujas.equals(naujas2)) {
                        updateSlaptazodis(vartotojasId, senas, naujas, prisijungimas);
                    } else
                        Toast.makeText(getContext(), "Slaptažodis pakartotas neteisingai", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getContext(), "Užpildykite laukus", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateSlaptazodis(int vartotojasId, String slaptazodis, String naujasSlaptazodis, String prisijungimas) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                String[] field = new String[4];
                field[0] = "vartotojasId";
                field[1] = "slaptazodis";
                field[2] = "naujasSlaptazodis";
                field[3] = "prisijungimas";
                String[] data = new String[4];
                data[0] = String.valueOf(vartotojasId);
                data[1] = slaptazodis;
                data[2] = naujasSlaptazodis;
                data[3] = prisijungimas;
                PutData putData = new PutData("http://192.168.0.109/DbOperations/updateSlaptazodis.php", "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = putData.getResult();
                        if (!result.equals("Negauti duomenys")) {
                            if (!result.equals("tinklo klaida")) {
                                if (!result.equals("slaptazodis neteisingas")) {
                                    if (result.equals(vartotojasId+"pavyko")) {
                                        Toast.makeText(getContext(), "slaptažodis pakeistas", Toast.LENGTH_SHORT).show();
                                        getActivity().getSupportFragmentManager().popBackStackImmediate();
                                    }
                                } else
                                    Toast.makeText(getContext(), "slaptažodis neteisingas", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }
        });
    }

    private boolean passwordVerify(String prisijungimas, String senas) {
        final boolean[] teisingas = {false};

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                String[] field = new String[2];
                field[0] = "username";
                field[1] = "password";
                String[] data = new String[2];
                data[0] = prisijungimas;
                data[1] = senas;
                PutData putData = new PutData("http://192.168.0.109/DbOperations/login.php", "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = putData.getResult();
                        if (!result.equals("Neteisingi duomenys")) {
                            if (!result.equals("tinklo klaida")) {
                                if (!result.equals("Visi laukai privalomi")) {
                                    int vartotojasIdDb = Integer.parseInt(result);
                                    if (vartotojasId == vartotojasIdDb) ;
                                }
                            }
                        }
                    }
                }
            }
        });
        return teisingas[0];
    }
}