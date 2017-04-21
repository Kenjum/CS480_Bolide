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

    public boolean checkForEmpty(int x, int y) {
        if (gameBoard.getPieceAt(y, x).getType() == Type.Empty_Space) {
            return true;
        }
        return false;
    }

    private void nextTurn() {
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
    public ChessPiece findKing(int blackWhite) {
        //0 is white, 1 is black
        int color = blackWhite;
        ChessPiece temp = gameBoard.getPieceAt(0, 0);

        for (int y = 0; y < 8; y++) {      //y
            for (int x = 0; x < 8; x++) {  //x
                if (gameBoard.getPieceAt(y, x).getType() == Type.King) {
                    //getting white king
                    if (color == 0) {
                        if (gameBoard.getPieceAt(y, x).getColor() == Color.White) {
                            return gameBoard.getPieceAt(y, x);

                        }
                    }
                    //getting black king
                    else if (color == 1) {

                        if (gameBoard.getPieceAt(y, x).getColor() == Color.Black) {
                            return gameBoard.getPieceAt(y, x);
                        }
                    }
                }
            }
        }
        //get king location
        return temp;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    public boolean isCheck(int blackWhite) {
        //0 is white, 1 is black
        //for first king then second
        ChessPiece king = findKing(blackWhite);
        //now we want to check what can hit it from the king's perspective.
        if (blackWhite == 0) {
            //check for Pawns///////////////////////////////////////////////////////////////////
            try {
                if (gameBoard.getPieceAt(king.getP2() - 1, king.getP1() - 1).getType() == Type.Pawn) {
                    if (gameBoard.getPieceAt(king.getP2() - 1, king.getP1() - 1).getColor() == Color.Black) {
                        return true;
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("out of bounds");
            }
            try {
                if (gameBoard.getPieceAt(king.getP2() + 1, king.getP1() - 1).getType() == Type.Pawn) {
                    if (gameBoard.getPieceAt(king.getP2() + 1, king.getP1() - 1).getColor() == Color.Black) {
                        return true;
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("out of bounds");
            }
            //check for Knights/////////////////////////////////////////////////////////////////
            try {
                if (gameBoard.getPieceAt(king.getP2() + 2, king.getP1() - 1).getType() == Type.Knight) {
                    if (gameBoard.getPieceAt(king.getP2() + 2, king.getP1() - 1).getColor() == Color.Black) {
                        return true;
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("out of bounds");
            }
            try {
                if (gameBoard.getPieceAt(king.getP2() + 2, king.getP1() + 1).getType() == Type.Knight) {
                    if (gameBoard.getPieceAt(king.getP2() + 2, king.getP1() + 1).getColor() == Color.Black) {
                        return true;
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("out of bounds");
            }
            try {
                if (gameBoard.getPieceAt(king.getP2() - 2, king.getP1() - 1).getType() == Type.Knight) {
                    if (gameBoard.getPieceAt(king.getP2() - 2, king.getP1() - 1).getColor() == Color.Black) {
                        return true;
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("out of bounds");
            }
            try {
                if (gameBoard.getPieceAt(king.getP2() - 2, king.getP1() + 1).getType() == Type.Knight) {
                    if (gameBoard.getPieceAt(king.getP2() - 2, king.getP1() + 1).getColor() == Color.Black) {
                        return true;
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("out of bounds");
            }
            try {
                if (gameBoard.getPieceAt(king.getP2() - 1, king.getP1() + 2).getType() == Type.Knight) {
                    if (gameBoard.getPieceAt(king.getP2() - 1, king.getP1() + 2).getColor() == Color.Black) {
                        return true;
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("out of bounds");
            }
            try {
                if (gameBoard.getPieceAt(king.getP2() + 1, king.getP1() + 2).getType() == Type.Knight) {
                    if (gameBoard.getPieceAt(king.getP2() + 1, king.getP1() + 2).getColor() == Color.Black) {
                        return true;
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("out of bounds");
            }
            try {
                if (gameBoard.getPieceAt(king.getP2() - 1, king.getP1() - 2).getType() == Type.Knight) {
                    if (gameBoard.getPieceAt(king.getP2() - 1, king.getP1() - 2).getColor() == Color.Black) {
                        return true;
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("out of bounds");
            }
            try {
                if (gameBoard.getPieceAt(king.getP2() + 1, king.getP1() - 2).getType() == Type.Knight) {
                    if (gameBoard.getPieceAt(king.getP2() + 1, king.getP1() - 2).getColor() == Color.Black) {
                        return true;
                    }
                }

            } catch (IndexOutOfBoundsException e) {
                System.out.println("out of bounds");
            }
            //check for Bishops & Queen/////////////////////////////////////////////////////////
            try {
                for (int y = king.getP1(); y < 8; y++) {
                    for (int x = king.getP2(); x < 8; x++) {
                        //checking diagonals
                        if (x == y) {
                            //No check if own pieces
                            if (gameBoard.getPieceAt(y, x).getColor() == Color.White) {
                                //Not Check
                                return false;
                            }
                            //If other pieces other than bishop appear, no check
                            else if (gameBoard.getPieceAt(y, x).getColor() == Color.Black) {
                                if (gameBoard.getPieceAt(y, x).getType() == Type.Knight ||
                                        gameBoard.getPieceAt(y, x).getType() == Type.Rook ||
                                        gameBoard.getPieceAt(y, x).getType() == Type.Pawn ||
                                        gameBoard.getPieceAt(y, x).getType() == Type.King) {
                                    //Not Check
                                    return false;
                                }
                                //if bishop uninterrupted, check
                                else if (gameBoard.getPieceAt(y, x).getType() == Type.Bishop ||
                                        gameBoard.getPieceAt(y, x).getType() == Type.Queen) {
                                    //Check
                                    return true;
                                }
                            }
                        }
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("out of bounds");
            }

            try {
                for (int y = king.getP1(); y > 8; y--) {
                    for (int x = king.getP2(); x < 8; x++) {
                        //checking diagonals
                        if (x == y) {
                            //No check if own pieces
                            if (gameBoard.getPieceAt(y, x).getColor() == Color.White) {
                                //Not Check
                                return false;
                            }
                            //If other pieces other than bishop appear, no check
                            else if (gameBoard.getPieceAt(y, x).getColor() == Color.Black) {
                                if (gameBoard.getPieceAt(y, x).getType() == Type.Knight ||
                                        gameBoard.getPieceAt(y, x).getType() == Type.Rook ||
                                        gameBoard.getPieceAt(y, x).getType() == Type.Pawn ||
                                        gameBoard.getPieceAt(y, x).getType() == Type.King) {
                                    //Not Check
                                    return false;
                                }
                                //if bishop uninterrupted, check
                                else if (gameBoard.getPieceAt(y, x).getType() == Type.Bishop ||
                                        gameBoard.getPieceAt(y, x).getType() == Type.Queen) {
                                    //Check
                                    return true;
                                }
                            }
                        }
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("out of bounds");
            }

            try {
                for (int y = king.getP1(); y < 8; y++) {
                    for (int x = king.getP2(); x > 8; x--) {
                        //checking diagonals
                        if (x == y) {
                            //No check if own pieces
                            if (gameBoard.getPieceAt(y, x).getColor() == Color.White) {
                                //Not Check
                                return false;
                            }
                            //If other pieces other than bishop appear, no check
                            else if (gameBoard.getPieceAt(y, x).getColor() == Color.Black) {
                                if (gameBoard.getPieceAt(y, x).getType() == Type.Knight ||
                                        gameBoard.getPieceAt(y, x).getType() == Type.Rook ||
                                        gameBoard.getPieceAt(y, x).getType() == Type.Pawn ||
                                        gameBoard.getPieceAt(y, x).getType() == Type.King) {
                                    //Not Check
                                    return false;
                                }
                                //if bishop uninterrupted, check
                                else if (gameBoard.getPieceAt(y, x).getType() == Type.Bishop ||
                                        gameBoard.getPieceAt(y, x).getType() == Type.Queen) {
                                    //Check
                                    return true;
                                }
                            }
                        }
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("out of bounds");
            }

            try {
                for (int y = king.getP1(); y > 8; y--) {
                    for (int x = king.getP2(); x > 8; x--) {
                        //checking diagonals
                        if (x == y) {
                            //No check if own pieces
                            if (gameBoard.getPieceAt(y, x).getColor() == Color.White) {
                                //Not Check
                                return false;
                            }
                            //If other pieces other than bishop appear, no check
                            else if (gameBoard.getPieceAt(y, x).getColor() == Color.Black) {
                                if (gameBoard.getPieceAt(y, x).getType() == Type.Knight ||
                                        gameBoard.getPieceAt(y, x).getType() == Type.Rook ||
                                        gameBoard.getPieceAt(y, x).getType() == Type.Pawn ||
                                        gameBoard.getPieceAt(y, x).getType() == Type.King) {
                                    //Not Check
                                    return false;
                                }
                                //if bishop uninterrupted, check
                                else if (gameBoard.getPieceAt(y, x).getType() == Type.Bishop ||
                                        gameBoard.getPieceAt(y, x).getType() == Type.Queen) {
                                    //Check
                                    return true;
                                }
                            }
                        }
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("out of bounds");
            }
            //check for Rooks & Queen///////////////////////////////////////////////////////////
            try {
                //checking horizontal
                int y = gameBoard.getPieceAt(king.getP2(), king.getP1()).getP1();
                for (int x = 0; x < 8; x++) {
                    if (gameBoard.getPieceAt(y, x).getColor() == Color.White) {
                        //Not Check
                        return false;
                    }
                    //If other pieces other than bishop appear, no check
                    else if (gameBoard.getPieceAt(y, x).getColor() == Color.Black) {
                        if (gameBoard.getPieceAt(y, x).getType() == Type.Knight ||
                                gameBoard.getPieceAt(y, x).getType() == Type.Bishop ||
                                gameBoard.getPieceAt(y, x).getType() == Type.Pawn ||
                                gameBoard.getPieceAt(y, x).getType() == Type.King) {
                            //Not Check
                            return false;
                        }
                        //if Rook uninterrupted, check
                        else if (gameBoard.getPieceAt(y, x).getType() == Type.Rook ||
                                gameBoard.getPieceAt(y, x).getType() == Type.Queen) {
                            //Check
                            return true;
                        }
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("out of bounds");
            }

            try {
                //checking horizontal
                int x = gameBoard.getPieceAt(king.getP2(), king.getP1()).getP2();
                for (int y = 0; y < 8; y++) {
                    if (gameBoard.getPieceAt(y, x).getColor() == Color.White) {
                        //Not Check
                        return false;
                    }
                    //If other pieces other than bishop appear, no check
                    else if (gameBoard.getPieceAt(y, x).getColor() == Color.Black) {
                        if (gameBoard.getPieceAt(y, x).getType() == Type.Knight ||
                                gameBoard.getPieceAt(y, x).getType() == Type.Bishop ||
                                gameBoard.getPieceAt(y, x).getType() == Type.Pawn ||
                                gameBoard.getPieceAt(y, x).getType() == Type.King) {
                            //Not Check
                            return false;
                        }
                        //if Rook uninterrupted, check
                        else if (gameBoard.getPieceAt(y, x).getType() == Type.Rook ||
                                gameBoard.getPieceAt(y, x).getType() == Type.Queen) {
                            //Check
                            return true;
                        }
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("out of bounds");
            }
            //check for Kings///////////////////////////////////////////////////////////////////
            //Will be using this check to see if some moves are safe. This will prevent
            //Other King from walking into striking distance.
            try {
                if (gameBoard.getPieceAt(king.getP2(), king.getP1()).getType() == Type.King) {
                    if (gameBoard.getPieceAt(king.getP2(), king.getP1()).getColor() == Color.Black) {
                        return true;
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("out of bounds");
            }
            try {
                if (gameBoard.getPieceAt(king.getP2(), king.getP1() - 1).getType() == Type.King) {
                    if (gameBoard.getPieceAt(king.getP2(), king.getP1() - 1).getColor() == Color.Black) {
                        return true;
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("out of bounds");
            }
            try {
                if (gameBoard.getPieceAt(king.getP2(), king.getP1() + 1).getType() == Type.King) {
                    if (gameBoard.getPieceAt(king.getP2(), king.getP1() + 1).getColor() == Color.Black) {
                        return true;
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("out of bounds");
            }
            try {
                if (gameBoard.getPieceAt(king.getP2() - 1, king.getP1()).getType() == Type.King) {
                    if (gameBoard.getPieceAt(king.getP2() - 1, king.getP1()).getColor() == Color.Black) {
                        return true;
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("out of bounds");
            }
            try {
                if (gameBoard.getPieceAt(king.getP2() - 1, king.getP1() - 1).getType() == Type.King) {
                    if (gameBoard.getPieceAt(king.getP2() - 1, king.getP1() - 1).getColor() == Color.Black) {
                        return true;
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("out of bounds");
            }
            try {
                if (gameBoard.getPieceAt(king.getP2() - 1, king.getP1() + 1).getType() == Type.King) {
                    if (gameBoard.getPieceAt(king.getP2() - 1, king.getP1() + 1).getColor() == Color.Black) {
                        return true;
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("out of bounds");
            }
            try {
                if (gameBoard.getPieceAt(king.getP2() + 1, king.getP1()).getType() == Type.King) {
                    if (gameBoard.getPieceAt(king.getP2() + 1, king.getP1()).getColor() == Color.Black) {
                        return true;
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("out of bounds");
            }
            try {
                if (gameBoard.getPieceAt(king.getP2() + 1, king.getP1() - 1).getType() == Type.King) {
                    if (gameBoard.getPieceAt(king.getP2() + 1, king.getP1() - 1).getColor() == Color.Black) {
                        return true;
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("out of bounds");
            }
            try {
                if (gameBoard.getPieceAt(king.getP2() + 1, king.getP1() + 1).getType() == Type.King) {
                    if (gameBoard.getPieceAt(king.getP2() + 1, king.getP1() + 1).getColor() == Color.Black) {
                        return true;
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("out of bounds");
            }
            //Each Piece Checked////////////////////////////////////////////////////////////////
        }
        ////////////////////////////////////////////////////////////////////////////////////////
        //This is for the Black King////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////
        else if (blackWhite == 1) {
            //check for Pawns///////////////////////////////////////////////////////////////////
            try {
                if (gameBoard.getPieceAt(king.getP2() - 1, king.getP1() - 1).getType() == Type.Pawn) {
                    if (gameBoard.getPieceAt(king.getP2() - 1, king.getP1() - 1).getColor() == Color.White) {
                        return true;
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("out of bounds");
            }
            try {
                if (gameBoard.getPieceAt(king.getP2() + 1, king.getP1() - 1).getType() == Type.Pawn) {
                    if (gameBoard.getPieceAt(king.getP2() + 1, king.getP1() - 1).getColor() == Color.White) {
                        return true;
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("out of bounds");
            }
            //check for Knights/////////////////////////////////////////////////////////////////
            try {
                if (gameBoard.getPieceAt(king.getP2() + 2, king.getP1() - 1).getType() == Type.Knight) {
                    if (gameBoard.getPieceAt(king.getP2() + 2, king.getP1() - 1).getColor() == Color.White) {
                        return true;
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("out of bounds");
            }
            try {
                if (gameBoard.getPieceAt(king.getP2() + 2, king.getP1() + 1).getType() == Type.Knight) {
                    if (gameBoard.getPieceAt(king.getP2() + 2, king.getP1() + 1).getColor() == Color.White) {
                        return true;
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("out of bounds");
            }
            try {
                if (gameBoard.getPieceAt(king.getP2() - 2, king.getP1() - 1).getType() == Type.Knight) {
                    if (gameBoard.getPieceAt(king.getP2() - 2, king.getP1() - 1).getColor() == Color.White) {
                        return true;
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("out of bounds");
            }
            try {
                if (gameBoard.getPieceAt(king.getP2() - 2, king.getP1() + 1).getType() == Type.Knight) {
                    if (gameBoard.getPieceAt(king.getP2() - 2, king.getP1() + 1).getColor() == Color.White) {
                        return true;
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("out of bounds");
            }
            try {
                if (gameBoard.getPieceAt(king.getP2() - 1, king.getP1() + 2).getType() == Type.Knight) {
                    if (gameBoard.getPieceAt(king.getP2() - 1, king.getP1() + 2).getColor() == Color.White) {
                        return true;
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("out of bounds");
            }
            try {
                if (gameBoard.getPieceAt(king.getP2() + 1, king.getP1() + 2).getType() == Type.Knight) {
                    if (gameBoard.getPieceAt(king.getP2() + 1, king.getP1() + 2).getColor() == Color.White) {
                        return true;
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("out of bounds");
            }
            try {
                if (gameBoard.getPieceAt(king.getP2() - 1, king.getP1() - 2).getType() == Type.Knight) {
                    if (gameBoard.getPieceAt(king.getP2() - 1, king.getP1() - 2).getColor() == Color.White) {
                        return true;
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("out of bounds");
            }
            try {
                if (gameBoard.getPieceAt(king.getP2() + 1, king.getP1() - 2).getType() == Type.Knight) {
                    if (gameBoard.getPieceAt(king.getP2() + 1, king.getP1() - 2).getColor() == Color.White) {
                        return true;
                    }
                }

            } catch (IndexOutOfBoundsException e) {
                System.out.println("out of bounds");
            }
            //check for Bishops & Queen/////////////////////////////////////////////////////////
            try {
                for (int y = king.getP1(); y < 8; y++) {
                    for (int x = king.getP2(); x < 8; x++) {
                        //checking diagonals
                        if (x == y) {
                            //No check if own pieces
                            if (gameBoard.getPieceAt(y, x).getColor() == Color.Black) {
                                //Not Check
                                return false;
                            }
                            //If other pieces other than bishop appear, no check
                            else if (gameBoard.getPieceAt(y, x).getColor() == Color.White) {
                                if (gameBoard.getPieceAt(y, x).getType() == Type.Knight ||
                                        gameBoard.getPieceAt(y, x).getType() == Type.Rook ||
                                        gameBoard.getPieceAt(y, x).getType() == Type.Pawn ||
                                        gameBoard.getPieceAt(y, x).getType() == Type.King) {
                                    //Not Check
                                    return false;
                                }
                                //if bishop uninterrupted, check
                                else if (gameBoard.getPieceAt(y, x).getType() == Type.Bishop ||
                                        gameBoard.getPieceAt(y, x).getType() == Type.Queen) {
                                    //Check
                                    return true;
                                }
                            }
                        }
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("out of bounds");
            }

            try {
                for (int y = king.getP1(); y > 8; y--) {
                    for (int x = king.getP2(); x < 8; x++) {
                        //checking diagonals
                        if (x == y) {
                            //No check if own pieces
                            if (gameBoard.getPieceAt(y, x).getColor() == Color.Black) {
                                //Not Check
                                return false;
                            }
                            //If other pieces other than bishop appear, no check
                            else if (gameBoard.getPieceAt(y, x).getColor() == Color.White) {
                                if (gameBoard.getPieceAt(y, x).getType() == Type.Knight ||
                                        gameBoard.getPieceAt(y, x).getType() == Type.Rook ||
                                        gameBoard.getPieceAt(y, x).getType() == Type.Pawn ||
                                        gameBoard.getPieceAt(y, x).getType() == Type.King) {
                                    //Not Check
                                    return false;
                                }
                                //if bishop uninterrupted, check
                                else if (gameBoard.getPieceAt(y, x).getType() == Type.Bishop ||
                                        gameBoard.getPieceAt(y, x).getType() == Type.Queen) {
                                    //Check
                                    return true;
                                }
                            }
                        }
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("out of bounds");
            }

            try {
                for (int y = king.getP1(); y < 8; y++) {
                    for (int x = king.getP2(); x > 8; x--) {
                        //checking diagonals
                        if (x == y) {
                            //No check if own pieces
                            if (gameBoard.getPieceAt(y, x).getColor() == Color.Black) {
                                //Not Check
                                return false;
                            }
                            //If other pieces other than bishop appear, no check
                            else if (gameBoard.getPieceAt(y, x).getColor() == Color.White) {
                                if (gameBoard.getPieceAt(y, x).getType() == Type.Knight ||
                                        gameBoard.getPieceAt(y, x).getType() == Type.Rook ||
                                        gameBoard.getPieceAt(y, x).getType() == Type.Pawn ||
                                        gameBoard.getPieceAt(y, x).getType() == Type.King) {
                                    //Not Check
                                    return false;
                                }
                                //if bishop uninterrupted, check
                                else if (gameBoard.getPieceAt(y, x).getType() == Type.Bishop ||
                                        gameBoard.getPieceAt(y, x).getType() == Type.Queen) {
                                    //Check
                                    return true;
                                }
                            }
                        }
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("out of bounds");
            }

            try {
                for (int y = king.getP1(); y > 8; y--) {
                    for (int x = king.getP2(); x > 8; x--) {
                        //checking diagonals
                        if (x == y) {
                            //No check if own pieces
                            if (gameBoard.getPieceAt(y, x).getColor() == Color.Black) {
                                //Not Check
                                return false;
                            }
                            //If other pieces other than bishop appear, no check
                            else if (gameBoard.getPieceAt(y, x).getColor() == Color.White) {
                                if (gameBoard.getPieceAt(y, x).getType() == Type.Knight ||
                                        gameBoard.getPieceAt(y, x).getType() == Type.Rook ||
                                        gameBoard.getPieceAt(y, x).getType() == Type.Pawn ||
                                        gameBoard.getPieceAt(y, x).getType() == Type.King) {
                                    //Not Check
                                    return false;
                                }
                                //if bishop uninterrupted, check
                                else if (gameBoard.getPieceAt(y, x).getType() == Type.Bishop ||
                                        gameBoard.getPieceAt(y, x).getType() == Type.Queen) {
                                    //Check
                                    return true;
                                }
                            }
                        }
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("out of bounds");
            }
            //check for Rooks & Queen///////////////////////////////////////////////////////////
            try {
                //checking horizontal
                int y = gameBoard.getPieceAt(king.getP2(), king.getP1()).getP1();
                for (int x = 0; x < 8; x++) {
                    if (gameBoard.getPieceAt(y, x).getColor() == Color.Black) {
                        //Not Check
                        return false;
                    }
                    //If other pieces other than bishop appear, no check
                    else if (gameBoard.getPieceAt(y, x).getColor() == Color.White) {
                        if (gameBoard.getPieceAt(y, x).getType() == Type.Knight ||
                                gameBoard.getPieceAt(y, x).getType() == Type.Bishop ||
                                gameBoard.getPieceAt(y, x).getType() == Type.Pawn ||
                                gameBoard.getPieceAt(y, x).getType() == Type.King) {
                            //Not Check
                            return false;
                        }
                        //if Rook uninterrupted, check
                        else if (gameBoard.getPieceAt(y, x).getType() == Type.Rook ||
                                gameBoard.getPieceAt(y, x).getType() == Type.Queen) {
                            //Check
                            return true;
                        }
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("out of bounds");
            }

            try {
                //checking horizontal
                int x = gameBoard.getPieceAt(king.getP2(), king.getP1()).getP2();
                for (int y = 0; y < 8; y++) {
                    if (gameBoard.getPieceAt(y, x).getColor() == Color.Black) {
                        //Not Check
                        return false;
                    }
                    //If other pieces other than bishop appear, no check
                    else if (gameBoard.getPieceAt(y, x).getColor() == Color.White) {
                        if (gameBoard.getPieceAt(y, x).getType() == Type.Knight ||
                                gameBoard.getPieceAt(y, x).getType() == Type.Bishop ||
                                gameBoard.getPieceAt(y, x).getType() == Type.Pawn ||
                                gameBoard.getPieceAt(y, x).getType() == Type.King) {
                            //Not Check
                            return false;
                        }
                        //if Rook uninterrupted, check
                        else if (gameBoard.getPieceAt(y, x).getType() == Type.Rook ||
                                gameBoard.getPieceAt(y, x).getType() == Type.Queen) {
                            //Check
                            return true;
                        }
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("out of bounds");
            }
            //check for Kings///////////////////////////////////////////////////////////////////
            //Will be using this check to see if some moves are safe. This will prevent
            //Other King from walking into striking distance.
            try {
                if (gameBoard.getPieceAt(king.getP2(), king.getP1()).getType() == Type.King) {
                    if (gameBoard.getPieceAt(king.getP2(), king.getP1()).getColor() == Color.White) {
                        return true;
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("out of bounds");
            }
            try {
                if (gameBoard.getPieceAt(king.getP2(), king.getP1() - 1).getType() == Type.King) {
                    if (gameBoard.getPieceAt(king.getP2(), king.getP1() - 1).getColor() == Color.White) {
                        return true;
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("out of bounds");
            }
            try {
                if (gameBoard.getPieceAt(king.getP2(), king.getP1() + 1).getType() == Type.King) {
                    if (gameBoard.getPieceAt(king.getP2(), king.getP1() + 1).getColor() == Color.White) {
                        return true;
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("out of bounds");
            }
            try {
                if (gameBoard.getPieceAt(king.getP2() - 1, king.getP1()).getType() == Type.King) {
                    if (gameBoard.getPieceAt(king.getP2() - 1, king.getP1()).getColor() == Color.White) {
                        return true;
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("out of bounds");
            }
            try {
                if (gameBoard.getPieceAt(king.getP2() - 1, king.getP1() - 1).getType() == Type.King) {
                    if (gameBoard.getPieceAt(king.getP2() - 1, king.getP1() - 1).getColor() == Color.White) {
                        return true;
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("out of bounds");
            }
            try {
                if (gameBoard.getPieceAt(king.getP2() - 1, king.getP1() + 1).getType() == Type.King) {
                    if (gameBoard.getPieceAt(king.getP2() - 1, king.getP1() + 1).getColor() == Color.White) {
                        return true;
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("out of bounds");
            }
            try {
                if (gameBoard.getPieceAt(king.getP2() + 1, king.getP1()).getType() == Type.King) {
                    if (gameBoard.getPieceAt(king.getP2() + 1, king.getP1()).getColor() == Color.White) {
                        return true;
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("out of bounds");
            }
            try {
                if (gameBoard.getPieceAt(king.getP2() + 1, king.getP1() - 1).getType() == Type.King) {
                    if (gameBoard.getPieceAt(king.getP2() + 1, king.getP1() - 1).getColor() == Color.White) {
                        return true;
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("out of bounds");
            }
            try {
                if (gameBoard.getPieceAt(king.getP2() + 1, king.getP1() + 1).getType() == Type.King) {
                    if (gameBoard.getPieceAt(king.getP2() + 1, king.getP1() + 1).getColor() == Color.White) {
                        return true;
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("out of bounds");
            }
            //Each Piece Checked////////////////////////////////////////////////////////////////
        }
        return false;
    }
}
