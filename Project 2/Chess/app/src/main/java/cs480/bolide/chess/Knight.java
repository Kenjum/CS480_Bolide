package cs480.bolide.chess;

/**
 * Created by bryce on 4/18/2017.
 */

public class Knight extends ChessPiece {
    public Knight(){
        super();
    }
    public Knight(Color c){
        super(-1,-1, c,Type.Knight);

    }
    public Knight(int x, int y, Color c){
        super(x,y,c,Type.Knight);


    }
}
