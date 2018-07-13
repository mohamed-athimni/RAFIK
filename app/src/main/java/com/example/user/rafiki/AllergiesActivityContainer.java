package com.example.user.rafiki;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sun.mail.imap.protocol.ID;

import java.util.ArrayList;
import java.util.List;

public class AllergiesActivityContainer extends AppCompatActivity {
    EditText donne1,donne2,donne3;
    TextView NonAllergies;
      SharedPreferences.Editor editor;
    String Testdonne2,Testdonne1,Testdonne3;
    MySQLiteOpenHelper helper;
    UserDataSource ds;
    List<String> list = new ArrayList<String>() ;
    int Id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allergies_container);
        helper = new MySQLiteOpenHelper(this, "Utilisateur", null);
        ds = new UserDataSource(helper);
        Id = getIntent().getExtras().getInt("Id");

        donne1 = findViewById(R.id.donne1);
        donne2 = findViewById(R.id.donne2);
        donne3 = findViewById(R.id.donne3);


        NonAllergies = findViewById(R.id.NonAllergies);
        String nomAllergies = getIntent().getExtras().getString("allergies");
        NonAllergies.setText(nomAllergies);

        list=ds.getListAllergies(Id);

            donne1.setText(list.get(2));
            donne2.setText(list.get(1));
            donne3.setText(list.get(0));

        donne1.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                donne1.setText("");
                long x = ds.Updateallergie("",donne2.getText().toString(),donne3.getText().toString(),Id);
                return false;
            }
        });

        donne2.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                donne1.setText("");
                long x = ds.Updateallergie(donne1.getText().toString(),"",donne3.getText().toString(),Id);
                return false;
            }
        });
        donne3.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                donne1.setText("");
                long x = ds.Updateallergie(donne1.getText().toString(),donne2.getText().toString(),"",Id);
                return false;
            }
        });
    }

    public void retoure_fiche(View view) {
        Testdonne1=donne1.getText().toString();
        Testdonne2=donne2.getText().toString();
        Testdonne3=donne3.getText().toString();
        ds.Updateallergie(Testdonne1,Testdonne2,Testdonne3,Id);
        startActivity(new Intent(this,Fiche_MedicaleActivity.class));
}

}
