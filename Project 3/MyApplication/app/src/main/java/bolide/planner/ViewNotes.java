package bolide.planner;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ViewNotes extends AppCompatActivity {

    DatabaseHelperNotes dbNotes;
    ViewGroup linearLayout;
    Cursor c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notes);
        populateList();
    }

    protected void onResume(){
        super.onResume();
        populateList();
    }

    protected void populateList() {
        dbNotes = new DatabaseHelperNotes(this);
        c = dbNotes.getAllData();
        linearLayout = (ViewGroup) findViewById(R.id.lLayout);

        linearLayout.removeAllViews();
        try{
            while(c.moveToNext()){
                Button bt = new Button(this);
                bt.setText(c.getString(1) + "\n\n" + c.getString(2));
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                bt.setGravity(Gravity.LEFT);
                bt.setLayoutParams(params);
                linearLayout.addView(bt);
            }
        }
        finally{
            c.close();
        }

    }

    public void addOnClick(View view){
        Intent i = new Intent (this, AddNote.class);
        startActivity(i);
    }


}
