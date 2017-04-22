package cs480.bolide.chess;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class ChessBoard_UInterface extends AppCompatActivity {
    private final String[][] PLACEMENT = {{"A8","B8","C8","D8","E8","F8","G8","H8"},
                                    {"A7","B7","C7","D7","E7","F7","G7","H7"},
                                    {"A6","B6","C6","D6","E6","F6","G6","H6"},
                                    {"A5","B5","C5","D5","E5","F5","G5","H5"},
                                    {"A4","B4","C4","D4","E4","F4","G4","H4"},
                                    {"A3","B3","C3","D3","E3","F3","G3","H3"},
                                    {"A2","B2","C2","D2","E2","F2","G2","H2"},
                                    {"A1","B1","C1","D1","E1","F1","G1","H1"}};
    private int x_initial = 0;
    private int y_initial = 0;
    private int x_final = 0;
    private int y_final = 0;
    private boolean alreadySelected = false;
    private GameEngine newGame;
    private ImageButton oldImButton;
    public void init(){
        newGame = new GameEngine();
    }
    public void reset(){
        x_initial = 0;
        y_initial = 0;
        x_final = 0;
        y_final = 0;
        alreadySelected = false;
        oldImButton = null;
    }
    public void onClick(View view){
        final ImageButton imButton = (ImageButton) view;
        final TextView debugXY = (TextView)findViewById(R.id.Debug);
        int temp = (Integer.parseInt(imButton.getTag().toString()));
        if(!alreadySelected) {
            if(!newGame.checkForEmpty( temp % 10,temp / 10)){
                alreadySelected = true;
                x_initial = temp % 10;
                y_initial = temp / 10;
                if(newGame.getTurn() == newGame.gameBoard.getPieceAt(y_initial,x_initial).getColor()) {
                    oldImButton = imButton;
                    debugXY.setTextKeepState(newGame.gameBoard.getPieceAt(y_initial, x_initial).printName() + " Selected!");
                }
                else{
                    reset();
                }
            }

        }else{
            x_final = temp%10;
            y_final = temp/10;
            String tempStr =newGame.gameBoard.getPieceAt(y_initial, x_initial).printName() + " to " +
                    PLACEMENT[y_final][x_final];
            //"X-Intial: "+ x_initial + "\nY-Initial: " + y_initial + "\nX-Final" + x_final + "\nY-Final" + y_final;
            debugXY.setTextKeepState(tempStr);
            //if same square is selected twice do nothing adn reset values
            //else send the values to the GameEngine and reset
            if(x_initial == x_final && y_initial == y_final){
                reset();
                debugXY.setTextKeepState("");
            }else{
                boolean validTurn = newGame.turn(x_initial,y_initial,x_final,y_final);

                //checking if move will make check
                int colorToCheck = getCurrentTurn(newGame);
                boolean checkCheck = newGame.isCheck(colorToCheck);
                //This is to check ex)if White move, white will not be in check.
                if(checkCheck == true){
                    debugXY.setTextKeepState("Will be in check");
                }


                if(validTurn == true){
                    imButton.setImageDrawable(oldImButton.getDrawable());
                    oldImButton.setImageDrawable(null);
                    newGame.nextTurn();
                    /*
                        the following code will check if a pawn is ready to be promoted,
                        if so a popup menu will occur
                     */
                    if(newGame.getPromotion() == true){
                        PopupMenu promoteMenu = new PopupMenu(ChessBoard_UInterface.this, imButton);
                        promoteMenu.getMenuInflater().inflate(R.menu.promotion_popup_menu, promoteMenu.getMenu());
                        promoteMenu.show();
                        /*
                            The following final variables are for the use of the onMenuItemClickLister,
                            without them are the keyword final the listener would not function properly
                         */
                        final Color currentColor = newGame.gameBoard.getPieceAt(y_final,x_final).getColor();
                        final  Bitmap Bqueen = BitmapFactory.decodeResource(getResources(),R.drawable.black_queen);
                        final  Bitmap Wqueen = BitmapFactory.decodeResource(getResources(),R.drawable.white_queen);
                        final  Bitmap Brook = BitmapFactory.decodeResource(getResources(),R.drawable.black_rook);
                        final  Bitmap Wrook = BitmapFactory.decodeResource(getResources(),R.drawable.white_rook);
                        final  Bitmap Bknight = BitmapFactory.decodeResource(getResources(),R.drawable.black_knight);
                        final  Bitmap Wknight = BitmapFactory.decodeResource(getResources(),R.drawable.white_knight);
                        final  Bitmap Bbishop = BitmapFactory.decodeResource(getResources(),R.drawable.black_bishop);
                        final  Bitmap Wbishop = BitmapFactory.decodeResource(getResources(),R.drawable.white_bishop);
                        final int x = x_final;
                        final int y = y_final;
                        promoteMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                /*
                                The switch statement is to figure out which item was selected User
                                Furthermore, it will promote the pawn into the chosen input and update the Graphic of the chess piece
                                 */
                                switch (item.getItemId()) {
                                    case R.id.PromoteQueen:
                                        newGame.promote(x,y, currentColor, Type.Queen);
                                        if(currentColor==Color.Black){
                                            imButton.setImageBitmap(Bqueen);
                                        }else{
                                            imButton.setImageBitmap(Wqueen);
                                        }
                                        break;
                                    case R.id.PromoteKnight:
                                        newGame.promote(x,y, currentColor, Type.Knight);
                                        if(currentColor==Color.Black){
                                            imButton.setImageBitmap(Bknight);
                                        }else{
                                            imButton.setImageBitmap(Wknight);
                                        }
                                        break;
                                    case R.id.PromoteBishop:
                                        newGame.promote(x,y, currentColor, Type.Bishop);
                                        if(currentColor==Color.Black){
                                            imButton.setImageBitmap(Bbishop);
                                        }else{
                                            imButton.setImageBitmap(Wbishop);
                                        }
                                        break;
                                    case R.id.PromoteRook:
                                        newGame.promote(x,y, currentColor, Type.Rook);
                                        if(currentColor==Color.Black){
                                            imButton.setImageBitmap(Brook);
                                        }else{
                                            imButton.setImageBitmap(Wrook);
                                        }
                                        break;
                                    default:
                                        newGame.promote(x,y, currentColor, Type.Queen);
                                        if(currentColor==Color.Black){
                                            imButton.setImageBitmap(Bqueen);
                                        }else{
                                            imButton.setImageBitmap(Wqueen);
                                        }
                                        break;
                                }
                                    return false;
                                }
                            });
                        }
                }else{
                    debugXY.setTextKeepState("Invalid Move");
                }
                reset();
            }
        }
    }

    public int getCurrentTurn(GameEngine ge){
        if(ge.getTurn() == Color.White){
            return 0;
        }else
            return 1;
    }

    //Createa the layout
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chess_board);
        init();
    }
}