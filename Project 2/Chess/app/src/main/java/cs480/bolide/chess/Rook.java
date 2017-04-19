package cs480.bolide.chess;

/**
 * Created by bryce on 4/18/2017.
 */

public class Rook extends ChessPiece {
    public Rook(){
        super();
    }
    public Rook(Color c){
        super(-1,-1, c,Type.Rook);

    }
    public Rook(int x, int y, Color c){
        super(x,y,c,Type.Rook);


    }


}
