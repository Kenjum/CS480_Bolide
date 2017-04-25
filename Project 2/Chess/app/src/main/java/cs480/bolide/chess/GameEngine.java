package cs480.bolide.chess;


import java.lang.reflect.Array;

import static android.icu.lang.UProperty.MATH;

public class GameEngine {
    private Color turn = Color.White;

    ArrayBoard gameBoard;
    ArrayBoard testBoard;
    private boolean promotable = false;
    private boolean castled = false;
    private boolean queenSideCastled = false;

    public GameEngine() {
        gameBoard = new ArrayBoard();
        testBoard = new ArrayBoard();
    }

    public boolean turn(int x1, int y1, int x2, int y2) {
        promotable = false;
        boolean validturn = validMove(x1, y1, x2, y2);
        //Testing //TODO
        //
        if (validturn == false) {
            return false;
        }

        gameBoard.move(x1, y1, x2, y2);
        if (checkFuture()) {    //checks if you move the piece, will it still be in check
            gameBoard.move(x2, y2, x1, y1);
            return false;
        }
        gameBoard.getPieceAt(y1, x1).setMoved(true);
        checkForPromotion(x2,y2);
        return true;
    }

    public boolean checkFuture(){//TODO
        int playerC = 0;
        if (getTurn() == Color.White) {
            playerC = 0;
        } else{
            playerC = 1;
        }
        boolean unsafeMove = isCheck(playerC);
        System.out.println("Unsafe Move:" + unsafeMove);

        return unsafeMove;
    }

    /*
        promote simply passes the variables int x, int y, Color c, Type newType to the promotePieceAt() function in the ArrayBoard class.
        It is used is the UI class, and to prevent the UI from directly altering the ArrayBoard
     */
    public void promote(int x, int y, Color c, Type newType){
        gameBoard.promotePieceAt(x,y,c, newType);
    }
    /*
        checkForPromotion checks is ChessPiece of Type pawn reaches to the other side of the board.
        Blacks pawns will need to reach any block with y = 7, White pawns will need to reach any block with y = 0,
     */
    public void checkForPromotion(int x,int y){
        ChessPiece tempPiece1 = gameBoard.getPieceAt(y, x);
        if(tempPiece1.getType() == Type.Pawn) {
            if (tempPiece1.getColor() == Color.White) {
                if (y == 0) promotable = true;
            } else {
                if (y == 7) promotable = true;
            }
        }
    }
    /*
        getPromotion simply returns the global boolean variable promotable
        used to see if a pwan is able to be promoted
     */
    public boolean getPromotion(){
        return promotable;
    }

