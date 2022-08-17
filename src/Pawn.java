public class Pawn extends Piece{
    private boolean moved;
    private boolean enPassantLeft;
    private boolean enPassantRight;
    public Pawn(PieceColor color) {
        super(color);
        moved = false;
        enPassantLeft = false;
        enPassantRight = false;
    }

    @Override
    public boolean validMove(int newX, int newY) {
        for(int[] move : moves){
            if ((newX == move[0]) && (newY == move[1])) {
                this.moved = true;
                return true;
            }
        }
        return false;
    }

    @Override
    public int generatePossibleMoves(Board board, int x, int y){
        moves.clear();
        if(pieceColor == PieceColor.WHITE){
            if(x+1 < 8) {
                if(!board.getSquare(x+1, y).containsPiece() && board.isSafe(pieceColor, this, x + 1, y)){
                    moves.add(new int[]{x+1, y});
                }
                if(y-1 >= 0){
                    if(board.getSquare(x+1, y-1).containsPiece() && board.getSquare(x+1, y-1).getPiece().getPieceColor() != pieceColor && board.isSafe(pieceColor, this, x + 1, y - 1)){
                        moves.add(new int[]{x+1, y-1});
                    }
                }
                if(y+1 < 8){
                    if(board.getSquare(x+1, y+1).containsPiece() && board.getSquare(x+1, y+1).getPiece().getPieceColor() != pieceColor && board.isSafe(pieceColor, this, x + 1, y + 1)){
                        moves.add(new int[]{x+1, y+1});
                    }
                }
            }
            if(x+2 < 8){
                if(!moved && !board.getSquare(x + 2, y).containsPiece() && !board.getSquare(x+1, y).containsPiece() && board.isSafe(pieceColor, this, x + 2, y)){
                    moves.add(new int[]{x + 2, y});
                }
            }
            if(enPassantLeft){
                if(y-1 >= 0 && !board.getSquare(x+1, y-1).containsPiece() && board.isSafe(pieceColor, this, x + 1, y - 1, x, y - 1)){
                    moves.add(new int[]{x+1, y-1});
                }
                enPassantLeft = false;
            }
            if(enPassantRight){
                if(y+1 < 8 && !board.getSquare(x+1, y+1).containsPiece() && board.isSafe(pieceColor, this, x + 1, y + 1, x, y + 1)){
                    moves.add(new int[]{x+1, y+1});
                }
                enPassantRight = false;
            }
        }
        else{
            if(x-1 >= 0){
                if(!board.getSquare(x-1, y).containsPiece() && board.isSafe(pieceColor, this, x - 1, y)){
                    moves.add(new int[]{x-1, y});
                }
                if(y-1 >= 0){
                    if(board.getSquare(x-1, y-1).containsPiece() && board.getSquare(x-1, y-1).getPiece().getPieceColor() != pieceColor && board.isSafe(pieceColor, this, x - 1, y - 1)){
                        moves.add(new int[]{x-1, y-1});
                    }
                }
                if(y+1 < 8){
                    if(board.getSquare(x-1, y+1).containsPiece() && board.getSquare(x-1, y+1).getPiece().getPieceColor() != pieceColor && board.isSafe(pieceColor, this, x - 1, y + 1)){
                        moves.add(new int[]{x-1, y+1});
                    }
                }
            }
            if(x-2 >= 0){
                if(!moved && !board.getSquare(x-2, y).containsPiece() && !board.getSquare(x-1, y).containsPiece() && board.isSafe(pieceColor, this, x - 2, y)){
                    moves.add(new int[]{x-2, y});
                }
            }
            if(enPassantLeft){
                if(y+1 < 8 && !board.getSquare(x-1, y+1).containsPiece() && board.isSafe(pieceColor, this, x - 1, y + 1, x, y + 1)){
                    moves.add(new int[]{x-1, y+1});
                }
                enPassantLeft = false;
            }
            if(enPassantRight){
                if(y-1 >= 0 && !board.getSquare(x-1, y-1).containsPiece() && board.isSafe(pieceColor, this, x - 1, y - 1, x, y - 1)){
                    moves.add(new int[]{x-1, y-1});
                }
                enPassantRight = false;
            }
        }
        return moves.size();
    }

    public void enableEnPassantLeft(){
        enPassantLeft = true;
    }

    public void enableEnPassantRight(){
        enPassantRight = true;
    }
}
