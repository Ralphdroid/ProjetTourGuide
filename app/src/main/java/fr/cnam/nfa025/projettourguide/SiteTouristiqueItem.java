package fr.cnam.nfa025.projettourguide;

import java.io.Serializable;

public class SiteTouristiqueItem implements Serializable {

    char lettre;
    int id;
    String nom = "";
    Double latitude ;
    Double longitude;
    String short_description;
    String description;
    String theme;
    Boolean isEntete = false;

    SiteTouristiqueItem(int id_p, String nom_p, Double lat_p, Double long_p, String short_description_p, String theme_p, String description_p) {

        id = id_p;
        nom = nom_p;
        latitude = lat_p;
        longitude = long_p;
        description = description_p;
        isEntete = false;
        short_description = short_description_p;
        theme = theme_p;

    }

    SiteTouristiqueItem(char lettre_p) {
        isEntete = true;
        lettre = lettre_p;
    }
}


