package com.example.user.rafiki;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;

import com.example.user.rafiki.ItemData.Constante;
import com.example.user.rafiki.ItemData.Contacts_Parentaux;
import com.example.user.rafiki.ItemData.Contacts_Urgences;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ContactUrgence extends AppCompatActivity {

    String Code, Code1;
    String[] codes = new String[199];
    Liste_code_payes adapter;
    MySQLiteOpenHelper helper;
    UserDataSource ds;
    Spinner Spinner,Spinner1;
    EditText Nom_asur, Tel_asur, Nom_urg, Tel_urg;
    List<Contacts_Urgences> listContacts_U= new ArrayList<Contacts_Urgences>();
    TableLayout T1,T2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_urgence);
        helper = new MySQLiteOpenHelper(this, "Utilisateur", null);
        ds = new UserDataSource(helper);

        Nom_asur = findViewById(R.id.nom_asur);
        Tel_asur = findViewById(R.id.tel_asur);
        Nom_urg = findViewById(R.id.nom_urgen);
        Tel_urg = findViewById(R.id.tel_urgen);
        Spinner = findViewById(R.id.spinner);
        Spinner1 = findViewById(R.id.spinner1);
        T1 = findViewById(R.id.tableLayout);
        T2 = findViewById(R.id.tableLayout1);

        remplirspinir();
        if (ds.getCountUrgences() > 0) {
            listContacts_U = ds.getListUrgences();
            Nom_asur.setText(listContacts_U.get(0).getNom_asur());
            Tel_asur.setText(listContacts_U.get(0).getTel_asur());
            Spinner.setSelection(TrouverIndice(listContacts_U.get(0).getCode_asur()));

            Nom_urg.setText(listContacts_U.get(0).getNom_urg());
            Tel_urg.setText(listContacts_U.get(0).getTel_urg());
            Spinner1.setSelection(TrouverIndice(listContacts_U.get(0).getCode_urg()));
        }
        T1.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                Nom_asur.setText("");
                Tel_asur.setText("");
                //long x = ds.UpdateAnti(new Contacts_Parentaux("", ""), 1);
                return false;
            }
        });
    }
    public void remplirspinir() {

        rempli_code_pays();
        adapter = new Liste_code_payes(this, codes, Constante.imgs);
        Spinner.setAdapter(adapter);
        Spinner1.setAdapter(adapter);
        Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Code = codes[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        Spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Code1 = codes[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void rempli_code_pays() {
        try {
            InputStream inputStream = getAssets().open("indicatif_pays.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            int x = 0;
            String ligne;
            while (bufferedReader.ready()) {

                ligne = bufferedReader.readLine();
                codes[x] = ligne;
                x++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public int TrouverIndice(String item) {
        int i = 0;
        for (String code : codes) {
            if (code.equals(item)) {
                return i;
            }
            i++;
        }
        return i;
    }
    public void retoure(View view) {

        listContacts_U.add(0, new Contacts_Urgences(Nom_asur.getText().toString(),
                Tel_asur.getText().toString(),Code,Nom_urg.getText().toString(),Tel_urg.getText().toString(),Code1));

        if (ds.getCountUrgences() <= 0) {
            int i = 0;
            while (i < listContacts_U.size()) {

                long x = ds.addUrgences(listContacts_U.get(i));
                i++;
            }
            Intent ite=new Intent(this,ContactsActivity.class);
            startActivity(ite);
            ContactUrgence.this.finish();
        } else {
            int i = 0;
            while (i < listContacts_U.size()) {

                long x = ds.UpdateUrgences(listContacts_U.get(i), i + 1);
                i++;
            }
            Intent ite=new Intent(this,ContactsActivity.class);
            startActivity(ite);
            ContactUrgence.this.finish();
        }
    }

    public void alert(View view) {
    }
}
