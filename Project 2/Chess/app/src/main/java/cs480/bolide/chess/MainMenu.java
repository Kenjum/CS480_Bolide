package cs480.bolide.chess;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenu extends AppCompatActivity {

    public Button buttonLocal;

    public void onClick(View View){
        Intent createGame = new Intent(MainMenu.this,ChessBoard_UInterface.class);
        startActivity(createGame);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }

}
