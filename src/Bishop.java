public class Bishop extends Piece{
    public Bishop(PieceColor color) {
        super(color);
    }

    @Override
    public boolean validMove(int newX, int newY) {
        for(int[] move : moves){
            if ((newX == move[0]) && (newY == move[1])) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int generatePossibleMoves(Board board, int x, int y){
        moves.clear();
        downRight(board, x, y);
        downLeft(board, x, y);
        upRight(board, x, y);
        upLeft(board, x, y);
        return moves.size();
    }
}
