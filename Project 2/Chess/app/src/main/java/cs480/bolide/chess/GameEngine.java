package cs480.bolide.chess;


import static android.icu.lang.UProperty.MATH;

public class GameEngine{
    private Color turn = Color.White;

    ArrayBoard gameBoard;
    public GameEngine(){
        gameBoard = new ArrayBoard();
    }
    public boolean turn(int x1,int y1,int x2,int y2){
        boolean validturn = validMove(x1,y1,x2,y2);
        if(validturn == false){
            return false;
        }
        gameBoard.getPieceAt(y1,x1).setMoved(true);
        gameBoard.move(x1,y1,x2,y2);
        return true;
    }
    public boolean validMove(int x1,int y1,int x2,int y2) {

        ChessPiece tempPiece1 = gameBoard.getPieceAt(y1, x1);
        ChessPiece tempPiece2 = gameBoard.getPieceAt(y2, x2);
        if(tempPiece1.getColor()!= turn){
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
                    nextTurn();
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
                nextTurn();
                return true;
            }
        }
        //logic for the move for knights
        if (tempPiece1.getType() == Type.Knight) {
            int xPath = Math.abs(x2 - x1);
            int yPath = Math.abs(y2 - y1);
            if (xPath <= 2 && yPath <= 2) {
                if (xPath == 2 && yPath == 1 || xPath == 1 && yPath == 2) {
                    nextTurn();
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
                    nextTurn();
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
                nextTurn();
                return true;
            }
        }
        //logic for the move of the queen
        if(tempPiece1.getType() == Type.Queen){
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
                    nextTurn();
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
                    nextTurn();
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
                nextTurn();
                return true;
            }
        }
        //logic for moving the king (needed)
        return false;
    }
    public boolean checkForEmpty(int x, int y){
        if( gameBoard.getPieceAt(y,x).getType() == Type.Empty_Space){
            return true;
        }return false;
    }
    private void nextTurn(){
        if(turn == Color.White){
            turn = Color.Black;
        }
        else{
            turn = Color.White;
        }
    }
    public Color getTurn(){
        return turn;
    }
}
