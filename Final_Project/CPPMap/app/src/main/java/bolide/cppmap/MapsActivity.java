package bolide.cppmap;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

//imports for spinner
import java.io.UnsupportedEncodingException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, DirectionFinderListener, OnMyLocationButtonClickListener, ActivityCompat.OnRequestPermissionsResultCallback {
    GoogleMap mMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean mPermissionDenied = false;

    Location location;                      //user location
    private String provider;
    private LocationManager locationManager;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;
    Bitmap pict;
    /*
    LatLngBounds CalPolyPomona is used to create the boundry
    The new LatLng is the southwest corner, the second is the northeast
     */

    private LatLngBounds CalPolyPomona = new LatLngBounds(new LatLng(34.048359, -117.829918), new LatLng(34.066456, -117.806628));

    Marker building[] = new Marker[221];

    Marker parking[] = new Marker[27];
    Marker other[] = new Marker[11];


    List<Marker> housingList = new ArrayList<Marker>();
    List<Marker> parkingList = new ArrayList<Marker>();
    List<Marker> sportsList = new ArrayList<Marker>();


    List<Marker> agriculture = new ArrayList<Marker>();
    List<Marker> administration = new ArrayList<Marker>();
    List<Marker> food = new ArrayList<Marker>();
    List<Marker> classes = new ArrayList<Marker>();
    List<Marker> POI = new ArrayList<Marker>();

    List<Marker> searchList = new ArrayList<Marker>();

    Marker current = null;
    Details info = new Details();

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

        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });


        //Spinner
        Spinner viewSpinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter viewAdapter = ArrayAdapter.createFromResource(this, R.array.locations_array, R.layout.support_simple_spinner_dropdown_item);
        viewSpinner.setAdapter(viewAdapter);
        viewSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {

                } else {
                    current = building[position];

                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(building[position].getPosition(), 20));
                    current.setTag(BitmapFactory.decodeResource(getResources(), info.getPicture(current.getTitle())));
                    pict = (Bitmap) current.getTag();
                    current.showInfoWindow();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        assignFilterButton();
        //THIS IS FOR GETTING USER LOCATION
        //Location Manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(false);

        provider = locationManager.getBestProvider(criteria, false);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        location = locationManager.getLastKnownLocation(provider);

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
        //removes google maps directions, rotation, and tilt
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.getUiSettings().setTiltGesturesEnabled(false);
        //map type
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //Sets the boundry
        mMap.setLatLngBoundsForCameraTarget(CalPolyPomona);
        LatLng nearPomona = new LatLng(34.0554622, -117.8181957);
        //Set max Zoom
        mMap.setMinZoomPreference(15);
        //Get Location of the user
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                current = marker;
                current.setTag(BitmapFactory.decodeResource(getResources(), info.getPicture(current.getTitle())));
                pict = (Bitmap) current.getTag();
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
                        image.setImageBitmap(pict);
                        discription.setText(marker.getSnippet());
                        Title.setText(marker.getTitle());
                        return v;
                    }
                });
                return false;

            }
        });
        mMap.setOnInfoWindowCloseListener(new GoogleMap.OnInfoWindowCloseListener() {
            @Override
            public void onInfoWindowClose(Marker marker) {

                pict.recycle();

            }
        });
        enableMyLocation();
        buildBuildings();
        buildParkingLots();
        buildOtherAreas();


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

    public void sendRequest() {  //TODO
        String destination = current.getPosition().toString().replaceAll("lat/lng:", "");
        //permission check
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        location = locationManager.getLastKnownLocation(provider);
        String userLocation = String.valueOf(location.getLatitude()) + ", "+ String.valueOf(location.getLongitude());
        if(current != null){
            current.hideInfoWindow();
        }
        try{
            new DirectionFinder(this, userLocation, destination).execute();
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
                .snippet(info.bldg1));
        building[1].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.class_icon));
        building[1].setTag("Classrooms");
        classes.add(building[1]);
        searchList.add(building[1]);
        //building[1].setTag(BitmapFactory.decodeResource(getResources(),R.drawable.building1));
        // 2: College of Agriculture
        building[2] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.057725, -117.826502))
                .title("2: College of Agriculture")
                .snippet(info.bldg2));
        building[2].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.class_icon));
        building[2].setTag("Classrooms");
        classes.add(building[2]);
        searchList.add(building[2]);
        // building[2].setTag(BitmapFactory.decodeResource(getResources(),R.drawable.building2));
        // 3: Science Laboratory
        building[3] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.058101, -117.825659))
                .title("3: Science Laboratory")
                .snippet(info.bldg3));
        building[3].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.class_icon));
        building[3].setTag("Classrooms");
        classes.add(building[3]);
        searchList.add(building[3]);
        //building[3].setTag(BitmapFactory.decodeResource(getResources(),R.drawable.building3));
        // 4: Biotechnology Building
        building[4] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.057479, -117.825380))
                .title("4: Biotechnology Building")
                .snippet(info.bldg4));
        building[4].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.class_icon));
        building[4].setTag("Classrooms");
        classes.add(building[4]);
        searchList.add(building[4]);
        //building[4].setTag(BitmapFactory.decodeResource(getResources(),R.drawable.building4));
        //4A: Bio Trek Learning Center
        Marker ttmp = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.057213, -117.826046))
                .title("4A: Bio Trek Learning Center")
                .snippet(info.bldg4A)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.landmark_icon)));
        POI.add(ttmp);
        // 5: College of Letters, Arts and Social Sciences
        building[5] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.057852, -117.824200))
                .title("5: College of Letters, Arts and Social Sciences")
                .snippet(info.bldg5));
        building[5].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.class_icon));
        building[5].setTag("Classrooms");
        classes.add(building[5]);
        searchList.add(building[5]);
        //building[5].setTag(BitmapFactory.decodeResource(getResources(),R.drawable.building5));
        // 6: College of Education and Integrative Studies
        building[6] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.058589, -117.822823))
                .title("6: College of Education and Integrative Studies")
                .snippet(info.bldg6));
        building[6].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.class_icon));
        building[6].setTag("Classrooms");
        classes.add(building[6]);
        searchList.add(building[6]);
        // building[6].setTag(BitmapFactory.decodeResource(getResources(),R.drawable.building6));
        // 7: College of Environmental Design
        building[7] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.057044, -117.827392))
                .title("7: College of Environmental Design")
                .snippet(info.bldg7));
        building[7].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.class_icon));
        building[7].setTag("Classrooms");
        classes.add(building[7]);
        searchList.add(building[7]);
        //building[7].setTag(BitmapFactory.decodeResource(getResources(),R.drawable.building7));
        // 8: College of Science Building
        building[8] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.058670, -117.824736))
                .title("8: College of Science Building")
                .snippet(info.bldg8));
        building[8].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.class_icon));
        building[8].setTag("Classrooms");
        classes.add(building[8]);
        searchList.add(building[8]);
        //building[8].setTag(BitmapFactory.decodeResource(getResources(),R.drawable.building8));
        //9: College of Engineering
        building[9] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.059093, -117.822404))
                .title("9: College of Engineering")
                .snippet(info.bldg9));
        building[9].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.class_icon));
        building[9].setTag("Classrooms");
        classes.add(building[9]);
        searchList.add(building[9]);
        //building[9].setTag(BitmapFactory.decodeResource(getResources(),R.drawable.building9));
        //13: Art Department and Engineering Annex
        building[13] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.058657, -117.820977))
                .title("13: Art Department and Engineering Anex")
                .snippet(info.bldg13));
        building[13].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.class_icon));
        building[13].setTag("Classrooms");
        classes.add(building[13]);
        searchList.add(building[13]);
        //   building[13].setTag(BitmapFactory.decodeResource(getResources(),R.drawable.building13));
        //13B: Army ROTC
        Marker tmp = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.058238, -117.820787))
                .title("13B: Army ROTC")
                .snippet(info.bldg13B)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.class_icon)));
        classes.add(tmp);
        //13C: Army ROTC
        Marker tmp2 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.058355, -117.820678))
                .title("13C: Army ROTC")
                .snippet(info.bldg13C)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.class_icon)));
        classes.add(tmp2);
        //13D: Pre-College TRIO Programs
        Marker tmp3 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.058481, -117.820435))
                .title("13D: Pre-College TRIO Programs")
                .snippet(info.bldg13D)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.class_icon)));
        classes.add(tmp3);
        //15: Library
        building[15] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.057887, -117.821539))
                .title("15: Library")
                .snippet(info.bldg15));
        building[15].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.library_icon));
        building[15].setTag("Classrooms");
        classes.add(building[15]);
        searchList.add(building[15]);
        //    building[15].setTag(BitmapFactory.decodeResource(getResources(),R.drawable.building15));
        //17: Engineering Laboratories
        building[17] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.059630, -117.821398))
                .title("17: Engineering Laboratories")
                .snippet(info.bldg17));
        building[17].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.class_icon));
        building[17].setTag("Classrooms");
        classes.add(building[17]);
        searchList.add(building[17]);
        //     building[17].setTag(BitmapFactory.decodeResource(getResources(),R.drawable.building17));
        //20: Residence Hall, Encinitas
        building[20] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.062248, -117.820642))
                .title("20: Residence Hall, Encinitas")
                .snippet(info.bldg20));
        building[20].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.housing_icon));
        building[20].setTag("Student Housing");
        housingList.add(building[20]);
        searchList.add(building[20]);

        //21: Residence Hall, Montecito
        building[21] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.062081, -117.819266))
                .title("21: Residence Hall, Montecito")
                .snippet(info.bldg21));
        building[21].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.housing_icon));
        building[21].setTag("Student Housing");
        housingList.add(building[21]);
        searchList.add(building[21]);
        //22: Residence Hall, Alamitos
        building[22] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.062114, -117.818024))
                .title("22: Residence Hall, Alamitos")
                .snippet(info.bldg22));
        building[22].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.housing_icon));
        building[22].setTag("Student Housing");
        housingList.add(building[22]);
        searchList.add(building[22]);

        //23: Resedence Hall, Aliso
        building[23] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.062845, -117.817848))
                .title("23: Resedence Hall, Aliso")
                .snippet(info.bldg23));
        building[23].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.housing_icon));
        building[23].setTag("Student Housing");
        housingList.add(building[23]);
        searchList.add(building[23]);
        //24: Music
        building[24] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.056936, -117.822586))
                .title("24: Music")
                .snippet(info.bldg24));
        building[24].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.class_icon));
        building[24].setTag("Classrooms");
        classes.add(building[24]);
        searchList.add(building[24]);
        //     building[24].setTag(BitmapFactory.decodeResource(getResources(),R.drawable.building24));
        //24A: Temp Class A
        Marker tmp4 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.056280, -117.822476))
                .title("24A: Temp Class A")
                .snippet(info.bldg24A)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.class_icon)));
        classes.add(tmp4);
        //24B: Temp Class B
        Marker tmp5 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.056166, -117.822405))
                .title("24B: Temp Class B")
                .snippet(info.bldg24B)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.class_icon)));
        classes.add(tmp5);
        //24C: Temp Class C
        Marker tmp6 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.055930, -117.822375))
                .title("24C: Temp Class C")
                .snippet(info.bldg24C)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.class_icon)));
        classes.add(tmp6);
        //24D: Temp Class D
        Marker tmp7 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.056022, -117.822510))
                .title("24D: Temp Class D")
                .snippet(info.bldg24D)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.class_icon)));
        classes.add(tmp7);
        //24E: Temp Class E
        Marker tmp8 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.056172, -117.822624))
                .title("24E: Temp Class E")
                .snippet(info.bldg24E)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.class_icon)));
        classes.add(tmp8);
        //25: Drama Department/Theatre
        building[25] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.056447, -117.822122))
                .title("25: Drama Department/Theatre")
                .snippet(info.bldg25));
        building[25].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.theater_icon));
        building[25].setTag("Classrooms");
        POI.add(building[25]);
        searchList.add(building[25]);
        //26: University Plaza
        building[26] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.056950, -117.820554))
                .title("26: University Plaza")
                .snippet(info.bldg26));
        building[26].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.student_icon));
        POI.add(building[26]);
        searchList.add(building[26]);
        //    building[26].setTag(BitmapFactory.decodeResource(getResources(),R.drawable.building26));
        //26A: Student Orientation Center
        Marker pTemp = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.056709, -117.820577))
                .title("26A: Student Orientation Center")
                .snippet(info.bldg26A)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.student_icon)));
        POI.add(pTemp);
        //28: Fruit/crops Unit
        building[28] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.059874, -117.810962))
                .title("28: Fruit/crops Unit")
                .snippet(info.bldg28));
        building[28].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.agriculture_icon));
        building[28].setTag("Agriculture");
        agriculture.add(building[28]);
        searchList.add(building[28]);
        //       building[28].setTag(BitmapFactory.decodeResource(getResources(),R.drawable.building28));
        //29: W.K. Kellogg Arabian Horse Center
        building[29] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.058483, -117.814486))
                .title("29: W.K. Kellogg Arabian Horse Center")
                .snippet(info.bldg29));
        building[29].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.agriculture_icon));
        building[29].setTag("Agriculture");
        agriculture.add(building[29]);
        searchList.add(building[29]);
