package cs480.bolide.chess;


import static android.icu.lang.UProperty.MATH;

public class GameEngine {
    private Color turn = Color.White;

    ArrayBoard gameBoard;
    private boolean promotable = false;

    public GameEngine() {
        gameBoard = new ArrayBoard();
    }

    public boolean turn(int x1, int y1, int x2, int y2) {
        promotable = false;
        boolean validturn = validMove(x1, y1, x2, y2);
        //Testing   TODO
        //These have to be reversed to white(1) black(0)
        int playerC = 0;
        if (getTurn() == Color.White) {
            playerC = 0;
        } else{
            playerC = 1;
        }

        boolean unsafeMove = isCheck(playerC);
        System.out.println("Unsafe Move:" + unsafeMove);
        //
        if (validturn == false) {
            return false;
        }
        gameBoard.getPieceAt(y1, x1).setMoved(true);
        gameBoard.move(x1, y1, x2, y2);
        checkForPromotion(x2,y2);
        return true;
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
            if(y2 == 7 && x2 == 0){
                if((gameBoard.getPieceAt(y2-1,x2).getType()==Type.King&& gameBoard.getPieceAt(y2-1,x2).getColor() != turn)||
                        (gameBoard.getPieceAt(y2-1,x2+1).getType()==Type.King&& gameBoard.getPieceAt(y2-1,x2+1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2,x2+1).getType()==Type.King&& gameBoard.getPieceAt(y2,x2+1).getColor() != turn)){
                    return false;
                }
            }
            if(y2 == 0 && x2 == 0){
                if((gameBoard.getPieceAt(y2+1,x2).getType()==Type.King&& gameBoard.getPieceAt(y2+1,x2).getColor() != turn)||
                        (gameBoard.getPieceAt(y2+1,x2+1).getType()==Type.King&& gameBoard.getPieceAt(y2+1,x2+1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2,x2+1).getType()==Type.King&& gameBoard.getPieceAt(y2,x2+1).getColor() != turn)){
                    return false;
                }

            }
            if(y2 == 0 && x2 == 7){
                if((gameBoard.getPieceAt(y2,x2-1).getType()==Type.King&& gameBoard.getPieceAt(y2,x2-1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2-1,x2-1).getType()==Type.King&& gameBoard.getPieceAt(y2-1,x2-1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2-1,x2).getType()==Type.King&& gameBoard.getPieceAt(y2-1,x2).getColor() != turn)){
                    return false;
                }
            }
            if(y2 == 7 && x2 == 7){
                if((gameBoard.getPieceAt(y2-1,x2).getType()==Type.King&& gameBoard.getPieceAt(y2-1,x2).getColor() != turn)||
                        (gameBoard.getPieceAt(y2-1,x2-1).getType()==Type.King&& gameBoard.getPieceAt(y2-1,x2-1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2,x2-1).getType()==Type.King&& gameBoard.getPieceAt(y2,x2-1).getColor() != turn)){
                    return false;
                }
            }
            if(y2 == 7){
                if((gameBoard.getPieceAt(y2,x2-1).getType()==Type.King&& gameBoard.getPieceAt(y2,x2-1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2-1,x2-1).getType()==Type.King&& gameBoard.getPieceAt(y2-1,x2-1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2-1,x2).getType()==Type.King&& gameBoard.getPieceAt(y2-1,x2).getColor() != turn)||
                        (gameBoard.getPieceAt(y2-1,x2+1).getType()==Type.King&& gameBoard.getPieceAt(y2-1,x2+1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2,x2+1).getType()==Type.King&& gameBoard.getPieceAt(y2,x2+1).getColor() != turn)){
                    return false;
                }
            }
            if(y2 ==0){
                if((gameBoard.getPieceAt(y2,x2-1).getType()==Type.King&& gameBoard.getPieceAt(y2,x2-1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2+1,x2-1).getType()==Type.King&& gameBoard.getPieceAt(y2+1,x2-1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2+1,x2).getType()==Type.King&& gameBoard.getPieceAt(y2+1,x2).getColor() != turn)||
                        (gameBoard.getPieceAt(y2+1,x2+1).getType()==Type.King&& gameBoard.getPieceAt(y2+1,x2+1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2,x2+1).getType()==Type.King&& gameBoard.getPieceAt(y2,x2+1).getColor() != turn)){
                    return false;
                }

            }
            if(x2 ==0){
                if((gameBoard.getPieceAt(y2+1,x2).getType()==Type.King&& gameBoard.getPieceAt(y2+1,x2).getColor() != turn)||
                        (gameBoard.getPieceAt(y2+1,x2+1).getType()==Type.King&& gameBoard.getPieceAt(y2+1,x2+1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2,x2+1).getType()==Type.King&& gameBoard.getPieceAt(y2,x2+1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2-1,x2+1).getType()==Type.King&& gameBoard.getPieceAt(y2-1,x2+1).getColor() != turn)||
                        (gameBoard.getPieceAt(y2+1,x2).getType()==Type.King&& gameBoard.getPieceAt(y2+1,x2).getColor() != turn)){
                    return false;
                }

            }
            if(x2 ==7){
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
            System.out.println("Enemy Pawn Location 1 " +"x:"+ (king.getP2() - 1)+ "y:"+ (king.getP1() + vertical));
            if (gameBoard.getPieceAt(king.getP1() + vertical, king.getP2() - 1).getType() == Type.Pawn && gameBoard.getPieceAt(king.getP1() + vertical, king.getP2() - 1).getColor() == enemyC) {
                return true;
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("out of bounds Pawn 1");
        }
        try {
            System.out.println("Enemy Pawn Location 2 " +"x:"+ (king.getP2() + 1)+ "y:"+ (king.getP1() + vertical));
            if (gameBoard.getPieceAt(king.getP1() + vertical, king.getP2() + 1).getType() == Type.Pawn && gameBoard.getPieceAt(king.getP1() + vertical, king.getP2() + 1).getColor() == enemyC) {
                return true;
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("out of bounds Pawn 2");
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
            if (gameBoard.getPieceAt(king.getP1() - 1, king.getP2() + 2).getType() == Type.Knight && gameBoard.getPieceAt(king.getP1() - 1, king.getP2() + 2).getColor() == enemyC) {
                return true;
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("out of bounds");
        }
        try {
            if (gameBoard.getPieceAt(king.getP1() + 1, king.getP2() + 2).getType() == Type.Knight && gameBoard.getPieceAt(king.getP1() + 1, king.getP2() + 2).getColor() == enemyC) {
                return true;
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("out of bounds");
        }
        try {
            if (gameBoard.getPieceAt(king.getP1() - 1, king.getP2() - 2).getType() == Type.Knight && gameBoard.getPieceAt(king.getP1() - 1, king.getP2() - 2).getColor() == enemyC) {
                return true;
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("out of bounds");
        }
        try {
            if (gameBoard.getPieceAt(king.getP1() + 1, king.getP2() - 2).getType() == Type.Knight && gameBoard.getPieceAt(king.getP1() + 1, king.getP2() - 2).getColor() == enemyC) {
                return true;
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("out of bounds");
        }
        try {
            if (gameBoard.getPieceAt(king.getP1() + 2, king.getP2() - 1).getType() == Type.Knight && gameBoard.getPieceAt(king.getP1() + 2, king.getP2() - 1).getColor() == enemyC) {
                return true;
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("out of bounds");
        }
        try {
            if (gameBoard.getPieceAt(king.getP1() + 2, king.getP2() + 1).getType() == Type.Knight && gameBoard.getPieceAt(king.getP1() + 2, king.getP2() + 1).getColor() == enemyC) {
                return true;
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("out of bounds");
        }
        try {
            if (gameBoard.getPieceAt(king.getP1() - 2, king.getP2() - 1).getType() == Type.Knight && gameBoard.getPieceAt(king.getP1() - 2, king.getP2() - 1).getColor() == enemyC) {
                return true;
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("out of bounds");
        }
        try {
            if (gameBoard.getPieceAt(king.getP1() - 2, king.getP2() + 1).getType() == Type.Knight && gameBoard.getPieceAt(king.getP1() - 2, king.getP2() + 1).getColor() == enemyC) {
                return true;
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("out of bounds");
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

        for (int y = king.getP1(); y < 8; y++) {
            for (int x = king.getP2(); x < 8; x++) {
                //checking diagonals
                    if (x == y && skip1 == false) {
                        //No check if own pieces
                        if (gameBoard.getPieceAt(y, x).getColor() == playerC) {
                            //Not Check
                            //skip1 = true;
                        }
                        //If other pieces other than bishop appear, no check
                        else if (gameBoard.getPieceAt(y, x).getColor() == enemyC) {
                            if (gameBoard.getPieceAt(y, x).getType() == Type.Knight ||
                                    gameBoard.getPieceAt(y, x).getType() == Type.Rook ||
                                    gameBoard.getPieceAt(y, x).getType() == Type.Pawn ||
                                    gameBoard.getPieceAt(y, x).getType() == Type.King) {
                                //Not Check
                                //skip1 = true;
                            }
                            //if bishop uninterrupted, check
                            if (gameBoard.getPieceAt(y, x).getType() == Type.Bishop ||
                                    gameBoard.getPieceAt(y, x).getType() == Type.Queen) {
                                //Check
                                System.out.println("Bishop 1 hit");
                                return true;
                            }
                        }
                    }
                }
            }
        for (int y = 7; y > king.getP1(); y--) {
            for (int x = king.getP2(); x < 8; x++) {
                //checking diagonals
                    if (x == y && skip2 == false) {
                        //No check if own pieces
                        if (gameBoard.getPieceAt(y, x).getColor() == playerC) {
                            //Not Check
                            //skip2 = true;
                        }
                        //If other pieces other than bishop appear, no check
                        else if (gameBoard.getPieceAt(y, x).getColor() == enemyC) {
                            if (gameBoard.getPieceAt(y, x).getType() == Type.Knight ||
                                    gameBoard.getPieceAt(y, x).getType() == Type.Rook ||
                                    gameBoard.getPieceAt(y, x).getType() == Type.Pawn ||
                                    gameBoard.getPieceAt(y, x).getType() == Type.King) {
                                //Not Check
                                //skip2 = true;
                            }
                            //if bishop uninterrupted, check
                            if (gameBoard.getPieceAt(y, x).getType() == Type.Bishop ||
                                    gameBoard.getPieceAt(y, x).getType() == Type.Queen) {
                                //Check
                                System.out.println("Bishop 1 hit");
                                return true;
                            }
                        }
                    }
                }
            }
        for (int y = king.getP1(); y < 8; y++) {
            for (int x = 7; x > king.getP2(); x--) {
                //checking diagonals
                    if (x == y && skip3 == false) {
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
                                //skip3 = true;
                            }
                            //if bishop uninterrupted, check
                            if (gameBoard.getPieceAt(y, x).getType() == Type.Bishop ||
                                    gameBoard.getPieceAt(y, x).getType() == Type.Queen) {
                                //Check
                                System.out.println("Bishop 1 hit");
                                return true;
                            }
                        }
                    }
                }
            }
        for (int y = 7; y > king.getP1(); y--) {
            for (int x = 7; x > king.getP2(); x--) {
                //checking diagonals
                    if (x == y && skip4 == false) {
                        //No check if own pieces
                        if (gameBoard.getPieceAt(y, x).getColor() == playerC) {
                            //Not Check
                            //skip4 = true;
                        }
                        //If other pieces other than bishop appear, no check
                        else if (gameBoard.getPieceAt(y, x).getColor() == enemyC) {
                            if (gameBoard.getPieceAt(y, x).getType() == Type.Knight ||
                                    gameBoard.getPieceAt(y, x).getType() == Type.Rook ||
                                    gameBoard.getPieceAt(y, x).getType() == Type.Pawn ||
                                    gameBoard.getPieceAt(y, x).getType() == Type.King) {
                                //Not Check
                                //skip4 = true;
                            }
                            //if bishop uninterrupted, check
                            if (gameBoard.getPieceAt(y, x).getType() == Type.Bishop ||
                                    gameBoard.getPieceAt(y, x).getType() == Type.Queen) {
                                //Check
                                System.out.println("Bishop 1 hit");
                                return true;
                            }
                        }
                    }
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
            for (int x = king.getP2(); x < 8; x++) {
                try {
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
                        return false;
                    }
                }
            }catch (IndexOutOfBoundsException e) {
                    System.out.println("out of bounds");
                }
        }


            //checking horizontal
        int x = king.getP2();
        for (y = king.getP1(); y < 8; y++) {
            try {
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
                    System.out.println("out of bounds");
                }
        }

            //checking horizontal
        y = king.getP1();
        for (x = 7; x > king.getP2(); x--) {
            try {
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
                    System.out.println("out of bounds");
                }
        }


            //checking horizontal
        x = king.getP2();
        for (y = 7; y > king.getP1(); y--) {
            try {
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
                    System.out.println("out of bounds");
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
            if (gameBoard.getPieceAt(king.getP2(), king.getP1() - 1).getType() == Type.King && gameBoard.getPieceAt(king.getP2(), king.getP1() - 1).getColor() == enemyC) {
                return true;
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("out of bounds");
        }
        try {
            if (gameBoard.getPieceAt(king.getP2(), king.getP1() + 1).getType() == Type.King && gameBoard.getPieceAt(king.getP2(), king.getP1() + 1).getColor() == enemyC) {
                return true;
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("out of bounds");
        }
        try {
            if (gameBoard.getPieceAt(king.getP2() - 1, king.getP1()).getType() == Type.King && gameBoard.getPieceAt(king.getP2() - 1, king.getP1()).getColor() == enemyC) {
                return true;
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("out of bounds");
        }
        try {
            if (gameBoard.getPieceAt(king.getP2() - 1, king.getP1() - 1).getType() == Type.King && gameBoard.getPieceAt(king.getP2() - 1, king.getP1() - 1).getColor() == enemyC) {
                return true;
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("out of bounds");
        }
        try {
            if (gameBoard.getPieceAt(king.getP2() - 1, king.getP1() + 1).getType() == Type.King && gameBoard.getPieceAt(king.getP2() - 1, king.getP1() + 1).getColor() == enemyC) {
                return true;
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("out of bounds");
        }
        try {
            if (gameBoard.getPieceAt(king.getP2() + 1, king.getP1()).getType() == Type.King && gameBoard.getPieceAt(king.getP2() + 1, king.getP1()).getColor() == enemyC) {
                return true;
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("out of bounds");
        }
        try {
            if (gameBoard.getPieceAt(king.getP2() + 1, king.getP1() - 1).getType() == Type.King && gameBoard.getPieceAt(king.getP2() + 1, king.getP1() - 1).getColor() == enemyC) {
                return true;
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("out of bounds");
        }
        try {
            if (gameBoard.getPieceAt(king.getP2() + 1, king.getP1() + 1).getType() == Type.King && gameBoard.getPieceAt(king.getP2() + 1, king.getP1() + 1).getColor() == enemyC) {
                return true;
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("out of bounds");
        }
        return false;
    }

}
