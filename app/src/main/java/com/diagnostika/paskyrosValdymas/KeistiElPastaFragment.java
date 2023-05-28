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

public class KeistiElPastaFragment extends Fragment {

    EditText editTextNaujasElPastas, editTextKeistiElPastaSlaptazodis;
    Button buttonKeitimasPakeistiElPasta;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_keisti_el_pasta, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editTextNaujasElPastas = view.findViewById(R.id.editTextNaujasElPastas);
        editTextKeistiElPastaSlaptazodis = view.findViewById(R.id.editTextKeistiElPastaSlaptazodis);
        buttonKeitimasPakeistiElPasta = view.findViewById(R.id.buttonKeitimasPakeistiElPasta);
        Bundle bundle = this.getArguments();
        int vartotojasId = bundle.getInt("vartotojasId");
        String prisijungimas = bundle.getString("prisijungimas");

        buttonKeitimasPakeistiElPasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String elPastas = String.valueOf(editTextNaujasElPastas.getText());
                String slaptazodis = String.valueOf(editTextKeistiElPastaSlaptazodis.getText());
                if (!elPastas.equals("") && !slaptazodis.equals("")){
                    updateElPastas(elPastas, slaptazodis, vartotojasId, prisijungimas);
                }else Toast.makeText(getContext(), "Užpildykite laukus", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateElPastas(String elPastas, String slaptazodis, int vartotojasId, String prisijungimas) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                String[] field = new String[4];
                field[0] = "vartotojasId";
                field[1] = "slaptazodis";
                field[2] = "elPastas";
                field[3] = "prisijungimas";
                String[] data = new String[4];
                data[0] = String.valueOf(vartotojasId);
                data[1] = slaptazodis;
                data[2] = elPastas;
                data[3] = prisijungimas;
                PutData putData = new PutData("http://192.168.0.109/DbOperations/updateElPastas.php", "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = putData.getResult();
                        if (!result.equals("Negauti duomenys")) {
                            if (!result.equals("tinklo klaida")) {
                                if (!result.equals("slaptazodis neteisingas")) {
                                    if (!result.equals("el pasto adresas uzimtas")) {
                                        if (result.equals(vartotojasId + "pavyko")) {
                                            Toast.makeText(getContext(), "el. pašto adresas pakeistas", Toast.LENGTH_SHORT).show();
                                            getActivity().getSupportFragmentManager().popBackStackImmediate();
                                        }
                                    } else Toast.makeText(getContext(), "el. pašto adresas uzimtas", Toast.LENGTH_SHORT).show();
                                } else
                                    Toast.makeText(getContext(), "slaptažodis neteisingas", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }
        });
    }

}