    public boolean validMove(int x1, int y1, int x2, int y2) {

        ChessPiece tempPiece1 = gameBoard.getPieceAt(y1, x1);
        ChessPiece tempPiece2 = gameBoard.getPieceAt(y2, x2);
        if (tempPiece1.getColor() != turn) {
            return false;
        }
        if (tempPiece1.getColor() == tempPiece2.getColor()) {
            return false;
        }
        //The logic for determining if a pawn made a legal move
        if (tempPiece1.getType() == Type.Pawn) {
            if (tempPiece1.getColor() == Color.White) {
                if ((y2 - y1) > 0) return false;
            }
            if (tempPiece1.getColor() == Color.Black) {
                if ((y2 - y1) < 0) return false;
            }
            int MAX_X_MOVE = 1;
            int MAX_Y_MOVE = 1;
            if (tempPiece1.getMoved() == false) {
                MAX_Y_MOVE = 2;
            }
            int xPath = Math.abs(x2 - x1);
            int yPath = Math.abs(y2 - y1);
            if (xPath <= MAX_X_MOVE && yPath <= MAX_Y_MOVE) {
                if (yPath == 2 && xPath > 0) {
                    return false;
                }
                if (yPath == 0 && xPath == 1) {
                    return false;
                }
                if (yPath == 1 && xPath == 1) {
                    if (checkForEmpty(x2, y2)) {
                        return false;
                    }
                    return true;
                }
                if (tempPiece1.getColor() == Color.White) {
                    for (int i = y1 - 1; i >= y2; i--) {
                        if (!checkForEmpty(x2, i))
                            return false;
                    }
                } else if (tempPiece1.getColor() == Color.Black) {
                    for (int i = y1 + 1; i <= y2; i++) {
                        if (!checkForEmpty(x2, i))
                            return false;
                    }
                }
                return true;
            }
        }
        //logic for the move for knights
        if (tempPiece1.getType() == Type.Knight) {
            int xPath = Math.abs(x2 - x1);
            int yPath = Math.abs(y2 - y1);
            if (xPath <= 2 && yPath <= 2) {
                if (xPath == 2 && yPath == 1 || xPath == 1 && yPath == 2) {
                    return true;
                }
            }
        }
        //logic for the move for rooks
        if (tempPiece1.getType() == Type.Rook) {
            int xPath = Math.abs(x2 - x1);
            int yPath = Math.abs(y2 - y1);
            if (xPath != 0 && yPath == 0 || xPath == 0 && yPath != 0) {
                if (xPath != 0) {
                    if (x2 - x1 < 0) {
                        for (int i = x1 - 1; i > x2; i--) {
                            if (!checkForEmpty(i, y2)) return false;
                        }
                    } else {
                        for (int i = x1 + 1; i < x2; i++) {
                            if (!checkForEmpty(i, y2))
                                return false;
                        }
                    }
                    return true;
                } else {
                    if (y2 - y1 < 0) {
                        for (int i = y1 - 1; i > y2; i--) {
                            if (!checkForEmpty(x2, i)) return false;
                        }
                    } else {
                        for (int i = y1 + 1; i < y2; i++) {
                            if (!checkForEmpty(x2, i)) return false;
                        }
                    }
                    return true;
                }
            }
        }
        //logic for the move of the bishop
        if (tempPiece1.getType() == Type.Bishop) {
            int xPath = Math.abs(x2 - x1);
            int yPath = Math.abs(y2 - y1);
            if (xPath == yPath) {
                if (x2 - x1 < 0 && y2 - y1 < 0) {
                    int i = x1 - 1;
                    int j = y1 - 1;
                    while (i > x2 && j > y2) {
                        if (!checkForEmpty(i, j)) return false;
                        i--;
                        j--;
                    }
                }
                if (x2 - x1 > 0 && y2 - y1 > 0) {
                    int i = x1 + 1;
                    int j = y1 + 1;
                    while (i < x2 && j < y2) {
                        if (!checkForEmpty(i, j)) return false;
                        i++;
                        j++;
                    }
                }
                if (x2 - x1 < 0 && y2 - y1 > 0) {
                    int i = x1 - 1;
                    int j = y1 + 1;
                    while (i > x2 && j < y2) {
                        if (!checkForEmpty(i, j)) return false;
                        i--;
                        j++;
                    }
                }
                if (x2 - x1 > 0 && y2 - y1 < 0) {
                    int i = x1 + 1;
                    int j = y1 - 1;
                    while (i < x2 && j > y2) {
                        if (!checkForEmpty(i, j)) return false;
                        i++;
                        j--;
                    }
                }
                return true;
            }
        }
        //logic for the move of the queen
        if (tempPiece1.getType() == Type.Queen) {
            int xPath = Math.abs(x2 - x1);
            int yPath = Math.abs(y2 - y1);
            if (xPath != 0 && yPath == 0 || xPath == 0 && yPath != 0) {
                if (xPath != 0) {
                    if (x2 - x1 < 0) {
                        for (int i = x1 - 1; i > x2; i--) {
                            if (!checkForEmpty(i, y2)) return false;
                        }
                    } else {
                        for (int i = x1 + 1; i < x2; i++) {
                            if (!checkForEmpty(i, y2))
                                return false;
                        }
                    }
                    return true;
                } else {
                    if (y2 - y1 < 0) {
                        for (int i = y1 - 1; i > y2; i--) {
                            if (!checkForEmpty(x2, i)) return false;
                        }
                    } else {
                        for (int i = y1 + 1; i < y2; i++) {
                            if (!checkForEmpty(x2, i)) return false;
                        }
                    }
                    return true;
                }
            }
            if (xPath == yPath) {
                if (x2 - x1 < 0 && y2 - y1 < 0) {
                    int i = x1 - 1;
                    int j = y1 - 1;
                    while (i > x2 && j > y2) {
                        if (!checkForEmpty(i, j)) return false;
                        i--;
                        j--;
                    }
                }
                if (x2 - x1 > 0 && y2 - y1 > 0) {
                    int i = x1 + 1;
                    int j = y1 + 1;
                    while (i < x2 && j < y2) {
                        if (!checkForEmpty(i, j)) return false;
                        i++;
                        j++;
                    }
                }
                if (x2 - x1 < 0 && y2 - y1 > 0) {
                    int i = x1 - 1;
                    int j = y1 + 1;
                    while (i > x2 && j < y2) {
                        if (!checkForEmpty(i, j)) return false;
                        i--;
                        j++;
                    }
                }
                if (x2 - x1 > 0 && y2 - y1 < 0) {
                    int i = x1 + 1;
                    int j = y1 - 1;
                    while (i < x2 && j > y2) {
                        if (!checkForEmpty(i, j)) return false;
                        i++;
                        j--;
                    }
                }
                return true;
            }
        }
        //logic for king movement
        if(tempPiece1.getType()==Type.King){
            //avoid pawn
            if(turn == Color.White){
                if(y2-1 >=0 && x2+1<=7 && x2-1 >=0) {
                    if (gameBoard.getPieceAt(y2 - 1, x2 - 1).getColor() != turn && gameBoard.getPieceAt(y2 - 1, x2 - 1).getType() == Type.Pawn) {
                        return false;
                    }
                    if (gameBoard.getPieceAt(y2 - 1, x2 + 1).getColor() != turn && gameBoard.getPieceAt(y2 - 1, x2 + 1).getType() == Type.Pawn) {
                        return false;
                    }
                }
                if(x2 == 0&& y2>0 &&gameBoard.getPieceAt(y2-1,x2+1).getColor()!=turn &&gameBoard.getPieceAt(y2-1,x2+1).getType() ==Type.Pawn ){
                    return false;
                }
                if(x2 == 7&& y2>0 &&gameBoard.getPieceAt(y2-1,x2-1).getColor()!=turn &&gameBoard.getPieceAt(y2-1,x2-1).getType() ==Type.Pawn ){
                    return false;
                }
            }
            else{
                if(y2+ 1 <=7 && x2+1<=7 && x2-1 >=0) {
                    if (gameBoard.getPieceAt(y2 + 1, x2 - 1).getColor() != turn && gameBoard.getPieceAt(y2 + 1, x2 - 1).getType() == Type.Pawn) {
                        return false;
                    }
                    if (gameBoard.getPieceAt(y2 + 1, x2 + 1).getColor() != turn && gameBoard.getPieceAt(y2 + 1, x2 + 1).getType() == Type.Pawn) {
                        return false;
                    }
                }
                if(x2 == 0&& y2<7 &&gameBoard.getPieceAt(y2+1,x2+1).getColor()!=turn &&gameBoard.getPieceAt(y2+1,x2+1).getType() ==Type.Pawn ){
                    return false;
                }
                if(x2 == 7&& y2<7 &&gameBoard.getPieceAt(y2+1,x2-1).getColor()!=turn &&gameBoard.getPieceAt(y2+1,x2-1).getType() ==Type.Pawn ){
                    return false;
                }

            }
            //avoid other king
            if(y2+ 1 <=7 &&y2 -1 >=0&& x2+1<=7 && x2-1 >=0){
                if((gameBoard.getPieceAt(y2+1,x2+1).getType()==Type.King&&!(y2+1==y1 && x2+1 == x1))||
                        (gameBoard.getPieceAt(y2,x2+1).getType()==Type.King&&!(y2==y1 && x2+1 == x1)) ||
                        (gameBoard.getPieceAt(y2-1,x2+1).getType()==Type.King&&!(y2-1==y1 && x2+1 == x1))||
                        (gameBoard.getPieceAt(y2-1,x2).getType()==Type.King&&!(y2-1==y1 && x2 == x1))||
                        (gameBoard.getPieceAt(y2-1,x2-1).getType()==Type.King&&!(y2-1==y1 && x2-1 == x1))||
                        (gameBoard.getPieceAt(y2,x2-1).getType()==Type.King&&!(y2==y1 && x2-1 == x1))||
                        (gameBoard.getPieceAt(y2+1,x2-1).getType()==Type.King&&!(y2+1==y1 && x2-1 == x1))||
                        (gameBoard.getPieceAt(y2+1,x2).getType()==Type.King&&!(y2+1==y1 && x2 == x1))){
                    return false;
                }
            }
            else if(y2 == 7 && x2 == 0){
                if((gameBoard.getPieceAt(y2-1,x2).getType()==Type.King&& gameBoard.getPieceAt(y2-1,x2).getColor() != turn)||
                        (gameBoard.getPieceAt(y2-1,x2+1).getType()==Type.King&& gameBoard.getPieceAt(y2-1,x2+1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2,x2+1).getType()==Type.King&& gameBoard.getPieceAt(y2,x2+1).getColor() != turn)){
                    return false;
                }
            }
            else if(y2 == 0 && x2 == 0){
                if((gameBoard.getPieceAt(y2+1,x2).getType()==Type.King&& gameBoard.getPieceAt(y2+1,x2).getColor() != turn)||
                        (gameBoard.getPieceAt(y2+1,x2+1).getType()==Type.King&& gameBoard.getPieceAt(y2+1,x2+1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2,x2+1).getType()==Type.King&& gameBoard.getPieceAt(y2,x2+1).getColor() != turn)){
                    return false;
                }

            }
            else if(y2 == 0 && x2 == 7){
                if((gameBoard.getPieceAt(y2,x2-1).getType()==Type.King&& gameBoard.getPieceAt(y2,x2-1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2+1,x2-1).getType()==Type.King&& gameBoard.getPieceAt(y2+1,x2-1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2+1,x2).getType()==Type.King&& gameBoard.getPieceAt(y2+1,x2).getColor() != turn)){
                    return false;
                }
            }
            else if(y2 == 7 && x2 == 7){
                if((gameBoard.getPieceAt(y2-1,x2).getType()==Type.King&& gameBoard.getPieceAt(y2-1,x2).getColor() != turn)||
                        (gameBoard.getPieceAt(y2-1,x2-1).getType()==Type.King&& gameBoard.getPieceAt(y2-1,x2-1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2,x2-1).getType()==Type.King&& gameBoard.getPieceAt(y2,x2-1).getColor() != turn)){
                    return false;
                }
            }
            else if(y2 == 7){
                if((gameBoard.getPieceAt(y2,x2-1).getType()==Type.King&& gameBoard.getPieceAt(y2,x2-1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2-1,x2-1).getType()==Type.King&& gameBoard.getPieceAt(y2-1,x2-1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2-1,x2).getType()==Type.King&& gameBoard.getPieceAt(y2-1,x2).getColor() != turn)||
                        (gameBoard.getPieceAt(y2-1,x2+1).getType()==Type.King&& gameBoard.getPieceAt(y2-1,x2+1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2,x2+1).getType()==Type.King&& gameBoard.getPieceAt(y2,x2+1).getColor() != turn)){
                    return false;
                }
            }
            else if(y2 ==0){
                if((gameBoard.getPieceAt(y2,x2-1).getType()==Type.King&& gameBoard.getPieceAt(y2,x2-1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2+1,x2-1).getType()==Type.King&& gameBoard.getPieceAt(y2+1,x2-1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2+1,x2).getType()==Type.King&& gameBoard.getPieceAt(y2+1,x2).getColor() != turn)||
                        (gameBoard.getPieceAt(y2+1,x2+1).getType()==Type.King&& gameBoard.getPieceAt(y2+1,x2+1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2,x2+1).getType()==Type.King&& gameBoard.getPieceAt(y2,x2+1).getColor() != turn)){
                    return false;
                }

            }
            else if(x2 ==0){
                if((gameBoard.getPieceAt(y2+1,x2).getType()==Type.King&& gameBoard.getPieceAt(y2+1,x2).getColor() != turn)||
                        (gameBoard.getPieceAt(y2+1,x2+1).getType()==Type.King&& gameBoard.getPieceAt(y2+1,x2+1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2,x2+1).getType()==Type.King&& gameBoard.getPieceAt(y2,x2+1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2-1,x2+1).getType()==Type.King&& gameBoard.getPieceAt(y2-1,x2+1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2+1,x2).getType()==Type.King&& gameBoard.getPieceAt(y2+1,x2).getColor() != turn)){
                    return false;
                }

            }
            else if(x2 ==7){
                if((gameBoard.getPieceAt(y2+1,x2).getType()==Type.King&& gameBoard.getPieceAt(y2+1,x2).getColor() != turn)||
                        (gameBoard.getPieceAt(y2+1,x2-1).getType()==Type.King&& gameBoard.getPieceAt(y2+1,x2-1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2,x2-1).getType()==Type.King&& gameBoard.getPieceAt(y2,x2-1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2-1,x2-1).getType()==Type.King&& gameBoard.getPieceAt(y2-1,x2-1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2-1,x2).getType()==Type.King&& gameBoard.getPieceAt(y2-1,x2).getColor() != turn)){
                    return false;
                }

            }
            //avoid knights
            //1
            if(x2>1 && x2<6 &&y2>1&&y2<6 ){
                if((gameBoard.getPieceAt(y2-2,x2-1).getType()==Type.Knight&& gameBoard.getPieceAt(y2-2,x2-1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2-2,x2+1).getType()==Type.Knight&& gameBoard.getPieceAt(y2-2,x2+1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2-1,x2+2).getType()==Type.Knight&& gameBoard.getPieceAt(y2-1,x2+2).getColor() != turn)||
                        (gameBoard.getPieceAt(y2+1,x2+2).getType()==Type.Knight&& gameBoard.getPieceAt(y2+1,x2+2).getColor() != turn)||
                        (gameBoard.getPieceAt(y2+2,x2+1).getType()==Type.Knight&& gameBoard.getPieceAt(y2+2,x2+1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2+2,x2-1).getType()==Type.Knight&& gameBoard.getPieceAt(y2+2,x2-1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2+1,x2-2).getType()==Type.Knight&& gameBoard.getPieceAt(y2+1,x2-2).getColor() != turn)||
                        (gameBoard.getPieceAt(y2-1,x2-2).getType()==Type.Knight&& gameBoard.getPieceAt(y2-1,x2-2).getColor() != turn)){
                    return false;
                }
            }


            //2
            if(y2 == 1 &&x2>1&&x2<6 ){
                if((    (gameBoard.getPieceAt(y2-1,x2+2).getType()==Type.Knight&& gameBoard.getPieceAt(y2-1,x2+2).getColor() != turn)||
                        (gameBoard.getPieceAt(y2+1,x2+2).getType()==Type.Knight&& gameBoard.getPieceAt(y2+1,x2+2).getColor() != turn)||
                        (gameBoard.getPieceAt(y2+2,x2+1).getType()==Type.Knight&& gameBoard.getPieceAt(y2+2,x2+1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2+2,x2-1).getType()==Type.Knight&& gameBoard.getPieceAt(y2+2,x2-1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2+1,x2-2).getType()==Type.Knight&& gameBoard.getPieceAt(y2+1,x2-2).getColor() != turn)||
                        (gameBoard.getPieceAt(y2-1,x2-2).getType()==Type.Knight&& gameBoard.getPieceAt(y2-1,x2-2).getColor() != turn))){
                    return false;
                }

            }
            if(y2 == 6 &&x2>1&&x2<6 ){
                if((gameBoard.getPieceAt(y2-2,x2-1).getType()==Type.Knight&& gameBoard.getPieceAt(y2-2,x2-1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2-2,x2+1).getType()==Type.Knight&& gameBoard.getPieceAt(y2-2,x2+1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2-1,x2+2).getType()==Type.Knight&& gameBoard.getPieceAt(y2-1,x2+2).getColor() != turn)||
                        (gameBoard.getPieceAt(y2+1,x2-2).getType()==Type.Knight&& gameBoard.getPieceAt(y2+1,x2-2).getColor() != turn)||
                        (gameBoard.getPieceAt(y2-1,x2-2).getType()==Type.Knight&& gameBoard.getPieceAt(y2-1,x2-2).getColor() != turn)){
                    return false;
                }
            }
            if(x2==1&& y2>1&&y2<6){
                if((gameBoard.getPieceAt(y2-2,x2-1).getType()==Type.Knight&& gameBoard.getPieceAt(y2-2,x2-1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2-2,x2+1).getType()==Type.Knight&& gameBoard.getPieceAt(y2-2,x2+1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2-1,x2+2).getType()==Type.Knight&& gameBoard.getPieceAt(y2-1,x2+2).getColor() != turn)||
                        (gameBoard.getPieceAt(y2+1,x2+2).getType()==Type.Knight&& gameBoard.getPieceAt(y2+1,x2+2).getColor() != turn)||
                        (gameBoard.getPieceAt(y2+2,x2+1).getType()==Type.Knight&& gameBoard.getPieceAt(y2+2,x2+1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2+2,x2-1).getType()==Type.Knight&& gameBoard.getPieceAt(y2+2,x2-1).getColor() != turn)){
                    return false;
                }
            }
            if(x2==6 && y2>1&&y2<6){
                if((gameBoard.getPieceAt(y2-2,x2-1).getType()==Type.Knight&& gameBoard.getPieceAt(y2-2,x2-1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2-2,x2+1).getType()==Type.Knight&& gameBoard.getPieceAt(y2-2,x2+1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2+2,x2+1).getType()==Type.Knight&& gameBoard.getPieceAt(y2+2,x2+1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2+2,x2-1).getType()==Type.Knight&& gameBoard.getPieceAt(y2+2,x2-1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2+1,x2-2).getType()==Type.Knight&& gameBoard.getPieceAt(y2+1,x2-2).getColor() != turn)||
                        (gameBoard.getPieceAt(y2-1,x2-2).getType()==Type.Knight&& gameBoard.getPieceAt(y2-1,x2-2).getColor() != turn)){
                    return false;
                }
            }
            //3
            if(y2 == 0&& x2>1&&x2<6){
                if((gameBoard.getPieceAt(y2+1,x2+2).getType()==Type.Knight&& gameBoard.getPieceAt(y2+1,x2+2).getColor() != turn)||
                        (gameBoard.getPieceAt(y2+2,x2+1).getType()==Type.Knight&& gameBoard.getPieceAt(y2+2,x2+1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2+2,x2-1).getType()==Type.Knight&& gameBoard.getPieceAt(y2+2,x2-1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2+1,x2-2).getType()==Type.Knight&& gameBoard.getPieceAt(y2+1,x2-2).getColor() != turn)){
                    return false;
                }
            }
            if(y2==7&& x2>1&&x2<6){
                if((gameBoard.getPieceAt(y2-2,x2-1).getType()==Type.Knight&& gameBoard.getPieceAt(y2-2,x2-1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2-2,x2+1).getType()==Type.Knight&& gameBoard.getPieceAt(y2-2,x2+1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2-1,x2+2).getType()==Type.Knight&& gameBoard.getPieceAt(y2-1,x2+2).getColor() != turn)||
                        (gameBoard.getPieceAt(y2-1,x2-2).getType()==Type.Knight&& gameBoard.getPieceAt(y2-1,x2-2).getColor() != turn)){
                    return false;
                }

            }
            if(x2==0 && y2>1&&y2<6){
                if((gameBoard.getPieceAt(y2-2,x2+1).getType()==Type.Knight&& gameBoard.getPieceAt(y2-2,x2+1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2-1,x2+2).getType()==Type.Knight&& gameBoard.getPieceAt(y2-1,x2+2).getColor() != turn)||
                        (gameBoard.getPieceAt(y2+1,x2+2).getType()==Type.Knight&& gameBoard.getPieceAt(y2+1,x2+2).getColor() != turn)||
                        (gameBoard.getPieceAt(y2+2,x2+1).getType()==Type.Knight&& gameBoard.getPieceAt(y2+2,x2+1).getColor() != turn)){
                    return false;
                }
            }
            if(x2==7 && y2>1 && y2<6){
                if((gameBoard.getPieceAt(y2-2,x2-1).getType()==Type.Knight&& gameBoard.getPieceAt(y2-2,x2-1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2+2,x2-1).getType()==Type.Knight&& gameBoard.getPieceAt(y2+2,x2-1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2+1,x2-2).getType()==Type.Knight&& gameBoard.getPieceAt(y2+1,x2-2).getColor() != turn)||
                        (gameBoard.getPieceAt(y2-1,x2-2).getType()==Type.Knight&& gameBoard.getPieceAt(y2-1,x2-2).getColor() != turn)){
                    return false;
                }
            }
            //4 four corners
            if(x2==0 &&y2==0){
                if((gameBoard.getPieceAt(y2+1,x2+2).getType()==Type.Knight&& gameBoard.getPieceAt(y2+1,x2+2).getColor() != turn)||
                        (gameBoard.getPieceAt(y2+2,x2+1).getType()==Type.Knight&& gameBoard.getPieceAt(y2+2,x2+1).getColor() != turn)){
                    return false;
                }

            }
            if(x2==0&& y2==7){
                if((gameBoard.getPieceAt(y2-2,x2+1).getType()==Type.Knight&& gameBoard.getPieceAt(y2-2,x2+1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2-1,x2+2).getType()==Type.Knight&& gameBoard.getPieceAt(y2-1,x2+2).getColor() != turn)){
                    return false;
                }
            }
            if(x2== 7 && y2 == 0){
                if((gameBoard.getPieceAt(y2+2,x2-1).getType()==Type.Knight&& gameBoard.getPieceAt(y2+2,x2-1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2+1,x2-2).getType()==Type.Knight&& gameBoard.getPieceAt(y2+1,x2-2).getColor() != turn)){
                    return false;
                }
            }
            if(x2==7&&y2==7){
                if((gameBoard.getPieceAt(y2-2,x2-1).getType()==Type.Knight&& gameBoard.getPieceAt(y2-2,x2-1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2-1,x2-2).getType()==Type.Knight&& gameBoard.getPieceAt(y2-1,x2-2).getColor() != turn)){
                    return false;
                }
            }

            //5 top left quadrant
            if(x2==1&&y2==0){
                if((gameBoard.getPieceAt(y2+1,x2+2).getType()==Type.Knight&& gameBoard.getPieceAt(y2+1,x2+2).getColor() != turn)||
                        (gameBoard.getPieceAt(y2+2,x2+1).getType()==Type.Knight&& gameBoard.getPieceAt(y2+2,x2+1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2+2,x2-1).getType()==Type.Knight&& gameBoard.getPieceAt(y2+2,x2-1).getColor() != turn)){
                    return false;
                }
            }
            if(x2==0&& y2==1){
                if((gameBoard.getPieceAt(y2-1,x2+2).getType()==Type.Knight&& gameBoard.getPieceAt(y2-1,x2+2).getColor() != turn)||
                        (gameBoard.getPieceAt(y2+1,x2+2).getType()==Type.Knight&& gameBoard.getPieceAt(y2+1,x2+2).getColor() != turn)||
                        (gameBoard.getPieceAt(y2+2,x2+1).getType()==Type.Knight&& gameBoard.getPieceAt(y2+2,x2+1).getColor() != turn)){
                    return false;
                }
            }
            if(x2==1 && y2 == 1){
                if((gameBoard.getPieceAt(y2-1,x2+2).getType()==Type.Knight&& gameBoard.getPieceAt(y2-1,x2+2).getColor() != turn)||
                        (gameBoard.getPieceAt(y2+1,x2+2).getType()==Type.Knight&& gameBoard.getPieceAt(y2+1,x2+2).getColor() != turn)||
                        (gameBoard.getPieceAt(y2+2,x2+1).getType()==Type.Knight&& gameBoard.getPieceAt(y2+2,x2+1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2+2,x2-1).getType()==Type.Knight&& gameBoard.getPieceAt(y2+2,x2-1).getColor() != turn)){
                    return false;
                }
            }
            //6 top right quadrant
            if(x2==6&&y2==0){
                if((gameBoard.getPieceAt(y2+2,x2+1).getType()==Type.Knight&& gameBoard.getPieceAt(y2+2,x2+1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2+2,x2-1).getType()==Type.Knight&& gameBoard.getPieceAt(y2+2,x2-1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2+1,x2-2).getType()==Type.Knight&& gameBoard.getPieceAt(y2+1,x2-2).getColor() != turn)){
                    return false;
                }
            }
            if(x2==6&&y2==1){
                if((gameBoard.getPieceAt(y2+2,x2+1).getType()==Type.Knight&& gameBoard.getPieceAt(y2+2,x2+1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2+2,x2-1).getType()==Type.Knight&& gameBoard.getPieceAt(y2+2,x2-1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2+1,x2-2).getType()==Type.Knight&& gameBoard.getPieceAt(y2+1,x2-2).getColor() != turn)||
                        (gameBoard.getPieceAt(y2-1,x2-2).getType()==Type.Knight&& gameBoard.getPieceAt(y2-1,x2-2).getColor() != turn)){
                    return false;
                }
            }
            if(x2==7&&y2==1){
                if((gameBoard.getPieceAt(y2+2,x2-1).getType()==Type.Knight&& gameBoard.getPieceAt(y2+2,x2-1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2+1,x2-2).getType()==Type.Knight&& gameBoard.getPieceAt(y2+1,x2-2).getColor() != turn)||
                        (gameBoard.getPieceAt(y2-1,x2-2).getType()==Type.Knight&& gameBoard.getPieceAt(y2-1,x2-2).getColor() != turn)){
                    return false;
                }
            }
            //7 bottom left quadrant
            if(x2==0&&y2==6){
                if((gameBoard.getPieceAt(y2-2,x2+1).getType()==Type.Knight&& gameBoard.getPieceAt(y2-2,x2+1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2-1,x2+2).getType()==Type.Knight&& gameBoard.getPieceAt(y2-1,x2+2).getColor() != turn)||
                        (gameBoard.getPieceAt(y2+1,x2+2).getType()==Type.Knight&& gameBoard.getPieceAt(y2+1,x2+2).getColor() != turn)){
                    return false;
                }
            }
            if(x2==1&&y2==6){
                if((gameBoard.getPieceAt(y2-2,x2-1).getType()==Type.Knight&& gameBoard.getPieceAt(y2-2,x2-1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2-2,x2+1).getType()==Type.Knight&& gameBoard.getPieceAt(y2-2,x2+1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2-1,x2+2).getType()==Type.Knight&& gameBoard.getPieceAt(y2-1,x2+2).getColor() != turn)||
                        (gameBoard.getPieceAt(y2+1,x2+2).getType()==Type.Knight&& gameBoard.getPieceAt(y2+1,x2+2).getColor() != turn)){
                    return false;
                }
            }
            if(x2==1&&y2==7){
                if((gameBoard.getPieceAt(y2-2,x2-1).getType()==Type.Knight&& gameBoard.getPieceAt(y2-2,x2-1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2-2,x2+1).getType()==Type.Knight&& gameBoard.getPieceAt(y2-2,x2+1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2-1,x2+2).getType()==Type.Knight&& gameBoard.getPieceAt(y2-1,x2+2).getColor() != turn)){
                    return false;
                }
            }
            //8
            if(x2==6&&y2==6){
                if((gameBoard.getPieceAt(y2-2,x2-1).getType()==Type.Knight&& gameBoard.getPieceAt(y2-2,x2-1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2-2,x2+1).getType()==Type.Knight&& gameBoard.getPieceAt(y2-2,x2+1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2+1,x2-2).getType()==Type.Knight&& gameBoard.getPieceAt(y2+1,x2-2).getColor() != turn)||
                        (gameBoard.getPieceAt(y2-1,x2-2).getType()==Type.Knight&& gameBoard.getPieceAt(y2-1,x2-2).getColor() != turn)){
                    return false;
                }
            }
            if(x2==6&&y2==7){
                if((gameBoard.getPieceAt(y2-2,x2-1).getType()==Type.Knight&& gameBoard.getPieceAt(y2-2,x2-1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2-2,x2+1).getType()==Type.Knight&& gameBoard.getPieceAt(y2-2,x2+1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2-1,x2-2).getType()==Type.Knight&& gameBoard.getPieceAt(y2-1,x2-2).getColor() != turn)){
                    return false;
                }
            }
            if(x2==7&&y2==6){
                if((gameBoard.getPieceAt(y2-2,x2-1).getType()==Type.Knight&& gameBoard.getPieceAt(y2-2,x2-1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2+1,x2-2).getType()==Type.Knight&& gameBoard.getPieceAt(y2+1,x2-2).getColor() != turn)||
                        (gameBoard.getPieceAt(y2-1,x2-2).getType()==Type.Knight&& gameBoard.getPieceAt(y2-1,x2-2).getColor() != turn)){
                    return false;
                }
            }
            //Rook (and Queen)

                for (int i =x2+1; i<=7;i++){
                    if(x2==7){
                        break;
                    }
                    if(gameBoard.getPieceAt(y2,i).getType()!= Type.Empty_Space) {
                        if (gameBoard.getPieceAt(y2, i).getColor() != turn) {
                            if (gameBoard.getPieceAt(y2, i).getType() == Type.Queen || gameBoard.getPieceAt(y2, i).getType() == Type.Rook) {
                                return false;
                            }
                            else break;
                        }
                        else{
                            if(i!=x1){
                                break;
                            }
                        }
                    }
                }
            for (int i =x2-1; i>=0;i--){
                if(x2==0){
                    break;
                }
                if(gameBoard.getPieceAt(y2,i).getType()!= Type.Empty_Space) {
                    if (gameBoard.getPieceAt(y2, i).getColor() != turn) {
                        if (gameBoard.getPieceAt(y2, i).getType() == Type.Queen || gameBoard.getPieceAt(y2, i).getType() == Type.Rook) {
                            return false;
                        }
                        else break;
                    }
                    else{
                        if(i!=x1){
                            break;
                        }
                    }
                }
            }
            for(int i = y2 + 1; i<=7;i++){
                if(y2==7){
                    break;
                }
                if(gameBoard.getPieceAt(i,x2).getType()!= Type.Empty_Space) {
                    if (gameBoard.getPieceAt(i, x2).getColor() != turn) {
                        if (gameBoard.getPieceAt(i, x2).getType() == Type.Queen || gameBoard.getPieceAt(i, x2).getType() == Type.Rook) {
                            return false;
                        }
                        else break;
                    }
                    else{
                        if(i!=y1){
                            break;
                        }
                    }
                }

            }
            for(int i = y2 - 1; i>=0;i--) {
                if (y2 == 0) {
                    break;
                }
                if (gameBoard.getPieceAt(i, x2).getType() != Type.Empty_Space) {
                    if (gameBoard.getPieceAt(i, x2).getColor() != turn) {
                        if (gameBoard.getPieceAt(i, x2).getType() == Type.Queen || gameBoard.getPieceAt(i, x2).getType() == Type.Rook) {
                            return false;
                        }
                        else break;
                    } else {
                        if(i != y1) {

                            break;
                        }
                    }
                }
            }

            //bishop (and Queen)

            int tempx=x2+1;
            int tempy=y2+1;
            while(tempx<=7&&tempy<=7){

                if(gameBoard.getPieceAt(tempy,tempx).getColor()==turn){
                    if(tempx!=x1||tempy!=y1) {
                        break;
                    }
                }
                if(gameBoard.getPieceAt(tempy,tempx).getType() == Type.Queen||gameBoard.getPieceAt(tempy,tempx).getType() == Type.Bishop){
                    return false;
                }
                if(gameBoard.getPieceAt(tempy,tempx).getType() == Type.Empty_Space) {
                    tempx++;
                    tempy++;
                }
                else{
                    break;
                }
            }
            tempx=x2-1;
            tempy=y2-1;
            while(tempx>=0&&tempy>=0){
                if(gameBoard.getPieceAt(tempy,tempx).getColor()==turn){
                    if(tempx!=x1||tempy!=y1) {
                        break;
                    }
                }
                if(gameBoard.getPieceAt(tempy,tempx).getType() == Type.Queen||gameBoard.getPieceAt(tempy,tempx).getType() == Type.Bishop){
                    return false;
                }
                if(gameBoard.getPieceAt(tempy,tempx).getType() == Type.Empty_Space) {
                    tempx--;
                    tempy--;
                }

                else{
                    break;
                }

            }
            tempx=x2+1;
            tempy=y2-1;
            while(tempx<=7&&tempy>=0){
                if(gameBoard.getPieceAt(tempy,tempx).getColor()==turn){
                    if(tempx!=x1||tempy!=y1) {
                        break;
                    }
                }
                if(gameBoard.getPieceAt(tempy,tempx).getType() == Type.Queen||gameBoard.getPieceAt(tempy,tempx).getType() == Type.Bishop){
                    return false;
                }
                if(gameBoard.getPieceAt(tempy,tempx).getType() == Type.Empty_Space) {
                    tempx++;
                    tempy--;
                }
                else{
                    break;
                }
            }
            tempx=x2-1;
            tempy=y2+1;
            while(tempx>=0&&tempy<=7){
                if(gameBoard.getPieceAt(tempy,tempx).getColor()==turn){
                    if(tempx!=x1||tempy!=y1) {
                        break;
                    }
                }
                if(gameBoard.getPieceAt(tempy,tempx).getType() == Type.Queen||gameBoard.getPieceAt(tempy,tempx).getType() == Type.Bishop){
                    return false;
                }
                if(gameBoard.getPieceAt(tempy,tempx).getType() == Type.Empty_Space) {
                    tempx--;
                    tempy++;
                }
                else{
                    break;
                }
            }

            //white castling
            if(turn == Color.White){
                //Queen side castle
                if(y1==7&&x1==4&&x2==2&&y2==7) {
                    if(this.isCheck(0)){
                        return false;
                    }
                    if (!gameBoard.getPieceAt(y1, x1).getMoved() && !gameBoard.getPieceAt(7, 0).getMoved()) {
                        if(gameBoard.getPieceAt(7,0).getType()!=Type.Rook){
                            return false;
                        }
                        //check for empty path
                        if(gameBoard.getPieceAt(7,1).getType()!=Type.Empty_Space||
                                gameBoard.getPieceAt(7,2).getType()!=Type.Empty_Space||
                                gameBoard.getPieceAt(7,3).getType()!=Type.Empty_Space){
                            return false;

                        }
                        //check for knights
                        if((gameBoard.getPieceAt(6,5).getType()== Type.Knight&&gameBoard.getPieceAt(6,5).getColor()!=turn)||
                                (gameBoard.getPieceAt(5,4).getType()== Type.Knight&&gameBoard.getPieceAt(5,4).getColor()!=turn)||
                                (gameBoard.getPieceAt(5,2).getType()== Type.Knight&&gameBoard.getPieceAt(5,2).getColor()!=turn)||
                                (gameBoard.getPieceAt(6,1).getType()== Type.Knight&&gameBoard.getPieceAt(6,1).getColor()!=turn)||

                                (gameBoard.getPieceAt(6,0).getType()== Type.Knight&&gameBoard.getPieceAt(6,0).getColor()!=turn)||
                                (gameBoard.getPieceAt(5,1).getType()== Type.Knight&&gameBoard.getPieceAt(5,1).getColor()!=turn)||
                                (gameBoard.getPieceAt(5,3).getType()== Type.Knight&&gameBoard.getPieceAt(5,3).getColor()!=turn)||
                                (gameBoard.getPieceAt(6,4).getType()== Type.Knight&&gameBoard.getPieceAt(6,4).getColor()!=turn)){
                            return false;

                        }
                        //pawn check
                        if((gameBoard.getPieceAt(6,2).getType()== Type.Pawn&&gameBoard.getPieceAt(6,2).getColor()!=turn)||
                                (gameBoard.getPieceAt(6,4).getType()== Type.Pawn&&gameBoard.getPieceAt(6,4).getColor()!=turn)){
                            return false;
                        }
                        //rook queen check
                        int pathx = 3, pathy = 7;
                        while(pathy >=0){
                            if(gameBoard.getPieceAt(pathy,pathx).getType()!= Type.Empty_Space){
                                if((gameBoard.getPieceAt(pathy,pathx).getType()!= Type.Rook|| gameBoard.getPieceAt(pathy,pathx).getType()!= Type.Queen)&&
                                        gameBoard.getPieceAt(pathy,pathx).getColor()!= turn){
                                    return false;
                                }else break;
                            }
                            pathy--;
                        }
                        pathx = 2;
                        pathy = 7;
                        while(pathy >=0){
                            if(gameBoard.getPieceAt(pathy,pathx).getType()!= Type.Empty_Space){
                                if((gameBoard.getPieceAt(pathy,pathx).getType()!= Type.Rook|| gameBoard.getPieceAt(pathy,pathx).getType()!= Type.Queen)&&
                                        gameBoard.getPieceAt(pathy,pathx).getColor()!= turn){
                                    return false;
                                }else break;
                            }
                            pathy--;
                        }
                        //bishop queen chck
                        pathx = 3;
                        pathy=7;
                        while(pathx<=7){
                            if(gameBoard.getPieceAt(pathy,pathx).getType()!= Type.Empty_Space){
                                if((gameBoard.getPieceAt(pathy,pathx).getType()!= Type.Bishop|| gameBoard.getPieceAt(pathy,pathx).getType()!= Type.Queen)&&
                                        gameBoard.getPieceAt(pathy,pathx).getColor()!= turn){
                                    return false;
                                }else break;
                            }
                            pathx++;
                            pathy--;

                        }
                        pathx = 3;
                        pathy=7;
                        while(pathx>=0){
                            if(gameBoard.getPieceAt(pathy,pathx).getType()!= Type.Empty_Space){
                                if((gameBoard.getPieceAt(pathy,pathx).getType()!= Type.Bishop|| gameBoard.getPieceAt(pathy,pathx).getType()!= Type.Queen)&&
                                        gameBoard.getPieceAt(pathy,pathx).getColor()!= turn){
                                    return false;
                                }else break;
                            }
                            pathx--;
                            pathy--;

                        }
                        pathx = 2;
                        pathy=7;
                        while(pathx<=7){
                            if(gameBoard.getPieceAt(pathy,pathx).getType()!= Type.Empty_Space){
                                if((gameBoard.getPieceAt(pathy,pathx).getType()!= Type.Bishop|| gameBoard.getPieceAt(pathy,pathx).getType()!= Type.Queen)&&
                                        gameBoard.getPieceAt(pathy,pathx).getColor()!= turn){
                                    return false;
                                }else break;
                            }
                            pathx++;
                            pathy--;

                        }
                        pathx = 2;
                        pathy=7;
                        while(pathx>=0){
                            if(gameBoard.getPieceAt(pathy,pathx).getType()!= Type.Empty_Space){
                                if((gameBoard.getPieceAt(pathy,pathx).getType()!= Type.Bishop|| gameBoard.getPieceAt(pathy,pathx).getType()!= Type.Queen)&&
                                        gameBoard.getPieceAt(pathy,pathx).getColor()!= turn){
                                    return false;
                                }else break;
                            }
                            pathx--;
                            pathy--;

                        }
                        queenSideCastled = true;
                        return true;


                    }
                }
                //king side catsle
                if(y1==7&&x1==4&&x2==6&&y2==7) {
                    if (this.isCheck(1)) {
                        return false;
                    }
                    if (!gameBoard.getPieceAt(y1, x1).getMoved() && !gameBoard.getPieceAt(7, 0).getMoved()) {
                        if (gameBoard.getPieceAt(7, 0).getType() != Type.Rook) {
                            return false;
                        }
                        //check for empty path
                        if (gameBoard.getPieceAt(7, 6).getType() != Type.Empty_Space ||
                                gameBoard.getPieceAt(7, 5).getType() != Type.Empty_Space ){
                            return false;

                        }
                        //check for knights
                        if ((gameBoard.getPieceAt(6, 7).getType() == Type.Knight && gameBoard.getPieceAt(6, 7).getColor() != turn) ||
                                (gameBoard.getPieceAt(5, 6).getType() == Type.Knight && gameBoard.getPieceAt(5, 6).getColor() != turn) ||
                                (gameBoard.getPieceAt(5, 4).getType() == Type.Knight && gameBoard.getPieceAt(5, 4).getColor() != turn) ||
                                (gameBoard.getPieceAt(6, 3).getType() == Type.Knight && gameBoard.getPieceAt(6, 3).getColor() != turn) ){
                            return false;

                        }
                        //pawn check
                        if ((gameBoard.getPieceAt(6, 2).getType() == Type.Pawn && gameBoard.getPieceAt(6, 6).getColor() != turn)) {
                            return false;
                        }
                        //rook queen check
                        int pathx = 5, pathy = 7;
                        while (pathy >= 0) {
                            if (gameBoard.getPieceAt(pathy, pathx).getType() != Type.Empty_Space) {
                                if ((gameBoard.getPieceAt(pathy, pathx).getType() != Type.Rook || gameBoard.getPieceAt(pathy, pathx).getType() != Type.Queen) &&
                                        gameBoard.getPieceAt(pathy, pathx).getColor() != turn) {
                                    return false;
                                } else break;
                            }
                            pathy--;
                        }
                        //bishop queen check
                        pathx = 5;
                        pathy = 7;
                        while (pathx <= 7) {
                            if (gameBoard.getPieceAt(pathy, pathx).getType() != Type.Empty_Space) {
                                if ((gameBoard.getPieceAt(pathy, pathx).getType() != Type.Bishop || gameBoard.getPieceAt(pathy, pathx).getType() != Type.Queen) &&
                                        gameBoard.getPieceAt(pathy, pathx).getColor() != turn) {
                                    return false;
                                } else break;
                            }
                            pathx++;
                            pathy--;

                        }
                        pathx = 5;
                        pathy = 7;
                        while (pathx >= 0) {
                            if (gameBoard.getPieceAt(pathy, pathx).getType() != Type.Empty_Space) {
                                if ((gameBoard.getPieceAt(pathy, pathx).getType() != Type.Bishop || gameBoard.getPieceAt(pathy, pathx).getType() != Type.Queen) &&
                                        gameBoard.getPieceAt(pathy, pathx).getColor() != turn) {
                                    return false;
                                } else break;
                            }
                            pathx--;
                            pathy--;

                        }
                        castled = true;
                        return true;


                    }
                }
            }
            //black castling
            if(turn == Color.Black){
                //Queen side castle
                if(y1==0&&x1==4&&x2==4&&y2==0) {
                    if(this.isCheck(1)){
                        return false;
                    }
                    if (!gameBoard.getPieceAt(y1, x1).getMoved() && !gameBoard.getPieceAt(0, 0).getMoved()) {
                        if(gameBoard.getPieceAt(7,0).getType()!=Type.Rook){
                            return false;
                        }
                        //check for empty path
                        if(gameBoard.getPieceAt(0,1).getType()!=Type.Empty_Space||
                                gameBoard.getPieceAt(0,2).getType()!=Type.Empty_Space||
                                gameBoard.getPieceAt(0,3).getType()!=Type.Empty_Space){
                            return false;

                        }
                        //check for knights
                        if((gameBoard.getPieceAt(1,5).getType()== Type.Knight&&gameBoard.getPieceAt(1,5).getColor()!=turn)||
                                (gameBoard.getPieceAt(2,4).getType()== Type.Knight&&gameBoard.getPieceAt(2,4).getColor()!=turn)||
                                (gameBoard.getPieceAt(2,2).getType()== Type.Knight&&gameBoard.getPieceAt(2,2).getColor()!=turn)||
                                (gameBoard.getPieceAt(1,1).getType()== Type.Knight&&gameBoard.getPieceAt(1,1).getColor()!=turn)||

                                (gameBoard.getPieceAt(6,0).getType()== Type.Knight&&gameBoard.getPieceAt(6,0).getColor()!=turn)||
                                (gameBoard.getPieceAt(5,1).getType()== Type.Knight&&gameBoard.getPieceAt(5,1).getColor()!=turn)||
                                (gameBoard.getPieceAt(5,3).getType()== Type.Knight&&gameBoard.getPieceAt(5,3).getColor()!=turn)||
                                (gameBoard.getPieceAt(6,4).getType()== Type.Knight&&gameBoard.getPieceAt(6,4).getColor()!=turn)){
                            return false;

                        }
                        //pawn check
                        if((gameBoard.getPieceAt(1,2).getType()== Type.Pawn&&gameBoard.getPieceAt(1,2).getColor()!=turn)||
                                (gameBoard.getPieceAt(1,4).getType()== Type.Pawn&&gameBoard.getPieceAt(1,4).getColor()!=turn)){
                            return false;
                        }
                        //rook queen check
                        int pathx = 3, pathy = 0;
                        while(pathy <=7){
                            if(gameBoard.getPieceAt(pathy,pathx).getType()!= Type.Empty_Space){
                                if((gameBoard.getPieceAt(pathy,pathx).getType()!= Type.Rook|| gameBoard.getPieceAt(pathy,pathx).getType()!= Type.Queen)&&
                                        gameBoard.getPieceAt(pathy,pathx).getColor()!= turn){
                                    return false;
                                }else break;
                            }
                            pathy++;
                        }
                        pathx = 2;
                        pathy = 0;
                        while(pathy <=7){
                            if(gameBoard.getPieceAt(pathy,pathx).getType()!= Type.Empty_Space){
                                if((gameBoard.getPieceAt(pathy,pathx).getType()!= Type.Rook|| gameBoard.getPieceAt(pathy,pathx).getType()!= Type.Queen)&&
                                        gameBoard.getPieceAt(pathy,pathx).getColor()!= turn){
                                    return false;
                                }else break;
                            }
                            pathy++;
                        }
                        //bishop queen chck
                        pathx = 3;
                        pathy=0;
                        while(pathx<=7){
                            if(gameBoard.getPieceAt(pathy,pathx).getType()!= Type.Empty_Space){
                                if((gameBoard.getPieceAt(pathy,pathx).getType()!= Type.Bishop|| gameBoard.getPieceAt(pathy,pathx).getType()!= Type.Queen)&&
                                        gameBoard.getPieceAt(pathy,pathx).getColor()!= turn){
                                    return false;
                                }else break;
                            }
                            pathx++;
                            pathy++;

                        }
                        pathx = 3;
                        pathy=0;
                        while(pathx>=0){
                            if(gameBoard.getPieceAt(pathy,pathx).getType()!= Type.Empty_Space){
                                if((gameBoard.getPieceAt(pathy,pathx).getType()!= Type.Bishop|| gameBoard.getPieceAt(pathy,pathx).getType()!= Type.Queen)&&
                                        gameBoard.getPieceAt(pathy,pathx).getColor()!= turn){
                                    return false;
                                }else break;
                            }
                            pathx--;
                            pathy++;

                        }
                        pathx = 2;
                        pathy=0;
                        while(pathx<=7){
                            if(gameBoard.getPieceAt(pathy,pathx).getType()!= Type.Empty_Space){
                                if((gameBoard.getPieceAt(pathy,pathx).getType()!= Type.Bishop|| gameBoard.getPieceAt(pathy,pathx).getType()!= Type.Queen)&&
                                        gameBoard.getPieceAt(pathy,pathx).getColor()!= turn){
                                    return false;
                                }else break;
                            }
                            pathx++;
                            pathy++;

                        }
                        pathx = 2;
                        pathy=0;
                        while(pathx>=0){
                            if(gameBoard.getPieceAt(pathy,pathx).getType()!= Type.Empty_Space){
                                if((gameBoard.getPieceAt(pathy,pathx).getType()!= Type.Bishop|| gameBoard.getPieceAt(pathy,pathx).getType()!= Type.Queen)&&
                                        gameBoard.getPieceAt(pathy,pathx).getColor()!= turn){
                                    return false;
                                }else break;
                            }
                            pathx--;
                            pathy++;

                        }
                        queenSideCastled = true;
                        return true;


                    }
                }
                //king side catsle
                if(y1==0&&x1==4&&x2==6&&y2==0) {
                    if (this.isCheck(1)) {
                        return false;
                    }
                    if (!gameBoard.getPieceAt(y1, x1).getMoved() && !gameBoard.getPieceAt(7, 0).getMoved()) {
                        if (gameBoard.getPieceAt(7, 0).getType() != Type.Rook) {
                            return false;
                        }
                        //check for empty path
                        if (gameBoard.getPieceAt(0, 6).getType() != Type.Empty_Space ||
                                gameBoard.getPieceAt(0, 5).getType() != Type.Empty_Space ){
                            return false;

                        }
                        //check for knights
                        if ((gameBoard.getPieceAt(1, 7).getType() == Type.Knight && gameBoard.getPieceAt(1, 7).getColor() != turn) ||
                                (gameBoard.getPieceAt(2, 6).getType() == Type.Knight && gameBoard.getPieceAt(2, 6).getColor() != turn) ||
                                (gameBoard.getPieceAt(2, 4).getType() == Type.Knight && gameBoard.getPieceAt(2, 4).getColor() != turn) ||
                                (gameBoard.getPieceAt(1, 3).getType() == Type.Knight && gameBoard.getPieceAt(1, 3).getColor() != turn) ){
                            return false;

                        }
                        //pawn check
                        if ((gameBoard.getPieceAt(1, 2).getType() == Type.Pawn && gameBoard.getPieceAt(1, 6).getColor() != turn)) {
                            return false;
                        }
                        //rook queen check
                        int pathx = 5, pathy = 0;
                        while (pathy <=7) {
                            if (gameBoard.getPieceAt(pathy, pathx).getType() != Type.Empty_Space) {
                                if ((gameBoard.getPieceAt(pathy, pathx).getType() != Type.Rook || gameBoard.getPieceAt(pathy, pathx).getType() != Type.Queen) &&
                                        gameBoard.getPieceAt(pathy, pathx).getColor() != turn) {
                                    return false;
                                } else break;
                            }
                            pathy++;
                        }
                        //bishop queen check
                        pathx = 5;
                        pathy = 0;
                        while (pathx <= 7) {
                            if (gameBoard.getPieceAt(pathy, pathx).getType() != Type.Empty_Space) {
                                if ((gameBoard.getPieceAt(pathy, pathx).getType() != Type.Bishop || gameBoard.getPieceAt(pathy, pathx).getType() != Type.Queen) &&
                                        gameBoard.getPieceAt(pathy, pathx).getColor() != turn) {
                                    return false;
                                } else break;
                            }
                            pathx++;
                            pathy++;

                        }
                        pathx = 5;
                        pathy = 0;
                        while (pathx >= 0) {
                            if (gameBoard.getPieceAt(pathy, pathx).getType() != Type.Empty_Space) {
                                if ((gameBoard.getPieceAt(pathy, pathx).getType() != Type.Bishop || gameBoard.getPieceAt(pathy, pathx).getType() != Type.Queen) &&
                                        gameBoard.getPieceAt(pathy, pathx).getColor() != turn) {
                                    return false;
                                } else break;
                            }
                            pathx--;
                            pathy++;

                        }
                        castled = true;
                        return true;


                    }
                }
            }











            if((y2 -y1 == 1|| y2 -y1 == -1) && x1==x2){

                return true;
            }
            else if(y2 == y1 && (x1 -x2 == 1 || x1 -x2 ==-1)){

                return true;
            }
            else if((x1 -x2 == 1 || x1 -x2 ==-1)&&(y2 -y1 == 1|| y2 -y1 == -1)){

                return true;
            }


        }

        return false;
    }

