package cs480.bolide.chess;

/**
 * Created by bryce on 4/18/2017.
 */

public class Pawn extends ChessPiece {
    public Pawn(){
        super();
    }
    public Pawn(Color c){
        super(-1,-1, c,Type.Pawn);

    }
    public Pawn(int x, int y, Color c){
        super(x,y,c,Type.Pawn);


    }

}
