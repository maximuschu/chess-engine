public class Square {
    private Piece piece;

    private final int x;
    private final int y;

    private int xScreen;
    private int yScreen;
    public Square(int x, int y, Piece piece){
        this.x = x;
        this.y = y;
        this.piece = piece;
        this.xScreen = (7-x)*Game.squareSize;
        this.yScreen = y*Game.squareSize;
    }

    public Piece getPiece(){
        return this.piece;
    }

    public void setPiece(Piece newPiece){
        this.piece = newPiece;
    }

    public void removePiece(){
        this.piece = null;
    }

    public boolean containsPiece(){
        return this.piece != null;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public int getXScreen(){
        return this.xScreen;
    }

    public int getYScreen(){
        return this.yScreen;
    }

    public void setXScreen(int x){
        this.xScreen = x;
    }

    public void setYScreen(int y){
        this.yScreen = y;
    }

    public void resetXScreen(){
        this.xScreen = (7-this.x)*Game.squareSize;
    }

    public void resetYScreen(){
        this.yScreen = this.y*Game.squareSize;
    }
}
