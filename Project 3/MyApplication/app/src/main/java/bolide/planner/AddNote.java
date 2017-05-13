package bolide.planner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddNote extends AppCompatActivity {
    EditText editTextTitle,editTextBody;
    Button btn_Save;
    DatabaseHelperNotes dbNotes;
    ViewNotes vn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        dbNotes = new DatabaseHelperNotes(this);
        vn = new ViewNotes();
        editTextTitle = (EditText)findViewById(R.id.editTextTitle);
        editTextBody = (EditText)findViewById(R.id.editTextBody);
        btn_Save = (Button)findViewById(R.id.btn_Save);
        btn_Save();
    }

    private void btn_Save() {
        btn_Save.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        boolean isInserted = dbNotes.insertData(editTextTitle.getText().toString(),
                                editTextBody.getText().toString());
                        if(isInserted == true){
                            Toast.makeText(AddNote.this,"Data Inserted", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(AddNote.this,"Data not Inserted", Toast.LENGTH_LONG).show();
                        }

                        //vn.populateList();
                        finish();
                    }
                }
        );
    }
}