//        building[29].setTag(BitmapFactory.decodeResource(getResources(),R.drawable.building29));
        //30: Agricultural Unit
        building[30] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.055163, -117.828428))
                .title("30: Agricultural Unit")
                .snippet(info.bldg30));
        building[30].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.agriculture_icon));
        building[30].setTag("Agriculture");
        agriculture.add(building[30]);
        searchList.add(building[30]);
//        building[30].setTag(BitmapFactory.decodeResource(getResources(),R.drawable.building30));
        //31: Poultry Unit/Poultry Houses
        building[31] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.054615, -117.828307))
                .title("31: Poultry Unit/Poultry Houses")
                .snippet(info.bldg31));
        building[31].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.agriculture_icon));
        building[31].setTag("Agriculture");
        agriculture.add(building[31]);
        searchList.add(building[31]);
//        building[31].setTag(BitmapFactory.decodeResource(getResources(),R.drawable.building31));
        //32:Beef Unit/ Feed Shed
        building[32] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.055488, -117.827618))
                .title("32:Beef Unit/ Feed Shed")
                .snippet(info.bldg32));
        building[32].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.agriculture_icon));
        building[32].setTag("Agriculture");
        agriculture.add(building[32]);
        searchList.add(building[32]);
//        building[32].setTag(BitmapFactory.decodeResource(getResources(),R.drawable.building32));
        //33: Feed Mill
        building[33] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.053965, -117.827521))
                .title("33: Feed Mill")
                .snippet(info.bldg33));
        building[33].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.agriculture_icon));
        building[33].setTag("Agriculture");
        agriculture.add(building[33]);
        searchList.add(building[33]);
