package cs480.bolide.chess;

/**
 * Created by bryce on 4/19/2017.
 */

public class ArrayBoard {
    private ChessPiece[][] board;

    public ArrayBoard() {
        board = new ChessPiece[8][8];
        //setting pieces on the board
        // Pawns
        for (int i = 0; i < 8; i++) {
            board[1][i] = new Pawn(i, 6, Color.Black);
            board[6][i] = new Pawn(i, 6, Color.White);
        }
        //Rooks
        board[0][0] = new Rook(0, 0, Color.Black);
        board[0][7] = new Rook(7, 0, Color.Black);

        board[7][0] = new Rook(0, 7, Color.White);
        board[7][7] = new Rook(7, 7, Color.White);

        //Knight
        board[0][1] = new Knight(1, 0, Color.Black);
        board[0][6] = new Knight(6, 0, Color.Black);

        board[7][1] = new Knight(1, 7, Color.White);
        board[7][6] = new Knight(6, 7, Color.White);

        //Bishop
        board[0][2] = new Bishop(2, 0, Color.Black);
        board[0][5] = new Bishop(5, 0, Color.Black);

        board[7][2] = new Bishop(7, 2, Color.White);
        board[7][5] = new Bishop(7, 5, Color.White);

        //Queen
        board[0][3] = new Queen(3, 0, Color.Black);

        board[7][3] = new Queen(3, 7, Color.White);

        //King
        board[0][4] = new King(4, 7, Color.Black);

        board[7][4] = new King(4, 7, Color.White);

        //Empty Spaces
        for (int i = 2; i < 6; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = new Empty_Space(i, j);
            }
        }
    }

    public ChessPiece getPieceAt(int x, int y) {
        return board[x][y];
    }

    public void move(int x1, int y1, int x2, int y2) {
        board[y2][x2]= board[y1][x1];
        board[y1][x1] = new Empty_Space(y1,x1);
    }





}
