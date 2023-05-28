package com.diagnostika.automobiliuValdymas;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MasinaInformacijaRedaguotiFragment extends Fragment {

    EditText editTextRedaguotiMasinaPavadinimas, editTextRedaguotiMasinaModelis, editTextRedaguotiMasinaVariklis, editTextRedaguotiMasinaGalia, editTextRedaguotiMasinaVinKodas;
    Spinner spinnerRedaguotiMasinaGaminotojas;
    Button buttonRedaguotiMasina;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_masina_informacija_redaguoti, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextRedaguotiMasinaGalia = view.findViewById(R.id.editTextRedaguotiMasinaGalia);
        editTextRedaguotiMasinaModelis = view.findViewById(R.id.editTextRedaguotiMasinaModelis);
        editTextRedaguotiMasinaPavadinimas = view.findViewById(R.id.editTextRedaguotiMasinaPavadinimas);
        editTextRedaguotiMasinaVariklis = view.findViewById(R.id.editTextRedaguotiMasinaVariklis);
        editTextRedaguotiMasinaVinKodas = view.findViewById(R.id.editTextRedaguotiMasinaVinKodas);
        spinnerRedaguotiMasinaGaminotojas = view.findViewById(R.id.spinnerRedaguotiMasinaGaminotojas);
        buttonRedaguotiMasina = view.findViewById(R.id.buttonRedaguotiMasina);
        fillSpinnerData();
        fillFieldData();

        buttonRedaguotiMasina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkChanges();
            }
        });


    }

    private void checkChanges() {

        String vartotojasId, pavadinimas;
        Bundle bundle = this.getArguments();
        vartotojasId = bundle.getString("vartotojasId");
        pavadinimas = bundle.getString("pavadinimas");

        if (!vartotojasId.equals("")) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    String[] field = new String[2];
                    field[0] = "vartotojasId";
                    field[1] = "pavadinimas";
                    String[] data = new String[2];
                    data[0] = vartotojasId;
                    data[1] = pavadinimas;

                    PutData putData = new PutData("http://192.168.0.109/DbOperations/selectAutoInformacijaByVIdPav.php", "POST", field, data);
                    if (putData.startPut()) {
                        if (putData.onComplete()) {
                            String result = putData.getResult();
                            if (!result.equals("Klaida duomenyse")) {

                                try {
                                    JSONArray jsonArray = new JSONArray(result);

                                    JSONObject jsonObject = jsonArray.getJSONObject(1);
                                    String pavadinimasDb = jsonObject.getString("PAVADINIMAS");
                                    String gamintojasDb = jsonObject.getString("GAMINTOJAS");
                                    String modelisDb = jsonObject.getString("MODELIS");
                                    String variklioTurisDb = jsonObject.getString("VARIKLIS");
                                    String galiaDb = jsonObject.getString("GALIA");
                                    String vinKodasDb = jsonObject.getString("VIN_NUMERIS");

                                    String pavadinimasChanged = String.valueOf(editTextRedaguotiMasinaPavadinimas.getText());
                                    String gamintojas = String.valueOf(spinnerRedaguotiMasinaGaminotojas.getSelectedItem());
                                    String modelis = String.valueOf(editTextRedaguotiMasinaModelis.getText());
                                    String galia = String.valueOf(editTextRedaguotiMasinaGalia.getText());
                                    String variklioTuris = String.valueOf(editTextRedaguotiMasinaVariklis.getText());
                                    String vinKodas = String.valueOf(editTextRedaguotiMasinaVinKodas.getText());

                                    if (!pavadinimasChanged.equals(pavadinimasDb) ||
                                            !gamintojas.equals(gamintojasDb) ||
                                            !modelis.equals(modelisDb) ||
                                            !(galia.equals(galiaDb)) ||
                                            !variklioTuris.equals(variklioTurisDb) ||
                                            !vinKodas.equals(vinKodasDb)) {
                                        if (!pavadinimas.equals("")) {
                                            if (!gamintojas.equals("")) {

                                                Handler handler = new Handler(Looper.getMainLooper());
                                                handler.post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        String[] field = new String[8];
                                                        field[0] = "vartotojasId";
                                                        field[1] = "galia";
                                                        field[2] = "gamintojas";
                                                        field[3] = "modelis";
                                                        field[4] = "pavadinimas";
                                                        field[5] = "variklis";
                                                        field[6] = "vinNumeris";
                                                        field[7] = "pavadinimasSenas";
                                                        String[] data = new String[8];
                                                        data[0] = vartotojasId;
                                                        data[1] = galia;
                                                        data[2] = gamintojas;
                                                        data[3] = modelis;
                                                        data[4] = pavadinimasChanged;
                                                        data[5] = variklioTuris;
                                                        data[6] = vinKodas;
                                                        data[7] = pavadinimas;


                                                        PutData putData = new PutData("http://192.168.0.109/DbOperations/updateAuto.php", "POST", field, data);
                                                        if (putData.startPut()) {
                                                            if (putData.onComplete()) {
                                                                String result = putData.getResult();
                                                                if (result.equals("pavyko")) {
                                                                    Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
                                                                    getActivity().getSupportFragmentManager().popBackStackImmediate();

                                                                } else {
                                                                    Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        }
                                                    }
                                                });

                                            } else
                                                Toast.makeText(getContext(), "Gamintojas privalomas", Toast.LENGTH_SHORT).show();

                                        }else
                                            Toast.makeText(getContext(), "Pavadinimas privalomas", Toast.LENGTH_SHORT).show();

                                    }else
                                        getActivity().getSupportFragmentManager().popBackStackImmediate();
                                }catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }


                            }

                        } else
                            Toast.makeText(getContext(), "Klaida duomenyse", Toast.LENGTH_SHORT).show();
                    }
                }

            });
        } else
            Toast.makeText(getContext(), "Klaida duomenyse", Toast.LENGTH_SHORT).show();

    }

    private void fillFieldData() {

        String vartotojasId, pavadinimas;
        Bundle bundle = this.getArguments();
        vartotojasId = bundle.getString("vartotojasId");
        pavadinimas = bundle.getString("pavadinimas");

        if (!vartotojasId.equals("")) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    String[] field = new String[2];
                    field[0] = "vartotojasId";
                    field[1] = "pavadinimas";
                    String[] data = new String[2];
                    data[0] = vartotojasId;
                    data[1] = pavadinimas;

                    PutData putData = new PutData("http://192.168.0.109/DbOperations/selectAutoInformacijaByVIdPav.php", "POST", field, data);
                    if (putData.startPut()) {
                        if (putData.onComplete()) {
                            String result = putData.getResult();
                            if (!result.equals("Klaida duomenyse")) {
                                try {
                                    JSONArray jsonArray = new JSONArray(result);

                                    JSONObject jsonObject = jsonArray.getJSONObject(1);
                                    String pavadinimasSelect = jsonObject.getString("PAVADINIMAS");
                                    String gamintojas = jsonObject.getString("GAMINTOJAS");
                                    String modelis = jsonObject.getString("MODELIS");
                                    String variklioTuris = jsonObject.getString("VARIKLIS");
                                    String galia = jsonObject.getString("GALIA");
                                    String vinKodas = jsonObject.getString("VIN_NUMERIS");

                                    editTextRedaguotiMasinaPavadinimas.setText(pavadinimasSelect);
                                    spinnerRedaguotiMasinaGaminotojas.setSelection(((ArrayAdapter)spinnerRedaguotiMasinaGaminotojas.getAdapter()).getPosition(gamintojas));
                                    editTextRedaguotiMasinaModelis.setText(modelis);
                                    editTextRedaguotiMasinaGalia.setText(galia+"");
                                    editTextRedaguotiMasinaVariklis.setText(variklioTuris);
                                    editTextRedaguotiMasinaVinKodas.setText(vinKodas);

                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }


                            }

                        } else
                            Toast.makeText(getContext(), "Klaida duomenyse", Toast.LENGTH_SHORT).show();
                    }
                }

            });
        } else
            Toast.makeText(getContext(), "Klaida duomenyse", Toast.LENGTH_SHORT).show();
    }


    private void fillSpinnerData() {

        ArrayList<String> arrayList = new ArrayList<>();
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
        spinnerRedaguotiMasinaGaminotojas.setAdapter(adapter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    }

}