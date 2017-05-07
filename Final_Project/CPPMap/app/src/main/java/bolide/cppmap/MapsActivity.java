package bolide.cppmap;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

//imports for spinner
import java.util.ArrayList;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,OnMyLocationButtonClickListener, ActivityCompat.OnRequestPermissionsResultCallback {
    GoogleMap mMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean mPermissionDenied = false;
    /*
    //Spinner for drop down menu
    Spinner spinner = (Spinner) findViewById(R.id.spinner);
    // Create an ArrayAdapter using the string array and a default spinner layout
    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
            R.array.locations_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
spinner.setAdapter(adapter);
    */
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
        //Set max Zoom
        mMap.setMinZoomPreference(15);
        //Get Location of the user
        mMap.setOnMyLocationButtonClickListener(this);
        enableMyLocation();
        buildBuildings();




    }
    private void enableMyLocation() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Asks the user Permission to access the location if the permission was missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE, Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }
    @Override
    public boolean onMyLocationButtonClick() {
        return false;

    }





    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }
        if (PermissionUtils.isPermissionGranted(permissions, grantResults, Manifest.permission.ACCESS_FINE_LOCATION)) {

            // Enable the my location layer if the permission has been granted.

            enableMyLocation();

        } else {

            // Display the missing permission error dialog when the fragments resume.

            mPermissionDenied = true;

        }

    }



    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog.newInstance(true).show(getSupportFragmentManager(), "dialog");
    }
    //function for placing building markers
    private void buildBuildings(){

        // 1: Building One
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.059499, -117.824131))
                .title("1: Building One"));
        // 2: College of Agriculture
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.057725, -117.826502))
                .title("2: College of Agriculture"));
        // 3: Science Laboratory
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.058562, -117.825102))
                .title("3: Science Laboratory"));
        // 4: Biotechnology Building
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.057172, -117.826655))
                .title("4: Biotechnology Building"));
        //4A: Bio Trek Learning Center
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.057213, -117.826046))
                .title("4A: Bio Trek Learning Center"));
        // 5: College of Letters, Arts and Social Sciences
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.057476, -117.825196))
                .title("5: College of Letters, Arts and Social Sciences"));
        // 6: College of Education and Integrative Studies
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.058589, -117.822823))
                .title("6: College of Education and Integrative Studies"));
        // 7: College of Environmental Design
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.057044, -117.827392))
                .title("7: College of Environmental Design"));
        // 8: College of Science Building
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.058562, -117.825102))
                .title("8: College of Science Building"));
        //9: College of Engineering
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.059093, -117.822404))
                .title("9: College of Engineering "));
        //13: Art Department and Engineering Annex
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.058657, -117.820977))
                .title("13: Art Department and Engineering Anex"));
        //13B: Army ROTC
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.058238, -117.820787))
                .title("13B: Army ROTC "));
        //13C: Army ROTC
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.058355, -117.820678))
                .title("13C: Army ROTC "));
        //13D: Pre-College TRIO Programs
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.058481, -117.820435))
                .title("13D: Pre-College TRIO Programs"));
        //15: Library
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.057887, -117.821539))
                .title("15: Library"));
        //17: Engineering Laboratories
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.059630, -117.821398))
                .title("17: Engineering Laboratories"));
        //20: Residence Hall, Enchinitas
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.062248, -117.820642))
                .title("20: Residence Hall, Enchinitas"));
        //21: Residence Hall, Montecito
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.062081, -117.819266))
                .title("21: Residence Hall, Montecito"));
        //22: Residence Hall, Alamitos
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.062114, -117.818024))
                .title("22: Residence Hall, Alamitos"));
        //23: Resedence Hall, Aliso
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.062845, -117.817848))
                .title("23: Resedence Hall, Aliso"));
        //24: Music
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.056936, -117.822586))
                .title("24: Music"));
        //24A: Temp Class A
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.056280, -117.822476))
                .title("24A: Temp Class A"));
        //24B: Temp Class B
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.056166, -117.822405))
                .title("24B: Temp Class B"));
        //24C: Temp Class C
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.055930, -117.822375))
                .title("24C: Temp Class C"));
        //24D: Temp Class D
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.056022, -117.822510))
                .title("24D: Temp Class D"));
        //24E: Temp Class E
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.056172, -117.822624))
                .title("24E: Temp Class E"));
        //25: Drama Department/Theatre
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.056447, -117.822122))
                .title("25: Drama Department/Theatre"));
        //26: University Plaza
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.056950, -117.820554))
                .title("26: University Plaza"));
        //26A: Student Orientation Center
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.056709, -117.820577))
                .title("26A: Student Orientation Center"));
        //28: Fruit/crops Unit
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.059874, -117.810962))
                .title("28: Fruit/crops Unit"));
        //29: W.K. Kellogg Arabian Horse Center
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.058483, -117.814486))
                .title("29: W.K. Kellogg Arabian Horse Center"));
        //30: Agricultural Unit
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.055163, -117.828428))
                .title("30: Agricultural Unit"));
        //31: Poultry Unit/Poultry Houses
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.054615, -117.828307))
                .title("30: Agricultural Unit"));
        //32:Beef Unit/ Feed Shed
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.055488, -117.827618))
                .title("32:Beef Unit/ Feed Shed"));
        //33: Feed Mill
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.053965, -117.827521))
                .title("33: Feed Mill"));
        //34: Meat Laboratory
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.053952, -117.828074))
                .title("34: Meat Laboratory "));
        //35: BSC - Bronco Student Center
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.056285, -117.821159))
                .title("35: BSC - Bronco Student Center"));
        //35A: W. Keith and Janet Kellogg University Art Gallery
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.057001, -117.821712))
                .title("35A: W. Keith and Janet Kellogg University Art Gallery"));
        //37: Swine Unit/Shelters
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.052152, -117.822887))
                .title("37: Swine Unit/Shelters"));
        //38: Sheep/Wool Unit
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.052159, -117.822381))
                .title("38: Sheep/Wool Unit"));
        //41: Darlene May Gymnasium
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.054076, -117.821229))
                .title("41: Darlene May Gymnasium"));
        //42: BRIC - Bronco Recreation Intramural Complex
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.054684, -117.820473))
                .title("42: BRIC - Bronco Recreation Intramural Complex"));
        //43: Kellogg Gymnasium 44: swimming pool
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.054263, -117.819278))
                .title("43: Kellogg Gymnasium\n44: swimming pool"));
        //45: Agricultural Engineering
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.061132, -117.811036))
                .title("45: Agricultural Engineering"));
        //46: Health Services
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.057830, -117.827853))
                .title("46: Health Services"));
        //47: Agricultural Engineering Tractor Shop
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.061781, -117.815016))
                .title("47: Agricultural Engineering Tractor Shop"));
        //48: Custodial Offices
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.061110, -117.815418))
                .title("48: Custodial Offices"));
        //49: Training Center
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.061381, -117.815756))
                .title("49: Training Center"));


    }


    //function for placing Parking Lot markers
    private void buildParkingLots(){

    }
    //function for placing markers of other points of interest
    private void buildOtherAreas(){

    }
    //function for placing Path markers
    private void buildPath(){

    }





}
