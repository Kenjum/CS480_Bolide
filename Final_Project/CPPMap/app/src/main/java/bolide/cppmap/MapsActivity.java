package bolide.cppmap;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.app.Activity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    GoogleMap mMap;
    /*
    LatLngBounds CalPolyPomona is used to create the boundry
    The new LatLng is the southwest corner, the second is the northeast
     */
    private LatLngBounds CalPolyPomona = new LatLngBounds( new LatLng(34.048359, -117.828218), new LatLng(34.065156, -117.806628));
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        The activity_maps.xml is reasponsible for the default camera location, type of map, initial zoom, ect.
         */
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        //Sets the boundry
        mMap.setLatLngBoundsForCameraTarget(CalPolyPomona);
        LatLng nearPomona = new LatLng(34.0554622,-117.8181957);

    }

}
