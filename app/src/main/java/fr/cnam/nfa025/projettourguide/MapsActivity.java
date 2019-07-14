package fr.cnam.nfa025.projettourguide;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LocationManager mLocationManager;
    ArrayList<SiteTouristiqueItem> arraySites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        arraySites = (ArrayList<SiteTouristiqueItem>)(getIntent().getSerializableExtra("arraySites"));

        Log.i(Util.TRACE, "[MApsActivity] => onCreate : tailles de la liste des sites à theme: " + arraySites.size() );

    }



    @Override
    public void onMapReady(GoogleMap googleMap) {



        mMap = googleMap;
        Location myLocation = getLastKnownLocation();

        // Add a marker in Sydney and move the camera
        LatLng myAndroidSM = new LatLng(myLocation.getLatitude(),  myLocation.getLongitude()); //(-34, 151);
        mMap.addMarker(new MarkerOptions().position(myAndroidSM).title("Je suis ici !"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(myLocation.getLatitude(),  myLocation.getLongitude()), 12.0f));

        //Ajout code
        HashMap<String, String> markers = new HashMap<>();

        //Ajout des marqueurs
        for (int i =0; i < arraySites.size(); i++) {
            SiteTouristiqueItem site = arraySites.get(i);

            Marker  marker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(site.latitude, site.longitude))
                    .title(site.nom).snippet(site.short_description));

            markers.put(marker.getId(), site.nom);

            Log.i(Util.TRACE, "[MApsActivity] => onMapReady: nom du site: " + site.nom + " -- lat: " + site.latitude + " -- short description: " + site.short_description);

        }


        //itinéraire
        //new CalculItineraire(this, mMap, "Tour eiffel", "Arc de triomphe").execute();

        /*
        Marker sireneM = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(48.853, 2.35))
                .title("Notre Dame").snippet("Certains prétendent l'avoir vu nager dans la Seine, pas très loin d'ici"));


        Marker licorneM = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(48.8584, 2.2945))
                .title("Tour eiffel").snippet("Symbole de la France dans le monde, vitrine de Paris, elle accueille aujourd’hui près" +
                        " de 7 millions de visiteurs par an (dont environ 75% d’étrangers), ce qui en fait le monument payant le plus visité au monde.!"));

        Marker dragonM = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(48.873792, 2.295028))
                .title("Arc de Triomphe").snippet("Attention il fait feux de tout bois pour défendre sa princesse !"));

*/


        //ajout des marqueurs dans la hashtable : id du marqueur / nom du fichier correspondant
        /*markers.put(sireneM.getId(), "Tour eiffel");
        markers.put(licorneM.getId(), "Arc de Triomphe");
        markers.put(dragonM.getId(), "Notre Dame");*/

        // Création d'une fenêtre d'info personnalisée
        mMap.setInfoWindowAdapter(new MyInfoWindow(this, markers));

    }

    private Location getLastKnownLocation() {
        mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

                return bestLocation;
            } else {
                Location l = mLocationManager.getLastKnownLocation(provider);
                if (l == null) {
                    continue;
                }
                if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                    // Found best last known location: %s", l);
                    bestLocation = l;
                }
            }

        }
        return bestLocation;
    }


    public void localiser(View v) {

        /*
        String sLatitude = ((EditText) (this.findViewById(R.id.latitude))).getText().toString();
        String sLongitude = ((EditText) (this.findViewById(R.id.longitude))).getText().toString();

        double Latitude = Double.parseDouble(sLatitude);
        double Longitude = Double.parseDouble(sLongitude);

        mapRefresh(Latitude, Longitude);
        */

    }


    public void mapRefresh(double lat_p, double long_p) {

        LatLng sydney = new LatLng(lat_p,  long_p); //(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Je suis ici !"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat_p,  long_p), 12.0f));

    }




}
