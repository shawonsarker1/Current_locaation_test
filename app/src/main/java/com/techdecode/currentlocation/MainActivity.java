package com.techdecode.currentlocation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    RequestQueue requestQueue;
    FusedLocationProviderClient fusedLocationProviderClient;
    TextView lattitude,longitude,address,city,country;
   TextView result;
    Button getLocation,insert_value;
    private final static int REQUEST_CODE = 100;

    String showUrl1="http://192.168.7.41/startech_Hrm/attendance_insert.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       

     /*  Intent dp=getIntent();
       String userName=dp.getStringExtra("username");
       result.setText("Hello "+userName);*/
      lattitude = findViewById(R.id.lattitude);
        longitude = findViewById(R.id.longitude);
        address = findViewById(R.id.address);
        city = findViewById(R.id.city);
        country = findViewById(R.id.country);
        getLocation = findViewById(R.id.getLocation);
        insert_value = findViewById(R.id.insert);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        //insert_database
        insert_value.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest request = new StringRequest(Request.Method.POST, showUrl1, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        Toast.makeText(getBaseContext(), "Data Insert Sucessfully!" ,Toast.LENGTH_LONG ).show();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        Map<String,String> parameters  = new HashMap<String, String>();

                        // Log.e("Log", "Shawon" + district.get(spinner_district.getSelectedItemPosition()).getName() );

                        parameters.put("lal",lattitude.getText().toString());
                        parameters.put("long",longitude.getText().toString());
                        parameters.put("add",address.getText().toString());
                        Log.e("Log", "Shawon" + address.getText().toString() );
                        parameters.put("city",city.getText().toString());
                        /*
                        parameters.put("Deliveryconnum",contact_num.getText().toString());
                        parameters.put("Deliveryemail",email2.getText().toString());

                        parameters.put("shipperad",address_se.getText().toString());
                        parameters.put("shippercity",city_1.getText().toString());
                        parameters.put("shipperstate",state_1.getText().toString());
                        parameters.put("shipperconname",con_name_1.getText().toString());
                        parameters.put("shipperconnum",contact_num1.getText().toString());
                        parameters.put("shipperemail",email1.getText().toString());
                       */



                        return parameters;
                    }
                };
                requestQueue.add(request);
            }
        });
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getLastLocation();

            }
        });







    }

    private void getLastLocation(){

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){


            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {

                            if (location != null){



                                try {
                                    Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                                    String result = null;
                                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                    lattitude.setText(""+ addresses.get(0).getLatitude());
                                    longitude.setText(""+ addresses.get(0).getLongitude());
                                    address.setText(addresses.get(0).getAddressLine(0));
                                    // city.setText("City: "+addresses.get(0).getLocality());
                                    country.setText(addresses.get(0).getCountryName());

                                    /*test*/
                                    if (addresses != null && addresses.size() > 0) {
                                        Address address = addresses.get(0);
                                        StringBuilder sb = new StringBuilder();
                                        for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                                            sb.append(address.getAddressLine(i)).append(",");//adress
                                        }
                                        sb.append(address.getLocality()).append(",");//village


                                        sb.append(address.getAdminArea()).append(","); //state

                                        sb.append(address.getSubAdminArea()).append(",");//district



                                        result = sb.toString();


                                        city.setText(result);
                                    }






                                } catch (IOException e) {
                                    e.printStackTrace();
                                }


                            }

                        }
                    });


        }else {

            askPermission();


        }


    }

    private void askPermission() {

        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @org.jetbrains.annotations.NotNull String[] permissions, @NonNull @org.jetbrains.annotations.NotNull int[] grantResults) {

        if (requestCode == REQUEST_CODE){

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){


                getLastLocation();

            }else {


                Toast.makeText(MainActivity.this,"Please provide the required permission",Toast.LENGTH_SHORT).show();

            }



        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}