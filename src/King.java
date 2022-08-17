public class King extends Piece{
    private static int[] X;
    private static int[] Y;
    private boolean moved;
    public King(PieceColor color){
        super(color);
        X = new int[]{-1, -1,-1, 0, 1, 1, 1, 0};
        Y = new int[]{-1, 0, 1, 1, 1, 0, -1, -1};
        moved = false;
    }

    @Override
    public boolean validMove(int newX, int newY) {
        for(int[] move : moves){
            if((newX == move[0]) && (newY == move[1])){
                this.moved = true;
                return true;
            }
        }
        return false;
    }

    @Override
    public int generatePossibleMoves(Board board, int x, int y){
        moves.clear();
        for(int i = 0; i < 8; i++){
            int newX = x + X[i];
            int newY = y + Y[i];
            if(board.inBounds(newX, newY)){
                if(board.getSquare(newX, newY).containsPiece()){
                    if(board.getSquare(newX, newY).getPiece().getPieceColor() != pieceColor && board.isSafe(pieceColor, newX, newY)){
                        moves.add(new int[]{newX, newY});
                    }
                }
                else{
                    if(board.isSafe(pieceColor, newX, newY)){
                        moves.add(new int[]{newX, newY});
                    }
                }
            }
        }

        // Castling
        if(!moved && !board.isCheck(pieceColor)){
            if(!board.getSquare(x, y+1).containsPiece() && !board.getSquare(x, y+2).containsPiece() && board.getSquare(x, y+3).containsPiece() && (board.getSquare(x, y+3).getPiece() instanceof Rook) && ((Rook) board.getSquare(x, y + 3).getPiece()).hasNotMoved()){
                if(board.isSafe(pieceColor, x, y + 1) && board.isSafe(pieceColor, x, y + 2)){
                    moves.add(new int[]{x, y+2});
                }
            }
            if(!board.getSquare(x, y-1).containsPiece() && !board.getSquare(x, y-2).containsPiece() && !board.getSquare(x, y-3).containsPiece() && board.getSquare(x, y-4).containsPiece() && (board.getSquare(x, y-4).getPiece() instanceof Rook) && ((Rook) board.getSquare(x, y - 4).getPiece()).hasNotMoved()){
                if(board.isSafe(pieceColor, x, y - 1) && board.isSafe(pieceColor, x, y - 2) && board.isSafe(pieceColor, x, y - 3)){
                    moves.add(new int[]{x, y-2});
                }
            }
        }
        return moves.size();
    }
}
