package com.example.restaurant_guidance.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.example.restaurant_guidance.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class InformationActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    LatLng latlng;
    private static final int REQUEST_CALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        final Bundle extras = getIntent().getExtras();
        TextView name = (TextView) findViewById(R.id.textViewName);
        name.setText(extras.get("name").toString());
        TextView address = (TextView) findViewById(R.id.textViewAddress);
        address.setText(extras.get("address").toString());
        TextView city = (TextView) findViewById(R.id.textViewCity);
        city.setText(extras.get("city").toString() + ", " + extras.get("state").toString() + ", " +extras.get("zipcode").toString());
        TextView distance = (TextView) findViewById(R.id.textViewDistance);
        distance.setText(extras.get("distance").toString() + " mi");
        TextView info = (TextView) findViewById(R.id.editTextInfo);
        info.setText(extras.get("info").toString());

        TextView link = (TextView) findViewById(R.id.textViewLink);
        link.setText(extras.get("url").toString());
        ImageButton imageButton = (ImageButton) findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(extras.get("url").toString()));
                startActivity(intent);
            }
        });

        latlng = new LatLng(Double.parseDouble(extras.get("lat").toString()),Double.parseDouble(extras.get("log").toString()));
        Button call = (Button) findViewById(R.id.buttonCall);
        Button direction = (Button) findViewById(R.id.buttonDirection);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync( this);

        call.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                makePhoneCall(extras.get("contact_number").toString());
            }
        });

        direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + latlng.latitude + "," + latlng.longitude + "&mode=d");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        Marker marker = mMap.addMarker(new MarkerOptions().position(latlng));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 16));

        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(false);
    }
    public void onMarkerDrag(Marker marker) {
        // TODO Auto-generated method stub

    }

    public void onMarkerDragEnd(Marker marker) {
        // TODO Auto-generated method stub

    }

    public void onMarkerDragStart(Marker marker) {
        // TODO Auto-generated method stub

    }

    public void onInfoWindowClick(Marker marker) {
        // TODO Auto-generated method stub
        Log.i("info","Clicked on marker ");

    }

    public void makePhoneCall(String number){
            if(number.isEmpty()){
                Toast.makeText(getApplicationContext(),"No Number provided by restuarent.",Toast.LENGTH_LONG).show();
            }
            else{
                    startActivity(new Intent(Intent.ACTION_CALL,Uri.parse("tel:" + number)));
            }
    }
}