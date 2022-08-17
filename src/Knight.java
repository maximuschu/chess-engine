public class Knight extends Piece{

    private static int[] X;
    private static int[] Y;
    public Knight(PieceColor color) {
        super(color);
        X = new int[]{2, 1, -1, -2, -2, -1, 1, 2};
        Y = new int[]{1, 2, 2, 1, -1, -2, -2, -1};
    }

    @Override
    public boolean validMove(int newX, int newY){
        for(int[] move : moves){
            if((newX == move[0]) && (newY == move[1])){
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
                    if(board.getSquare(newX, newY).getPiece().getPieceColor() != pieceColor && board.isSafe(pieceColor, this, newX, newY)){
                        moves.add(new int[]{newX, newY});
                    }
                }
                else{
                    if(board.isSafe(pieceColor, this, newX, newY))
                        moves.add(new int[]{newX, newY});
                }
            }
        }
        return moves.size();
    }
}
