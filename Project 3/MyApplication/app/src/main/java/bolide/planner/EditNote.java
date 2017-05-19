package bolide.planner;

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

public class EditNote extends AppCompatActivity {
    EditText editTextTitle,editTextBody;
    Button btn_Save, btn_Delete, btn_Record, btn_Play, btn_StopPlayback;
    DatabaseHelperNotes dbNotes;
    String id;

    MediaPlayer mPlayer = new MediaPlayer();
    MediaRecorder recorder = new MediaRecorder();

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
        btn_Record = (Button)findViewById(R.id.btn_Record);
        btn_Play = (Button)findViewById(R.id.btn_Play);
        btn_StopPlayback = (Button) findViewById(R.id.btn_StopPlayback);

        editTextTitle.setText(extras.getString("title"));
        editTextBody.setText(extras.getString("body"));
        id = extras.getString("id");
        btn_Save();
        btn_Delete();

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



    private void btn_Delete() {
        btn_Delete.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        dbNotes.deleteData(getIntent().getExtras().getString("id"));
                        String fileName = getFilesDir()+"/"+id+"n.m4a";
                        File myFile = new File(fileName);
                        if(myFile.exists())
                            myFile.delete();
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