//        building[33].setTag(BitmapFactory.decodeResource(getResources(),R.drawable.building33));
        //34: Meat Laboratory
        building[34] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.053952, -117.828074))
                .title("34: Meat Laboratory")
                .snippet(info.bldg34));
        building[34].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.agriculture_icon));
        building[34].setTag("Agriculture");
        agriculture.add(building[34]);
        searchList.add(building[34]);
        //       building[34].setTag(BitmapFactory.decodeResource(getResources(),R.drawable.building34));
        //35: BSC - Bronco Student Center
        building[35] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.056285, -117.821159))
                .title("35: BSC - Bronco Student Center")
                .snippet(info.bldg35));
        building[35].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.food_icon));
        building[35].setTag("Food");
        food.add(building[35]);
        searchList.add(building[35]);
//        building[35].setTag(BitmapFactory.decodeResource(getResources(),R.drawable.building35));
        //35A: W. Keith and Janet Kellogg University Art Gallery
        Marker ttmp2 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.057001, -117.821712))
                .title("35A: W. Keith and Janet Kellogg University Art Gallery")
                .snippet(info.bldg35A)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.art_icon)));
        POI.add(ttmp2);
        //37: Swine Unit/Shelters
        building[37] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.052152, -117.822887))
                .title("37: Swine Unit/Shelters")
                .snippet(info.bldg37));
        building[37].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.agriculture_icon));
        building[37].setTag("Agriculture");
        agriculture.add(building[37]);
        searchList.add(building[37]);
//        building[37].setTag(BitmapFactory.decodeResource(getResources(),R.drawable.building37));
        //38: Sheep/Wool Unit
        building[38] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.052159, -117.822381))
                .title("38: Sheep/Wool Unit")
                .snippet(info.bldg38));
        building[38].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.agriculture_icon));
        building[38].setTag("Agriculture");
        agriculture.add(building[38]);
        searchList.add(building[38]);
