package fr.cnam.nfa025.projettourguide;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper implements Serializable {

    SQLiteDatabase db;
    String table_name;
    String DROP_TABLE, CREATE_TABLE;
    boolean showLog = Util.log_DATABASEHANDLER;

    public DatabaseHandler(Context context, String table_name_p, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, table_name_p, factory, version);

        if (showLog)
            Log.i(Util.TRACE, "[DatabaseHandler] => constructeur !");
        
        table_name = table_name_p;

        if (table_name.equals(Util.TABLE_SITE_TOUTISTIQUE)) {

            DROP_TABLE = Util.TABLE_DROP_SITE_TOURISTIQUE;
            CREATE_TABLE = Util.TABLE_CREATE_SITE_TOURISTIQUE;

        } else {

            DROP_TABLE = Util.TABLE_DROP_USER;
            CREATE_TABLE = Util.TABLE_CREATE_USER;
        }

    }

    @Override
    public void onCreate(SQLiteDatabase db_p) {

        this.db = db_p;

        if (showLog)
            Log.i(Util.TRACE, "[DatabaseHandler] => onCreate: !");

        db.execSQL(DROP_TABLE);

        if (showLog)
            Log.i(Util.TRACE, "[DatabaseHandler] => onCreate: " + table_name + "_CREATE");

        db.execSQL(CREATE_TABLE);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (showLog)
            Log.i(Util.TRACE, "[DatabaseHandler] => onUpgrade !");

        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

    public void setSQLCreate(String sqlCreateTable_p) {

        //this.JUDO_TABLE_DROP = sqlCreateTable_p;

    }


    public boolean addUser(String nom_p, String prenom_p, String sexe_p, String login_p, String password_p, String preference_p) {

        SQLiteDatabase db = this.getWritableDatabase();

        try {

            ContentValues values = new ContentValues();
            values.put("nom", nom_p);
            values.put("prenom", prenom_p);
            values.put("sexe", sexe_p + "");
            values.put("login", login_p);
            values.put("password", password_p);
            values.put("preference", preference_p);
            db.insert("user_table", null, values);

            if (showLog)
                Log.i(Util.TRACE, "[DatabaseHandler] => addUser: " + table_name + ": Values: " + values.size());

            Cursor c = (Cursor) db.rawQuery("SELECT * FROM " + table_name + " ;", null);

            if (showLog)
                Log.i(Util.TRACE, "[DatabaseHandler] => addUser: " + table_name + ": Ajout => nb = " + c.getCount());

            db.close();
            return true;


        } catch (Exception e) {


            if (showLog)
                Log.i(Util.TRACE, "[DatabaseHandler] => addUser: " + table_name + ": l'Ajout s'est mal passé !");

            db.close();
            return false;
        }


    }

    public boolean verifLoginUnique(String login_p) {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = (Cursor) db.rawQuery("SELECT * FROM " + table_name + "  WHERE  login LIKE '" + login_p + "';", null);
        boolean resultat = (c.getCount() > 0) ? false : true;
        db.close();

        return resultat;
    }

    public boolean isAuthorized(String login_p, String pwd_p) {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = (Cursor) db.rawQuery("SELECT * FROM " + table_name + "  WHERE  login LIKE '" + login_p + "' AND password LIKE '" + pwd_p + "';", null);

        boolean resultat = (c.getCount() > 0) ? true : false;
        db.close();

        return resultat;

    }

    public Profile getUserProfile(String login_p, String pwd_p) {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = (Cursor) db.rawQuery("SELECT * FROM " + table_name + "  WHERE  login LIKE '" + login_p + "' AND password LIKE '" + pwd_p + "';", null);

        Profile profile = null;

        try {
            if (cursor != null) {

                if (cursor.moveToFirst()) {
                    do {
                        profile = new Profile(cursor.getString(1),  cursor.getString(2), cursor.getString(3),
                                cursor.getString(4), cursor.getString(6));

                        // move to next row
                    } while (cursor.moveToNext());
                }
            }

        } catch (Exception e) {

            if (showLog)
                Log.i(Util.TRACE, "[DatabaseHandler] => getUserProfile: Pb dans la réupération des données de l'user !"  );
        }

        db.close();

        return profile;

    }

    public ArrayList<SiteTouristiqueItem> getSitesPreferences(ArrayList<String> preferences_p) {

        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<SiteTouristiqueItem>  sitesPreferences = new ArrayList<SiteTouristiqueItem>();

        for (int i = 0; i < preferences_p.size(); i++) {

            Cursor cursor = (Cursor) db.rawQuery("SELECT * FROM " + table_name + "  WHERE  themes LIKE '%" + preferences_p.get(i) + "%' ;", null);
            try {
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

                            sitesPreferences.add(new SiteTouristiqueItem(id, nom,
                                    latitude, longitude, short_description, theme, description));

                            if (showLog)
                                Log.i(Util.TRACE, "[DatabaseHandler] => getSitesPreferences: Latitude: " + cursor.getLong(3)  );


                            // move to next row
                        } while (cursor.moveToNext());
                    }
                } else

                if (showLog)
                    Log.i(Util.TRACE, "[DatabaseHandler]: getSitesPreferences: la donnée n'a pas réussi à être lue !");

            } catch (Exception e) {

                if (showLog)
                    Log.i(Util.TRACE, "[DatabaseHandler]: getSitesPreferences: La table est vide ou pb !");
            }

        }

        db.close();
        return sitesPreferences;

    }

}