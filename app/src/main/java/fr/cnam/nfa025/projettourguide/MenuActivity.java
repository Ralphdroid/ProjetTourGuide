package fr.cnam.nfa025.projettourguide;

import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {


    monDOA siteTourDOA;
    Profile userProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //DB Site touristique
        siteTourDOA = new monDOA(getBaseContext(), Util.TABLE_SITE_TOUTISTIQUE); //, lecteurfichier());
        siteTourDOA.ajouterFromFile(lecteurfichier());



        //je récupère le profile du user
        userProfile = (Profile)getIntent().getSerializableExtra("user_profile");
        //Log.i(Util.TRACE, "[MenuActivity] => onCreate: profile du user: " + userProfile.toString());

    }


    public String lecteurfichier() {

        AssetManager assetManager = getAssets();
        InputStream input;
        String text = "";

        try {

            input = assetManager.open("sites_touristiques.sql");

            int size = input.available();

            byte[] buffer = new byte[size];
            input.read(buffer);
            input.close();

            text = new String(buffer);

            Log.i(Util.TRACE, "[MenuActivity] => lecteurfichier: " + text); //txtFileName.setText(text);

        } catch (Exception e) {
            return text;
        }

        return text;

    }




    public void buttonclicked(View v) {

        int choix_l = v.getId();

        switch (choix_l) {

            case R.id.afficherCarte: {

                Intent iMaps = new Intent(MenuActivity.this, MapsActivity.class);

                //recuperer les sites touristiques en fonction des préférences du user
                ArrayList<SiteTouristiqueItem> arraySites = getArraySites();
                iMaps.putExtra("arraySites", arraySites);
                this.startActivity(iMaps);

                break;

            }

            case R.id.itineraire: {

                Intent iti_intent = new Intent();
                this.startActivity(iti_intent);

                break;

            }

            case R.id.profile: {

                Intent profile_intent = new Intent();
                break;

            }


            case R.id.listesite: {

                Intent listesite_intent = new Intent(this, ListeSite.class);
                this.startActivity(listesite_intent);
                break;

            }

        }
    }


    public ArrayList<SiteTouristiqueItem> getArraySites() {

        return siteTourDOA.getSitesPreferences(userProfile.preference);

    }
}
