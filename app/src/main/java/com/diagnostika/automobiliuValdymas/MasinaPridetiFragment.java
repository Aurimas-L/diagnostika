package com.diagnostika.automobiliuValdymas;

import android.annotation.SuppressLint;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.diagnostika.R;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.util.ArrayList;


public class MasinaPridetiFragment extends Fragment {

    EditText editTextPridetiMasinaPavadinimas, editTextPridetiMasinaModelis, editTextPridetiMasinaVariklis, editTextPridetiMasinaGalia, editTextPridetiMasinaVinKodas;
    Spinner spinnerPridetiMasinaGaminotojas;
    Button buttonPridėtiMasina;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_masina_prideti, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editTextPridetiMasinaGalia = view.findViewById(R.id.editTextPridetiMasinaGalia);
        editTextPridetiMasinaModelis = view.findViewById(R.id.editTextPridetiMasinaModelis);
        editTextPridetiMasinaPavadinimas = view.findViewById(R.id.editTextPridetiMasinaPavadinimas);
        editTextPridetiMasinaVariklis = view.findViewById(R.id.editTextPridetiMasinaVariklis);
        editTextPridetiMasinaVinKodas = view.findViewById(R.id.editTextPridetiMasinaVinKodas);
        spinnerPridetiMasinaGaminotojas = view.findViewById(R.id.spinnerPridetiMasinaGaminotojas);
        buttonPridėtiMasina = view.findViewById(R.id.buttonPridėtiMasina);
        fillSpinnerData();

        buttonPridėtiMasina.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SuspiciousIndentation")
            @Override
            public void onClick(View view) {
                if(TikrintiPrivalomusDuomenis(String.valueOf(editTextPridetiMasinaPavadinimas.getText())))
                pridetiAutomobili();
            }
        });



    }

    private boolean TikrintiPrivalomusDuomenis(String pavadinimas) {
        String gamintojas = spinnerPridetiMasinaGaminotojas.getSelectedItem().toString();
        Boolean result = false;
        if (!pavadinimas.equals("")){
            if (!gamintojas.equals("pasirinkite gamintoja")){
                result = true;
            }else
                Toast.makeText(getContext(), "Pasirinkite Gamintoja", Toast.LENGTH_SHORT).show();
        }else
            Toast.makeText(getContext(), "Iveskite Pavadinima", Toast.LENGTH_SHORT).show();
        return result;
    }

    private void pridetiAutomobili() {

        String  vartotojasId, galia, modelis, pavadinimas, variklis, vinkodas, gamintojas;
        galia =  String.valueOf(editTextPridetiMasinaGalia.getText());
        modelis = String.valueOf(editTextPridetiMasinaModelis.getText());
        pavadinimas = String.valueOf(editTextPridetiMasinaPavadinimas.getText());
        variklis = String.valueOf(editTextPridetiMasinaVariklis.getText());
        vinkodas = String.valueOf(editTextPridetiMasinaVinKodas.getText());
        gamintojas = String.valueOf(spinnerPridetiMasinaGaminotojas.getSelectedItem());
        vartotojasId = "1";

                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        String[] field = new String[7];
                        field[0] = "vartotojasId";
                        field[1] = "galia";
                        field[2] = "gamintojas";
                        field[3] = "modelis";
                        field[4] = "pavadinimas";
                        field[5] = "variklis";
                        field[6] = "vinNumeris";
                        String[] data = new String[7];
                        data[0] = vartotojasId;
                        data[1] = galia;
                        data[2] = gamintojas;
                        data[3] = modelis;
                        data[4] = pavadinimas;
                        data[5] = variklis;
                        data[6] = vinkodas;


                        PutData putData = new PutData("http://192.168.0.109/DbOperations/insertAutomobilis.php", "POST", field, data);
                        if (putData.startPut()) {
                            if (putData.onComplete()) {
                                String result = putData.getResult();
                                if(result.equals("Automobilis pridetas")){
                                    Toast.makeText(getContext(), "Automobilis pridėtas", Toast.LENGTH_SHORT).show();
                                    getActivity().getSupportFragmentManager().popBackStackImmediate();

                                }
                                else {
                                    Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                });



    }

    private void fillSpinnerData() {

         ArrayList<String> arrayList = new ArrayList<>();
         arrayList.add("pasirinkite gamintoja");
         arrayList.add("Lexus");
         arrayList.add("BMW");
         arrayList.add("TOYOTA");
         arrayList.add("SUBARU");
         arrayList.add("MAZDA");
        arrayList.add("MERCEDES");
        arrayList.add("HONDA");
        arrayList.add("NISAN");
        arrayList.add("OPEL");
        arrayList.add("AUDI");
        arrayList.add("volkswagen");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                 android.R.layout.simple_spinner_dropdown_item, arrayList);
        spinnerPridetiMasinaGaminotojas.setAdapter(adapter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    }


}