    public boolean checkForEmpty(int x, int y) {
        if (gameBoard.getPieceAt(y, x).getType() == Type.Empty_Space) {
            return true;
        }
        return false;
    }

    public void nextTurn() {
        if (turn == Color.White) {
            turn = Color.Black;
        } else {
            turn = Color.White;
        }
    }

    public Color getTurn() {
        return turn;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    public int findKingY(int blackWhite) {
        //0 is white, 1 is black
        ChessPiece temp = gameBoard.getPieceAt(0, 0);
        int resultY = 0;

        for (int y = 0; y < 8; y++) {      //y
            for (int x = 0; x < 8; x++) {  //x
                //White King
                if(blackWhite == 0){
                    if (gameBoard.getPieceAt(y, x).getType() == Type.King && gameBoard.getPieceAt(y, x).getColor() == Color.White){
                        return y;
                    }
                }
                //BlackKing
                if(blackWhite == 1){
                    if (gameBoard.getPieceAt(y, x).getType() == Type.King && gameBoard.getPieceAt(y, x).getColor() == Color.Black){
                        return y;
                    }
                }
            }
        }
        //get king location
        return resultY;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    public int findKingX(int blackWhite) {
        //0 is white, 1 is black
        ChessPiece temp = gameBoard.getPieceAt(0, 0);
        int resultX = 0;

        for (int y = 0; y < 8; y++) {      //y
            for (int x = 0; x < 8; x++) {  //x
                //White King
                if(blackWhite == 0){
                    if (gameBoard.getPieceAt(y, x).getType() == Type.King && gameBoard.getPieceAt(y, x).getColor() == Color.White){
                        return x;
                    }
                }
                //BlackKing
                if(blackWhite == 1){
                    if (gameBoard.getPieceAt(y, x).getType() == Type.King && gameBoard.getPieceAt(y, x).getColor() == Color.Black){
                        return x;
                    }
                }
            }
        }
        //get king location
        return resultX;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    public boolean isCheck(int blackWhite) {    //TODO
        //0 is checking for white, 1 is checking for black
        //for first king then second
        //false means not in check
        Color BWColor = Color.White;

        if(blackWhite == 1){
            BWColor = Color.Black;
        }else{
            BWColor = Color.White;
        }


        ChessPiece king = new King(findKingY(blackWhite),findKingX(blackWhite),BWColor);
        System.out.println("CurrentPlayer King x:" + king.getP2() + " " + "y:" + king.getP1());
        boolean result = false;

        if (checkPawn(king) == true) {
            result = true;
        }
        if (checkKnight(king) == true) {
            result = true;
        }
        if (checkBishopAndQueen(king) == true) {
            result = true;
        }
        if (checkRookAndQueen(king) == true) {
            result = true;
        }
        if (checkKing(king) == true) {
            result = true;
        }
        return result;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////checkPawn
    public boolean checkPawn(ChessPiece king){  //TODO
        Color enemyC = Color.Black;
        int vertical = 0;
        if(king.getColor() == Color.White){
            vertical = -1;
        }else{
            enemyC = Color.White;
            vertical = 1;
        }

        try {
            if (gameBoard.getPieceAt(king.getP1() + vertical, king.getP2() - 1).getType() == Type.Pawn && gameBoard.getPieceAt(king.getP1() + vertical, king.getP2() - 1).getColor() == enemyC) {
                return true;
            }
        } catch (IndexOutOfBoundsException e) {
        }
        try {
            if (gameBoard.getPieceAt(king.getP1() + vertical, king.getP2() + 1).getType() == Type.Pawn && gameBoard.getPieceAt(king.getP1() + vertical, king.getP2() + 1).getColor() == enemyC) {
                return true;
            }
        } catch (IndexOutOfBoundsException e) {
        }
        return false;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////checkKnight
    public boolean checkKnight(ChessPiece king){ //TODO
        Color enemyC = Color.Black;
        if(king.getColor() == Color.White){
            enemyC = Color.White;
        }

        try {
            if (gameBoard.getPieceAt(king.getP1() - 1, king.getP2() + 2).getType() == Type.Knight && gameBoard.getPieceAt(king.getP1() - 1, king.getP2() + 2).getColor() != enemyC) {
                return true;
            }
        } catch (IndexOutOfBoundsException e) {
        }
        try {
            if (gameBoard.getPieceAt(king.getP1() + 1, king.getP2() + 2).getType() == Type.Knight && gameBoard.getPieceAt(king.getP1() + 1, king.getP2() + 2).getColor() != enemyC) {
                return true;
            }
        } catch (IndexOutOfBoundsException e) {
        }
        try {
            if (gameBoard.getPieceAt(king.getP1() - 1, king.getP2() - 2).getType() == Type.Knight && gameBoard.getPieceAt(king.getP1() - 1, king.getP2() - 2).getColor() != enemyC) {
                return true;
            }
        } catch (IndexOutOfBoundsException e) {
        }
        try {
            if (gameBoard.getPieceAt(king.getP1() + 1, king.getP2() - 2).getType() == Type.Knight && gameBoard.getPieceAt(king.getP1() + 1, king.getP2() - 2).getColor() != enemyC) {
                return true;
            }
        } catch (IndexOutOfBoundsException e) {
        }
        try {
            if (gameBoard.getPieceAt(king.getP1() + 2, king.getP2() - 1).getType() == Type.Knight && gameBoard.getPieceAt(king.getP1() + 2, king.getP2() - 1).getColor() != enemyC) {
                return true;
            }
        } catch (IndexOutOfBoundsException e) {
        }
        try {
            if (gameBoard.getPieceAt(king.getP1() + 2, king.getP2() + 1).getType() == Type.Knight && gameBoard.getPieceAt(king.getP1() + 2, king.getP2() + 1).getColor() != enemyC) {
                return true;
            }
        } catch (IndexOutOfBoundsException e) {
        }
        try {
            if (gameBoard.getPieceAt(king.getP1() - 2, king.getP2() - 1).getType() == Type.Knight && gameBoard.getPieceAt(king.getP1() - 2, king.getP2() - 1).getColor() != enemyC) {
                return true;
            }
        } catch (IndexOutOfBoundsException e) {
        }
        try {
            if (gameBoard.getPieceAt(king.getP1() - 2, king.getP2() + 1).getType() == Type.Knight && gameBoard.getPieceAt(king.getP1() - 2, king.getP2() + 1).getColor() != enemyC) {
                return true;
            }
        } catch (IndexOutOfBoundsException e) {
        }
        return false;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////checkBishopAndQueen
    public boolean checkBishopAndQueen(ChessPiece king){ //TODO
        boolean skip1 = false;
        boolean skip2 = false;
        boolean skip3 = false;
        boolean skip4 = false;

        Color playerC = Color.White;
        Color enemyC = Color.Black;
        if(king.getColor() == Color.Black){
            playerC = Color.Black;
            enemyC = Color.White;
        }

        int y = king.getP1();
        int x = king.getP2();

            while (x < 7 && y < 7) {
                try {
                x++;
                y++;
                if (skip1 == false) {
                    //No check if own pieces
                    if (gameBoard.getPieceAt(y, x).getColor() == playerC) {
                        //Not Check
                        skip1 = true;
                    }
                    //If other pieces other than bishop appear, no check
                    else if (gameBoard.getPieceAt(y, x).getColor() == enemyC) {
                        if (gameBoard.getPieceAt(y, x).getType() == Type.Knight ||
                                gameBoard.getPieceAt(y, x).getType() == Type.Rook ||
                                gameBoard.getPieceAt(y, x).getType() == Type.Pawn ||
                                gameBoard.getPieceAt(y, x).getType() == Type.King) {
                            //Not Check
                            skip1 = true;
                        }
                        //if bishop uninterrupted, check
                        else if (gameBoard.getPieceAt(y, x).getType() == Type.Bishop ||
                                gameBoard.getPieceAt(y, x).getType() == Type.Queen) {
                            //Check
                            return true;
                        }
                    }
                }

            }catch (IndexOutOfBoundsException e){
                }
        }
        y = king.getP1();
        x = king.getP2();

            while (x < 7 && y > 0) {
                try {
                x++;
                y--;
                //checking diagonals
                if (skip2 == false) {
                    //No check if own pieces
                    if (gameBoard.getPieceAt(y, x).getColor() == playerC) {
                        //Not Check
                        skip2 = true;
                    }
                    //If other pieces other than bishop appear, no check
                    else if (gameBoard.getPieceAt(y, x).getColor() == enemyC) {
                        if (gameBoard.getPieceAt(y, x).getType() == Type.Knight ||
                                gameBoard.getPieceAt(y, x).getType() == Type.Rook ||
                                gameBoard.getPieceAt(y, x).getType() == Type.Pawn ||
                                gameBoard.getPieceAt(y, x).getType() == Type.King) {
                            //Not Check
                            skip2 = true;
                        }
                        //if bishop uninterrupted, check
                        else if (gameBoard.getPieceAt(y, x).getType() == Type.Bishop ||
                                gameBoard.getPieceAt(y, x).getType() == Type.Queen) {
                            //Check
                            return true;
                        }
                    }
                }
            }catch(IndexOutOfBoundsException e){
                }
        }
        y = king.getP1();
        x = king.getP2();

            while (x > 0 && y < 7) {
                try {
                x--;
                y++;
                //checking diagonals
                if (skip3 == false) {
                    //No check if own pieces
                    if (gameBoard.getPieceAt(y, x).getColor() == playerC) {
                        //Not Check
                        skip3 = true;
                    }
                    //If other pieces other than bishop appear, no check
                    else if (gameBoard.getPieceAt(y, x).getColor() == enemyC) {
                        if (gameBoard.getPieceAt(y, x).getType() == Type.Knight ||
                                gameBoard.getPieceAt(y, x).getType() == Type.Rook ||
                                gameBoard.getPieceAt(y, x).getType() == Type.Pawn ||
                                gameBoard.getPieceAt(y, x).getType() == Type.King) {
                            //Not Check
                            skip3 = true;
                        }
                        //if bishop uninterrupted, check
                        else if (gameBoard.getPieceAt(y, x).getType() == Type.Bishop ||
                                gameBoard.getPieceAt(y, x).getType() == Type.Queen) {
                            //Check
                            return true;
                        }
                    }
                }
            }catch(IndexOutOfBoundsException e){
                }
        }
        y = king.getP1();
        x = king.getP2();

            while (x > 0 && y > 0) {
                try {
                x--;
                y--;
                //checking diagonals
                if (skip4 == false) {
                    //No check if own pieces
                    if (gameBoard.getPieceAt(y, x).getColor() == playerC) {
                        //Not Check
                        skip4 = true;
                    }
                    //If other pieces other than bishop appear, no check
                    else if (gameBoard.getPieceAt(y, x).getColor() == enemyC) {
                        if (gameBoard.getPieceAt(y, x).getType() == Type.Knight ||
                                gameBoard.getPieceAt(y, x).getType() == Type.Rook ||
                                gameBoard.getPieceAt(y, x).getType() == Type.Pawn ||
                                gameBoard.getPieceAt(y, x).getType() == Type.King) {
                            //Not Check
                            skip4 = true;
                        }
                        //if bishop uninterrupted, check
                        else if (gameBoard.getPieceAt(y, x).getType() == Type.Bishop ||
                                gameBoard.getPieceAt(y, x).getType() == Type.Queen) {
                            //Check
                            return true;
                        }
                    }
                }

            }catch(IndexOutOfBoundsException e){
                }
        }
        return false;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////checkRookAndQueen
    public boolean checkRookAndQueen(ChessPiece king){ //TODO
        Color playerC = Color.White;
        Color enemyC = Color.Black;
        if(king.getColor() == Color.Black){
            playerC = Color.Black;
            enemyC = Color.White;
        }


            //checking horizontal
        int y = king.getP1();
        int x = king.getP2();
            while (x < 8) {
                try {
                    x++;
                if (gameBoard.getPieceAt(y, x).getColor() == playerC) {
                    //Not Check
                    break;
                }
                //If other pieces other than rook appear, no check
                else if (gameBoard.getPieceAt(y, x).getColor() == enemyC) {
                    if (gameBoard.getPieceAt(y, x).getType() == Type.Knight ||
                            gameBoard.getPieceAt(y, x).getType() == Type.Bishop ||
                            gameBoard.getPieceAt(y, x).getType() == Type.Pawn ||
                            gameBoard.getPieceAt(y, x).getType() == Type.King) {
                        //Not Check
                        break;
                    }
                    //if rook uninterrupted, check
                    else if (gameBoard.getPieceAt(y, x).getType() == Type.Rook ||
                            gameBoard.getPieceAt(y, x).getType() == Type.Queen) {
                        //Check
                        return true;
                    }
                }
            }catch (IndexOutOfBoundsException e) {
                }
        }


            //checking horizontal
        y = king.getP1();
        x = king.getP2();
        while (y < 8) {
            try {
                y++;
                if (gameBoard.getPieceAt(y, x).getColor() == playerC) {
                    //Not Check
                    break;
                }
                //If other pieces other than rook appear, no check
                else if (gameBoard.getPieceAt(y, x).getColor() == enemyC) {
                    if (gameBoard.getPieceAt(y, x).getType() == Type.Knight ||
                            gameBoard.getPieceAt(y, x).getType() == Type.Bishop ||
                            gameBoard.getPieceAt(y, x).getType() == Type.Pawn ||
                            gameBoard.getPieceAt(y, x).getType() == Type.King) {
                        //Not Check
                        break;
                    }
                    //if rook uninterrupted, check
                    else if (gameBoard.getPieceAt(y, x).getType() == Type.Rook ||
                            gameBoard.getPieceAt(y, x).getType() == Type.Queen) {
                        //Check
                        return true;
                    }
                }
            }catch (IndexOutOfBoundsException e) {
                }
        }

            //checking horizontal
        y = king.getP1();
        x = king.getP2();
        while (x > 0) {
            try {
                x--;
                if (gameBoard.getPieceAt(y, x).getColor() == playerC) {
                    //Not Check
                    break;
                }
                //If other pieces other than rook appear, no check
                else if (gameBoard.getPieceAt(y, x).getColor() == enemyC) {
                    if (gameBoard.getPieceAt(y, x).getType() == Type.Knight ||
                            gameBoard.getPieceAt(y, x).getType() == Type.Bishop ||
                            gameBoard.getPieceAt(y, x).getType() == Type.Pawn ||
                            gameBoard.getPieceAt(y, x).getType() == Type.King) {
                        //Not Check
                        break;
                    }
                    //if rook uninterrupted, check
                    else if (gameBoard.getPieceAt(y, x).getType() == Type.Rook ||
                            gameBoard.getPieceAt(y, x).getType() == Type.Queen) {
                        //Check
                        return true;
                    }
                }
            }catch (IndexOutOfBoundsException e) {
                }
        }


            //checking horizontal
        y = king.getP1();
        x = king.getP2();
        while (y > 0) {
            try {
                y--;
                if (gameBoard.getPieceAt(y, x).getColor() == playerC) {
                    //Not Check
                    break;
                }
                //If other pieces other than rook appear, no check
                else if (gameBoard.getPieceAt(y, x).getColor() == enemyC) {
                    if (gameBoard.getPieceAt(y, x).getType() == Type.Knight ||
                            gameBoard.getPieceAt(y, x).getType() == Type.Bishop ||
                            gameBoard.getPieceAt(y, x).getType() == Type.Pawn ||
                            gameBoard.getPieceAt(y, x).getType() == Type.King) {
                        //Not Check
                        break;
                    }
                    //if rook uninterrupted, check
                    else if (gameBoard.getPieceAt(y, x).getType() == Type.Rook ||
                            gameBoard.getPieceAt(y, x).getType() == Type.Queen) {
                        //Check
                        return true;
                    }
                }
            }catch (IndexOutOfBoundsException e) {
                }
        }
        return false;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////checkKing
    public boolean checkKing(ChessPiece king){  //TODO
        Color enemyC = Color.Black;
        if(king.getColor() == Color.Black){
            enemyC = Color.White;
        }
        try {
            if (gameBoard.getPieceAt(king.getP1(), king.getP2() - 1).getType() == Type.King && gameBoard.getPieceAt(king.getP1(), king.getP2() - 1).getColor() == enemyC) {
                return true;
            }
        } catch (IndexOutOfBoundsException e) {
        }
        try {
            if (gameBoard.getPieceAt(king.getP1(), king.getP2() + 1).getType() == Type.King && gameBoard.getPieceAt(king.getP1(), king.getP2() + 1).getColor() == enemyC) {
                return true;
            }
        } catch (IndexOutOfBoundsException e) {
        }
        try {
            if (gameBoard.getPieceAt(king.getP1() - 1, king.getP2()).getType() == Type.King && gameBoard.getPieceAt(king.getP1() - 1, king.getP2()).getColor() == enemyC) {
                return true;
            }
        } catch (IndexOutOfBoundsException e) {
        }
        try {
            if (gameBoard.getPieceAt(king.getP1() - 1, king.getP2() - 1).getType() == Type.King && gameBoard.getPieceAt(king.getP1() - 1, king.getP2() - 1).getColor() == enemyC) {
                return true;
            }
        } catch (IndexOutOfBoundsException e) {
        }
        try {
            if (gameBoard.getPieceAt(king.getP1() - 1, king.getP2() + 1).getType() == Type.King && gameBoard.getPieceAt(king.getP1() - 1, king.getP2() + 1).getColor() == enemyC) {
                return true;
            }
        } catch (IndexOutOfBoundsException e) {
        }
        try {
            if (gameBoard.getPieceAt(king.getP1() + 1, king.getP2()).getType() == Type.King && gameBoard.getPieceAt(king.getP1() + 1, king.getP2()).getColor() == enemyC) {
                return true;
            }
        } catch (IndexOutOfBoundsException e) {
        }
        try {
            if (gameBoard.getPieceAt(king.getP1() + 1, king.getP2() - 1).getType() == Type.King && gameBoard.getPieceAt(king.getP1() + 1, king.getP2() - 1).getColor() == enemyC) {
                return true;
            }
        } catch (IndexOutOfBoundsException e) {
        }
        try {
            if (gameBoard.getPieceAt(king.getP1() + 1, king.getP2() + 1).getType() == Type.King && gameBoard.getPieceAt(king.getP1() + 1, king.getP2() + 1).getColor() == enemyC) {
                return true;
            }
        } catch (IndexOutOfBoundsException e) {
        }
        return false;
    }

    public int gameStateCheck(){    //TODO
        //return 0 = ignore. 1 = stalemate. 2 = checkmate
        boolean cantMove = true;
        int result = 0;
        int playerC = 0;   //0 = white, 1 = black
        if(getTurn() == Color.Black){
            playerC = 1;
        }else{
            playerC = 0;
        }

        //go through the whole board
        for(int y = 0; y<8; y++){
            for(int x = 0; x<8; x++){
                //pass in getTurn for which colors to be checking.
                //function for pawn
                if(gameBoard.getPieceAt(y,x).getType() == Type.Pawn && gameBoard.getPieceAt(y,x).getColor() == getTurn()){
                    ChessPiece pawn = new Pawn(y,x,getTurn());
                    if(gameStatePawn(pawn) == false){
                        cantMove = false;
                    }
                }
                //function for knights
                else if(gameBoard.getPieceAt(y,x).getType() == Type.Knight && gameBoard.getPieceAt(y,x).getColor() == getTurn()){
                    ChessPiece knight = new Knight(y,x,getTurn());
                    if(gameStateKnight(knight) == false){
                        cantMove = false;
                    }
                }
                //function for bishop
                else if(gameBoard.getPieceAt(y,x).getType() == Type.Bishop && gameBoard.getPieceAt(y,x).getColor() == getTurn()){
                    ChessPiece bishop = new Bishop(y,x,getTurn());
                    if(gameStateBishop(bishop) == false){
                        cantMove = false;
                    }

                }
                //function for rook
                else if(gameBoard.getPieceAt(y,x).getType() == Type.Rook && gameBoard.getPieceAt(y,x).getColor() == getTurn()){
                    ChessPiece rook = new Rook(y,x,getTurn());
                    if(gameStateRook(rook) == false){
                        cantMove = false;
                    }
                }
                //function for queen
                else if(gameBoard.getPieceAt(y,x).getType() == Type.Queen && gameBoard.getPieceAt(y,x).getColor() == getTurn()){
                    ChessPiece queen = new Queen(y,x,getTurn());
                    if(gameStateQueen(queen) == false){
                        cantMove = false;
                    }
                }
                //function for king
                else if(gameBoard.getPieceAt(y,x).getType() == Type.King && gameBoard.getPieceAt(y,x).getColor() == getTurn()){
                    ChessPiece king = new King(y,x,getTurn());
                    if(gameStateKing(king) == false){
                        cantMove = false;
                    }
                }
            }
        }
        if(onlyKings()){
            result = 1;
        }
        else if(isCheck(playerC) && cantMove){   //checkmate
            result = 2;
        }
        else if(cantMove){                  //stalemate
            result = 1;
        }else{
            result = 0;
        }

        return result;
    }

    public boolean onlyKings(){
        int pieceCount = 0;
        for(int y = 0; y<8; y++){
            for(int x = 0; x<8; x++){
                if (gameBoard.getPieceAt(y,x).getType() != Type.Empty_Space) {
                    pieceCount++;
                }
            }
        }
        if (pieceCount == 2) {
            return true;
        }
        return false;
    }

    //Variable for which direction (for pawn).
    //Check for possible steps
    public boolean gameStatePawn(ChessPiece pawn){ //TODO
        testBoard.cloneBoard(gameBoard);
        int vertical = 0;
        if(getTurn() == Color.Black){
            vertical = 1;
        }else{
            vertical = -1;
        }
        try {
            if (gameBoard.getPieceAt(pawn.getP1() + vertical, pawn.getP2()).getType() == Type.Empty_Space) {
                gameBoard.move(pawn.getP1(), pawn.getP2(), pawn.getP1() + vertical, pawn.getP2());
                if(checkFuture() == false){
                    gameBoard.cloneBoard(testBoard);
                    return false;
                }
                gameBoard.cloneBoard(testBoard);
            }

        } catch (IndexOutOfBoundsException e) {
        }
        try {
            if (gameBoard.getPieceAt(pawn.getP1() + vertical, pawn.getP2() - 1).getType() != Type.Empty_Space && gameBoard.getPieceAt(pawn.getP1() + vertical, pawn.getP2() - 1).getColor() != getTurn()) {
                gameBoard.move(pawn.getP1(), pawn.getP2(), pawn.getP1() + vertical, pawn.getP2() - 1);
                if(checkFuture() == false){
                    gameBoard.cloneBoard(testBoard);
                    return false;
                }
                gameBoard.cloneBoard(testBoard);
            }

        } catch (IndexOutOfBoundsException e) {
        }
        try {
            if (checkFuture() == false && gameBoard.getPieceAt(pawn.getP1() + vertical, pawn.getP2() + 1).getType() != Type.Empty_Space && gameBoard.getPieceAt(pawn.getP1() + vertical, pawn.getP2() + 1).getColor() != getTurn()) {
                gameBoard.move(pawn.getP1(), pawn.getP2(), pawn.getP1() + vertical, pawn.getP2() + 1);
                if(checkFuture() == false){
                    gameBoard.cloneBoard(testBoard);
                    return false;
                }
                gameBoard.cloneBoard(testBoard);
            }

        } catch (IndexOutOfBoundsException e) {
        }
        return true;
    }
    //Knights
    public boolean gameStateKnight(ChessPiece knight){ //TODO
        testBoard.cloneBoard(gameBoard);
        Color playerC = Color.White;
        if(knight.getColor() == Color.Black){
            playerC = Color.Black;
        }

        try {
            if (gameBoard.getPieceAt(knight.getP1() - 1, knight.getP2() + 2).getColor() != playerC) {
                gameBoard.move(knight.getP1(), knight.getP2(), knight.getP1() - 1, knight.getP2() + 2);
                if (checkFuture() == false) {
                    gameBoard.cloneBoard(testBoard);
                    return false;
                }
                gameBoard.cloneBoard(testBoard);
            }

        } catch (IndexOutOfBoundsException e) {
        }
        try {
            if (gameBoard.getPieceAt(knight.getP1() + 1, knight.getP2() + 2).getColor() != playerC) {
                gameBoard.move(knight.getP1(), knight.getP2(), knight.getP1() + 1, knight.getP2() + 2);
                if (checkFuture() == false) {
                    gameBoard.cloneBoard(testBoard);
                    return false;
                }
                gameBoard.cloneBoard(testBoard);
            }

        } catch (IndexOutOfBoundsException e) {
        }
        try {
            if (gameBoard.getPieceAt(knight.getP1() - 1, knight.getP2() - 2).getColor() != playerC) {
                gameBoard.move(knight.getP1(), knight.getP2(), knight.getP1() - 1, knight.getP2() - 2);
                if(checkFuture() == false){
                    gameBoard.cloneBoard(testBoard);
                    return false;
                }
                gameBoard.cloneBoard(testBoard);
            }

        } catch (IndexOutOfBoundsException e) {
        }
        try {
            if (gameBoard.getPieceAt(knight.getP1() + 1, knight.getP2() - 2).getColor() != playerC) {
                gameBoard.move(knight.getP1(), knight.getP2(), knight.getP1() + 1, knight.getP2() - 2);
                if(checkFuture() == false){
                    gameBoard.cloneBoard(testBoard);
                    return false;
                }
                gameBoard.cloneBoard(testBoard);
            }

        } catch (IndexOutOfBoundsException e) {
        }
        try {
            if (gameBoard.getPieceAt(knight.getP1() + 2, knight.getP2() - 1).getColor() != playerC) {
                gameBoard.move(knight.getP1(), knight.getP2(), knight.getP1() + 2, knight.getP2() - 1);
                if(checkFuture() == false){
                    gameBoard.cloneBoard(testBoard);
                    return false;
                }
                gameBoard.cloneBoard(testBoard);
            }

        } catch (IndexOutOfBoundsException e) {
        }
        try {
            if (gameBoard.getPieceAt(knight.getP1() + 2, knight.getP2() + 1).getColor() != playerC) {
                gameBoard.move(knight.getP1(), knight.getP2(), knight.getP1() + 2, knight.getP2() + 1);
                if(checkFuture() == false){
                    gameBoard.cloneBoard(testBoard);
                    return false;
                }
                gameBoard.cloneBoard(testBoard);
            }

        } catch (IndexOutOfBoundsException e) {
        }
        try {
            if (gameBoard.getPieceAt(knight.getP1() - 2, knight.getP2() - 1).getColor() != playerC) {
                gameBoard.move(knight.getP1(), knight.getP2(), knight.getP1() - 2, knight.getP2() - 1);
                if(checkFuture() == false){
                    gameBoard.cloneBoard(testBoard);
                    return false;
                }
                gameBoard.cloneBoard(testBoard);
            }

        } catch (IndexOutOfBoundsException e) {
        }
        try {
            if (gameBoard.getPieceAt(knight.getP1() - 2, knight.getP2() + 1).getColor() != playerC) {
                gameBoard.move(knight.getP1(), knight.getP2(), knight.getP1() - 2, knight.getP2() + 1);
                if(checkFuture() == false){
                    gameBoard.cloneBoard(testBoard);
                    return false;
                }
                gameBoard.cloneBoard(testBoard);
            }

        } catch (IndexOutOfBoundsException e) {
        }
        return true;
    }

    //Bishop
    public boolean gameStateBishop(ChessPiece bishop){ //TODO
        testBoard.cloneBoard(gameBoard);
        boolean skip1 = false;
        boolean skip2 = false;
        boolean skip3 = false;
        boolean skip4 = false;

        Color playerC = Color.White;
        if(bishop.getColor() == Color.Black){
            playerC = Color.Black;
        }

        int y = bishop.getP1();
        int x = bishop.getP2();
        while (x < 7 && y < 7) {
            try {
                x++;
                y++;
                if (skip1 == false) {
                    //No check if own pieces
                    if (gameBoard.getPieceAt(y, x).getColor() == playerC) {
                        skip1 = true;
                    }
                    //If any enemy targets
                    else if (gameBoard.getPieceAt(y, x).getColor() != playerC) {
                        gameBoard.move(bishop.getP1(), bishop.getP2(), y, x);
                        if(checkFuture() == false){
                            gameBoard.cloneBoard(testBoard);
                            return false;
                        }
                        gameBoard.cloneBoard(testBoard);
                    }

                }

            }catch (IndexOutOfBoundsException e){
            }
        }
        y = bishop.getP1();
        x = bishop.getP2();
        while (x < 7 && y > 0) {
            try {
                x++;
                y--;
                //checking diagonals
                if (skip2 == false) {
                    //No check if own pieces
                    if (gameBoard.getPieceAt(y, x).getColor() == playerC) {
                        skip2 = true;
                    }
                    //If any enemy targets
                    else if (gameBoard.getPieceAt(y, x).getColor() != playerC) {
                        gameBoard.move(bishop.getP1(), bishop.getP2(), y, x);
                        if(checkFuture() == false){
                            gameBoard.cloneBoard(testBoard);
                            return false;
                        }
                        gameBoard.cloneBoard(testBoard);
                    }

                }
            }catch(IndexOutOfBoundsException e){
            }
        }
        y = bishop.getP1();
        x = bishop.getP2();
        while (x > 0 && y < 7) {
            try {
                x--;
                y++;
                //checking diagonals
                if (skip3 == false) {
                    //No check if own pieces
                    if (gameBoard.getPieceAt(y, x).getColor() == playerC) {
                        skip3 = true;
                    }
                    //If any enemy targets
                    else if (gameBoard.getPieceAt(y, x).getColor() != playerC) {
                        gameBoard.move(bishop.getP1(), bishop.getP2(), y, x);
                        if(checkFuture() == false){
                            gameBoard.cloneBoard(testBoard);
                            return false;
                        }
                        gameBoard.cloneBoard(testBoard);
                    }

                }
            }catch(IndexOutOfBoundsException e){
            }
        }
        y = bishop.getP1();
        x = bishop.getP2();
        while (x > 0 && y > 0) {
            try {
                x--;
                y--;
                //checking diagonals
                if (skip4 == false) {
                    //No check if own pieces
                    if (gameBoard.getPieceAt(y, x).getColor() == playerC) {
                        skip4 = true;
                    }
                    //If any enemy targets
                    else if (gameBoard.getPieceAt(y, x).getColor() != playerC) {
                        gameBoard.move(bishop.getP1(), bishop.getP2(), y, x);
                        if(checkFuture() == false){
                            gameBoard.cloneBoard(testBoard);
                            return false;
                        }
                        gameBoard.cloneBoard(testBoard);
                    }

                }
            }catch(IndexOutOfBoundsException e){
            }
        }
        return true;
    }

    //Rook
    public boolean gameStateRook(ChessPiece rook){ //TODO
        testBoard.cloneBoard(gameBoard);
        boolean skip1 = false;
        boolean skip2 = false;
        boolean skip3 = false;
        boolean skip4 = false;

        Color playerC = Color.White;
        if(rook.getColor() == Color.Black){
            playerC = Color.Black;
        }

        //checking horizontal
        int y = rook.getP1();
        int x = rook.getP2();
        while (x < 8) {
            try {
                x++;
                if (skip1 == false) {
                    //No check if own pieces
                    if (gameBoard.getPieceAt(y, x).getColor() == playerC) {
                        skip1 = true;
                    }
                    //If any enemy targets
                    else if (gameBoard.getPieceAt(y, x).getColor() != playerC) {
                        gameBoard.move(rook.getP1(), rook.getP2(), y, x);
                        if(checkFuture() == false){
                            gameBoard.cloneBoard(testBoard);
                            return false;
                        }
                        gameBoard.cloneBoard(testBoard);
                    }

                }
            }catch (IndexOutOfBoundsException e) {
            }
        }
        //checking vertical
        y = rook.getP1();
        x = rook.getP2();
        while (y < 8) {
            try {
                y++;
                if (skip2 == false) {
                    //No check if own pieces
                    if (gameBoard.getPieceAt(y, x).getColor() == playerC) {
                        skip2 = true;
                    }
                    //If any enemy targets
                    else if (gameBoard.getPieceAt(y, x).getColor() != playerC) {
                        gameBoard.move(rook.getP1(), rook.getP2(), y, x);
                        if(checkFuture() == false){
                            gameBoard.cloneBoard(testBoard);
                            return false;
                        }
                        gameBoard.cloneBoard(testBoard);
                    }

                }
            }catch (IndexOutOfBoundsException e) {
            }
        }
        //checking horizontal
        y = rook.getP1();
        x = rook.getP2();
        while (x > 0) {
            try {
                x--;
                if (skip3 == false) {
                    //No check if own pieces
                    if (gameBoard.getPieceAt(y, x).getColor() == playerC) {
                        skip3 = true;
                    }
                    //If any enemy targets
                    else if (gameBoard.getPieceAt(y, x).getColor() != playerC) {
                        gameBoard.move(rook.getP1(), rook.getP2(), y, x);
                        if(checkFuture() == false){
                            gameBoard.cloneBoard(testBoard);
                            return false;
                        }
                        gameBoard.cloneBoard(testBoard);
                    }

                }
            }catch (IndexOutOfBoundsException e) {
            }
        }
        //checking vertical
        y = rook.getP1();
        x = rook.getP2();
        while (y > 0) {
            try {
                y--;
                if (skip4 == false) {
                    //No check if own pieces
                    if (gameBoard.getPieceAt(y, x).getColor() == playerC) {
                        skip4 = true;
                    }
                    //If any enemy targets
                    else if (gameBoard.getPieceAt(y, x).getColor() != playerC) {
                        gameBoard.move(rook.getP1(), rook.getP2(), y, x);
                        if(checkFuture() == false){
                            gameBoard.cloneBoard(testBoard);
                            return false;
                        }
                        gameBoard.cloneBoard(testBoard);
                    }

                }
            }catch (IndexOutOfBoundsException e) {
            }
        }
        return true;
    }

    //Queen
    public boolean gameStateQueen(ChessPiece queen){ //TODO
        testBoard.cloneBoard(gameBoard);
        boolean skip1 = false;
        boolean skip2 = false;
        boolean skip3 = false;
        boolean skip4 = false;

        Color playerC = Color.White;
        if(queen.getColor() == Color.Black){
            playerC = Color.Black;
        }

        //checking horizontal
        int y = queen.getP1();
        int x = queen.getP2();
        while (x < 8) {
            try {
                x++;
                if (skip1 == false) {
                    //No check if own pieces
                    if (gameBoard.getPieceAt(y, x).getColor() == playerC) {
                        skip1 = true;
                    }
                    //If any enemy targets
                    else if (gameBoard.getPieceAt(y, x).getColor() != playerC) {
                        gameBoard.move(queen.getP1(), queen.getP2(), y, x);
                        if(checkFuture() == false){
                            gameBoard.cloneBoard(testBoard);
                            return false;
                        }
                        gameBoard.cloneBoard(testBoard);
                    }

                }
            }catch (IndexOutOfBoundsException e) {
            }
        }
        //checking vertical
        y = queen.getP1();
        x = queen.getP2();
        while (y < 8) {
            try {
                y++;
                if (skip2 == false) {
                    //No check if own pieces
                    if (gameBoard.getPieceAt(y, x).getColor() == playerC) {
                        skip2 = true;
                    }
                    //If any enemy targets
                    else if (gameBoard.getPieceAt(y, x).getColor() != playerC) {
                        gameBoard.move(queen.getP1(), queen.getP2(), y, x);
                        if(checkFuture() == false){
                            gameBoard.cloneBoard(testBoard);
                            return false;
                        }
                        gameBoard.cloneBoard(testBoard);
                    }

                }
            }catch (IndexOutOfBoundsException e) {
            }
        }
        //checking horizontal
        y = queen.getP1();
        x = queen.getP2();
        while (x > 0) {
            try {
                x--;
                if (skip3 == false) {
                    //No check if own pieces
                    if (gameBoard.getPieceAt(y, x).getColor() == playerC) {
                        skip3 = true;
                    }
                    //If any enemy targets
                    else if (gameBoard.getPieceAt(y, x).getColor() != playerC) {
                        gameBoard.move(queen.getP1(), queen.getP2(), y, x);
                        if(checkFuture() == false){
                            gameBoard.cloneBoard(testBoard);
                            return false;
                        }
                        gameBoard.cloneBoard(testBoard);
                    }

                }
            }catch (IndexOutOfBoundsException e) {
            }
        }
        //checking vertical
        y = queen.getP1();
        x = queen.getP2();
        while (y > 0) {
            try {
                y--;
                if (skip4 == false) {
                    //No check if own pieces
                    if (gameBoard.getPieceAt(y, x).getColor() == playerC) {
                        skip4 = true;
                    }
                    //If any enemy targets
                    else if (gameBoard.getPieceAt(y, x).getColor() != playerC) {
                        gameBoard.move(queen.getP1(), queen.getP2(), y, x);
                        if(checkFuture() == false){
                            gameBoard.cloneBoard(testBoard);
                            return false;
                        }
                        gameBoard.cloneBoard(testBoard);
                    }

                }
            }catch (IndexOutOfBoundsException e) {
            }
        }

        skip1 = false;
        skip2 = false;
        skip3 = false;
        skip4 = false;

        playerC = Color.White;
        if(queen.getColor() == Color.Black){
            playerC = Color.Black;
        }

        y = queen.getP1();
        x = queen.getP2();
        while (x < 7 && y < 7) {
            try {
                x++;
                y++;
                if (skip1 == false) {
                    //No check if own pieces
                    if (gameBoard.getPieceAt(y, x).getColor() == playerC) {
                        skip1 = true;
                    }
                    //If any enemy targets
                    else if (gameBoard.getPieceAt(y, x).getColor() != playerC) {
                        gameBoard.move(queen.getP1(), queen.getP2(), y, x);
                        if(checkFuture() == false){
                            gameBoard.cloneBoard(testBoard);
                            return false;
                        }
                        gameBoard.cloneBoard(testBoard);
                    }

                }

            }catch (IndexOutOfBoundsException e){
            }
        }
        y = queen.getP1();
        x = queen.getP2();
        while (x < 7 && y > 0) {
            try {
                x++;
                y--;
                //checking diagonals
                if (skip2 == false) {
                    //No check if own pieces
                    if (gameBoard.getPieceAt(y, x).getColor() == playerC) {
                        skip2 = true;
                    }
                    //If any enemy targets
                    else if (gameBoard.getPieceAt(y, x).getColor() != playerC) {
                        gameBoard.move(queen.getP1(), queen.getP2(), y, x);
                        if(checkFuture() == false){
                            gameBoard.cloneBoard(testBoard);
                            return false;
                        }
                        gameBoard.cloneBoard(testBoard);
                    }

                }
            }catch(IndexOutOfBoundsException e){
            }
        }
        y = queen.getP1();
        x = queen.getP2();
        while (x > 0 && y < 7) {
            try {
                x--;
                y++;
                //checking diagonals
                if (skip3 == false) {
                    //No check if own pieces
                    if (gameBoard.getPieceAt(y, x).getColor() == playerC) {
                        skip3 = true;
                    }
                    //If any enemy targets
                    else if (gameBoard.getPieceAt(y, x).getColor() != playerC) {
                        gameBoard.move(queen.getP1(), queen.getP2(), y, x);
                        if(checkFuture() == false){
                            gameBoard.cloneBoard(testBoard);
                            return false;
                        }
                        gameBoard.cloneBoard(testBoard);
                    }

                }
            }catch(IndexOutOfBoundsException e){
            }
        }
        y = queen.getP1();
        x = queen.getP2();
        while (x > 0 && y > 0) {
            try {
                x--;
                y--;
                //checking diagonals
                if (skip4 == false) {
                    //No check if own pieces
                    if (gameBoard.getPieceAt(y, x).getColor() == playerC) {
                        skip4 = true;
                    }
                    //If any enemy targets
                    else if (gameBoard.getPieceAt(y, x).getColor() != playerC) {
                        gameBoard.move(queen.getP1(), queen.getP2(), y, x);
                        if(checkFuture() == false){
                            gameBoard.cloneBoard(testBoard);
                            return false;
                        }
                        gameBoard.cloneBoard(testBoard);
                    }

                }
            }catch(IndexOutOfBoundsException e){
            }
        }
        return true;
    }

    //King
    public boolean gameStateKing(ChessPiece king){ //TODO
        testBoard.cloneBoard(gameBoard);
        Color playerC = Color.White;
        if(king.getColor() == Color.Black){
            playerC = Color.Black;
        }
        try {
            if (gameBoard.getPieceAt(king.getP1(), king.getP2() - 1).getColor() != playerC) {
                gameBoard.move(king.getP1(), king.getP2(), king.getP1(), king.getP2() - 1);
                if(checkFuture() == false){
                    gameBoard.cloneBoard(testBoard);
                    return false;
                }
                gameBoard.cloneBoard(testBoard);
            }

        } catch (IndexOutOfBoundsException e) {
        }
        try {
            if (gameBoard.getPieceAt(king.getP1(), king.getP2() + 1).getColor() != playerC) {
                gameBoard.move(king.getP1(), king.getP2(), king.getP1(), king.getP2() + 1);
                if(checkFuture() == false){
                    gameBoard.cloneBoard(testBoard);
                    return false;
                }
                gameBoard.cloneBoard(testBoard);
            }

        } catch (IndexOutOfBoundsException e) {
        }
        try {
            if (gameBoard.getPieceAt(king.getP1() - 1, king.getP2()).getColor() != playerC) {
                gameBoard.move(king.getP1(), king.getP2(), king.getP1() - 1, king.getP2());
                if(checkFuture() == false){
                    gameBoard.cloneBoard(testBoard);
                    return false;
                }
                gameBoard.cloneBoard(testBoard);
            }

        } catch (IndexOutOfBoundsException e) {
        }
        try {
            if (gameBoard.getPieceAt(king.getP1() - 1, king.getP2() - 1).getColor() != playerC) {
                gameBoard.move(king.getP1(), king.getP2(), king.getP1() - 1, king.getP2() - 1);
                if(checkFuture() == false){
                    gameBoard.cloneBoard(testBoard);
                    return false;
                }
                gameBoard.cloneBoard(testBoard);
            }

        } catch (IndexOutOfBoundsException e) {
        }
        try {
            if (gameBoard.getPieceAt(king.getP1() - 1, king.getP2() + 1).getColor() != playerC) {
                gameBoard.move(king.getP1(), king.getP2(), king.getP1() - 1, king.getP2() + 1);
                if(checkFuture() == false){
                    gameBoard.cloneBoard(testBoard);
                    return false;
                }
                gameBoard.cloneBoard(testBoard);
            }

        } catch (IndexOutOfBoundsException e) {
        }
        try {
            if (gameBoard.getPieceAt(king.getP1() + 1, king.getP2()).getColor() != playerC) {
                gameBoard.move(king.getP1(), king.getP2(), king.getP1() + 1, king.getP2());
                if(checkFuture() == false){
                    gameBoard.cloneBoard(testBoard);
                    return false;
                }
                gameBoard.cloneBoard(testBoard);
            }

        } catch (IndexOutOfBoundsException e) {
        }
        try {
            if (gameBoard.getPieceAt(king.getP1() + 1, king.getP2() - 1).getColor() != playerC) {
                gameBoard.move(king.getP1(), king.getP2(), king.getP1() + 1, king.getP2() - 1);
                if(checkFuture() == false){
                    gameBoard.cloneBoard(testBoard);
                    return false;
                }
                gameBoard.cloneBoard(testBoard);
            }

        } catch (IndexOutOfBoundsException e) {
        }
        try {
            if (gameBoard.getPieceAt(king.getP1() + 1, king.getP2() + 1).getColor() != playerC) {
                gameBoard.move(king.getP1(), king.getP2(), king.getP1() + 1, king.getP2() + 1);
                if(checkFuture() == false){
                    gameBoard.cloneBoard(testBoard);
                    return false;
                }
                gameBoard.cloneBoard(testBoard);
            }

        } catch (IndexOutOfBoundsException e) {
        }
        return true;
    }


    private void resetCatsle(){
        castled = false;
        queenSideCastled =false;
    }
    public boolean getQueencat(){
        boolean temp = queenSideCastled;
        resetCatsle();
        return temp;
    }
}
