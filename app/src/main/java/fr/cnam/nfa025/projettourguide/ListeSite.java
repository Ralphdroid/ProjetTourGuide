package fr.cnam.nfa025.projettourguide;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListeSite extends Activity {

    monDOA siteTouristique_db;
    SiteTouristiqueAdapter adapter;
    ArrayList<String> listeSiteTouristique;
    Map<Integer, Integer> data;

    @Override
    public void onCreate(Bundle b) {

        super.onCreate(b);
        setContentView(R.layout.liste_site);

        siteTouristique_db = new monDOA(getBaseContext(), Util.TABLE_SITE_TOUTISTIQUE);
        listeSiteTouristique = new ArrayList<String>();
        data = new HashMap<Integer, Integer>() ;

        //afficher_sites();
        Log.i(Util.TRACE, "ListeSite: onCreate : Appel de afficherListeParOrdreAlphab()" );
        afficherListeParOrdreAlphab();
    }

    private void afficher_sites() {

        SQLiteDatabase mDb = siteTouristique_db.mHandler.getWritableDatabase();
        long count = DatabaseUtils.queryNumEntries(mDb, Util.TABLE_SITE_TOUTISTIQUE);

        mDb.close();
        Log.i(Util.TRACE, "  ===> Modification: Nb de lignes de la table = " +  count);

    }


    public void afficherListeParOrdreAlphab() {

        // Construct the data source
        ArrayList<SiteTouristiqueItem> arrayOfSites = new ArrayList<SiteTouristiqueItem>();
        // Create the adapter to convert the array to views
        adapter = new SiteTouristiqueAdapter(this, arrayOfSites);

        // Attach the adapter to a ListView
        ListView mlistView = (ListView) findViewById(R.id.lv_listesite);
        mlistView.setAdapter(adapter);
        mlistView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent localiserCarte_intent = new Intent(ListeSite.this , MapsActivity.class);
                localiserCarte_intent.putExtra("id_mod", data.get(position));
                startActivityForResult(localiserCarte_intent, 1);

            }
        });

        for(char alphabet = 'a'; alphabet <='z'; alphabet ++ )
        {
            afficherListe(alphabet);
        }


    }

    public void afficherListe(char alphabet_p) {

        SQLiteDatabase mDb = siteTouristique_db.mHandler.getWritableDatabase();

        try {

            Cursor cursor = (Cursor) mDb.rawQuery("select * from " + Util.TABLE_SITE_TOUTISTIQUE + " where NOM LIKE '" + alphabet_p + "%';", null);

            if (cursor.getCount()> 0) {
                SiteTouristiqueItem nouveauSiteTouristiqueItem = new SiteTouristiqueItem(alphabet_p);
                adapter.add(nouveauSiteTouristiqueItem);
                //listeSiteTouristique.add(alphabet_p + "");
                //Log.i(Util.TRACE, "afficherListe: taille du cursor = " + cursor.getCount() + " OK !!");
            } else return; // else Log.i(Util.TRACE, "afficherListe: taille du cursor = 0 !!!!!!!!!!!");

            if (cursor != null) {

                // move cursor to first row
                if (cursor.moveToFirst()) {
                    do {

                        // Add item to adapter
                        int id = cursor.getInt(0);
                        String nom = cursor.getString(1);
                        Double latitude = cursor.getDouble(2);
                        Double longitude = cursor.getDouble(3);
                        String short_description = cursor.getString(4);
                        String theme = cursor.getString(5);
                        String description = cursor.getString(6);

                        SiteTouristiqueItem nouveauSiteTouristique = new SiteTouristiqueItem(id, nom,
                                latitude, longitude, short_description, theme, description);
                        adapter.add(nouveauSiteTouristique);

                        // move to next row
                    } while (cursor.moveToNext());
                }
            } else Log.i(Util.TRACE, "afficherListe: la donnée n'a pas réussi à être lue ! -- lettre : " + alphabet_p);

        } catch (Exception e) {

            Log.i(Util.TRACE, "Modification: La table est vide ou pb !"  );
        }

        adapter.notifyDataSetChanged();

    }



}
