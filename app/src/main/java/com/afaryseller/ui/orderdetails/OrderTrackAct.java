package com.afaryseller.ui.orderdetails;

import android.animation.ValueAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.afaryseller.R;
import com.afaryseller.databinding.ActivityOrderTrackBinding;
import com.afaryseller.utility.DrawPollyLine;
import com.afaryseller.utility.LatLngInterpolator;
import com.afaryseller.utility.MarkerAnimation;
import com.afaryseller.utility.MyService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;

import org.json.JSONObject;

import java.util.ArrayList;

public class   OrderTrackAct extends AppCompatActivity implements OnMapReadyCallback {
    ActivityOrderTrackBinding binding;
    SupportMapFragment mapFragment;
    GoogleMap mMap;

    private Marker currentLocationMarker;
    MarkerOptions sellerLocationMarker,userLocationMarker;

    Location currentLocation;
    private long startTimeNew = 0L;
    boolean isMarkerRotating = false;

    private LatLng pickLocation, droplocation;

    OrderDetailsModel.Result model;


    BroadcastReceiver DriverLocationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("DriverLocationReceiver ====","=====");
            try {
                JSONObject object = new JSONObject(intent.getStringExtra("object"));
                if(intent.getStringExtra("object")!= null) {
                    JSONObject result = object.getJSONObject("result");
                    Log.e("DriverLocationReceiver ====",intent.getStringExtra("object")+"");

                    if(object.getString("status").equals("1")){
                        showMarkerCurrentLocation(new LatLng(Double.parseDouble(result.getString("current_latitude")),Double.parseDouble(result.getString("current_longitude"))));
                    }


                    else {
                        Toast.makeText(OrderTrackAct.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                        // startActivity(new Intent(getActivity.this,HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        // finish();
                    }


                }


            }
            catch (Exception e){
                e.printStackTrace();
            }

        }
    };





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_order_track);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        initViews();
    }

    private void initViews() {

        if(getIntent()!=null){
            model = (OrderDetailsModel.Result)getIntent().getSerializableExtra("orderDetails");
            binding.tvOrderId.setText(getString(R.string.order_id)+ " " + model.getDeliveryPerson().getOrderId());
            binding.tvOrderStatus.setText(getString(R.string.order_status)+" " + model.getStatus());
            pickLocation = new LatLng(Double.parseDouble(model.getProductList().get(0).getShopLat()),Double.parseDouble(model.getProductList().get(0).getShopLon()));
            droplocation = new LatLng(Double.parseDouble(model.getDeliverLat()),Double.parseDouble(model.getDeliverLon()));

        }


        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(OrderTrackAct.this);


        startService(new Intent(OrderTrackAct.this, MyService.class));




        sellerLocationMarker = new MarkerOptions().title("seller Location")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_marker));

        userLocationMarker = new MarkerOptions().title("User Location")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.blue_marker));

       binding.backNavigation.setOnClickListener(view -> finish());

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMapToolbarEnabled(false);


    }




