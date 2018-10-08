package e.q.represent;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import android.content.Context;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Geocoder gc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gc = new Geocoder(getApplicationContext(), Locale.getDefault());
    }


    public void sendMessage(View view) {
        Intent intent = new Intent(this, SearchView.class);
        intent.putExtra("source", "normal");
        startActivity(intent);
    }

    public void sendRandomMessage(View view) {
        Random rand = new Random();
        int n = rand.nextInt(100000);
        String regex = "\\d{5}";
        String zipcode = Integer.toString(n);
        while(!zipcode.matches(regex)){
            n = rand.nextInt(100000);
            zipcode = Integer.toString(n);
        }
        Intent intent = new Intent(this, SearchView.class);

        intent.putExtra("source", "random");
        intent.putExtra("zipcode", zipcode);
        startActivity(intent);
    }

    public void sendCurrentMessage(View view) throws IOException {
        LocationManager locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);


        LocationListener locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(android.location.Location location) {
                double latitude=location.getLatitude();
                double longitude=location.getLongitude();
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
        };
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000L,500.0f, locationListener);
        Location location = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        double latitude=0;
        double longitude=0;
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        List<Address> addresses = gc.getFromLocation(latitude, longitude, 10);

        String zipcode = addresses.get(0).getPostalCode();
        Intent intent = new Intent(this, SearchView.class);
        //Create the bundle
        intent.putExtra("source", "current");
        intent.putExtra("zipcode", zipcode);
        startActivity(intent);
    }



}
