package bolide.planner;

import android.content.ContentValues;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class AddNote extends AppCompatActivity {
    EditText editTextTitle,editTextBody;
    Button btn_Save, btn_Record, btn_Play, btn_StopPlayback;
    DatabaseHelperNotes dbNotes;
    String id;

    MediaPlayer mPlayer = new MediaPlayer();
    MediaRecorder recorder = new MediaRecorder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        Bundle extras = getIntent().getExtras();
        id = extras.getString("id");

        dbNotes = new DatabaseHelperNotes(this);
        editTextTitle = (EditText)findViewById(R.id.editTextTitle);
        editTextBody = (EditText)findViewById(R.id.editTextBody);
        btn_Save = (Button)findViewById(R.id.btn_Save);
        btn_Record = (Button)findViewById(R.id.btn_Record);
        btn_Play = (Button)findViewById(R.id.btn_Play);
        btn_StopPlayback = (Button) findViewById(R.id.btn_StopPlayback);
        btn_Save();

        //WIP >>
        btn_Play.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        mPlayer.reset();
                        try {
                            mPlayer.setDataSource(getFilesDir()+"/"+id+"n.m4a");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            mPlayer.prepare();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mPlayer.start();
                    }
                }
        );

        btn_StopPlayback.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mPlayer.stop();
                mPlayer.reset();
                return false;
            }
        });

        btn_Record.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    String fileName = getFilesDir()+"/"+id+"n.m4a";
                    File myFile = new File(fileName);
                    if(myFile.exists())
                        myFile.delete();
                    recorder = new MediaRecorder();
                    recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
                    recorder.setOutputFile(getFilesDir()+"/"+id+"n.m4a");
                    try {
                        recorder.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    recorder.start();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    recorder.reset();
                    recorder.release();
                }
                return false;
            }
        });


        //WIP <<
    }

    @Override
    public void onBackPressed() {
        String fileName = getFilesDir()+"/"+id+"n.m4a";
        File myFile = new File(fileName);
        if(myFile.exists())
            myFile.delete();
        setResult(RESULT_CANCELED);
        super.onBackPressed();
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
                        finish();
                    }
                }
        );
    }

}
