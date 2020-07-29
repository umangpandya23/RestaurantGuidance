package com.example.restaurant_guidance.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.example.restaurant_guidance.R;
import com.example.restaurant_guidance.ui.AboutUsActivity;
import com.example.restaurant_guidance.ui.AuthenticationActivity;
import com.example.restaurant_guidance.ui.InformationActivity;
import com.example.restaurant_guidance.ui.TermsActivity;
import com.example.restaurant_guidance.ui.TutorialActivity;
import com.example.restaurant_guidance.ui.AuthenticationActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.amazonaws.mobile.client.UserStateDetails;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;

public class Home extends AppCompatActivity implements GoogleMap.OnMarkerClickListener,
        GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerDragListener, OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    Menu menu;
    TextView textView, name, username;
    LatLng maha;
    DatabaseReference reff;
    private Button btnMap;
    private Button btnList;
    private ImageButton imageButton,imageButton2;
    private LinearLayout llMaps;
    private LinearLayout llListing;
    private LinearLayout llTriggerButton;
    private RecyclerView lv;
    private RelativeLayout rlSecond;
    private GoogleMap mMap;
    RestuarentAdapter restuarentAdapter;
    private ArrayList<Restuarent> restaurentList;
    Restuarent rl;
    SupportMapFragment mapFragment;
    private double CurrentLat = 41.978651;
    private double CurrentLong= -87.842428;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        btnMap = (Button) findViewById(R.id.btnMap);
        imageButton = (ImageButton) findViewById(R.id.imageButton);
        imageButton2 = (ImageButton) findViewById(R.id.imageButton2);
        llTriggerButton = (LinearLayout) findViewById(R.id.llTriggerButtons);
        btnList = (Button) findViewById(R.id.btnList);
        llListing = (LinearLayout) findViewById(R.id.llListingsList);
        llMaps = (LinearLayout) findViewById(R.id.llMap);
        rlSecond = (RelativeLayout) findViewById(R.id.rlSecond);
        maha = new LatLng(CurrentLat, CurrentLong);
        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);

        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View view) {
                if(!drawerLayout.isDrawerOpen(Gravity.START)) drawerLayout.openDrawer(Gravity.START);
                else drawerLayout.closeDrawer(Gravity.END);
            }
        });

        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        restaurentList = new ArrayList<Restuarent>();
        reff = FirebaseDatabase.getInstance().getReference().child("Restuarent");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ss : snapshot.getChildren()) {
                    Restuarent r = new Restuarent();
                    r.setName(ss.child("name").getValue().toString());
                    r.setAddress(ss.child("address").getValue().toString());
                    r.setContact_number(ss.child("contact_number").getValue().toString());
                    r.setUrl(ss.child("url").getValue().toString());
                    r.setInfo(ss.child("info").getValue().toString());
                    r.setCity(ss.child("city").getValue().toString());
                    r.setState(ss.child("state").getValue().toString());
                    r.setZipcode(ss.child("zipcode").getValue().toString());
                    double lat = (double) ss.child("lat").getValue();
                    double log = (double) ss.child("log").getValue();
                    r.setLog(log);
                    r.setLat(lat);
                    DecimalFormat df = new DecimalFormat("#.##");
                    String formatted = df.format(distance(CurrentLat,r.getLat(),CurrentLong,r.getLog()));
                    r.setDistance(formatted);
                    restaurentList.add(r);
                    Log.i("info", r.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        restaurentList.sort(new Comparator<Restuarent>() {
            @Override
            public int compare(Restuarent restuarent, Restuarent t1) {
                double res = Double.parseDouble(restuarent.getDistance());
                double relist = Double.parseDouble(t1.getDistance());
                if (res == relist)
                    return 0;
                else if (res > relist)
                    return 1;
                else
                    return -1;
            }
        });

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setUpMapIfNeeded();

        name = (TextView) findViewById(R.id.name);
        username = (TextView) findViewById(R.id.username);

        lv = findViewById(R.id.lvResults);
        restuarentAdapter = new RestuarentAdapter(this, restaurentList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        lv.setLayoutManager(layoutManager);
        lv.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        lv.setAdapter(restuarentAdapter);

        btnMap.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                llMaps.setVisibility(View.VISIBLE);
                llListing.setVisibility(View.GONE);

                btnList.setBackgroundResource(R.drawable.ovalnotselected);
                btnList.setTextColor(getColor(R.color.colorWhite));

                btnMap.setBackgroundResource(R.drawable.ovalselected);
                btnMap.setTextColor(getColor(R.color.colorBlack));
                addMarkersToMap();
            }
        });

        btnList.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                llMaps.setVisibility(View.GONE);
                llListing.setVisibility(View.VISIBLE);

                btnList.setBackgroundResource(R.drawable.ovalselected);
                btnList.setTextColor(getColor(R.color.colorBlack));

                btnMap.setBackgroundResource(R.drawable.ovalnotselected);
                btnMap.setTextColor(getColor(R.color.colorWhite));

            }
        });
        btnMap.performClick();

    }

    @Override
    public void onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {super.onBackPressed();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        mMap.addMarker(new MarkerOptions().position(maha).title("Current Location."));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(maha,10));
   }

    private void addMarkersToMap() {
        //get density
        for (Restuarent r : restaurentList) {
            Resources resources = getBaseContext().getResources();
            float scale = resources.getDisplayMetrics().density;

            // Load the marker image
            Bitmap myRefBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.house);

            // Create a mutable bitmap with the same size as the marker
            // image
            Bitmap myWrittenBitmap = Bitmap.createBitmap(myRefBitmap.getWidth(), myRefBitmap.getHeight(), Bitmap.Config.ARGB_4444);
            myWrittenBitmap.setDensity(300);
            // Create a Canvas on which to draw and a Paint to write text.
            Canvas canvas = new Canvas(myWrittenBitmap);
            Paint txtPaint = new Paint();
            txtPaint.setColor(Color.rgb(92, 82, 76));
            txtPaint.setTextSize((int) (5 * scale));
            txtPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
            txtPaint.setTypeface(Typeface.DEFAULT_BOLD);
            txtPaint.setTextAlign(Paint.Align.CENTER);

            // Draw ref bitmap then text on our canvas
            canvas.drawBitmap(myRefBitmap, 0, 0, null);
            Bitmap icon = null;
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(r.getLat(), r.getLog()))
                    .title(r.getName())
                    .snippet(r.getAddress() + " " + r.getCity() + " " + r.getState()
                            + " " + r.getZipcode())
                    .icon(BitmapDescriptorFactory.fromBitmap(myWrittenBitmap)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(r.getLat(), r.getLog()), 4));
        }
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the
        // map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    mMap = googleMap;
                    setUpMap();

                }
            });
            // Check if we were successful in obtaining the map.

        }
        if (mMap != null) {
            setUpMap();
        }
    }

    private void setUpMap() {
        // Hide the zoom controls as the button panel will cover it.
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMarkerDragListener(this);
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            public void onInfoWindowClick(Marker marker) {
                // TODO Auto-generated method stub
                Log.i("info","Clicked on marker  111");
                LatLng ll = marker.getPosition();
                double latTemp = ll.latitude;
                double longTemp = ll.longitude;
                Intent intent = new Intent(Home.this, InformationActivity.class);
                for(Restuarent res : restaurentList){
                    if((res.getLat()==latTemp) && (res.getLog()==longTemp)){
                        rl = res;
                        break;
                    }
                }
                Bundle b = new Bundle();
                b.putString("name",rl.getName() );
                b.putString("address", rl.getAddress());
                b.putString("city", rl.getCity());
                b.putString("state", rl.getState());
                b.putString("zipcode",rl.getZipcode());
                b.putString("contact_number",rl.getContact_number());
                b.putString("url",rl.getUrl());
                b.putString("info",rl.getInfo());
                b.putString("distance", rl.getDistance());
                b.putDouble("lat",rl.getLat());
                b.putDouble("log",rl.getLog());
                intent.putExtras(b);
                startActivity(intent);

            }
        });
        addMarkersToMap();
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
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

    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 16));
        return true;
    }

    public static Double distance(double lat1,
                                  double lat2, double lon1,
                                  double lon2)
    {

        // The math module contains a function
        // named toRadians which converts from
        // degrees to radians.
        lon1 = Math.toRadians(lon1);
        lon2 = Math.toRadians(lon2);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        // Haversine formula
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2),2);

        double c = 2 * Math.asin(Math.sqrt(a));

        // Radius of earth in kilometers. Use 3956
        // for miles
        double r = 6371;

        // calculate the result
        return(c * r * 0.621371);
    }

    public void inform(int ResToShow){
        Intent intent = new Intent(Home.this, InformationActivity.class);
        rl = restaurentList.get(ResToShow);
        Bundle b = new Bundle();
        b.putString("name",rl.getName() );
        b.putString("address", rl.getAddress());
        b.putString("city", rl.getCity());
        b.putString("state", rl.getState());
        b.putString("zipcode",rl.getZipcode());
        b.putString("contact_number",rl.getContact_number());
        b.putString("url",rl.getUrl());
        b.putString("info",rl.getInfo());
        b.putString("distance", rl.getDistance());
        b.putDouble("lat",rl.getLat());
        b.putDouble("log",rl.getLog());
        intent.putExtras(b);
        startActivity(intent);
    }

    public void clearScreen(){
        rlSecond.setVisibility(View.GONE);
    }
    public void HomeScreen(){
        rlSecond.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                break;

            case R.id.nav_tutorial:
                Intent intent3 = new Intent(Home.this, TutorialActivity.class);
                startActivity(intent3);
                break;

            case R.id.nav_about_us:
                Intent intent1 = new Intent(Home.this, AboutUsActivity.class);
                startActivity(intent1);
                break;

            case R.id.nav_need_help:
                Intent mEmailIntent = new Intent(Intent.ACTION_SEND);
                mEmailIntent.setData(Uri.parse("mailto:"));
                mEmailIntent.setType("text/plain");
                mEmailIntent.putExtra(Intent.EXTRA_EMAIL,new String[]{"umangrakeshkumar.pandya@ucdenver.edu"});
                try {
                    startActivity(Intent.createChooser(mEmailIntent,"Choose an E-mail Client"));
                }catch (Exception e){
                    Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.nav_terms:
                Intent intent2 = new Intent(Home.this, TermsActivity.class);
                startActivity(intent2);
                break;

            case R.id.nav_logout:
                Intent intent = new Intent(Home.this, AuthenticationActivity.class);
                AWSMobileClient.getInstance().signOut();
                startActivity(intent);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START); return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}