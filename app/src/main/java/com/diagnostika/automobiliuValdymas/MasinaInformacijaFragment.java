package com.diagnostika.automobiliuValdymas;

import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.diagnostika.MainActivity;
import com.diagnostika.R;
import com.diagnostika.koduTikrinimas.PaieskaRezultatai;
import com.diagnostika.paskyrosValdymas.Prisijungimas;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Objects.DiagnostikosKodas;

public class MasinaInformacijaFragment extends Fragment {

    TextView textMasinosInformacijaPavadinimas, textMasinosInformacijaModelis, textMasinosInformacijaVariklioTuris, textMasinosInformacijaGalia, textMasinosInformacijaVinKodas, textMasinosInformacijaGamintojas;

    Button buttonRedaguotiInformacija, buttonPasalintiAutomobili;

    LinearLayout linearLayoutAutomobilisIstorijaTurinys, getLinearLayoutAutomobilisAtaskaitosTurinys;

    int automobilisId;
    String vartotojasId;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_masina_informacija, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String pavadinimas;
        Bundle bundle = this.getArguments();
        vartotojasId = bundle.getString("vartotojasId");
        pavadinimas = bundle.getString("pavadinimas");
        textMasinosInformacijaPavadinimas = view.findViewById(R.id.textMasinosInformacijaPavadinimas);
        textMasinosInformacijaGamintojas = view.findViewById(R.id.textMasinosInformacijaGamintojas);
        textMasinosInformacijaModelis = view.findViewById(R.id.textMasinosInformacijaModelis);
        textMasinosInformacijaVariklioTuris = view.findViewById(R.id.textMasinosInformacijaVariklioTuris);
        textMasinosInformacijaGalia = view.findViewById(R.id.textMasinosInformacijaGalia);
        textMasinosInformacijaVinKodas = view.findViewById(R.id.textMasinosInformacijaVinKodas);
        buttonRedaguotiInformacija = view.findViewById(R.id.buttonRedaguotiInformacija);
        buttonPasalintiAutomobili = view.findViewById(R.id.buttonPasalintiAutomobili);
        linearLayoutAutomobilisIstorijaTurinys = view.findViewById(R.id.linearLayoutAutomobilisIstorijaTurinys);
        getLinearLayoutAutomobilisAtaskaitosTurinys = view.findViewById(R.id.linearLayoutAutomobilisAtaskaitosTurinys);

        fillData();


