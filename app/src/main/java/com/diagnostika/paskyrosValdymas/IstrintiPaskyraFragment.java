package com.diagnostika.paskyrosValdymas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.diagnostika.MainActivity;
import com.diagnostika.R;
import com.vishnusivadas.advanced_httpurlconnection.PutData;


public class IstrintiPaskyraFragment extends Fragment {

    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String AUTOMOBILIS = "automobilis";
    private static final String VARTOTOJAS = "vartotojas";
    Button buttonPatvirtintiIstrintiPaskyra;
    EditText editTextIstrintiPaskyraSlaptazodis;

    public IstrintiPaskyraFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_istrinti_paskyra, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editTextIstrintiPaskyraSlaptazodis = view.findViewById(R.id.editTextIstrintiPaskyraSlaptazodis);
        buttonPatvirtintiIstrintiPaskyra = view.findViewById(R.id.buttonPatvirtintiIstrintiPaskyra);
        Bundle bundle = this.getArguments();
        int vartotojasId = bundle.getInt("vartotojasId");
        String prisijungimas = bundle.getString("prisijungimas");

        buttonPatvirtintiIstrintiPaskyra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String slaptazodis = String.valueOf(editTextIstrintiPaskyraSlaptazodis.getText());
                if (!slaptazodis.equals("")) {
                    uzklausaIstrintiPaskyra(vartotojasId, prisijungimas, slaptazodis);
                } else
                    Toast.makeText(getContext(), "Įveskite slaptažodį", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uzklausaIstrintiPaskyra(int vartotojasId, String prisijungimas, String slaptazodis) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                String[] field = new String[3];
                field[0] = "vartotojasId";
                field[1] = "prisijungimas";
                field[2] = "slaptazodis";
                String[] data = new String[3];
                data[0] = String.valueOf(vartotojasId);
                data[1] = prisijungimas;
                data[2] = slaptazodis;
                PutData putData = new PutData("http://192.168.0.109/DbOperations/deleteVartotojas.php", "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = putData.getResult();
                        if (!result.equals("Negauti duomenys")) {
                            if (!result.equals("tinklo klaida")) {
                                if (!result.equals("slaptazodis neteisingas")) {
                                    if (result.equals("vartotojas ištrintas")) {
                                        Toast.makeText(getContext(), "paskyra ištrinta", Toast.LENGTH_SHORT).show();
                                        signOut();
                                        Intent intent = new Intent(getActivity(), MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                                        startActivity(intent);
                                        getActivity().finish();
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
    private void signOut() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(VARTOTOJAS, 0);
        editor.putString(AUTOMOBILIS, "");
        editor.apply();
        Toast.makeText(getContext(), "Atsijungta", Toast.LENGTH_SHORT);
    }

}