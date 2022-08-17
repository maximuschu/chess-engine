public class Rook extends Piece{
    private boolean moved;
    public Rook(PieceColor color) {
        super(color);
        moved = false;
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
        down(board, x, y);
        up(board, x, y);
        right(board, x, y);
        left(board, x, y);
        return moves.size();
    }

    protected boolean hasNotMoved(){
        return !this.moved;
    }
}