//        building[38].setTag(BitmapFactory.decodeResource(getResources(),R.drawable.building38));
        //41: Darlene May Gymnasium
        building[41] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.054076, -117.821229))
                .title("41: Darlene May Gymnasium")
                .snippet(info.bldg41));
        building[41].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.sports_icon));
        building[41].setTag("Athletics");
        sportsList.add(building[41]);
        searchList.add(building[41]);
        //42: BRIC - Bronco Recreation Intramural Complex
        building[42] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.054684, -117.820473))
                .title("42: BRIC - Bronco Recreation Intramural Complex")
                .snippet(info.bldg42));
        building[42].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.sports_icon));
        building[42].setTag("Athletics");
        sportsList.add(building[42]);
        searchList.add(building[42]);
        //43: Kellogg Gymnasium
        building[43] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.054263, -117.819278))
                .title("43: Kellogg Gymnasium")
                .snippet(info.bldg43));
        building[43].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.sports_icon));
        building[43].setTag("Athletics");
        sportsList.add(building[43]);
        searchList.add(building[43]);
        //45: Agricultural Engineering
        building[45] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.061132, -117.811036))
                .title("45: Agricultural Engineering")
                .snippet(info.bldg45));
        building[45].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.agriculture_icon));
        building[45].setTag("Agriculture");
        agriculture.add(building[45]);
        searchList.add(building[45]);
//        building[45].setTag(BitmapFactory.decodeResource(getResources(),R.drawable.building45));
        //46: Health Services
        building[46] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.057830, -117.827853))
                .title("46: Health Services")
                .snippet(info.bldg46)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.health_icon)));
        POI.add(building[46]);
        searchList.add(building[46]);
//        building[46].setTag(BitmapFactory.decodeResource(getResources(),R.drawable.building46));
        //47: Agricultural Engineering Tractor Shop
        building[47] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.061781, -117.815016))
                .title("47: Agricultural Engineering Tractor Shop")
                .snippet(info.bldg47));
        building[47].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.agriculture_icon));
        building[47].setTag("Agriculture");
        agriculture.add(building[47]);
        searchList.add(building[47]);
        //building[47].setTag(BitmapFactory.decodeResource(getResources(),R.drawable.building47));
        //48: Custodial Offices
        building[48] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.061110, -117.815418))
                .title("48: Custodial Offices")
                .snippet(info.bldg48));
        building[48].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.misc_icon));
        administration.add(building[48]);
        searchList.add(building[48]);
        //building[48].setTag(BitmapFactory.decodeResource(getResources(),R.drawable.building48));
        //49: Training Center
        building[49] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.061381, -117.815756))
                .title("49: Training Center")
                .snippet(info.bldg49));
        building[49].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.misc_icon));
        administration.add(building[49]);
        searchList.add(building[49]);
        //building[49].setTag(BitmapFactory.decodeResource(getResources(),R.drawable.building49));
        //52: Vista Market
        building[52] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.053928, -117.817950))
                .title("52: Vista Market")
                .snippet(info.bldg52));
        building[52].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.food_icon));
        building[52].setTag("Food");
        food.add(building[52]);
        searchList.add(building[52]);
//        building[52].setTag(BitmapFactory.decodeResource(getResources(),R.drawable.building52));

        //54: Residence Suites, Vista de las Estrellas
        building[54] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.055359, -117.818905))
                .title("54: Residence Suites, Vista de las Estrellas")
                .snippet(info.bldg54));
        building[54].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.housing_icon));
        building[54].setTag("Student Housing");
        housingList.add(building[54]);
        searchList.add(building[54]);

        //55: Foundation Administration Offices
        building[55] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.056215, -117.819824))
                .title("55: Foundation Administration Offices")
                .snippet(info.bldg55));
        building[55].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.misc_icon));
        administration.add(building[55]);
        searchList.add(building[55]);
//        building[55].setTag(BitmapFactory.decodeResource(getResources(),R.drawable.building55));
        //56: Storage Building
        building[56] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.061885, -117.810827))
                .title("56: Storage Building")
                .snippet(info.bldg56));
        building[56].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.misc_icon));
        administration.add(building[56]);
        searchList.add(building[56]);