        buttonRedaguotiInformacija.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundleSend = new Bundle();
                bundleSend.putString("vartotojasId", vartotojasId);
                bundleSend.putString("pavadinimas", pavadinimas);
                Fragment fragment = new MasinaInformacijaRedaguotiFragment();
                fragment.setArguments(bundleSend);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.flFrangment, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        buttonPasalintiAutomobili.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteAuto(vartotojasId, pavadinimas);

            }
        });

    }

    private void deleteAuto(String vartotojasId, String pavadinimas) {

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

                    PutData putData = new PutData("http://192.168.0.109/DbOperations/deleteAuto.php", "POST", field, data);
                    if (putData.startPut()) {
                        if (putData.onComplete()) {
                            String result = putData.getResult();
                            if (!result.equals("Klaida duomenyse")) {
                                Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
                                getActivity().getSupportFragmentManager().popBackStackImmediate();

                            } else
                                Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        } else
            Toast.makeText(getContext(), "Klaida su prisijungimu", Toast.LENGTH_SHORT).show();
    }


    private void fillData() {

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
                                    automobilisId = jsonObject.getInt("AUTOMOBILIO_ID");
                                    String pavadinimasSelect = jsonObject.getString("PAVADINIMAS");
                                    String gamintojas = jsonObject.getString("GAMINTOJAS");
                                    String modelis = jsonObject.getString("MODELIS");
                                    String variklioTuris = jsonObject.getString("VARIKLIS");
                                    String galia = jsonObject.getString("GALIA");
                                    String vinKodas = jsonObject.getString("VIN_NUMERIS");

                                    textMasinosInformacijaPavadinimas.setText("Pavadinimas: " + pavadinimasSelect);
                                    textMasinosInformacijaGamintojas.setText("Gamintojas: " + gamintojas);
                                    textMasinosInformacijaModelis.setText("Modelis: " + modelis);
                                    textMasinosInformacijaGalia.setText("Galia: " + galia);
                                    textMasinosInformacijaVariklioTuris.setText("Variklio Tūris: " + variklioTuris);
                                    textMasinosInformacijaVinKodas.setText("VIN numeris: " + vinKodas);


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

        fillIstorijaSarasas();
        fillAtaskaitosSarasas();
    }

    private void fillAtaskaitosSarasas() {

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                String[] field = new String[2];
                field[0] = "automobilioId";
                field[1] = "vartotojasId";
                String[] data = new String[2];
                data[0] = String.valueOf(automobilisId);
                data[1] = vartotojasId;

                PutData putData = new PutData("http://192.168.0.109/DbOperations/selectAtaskaitos.php", "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = putData.getResult();
                        if (!result.equals("Negauti duomenys")) {
                            if (!result.equals("tinklo klaida")) {
                                if (!result.equals("nera")) {

                                    try {
                                        JSONArray jsonArray = new JSONArray(result);
                                        for (int i = 1; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            String sukurimoLaikas = jsonObject.getString("SUKURIMO_LAIKAS");
                                            addAutomobilisAtaskaitaItem(sukurimoLaikas);
                                        }

                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }
                                }else addAutomobilisAtaskaitaItem("Nėra išsaugotų ataskaitų");
                            }
                        }
                    }
                }
            }

        });


    }

    private void addAutomobilisAtaskaitaItem(String paieskosData) {
        TextView textData;

        final View aa = getLayoutInflater().inflate(R.layout.susije_item, null, false);

        textData = aa.findViewById(R.id.textViewSusijesKodas);

        textData.setText(paieskosData);

        if (!paieskosData.equals("Nėra išsaugotų ataskaitų")) {

            aa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextView textDatairlaikas;
                    textDatairlaikas = aa.findViewById(R.id.textViewSusijesKodas);
                    String datairLaikas = (String) textDatairlaikas.getText();
                    uzklausaAtaskaitaInfo(datairLaikas);


                }
            });
        }

        getLinearLayoutAutomobilisAtaskaitosTurinys.addView(aa);

    }

    private void uzklausaAtaskaitaInfo(String datairLaikas) {

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {

                String[] field = new String[2];
                field[0] = "dataIrLaikas";
                field[1] = "vartotojasId";
                String[] data = new String[2];
                data[0] = datairLaikas;
                data[1] = vartotojasId;
                PutData putData = new PutData("http://192.168.0.109/DbOperations/selectAtaskaitaInfo.php", "POST", field, data);

                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = putData.getResult();
                        if (!result.equals("Negauti duomenys")) {
                            if (!result.equals("tinklo klaida")) {
                                if(!result.equals("ataskaita nerasta")) {
                                    if(!result.equals("kodu nera")) {
                                        try {
                                            JSONArray jsonArray = new JSONArray(result);
                                            String kodai = "";
                                            int koduKiekis = 0;
                                            for (int i = 1; i < jsonArray.length(); i++) {
                                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                String OBDReiksme = jsonObject.getString("OBD_REIKSME");
                                                if (i>1) kodai += (",");
                                                kodai += ("'" + OBDReiksme + "'");
                                                koduKiekis++;
                                            }
                                            uzklausaKoduSarasas(kodai,koduKiekis);
                                        } catch (JSONException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

        });
    }

    private void fillIstorijaSarasas() {

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                String[] field = new String[1];
                field[0] = "automobilisId";
                String[] data = new String[1];
                data[0] = String.valueOf(automobilisId);
                PutData putData = new PutData("http://192.168.0.109/DbOperations/selectAutomobilioIstorija.php", "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = putData.getResult();
                        if (!result.equals("klaida duomenyse")) {
                            if (!result.equals("tinklo klaida")) {
                                if (!result.equals("nera")) {

                                    try {
                                        JSONArray jsonArray = new JSONArray(result);
                                        for (int i = 1; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                                            String OBDReiksme = jsonObject.getString("OBD_REIKSME");
                                            String paieskosData = jsonObject.getString("DATA");
                                            addAutomobilisIstorijaItem(OBDReiksme, paieskosData);
                                        }
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }

                                } else
                                    addAutomobilisIstorijaItem("ieškotų gedimų nėra", "");
                            }
                        }
                    }
                }
            }
        });


    }

    private void addAutomobilisIstorijaItem(String obdReiksme, String paieskosData) {
        TextView textIstorijaKodasPav, textIstorijaData;

        final View aa = getLayoutInflater().inflate(R.layout.istorija_item, null, false);

        textIstorijaKodasPav = aa.findViewById(R.id.textIstorijaKodasPav);
        textIstorijaData = aa.findViewById(R.id.textIstorijaData);

        textIstorijaKodasPav.setText(obdReiksme);
        textIstorijaData.setText("Ieškota: " + paieskosData);

        aa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uzklausaKodas(obdReiksme);

            }
        });

        linearLayoutAutomobilisIstorijaTurinys.addView(aa);

    }

    private void uzklausaKodas(String OBDKodas) {


        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {

                String[] field = new String[1];
                field[0] = "OBDkodas";
                String[] data = new String[1];
                data[0] = OBDKodas;
                PutData putData = new PutData("http://192.168.0.109/DbOperations/selectKodas.php", "POST", field, data);

                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = putData.getResult();
                        ;
                        if (!result.equals("Klaida duomenyse")) {
                            if (!result.equals("Kodas " + OBDKodas + " neregistruotas")) {

                                Intent intent = new Intent(getActivity(), PaieskaRezultatai.class);
                                intent.putExtra("rezultatas", result);
                                intent.putExtra("kiekis", 1);
                                startActivity(intent);

                            } else
                                Toast.makeText(getContext(), "Kodas " + OBDKodas + " neregistruotas", Toast.LENGTH_SHORT).show();
                            ;
                        } else
                            Toast.makeText(getContext(), "klaida duomneyse", Toast.LENGTH_SHORT).show();
                        ;
                    }
                }
            }

        });

    }

    private void uzklausaKoduSarasas(String kodai, int koduKiekis) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {

                String[] field = new String[1];
                field[0] = "kodai";
                String[] data = new String[1];
                data[0] = kodai;
                PutData putData = new PutData("http://192.168.0.109/DbOperations/selectKoduSarasas.php", "POST", field, data);

                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = putData.getResult();
                        ;
                        if (!result.equals("Klaida duomenyse")) {
                            if (!result.equals("Kodai neregistruoti")) {

                                Intent intent = new Intent(getActivity(), PaieskaRezultatai.class);
                                intent.putExtra("rezultatas", result);
                                intent.putExtra("kodai", kodai);
                                intent.putExtra("kiekis", koduKiekis);
                                intent.putExtra("ataskaita", true);

                                startActivity(intent);

                            } else
                                Toast.makeText(getContext(), "Kodai neregistruoti", Toast.LENGTH_SHORT).show();
                            ;
                        } else
                            Toast.makeText(getContext(), "klaida duomneyse", Toast.LENGTH_SHORT).show();
                        ;
                    }
                }
            }

        });

    }


}
