package com.diagnostika.automobiliuValdymas;

import android.content.Context;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.diagnostika.R;
import com.diagnostika.automobiliuValdymas.MasinaInformacijaFragment;
import com.diagnostika.automobiliuValdymas.MasinaPridetiFragment;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MasinaFragment extends Fragment {

    private static final String SHARED_PREFS = "sharedPrefs";
    private static final  String AUTOMOBILIS = "automobilis";
    private static final  String VARTOTOJAS = "vartotojas";

    private String pasirinktasAuto;
    private int vartotojasId;

    Spinner pasirinktasAutomobilisSpinner;
    Button buttonPridetiAuto, buttonAutoInfo, buttonPasirinktiAuto;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_masina, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pasirinktasAutomobilisSpinner = view.findViewById(R.id.pasirinktasAutomobilisSpinner);
        buttonAutoInfo = view.findViewById(R.id.buttonAutoInfo);
        buttonPridetiAuto = view.findViewById(R.id.buttonPridetiAuto);
        buttonPasirinktiAuto = view.findViewById(R.id.buttonPasirinktiAuto);


        loadData();

        buttonPridetiAuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new MasinaPridetiFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.flFrangment, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        buttonAutoInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!pasirinktasAutomobilisSpinner.getSelectedItem().equals("prisidėkite automobilį")) {
                    String vartotojasId, pavadinimas;
                    vartotojasId = "1";
                    pavadinimas = String.valueOf(pasirinktasAutomobilisSpinner.getSelectedItem());
                    Bundle bundle = new Bundle();
                    bundle.putString("vartotojasId", vartotojasId);
                    bundle.putString("pavadinimas", pavadinimas);
                    Fragment fragment = new MasinaInformacijaFragment();
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.flFrangment, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                }
                else Toast.makeText(getContext(), "Prisidėkite automobilį", Toast.LENGTH_SHORT).show();
            }
        });
        buttonPasirinktiAuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });

        fillData();

    }

    private void saveData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(AUTOMOBILIS, pasirinktasAutomobilisSpinner.getSelectedItem().toString());
        editor.apply();

        Toast.makeText(getContext(),"Automobilis pasirinktas",Toast.LENGTH_SHORT);
    }

    public void loadData(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        pasirinktasAuto = sharedPreferences.getString(AUTOMOBILIS,"");
        vartotojasId = sharedPreferences.getInt(VARTOTOJAS,0);
    }

    private void fillData() {

        automobiliųPavadinimaiIsDb();

       // ArrayList<String> arrayList = new ArrayList<>();
       // arrayList.add("JAVA");
       // arrayList.add("ANDROID");
      //  arrayList.add("C Language");
       // arrayList.add("CPP Language");
       // arrayList.add("Go Language");
       // ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
       //         android.R.layout.simple_spinner_dropdown_item, arrayList);
        //pasirinktasAutomobilisSpinner.setAdapter(adapter);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        }

    private void automobiliųPavadinimaiIsDb() {

        if (vartotojasId !=0 ) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    String[] field = new String[1];
                    field[0] = "vartotojasId";
                    String[] data = new String[1];
                    data[0] = String.valueOf(vartotojasId);

                    PutData putData = new PutData("http://192.168.0.109/DbOperations/selectAutomobiliuPav.php", "POST", field, data);
                    if (putData.startPut()) {
                        if (putData.onComplete()) {
                            String result = putData.getResult();
                            Log.i("PutData", result); //
                            if (!result.equals("Klaida duomenyse")) {
                                Log.i("tas daiktas: ", result);

                                ArrayList<String> arrayList = new ArrayList<>();

                                try {
                                    JSONArray jsonArray = new JSONArray(result);
                                    for (int i = 1; i < jsonArray.length(); i++) {

                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        String pavadinimas = jsonObject.getString("PAVADINIMAS");
                                        arrayList.add(pavadinimas);

                                    }
                                } catch (JSONException e) {
                                    arrayList.add("prisidėkite automobilį");
                                }

                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                                        android.R.layout.simple_spinner_dropdown_item, arrayList);
                                pasirinktasAutomobilisSpinner.setAdapter(adapter);
                                if (!pasirinktasAuto.equals("")) pasirinktasAutomobilisSpinner.setSelection(((ArrayAdapter) pasirinktasAutomobilisSpinner.getAdapter()).getPosition(pasirinktasAuto));
                            }

                        } else
                            Toast.makeText(getContext(), "Klaida duomenyse", Toast.LENGTH_SHORT).show();
                    }
                }

            });
        } else
            Toast.makeText(getContext(), "Klaida duomenyse", Toast.LENGTH_SHORT).show();

    }

}