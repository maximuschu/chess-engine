public enum PieceColor {
    WHITE, BLACK;
    PieceColor toggle() {
        if (this.equals(BLACK))
            return WHITE;
        else
            return BLACK;
    }
}
