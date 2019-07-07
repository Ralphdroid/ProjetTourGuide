package fr.cnam.nfa025.projettourguide;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class SiteTouristiqueAdapter extends ArrayAdapter<SiteTouristiqueItem> {


        public SiteTouristiqueAdapter(Context context, ArrayList<SiteTouristiqueItem> sitestouristiques) {
            super(context, 0, sitestouristiques);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {



            //Log.i(Util.TRACE, "SiteTouristiqueAdapter: getView()");
            // je récupère le site touristique en fonction de sa position
            SiteTouristiqueItem sitetouristique_item = getItem(position);

            // Je vérifie si la vue existe, si non alors je la crée
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.sitetouristique_item, parent, false);
            }

            if (!sitetouristique_item.isEntete) {
                // je récupère les objet graphique
                TextView tv_nom = (TextView) convertView.findViewById(R.id.nom_site);
                TextView tv_description = (TextView) convertView.findViewById(R.id.description);

                // Je remplie les données dans la vue Modèle à l'aide de l'objet de données
                tv_nom.setText(sitetouristique_item.nom);
                tv_description.setText(sitetouristique_item.description);

                Log.i(Util.TRACE, "SiteTouristiqueAdapter: position: " + position + " -- nom: " + sitetouristique_item.nom );

            } else {
                Log.i(Util.TRACE, "SiteTouristiqueAdapter: position: " + position + " -- Lettre: " + sitetouristique_item.lettre + "");
                TextView tv_entete = (TextView) convertView.findViewById(R.id.entete);
                tv_entete.setText((sitetouristique_item.lettre + ""));
                TextView tv_nom = (TextView) convertView.findViewById(R.id.nom_site);
                TextView tv_description = (TextView) convertView.findViewById(R.id.description);

                // Je remplie les données dans la vue Modèle à l'aide de l'objet de données
                tv_nom.setText("");
                tv_description.setText("");

            }
            // Return the completed view to render on screen
            return convertView;
        }



}
