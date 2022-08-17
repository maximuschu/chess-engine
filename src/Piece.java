import java.util.ArrayList;

public abstract class Piece {
    protected PieceColor pieceColor;
    protected ArrayList<int[]> moves;

    public Piece(PieceColor color){
        this.pieceColor = color; this.moves = new ArrayList<>();
    }

    public PieceColor getPieceColor(){
        return pieceColor;
    }

    abstract int generatePossibleMoves(Board board, int x, int y);

    abstract boolean validMove(int newX, int newY);

    protected void down(Board board, int x, int y){
        int xDown = x + 1;
        while(xDown < 8){
            if(board.getSquare(xDown, y).containsPiece()){
                if(pieceColor != board.getSquare(xDown, y).getPiece().getPieceColor()){
                    if(board.isSafe(pieceColor, this, xDown, y))
                        moves.add(new int[]{xDown, y});
                }
                break;
            }
            else{
                if(board.isSafe(pieceColor, this, xDown, y))
                    moves.add(new int[]{xDown, y});
                xDown += 1;
            }
        }
    }

    protected void up(Board board, int x, int y){
        int xUp = x - 1;
        while(xUp >= 0){
            if(board.getSquare(xUp, y).containsPiece()){
                if(pieceColor != board.getSquare(xUp, y).getPiece().getPieceColor()){
                    if(board.isSafe(pieceColor, this, xUp, y))
                        moves.add(new int[]{xUp, y});
                }
                break;
            }
            else{
                if(board.isSafe(pieceColor, this, xUp, y))
                    moves.add(new int[]{xUp, y});
                xUp -= 1;
            }
        }
    }

    protected void right(Board board, int x, int y){
        int yRight = y + 1;
        while(yRight < 8){
            if(board.getSquare(x, yRight).containsPiece()){
                if(pieceColor != board.getSquare(x, yRight).getPiece().getPieceColor()){
                    if(board.isSafe(pieceColor, this, x, yRight))
                        moves.add(new int[]{x, yRight});
                }
                break;
            }
            else{
                if(board.isSafe(pieceColor, this, x, yRight))
                    moves.add(new int[]{x, yRight});
                yRight += 1;
            }
        }
    }

    protected void left(Board board, int x, int y){
        int yLeft = y - 1;
        while(yLeft >= 0){
            if(board.getSquare(x, yLeft).containsPiece()){
                if(pieceColor != board.getSquare(x, yLeft).getPiece().getPieceColor()){
                    if(board.isSafe(pieceColor, this, x, yLeft))
                        moves.add(new int[]{x, yLeft});
                }
                break;
            }
            else{
                if(board.isSafe(pieceColor, this, x, yLeft))
                    moves.add(new int[]{x, yLeft});
                yLeft -= 1;
            }
        }
    }

    protected void downRight(Board board, int x, int y){
        int xDown = x + 1;
        int yRight = y + 1;
        while(xDown < 8 && yRight < 8){
            if(board.getSquare(xDown, yRight).containsPiece()){
                if(pieceColor != board.getSquare(xDown, yRight).getPiece().getPieceColor()){
                    if(board.isSafe(pieceColor, this, xDown, yRight))
                        moves.add(new int[]{xDown, yRight});
                }
                break;
            }
            else{
                if(board.isSafe(pieceColor, this, xDown, yRight))
                    moves.add(new int[]{xDown, yRight});
                xDown += 1;
                yRight += 1;
            }
        }
    }

    protected void downLeft(Board board, int x, int y){
        int xDown = x + 1;
        int yLeft = y - 1;
        while(xDown < 8 && yLeft >= 0){
            if(board.getSquare(xDown, yLeft).containsPiece()){
                if(pieceColor != board.getSquare(xDown, yLeft).getPiece().getPieceColor()){
                    if(board.isSafe(pieceColor, this, xDown, yLeft))
                        moves.add(new int[]{xDown, yLeft});
                }
                break;
            }
            else{
                if(board.isSafe(pieceColor, this, xDown, yLeft))
                    moves.add(new int[]{xDown, yLeft});
                xDown += 1;
                yLeft -= 1;
            }
        }
    }

    protected void upRight(Board board, int x, int y){
        int xUp = x - 1;
        int yRight = y + 1;
        while(xUp >= 0 && yRight < 8){
            if(board.getSquare(xUp, yRight).containsPiece()){
                if(pieceColor != board.getSquare(xUp, yRight).getPiece().getPieceColor()){
                    if(board.isSafe(pieceColor, this, xUp, yRight))
                        moves.add(new int[]{xUp, yRight});
                }
                break;
            }
            else{
                if(board.isSafe(pieceColor, this, xUp, yRight))
                    moves.add(new int[]{xUp, yRight});
                xUp -= 1;
                yRight += 1;
            }
        }
    }

    protected void upLeft(Board board, int x, int y){
        int xUp = x - 1;
        int yLeft = y - 1;
        while(xUp >= 0 && yLeft >= 0){
            if(board.getSquare(xUp, yLeft).containsPiece()){
                if(pieceColor != board.getSquare(xUp, yLeft).getPiece().getPieceColor()){
                    if(board.isSafe(pieceColor, this, xUp, yLeft))
                        moves.add(new int[]{xUp, yLeft});
                }
                break;
            }
            else{
                if(board.isSafe(pieceColor, this, xUp, yLeft))
                    moves.add(new int[]{xUp, yLeft});
                xUp -= 1;
                yLeft -= 1;
            }
        }
    }
}
