package fr.cnam.nfa025.projettourguide;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.v4.widget.TextViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;

public class SiteTouristiqueAdapter extends ArrayAdapter<SiteTouristiqueItem> {


        public SiteTouristiqueAdapter(Context context, ArrayList<SiteTouristiqueItem> sitestouristiques) {
            super(context, 0, sitestouristiques);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            // je récupère le site touristique en fonction de sa position
            SiteTouristiqueItem sitetouristique_item = getItem(position);


            // Je vérifie si la vue existe, si non alors je la crée
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.sitetouristique_item, parent, false);
            }

            TextView tv_nom = (TextView) convertView.findViewById(R.id.nom_site);
            TextView tv_description = (TextView) convertView.findViewById(R.id.short_description);
            ImageView iv_photo = (ImageView)convertView.findViewById(R.id.image_site);
            TextView tv_entete = (TextView) convertView.findViewById(R.id.entete);

            if (!sitetouristique_item.isEntete) {

                // Je remplie les données dans la vue Modèle à l'aide de l'objet de données
                tv_nom.setText(sitetouristique_item.nom);
                tv_description.setText(sitetouristique_item.description);

                if(sitetouristique_item.nom.equals("Tour eiffel")){
                    iv_photo.setImageResource(R.drawable.tour_eiffel);
                }else if(sitetouristique_item.nom.equals("Arc de Triomphe")){
                    iv_photo.setImageResource(R.drawable.arc_de_triomphe);
                }else{
                    iv_photo.setImageResource(R.drawable.ndp);
                }


                if (Util.log_SITETOURISTIQUEADAPTER)
                    Log.i(Util.TRACE, "[SiteTouristiqueAdapter] => getView() : position: " + position + " -- nom: " + sitetouristique_item.nom );

            } else {

                if (Util.log_SITETOURISTIQUEADAPTER)
                    Log.i(Util.TRACE, "[SiteTouristiqueAdapter] => getView() : position: " + position + " -- Lettre: " + sitetouristique_item.lettre + "");

                tv_entete.setText(((sitetouristique_item.lettre) + "").toUpperCase());


                // Je remplie les données dans la vue Modèle à l'aide de l'objet de données
                tv_nom.setText("");
                tv_description.setText("");
                iv_photo.setImageDrawable(null);

            }
            // Return the completed view to render on screen
            return convertView;
        }



}
