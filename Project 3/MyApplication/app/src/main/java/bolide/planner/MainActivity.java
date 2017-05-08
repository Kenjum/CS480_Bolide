package bolide.planner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {
    Spinner viewSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewSpinner = (Spinner) findViewById(R.id.views_spinner);
        ArrayAdapter viewAdapter = ArrayAdapter.createFromResource(this,R.array.views,R.layout.support_simple_spinner_dropdown_item);
        viewSpinner.setAdapter(viewAdapter);
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
                }
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
}
