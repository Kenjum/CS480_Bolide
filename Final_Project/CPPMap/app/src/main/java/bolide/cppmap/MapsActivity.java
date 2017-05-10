package bolide.cppmap;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

//imports for spinner
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, DirectionFinderListener, OnMyLocationButtonClickListener, ActivityCompat.OnRequestPermissionsResultCallback {
    GoogleMap mMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean mPermissionDenied = false;

    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;

    /*
    LatLngBounds CalPolyPomona is used to create the boundry
    The new LatLng is the southwest corner, the second is the northeast
     */

    private LatLngBounds CalPolyPomona = new LatLngBounds( new LatLng(34.048359, -117.828218), new LatLng(34.065156, -117.806628));
    Marker testMarker;
    Marker building[] = new Marker[220];
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

        //TODO for navigation
        Button btnGo = (Button) findViewById(R.id.btnGo);

        btnGo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });


        //Spinner
        Spinner viewSpinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter viewAdapter = ArrayAdapter.createFromResource(this,R.array.locations_array,R.layout.support_simple_spinner_dropdown_item);
        viewSpinner.setAdapter(viewAdapter);
        viewSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position ==0){

                }else {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(building[position].getPosition(), 20));
                    building[position].showInfoWindow();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //Sets the boundry
        mMap.setLatLngBoundsForCameraTarget(CalPolyPomona);
        LatLng nearPomona = new LatLng(34.0554622,-117.8181957);
        //Set max Zoom
        mMap.setMinZoomPreference(15);
        //Get Location of the user
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                    @Override
                    public View getInfoWindow(Marker marker) {
                        return null;
                    }
                    @Override
                    public View getInfoContents(Marker marker) {
                        View v = getLayoutInflater().inflate(R.layout.custom_info_window, null);
                        ImageView image = (ImageView) v.findViewById(R.id.marker_image);
                        TextView Title = (TextView) v.findViewById(R.id.title);
                        TextView discription = (TextView) v.findViewById(R.id.description);
                        image.setImageBitmap((Bitmap)marker.getTag());
                        discription.setText(marker.getSnippet());
                        Title.setText(marker.getTitle());
                        return v;
                    }
                });
                return false;
            }
        });
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

    public void sendRequest(){  //TODO

//      .getPosition.toString() yields "(coord, coord)". This is to remove the parenthesis.
        String test1 = "(34.057777, -117.823837)".replaceAll("[()]", "");
        String test2 = "Cal Poly Pomona";
        try{
            new DirectionFinder(this,test1,test2).execute();
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }
    }

    //function for placing building markers
    private void buildBuildings(){

        // 1: Building One
        building[1] = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.059499, -117.824131))
                .title("1: Building One")
                .snippet("Holds the Departments of Communication, Economics, and Philosophy. It also\n" +
                        "contains the home of IT Services and the Kellogg Honors College."));
        building[1].setTag(BitmapFactory.decodeResource(getResources(),R.drawable.building1));
        // 2: College of Agriculture
        building[2] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.057725, -117.826502))
                .title("2: College of Agriculture"));
        // 3: Science Laboratory
        building[3] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.058101, -117.825659))
                .title("3: Science Laboratory"));
        // 4: Biotechnology Building
        building[4] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.057479, -117.825380))
                .title("4: Biotechnology Building"));
        //4A: Bio Trek Learning Center
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.057213, -117.826046))
                .title("4A: Bio Trek Learning Center"));
        // 5: College of Letters, Arts and Social Sciences
        building[5] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.057852, -117.824200))
                .title("5: College of Letters, Arts and Social Sciences"));
        // 6: College of Education and Integrative Studies
        building[6] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.058589, -117.822823))
                .title("6: College of Education and Integrative Studies"));
        // 7: College of Environmental Design
        building[7] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.057044, -117.827392))
                .title("7: College of Environmental Design"));
        // 8: College of Science Building
        building[8] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.058670, -117.824736))
                .title("8: College of Science Building"));
        //9: College of Engineering
        building[9] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.059093, -117.822404))
                .title("9: College of Engineering "));
        //13: Art Department and Engineering Annex
        building[13] =mMap.addMarker(new MarkerOptions()
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
        building[15] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.057887, -117.821539))
                .title("15: Library"));
        //17: Engineering Laboratories
        building[17] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.059630, -117.821398))
                .title("17: Engineering Laboratories"));
        //20: Residence Hall, Enchinitas
        building[20] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.062248, -117.820642))
                .title("20: Residence Hall, Enchinitas"));
        //21: Residence Hall, Montecito
        building[21] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.062081, -117.819266))
                .title("21: Residence Hall, Montecito"));
        //22: Residence Hall, Alamitos
        building[22] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.062114, -117.818024))
                .title("22: Residence Hall, Alamitos"));
        //23: Resedence Hall, Aliso
        building[23] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.062845, -117.817848))
                .title("23: Resedence Hall, Aliso"));
        //24: Music
        building[24] =mMap.addMarker(new MarkerOptions()
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
        building[25] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.056447, -117.822122))
                .title("25: Drama Department/Theatre"));
        //26: University Plaza
        building[26] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.056950, -117.820554))
                .title("26: University Plaza"));
        //26A: Student Orientation Center
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.056709, -117.820577))
                .title("26A: Student Orientation Center"));
        //28: Fruit/crops Unit
        building[28] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.059874, -117.810962))
                .title("28: Fruit/crops Unit"));
        //29: W.K. Kellogg Arabian Horse Center
        building[29] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.058483, -117.814486))
                .title("29: W.K. Kellogg Arabian Horse Center"));
        //30: Agricultural Unit
        building[30] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.055163, -117.828428))
                .title("30: Agricultural Unit"));
        //31: Poultry Unit/Poultry Houses
        building[31] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.054615, -117.828307))
                .title("30: Agricultural Unit"));
        //32:Beef Unit/ Feed Shed
        building[32] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.055488, -117.827618))
                .title("32:Beef Unit/ Feed Shed"));
        //33: Feed Mill
        building[33] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.053965, -117.827521))
                .title("33: Feed Mill"));
        //34: Meat Laboratory
        building[34] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.053952, -117.828074))
                .title("34: Meat Laboratory "));
        //35: BSC - Bronco Student Center
        building[35] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.056285, -117.821159))
                .title("35: BSC - Bronco Student Center"));
        //35A: W. Keith and Janet Kellogg University Art Gallery
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.057001, -117.821712))
                .title("35A: W. Keith and Janet Kellogg University Art Gallery"));
        //37: Swine Unit/Shelters
        building[37] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.052152, -117.822887))
                .title("37: Swine Unit/Shelters"));
        //38: Sheep/Wool Unit
        building[38] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.052159, -117.822381))
                .title("38: Sheep/Wool Unit"));
        //41: Darlene May Gymnasium
        building[41] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.054076, -117.821229))
                .title("41: Darlene May Gymnasium"));
        //42: BRIC - Bronco Recreation Intramural Complex
        building[42] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.054684, -117.820473))
                .title("42: BRIC - Bronco Recreation Intramural Complex"));
        //43: Kellogg Gymnasium
        building[43] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.054263, -117.819278))
                .title("43: Kellogg Gymnasium 44: swimming pool"));
        //45: Agricultural Engineering
        building[45] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.061132, -117.811036))
                .title("45: Agricultural Engineering"));
        //46: Health Services
        building[46] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.057830, -117.827853))
                .title("46: Health Services"));
        //47: Agricultural Engineering Tractor Shop
        building[47] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.061781, -117.815016))
                .title("47: Agricultural Engineering Tractor Shop"));
        //48: Custodial Offices
        building[48] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.061110, -117.815418))
                .title("48: Custodial Offices"));
        //49: Training Center
        building[49] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.061381, -117.815756))
                .title("49: Training Center"));
        //52: Vista Market
        building[52] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.053928, -117.817950))
                .title("52: Vista Market"));
        //54: Residence Suites, Vista de las Estrellas
        building[54] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.055359, -117.818905))
                .title("54: Residence Suites, Vista de las Estrellas"));
        //55: Foundation Administration Offices
        building[55] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.056215, -117.819824))
                .title("55: Foundation Administration Offices"));
        //56: Storage Building
        building[56] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.061885, -117.810827))
                .title("56: Storage Building"));
        //57: Residence Halls, Palmitas
        building[57] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.060521, -117.822323))
                .title("57: Residence Halls, Palmitas"));
        //58: Residence Halls, Cedritos
        building[58] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.061503, -117.821223))
                .title("58: Residence Halls, Cedritos"));
        //59:La Cienega Center - University Housing Services
        building[59] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.060838, -117.821909))
                .title("59:La Cienega Center - University Housing Services"));
        //60: Residence Suites
        building[60] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.054906, -117.818549))
                .title("60: Residence Suites"));
        //61: Residence Suites, Vista del Sol
        building[61] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.054768, -117.818050))
                .title("61: Residence Suites, Vista del Sol"));
        //62: Residence Suites, Vista de las Montanas
        building[62] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.054399, -117.817465))
                .title("62: Residence Suites, Vista de las Montanas"));
        //63: Residence Suites, Vista del Luna
        building[63] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.053710, -117.817138))
                .title("63: Residence Suites, Vista del Luna"));
        //64: Rose Float Laboratory
        building[64] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.060184, -117.808222))
                .title("64: Rose Float Laboratory"));
        //65: Pesticide Building
        building[65] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.061620, -117.810742))
                .title("65: Pesticide Building"));
        //66: Bronco Bookstore
        building[66] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.056024, -117.820506))
                .title("66: Bronco Bookstore"));
        //67: Equine Research Facility
        building[67] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.059253, -117.813959))
                .title("67: Equine Research Facility"));
        //68: Hay Barn
        building[68] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.054375, -117.827668))
                .title("68: Hay Barn"));
        //70: Los Olivos Comons
        building[70] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.062332, -117.821550))
                .title("70: Los Olivos Comons"));
        //71: Recreation/Maintenance
        building[71] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.062992, -117.816752))
                .title("71: Recreation/Maintenance"));
        //75: Procurement/Receiving
        building[75] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.059590, -117.807834))
                .title("75: Procurement/Receiving"));
        //76: Kellogg West Education/Dining
        building[76] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.056560, -117.824966))
                .title("76: Kellogg West Education/Dining"));
        //77: Kellogg West Main Lodge
        building[77] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.056340, -117.825972))
                .title("77: Kellogg West Main Lodge"));
        //78: Kellogg West Addition
        building[78] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.056772, -117.825690))
                .title("78: Kellogg West Addition"));
        //79: Collins College of Hospitality Management
        building[79] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.054942, -117.823998))
                .title("79: Collins College of Hospitality Management"));
        //79A: Collins College of Hospitality Management
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.055098, -117.824765))
                .title("79A: Collins College of Hospitality Management"));
        //79B: Collins College of Hospitality Management
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.055318, -117.824457))
                .title("79B: Collins College of Hospitality Management"));
        //80: Collins College of Hospitality Management, Marriott Learning Center
        building[80] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.054887, -117.825224))
                .title("80: Collins College of Hospitality Management, Marriott Learning Center"));
        //81: Facilities Management
        building[81] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.059511, -117.808871))
                .title("81: Facilities Management"));
        //82: Facilities Management Warehouse
        building[82] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.059789, -117.808664))
                .title("82: Facilities Management Warehouse"));
        //82A: Carpenter Shop
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.059969, -117.808919))
                .title("82A: Carpenter Shop"));
        //83: Auto Shop
        building[83] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.059429, -117.808208))
                .title("83: Auto Shop"));
        //85: I-Poly High School
        building[85] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.050909, -117.819478))
                .title("85: I-Poly High School"));
        //86: English Language Institute
        building[86] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.053356, -117.819679))
                .title("86: English Language Institute"));
        //86A: English Language Institute
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.053225, -117.819977))
                .title("86A: English Language Institute"));
        //86B: English Language Institute
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.053500, -117.820086))
                .title("86B: English Language Institute"));
        //86C: English Language Institute
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.053317, -117.820168))
                .title("86C: English Language Institute"));
        //89: Interim Design Center
        building[89] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.060594, -117.812267))
                .title("89: Interim Design Center"));

        //90: Medic-1
        building[90] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.061185, -117.816596))
                .title("90: Medic-1"));
        //91:Student Affairs Information Technology Services
        building[91] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.061403, -117.818651))
                .title("91:Student Affairs Information Technology Services"));
        //92: Laboratory Facility
        building[92] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.057763, -117.826177))
                .title("92: Laboratory Facility"));
        //94: University Office Building
        building[94] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.059105, -117.823269))
                .title("94: University Office Building"));
        //95: Cultural Centers
        building[95] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.057945, -117.82269))
                .title("95: Cultural Centers"));
        //97: Campus Center
        building[97] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.057845, -117.823344))
                .title("97: Campus Center"));
        //98: Classroom/Laboratory/Administration
        building[98] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.059467, -117.820099))
                .title("98: Classroom/Laboratory/Administration"));
        //99: Storage Building
        building[99] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.061853, -117.810996))
                .title("99: Storage Building"));
        //100: Storage Building
        building[100] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.062035, -117.810819))
                .title("100: Storage Building"));
        //106: Parking Structure
        building[106] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.060183, -117.816986))
                .title("106: Parking Structure"));
        //109: Police and Parking Services
        building[109] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.060717, -117.815772))
                .title("109: Police and Parking Services"));
        //111: Manor House
        building[111] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.060512, -117.822935))
                .title("111: Manor House"));
        //112: Kellogg House Pomona
        building[112] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.062604, -117.824471))
                .title("112: Kellogg House Pomona"));
        //113: Guest House
        building[113] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.062441, -117.824738))
                .title("113: Guest House"));
        //116: Child Care Center
        building[116] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.056123, -117.819481))
                .title("116: Child Care Center"));
        //162: College of Business Admistration
        building[162] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.061289, -117.819552))
                .title("162: College of Business Admistration"));
        //163: College of Business Admistration
        building[163] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.061216, -117.820182))
                .title("163: College of Business Admistration"));
        //164: College of Business Admistration
        building[164] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.061583, -117.819867))
                .title("164: College of Business Admistration"));
        //193: Chilled Water Center Plant
        building[193] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.057019, -117.829539))
                .title("193: Chilled Water Center Plant"));
        //200: University Village
        building[200] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.048704, -117.815410))
                .title("200: University Village"));




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


    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, "Please wait.",
                "Finding direction..!", true);

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }
}
