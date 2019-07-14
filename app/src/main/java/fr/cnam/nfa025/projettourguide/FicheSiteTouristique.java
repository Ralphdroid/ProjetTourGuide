package fr.cnam.nfa025.projettourguide;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class FicheSiteTouristique extends Activity {

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.fiche_sitetouristique);

        SiteTouristiqueItem sitetouristique_item =  (SiteTouristiqueItem)getIntent().getSerializableExtra("ficheSiteTouristique");

        Log.i(Util.TRACE, "[] => onCreate(): nom du site: " + sitetouristique_item.nom);

        afficherFiche(sitetouristique_item);
    }

    public void afficherFiche(SiteTouristiqueItem fiche_sitetouristique) {

        ImageView iv_image_site_touristique = (ImageView)findViewById(R.id.image_site);
        TextView tv_nom_site_touristique = (TextView)findViewById(R.id.nom_site);
        TextView tv_description_site_tourisstique = (TextView)findViewById(R.id.description);

        tv_description_site_tourisstique.setText(fiche_sitetouristique.description);
        tv_nom_site_touristique.setText(fiche_sitetouristique.nom);

        if(fiche_sitetouristique.nom.equals("Tour eiffel")){
            iv_image_site_touristique.setImageResource(R.drawable.tour_eiffel);
        }else if(fiche_sitetouristique.nom.equals("Arc de Triomphe")){
            iv_image_site_touristique.setImageResource(R.drawable.arc_de_triomphe);
        }else{
            iv_image_site_touristique.setImageResource(R.drawable.ndp);
        }

    }

}
