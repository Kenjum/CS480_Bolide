package cs480.bolide.chess;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class LocalChessBoard extends AppCompatActivity {


    public void init(){

    }
    public void onClick(View view){
        Bitmap pawnblk = BitmapFactory.decodeResource(getResources(),R.drawable.black_bishop);
       ImageButton imButton = (ImageButton)view;
        imButton.setImageBitmap(pawnblk);
    }

    //Createa the layout
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_chess_board);
        init();
    }
}