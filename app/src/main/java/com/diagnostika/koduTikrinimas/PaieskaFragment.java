package com.diagnostika.koduTikrinimas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.diagnostika.R;
import com.diagnostika.koduTikrinimas.PaieskaRezultatai;
import com.diagnostika.paskyrosValdymas.Prisijungimas;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.util.ArrayList;
import java.util.Locale;

public class PaieskaFragment extends Fragment {

    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String AUTOMOBILIS = "automobilis";
    private static final String VARTOTOJAS = "vartotojas";

    private String pasirinktasAuto;
    private int vartotojasId;

    EditText editTextPaieskaNeprisijungus;
    LinearLayout sarasas;
    ArrayList<String> paieskaSarasas = new ArrayList<>();
    TextView textTikrintiSarasa, buttonPrisijungti;
    Button buttonTikrinti;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_paieska, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sarasas = view.findViewById(R.id.listPaieska);
        buttonTikrinti = view.findViewById(R.id.buttonTikrinti);
        buttonPrisijungti = view.findViewById(R.id.buttonPrisijungti);
        editTextPaieskaNeprisijungus = view.findViewById(R.id.editTextPaieska);
        textTikrintiSarasa = view.findViewById(R.id.textTikrintiSarasa);
        buttonPrisijungti = view.findViewById(R.id.buttonPrisijungti);
        ImageView imageView = view.findViewById(R.id.deleteImg);
        imageView.setVisibility(View.GONE);

        loadData();

        if (isPrisijunges()) {
            buttonPrisijungti.setVisibility(View.GONE);
        } else {
            textTikrintiSarasa.setVisibility((View.GONE));
        }

        textTikrintiSarasa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addView();
                textTikrintiSarasa.setVisibility(View.GONE);
            }
        });

        buttonTikrinti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (textTikrintiSarasa.getVisibility() == View.VISIBLE) paieskaVienasKodas();
                else paieskaKoduSarasas();
            }
        });

        buttonPrisijungti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Prisijungimas.class);
                startActivity(intent);
            }
        });
    }


    private boolean isPrisijunges() {
        boolean status = false;
        if (vartotojasId != 0) status = true;
        return status;
    }

    private void addView() {
        final View aa = getLayoutInflater().inflate(R.layout.add_paieska, null, false);
        ImageView deleteImg = aa.findViewById(R.id.deleteImg);
        deleteImg.setVisibility(View.VISIBLE);
        EditText textPaieska = aa.findViewById(R.id.editTextPaieska);

        textPaieska.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (textPaieska.getText().toString().equals("")) addView();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        deleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeView(aa);
            }

        });
        sarasas.addView(aa);
    }

    private void removeView(View view) {
        sarasas.removeView(view);
        if (sarasas.getChildCount() == 1) textTikrintiSarasa.setVisibility(View.VISIBLE);
    }


    private void paieskaVienasKodas() {

        String obdKodas = String.valueOf(editTextPaieskaNeprisijungus.getText());

        if (!obdKodas.equals("")) {
            if (tikrintiFormata(obdKodas)) {
                uzklausaKodas(obdKodas);
            } else Toast.makeText(getContext(), "Formatas neteisingas", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(getContext(), "įveskite diagnostikos kodą", Toast.LENGTH_SHORT).show();
    }

    private boolean tikrintiFormata(String obdKodas) {
        boolean formatasTinkamas = true;
        obdKodas = obdKodas.toUpperCase(Locale.ROOT);
        if (obdKodas.length() == 5) {
            if ((obdKodas.charAt(0) != 'B') && (obdKodas.charAt(0) != 'C') && (obdKodas.charAt(0) != 'P') && (obdKodas.charAt(0) != 'U'))
                formatasTinkamas = false;
            if ((obdKodas.charAt(1) != '0') && (obdKodas.charAt(1) != '1') && (obdKodas.charAt(1) != '2') && (obdKodas.charAt(1) != '3'))
                formatasTinkamas = false;
            if ((obdKodas.charAt(2) != '0') && (obdKodas.charAt(2) != '1') && (obdKodas.charAt(2) != '2') && (obdKodas.charAt(2) != '3') && (obdKodas.charAt(2) != '4') && (obdKodas.charAt(2) != '5') && (obdKodas.charAt(2) != '6') && (obdKodas.charAt(2) != '7'))
                formatasTinkamas = false;
            if ((obdKodas.charAt(3) != '0') && (obdKodas.charAt(3) != '1') && (obdKodas.charAt(3) != '2') && (obdKodas.charAt(3) != '3') && (obdKodas.charAt(3) != '4') && (obdKodas.charAt(3) != '5') && (obdKodas.charAt(3) != '6') && (obdKodas.charAt(3) != '7') && (obdKodas.charAt(3) != '8') && (obdKodas.charAt(3) != '9'))
                formatasTinkamas = false;
            if ((obdKodas.charAt(4) != '1') && (obdKodas.charAt(4) != '2') && (obdKodas.charAt(4) != '3') && (obdKodas.charAt(4) != '4') && (obdKodas.charAt(4) != '5') && (obdKodas.charAt(4) != '6') && (obdKodas.charAt(4) != '7') && (obdKodas.charAt(4) != '8') && (obdKodas.charAt(4) != '9'))
                formatasTinkamas = false;
        } else formatasTinkamas = false;
        return formatasTinkamas;
    }

    private void paieskaKoduSarasas() {

        boolean formatasTinkamas = true;
        String kodai = "";
        int koduKiekis = 0;
        for (int i = 0; i < sarasas.getChildCount(); i++) {
            if (sarasas.getChildAt(i) instanceof RelativeLayout) {
                RelativeLayout ll = (RelativeLayout) sarasas.getChildAt(i);
                for (int j = 0; j < ll.getChildCount(); j++) {
                    if (ll.getChildAt(j) instanceof EditText) {
                        EditText et = (EditText) ll.getChildAt(j);
                        if (et.getId() == R.id.editTextPaieska)
                            if (!et.getText().toString().equals("")) {
                                if (tikrintiFormata(String.valueOf(et.getText()))) {
                                    if (i > 0) kodai += (",");
                                    kodai += ("'" + et.getText().toString() + "'");
                                    koduKiekis++;
                                } else {
                                    Toast.makeText(getContext(), "Formatas netinkamas", Toast.LENGTH_SHORT).show();
                                    formatasTinkamas = false;
                                }
                            }
                    }
                }
            }
        }

        if (formatasTinkamas) uzklausaKoduSarasas(kodai, koduKiekis);


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
                                Toast.makeText(getContext(), "Kodas " + OBDKodas + " nerastas", Toast.LENGTH_SHORT).show();
                            ;
                        } else
                            Toast.makeText(getContext(), "klaida duomneyse", Toast.LENGTH_SHORT).show();
                        ;
                    }
                }
            }

        });

    }


    public void loadData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        pasirinktasAuto = sharedPreferences.getString(AUTOMOBILIS, "");
        vartotojasId = sharedPreferences.getInt(VARTOTOJAS, 0);
    }


}