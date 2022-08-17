public class Board {
    private final Square[][] board;
    private static int[] knightX;
    private static int[] knightY;
    private static int[] kingX;
    private static int[] kingY;
    private Square whiteKing;
    private Square blackKing;

    /*ArrayList<int[]> whiteKingSquares;
    ArrayList<int[]> blackKingSquares;
    ArrayList<int[]> whiteKingEnemies;
    ArrayList<int[]> blackKingEnemies;*/

    public Board(){
        this.board = new Square[8][8];
        knightX = new int[]{2, 1, -1, -2, -2, -1, 1, 2};
        knightY = new int[]{1, 2, 2, 1, -1, -2, -2, -1};
        kingX = new int[]{-1, -1,-1, 0, 1, 1, 1, 0};
        kingY = new int[]{-1, 0, 1, 1, 1, 0, -1, -1};
        whiteKing = null;
        blackKing = null;
        initializeBoard();
    }

    public void initializeBoard(){
        for(int column = 0; column < 8; column++){
            board[1][column] = new Square(1, column, new Pawn(PieceColor.WHITE));
        }
        for(int column = 0; column < 8; column++){
            board[6][column] = new Square(6, column, new Pawn(PieceColor.BLACK));
        }

        board[0][0] = new Square(0, 0, new Rook(PieceColor.WHITE));
        board[0][7] = new Square(0, 7, new Rook(PieceColor.WHITE));
        board[0][1] = new Square(0, 1, new Knight(PieceColor.WHITE));
        board[0][6] = new Square(0, 6, new Knight(PieceColor.WHITE));
        board[0][2] = new Square(0, 2, new Bishop(PieceColor.WHITE));
        board[0][5] = new Square(0, 5, new Bishop(PieceColor.WHITE));
        board[0][3] = new Square(0, 3, new Queen(PieceColor.WHITE));
        board[0][4] = new Square(0, 4, new King(PieceColor.WHITE));
        whiteKing = getSquare(0, 4);

        board[7][0] = new Square(7, 0, new Rook(PieceColor.BLACK));
        board[7][7] = new Square(7, 7, new Rook(PieceColor.BLACK));
        board[7][1] = new Square(7, 1, new Knight(PieceColor.BLACK));
        board[7][6] = new Square(7, 6, new Knight(PieceColor.BLACK));
        board[7][2] = new Square(7, 2, new Bishop(PieceColor.BLACK));
        board[7][5] = new Square(7, 5, new Bishop(PieceColor.BLACK));
        board[7][3] = new Square(7, 3, new Queen(PieceColor.BLACK));
        board[7][4] = new Square(7, 4, new King(PieceColor.BLACK));
        blackKing = getSquare(7, 4);

        for(int row = 2; row < 6; row++){
            for(int column = 0; column < 8; column++){
                board[row][column] = new Square(row, column, null);
            }
        }
    }
    public Square getSquare(int x, int y){
        return board[x][y];
    }

    public boolean inBounds(int x, int y){
        return ((x >= 0) && (x < 8) && (y >= 0) && (y < 8));
    }

    @Override
    public String toString(){
        StringBuilder result = new StringBuilder();
        for(int i = 7; i >= 0; i--){
            for(int j = 0; j < board.length; j++){
                if(board[i][j].getPiece() == null)
                    result.append("*");
                else {
                    result.append(board[i][j].getPiece().getPieceColor());
                    result.append(" ");
                    result.append(board[i][j].getPiece());
                    result.append(" ");
                }
            }
            result.append("\n");
        }
        return result.toString();
    }

    public int generateValidMoves(PieceColor color){
        int total = 0;
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(board[i][j].containsPiece() && board[i][j].getPiece().getPieceColor() == color){
                    int current = board[i][j].getPiece().generatePossibleMoves(this, i, j);
                    total += current;
                }
            }
        }
        return total;
    }

    public void updateKing(PieceColor color, int x, int y){
        if(color == PieceColor.WHITE){
            whiteKing = getSquare(x, y);
        }
        else{
            blackKing = getSquare(x, y);
        }
    }

    public boolean isCheck(PieceColor color){
        int x;
        int y;
        if(color == PieceColor.WHITE){
            x = whiteKing.getX();
            y = whiteKing.getY();
            if(y-1 >= 0){
                if(getSquare(x+1, y-1).containsPiece() && getSquare(x+1, y-1).getPiece() instanceof Pawn && getSquare(x+1, y-1).getPiece().getPieceColor() != color)
                    return true;
            }
            if(y+1 < 8){
                if(getSquare(x+1, y+1).containsPiece() && getSquare(x+1, y+1).getPiece() instanceof Pawn && getSquare(x+1, y+1).getPiece().getPieceColor() != color)
                    return true;
            }
        }
        else{
            x = blackKing.getX();
            y = blackKing.getY();
            if(y-1 >= 0){
                if(getSquare(x-1, y-1).containsPiece() && getSquare(x-1, y-1).getPiece() instanceof Pawn && getSquare(x-1, y-1).getPiece().getPieceColor() != color)
                    return true;
            }
            if(y+1 < 8){
                if(getSquare(x-1, y+1).containsPiece() && getSquare(x-1, y+1).getPiece() instanceof Pawn && getSquare(x-1, y+1).getPiece().getPieceColor() != color)
                    return true;
            }
        }

        for(int i = 0; i < 8; i++){
            int newX = x + kingX[i];
            int newY = y + kingY[i];
            if(inBounds(newX, newY)){
                if(getSquare(newX, newY).containsPiece()){
                    if(getSquare(newX, newY).getPiece() instanceof King && getSquare(newX, newY).getPiece().getPieceColor() != color){
                        return true;
                    }
                }
            }
        }

        int xDown = x + 1;
        while(xDown < 8){
            if(this.getSquare(xDown, y).containsPiece()){
                if(color != this.getSquare(xDown, y).getPiece().getPieceColor()){
                    if(this.getSquare(xDown, y).getPiece() instanceof Rook || this.getSquare(xDown, y).getPiece() instanceof Queen)
                        return true;
                    else
                        break;
                }
                else{
                    if(this.getSquare(xDown, y).getPiece() instanceof King){
                        xDown += 1;
                    }
                    else
                        break;
                }
            }
            else{
                xDown += 1;
            }
        }

        int xUp = x - 1;
        while(xUp >= 0){
            if(this.getSquare(xUp, y).containsPiece()){
                if(color != this.getSquare(xUp, y).getPiece().getPieceColor()){
                    if(this.getSquare(xUp, y).getPiece() instanceof Rook || this.getSquare(xUp, y).getPiece() instanceof Queen)
                        return true;
                    else
                        break;
                }
                else{
                    if(this.getSquare(xUp, y).getPiece() instanceof King){
                        xUp -= 1;
                    }
                    else
                        break;
                }
            }
            else{
                xUp -= 1;
            }
        }

        int yRight = y + 1;
        while(yRight < 8){
            if(this.getSquare(x, yRight).containsPiece()){
                if(color != this.getSquare(x, yRight).getPiece().getPieceColor()){
                    if(this.getSquare(x, yRight).getPiece() instanceof Rook || this.getSquare(x, yRight).getPiece() instanceof Queen)
                        return true;
                    else
                        break;
                }
                else{
                    if(this.getSquare(x, yRight).getPiece() instanceof King){
                        yRight += 1;
                    }
                    else
                        break;
                }
            }
            else{
                yRight += 1;
            }
        }

        int yLeft = y - 1;
        while(yLeft >= 0){
            if(this.getSquare(x, yLeft).containsPiece()){
                if(color != this.getSquare(x, yLeft).getPiece().getPieceColor()){
                    if(this.getSquare(x, yLeft).getPiece() instanceof Rook || this.getSquare(x, yLeft).getPiece() instanceof Queen)
                        return true;
                    else
                        break;
                }
                else{
                    if(this.getSquare(x, yLeft).getPiece() instanceof King){
                        yLeft -= 1;
                    }
                    else
                        break;
                }
            }
            else{
                yLeft -= 1;
            }
        }

        xDown = x + 1;
        yRight = y + 1;
        while(xDown < 8 && yRight < 8){
            if(this.getSquare(xDown, yRight).containsPiece()){
                if(color != this.getSquare(xDown, yRight).getPiece().getPieceColor()){
                    if(this.getSquare(xDown, yRight).getPiece() instanceof Bishop || this.getSquare(xDown, yRight).getPiece() instanceof Queen)
                        return true;
                    else
                        break;
                }
                else{
                    if(this.getSquare(xDown, yRight).getPiece() instanceof King){
                        xDown += 1;
                        yRight += 1;
                    }
                    else
                        break;
                }
            }
            else{
                xDown += 1;
                yRight += 1;
            }
        }

        xDown = x + 1;
        yLeft = y - 1;
        while(xDown < 8 && yLeft >= 0){
            if(this.getSquare(xDown, yLeft).containsPiece()){
                if(color != this.getSquare(xDown, yLeft).getPiece().getPieceColor()){
                    if(this.getSquare(xDown, yLeft).getPiece() instanceof Bishop || this.getSquare(xDown, yLeft).getPiece() instanceof Queen)
                        return true;
                    else
                        break;
                }
                else{
                    if(this.getSquare(xDown, yLeft).getPiece() instanceof King){
                        xDown += 1;
                        yLeft -= 1;
                    }
                    else
                        break;
                }
            }
            else{
                xDown += 1;
                yLeft -= 1;
            }
        }

        xUp = x - 1;
        yRight = y + 1;
        while(xUp >= 0 && yRight < 8){
            if(this.getSquare(xUp, yRight).containsPiece()){
                if(color != this.getSquare(xUp, yRight).getPiece().getPieceColor()){
                    if(this.getSquare(xUp, yRight).getPiece() instanceof Bishop || this.getSquare(xUp, yRight).getPiece() instanceof Queen)
                        return true;
                    else
                        break;
                }
                else{
                    if(this.getSquare(xUp, yRight).getPiece() instanceof King){
                        xUp -= 1;
                        yRight += 1;
                    }
                    else
                        break;
                }
            }
            else{
                xUp -= 1;
                yRight += 1;
            }
        }

        xUp = x - 1;
        yLeft = y - 1;
        while(xUp >= 0 && yLeft >= 0){
            if(this.getSquare(xUp, yLeft).containsPiece()){
                if(color != this.getSquare(xUp, yLeft).getPiece().getPieceColor()){
                    if(this.getSquare(xUp, yLeft).getPiece() instanceof Bishop || this.getSquare(xUp, yLeft).getPiece() instanceof Queen)
                        return true;
                    else
                        break;
                }
                else{
                    if(this.getSquare(xUp, yLeft).getPiece() instanceof King){
                        xUp -= 1;
                        yLeft -= 1;
                    }
                    else
                        break;
                }
            }
            else{
                xUp -= 1;
                yLeft -= 1;
            }
        }

        for(int i = 0; i < 8; i++){
            int newX = x + knightX[i];
            int newY = y + knightY[i];
            if(inBounds(newX, newY)){
                if(getSquare(newX, newY).containsPiece()){
                    if(getSquare(newX, newY).getPiece() instanceof Knight && getSquare(newX, newY).getPiece().getPieceColor() != color){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isSafe(PieceColor color, int x, int y){
        if(color == PieceColor.WHITE){
            if(y-1 >= 0){
                if(getSquare(x+1, y-1).containsPiece() && getSquare(x+1, y-1).getPiece() instanceof Pawn && getSquare(x+1, y-1).getPiece().getPieceColor() != color)
                    return false;
            }
            if(y+1 < 8){
                if(getSquare(x+1, y+1).containsPiece() && getSquare(x+1, y+1).getPiece() instanceof Pawn && getSquare(x+1, y+1).getPiece().getPieceColor() != color)
                    return false;
            }
        }
        else{
            if(y-1 >= 0){
                if(getSquare(x-1, y-1).containsPiece() && getSquare(x-1, y-1).getPiece() instanceof Pawn && getSquare(x-1, y-1).getPiece().getPieceColor() != color)
                    return false;
            }
            if(y+1 < 8){
                if(getSquare(x-1, y+1).containsPiece() && getSquare(x-1, y+1).getPiece() instanceof Pawn && getSquare(x-1, y+1).getPiece().getPieceColor() != color)
                    return false;
            }
        }

        for(int i = 0; i < 8; i++){
            int newX = x + kingX[i];
            int newY = y + kingY[i];
            if(inBounds(newX, newY)){
                if(getSquare(newX, newY).containsPiece()){
                    if(getSquare(newX, newY).getPiece() instanceof King && getSquare(newX, newY).getPiece().getPieceColor() != color){
                        return false;
                    }
                }
            }
        }

        int xDown = x + 1;
        while(xDown < 8){
            if(this.getSquare(xDown, y).containsPiece()){
                if(color != this.getSquare(xDown, y).getPiece().getPieceColor()){
                    if(this.getSquare(xDown, y).getPiece() instanceof Rook || this.getSquare(xDown, y).getPiece() instanceof Queen)
                        return false;
                    else
                        break;
                }
                else{
                    if(this.getSquare(xDown, y).getPiece() instanceof King){
                        xDown += 1;
                    }
                    else
                        break;
                }
            }
            else{
                xDown += 1;
            }
        }

        int xUp = x - 1;
        while(xUp >= 0){
            if(this.getSquare(xUp, y).containsPiece()){
                if(color != this.getSquare(xUp, y).getPiece().getPieceColor()){
                    if(this.getSquare(xUp, y).getPiece() instanceof Rook || this.getSquare(xUp, y).getPiece() instanceof Queen)
                        return false;
                    else
                        break;
                }
                else{
                    if(this.getSquare(xUp, y).getPiece() instanceof King){
                        xUp -= 1;
                    }
                    else
                        break;
                }
            }
            else{
                xUp -= 1;
            }
        }

        int yRight = y + 1;
        while(yRight < 8){
            if(this.getSquare(x, yRight).containsPiece()){
                if(color != this.getSquare(x, yRight).getPiece().getPieceColor()){
                    if(this.getSquare(x, yRight).getPiece() instanceof Rook || this.getSquare(x, yRight).getPiece() instanceof Queen)
                        return false;
                    else
                        break;
                }
                else{
                    if(this.getSquare(x, yRight).getPiece() instanceof King){
                        yRight += 1;
                    }
                    else
                        break;
                }
            }
            else{
                yRight += 1;
            }
        }

        int yLeft = y - 1;
        while(yLeft >= 0){
            if(this.getSquare(x, yLeft).containsPiece()){
                if(color != this.getSquare(x, yLeft).getPiece().getPieceColor()){
                    if(this.getSquare(x, yLeft).getPiece() instanceof Rook || this.getSquare(x, yLeft).getPiece() instanceof Queen)
                        return false;
                    else
                        break;
                }
                else{
                    if(this.getSquare(x, yLeft).getPiece() instanceof King){
                        yLeft -= 1;
                    }
                    else
                        break;
                }
            }
            else{
                yLeft -= 1;
            }
        }

        xDown = x + 1;
        yRight = y + 1;
        while(xDown < 8 && yRight < 8){
            if(this.getSquare(xDown, yRight).containsPiece()){
                if(color != this.getSquare(xDown, yRight).getPiece().getPieceColor()){
                    if(this.getSquare(xDown, yRight).getPiece() instanceof Bishop || this.getSquare(xDown, yRight).getPiece() instanceof Queen)
                        return false;
                    else
                        break;
                }
                else{
                    if(this.getSquare(xDown, yRight).getPiece() instanceof King){
                        xDown += 1;
                        yRight += 1;
                    }
                    else
                        break;
                }
            }
            else{
                xDown += 1;
                yRight += 1;
            }
        }

        xDown = x + 1;
        yLeft = y - 1;
        while(xDown < 8 && yLeft >= 0){
            if(this.getSquare(xDown, yLeft).containsPiece()){
                if(color != this.getSquare(xDown, yLeft).getPiece().getPieceColor()){
                    if(this.getSquare(xDown, yLeft).getPiece() instanceof Bishop || this.getSquare(xDown, yLeft).getPiece() instanceof Queen)
                        return false;
                    else
                        break;
                }
                else{
                    if(this.getSquare(xDown, yLeft).getPiece() instanceof King){
                        xDown += 1;
                        yLeft -= 1;
                    }
                    else
                        break;
                }
            }
            else{
                xDown += 1;
                yLeft -= 1;
            }
        }

        xUp = x - 1;
        yRight = y + 1;
        while(xUp >= 0 && yRight < 8){
            if(this.getSquare(xUp, yRight).containsPiece()){
                if(color != this.getSquare(xUp, yRight).getPiece().getPieceColor()){
                    if(this.getSquare(xUp, yRight).getPiece() instanceof Bishop || this.getSquare(xUp, yRight).getPiece() instanceof Queen)
                        return false;
                    else
                        break;
                }
                else{
                    if(this.getSquare(xUp, yRight).getPiece() instanceof King){
                        xUp -= 1;
                        yRight += 1;
                    }
                    else
                        break;
                }
            }
            else{
                xUp -= 1;
                yRight += 1;
            }
        }

        xUp = x - 1;
        yLeft = y - 1;
        while(xUp >= 0 && yLeft >= 0){
            if(this.getSquare(xUp, yLeft).containsPiece()){
                if(color != this.getSquare(xUp, yLeft).getPiece().getPieceColor()){
                    if(this.getSquare(xUp, yLeft).getPiece() instanceof Bishop || this.getSquare(xUp, yLeft).getPiece() instanceof Queen)
                        return false;
                    else
                        break;
                }
                else{
                    if(this.getSquare(xUp, yLeft).getPiece() instanceof King){
                        xUp -= 1;
                        yLeft -= 1;
                    }
                    else
                        break;
                }
            }
            else{
                xUp -= 1;
                yLeft -= 1;
            }
        }

        for(int i = 0; i < 8; i++){
            int newX = x + knightX[i];
            int newY = y + knightY[i];
            if(inBounds(newX, newY)){
                if(getSquare(newX, newY).containsPiece()){
                    if(getSquare(newX, newY).getPiece() instanceof Knight && getSquare(newX, newY).getPiece().getPieceColor() != color){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    // public boolean isCheck(PieceColor color, Piece currentPiece)
    // Keep track of all possible king attack squares as well as potential enemy pieces within these squares.
    // If in check, check if moved piece is within these squares, do is check that ignores current piece if it
    // moves out of the attack squares to see if in check; if it captures a piece, do the same thing, but also
    // the attacked square and the moved piece.

    public boolean isSafe(PieceColor color, Piece currentPiece, int movedX, int movedY){
        int x;
        int y;
        if(color == PieceColor.WHITE){
            x = whiteKing.getX();
            y = whiteKing.getY();
            if(y-1 >= 0){
                if(getSquare(x+1, y-1).containsPiece() && getSquare(x+1, y-1).getPiece() instanceof Pawn && getSquare(x+1, y-1).getPiece().getPieceColor() != color){
                    if(x+1 != movedX || y-1 != movedY)
                        return false;
                }
            }
            if(y+1 < 8){
                if(getSquare(x+1, y+1).containsPiece() && getSquare(x+1, y+1).getPiece() instanceof Pawn && getSquare(x+1, y+1).getPiece().getPieceColor() != color){
                    if(x+1 != movedX || y+1 != movedY)
                        return false;
                }
            }
        }
        else{
            x = blackKing.getX();
            y = blackKing.getY();
            if(y-1 >= 0){
                if(getSquare(x-1, y-1).containsPiece() && getSquare(x-1, y-1).getPiece() instanceof Pawn && getSquare(x-1, y-1).getPiece().getPieceColor() != color){
                    if(x-1 != movedX || y-1 != movedY)
                        return false;
                }
            }
            if(y+1 < 8){
                if(getSquare(x-1, y+1).containsPiece() && getSquare(x-1, y+1).getPiece() instanceof Pawn && getSquare(x-1, y+1).getPiece().getPieceColor() != color){
                    if(x-1 != movedX || y+1 != movedY)
                        return false;
                }
            }
        }

        for(int i = 0; i < 8; i++){
            int newX = x + kingX[i];
            int newY = y + kingY[i];
            if(inBounds(newX, newY)){
                if(getSquare(newX, newY).containsPiece()){
                    if(getSquare(newX, newY).getPiece() instanceof King && getSquare(newX, newY).getPiece().getPieceColor() != color){
                        return false;
                    }
                }
            }
        }

        int xDown = x + 1;
        while(xDown < 8){
            if(xDown == movedX && y == movedY)
                break;
            if(this.getSquare(xDown, y).containsPiece()){
                if(color != this.getSquare(xDown, y).getPiece().getPieceColor()){
                    if(this.getSquare(xDown, y).getPiece() instanceof Rook || this.getSquare(xDown, y).getPiece() instanceof Queen)
                        return false;
                    else
                        break;
                }
                else{
                    if(this.getSquare(xDown, y).getPiece() == currentPiece){
                        xDown += 1;
                        continue;
                    }
                    if(this.getSquare(xDown, y).getPiece() instanceof King){
                        xDown += 1;
                    }
                    else
                        break;
                }
            }
            else{
                xDown += 1;
            }
        }

        int xUp = x - 1;
        while(xUp >= 0){
            if(xUp == movedX && y == movedY)
                break;
            if(this.getSquare(xUp, y).containsPiece()){
                if(color != this.getSquare(xUp, y).getPiece().getPieceColor()){
                    if(this.getSquare(xUp, y).getPiece() instanceof Rook || this.getSquare(xUp, y).getPiece() instanceof Queen)
                        return false;
                    else
                        break;
                }
                else{
                    if(this.getSquare(xUp, y).getPiece() == currentPiece){
                        xUp -= 1;
                        continue;
                    }
                    if(this.getSquare(xUp, y).getPiece() instanceof King){
                        xUp -= 1;
                    }
                    else
                        break;
                }
            }
            else{
                xUp -= 1;
            }
        }

        int yRight = y + 1;
        while(yRight < 8){
            if(x == movedX && yRight == movedY)
                break;
            if(this.getSquare(x, yRight).containsPiece()){
                if(color != this.getSquare(x, yRight).getPiece().getPieceColor()){
                    if(this.getSquare(x, yRight).getPiece() instanceof Rook || this.getSquare(x, yRight).getPiece() instanceof Queen)
                        return false;
                    else
                        break;
                }
                else{
                    if(this.getSquare(x, yRight).getPiece() == currentPiece){
                        yRight += 1;
                        continue;
                    }
                    if(this.getSquare(x, yRight).getPiece() instanceof King){
                        yRight += 1;
                    }
                    else
                        break;
                }
            }
            else{
                yRight += 1;
            }
        }

        int yLeft = y - 1;
        while(yLeft >= 0){
            if(x == movedX && yLeft == movedY)
                break;
            if(this.getSquare(x, yLeft).containsPiece()){
                if(color != this.getSquare(x, yLeft).getPiece().getPieceColor()){
                    if(this.getSquare(x, yLeft).getPiece() instanceof Rook || this.getSquare(x, yLeft).getPiece() instanceof Queen)
                        return false;
                    else
                        break;
                }
                else{
                    if(this.getSquare(x, yLeft).getPiece() == currentPiece){
                        yLeft -= 1;
                        continue;
                    }
                    if(this.getSquare(x, yLeft).getPiece() instanceof King){
                        yLeft -= 1;
                    }
                    else
                        break;
                }
            }
            else{
                yLeft -= 1;
            }
        }

        xDown = x + 1;
        yRight = y + 1;
        while(xDown < 8 && yRight < 8){
            if(xDown == movedX && yRight == movedY)
                break;
            if(this.getSquare(xDown, yRight).containsPiece()){
                if(color != this.getSquare(xDown, yRight).getPiece().getPieceColor()){
                    if(this.getSquare(xDown, yRight).getPiece() instanceof Bishop || this.getSquare(xDown, yRight).getPiece() instanceof Queen)
                        return false;
                    else
                        break;
                }
                else{
                    if(this.getSquare(xDown, yRight).getPiece() == currentPiece){
                        xDown += 1;
                        yRight += 1;
                        continue;
                    }
                    if(this.getSquare(xDown, yRight).getPiece() instanceof King){
                        xDown += 1;
                        yRight += 1;
                    }
                    else
                        break;
                }
            }
            else{
                xDown += 1;
                yRight += 1;
            }
        }

        xDown = x + 1;
        yLeft = y - 1;
        while(xDown < 8 && yLeft >= 0){
            if(xDown == movedX && yLeft == movedY)
                break;
            if(this.getSquare(xDown, yLeft).containsPiece()){
                if(color != this.getSquare(xDown, yLeft).getPiece().getPieceColor()){
                    if(this.getSquare(xDown, yLeft).getPiece() instanceof Bishop || this.getSquare(xDown, yLeft).getPiece() instanceof Queen)
                        return false;
                    else
                        break;
                }
                else{
                    if(this.getSquare(xDown, yLeft).getPiece() == currentPiece){
                        xDown += 1;
                        yLeft -= 1;
                        continue;
                    }
                    if(this.getSquare(xDown, yLeft).getPiece() instanceof King){
                        xDown += 1;
                        yLeft -= 1;
                    }
                    else
                        break;
                }
            }
            else{
                xDown += 1;
                yLeft -= 1;
            }
        }

        xUp = x - 1;
        yRight = y + 1;
        while(xUp >= 0 && yRight < 8){
            if(xUp == movedX && yRight == movedY)
                break;
            if(this.getSquare(xUp, yRight).containsPiece()){
                if(color != this.getSquare(xUp, yRight).getPiece().getPieceColor()){
                    if(this.getSquare(xUp, yRight).getPiece() instanceof Bishop || this.getSquare(xUp, yRight).getPiece() instanceof Queen)
                        return false;
                    else
                        break;
                }
                else{
                    if(this.getSquare(xUp, yRight).getPiece() == currentPiece){
                        xUp -= 1;
                        yRight += 1;
                        continue;
                    }
                    if(this.getSquare(xUp, yRight).getPiece() instanceof King){
                        xUp -= 1;
                        yRight += 1;
                    }
                    else
                        break;
                }
            }
            else{
                xUp -= 1;
                yRight += 1;
            }
        }

        xUp = x - 1;
        yLeft = y - 1;
        while(xUp >= 0 && yLeft >= 0){
            if(xUp == movedX && yLeft == movedY)
                break;
            if(this.getSquare(xUp, yLeft).containsPiece()){
                if(color != this.getSquare(xUp, yLeft).getPiece().getPieceColor()){
                    if(this.getSquare(xUp, yLeft).getPiece() instanceof Bishop || this.getSquare(xUp, yLeft).getPiece() instanceof Queen)
                        return false;
                    else
                        break;
                }
                else{
                    if(this.getSquare(xUp, yLeft).getPiece() == currentPiece){
                        xUp -= 1;
                        yLeft -= 1;
                        continue;
                    }
                    if(this.getSquare(xUp, yLeft).getPiece() instanceof King){
                        xUp -= 1;
                        yLeft -= 1;
                    }
                    else
                        break;
                }
            }
            else{
                xUp -= 1;
                yLeft -= 1;
            }
        }

        for(int i = 0; i < 8; i++){
            int newX = x + knightX[i];
            int newY = y + knightY[i];
            if(newX == movedX && newY == movedY)
                continue;
            if(inBounds(newX, newY)){
                if(getSquare(newX, newY).containsPiece()){
                    if(getSquare(newX, newY).getPiece() instanceof Knight && getSquare(newX, newY).getPiece().getPieceColor() != color){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    // For instances such as en passant where more than two pieces involved.
    public boolean isSafe(PieceColor color, Piece currentPiece, int movedX, int movedY, int capturedX, int capturedY){
        int x;
        int y;
        if(color == PieceColor.WHITE){
            x = whiteKing.getX();
            y = whiteKing.getY();
            if(y-1 >= 0){
                if(!(x + 1 == capturedX && y - 1 == capturedY)){
                    if(getSquare(x + 1, y - 1).containsPiece() && getSquare(x + 1, y - 1).getPiece() instanceof Pawn && getSquare(x + 1, y - 1).getPiece().getPieceColor() != color) {
                        if (x + 1 != movedX || y - 1 != movedY)
                            return false;
                    }
                }
            }
            if(y+1 < 8){
                if(!(x + 1 == capturedX && y + 1 == capturedY)){
                    if (getSquare(x + 1, y + 1).containsPiece() && getSquare(x + 1, y + 1).getPiece() instanceof Pawn && getSquare(x + 1, y + 1).getPiece().getPieceColor() != color) {
                        if (x + 1 != movedX || y + 1 != movedY)
                            return false;
                    }
                }
            }
        }
        else{
            x = blackKing.getX();
            y = blackKing.getY();
            if(y-1 >= 0){
                if(!(x - 1 == capturedX && y - 1 == capturedY)){
                    if (getSquare(x - 1, y - 1).containsPiece() && getSquare(x - 1, y - 1).getPiece() instanceof Pawn && getSquare(x - 1, y - 1).getPiece().getPieceColor() != color) {
                        if (x - 1 != movedX || y - 1 != movedY)
                            return false;
                    }
                }
            }
            if(y+1 < 8){
                if(!(x - 1 == capturedX && y + 1 == capturedY)){
                    if(getSquare(x - 1, y + 1).containsPiece() && getSquare(x - 1, y + 1).getPiece() instanceof Pawn && getSquare(x - 1, y + 1).getPiece().getPieceColor() != color) {
                        if(x - 1 != movedX || y + 1 != movedY)
                            return false;
                    }
                }
            }
        }

        for(int i = 0; i < 8; i++){
            int newX = x + kingX[i];
            int newY = y + kingY[i];
            if(inBounds(newX, newY)){
                if(getSquare(newX, newY).containsPiece()){
                    if(getSquare(newX, newY).getPiece() instanceof King && getSquare(newX, newY).getPiece().getPieceColor() != color){
                        return false;
                    }
                }
            }
        }

        int xDown = x + 1;
        while(xDown < 8){
            if(xDown == movedX && y == movedY)
                break;
            if(this.getSquare(xDown, y).containsPiece()){
                if(color != this.getSquare(xDown, y).getPiece().getPieceColor()){
                    if(xDown == capturedX && y == capturedY){
                        xDown += 1;
                        continue;
                    }
                    if(this.getSquare(xDown, y).getPiece() instanceof Rook || this.getSquare(xDown, y).getPiece() instanceof Queen)
                        return false;
                    else
                        break;
                }
                else{
                    if(this.getSquare(xDown, y).getPiece() == currentPiece){
                        xDown += 1;
                        continue;
                    }
                    if(this.getSquare(xDown, y).getPiece() instanceof King){
                        xDown += 1;
                    }
                    else
                        break;
                }
            }
            else{
                xDown += 1;
            }
        }

        int xUp = x - 1;
        while(xUp >= 0){
            if(xUp == movedX && y == movedY)
                break;
            if(this.getSquare(xUp, y).containsPiece()){
                if(color != this.getSquare(xUp, y).getPiece().getPieceColor()){
                    if(xUp == capturedX && y == capturedY){
                        xUp -= 1;
                        continue;
                    }
                    if(this.getSquare(xUp, y).getPiece() instanceof Rook || this.getSquare(xUp, y).getPiece() instanceof Queen)
                        return false;
                    else
                        break;
                }
                else{
                    if(this.getSquare(xUp, y).getPiece() == currentPiece){
                        xUp -= 1;
                        continue;
                    }
                    if(this.getSquare(xUp, y).getPiece() instanceof King){
                        xUp -= 1;
                    }
                    else
                        break;
                }
            }
            else{
                xUp -= 1;
            }
        }

        int yRight = y + 1;
        while(yRight < 8){
            if(x == movedX && yRight == movedY)
                break;
            if(this.getSquare(x, yRight).containsPiece()){
                if(color != this.getSquare(x, yRight).getPiece().getPieceColor()){
                    if(x == capturedX && yRight == capturedY){
                        yRight += 1;
                        continue;
                    }
                    if(this.getSquare(x, yRight).getPiece() instanceof Rook || this.getSquare(x, yRight).getPiece() instanceof Queen)
                        return false;
                    else
                        break;
                }
                else{
                    if(this.getSquare(x, yRight).getPiece() == currentPiece){
                        yRight += 1;
                        continue;
                    }
                    if(this.getSquare(x, yRight).getPiece() instanceof King){
                        yRight += 1;
                    }
                    else
                        break;
                }
            }
            else{
                yRight += 1;
            }
        }

        int yLeft = y - 1;
        while(yLeft >= 0){
            if(x == movedX && yLeft == movedY)
                break;
            if(this.getSquare(x, yLeft).containsPiece()){
                if(color != this.getSquare(x, yLeft).getPiece().getPieceColor()){
                    if(x == capturedX && yLeft == capturedY){
                        yLeft -= 1;
                        continue;
                    }
                    if(this.getSquare(x, yLeft).getPiece() instanceof Rook || this.getSquare(x, yLeft).getPiece() instanceof Queen)
                        return false;
                    else
                        break;
                }
                else{
                    if(this.getSquare(x, yLeft).getPiece() == currentPiece){
                        yLeft -= 1;
                        continue;
                    }
                    if(this.getSquare(x, yLeft).getPiece() instanceof King){
                        yLeft -= 1;
                    }
                    else
                        break;
                }
            }
            else{
                yLeft -= 1;
            }
        }

        xDown = x + 1;
        yRight = y + 1;
        while(xDown < 8 && yRight < 8){
            if(xDown == movedX && yRight == movedY)
                break;
            if(this.getSquare(xDown, yRight).containsPiece()){
                if(color != this.getSquare(xDown, yRight).getPiece().getPieceColor()){
                    if(xDown == capturedX && yRight == capturedY){
                        xDown += 1;
                        yRight += 1;
                        continue;
                    }
                    if(this.getSquare(xDown, yRight).getPiece() instanceof Bishop || this.getSquare(xDown, yRight).getPiece() instanceof Queen)
                        return false;
                    else
                        break;
                }
                else{
                    if(this.getSquare(xDown, yRight).getPiece() == currentPiece){
                        xDown += 1;
                        yRight += 1;
                        continue;
                    }
                    if(this.getSquare(xDown, yRight).getPiece() instanceof King){
                        xDown += 1;
                        yRight += 1;
                    }
                    else
                        break;
                }
            }
            else{
                xDown += 1;
                yRight += 1;
            }
        }

        xDown = x + 1;
        yLeft = y - 1;
        while(xDown < 8 && yLeft >= 0){
            if(xDown == movedX && yLeft == movedY)
                break;
            if(this.getSquare(xDown, yLeft).containsPiece()){
                if(color != this.getSquare(xDown, yLeft).getPiece().getPieceColor()){
                    if(xDown == capturedX && yLeft == capturedY){
                        xDown += 1;
                        yLeft -= 1;
                        continue;
                    }
                    if(this.getSquare(xDown, yLeft).getPiece() instanceof Bishop || this.getSquare(xDown, yLeft).getPiece() instanceof Queen)
                        return false;
                    else
                        break;
                }
                else{
                    if(this.getSquare(xDown, yLeft).getPiece() == currentPiece){
                        xDown += 1;
                        yLeft -= 1;
                        continue;
                    }
                    if(this.getSquare(xDown, yLeft).getPiece() instanceof King){
                        xDown += 1;
                        yLeft -= 1;
                    }
                    else
                        break;
                }
            }
            else{
                xDown += 1;
                yLeft -= 1;
            }
        }

        xUp = x - 1;
        yRight = y + 1;
        while(xUp >= 0 && yRight < 8){
            if(xUp == movedX && yRight == movedY)
                break;
            if(this.getSquare(xUp, yRight).containsPiece()){
                if(color != this.getSquare(xUp, yRight).getPiece().getPieceColor()){
                    if(xUp == capturedX && yRight == capturedY){
                        xUp -= 1;
                        yRight += 1;
                        continue;
                    }
                    if(this.getSquare(xUp, yRight).getPiece() instanceof Bishop || this.getSquare(xUp, yRight).getPiece() instanceof Queen)
                        return false;
                    else
                        break;
                }
                else{
                    if(this.getSquare(xUp, yRight).getPiece() == currentPiece){
                        xUp -= 1;
                        yRight += 1;
                        continue;
                    }
                    if(this.getSquare(xUp, yRight).getPiece() instanceof King){
                        xUp -= 1;
                        yRight += 1;
                    }
                    else
                        break;
                }
            }
            else{
                xUp -= 1;
                yRight += 1;
            }
        }

        xUp = x - 1;
        yLeft = y - 1;
        while(xUp >= 0 && yLeft >= 0){
            if(xUp == movedX && yLeft == movedY)
                break;
            if(this.getSquare(xUp, yLeft).containsPiece()){
                if(color != this.getSquare(xUp, yLeft).getPiece().getPieceColor()){
                    if(xUp == capturedX && yLeft == capturedY){
                        xUp -= 1;
                        yLeft -= 1;
                        continue;
                    }
                    if(this.getSquare(xUp, yLeft).getPiece() instanceof Bishop || this.getSquare(xUp, yLeft).getPiece() instanceof Queen)
                        return false;
                    else
                        break;
                }
                else{
                    if(this.getSquare(xUp, yLeft).getPiece() == currentPiece){
                        xUp -= 1;
                        yLeft -= 1;
                        continue;
                    }
                    if(this.getSquare(xUp, yLeft).getPiece() instanceof King){
                        xUp -= 1;
                        yLeft -= 1;
                    }
                    else
                        break;
                }
            }
            else{
                xUp -= 1;
                yLeft -= 1;
            }
        }

        for(int i = 0; i < 8; i++){
            int newX = x + knightX[i];
            int newY = y + knightY[i];
            if(newX == movedX && newY == movedY)
                continue;
            if(inBounds(newX, newY)){
                if(getSquare(newX, newY).containsPiece()){
                    if(getSquare(newX, newY).getPiece() instanceof Knight && getSquare(newX, newY).getPiece().getPieceColor() != color){
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
