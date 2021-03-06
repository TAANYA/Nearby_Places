package com.example.tanya.nearbyplaces;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    EditText editText;
    ImageButton btn;
    private static ArrayList<Model_Places> places = new ArrayList<Model_Places>();
    String base_url = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=%s&key=AIzaSyCcAOkDBjPGjX1iT4RtzOQMnHjOD-r9iuU";

    public Model_Places model_places;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        editText = (EditText) findViewById(R.id.editText);
        btn = (ImageButton) findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String place = editText.getText().toString();

                String url = String.format(base_url, place);

                GetData data = new GetData();
                data.execute(url);


            }
        });


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }


    public class GetData extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            String url = strings[0];
            try {
                URL url1 = new URL(url);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url1.openConnection();
                InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder result = new StringBuilder();

                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    result.append(line);
                }

                Log.i("dsatatatatatta", "doInBackground: " + result.toString());
                parseJSON(result.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        private void parseJSON(String s) {

            try {
                JSONObject object = new JSONObject(s);
                JSONArray arr = object.getJSONArray("results");

                places.clear();

                for (int i = 0; i < arr.length(); i++) {
                    String name = ((JSONObject) arr.get(i)).getString("name");
                    double lng = ((JSONObject) arr.get(i)).getJSONObject("geometry").getJSONObject("location").getDouble("lng");

                    double lat = ((JSONObject) arr.get(i)).getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                    String icon = ((JSONObject) arr.get(i)).getString("icon");


                    model_places = new Model_Places(lat, lng, name, icon);

                    places.add(model_places);

                }
                Log.d("abc", "parseJSON: " + places);

                //Log.i("loggggg", "parseJSON: "+ arr.length());
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            for (int i = 0; i < places.size(); i++) {
                addMarkerToMap(places.get(i));
            }
        }

        private void addMarkerToMap(final Model_Places model_places) {
            LatLng sydney = new LatLng(model_places.getLat(), model_places.getLng());
            mMap.addMarker(new MarkerOptions().position(sydney).title(model_places.getName()));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            mMap.moveCamera(CameraUpdateFactory.zoomBy(6f));
            {


            }

        }
    }
}
