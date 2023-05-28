package com.diagnostika.paskyrosValdymas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.diagnostika.MainActivity;
import com.diagnostika.R;
import com.diagnostika.paskyrosValdymas.KeistiElPastaFragment;
import com.diagnostika.paskyrosValdymas.KeistiSlaptazodiFragment;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class PaskyraFragment extends Fragment {

    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String AUTOMOBILIS = "automobilis";
    private static final String VARTOTOJAS = "vartotojas";

    private String pasirinktasAuto;
    private int vartotojasId;
    TextView textPaskyraVardas, textPaskyraPrisijungimas, textPaskyraELPastas;
    Button buttonAtsijungti, buttonPakeistiSlaptazodi, buttonPakeistiElPasta, buttonIstrintiPaskyra;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_paskyra, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        buttonAtsijungti = view.findViewById(R.id.buttonAtsijungti);
        buttonPakeistiElPasta = view.findViewById(R.id.buttonPakeistiElPasta);
        buttonPakeistiSlaptazodi = view.findViewById(R.id.buttonPakeistiSlaptazodi);
        buttonIstrintiPaskyra = view.findViewById(R.id.buttonIstrintiPaskyra);
        textPaskyraELPastas = view.findViewById(R.id.textPaskyraELPastas);
        textPaskyraPrisijungimas = view.findViewById(R.id.textPaskyraPrisijungimas);
        textPaskyraVardas = view.findViewById(R.id.textPaskyraVardas);
        loadData();
        selectVartotojas();
        buttonAtsijungti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                startActivity(intent);
                getActivity().finish();
            }
        });
        buttonPakeistiSlaptazodi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundleSend = new Bundle();
                bundleSend.putInt("vartotojasId", vartotojasId);
                bundleSend.putString("prisijungimas", String.valueOf(textPaskyraPrisijungimas.getText().toString().substring(15)));
                Fragment fragment = new KeistiSlaptazodiFragment();
                fragment.setArguments(bundleSend);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.flFrangment, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        buttonPakeistiElPasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundleSend = new Bundle();
                bundleSend.putInt("vartotojasId", vartotojasId);
                bundleSend.putString("prisijungimas", String.valueOf(textPaskyraPrisijungimas.getText().toString().substring(15)));
                Fragment fragment = new KeistiElPastaFragment();
                fragment.setArguments(bundleSend);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.flFrangment, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        buttonIstrintiPaskyra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundleSend = new Bundle();
                bundleSend.putInt("vartotojasId", vartotojasId);
                bundleSend.putString("prisijungimas", String.valueOf(textPaskyraPrisijungimas.getText().toString().substring(15)));
                Fragment fragment = new IstrintiPaskyraFragment();
                fragment.setArguments(bundleSend);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.flFrangment, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    private void selectVartotojas() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                String[] field = new String[1];
                field[0] = "vartotojoId";
                String[] data = new String[1];
                data[0] = String.valueOf(vartotojasId);
                PutData putData = new PutData("http://192.168.0.109/DbOperations/selectVartotojas.php", "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = putData.getResult();
                        if (!result.equals("tinklo klaida")) {
                            if (!result.equals("nera")) {
                                try {
                                    JSONArray jsonArray = new JSONArray(result);
                                    String vardas = null, prisijungimas = null, elPastas = null;
                                    for (int i = 1; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        vardas = jsonObject.getString("VARDAS");
                                        prisijungimas = jsonObject.getString("PRISIJUNGIMAS");
                                        elPastas = jsonObject.getString("ELEKTRONINIO_PASTO_ADRESAS");
                                    }
                                    textPaskyraELPastas.setText(textPaskyraELPastas.getText() + elPastas);
                                    textPaskyraPrisijungimas.setText(textPaskyraPrisijungimas.getText() + prisijungimas);
                                    textPaskyraVardas.setText(textPaskyraVardas.getText() + vardas);
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }

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

    public void loadData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        pasirinktasAuto = sharedPreferences.getString(AUTOMOBILIS, "");
        vartotojasId = sharedPreferences.getInt(VARTOTOJAS, 0);
    }
}