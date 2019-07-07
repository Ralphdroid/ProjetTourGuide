package fr.cnam.nfa025.projettourguide;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class CreationCompte extends Activity {

    monDOA monDOA_user;

    @Override
    public void onCreate(Bundle b){

        super.onCreate(b);
        setContentView(R.layout.creation_compte_form);

        CheckBox cb_sortie = findViewById(R.id.pref_sortie);
        cb_sortie.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                LinearLayout ll_sorties = findViewById(R.id.toutes_sorties);
                ll_sorties.setVisibility(View.VISIBLE);

            }
        });

        monDOA_user = new monDOA(this.getApplicationContext(), Util.TABLE_USER);



    }

    public void action(View v) {

        //on teste pour savoir si le login n'existe pas déjà:
        EditText et_login = (EditText)findViewById(R.id.login_temp);
        String login = et_login.getText().toString();

        if (monDOA_user.verifUniciteLogin(login)) {


            //j'ajoute le nouveau utilisateur dans ma base de données USER
            String sexe = (((CheckBox) (findViewById(R.id.cb_homme))).isChecked()) ? "homme" : "femme";
            String nom = ((EditText) findViewById(R.id.nom)).getText().toString();
            String prenom = ((EditText) findViewById(R.id.prenom)).getText().toString();
            String pwd = ((EditText) findViewById(R.id.password_temp)).getText().toString();
            String pref = getPreferences();

            monDOA_user.ajouterUnUser(nom, prenom, sexe, login, pwd, pref); //"Egouy", "Ralph", 'h', "Kenny", "test", "preference1");
            finish();

        } else {

            et_login.setBackgroundColor(Color.RED);
            Toast.makeText(this, "Le login a déjà été utlisé. Merci de saisir un nouveau", Toast.LENGTH_LONG).show();
            //Log.i(Util.TRACE, "CreationCompte: le login existe deja: Recommencez:");
        }

    }


    public String getPreferences(){

        String preferences = "";

        if (((CheckBox)(findViewById(R.id.pref_culture))).isChecked()) preferences = preferences + "culture;";
        if (((CheckBox)(findViewById(R.id.pref_gastronomie))).isChecked()) preferences += "gastronomie;";
        if (((CheckBox)(findViewById(R.id.pref_historique))).isChecked()) preferences += "historique;";
        if (((CheckBox)(findViewById(R.id.pref_detente))).isChecked()) preferences += "detente;";
        if (((CheckBox)(findViewById(R.id.pref_shopping))).isChecked()) preferences += "shopping;";
        if (((CheckBox)(findViewById(R.id.pref_theatre))).isChecked()) preferences += "theatre;";
        if (((CheckBox)(findViewById(R.id.pref_cinema))).isChecked()) preferences += "cinema;";

        return preferences;

    }

}
