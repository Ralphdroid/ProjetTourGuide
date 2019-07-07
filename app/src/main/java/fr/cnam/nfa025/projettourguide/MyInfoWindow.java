package fr.cnam.nfa025.projettourguide;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;

//import claudia.com.googlemapproject.MapsActivity;
//import claudia.com.googlemapproject.R;

/**
 * Classe qui permet de créer une fenêtre d'info personnalisée
 * Created by claudia on 08/03/2017.
 */
public class MyInfoWindow implements GoogleMap.InfoWindowAdapter {


    private String titre ;
    private String description;
    private Context context ;
    private HashMap<String,String> markers ;

    /**
     * Construcuteur
     * @param context
     */
    public MyInfoWindow(Context context,HashMap<String,String> markers) {
        this.context  = context;
        this.markers = markers;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {

        //Récupération du layout correspondant à la fenêtre d'info
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.windowlayout, null);


        //Récupération des éléments du layout
        TextView mTitle = (TextView) v.findViewById(R.id.title);
        TextView mDesc = (TextView) v.findViewById(R.id.description);
        ImageView mImage = (ImageView) v.findViewById(R.id.imageView);

        //Personnalisation des éléments (on récupère les infos du marqueur)
        mTitle.setText(marker.getTitle());
        mDesc.setText(marker.getSnippet());

        if (marker != null) {

            //Log.i("MSG", "valeur du marker: " + marker);
            if (markers.get(marker.getId()).equals("Tour eiffel")) {
                mImage.setImageResource(R.drawable.tour_eiffel);
            } else if (markers.get(marker.getId()).equals("Arc de Triomphe")) {
                mImage.setImageResource(R.drawable.arc_de_triomphe);
            } else {
                mImage.setImageResource(R.drawable.ndp);
            }
        }

        // Returning the view containing InfoWindow contents
        return v;
    }
}