package cs480.bolide.chess;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class ChessBoard_UInterface extends AppCompatActivity {
    private int x_initial = 0;
    private int y_initial = 0;
    private int x_final = 0;
    private int y_final = 0;
    private boolean alreadySelected = false;


    public void init(){
        GameEngine newGame = new GameEngine();
    }
    public void reset(){
        x_initial = 0;
        y_initial = 0;
        x_final = 0;
        y_final = 0;
        alreadySelected = false;
    }
    public void onClick(View view){
        ImageButton imButton = (ImageButton) view;
        TextView debugXY = (TextView)findViewById(R.id.Debug);
        if(!alreadySelected) {
            alreadySelected = true;
            int temp = (Integer.parseInt(imButton.getTag().toString()));
            x_initial = temp%10;
            y_initial = temp/10;
            debugXY.setTextKeepState("First Square Selected!!!");

        }else{
            alreadySelected = false;
            int temp = (Integer.parseInt(imButton.getTag().toString())) ;
            x_final = temp%10;
            y_final = temp/10;
            String tempStr = "X-Intial: "+ x_initial + "\nY-Initial: " + y_initial + "\nX-Final" + x_final + "\nY-Final" + y_final;
            debugXY.setTextKeepState(tempStr);
            //if same square is selected twice do nothing adn reset values
            //else send the values to the GameEngine and reset
            if(x_initial == x_final && y_initial == y_final){
                reset();
            }else{

                reset();
            }
        }
    }

    //Createa the layout
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chess_board);
        init();
    }
}