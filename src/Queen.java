public class Queen extends Piece{
    public Queen(PieceColor color){
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
        down(board, x, y);
        up(board, x, y);
        right(board, x, y);
        left(board, x, y);
        downRight(board, x, y);
        downLeft(board, x, y);
        upRight(board, x, y);
        upLeft(board, x, y);
        return moves.size();
    }
}
