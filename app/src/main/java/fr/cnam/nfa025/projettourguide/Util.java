package fr.cnam.nfa025.projettourguide;

public class Util {


    public static final String TRACE = "Trace";

    //DB sites touristique
    public static final String TABLE_SITE_TOUTISTIQUE = "sitetouristique";
    public static final String KEY = "id";
    public static final String NOM = "nom";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String DESCRIPTION = "description";
    public static final String LIEN1 = "lien1";
    public static final String LIEN2 = "lien2";
    public static final String PHOTO = "photo";
    public static final String AUDIO = "audio";
    public static final String THEMES = "themes";
    public static final String SHORT_DESCRIPTION = "short_description";

    public static final String TABLE_CREATE_SITE_TOURISTIQUE = "CREATE TABLE " + TABLE_SITE_TOUTISTIQUE +
            " (" + KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + NOM + " VARCHAR(100), "
            + LATITUDE + " REAL, "
            + LONGITUDE + " REAL, "
            + SHORT_DESCRIPTION + " VARCHAR(50), "
            + THEMES + " VARCHAR(100), "
            + DESCRIPTION + " VARCHAR(1000), "
            + LIEN1 + " BLOB, "
            + LIEN2 + " BLOB, "
            + PHOTO + " VARCHAR(100), "
            + AUDIO + " VARCHAR(100));";

    public static final String TABLE_DROP_SITE_TOURISTIQUE =  "DROP TABLE IF EXISTS " + TABLE_SITE_TOUTISTIQUE + ";";
    public static final String DELETE_DB = "DELETE FROM " + TABLE_SITE_TOUTISTIQUE + ";";


    //DB Users
    public static final String TABLE_USER = "user_table";
    public static final String PRENOM = "prenom";
    public static final String LOGIN = "login";
    public static final String PWD = "password";
    public static final String SEXE = "sexe";
    public static final String PREFERENCE = "preference";


    public static final String TABLE_CREATE_USER = "CREATE TABLE " + TABLE_USER +
            " (" + KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            NOM + " VARCHAR(20), " + PRENOM + " VARCHAR(20), " + SEXE + " VARCHAR(10), " +
            LOGIN + " VARCHAR(20), " + PWD + " VARCHAR(20), " + PREFERENCE + " VARCHAR(100));";


    public static final String TABLE_DROP_USER =  "DROP TABLE IF EXISTS " + TABLE_USER + ";";

}
