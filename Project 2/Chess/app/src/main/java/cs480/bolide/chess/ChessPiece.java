package cs480.bolide.chess;

/**
 * Created by bryce on 4/17/2017.
 */

public abstract class ChessPiece {
    private int p1,p2;
    private boolean moved;
    private Color color;
    private Type type;
    public ChessPiece(){
        p1 = 0;
        p2 = 0;
        moved = false;
        type = null;
        color = null;
    }
    public ChessPiece(int x, int y, Color c, Type t){
        p1 = x;
        p2 = y;
        moved = false;
        color =c;
        type = t;


    }
    public void setPosition(int x, int y){
        p1 = x;
        p2 = y;
    }
    public int getP1(){
        return p1;
    }
    public int getP2(){
        return p2;
    }
    public Type getType(){
        return type;
    }
    public Color getColor(){ return color; }
    public void setMoved(boolean a){
        moved = a;
    }
    public boolean getMoved(){ return moved;}






}


