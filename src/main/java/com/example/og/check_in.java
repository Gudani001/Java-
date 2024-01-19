package com.example.og;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class check_in extends AppCompatActivity {
EditText address;
    TextView date;
    Button getLocation,confirm;
    public LocationManager locationManager;
    public LocationListener locationListener = new MyLocationListener();
    String lat, lon;
    private boolean gps_enable = false;
    private boolean network_enable = false;
    //address generating
    Geocoder geocoder;
    List<Address> myaddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);

         date= findViewById(R.id.date);
        address = findViewById(R.id.address);
        getLocation =  findViewById(R.id.getLocation);
        confirm =  findViewById(R.id.confirm);
        //name = findViewById(R.id.name);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMyLocation();
            }
        });


        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        date.setText(currentDate);


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //get  username from login activity

                SharedPreferences sharedPreferences = getSharedPreferences("myKey", MODE_PRIVATE);
                String Name = sharedPreferences.getString("sUser","");

                // takes date and address to make it a string
                String Date = date.getText().toString().trim();
                String Address = address.getText().toString().trim();
                ContentValues contentValues = new ContentValues();

                contentValues.put("name",Name);
                contentValues.put("date",Date);
                contentValues.put("address",Address);


                CheckIn(check_in.this, contentValues);

            }
        });

//calls a function to check location permission
        checkLocationPermission();
    }

    class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                locationManager.removeUpdates(locationListener);
                lat = "" + location.getLatitude();
                lon = "" + location.getLongitude();

                geocoder = new Geocoder(check_in.this, Locale.getDefault());

                try {
                    myaddress = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String address1 = myaddress.get(0).getAddressLine(0);
                address.setText(address1);
            }
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

    public void getMyLocation() {
        try {
            gps_enable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {

        }
        try {
            network_enable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {

        }
        if (!gps_enable && !network_enable) {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(check_in.this);
            builder.setTitle("Attention");
            builder.setTitle("please enable location and internet");
            builder.create().show();

        }
        if (gps_enable) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }
        if(network_enable){
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }
    }
    private boolean checkLocationPermission(){
        int location = ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION);
        int location2 =ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION);

        List<String> listPermission = new ArrayList<>();
        if (location != PackageManager.PERMISSION_GRANTED){
            listPermission.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (location2 != PackageManager.PERMISSION_GRANTED){
            listPermission.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if(!listPermission.isEmpty()){
            ActivityCompat.requestPermissions(this,listPermission.toArray(new String[listPermission.size()]),
                    1);
        }
        return true;
    }
    private static void CheckIn(final Context c, ContentValues contentValues) {


        new server("https://lamp.ms.wits.ac.za/home/s1853035/OG_LOC.php", contentValues) {
            @Override
            protected void onPostExecute(String output) {

                if (output.equals("1")) {
                    Toast.makeText(c, "successful check in", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(c, "check in fail", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

}