//        building[56].setTag(BitmapFactory.decodeResource(getResources(),R.drawable.building56));
        //57: Residence Halls, Palmitas
        building[57] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.060521, -117.822323))
                .title("57: Residence Halls, Palmitas")
                .snippet(info.bldg57));
        building[57].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.housing_icon));
        building[57].setTag("Student Housing");
        housingList.add(building[57]);
        searchList.add(building[57]);

        //58: Residence Halls, Cedritos
        building[58] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.061503, -117.821223))
                .title("58: Residence Halls, Cedritos")
                .snippet(info.bldg58));
        building[58].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.housing_icon));
        building[58].setTag("Student Housing");
        housingList.add(building[58]);
        searchList.add(building[58]);

        //59:La Cienega Center - University Housing Services
        building[59] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.060838, -117.821909))
                .title("59:La Cienega Center - University Housing Services")
                .snippet(info.bldg59));
        building[59].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.housing_icon));
        building[59].setTag("Student Housing");
        housingList.add(building[59]);
        searchList.add(building[59]);

        //60: Residence Suites
        building[60] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.054906, -117.818549))
                .title("60: Residence Suites")
                .snippet(info.bldg60));
        building[60].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.housing_icon));
        building[60].setTag("Student Housing");
        housingList.add(building[60]);
        searchList.add(building[60]);

        //61: Residence Suites, Vista del Sol
        building[61] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.054768, -117.818050))
                .title("61: Residence Suites, Vista del Sol")
                .snippet(info.bldg61));
        building[61].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.housing_icon));
        building[61].setTag("Student Housing");
        housingList.add(building[61]);
        searchList.add(building[61]);

        //62: Residence Suites, Vista de las Montanas
        building[62] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.054399, -117.817465))
                .title("62: Residence Suites, Vista de las Montanas")
                .snippet(info.bldg62));
        building[62].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.housing_icon));
        building[62].setTag("Student Housing");
        housingList.add(building[62]);
        searchList.add(building[62]);

        //63: Residence Suites, Vista del Luna
        building[63] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.053710, -117.817138))
                .title("63: Residence Suites, Vista del Luna")
                .snippet(info.bldg63));
        building[63].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.housing_icon));
        building[63].setTag("Student Housing");
        housingList.add(building[63]);
        searchList.add(building[63]);

        //64: Rose Float Laboratory
        building[64] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.060184, -117.808222))
                .title("64: Rose Float Laboratory")
                .snippet(info.bldg64));
        building[64].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.misc_icon));
        administration.add(building[64]);
        searchList.add(building[64]);
        //65: Pesticide Building
        building[65] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.061620, -117.810742))
                .title("65: Pesticide Building")
                .snippet(info.bldg65));
        building[65].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.misc_icon));
        administration.add(building[65]);
        searchList.add(building[65]);
        //66: Bronco Bookstore
        building[66] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.056024, -117.820506))
                .title("66: Bronco Bookstore")
                .snippet(info.bldg66));
        building[66].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.class_icon));
        building[66].setTag("Classrooms");
        classes.add(building[66]);
        searchList.add(building[66]);
        //67: Equine Research Facility
        building[67] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.059253, -117.813959))
                .title("67: Equine Research Facility")
                .snippet(info.bldg67));
        building[67].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.class_icon));
        classes.add(building[67]);
        searchList.add(building[67]);
        //68: Hay Barn
        building[68] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.054375, -117.827668))
                .title("68: Hay Barn")
                .snippet(info.bldg68));
        building[68].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.agriculture_icon));
        building[68].setTag("Agriculture");
        agriculture.add(building[68]);
        searchList.add(building[68]);
        //70: Los Olivos Commons
        building[70] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.062332, -117.821550))
                .title("70: Los Olivos Commons")
                .snippet(info.bldg70));
        building[70].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.food_icon));
        building[70].setTag("Food");
        food.add(building[70]);
        searchList.add(building[70]);
        //71: Recreation/Maintenance
        building[71] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.062992, -117.816752))
                .title("71: Recreation/Maintenance")
                .snippet(info.bldg71));
        building[71].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.misc_icon));
        administration.add(building[71]);
        searchList.add(building[71]);
        //75: Procurement/Receiving
        building[75] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.059590, -117.807834))
                .title("75: Procurement/Receiving")
                .snippet(info.bldg75));
        building[75].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.misc_icon));
        administration.add(building[75]);
        searchList.add(building[75]);
        //76: Kellogg West Education/Dining
        building[76] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.056560, -117.824966))
                .title("76: Kellogg West Education/Dining")
                .snippet(info.bldg76));
        building[76].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.food_icon));
        building[76].setTag("Food");
        food.add(building[76]);
        searchList.add(building[76]);
        //77: Kellogg West Main Lodge
        building[77] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.056340, -117.825972))
                .title("77: Kellogg West Main Lodge")
                .snippet(info.bldg77));
        building[77].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.hotel_icon));
        building[77].setTag("Landmark");
        POI.add(building[77]);
        searchList.add(building[77]);
        //78: Kellogg West Addition
        building[78] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.056772, -117.825690))
                .title("78: Kellogg West Addition")
                .snippet(info.bldg78));
        building[78].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.hotel_icon));
        building[78].setTag("Landmark");
        POI.add(building[78]);
        searchList.add(building[78]);
        //79: Collins College of Hospitality Management
        building[79] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.054942, -117.823998))
                .title("79: Collins College of Hospitality Management")
                .snippet(info.bldg79));
        building[79].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.class_icon));
        building[79].setTag("Classrooms");
        classes.add(building[79]);
        searchList.add(building[79]);
        //79A: Collins College of Hospitality Management
        Marker ctmp = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.055098, -117.824765))
                .title("79A: Collins College of Hospitality Management")
                .snippet(info.bldg79A)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.class_icon)));
        classes.add(ctmp);
        //79B: Collins College of Hospitality Management
        Marker ctmp2 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.055318, -117.824457))
                .title("79B: Collins College of Hospitality Management")
                .snippet(info.bldg79B)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.class_icon)));
        classes.add(ctmp2);
        //80: Collins College of Hospitality Management, Marriott Learning Center
        building[80] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.054887, -117.825224))
                .title("80: Collins College of Hospitality Management, Marriott Learning Center")
                .snippet(info.bldg80));
        building[80].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.class_icon));
        classes.add(building[80]);
        searchList.add(building[80]);
        //81: Facilities Management
        building[81] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.059511, -117.808871))
                .title("81: Facilities Management")
                .snippet(info.bldg81));
        building[81].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.misc_icon));
        administration.add(building[81]);
        searchList.add(building[81]);
        //82: Facilities Management Warehouse
        building[82] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.059789, -117.808664))
                .title("82: Facilities Management Warehouse")
                .snippet(info.bldg82));
        building[82].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.misc_icon));
        administration.add(building[82]);
        searchList.add(building[82]);
       //82A: Carpenter Shop
        Marker tttmp = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.059969, -117.808919))
                .title("82A: Carpenter Shop")
                .snippet(info.bldg82A)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.misc_icon)));
        administration.add(tttmp);
        //83: Auto Shop
        building[83] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.059429, -117.808208))
                .title("83: Auto Shop")
                .snippet(info.bldg83));
        building[83].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.misc_icon));
        administration.add(building[83]);
        searchList.add(building[83]);
        //85: I-Poly High School
        building[85] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.050909, -117.819478))
                .title("85: I-Poly High School")
                .snippet(info.bldg85));
        building[85].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.class_icon));
        classes.add(building[85]);
        searchList.add(building[85]);
        //86: English Language Institute
        building[86] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.053356, -117.819679))
                .title("86: English Language Institute")
                .snippet(info.bldg86));
        building[86].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.class_icon));
        classes.add(building[86]);
        searchList.add(building[86]);
        //86A: English Language Institute
        Marker ctmp3 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.053225, -117.819977))
                .title("86A: English Language Institute")
                .snippet(info.bldg86A)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.class_icon)));
        classes.add(ctmp3);
        //86B: English Language Institute
        Marker ctmp5 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.053500, -117.820086))
                .title("86B: English Language Institute")
                .snippet(info.bldg86B)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.class_icon)));
        classes.add(ctmp5);
        //86C: English Language Institute
        Marker ctmp4 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.053317, -117.820168))
                .title("86C: English Language Institute")
                .snippet(info.bldg86C)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.class_icon)));
        classes.add(ctmp4);
        //89: Interim Design Center
        building[89] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.060594, -117.812267))
                .title("89: Interim Design Center")
                .snippet(info.bldg89));
        building[89].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.class_icon));
        classes.add(building[89]);
        searchList.add(building[89]);
        //90: Medic-1
        building[90] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.061185, -117.816596))
                .title("90: Medic-1")
                .snippet(info.bldg90)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.health_icon)));
        POI.add(building[90]);
        searchList.add(building[90]);
        //91:Student Affairs Information Technology Services
        building[91] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.061403, -117.818651))
                .title("91:Student Affairs Information Technology Services")
                .snippet(info.bldg91));
        building[91].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.misc_icon));
        administration.add(building[91]);
        searchList.add(building[91]);
        //92: Laboratory Facility
        building[92] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.057763, -117.826177))
                .title("92: Laboratory Facility")
                .snippet(info.bldg92));
        building[92].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.class_icon));
        classes.add(building[92]);
        searchList.add(building[92]);
        //94: University Office Building
        building[94] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.059105, -117.823269))
                .title("94: University Office Building")
                .snippet(info.bldg94));
        building[94].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.class_icon));
        classes.add(building[94]);
        searchList.add(building[94]);
        //95: Cultural Centers
        building[95] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.057945, -117.82269))
                .title("95: Cultural Centers")
                .snippet(info.bldg95));
        building[95].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.student_icon));
        POI.add(building[95]);
        searchList.add(building[95]);
        //97: Campus Center
        building[97] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.057845, -117.823344))
                .title("97: Campus Center")
                .snippet(info.bldg97));
        searchList.add(building[97]);
        building[97].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.food_icon));
        food.add(building[97]);
        searchList.add(building[97]);
        //98: Classroom/Laboratory/Administration
        building[98] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.059467, -117.820099))
                .title("98: Classroom/Laboratory/Administration")
                .snippet(info.bldg98));
        building[98].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.class_icon));
        classes.add(building[98]);
        searchList.add(building[98]);
        //99: Storage Building
        building[99] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.061853, -117.810996))
                .title("99: Storage Building")
                .snippet(info.bldg99));
        building[99].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.misc_icon));
        administration.add(building[99]);
        searchList.add(building[99]);
        //100: Storage Building
        building[100] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.062035, -117.810819))
                .title("100: Storage Building")
                .snippet(info.bldg100));
        building[100].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.misc_icon));
        administration.add(building[100]);
        searchList.add(building[100]);
        //106: Parking Structure
        building[106] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.060183, -117.816986))
                .title("106: Parking Structure")
                .snippet(info.bldg106));
        building[106].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.parking_icon));
        parkingList.add(building[106]);
        searchList.add(building[106]);



        //109: Police and Parking Services
        building[109] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.060717, -117.815772))
                .title("109: Police and Parking Services")
                .snippet(info.bldg109));
        building[109].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.police_icon));
        administration.add(building[109]);
        searchList.add(building[109]);
        //111: Manor House
        building[111] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.060512, -117.822935))
                .title("111: Manor House")
                .snippet(info.bldg111));
        building[111].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.hotel_icon));
        POI.add(building[111]);
        searchList.add(building[111]);
        //112: Kellogg House Pomona
        building[112] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.062604, -117.824471))
                .title("112: Kellogg House Pomona")
                .snippet(info.bldg112));
        building[112].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.hotel_icon));
        POI.add(building[112]);
        searchList.add(building[112]);
        //113: Guest House
        building[113] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.062441, -117.824738))
                .title("113: Guest House")
                .snippet(info.bldg113));
        building[113].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.hotel_icon));
        POI.add(building[113]);
        searchList.add(building[113]);
        //116: Child Care Center
        building[116] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.056123, -117.819481))
                .title("116: Child Care Center")
                .snippet(info.bldg116));
        building[116].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.childcare_icon));
        POI.add(building[116]);
        searchList.add(building[116]);
        //118: American Red Cross
        building[118] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.053657, -117.811616))
                .title("118: American Red Cross")
                .snippet(info.bldg118)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.health_icon)));
        POI.add(building[118]);
        //162: College of Business Admistration
        building[162] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.061289, -117.819552))
                .title("162: College of Business Admistration")
                .snippet(info.bldg162));
        building[162].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.class_icon));
        classes.add(building[162]);
        searchList.add(building[162]);
        //163: College of Business Admistration
        building[163] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.061216, -117.820182))
                .title("163: College of Business Admistration")
                .snippet(info.bldg163));
        building[163].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.class_icon));
        classes.add(building[163]);
        searchList.add(building[163]);
        //164: College of Business Admistration
        building[164] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.061583, -117.819867))
                .title("164: College of Business Admistration")
                .snippet(info.bldg164));
        building[164].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.class_icon));
        classes.add(building[164]);
        searchList.add(building[164]);
        //193: Chilled Water Center Plant
        building[193] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.057019, -117.829539))
                .title("193: Chilled Water Center Plant")
                .snippet(info.bldg193));
        building[193].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.misc_icon));
        administration.add(building[193]);
        searchList.add(building[193]);
        //200: University Village
        building[200] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.048704, -117.815410))
                .title("200: University Village")
                .snippet(info.bldg200));
        building[200].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.housing_icon));
        housingList.add(building[200]);
        searchList.add(building[200]);
        //211: Agriscapes/Farm Store
        building[211] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.048422, -117.819040))
                .title("211: Agriscapes/Farm Store")
                .snippet(info.bldg211));
        building[211].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.food_icon));
        food.add(building[211]);
        searchList.add(building[211]);

        //220: Center for Training, Technology and Incubation
        building[220] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.050322, -117.815124))
                .title("220: Center for Training, Technology and Incubation")
                .snippet(info.bldg220));
        building[220].setIcon(BitmapDescriptorFactory.fromResource(R.drawable.misc_icon));
        administration.add(building[220]);
        searchList.add(building[200]);
    }


    //function for placing Parking Lot markers
    private void buildParkingLots(){
        //Lot A
        parking[0] = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.060597, -117.824458))
                .title("Parking Lot A")
                .snippet(info.lota)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.parking_icon)));
        //Lot B
        parking[1] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.051815, -117.815982))
                .title("Parking Lot B")
                .snippet(info.lotb)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.parking_icon)));
        //Lot E1
        parking[2] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.061361, -117.811669))
                .title("Parking Lot E1")
                .snippet(info.lote1)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.parking_icon)));
        //Lot E2
        parking[3] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.060774, -117.812506))
                .title("Parking Lot E2")
                .snippet(info.lote2)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.parking_icon)));
        //Lot F1
        parking[4] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.062285, -117.817034))
                .title("Parking Lot F1")
                .snippet(info.lotf1)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.parking_icon)));
        //Lot F2
        parking[5] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.061556, -117.817721))
                .title("Parking Lot F2")
                .snippet(info.lotf2)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.parking_icon)));
        //Lot F3
        parking[6] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.061983, -117.816283))
                .title("Parking Lot F3")
                .snippet(info.lotf3)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.parking_icon)));
        //Lot F4
        parking[7] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.061129, -117.817313))
                .title("Parking Lot F4")
                .snippet(info.lotf4)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.parking_icon)));
        //Lot F5
        parking[8] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.061591, -117.815532))
                .title("Parking Lot F5")
                .snippet(info.lotf5)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.parking_icon)));
        //Lot F8
        parking[9] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.059191, -117.817206))
                .title("Parking Lot F8")
                .snippet(info.lotf8)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.parking_icon)));
        //Lot F9
        parking[10] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.060311, -117.815382))
                .title("Parking Lot F9")
                .snippet(info.lotf9)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.parking_icon)));
        //Lot F10
        parking[11] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.061040, -117.814502))
                .title("Parking Lot F10")
                .snippet(info.lotf10)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.parking_icon)));
        //Lot G
        parking[12] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.055618, -117.819717))
                .title("Parking Lot G")
                .snippet(info.lotg)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.parking_icon)));
        //Lot H
        parking[13] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.060969, -117.818730))
                .title("Parking Lot H")
                .snippet(info.loth)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.parking_icon)));
        //Lot I
        parking[14] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.048843, -117.819554))
                .title("Parking Lot I")
                .snippet(info.loti)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.parking_icon)));
        //Lot J
        parking[15] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.057245, -117.828768))
                .title("Parking Lot J")
                .snippet(info.lotj)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.parking_icon)));
        //Lot L
        parking[16] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.055485, -117.825452))
                .title("Parking Lot L")
                .snippet(info.lotl)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.parking_icon)));
        //Lot M
        parking[17] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.055485, -117.829271))
                .title("Parking Lot M")
                .snippet(info.lotm)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.parking_icon)));
        //Lot N
        parking[18] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.056143, -117.818822))
                .title("Parking Lot N")
                .snippet(info.lotn)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.parking_icon)));
        //Lot O
        parking[19] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.049921, -117.814423))
                .title("Parking Lot O")
                .snippet(info.loto)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.parking_icon)));
        //Overflow Parking
        parking[20] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.053437, -117.810647))
                .title("Overflow Parking")
                .snippet(info.lotoverfow)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.parking_icon)));
        //Lot P
        parking[21] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.054254, -117.815410))
                .title("Parking Lot P")
                .snippet(info.lotp)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.parking_icon)));
        //Parking Structure 2
        parking[22] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.051729, -117.819723))
                .title("Parking Structure 2")
                .snippet(info.lotstruct2)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.parking_icon)));
        //Lot Q
        parking[23] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.055036, -117.817105))
                .title("Parking Lot Q")
                .snippet(info.lotq)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.parking_icon)));
        //Lot R
        parking[24] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.063551, -117.825602))
                .title("Parking Lot R")
                .snippet(info.lotr)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.parking_icon)));
        //Lot U
        parking[25] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.049027, -117.817319))
                .title("Parking Lot U")
                .snippet(info.lotu)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.parking_icon)));
        //Unpaved Overflow
        parking[26] =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.049578, -117.813821))
                .title("Unpaved Overflow Parking")
                .snippet(info.lotunpavedoverflow)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.parking_icon)));
        for(int i =0; i<27; i++){
            parkingList.add(parking[i]);
        }
    }

    //function for placing markers of other points of interest
    private void buildOtherAreas(){
        //Pumpkin patch
        other[0]=mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.049560, -117.818306))
                .title("Pumpkin patch")
                .snippet(info.pumpkin)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.landmark_icon)));
        POI.add(other[0]);
        //BioTrek Ethnobotany Garden
        other[1]=mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.057135, -117.826465))
                .title("BioTrek Ethnobotany Garden")
                .snippet(info.biotrek)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.landmark_icon)));
        POI.add(other[1]);
        //Engineering Meadow
        other[2]=mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.058548, -117.823724))
                .title("Engineering Meadow")
                .snippet(info.emeadow)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.landmark_icon)));
        POI.add(other[2]);
        //Japanese Garden
        other[3]=mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.059832, -117.820457))
                .title("Japanese Garden")
                .snippet(info.jgarden)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.landmark_icon)));
        POI.add(other[3]);
        //Kellogg track and Soccer Stadium
        other[4]=mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.052341, -117.816815))
                .title("Kellogg track and Soccer Stadium")
                .snippet(info.track)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.sports_icon)));
        sportsList.add(other[4]);
        //Scolinos Baseball Field
        other[5]=mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.053513, -117.815971))
                .title("Scolinos Baseball Field")
                .snippet(info.baseball)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.sports_icon)));
        sportsList.add(other[5]);
        //Rose Garden
        other[6]=mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.060819, -117.820413))
                .title("Rose Garden")
                .snippet(info.rose)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.landmark_icon)));
        POI.add(other[6]);
        //Soccer Field
        other[7]=mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.052695, -117.818546))
                .title("Soccer Field")
                .snippet(info.soccer)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.sports_icon)));
        sportsList.add(other[7]);
        //Tennis court
        other[8]=mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.052891, -117.820391))
                .title("Tennis court")
                .snippet(info.tennis)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.sports_icon)));
        sportsList.add(other[8]);

        //University Quad
        other[9]=mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.058548, -117.823724))
                .title("University Quad")
                .snippet(info.quad)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.landmark_icon)));
        POI.add(other[9]);
        //Turtle Pond
        other[10]=mMap.addMarker(new MarkerOptions()
                .position(new LatLng(34.061033, -117.821571))
                .title("Turtle Pond")
                .snippet(info.tpond)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.landmark_icon)));
        POI.add(other[10]);
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

    public void assignFilterButton(){
        final Button filterButton = (Button) findViewById(R.id.filter_button);
        filterButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(current != null && current.isInfoWindowShown())current.hideInfoWindow();
                PopupMenu popup = new PopupMenu(MapsActivity.this, filterButton);
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        String str = item.toString();
                        switch(str){
                            case "ALL":
                                enableAllMarkers();
                                break;
                            case "HOUSING":
                                disableAllMarkers();
                                enableMarker(housingList);
                                break;
                            case "PARKING":
                                disableAllMarkers();
                                enableMarker(parkingList);
                                break;
                            case "FITNESS":
                                disableAllMarkers();
                                enableMarker(sportsList);
                                break;
                            case "POINTS OF INTEREST":
                                disableAllMarkers();
                                enableMarker(POI);
                                break;
                            case "CLASSES":
                                disableAllMarkers();
                                enableMarker(classes);
                                break;
                            case "FOOD":
                                disableAllMarkers();
                                enableMarker(food);
                                break;
                            case "ADMININSTRATION":
                                disableAllMarkers();
                                enableMarker(administration);
                                break;
                            case "AGRICULTURE":
                                disableAllMarkers();
                                enableMarker(agriculture);
                                break;

                        }
                        Toast.makeText(
                                MapsActivity.this,
                                "Filter: " + item.getTitle(),
                                Toast.LENGTH_SHORT
                        ).show();
                        return true;
                    }
                });
                popup.show();
            }
        });
    }
    public void enableAllMarkers(){
        for(int i = 0; i < agriculture.size(); i++){
            if(agriculture.get(i)!=null)agriculture.get(i).setVisible(true);
        }
        for(int i = 0; i < classes.size(); i++){
            if(classes.get(i)!=null)classes.get(i).setVisible(true);
        }
        for(int i = 0; i < housingList.size(); i++){
            if(housingList.get(i)!=null)housingList.get(i).setVisible(true);
        }
        for(int i = 0; i < parkingList.size(); i++){
            if(parkingList.get(i)!=null)parkingList.get(i).setVisible(true);
        }
        for(int i = 0; i < food.size(); i++){
            if(food.get(i)!=null)food.get(i).setVisible(true);
        }
        for(int i = 0; i < sportsList.size(); i++){
            if(sportsList.get(i)!=null)sportsList.get(i).setVisible(true);
        }
        for(int i = 0; i < POI.size(); i++){
            if(POI.get(i)!=null)POI.get(i).setVisible(true);
        }
        for(int i = 0; i < administration.size(); i++){
            if(administration.get(i)!=null)administration.get(i).setVisible(true);
        }
    }
    public void enableMarker(List<Marker> toEnable){
        for(int i = 0; i < toEnable.size(); i++){
            if(toEnable.get(i)!=null)toEnable.get(i).setVisible(true);
        }
    }

    public void disableAllMarkers(){
        for(int i = 0; i < agriculture.size(); i++){
            if(agriculture.get(i)!=null)agriculture.get(i).setVisible(false);
        }
        for(int i = 0; i < classes.size(); i++){
            if(classes.get(i)!=null)classes.get(i).setVisible(false);
        }
        for(int i = 0; i < housingList.size(); i++){
            if(housingList.get(i)!=null)housingList.get(i).setVisible(false);
        }
        for(int i = 0; i < parkingList.size(); i++){
            if(parkingList.get(i)!=null)parkingList.get(i).setVisible(false);
        }
        for(int i = 0; i < food.size(); i++){
            if(food.get(i)!=null)food.get(i).setVisible(false);
        }
        for(int i = 0; i < sportsList.size(); i++){
            if(sportsList.get(i)!=null)sportsList.get(i).setVisible(false);
        }
        for(int i = 0; i < POI.size(); i++){
            if(POI.get(i)!=null)POI.get(i).setVisible(false);
        }
        for(int i = 0; i < administration.size(); i++){
            if(administration.get(i)!=null)administration.get(i).setVisible(false);
        }
    }

}
