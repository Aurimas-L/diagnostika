package com.diagnostika.koduTikrinimas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import com.diagnostika.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Objects.DiagnostikosKodas;

public class PaieskaRezultatai extends AppCompatActivity {


    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String AUTOMOBILIS = "automobilis";
    private static final String VARTOTOJAS = "vartotojas";

    private String pasirinktasAuto;
    private int vartotojasId;
    private Short servisoPaskirtis;
    LinearLayout listOBDKodai;
    int kiekis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paieska_rezultatai);
        loadData();

        listOBDKodai = findViewById(R.id.listOBDKodai);

        Intent intent = getIntent();
        String result = intent.getStringExtra("rezultatas");
        kiekis = intent.getIntExtra("kiekis", 1);
        String kodai = intent.getStringExtra("kodai");
        boolean arAtaskaita = intent.getBooleanExtra("ataskaita", false);

        if (kiekis == 1) {
            DiagnostikosKodas diagnostikosKodas = jsonToKodas(result);
            addKodas(diagnostikosKodas);
            pridetiNuorodaZemelapis();
        }
        if (kiekis > 1) {
            DiagnostikosKodas diagnostikosKodai[] = jsonToKodai(result);
            formuotiKoduSarasas(diagnostikosKodai);
            pridetiNuorodaZemelapis();
            if (!arAtaskaita && !arAutomobilisPasirinktas()) pridetiAtaskaitosIssaugojima(kodai, kiekis);
            pridetiPDFFormavima();
        }


    }

    private void pridetiPDFFormavima() {
        final View bb = getLayoutInflater().inflate(R.layout.mygtukas_item, null, false);
        Button button = bb.findViewById(R.id.mygtukas);
        button.setText("Formuoti PDF dokumentą");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PaieskaRezultatai.this, "Neveikia", Toast.LENGTH_SHORT).show();
            }
        });

        listOBDKodai.addView(bb);
    }

    private void pridetiAtaskaitosIssaugojima(String kodai, int koduKiekis) {
        final View bb = getLayoutInflater().inflate(R.layout.mygtukas_item, null, false);
        Button button = bb.findViewById(R.id.mygtukas);
        button.setText("Iššsaugoti ataskaitą");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertAtaskaita(kodai, koduKiekis, button);
            }
        });

        listOBDKodai.addView(bb);
    }

    private void insertAtaskaita(String kodai, int koduKiekis, Button button) {

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                int ataskaitaID = 0;
                String[] field = new String[3];
                field[0] = "pasirinktasAuto";
                field[1] = "vartotojasId";
                field[2] = "gedimuKiekis";
                String[] data = new String[3];
                data[0] = pasirinktasAuto;
                data[1] = String.valueOf(vartotojasId);
                data[2] = String.valueOf(koduKiekis);
                PutData putData = new PutData("http://192.168.0.109/DbOperations/insertAtaskaita.php", "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = putData.getResult();
                        if (!result.equals("Negauti duomenys")) {
                            if (!result.equals("tinklo klaida")) {
                                if (!result.equals("klaida")) {
                                    if (!result.equals("nepavyko")) {
                                        Toast.makeText(getApplicationContext(), "ataskaita išsaugota: " + pasirinktasAuto + "", Toast.LENGTH_SHORT).show();
                                        button.setVisibility(View.GONE);
                                        ataskaitaID = Integer.parseInt(result);
                                    } else Log.i("klaida išsaugant ataskaitą", result);
                                } else Log.i("klaida ieškant automobilio", result);
                            }
                        }
                    }
                }
                if (ataskaitaID != 0) {
                    String kodaiList[] = kodai.split(",");
                    for (int i = 0; i < kodaiList.length; i++) {
                        String[] fieldKodas = new String[2];
                        fieldKodas[0] = "ataskaitaId";
                        fieldKodas[1] = "obdReiksme";
                        String[] dataKodas = new String[2];
                        dataKodas[0] = String.valueOf(ataskaitaID);
                        dataKodas[1] = String.valueOf(kodaiList[i]);
                        PutData putDataKodai = new PutData("http://192.168.0.109/DbOperations/insertAtaskaitaKodas.php", "POST", fieldKodas, dataKodas);
                        if (putDataKodai.startPut()) {
                            if (putDataKodai.onComplete()) {
                                String result = putDataKodai.getResult();
                            }
                        }
                    }
                }
            }
        });

    }

    private void pridetiNuorodaZemelapis() {

        final View bb = getLayoutInflater().inflate(R.layout.mygtukas_item, null, false);
        Button button = bb.findViewById(R.id.mygtukas);
        button.setText("Ieškoti autoserviso");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = "geo:0,0?z=10&q=";
                if (servisoPaskirtis == 1) query += "Specializuotas autoservisas";
                else if (servisoPaskirtis == 2) query += "automobilių elektrikas";
                else if (servisoPaskirtis == 3) query += "automobilių važiuoklės remontas";
                else if (servisoPaskirtis == 4) query += "automobilių variklių remontas";
                else query += "autoservisas";

                Uri gmmIntentUri = Uri.parse(query);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        listOBDKodai.addView(bb);
    }

    private void formuotiKoduSarasas(DiagnostikosKodas[] diagnostikosKodai) {

        for (int i = 0; i < kiekis; i++)
            addKodas(diagnostikosKodai[i]);
    }


    private void addKodas(DiagnostikosKodas diagnostikosKodas) {
        TextView textKodasPavadinimas, textArIeskotas, textKritiskumasPavadinimas, textKritiskumasAprasymas, textKodasAprasymas,
                textGedimoPriezastisAprasymas, textTvarkymoAprasymas, textSusijeGedimaiAprasymas,
                textKainaAprasymas, textKainaPavadinimas, textInformacijaAtnaujinta;
        LinearLayout listViewSusijeKodai;


        final View aa = getLayoutInflater().inflate(R.layout.kodas_item, null, false);

        textKodasPavadinimas = aa.findViewById(R.id.textKodasPavadinimas);
        textArIeskotas = aa.findViewById(R.id.textArIeskotas);
        textKritiskumasPavadinimas = aa.findViewById(R.id.textKritiskumasPavadinimas);
        textKritiskumasAprasymas = aa.findViewById(R.id.textKritiskumasAprasymas);
        textKodasAprasymas = aa.findViewById(R.id.textKodasAprasymas);
        textGedimoPriezastisAprasymas = aa.findViewById(R.id.textGedimoPriezastisAprasymas);
        textTvarkymoAprasymas = aa.findViewById(R.id.textTvarkymoAprasymas);
        textSusijeGedimaiAprasymas = aa.findViewById(R.id.textSusijeGedimaiAprasymas);
        textKainaPavadinimas = aa.findViewById(R.id.textKainaPavadinimas);
        textKainaAprasymas = aa.findViewById(R.id.textKainaAprasymas);
        textInformacijaAtnaujinta = aa.findViewById(R.id.textInformacijaAtnaujinta);
        listViewSusijeKodai = aa.findViewById(R.id.listViewSusijeKodai);

        if (diagnostikosKodas.getOBDKodas().equals(""));

        textKodasPavadinimas.setText(diagnostikosKodas.getOBDKodas());
        if (arAutomobilisPasirinktas())
            uzklausaPatikrintiIstorija(diagnostikosKodas.getOBDKodas(), textArIeskotas);
        else textArIeskotas.setVisibility(View.GONE);
        textKritiskumasPavadinimas.setText("Kritiškumas: " + diagnostikosKodas.getKritiskumas());
        if (diagnostikosKodas.getKritiskumas().equals("mažas"))
            textKritiskumasAprasymas.setText("Kodo kritiškumas yra mažas, paprastai tokie gedimai netrukdo automobilio eksploatacijai.");
        if (diagnostikosKodas.getKritiskumas().equals("vidutinis"))
            textKritiskumasAprasymas.setText("Kodo kritiškumas vidutinis, patartina vykti į autoservisą.");
        if (diagnostikosKodas.getKritiskumas().equals("didelis"))
            textKritiskumasAprasymas.setText("Kodo kritiškumas didelis, automobilio eksloatacija gali turėti rimtų padarinių.");
        textKodasAprasymas.setText(diagnostikosKodas.getAprasymas());
        textGedimoPriezastisAprasymas.setText(diagnostikosKodas.getPriezastis());
        textTvarkymoAprasymas.setText(diagnostikosKodas.getTvarkymas());
        textSusijeGedimaiAprasymas.setText(diagnostikosKodas.getSusijeGedimai());
        textKainaPavadinimas.setText("galima tvarkymo kaina: " + diagnostikosKodas.getKaina());
        textKainaAprasymas.setText("kaina gali skirtis priklausomai nuo gedimo priežąsties");
        textInformacijaAtnaujinta.setText("informacija atnaujinta: " + diagnostikosKodas.getAtnaujintas());

        addSusijeKodai(diagnostikosKodas.getOBDKodas(), listViewSusijeKodai);

        listOBDKodai.addView(aa);

        if (arAutomobilisPasirinktas()) {
            uzklausaPridetiIstorija(diagnostikosKodas.getOBDKodas());
        }

    }

    private void uzklausaPatikrintiIstorija(String obdKodas, TextView textArIeskotas) {

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                String[] field = new String[3];
                field[0] = "vartotojasId";
                field[1] = "pasirinktasAuto";
                field[2] = "OBDKodas";
                String[] data = new String[3];
                data[0] = String.valueOf(vartotojasId);
                data[1] = pasirinktasAuto;
                data[2] = obdKodas;
                PutData putData = new PutData("http://192.168.0.109/DbOperations/selectIstorija.php", "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = putData.getResult();
                        if (!result.equals("Negauti duomenys")) {
                            if (!result.equals("nera")) {
                                try {
                                    JSONArray jsonArray = new JSONArray(result);
                                    String OBD, istorijosData = null;
                                    for (int i = 1; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        istorijosData = jsonObject.getString("DATA");
                                    }
                                    textArIeskotas.setText("Šis kodas ieškotas: " + istorijosData);
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }

                            } else textArIeskotas.setVisibility(View.GONE);
                        } else textArIeskotas.setVisibility(View.GONE);
                    } else textArIeskotas.setVisibility(View.GONE);
                }
            }
        });

    }

    private void uzklausaPridetiIstorija(String OBDKodas) {

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                String[] field = new String[3];
                field[0] = "vartotojasId";
                field[1] = "pasirinktasAuto";
                field[2] = "OBDKodas";
                String[] data = new String[3];
                data[0] = String.valueOf(vartotojasId);
                data[1] = pasirinktasAuto;
                data[2] = OBDKodas;

                PutData putData = new PutData("http://192.168.0.109/DbOperations/insertIstorija.php", "POST", field, data);
                putData.startPut();
            }
        });


    }

    private boolean arAutomobilisPasirinktas() {
        boolean result = false;
        if (vartotojasId != 0 && !pasirinktasAuto.equals("")) result = true;
        return result;
    }

    private void addSusijeKodai(String obdKodas, LinearLayout listViewSusijeKodai) {


        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                String[] field = new String[1];
                field[0] = "OBDKodas";
                String[] data = new String[1];
                data[0] = obdKodas;
                PutData putData = new PutData("http://192.168.0.109/DbOperations/selectSusijeKodai.php", "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = putData.getResult();
                        if (!result.equals("Klaida duomenyse")) {
                            ArrayList<String> arrayList = new ArrayList<>();

                            try {
                                JSONArray jsonArray = new JSONArray(result);
                                for (int i = 1; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String OBD = jsonObject.getString("DIA_OBD_REIKSME");

                                    final View bb = getLayoutInflater().inflate(R.layout.susije_item, null, false);
                                    TextView textView;
                                    textView = bb.findViewById(R.id.textViewSusijesKodas);
                                    textView.setText(OBD);
                                    bb.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            uzklausaKodas(OBD);
                                        }
                                    });
                                    listViewSusijeKodai.addView(bb);
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }

                        }
                    }
                }
            }

        });

    }

    private void uzklausaKodas(String obd) {

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {

                String[] field = new String[1];
                field[0] = "OBDkodas";
                String[] data = new String[1];
                data[0] = obd;
                PutData putData = new PutData("http://192.168.0.109/DbOperations/selectKodas.php", "POST", field, data);

                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = putData.getResult();
                        ;
                        if (!result.equals("Klaida duomenyse")) {
                            if (!result.equals("Kodas " + obd + " neregistruotas")) {
                                Intent intent = new Intent(getApplicationContext(), PaieskaRezultatai.class);
                                intent.putExtra("rezultatas", result);
                                startActivity(intent);
                            } else
                                Toast.makeText(getApplicationContext(), "Kodas " + obd + " neregistruotas", Toast.LENGTH_SHORT).show();
                            ;
                        } else
                            Toast.makeText(getApplicationContext(), "klaida duomneyse", Toast.LENGTH_SHORT).show();
                        ;
                    }
                }
            }
        });
    }


    private DiagnostikosKodas jsonToKodas(String result) {
        DiagnostikosKodas diagnostikosKodas = new DiagnostikosKodas("Kodas nerastas");
        try {
            JSONArray jsonArray = new JSONArray(result);

            for (int i = 1; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String OBDKodasDB = jsonObject.getString("OBD_REIKSME");
                String kritiskumas = jsonObject.getString("KRITISKUMAS");
                String aprasymas = jsonObject.getString("APRASYMAS");
                String priezastis = jsonObject.getString("PRIEZASTYS");
                String tvarkymas = jsonObject.getString("TVARKYMAS");
                String susijeGedimai = jsonObject.getString("SUSIJE_GEDIMAI");
                int kaina = jsonObject.getInt("KAINA");
                String sukurtas = jsonObject.getString("SUKURIMO_DATA");
                String atnaujintas = jsonObject.getString("ATNAUJINIMO_DATA");
                short paskirtis = (short) jsonObject.getInt("SERVISO_PASKIRTIS");
                servisoPaskirtis = paskirtis;
                diagnostikosKodas = new DiagnostikosKodas(OBDKodasDB, kritiskumas, aprasymas, priezastis, tvarkymas, susijeGedimai, kaina, sukurtas, atnaujintas, paskirtis);
            }
        } catch (JSONException e) {
         diagnostikosKodas = new DiagnostikosKodas("Kodas nerastas");
        }

        return diagnostikosKodas;
    }

    private DiagnostikosKodas[] jsonToKodai(String result) {
        DiagnostikosKodas[] koduSarasas = new DiagnostikosKodas[kiekis];
        try {
            JSONArray jsonArray = new JSONArray(result);
            Short paskirtiesReiksmes[] = new Short[kiekis];
            for (int i = 1; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String OBDKodasDB = jsonObject.getString("OBD_REIKSME");
                String kritiskumas = jsonObject.getString("KRITISKUMAS");
                String aprasymas = jsonObject.getString("APRASYMAS");
                String priezastis = jsonObject.getString("PRIEZASTYS");
                String tvarkymas = jsonObject.getString("TVARKYMAS");
                String susijeGedimai = jsonObject.getString("SUSIJE_GEDIMAI");
                int kaina = jsonObject.getInt("KAINA");
                String sukurtas = jsonObject.getString("SUKURIMO_DATA");
                String atnaujintas = jsonObject.getString("ATNAUJINIMO_DATA");
                short paskirtis = (short) jsonObject.getInt("SERVISO_PASKIRTIS");
                DiagnostikosKodas diagnostikosKodas = new DiagnostikosKodas(OBDKodasDB, kritiskumas, aprasymas, priezastis, tvarkymas, susijeGedimai, kaina, sukurtas, atnaujintas, paskirtis);
                koduSarasas[i - 1] = diagnostikosKodas;
                paskirtiesReiksmes[i - 1] = paskirtis;
            }
            servisoPaskirtis = mode(paskirtiesReiksmes, kiekis);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return koduSarasas;
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        pasirinktasAuto = sharedPreferences.getString(AUTOMOBILIS, "");
        vartotojasId = sharedPreferences.getInt(VARTOTOJAS, 0);
    }

    static Short mode(Short[] a, int n) {
        short maxValue = 0, maxCount = 0, i, j;

        for (i = 0; i < n; ++i) {
            short count = 0;
            for (j = 0; j < n; ++j) {
                if (a[j] == a[i])
                    ++count;
            }
            if (count > maxCount) {
                maxCount = count;
                maxValue = a[i];
            }
        }
        return maxValue;
    }

}

