package cs480.bolide.chess;

/**
 * Created by bryce on 4/19/2017.
 */

public class ArrayBoard {
    private ChessPiece[][] board;

    public ArrayBoard(){
        board = new ChessPiece[8][8];
        //setting pieces on the board
        // Pawns
        for(int i =0; i<8;i++){
            board[1][i]= new Pawn(1,i,Color.White);
            board[6][i] = new Pawn(6,i,Color.Black);
        }
        //Rooks
        board[0][0] = new Rook(0,0,Color.White);
        board[0][7] = new Rook(0,7,Color.White);

        board[7][0] = new Rook(7,0,Color.Black);
        board[7][7] = new Rook(7,7,Color.Black);

        //Knight
        board[0][1] = new Knight(0,1,Color.White);
        board[0][6] = new Knight(0,6,Color.White);

        board[7][1] = new Knight(7,1,Color.Black);
        board[7][6] = new Knight(7,6,Color.Black);

        //Bishop
        board[0][2] = new Bishop(0,2,Color.White);
        board[0][5] = new Bishop(0,5,Color.White);

        board[7][2] = new Bishop(7,2,Color.Black);
        board[7][5] = new Bishop(7,5,Color.Black);

        //Queen
        board[0][3] = new Queen(0,3,Color.White);

        board[7][3] = new Queen(7,3,Color.Black);

        //King
        board[0][4] = new King(0,4,Color.White);

        board[7][4] = new King(7,4,Color.Black);

        //Empty Spaces
        for (int i = 2; i <6; i++){
            for(int j = 0; j<8; j++){
                board[i][j] = new Empty_Space(i,j);
            }
        }
    }
    public ChessPiece getPieceAt(int x, int y){
        return board[x][y];
    }






}
