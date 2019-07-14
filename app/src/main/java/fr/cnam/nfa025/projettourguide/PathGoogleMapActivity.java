package fr.cnam.nfa025.projettourguide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class PathGoogleMapActivity extends FragmentActivity implements OnMapReadyCallback {

    ArrayList<SiteTouristiqueItem> arraySites;

    GoogleMap googleMap;
    LocationManager mLocationManager;
    final String TAG = "PathGoogleMapActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path_google_map);

        arraySites = (ArrayList<SiteTouristiqueItem>)(getIntent().getSerializableExtra("arraySites"));

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        String url = getMapsApiDirectionsUrl();
        ReadTask downloadTask = new ReadTask();
        downloadTask.execute(url);

    }

    @Override
    public void onMapReady(GoogleMap map) {

        googleMap = map;
        Location myLocation = getLastKnownLocation();

        // Add a marker in Sydney and move the camera
        LatLng myAndroidSM = new LatLng(myLocation.getLatitude(),  myLocation.getLongitude()); //(-34, 151);
        googleMap.addMarker(new MarkerOptions().position(myAndroidSM).title("Je suis ici !"));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(myLocation.getLatitude(),  myLocation.getLongitude()), 12.0f));

        addMarkers();

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myAndroidSM,
                13));

    }

    private Location getLastKnownLocation() {
        mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
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


    private String getMapsApiDirectionsUrl() {

        String url = "https://maps.googleapis.com/maps/api/directions/json?";

        Location myLocation = getLastKnownLocation();



        try{
            String str_origin = "origin="+myLocation.getLatitude()+","+myLocation.getLongitude();
            String str_dest = "destination="+arraySites.get(arraySites.size()-1).latitude+","+ arraySites.get(arraySites.size()-1).longitude;
            String sensor = "sensor=false";
            String waypoints = "";
            for(int i=0;i<arraySites.size()-1;i++){
                SiteTouristiqueItem sitetouristique  = (SiteTouristiqueItem) arraySites.get(i);
                if(i==0)  waypoints = "waypoints=";
                waypoints += sitetouristique.latitude + "," + sitetouristique.longitude + "|";
            }
            String parameters = str_origin+"&"+str_dest+"&"+sensor+"&"+waypoints+"&mode=walking&key="+Util.API_KEY;
            url += parameters;

        }
        catch(Exception e){
            e.printStackTrace();
            url = null;
        }

        Log.i(Util.TRACE, "[PathGoogleMapActivity] => getMapsApiDirectionsUrl: URL = " + url);

        return url;
    }

    private void addMarkers() {

        //Ajout code
        HashMap<String, String> markers = new HashMap<>();

        //Ajout des marqueurs
        for (int i =0; i < arraySites.size(); i++) {
            SiteTouristiqueItem site = arraySites.get(i);

            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(site.latitude, site.longitude))
                    .title(site.nom).snippet(site.short_description));

            markers.put(marker.getId(), site.nom);

            // Création d'une fenêtre d'info personnalisée
            googleMap.setInfoWindowAdapter(new MyInfoWindow(this, markers));

            Log.i(Util.TRACE, "[MApsActivity] => onMapReady: nom du site: " + site.nom + " -- lat: " + site.latitude + " -- short description: " + site.short_description);

        }

    }

    private class ReadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                HttpConnection http = new HttpConnection();
                data = http.readUrl(url[0]);
            } catch (Exception e) {
                Log.i(Util.TRACE, "[PathGoogleMapActivity] => ReadTask : erreur " + e.toString());
            }

            Log.i(Util.TRACE, "[PathGoogleMapActivity] => ReadTask : data: " + data);
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            new ParserTask().execute(result);
        }
    }

    private class ParserTask extends
            AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(
                String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                PathJSONParser parser = new PathJSONParser();
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Log.i(Util.TRACE, "[PathGoogleMapActivity] => ParserTask : taille de routes: " + routes.size());
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
            ArrayList<LatLng> points = null;
            PolylineOptions polyLineOptions = null;

            Log.i(Util.TRACE, "[PathGoogleMapActivity] => onPostExecute : taille de routes: " + routes.size());

            // traversing through routes
            for (int i = 0; i < routes.size(); i++) {
                points = new ArrayList<LatLng>();
                polyLineOptions = new PolylineOptions();
                List<HashMap<String, String>> path = routes.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                polyLineOptions.addAll(points);
                polyLineOptions.width(2);
                polyLineOptions.color(Color.BLUE);
            }

            googleMap.addPolyline(polyLineOptions);
        }
    }
}