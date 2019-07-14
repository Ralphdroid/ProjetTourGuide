package fr.cnam.nfa025.projettourguide;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.io.InputStream;
import java.util.ArrayList;


//Objet d'accès aux données (Data access object (DOA))
public class monDOA {

    Context context;
    protected SQLiteDatabase mDb = null;
    protected DatabaseHandler mHandler = null;
    protected final static int VERSION = 1;
    String table_name;

    monDOA() {}

    monDOA(Context context_p, String table_name_p) {

        context = context_p;
        table_name = table_name_p;
        this.mHandler = new DatabaseHandler(context_p, table_name, null, VERSION);
        SQLiteDatabase checkDB;

        //je crée la table si elle n'existe pas...
        try {
            if (Util.log_MONDOA)
                Log.i(Util.TRACE, "[monDOA] => constructeur : " + table_name_p + ": Test existence de la table (apres): La table existe :" + table_name_p);

            checkDB = SQLiteDatabase.openDatabase(context_p.getDatabasePath(table_name_p).getPath(),
                    null, SQLiteDatabase.OPEN_READONLY);

            checkDB.close();

        } catch (SQLiteException e) {
            // base de données n'existe pas.
            if (Util.log_MONDOA)
                Log.i(Util.TRACE, "[monDOA] => constructeur : " + table_name_p + ": Test existence de la table (apres): KO elle n'existe pas ! => je la crée !");

            this.mHandler.onCreate(this.mHandler.getWritableDatabase());
        }

    }



    public boolean ajouterUnSiteTouristique(String table_name_p, String nom_p, String ville_p, String sexe_p) {

        mDb = this.mHandler.getWritableDatabase();

        try {

            ContentValues values = new ContentValues();
            values.put("nom", nom_p);
            values.put("ville", ville_p);
            mDb.insert(table_name_p, null, values);


            Cursor c = (Cursor) mDb.rawQuery("select * from " + table_name_p + ";", null);

            if (Util.log_MONDOA)
                Log.i(Util.TRACE, "[monDOA] => ajouterSiteTouristique: Ajout => nb = " + c.getCount());

            return true;


        } catch (Exception e) {

            if (Util.log_MONDOA)
                Log.i(Util.TRACE, "[monDOA] => ajouterSiteTouristique: l'Ajout s'est mal passé !");

            return false;
        }

    }


    public boolean ajouterUnUser_trash(String table_name_p, String nom_p, String prenom_p, String sexe_p, String login_p, String password_p) {

        mDb = this.mHandler.getWritableDatabase();

        try {

            ContentValues values = new ContentValues();
            values.put("nom", nom_p);
            values.put("prenom", prenom_p);
            values.put("sexe_p", sexe_p+"");
            values.put("login", login_p);
            values.put("password", password_p);
            mDb.insert(table_name_p, null, values);

            if (Util.log_MONDOA)
                Log.i(Util.TRACE, "monDOA - " + table_name_p + ": Values: " + values.size());

            Cursor c = (Cursor) mDb.rawQuery("select * from " + table_name_p + ";", null);

            if (Util.log_MONDOA)
                Log.i(Util.TRACE, "monDOA - " + table_name_p + ": Ajout => nb = " + c.getCount());

            return true;


        } catch (Exception e) {

            if (Util.log_MONDOA)
                Log.i(Util.TRACE, "monDOA - " + table_name_p + ": l'Ajout s'est mal passé !");

            return false;
        }

    }


    public void ajouterUnUser(String nom_p, String prenom_p, String sexe_p, String login_p, String password_p, String preference_p) {

        if (mHandler.addUser(nom_p, prenom_p, sexe_p, login_p, password_p, preference_p))

            if (Util.log_MONDOA)
                Log.i(Util.TRACE, "[monDOA] => ajouterUnUser - table: " + table_name + ": OK !!");
        else

            if (Util.log_MONDOA)
                Log.i(Util.TRACE, "[monDOA] => ajouterUnUser - table: " + table_name + ": KO!!!!!!!! !!");

    }


    public boolean verifUniciteLogin(String login_p) {

        if (mHandler.verifLoginUnique(login_p)) {

            if (Util.log_MONDOA)
                Log.i(Util.TRACE, "[monDOA] => ajouterUnUser - table: " + table_name + ": le login n'existe pas DONC je l'ajoute");
            return true;
        } else {

            if (Util.log_MONDOA)
                Log.i(Util.TRACE, "[monDOA] => ajouterUnUser - table: " + table_name + ": le login existe deja");
            return false;
        }

    }


    public boolean ajouterFromFile(String src_p) {

        SQLiteDatabase mDb = this.mHandler.getWritableDatabase();

        if (Util.log_MONDOA)
            Log.i(Util.TRACE, "[monDOA] => ajouterFromFile: " + src_p);

        mDb.execSQL(Util.DELETE_DB);
        mDb.execSQL(src_p);
        return true;
    }

    public boolean modifier (String table_name_p, String nom_p, Integer longitude_p, Integer latitude_p, Integer id_p) {

        if (Util.log_MONDOA)
            Log.i(Util.TRACE, "SiteTouristiqueDOA: la Modification....!!!!!");
        // CODE
        SQLiteDatabase mDb = this.mHandler.getWritableDatabase();

        try {

            String strSQL = "UPDATE " + table_name_p

                    + " SET " + Util.NOM + " = '" + nom_p + "',"
                    + Util.LONGITUDE + " = '" + longitude_p 
                    + Util.LATITUDE + " = '" + latitude_p + "' WHERE " + Util.KEY + " = " + id_p + ";";

            mDb.execSQL(strSQL);


            return true;


        } catch (Exception e) {

            if (Util.log_MONDOA)
                Log.i(Util.TRACE, "SiteTouristiqueDOA: la Modification s'est mal passé !");

            return false;
        }


    }


    public boolean supprimer(String table_name_p, Integer id_p) {


        if (Util.log_MONDOA)
            Log.i(Util.TRACE, "SiteTouristiqueDOA: la Suppression....!!!!!");
        // CODE
        SQLiteDatabase mDb = this.mHandler.getWritableDatabase();;

        try {

            if (Util.log_MONDOA)
                Log.i(Util.TRACE, "SiteTouristiqueDOA: test 1");
            String strSQL = "DELETE FROM " + table_name_p

                    + " WHERE " + Util.KEY + " = " + id_p + ";";


            if (Util.log_MONDOA)
                Log.i(Util.TRACE, "SiteTouristiqueDOA: test 2");

            mDb.execSQL(strSQL);

            if (Util.log_MONDOA)
                Log.i(Util.TRACE, "SiteTouristiqueDOA: test 3 ");

            return true;


        } catch (Exception e) {

            if (Util.log_MONDOA)
                Log.i(Util.TRACE, "SiteTouristiqueDOA: la Suppression s'est mal passée !");
            return false;
        }

    }


    public boolean verifCoupleLoginPwd(String login_p, String pwd_p) {

        return mHandler.isAuthorized(login_p, pwd_p);
    }

    /**
     * @param m le métier modifié
     */
    public void modifier(monDOA m) {
        // CODE
    }

    /**
     * @param id l'identifiant du métier à récupérer
     */

    /**
     public clubJudo selectionner(long id) {
     // CODE

     return Null;
     }*/


    public Profile getUserProfile(String login_p, String pwd_p) {

        return mHandler.getUserProfile(login_p, pwd_p);
    }


    public ArrayList<SiteTouristiqueItem> getSitesPreferences(ArrayList<String> preferences_p) {

        return mHandler.getSitesPreferences(preferences_p);
    }

}
