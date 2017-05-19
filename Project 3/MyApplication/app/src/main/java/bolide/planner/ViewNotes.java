package bolide.planner;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class ViewNotes extends AppCompatActivity {

    DatabaseHelperNotes dbNotes;
    List<Note> al = new ArrayList<Note>();
    ViewGroup linearLayout;
    Cursor c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notes);
        populateArrayList();
        populateList();
    }

    protected void onResume(){
        super.onResume();
        populateArrayList();
        populateList();
    }

    public void populateArrayList(){
        dbNotes = new DatabaseHelperNotes(this);
        c = dbNotes.getAllData();
        al.clear();
        while(c.moveToNext()){
            Note temp = new Note(c.getString(0),c.getString(1),c.getString(2));
            al.add(temp);
        }
    }

    public void populateList() {
        linearLayout = (ViewGroup) findViewById(R.id.lLayout);

        linearLayout.removeAllViews();
        try{
            for(int i = 0; i<al.size();i++) {
                Button bt = new Button(this);
                bt.setText(al.get(i).getTitle() + "\n\n" + al.get(i).getBody());
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                bt.setGravity(Gravity.LEFT);
                bt.setLayoutParams(params);

                final int index = i;
                bt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent editI = new Intent(v.getContext(), EditNote.class);
                        editI.putExtra("id",al.get(index).getID());
                        editI.putExtra("title",al.get(index).getTitle());
                        editI.putExtra("body",al.get(index).getBody());
                        startActivity(editI);
                    }
                });

                linearLayout.addView(bt);
            }
        }
        finally{
            c.close();
        }
    }

    //Creates an instance, insert it, get the id, set that as extra, then delete just created entry.
    public void addOnClick(View view){
        dbNotes = new DatabaseHelperNotes(this);
        c = dbNotes.getAllData();
        dbNotes.insertData("temp", "temp");
        String temp = "";
        while(c.moveToNext()){
            temp = c.getString(0);
        }
        Intent i = new Intent (this, AddNote.class);
        dbNotes.deleteData(temp);
        int tempI = Integer.parseInt(temp) + 1;
        temp = Integer.toString(tempI);
        i.putExtra("id",temp);
        startActivity(i);
    }
}
