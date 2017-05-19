package bolide.planner;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import bolide.planner.Fragments.DayFragment;
import bolide.planner.Views.ViewDay;

public class MainActivity extends AppCompatActivity {
    Spinner viewSpinner;
    DatabaseHelperNotes dbNotes;

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        dbNotes = new DatabaseHelperNotes(this);
        CreateSpinner();
        final View fragmentView = (View)findViewById(R.id.fragment);
        final View day_fragment = (View)findViewById(R.id.fragmentday);
        day_fragment.setVisibility(View.GONE);
        viewSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0://month
                        fragmentView.setVisibility(View.VISIBLE);
                        day_fragment.setVisibility(View.GONE);
                        break;
                    case 1://week
                        fragmentView.setVisibility(View.GONE);
                        day_fragment.setVisibility(View.GONE);
                        break;
                    case 2://day
                        fragmentView.setVisibility(View.GONE);
                        day_fragment.setVisibility(View.VISIBLE);
                        break;
                };
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void notesOnClick(View view){
        Intent i = new Intent (this, ViewNotes.class);
        startActivity(i);
    }
    private void CreateSpinner(){
        viewSpinner = (Spinner) findViewById(R.id.views_spinner);
        ArrayAdapter viewAdapter = ArrayAdapter.createFromResource(this,R.array.views,R.layout.support_simple_spinner_dropdown_item);
        viewSpinner.setAdapter(viewAdapter);
    }
}
