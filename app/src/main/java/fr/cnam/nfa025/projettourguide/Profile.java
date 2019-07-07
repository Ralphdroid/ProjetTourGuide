package fr.cnam.nfa025.projettourguide;


import java.io.Serializable;
import java.util.ArrayList;

class Profile implements Serializable {

    String nom, prenom, login, sexe, preference_string;

    ArrayList<String> preference;

    Profile (String nom_p, String prenom_p, String sexe_p, String login_p, String preference_p ) {


        nom = nom_p;
        prenom = prenom_p;
        sexe = sexe_p;
        login = login_p;
        preference_string = preference_p;
        String[] preference_tmp = preference_p.split(";");
        preference = new ArrayList<String>();

        for (int i=0; i < preference_tmp.length; i++)
            preference.add(preference_tmp[i]);

    }

    public String toString() {

        return "Profile du User: Nom: " + nom + " -- Prénom: " + prenom + " -- sexe: " + sexe + " -- Preference: " + preference_string;

    }


}