/*
    private void showMarkerCurrentLocation(@NonNull LatLng currentLocation) {
        if (currentLocation != null) {
            if (currentLocationMarker == null) {
                if (mMap != null) {
                    currentLocationMarker = mMap.addMarker(new MarkerOptions().position(currentLocation).title("My Location")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.car_top))
                            .flat(true).anchor(0.5f,0.5f).rotation(180));
                    animateCamera(currentLocation);
                    if(pickLocation!=null) {
                        userLocationMarker = mMap.addMarker(new MarkerOptions().position(pickLocation).title("User Location")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_marker)));
                    }
                }
            } else {
                Log.e("Driver Location", "Hello Marker Animation");
                // animateCamera(currentLocation);
                MarkerAnimation.animateMarkerToGB(currentLocationMarker, currentLocation, new LatLngInterpolator.Spherical());
                //   rotateMarker(currentLocationMarker, (float) bearingBetweenLocations(currentLocationMarker.getPosition(),currentLocation));
                rotateMarker11(currentLocationMarker,(float) bearingBetweenLocations(currentLocationMarker.getPosition(),currentLocation));

            }
        }
    }
*/


    private void showMarkerCurrentLocation(@NonNull LatLng currentLocation) {
        if (currentLocation != null) {
            if (currentLocationMarker == null) {
                currentLocationMarker = mMap.addMarker(new MarkerOptions().position(currentLocation).title("My Location")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.car_top))
                        .flat(true).anchor(0.5f,0.5f).rotation(180));
                animateCamera(currentLocation);
                // createCircle(currentLocationMarker,100.0);
           if(model.getStatus().equalsIgnoreCase("PickedUp")) {
               DrawPollyLine.get(this)
                       .setOrigin(pickLocation)
                       .setDestination(droplocation)
                       .execute(new DrawPollyLine.onPolyLineResponse() {
                           @Override
                           public void Success(ArrayList<LatLng> latLngs) {
                               PolylineOptions options = new PolylineOptions();
                               options.addAll(latLngs);
                               options.color(getColor(R.color.colorPrimary));
                               options.width(10);
                               options.startCap(new SquareCap());
                               options.endCap(new SquareCap());
                               Polyline line = mMap.addPolyline(options);
                           }
                       });
           }
       //    else {
        //       sellerLocationMarker.position(pickLocation);
        //       mMap.addMarker(sellerLocationMarker);

        //   }


            } else {
                drawRoute(currentLocation);
            }
        }


    }






    public void rotateMarker11(final Marker marker, final float toRotation) {
        long d = 2000;
        ValueAnimator animator = ValueAnimator.ofFloat(marker.getRotation(), toRotation);
        animator.setDuration(d);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float rotate = Float.parseFloat(animation.getAnimatedValue().toString());
                marker.setRotation(rotate);
            }
        });
        animator.start();
    }

    private void animateCamera(@NonNull LatLng location) {
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(getCameraPositionWithBearing(location)));
    }

    @NonNull
    private CameraPosition getCameraPositionWithBearing(LatLng latLng) {
        return new CameraPosition.Builder().target(latLng).zoom(16).build();
    }

    private double bearingBetweenLocations(LatLng latLng1, LatLng latLng2) {
        double PI = 3.14159;
        double lat1 = latLng1.latitude * PI / 180;
        double long1 = latLng1.longitude * PI / 180;
        double lat2 = latLng2.latitude * PI / 180;
        double long2 = latLng2.longitude * PI / 180;
        double dLon = (long2 - long1);
        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
                * Math.cos(lat2) * Math.cos(dLon);
        double brng = Math.atan2(y, x);
        brng = Math.toDegrees(brng);
        brng = (brng + 360) % 360;
        return brng;
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(DriverLocationReceiver, new IntentFilter("check_driver_location"));

    }


    @Override
    public void onStop() {
        super.onStop();
        stopService(new Intent(OrderTrackAct.this, MyService.class));
        unregisterReceiver(DriverLocationReceiver);

    }



    private void drawRoute(LatLng currentLocation) {
        // if(chkMap==false) {

        if(currentLocationMarker== null) {
            currentLocationMarker = mMap.addMarker(new MarkerOptions().position(currentLocation).title("My Location")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.car_top))
                    .flat(true).anchor(0.5f, 0.5f).rotation(180));
        }
        animateCamera(currentLocation);
        MarkerAnimation.animateMarkerToGB(currentLocationMarker,currentLocation, new LatLngInterpolator.Spherical());
        rotateMarker11(currentLocationMarker,(float) bearingBetweenLocations(currentLocationMarker.getPosition(),currentLocation));
      if(model.getStatus().equalsIgnoreCase("Accepted")){
          sellerLocationMarker.position(pickLocation);
          mMap.addMarker(sellerLocationMarker);

      }
      else {
          sellerLocationMarker.position(pickLocation);
          userLocationMarker.position(droplocation);
          mMap.addMarker(sellerLocationMarker);
          mMap.addMarker(userLocationMarker);
      }

        //   chkMap = true;
        Log.e("Driver Location","Hello Marker Animation==");
        //  }else/*{
        //   currentLocationMarker = mMap.addMarker(new MarkerOptions().position(currentLocation).title("My Location")
        //           .icon(BitmapDescriptorFactory.fromResource(R.drawable.car_top))
        //           .flat(true).anchor(0.5f,0.5f).rotation(180));
        //   animateCamera(currentLocation);
        //    MarkerAnimation.animateMarkerToGB(currentLocationMarker,currentLocation, new LatLngInterpolator.Spherical());
        //    rotateMarker11(currentLocationMarker,(float) bearingBetweenLocations(currentLocationMarker.getPosition(),currentLocation));
        //    chkMap = true;
        //     Log.e("Driver Location","Hello Marker Animation2222==");

        //  }*/





    }


}
