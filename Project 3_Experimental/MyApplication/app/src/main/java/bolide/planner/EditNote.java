package bolide.planner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditNote extends AppCompatActivity {
    EditText editTextTitle,editTextBody;
    Button btn_Save, btn_Delete;
    DatabaseHelperNotes dbNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        Bundle extras = getIntent().getExtras();

        dbNotes = new DatabaseHelperNotes(this);
        editTextTitle = (EditText)findViewById(R.id.editTextTitle);
        editTextBody = (EditText)findViewById(R.id.editTextBody);
        btn_Save = (Button)findViewById(R.id.btn_Save);
        btn_Delete = (Button)findViewById(R.id.btn_Delete);

        editTextTitle.setText(extras.getString("title"));
        editTextBody.setText(extras.getString("body"));
        btn_Save();
        btn_Delete();
    }

    private void btn_Delete() {
        btn_Delete.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        dbNotes.deleteData(getIntent().getExtras().getString("id"));
                        finish();
                    }
                }
        );
    }

    private void btn_Save() {
        btn_Save.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        boolean isUpdated = dbNotes.updateData(getIntent().getExtras().getString("id"), editTextTitle.getText().toString(),
                                editTextBody.getText().toString());
                        if(isUpdated == true){
                            Toast.makeText(EditNote.this,"Data Saved", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(EditNote.this,"Data not Saved", Toast.LENGTH_LONG).show();
                        }
                        finish();
                    }
                }
        );
    }
}
