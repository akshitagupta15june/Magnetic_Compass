package com.example.compass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements LocationListener {
    private static final String TAG="MainActivity";
    private Compass compass;
    private ImageView dial;
    private ImageView hand;
    private TextView label;
    private  float currentAzimuth;
    private TextView geo;
    private LocationManager locationManager;
    private  SOWTFormatter sowtFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sowtFormatter=new SOWTFormatter(this);
        label=findViewById(R.id.label);
        geo=findViewById(R.id.geo);
        dial=findViewById(R.id.dial);
        hand=findViewById(R.id.hand);
        locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);

        onLocationChanged(location);
        setupCompass();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"start compass");
        compass.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        compass.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        compass.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"stop compass");
        compass.stop();
    }

    private void setupCompass() {

        compass=new Compass(this);
        Compass.CompassListener cl=getCompassListener();
        compass.setListener(cl);
    }
    private void adjustArrow(float azimuth){
        Log.d(TAG,"will set rotation from "+currentAzimuth +" to "+azimuth);
        Animation an=new RotateAnimation(-currentAzimuth,-azimuth,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        currentAzimuth=azimuth;
        an.setDuration(500);
        an.setRepeatCount(0);
        an.setFillAfter(true);
        hand.startAnimation(an);

    }
    private void  adjustSotwLabel(float azimuth){
        label.setText(sowtFormatter.format(azimuth));
    }

    private Compass.CompassListener getCompassListener() {
        return new Compass.CompassListener() {
            @Override
            public void onNewAzimuth(final float azimuth) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adjustArrow(azimuth);
                        adjustSotwLabel(azimuth);

                    }
                });
            }
        };
    }

    @Override
    public void onLocationChanged(Location location) {
        double longitude=location.getLongitude();
        double lattitude=location.getLatitude();
        geo.setText("longitude: " + longitude + "\n" + "latitude: " + lattitude);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
