package cs480.bolide.chess;

/**
 * Created by bryce on 4/18/2017.
 */

public class King extends ChessPiece {
    private boolean inCheck;
    public King(){
        super();
        inCheck = false;
    }
    public King(Color c){
        super(-1,-1, c,Type.King);
        inCheck = false;

    }
    public King(int x, int y, Color c){
        super(x,y,c,Type.King);
        inCheck = false;
    }
}
