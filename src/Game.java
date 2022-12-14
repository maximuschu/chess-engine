import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Game {
    private static Square currentSquare = null;
    private static PieceColor currentColor = PieceColor.WHITE;
    public static int squareSize = 64;
    private static boolean pawnPromotionMenu = false;
    private static int pawnPromotionY = 0; // Pawn promotion y to allow for promotion via capture to render correctly.
    private static boolean stalemate = false;
    private static boolean checkmate = false;
    private static ArrayList<int[]> allMoves = new ArrayList<>();

    public static void main(String[] args){
        try{
            BufferedImage all = ImageIO.read(new File("src/pieces.png"));
            Image[] piecesImages = new Image[12];
            int index = 0;
            for(int y = 0; y < 400; y += 200){
                for(int x = 0; x < 1200; x += 200){
                    piecesImages[index] = all.getSubimage(x, y, 200, 200).getScaledInstance(squareSize, squareSize, BufferedImage.SCALE_SMOOTH);
                    index++;
                }
            }
            BufferedImage closeImage = ImageIO.read(new File("src/close.png"));
            Image closeButton = closeImage.getScaledInstance(squareSize, squareSize, BufferedImage.SCALE_SMOOTH);
            newGame(piecesImages, closeButton);
        }
        catch(IOException e){
            System.out.println("Pieces images not found.");
        }
    }
    private static void newGame(Image[] piecesImages, Image closeButton){
        Board board = new Board();
        JFrame frame = new JFrame();
        Container c = frame.getContentPane();
        Dimension d = new Dimension(squareSize*8 + squareSize*4,squareSize*8);
        c.setPreferredSize(d);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
        frame.setBackground(new Color(229, 219, 214));
        drawBoard(piecesImages, closeButton, board, frame);
    }

    private static void resetBoard(Board board){
        currentColor = PieceColor.WHITE;
        board.initializeBoard();
        currentSquare = null;
        pawnPromotionMenu = false;
        stalemate = false;
        checkmate = false;
        boolean check = board.isCheck(currentColor);
        int totalMoves = board.generateValidMoves(currentColor);
    }

    private static void nextTurn(Board board){
        currentSquare.removePiece();
        currentColor = currentColor.toggle();
        boolean check = board.isCheck(currentColor);
        System.out.println(check);
        int totalMoves = board.generateValidMoves(currentColor);
        System.out.println(totalMoves);
        if(totalMoves == 0)
            determineResult(check);
        currentSquare = null;
        pawnPromotionMenu = false;
    }

    private static void resetTurn(Board board){
        boolean check = board.isCheck(currentColor);
        System.out.println(check);
        int totalMoves = board.generateValidMoves(currentColor);
        System.out.println(totalMoves);
        if(totalMoves == 0)
            determineResult(check);
        currentSquare = null;
        pawnPromotionMenu = false;
    }

    private static void drawBoard(Image[] piecesImages, Image closeButton, Board board, JFrame frame){
        JPanel pn = new JPanel(){
            @Override
            public void paint(Graphics g) {
                // Draw board squares.
                boolean white = true;
                for (int y = 0; y < 8; y++) {
                    for (int x = 0; x < 8; x++) {
                        if (white) {
                            g.setColor(new Color(227, 211, 175));
                        }
                        else {
                            g.setColor(new Color(163, 116, 90));
                        }
                        g.fillRect(x * squareSize, y * squareSize, squareSize, squareSize);
                        white = !white;

                    }
                    white = !white;
                }

                // Draw pieces other than currently selected piece.
                for(int y = 0; y < 8; y++){
                    for (int x = 0; x < 8; x++){
                        if(board.getSquare(x, y).getPiece() != null && board.getSquare(x, y) != currentSquare){
                            int index;
                            if(board.getSquare(x, y).getPiece() instanceof King)
                                index = 0;
                            else if(board.getSquare(x, y).getPiece() instanceof Queen)
                                index = 1;
                            else if(board.getSquare(x, y).getPiece() instanceof Bishop)
                                index = 2;
                            else if(board.getSquare(x, y).getPiece() instanceof Knight)
                                index = 3;
                            else if(board.getSquare(x, y).getPiece() instanceof Rook)
                                index = 4;
                            else
                                index = 5;
                            if(board.getSquare(x, y).getPiece().getPieceColor() == PieceColor.BLACK)
                                index += 6;
                            g.drawImage(piecesImages[index], board.getSquare(x, y).getYScreen(), board.getSquare(x, y).getXScreen(), this);
                        }
                    }
                }

                // Currently selected piece needs to be on top, drawn last.
                if(currentSquare != null && !pawnPromotionMenu){
                    int index;
                    if(currentSquare.getPiece() instanceof King)
                        index = 0;
                    else if(currentSquare.getPiece() instanceof Queen)
                        index = 1;
                    else if(currentSquare.getPiece() instanceof Bishop)
                        index = 2;
                    else if(currentSquare.getPiece() instanceof Knight)
                        index = 3;
                    else if(currentSquare.getPiece() instanceof Rook)
                        index = 4;
                    else
                        index = 5;
                    if(currentSquare.getPiece().getPieceColor() == PieceColor.BLACK)
                        index += 6;
                    g.drawImage(piecesImages[index], currentSquare.getYScreen(), currentSquare.getXScreen(), this);
                }

                // Draw pawn promotion selection choices.
                if(pawnPromotionMenu){
                    if(currentSquare != null){
                        int pawnPromote = pawnPromotionY;
                        // White promotion menu
                        if(currentSquare.containsPiece() && currentSquare.getPiece().getPieceColor() == PieceColor.WHITE){
                            g.setColor(new Color(255, 255, 255));
                            g.fillRect(pawnPromote * squareSize, 0, squareSize, squareSize); // Queen
                            g.drawImage(piecesImages[1], pawnPromote * squareSize, 0, this);
                            g.fillRect(pawnPromote * squareSize, squareSize, squareSize, squareSize); // Knight
                            g.drawImage(piecesImages[3], pawnPromote * squareSize, squareSize, this);
                            g.fillRect(pawnPromote * squareSize, 2 * squareSize, squareSize, squareSize); // Rook
                            g.drawImage(piecesImages[4], pawnPromote * squareSize, 2 * squareSize, this);
                            g.fillRect(pawnPromote * squareSize, 3 * squareSize, squareSize, squareSize); // Bishop
                            g.drawImage(piecesImages[2], pawnPromote * squareSize, 3 * squareSize, this);
                            g.setColor(new Color(239, 239, 239));
                            g.fillRect(pawnPromote * squareSize, 4 * squareSize, squareSize, squareSize); // Cancel
                            g.drawImage(closeButton, pawnPromote * squareSize, 4 * squareSize, this);
                        }

                        // Black promotion menu
                        else if(currentSquare.containsPiece() && currentSquare.getPiece().getPieceColor() == PieceColor.BLACK){
                            g.setColor(new Color(255, 255, 255));
                            g.fillRect(pawnPromote * squareSize, 7 * squareSize, squareSize, squareSize); // Queen
                            g.drawImage(piecesImages[7], pawnPromote * squareSize, 7 * squareSize, this);
                            g.fillRect(pawnPromote * squareSize, 6 * squareSize, squareSize, squareSize); // Knight
                            g.drawImage(piecesImages[9], pawnPromote * squareSize, 6 * squareSize, this);
                            g.fillRect(pawnPromote * squareSize, 5 * squareSize, squareSize, squareSize); // Rook
                            g.drawImage(piecesImages[10], pawnPromote * squareSize, 5 * squareSize, this);
                            g.fillRect(pawnPromote * squareSize, 4 * squareSize, squareSize, squareSize); // Bishop
                            g.drawImage(piecesImages[8], pawnPromote * squareSize, 4 * squareSize, this);
                            g.setColor(new Color(239, 239, 239));
                            g.fillRect(pawnPromote * squareSize, 3 * squareSize, squareSize, squareSize); // Cancel
                            g.drawImage(closeButton, pawnPromote * squareSize, 3 * squareSize, this);
                        }
                    }
                }
                if(stalemate || checkmate){
                    //g.setColor(new Color(0, 0, 0));
                    //g.drawRoundRect(2*squareSize,3*squareSize,4*squareSize,2*squareSize,50,50);
                    g.setColor(new Color(234, 234, 234));
                    g.fillRoundRect(2*squareSize,3*squareSize,4*squareSize,2*squareSize,50,50);
                    g.setColor(new Color(0, 0, 0));
                    g.setFont(new Font("TimesRoman", Font.PLAIN, 25));
                    if(stalemate){
                        g.drawString("Stalemate", 3*squareSize,4*squareSize);
                    }
                    else{
                        g.drawString("Checkmate", 3*squareSize,4*squareSize);
                    }
                }
            }
        };


        resetTurn(board);
        frame.add(pn);
        pn.addMouseMotionListener(new MouseMotionListener(){
            @Override
            public void mouseDragged(MouseEvent e){
                // Only allow dragging if not on pawn promotion menu.
                if(!pawnPromotionMenu && currentSquare != null){
                    currentSquare.setXScreen(e.getY() - squareSize/2);
                    currentSquare.setYScreen(e.getX() - squareSize/2);
                    frame.repaint();
                }
            }

            @Override
            public void mouseMoved(MouseEvent e){
                // None
            }
        });
        pn.addMouseListener(new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent e){
                // Add click movement, if currentSquare null set as current piece if piece in selected coordinates,
                // otherwise move piece if possible.
                //resetBoard(board);
            }

            @Override
            public void mousePressed(MouseEvent e){
                // Set currently selected piece.
                if(!pawnPromotionMenu){
                    currentSquare = getPiece(e.getX(), e.getY(), board);
                }

                // Determine what square was pressed for pawn promotion selection.
                else{
                    Square selection = getSquare(e.getX(), e.getY(), board);
                    // White pawn promotion
                    if(currentSquare.getPiece().getPieceColor() == PieceColor.WHITE){
                        if(selection.getX() == 7 && selection.getY() == pawnPromotionY){
                            board.getSquare(7, pawnPromotionY).setPiece(new Queen(PieceColor.WHITE));
                            nextTurn(board);
                        }
                        else if(selection.getX() == 6 && selection.getY() == pawnPromotionY){
                            board.getSquare(7, pawnPromotionY).setPiece(new Knight(PieceColor.WHITE));
                            nextTurn(board);
                        }
                        else if(selection.getX() == 5 && selection.getY() == pawnPromotionY){
                            board.getSquare(7, pawnPromotionY).setPiece(new Rook(PieceColor.WHITE));
                            nextTurn(board);
                        }
                        else if(selection.getX() == 4 && selection.getY() == pawnPromotionY){
                            board.getSquare(7, pawnPromotionY).setPiece(new Bishop(PieceColor.WHITE));
                            nextTurn(board);
                        }
                        else{
                            resetTurn(board);
                        }
                    }
                    // Black pawn promotion
                    else{
                        if(selection.getX() == 0 && selection.getY() == pawnPromotionY){
                            board.getSquare(0, pawnPromotionY).setPiece(new Queen(PieceColor.BLACK));
                            nextTurn(board);
                        }
                        else if(selection.getX() == 1 && selection.getY() == pawnPromotionY){
                            board.getSquare(0, pawnPromotionY).setPiece(new Knight(PieceColor.BLACK));
                            nextTurn(board);
                        }
                        else if(selection.getX() == 2 && selection.getY() == pawnPromotionY){
                            board.getSquare(0, pawnPromotionY).setPiece(new Rook(PieceColor.BLACK));
                            nextTurn(board);
                        }
                        else if(selection.getX() == 3 && selection.getY() == pawnPromotionY){
                            board.getSquare(0, pawnPromotionY).setPiece(new Bishop(PieceColor.BLACK));
                            nextTurn(board);
                        }
                        else{
                            resetBoard(board);
                        }
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if(currentSquare!= null) {
                    int newY = (e.getX() / squareSize);
                    int newX = (e.getY() / squareSize);
                    currentSquare.resetXScreen();
                    currentSquare.resetYScreen();
                    // Check in bounds, check if piece matches current player, ensures at least one coordinate is different to be a potential move,
                    // checks if potential spot is either empty or contains enemy piece, checks if move is a valid move.
                    if(board.inBounds((7 - newX), newY) && currentSquare.getPiece().getPieceColor() == currentColor && (((7 - newX) != currentSquare.getX()) || (newY != currentSquare.getY())) &&
                            (board.getSquare((7 - newX), newY).getPiece() == null || board.getSquare((7 - newX), newY).getPiece().getPieceColor() != currentColor) && currentSquare.getPiece().validMove((7 - newX), newY)){
                        // If current piece is king need to check if castling was executed.
                        if(currentSquare.getPiece() instanceof King){
                            if(newY - currentSquare.getY() == 2){
                                Piece temp = board.getSquare((7 - newX), currentSquare.getY() + 3).getPiece();
                                board.getSquare((7 - newX), currentSquare.getY() + 3).removePiece();
                                board.getSquare((7 - newX), currentSquare.getY() + 1).setPiece(temp);
                            }
                            if(newY - currentSquare.getY() == -2){
                                Piece temp = board.getSquare((7 - newX), currentSquare.getY() - 4).getPiece();
                                board.getSquare((7 - newX), currentSquare.getY() - 4).removePiece();
                                board.getSquare((7 - newX), currentSquare.getY() - 1).setPiece(temp);
                            }
                            board.updateKing(currentColor, (7 - newX), newY);
                        }

                        // If current piece is pawn needs to check for en passant and pawn promotion.
                        if(currentSquare.getPiece() instanceof Pawn){
                            if(currentSquare.getPiece().getPieceColor() == PieceColor.WHITE){
                                // Pawn promotion
                                if((7 - newX) == 7){
                                    pawnPromotionMenu = true;
                                    pawnPromotionY = newY;
                                }
                                // White enable en passant
                                if(currentSquare.getX() == 1 && (7 - newX) == 3){
                                    int leftY = currentSquare.getY() - 1;
                                    int rightY = currentSquare.getY() + 1;
                                    if(leftY >= 0 && board.getSquare((7 - newX), leftY).containsPiece() && board.getSquare((7 - newX), leftY).getPiece() instanceof Pawn && board.getSquare((7 - newX), leftY).getPiece().getPieceColor() == PieceColor.BLACK){
                                        ((Pawn) board.getSquare((7 - newX), leftY).getPiece()).enableEnPassantLeft();
                                    }
                                    if(rightY < 8 && board.getSquare((7 - newX), rightY).containsPiece() && board.getSquare((7 - newX), rightY).getPiece() instanceof Pawn && board.getSquare((7 - newX), rightY).getPiece().getPieceColor() == PieceColor.BLACK){
                                        ((Pawn) board.getSquare((7 - newX), rightY).getPiece()).enableEnPassantRight();
                                    }
                                }
                            }
                            else{
                                // Pawn promotion.
                                if((7 - newX) == 0){
                                    pawnPromotionMenu = true;
                                    pawnPromotionY = newY;
                                }
                                // Black enable en passant.
                                if(currentSquare.getX() == 6 && (7 - newX) == 4){
                                    int leftY = currentSquare.getY() - 1;
                                    int rightY = currentSquare.getY() + 1;
                                    if(leftY >= 0 && board.getSquare((7 - newX), leftY).containsPiece() && board.getSquare((7 - newX), leftY).getPiece() instanceof Pawn && board.getSquare((7 - newX), leftY).getPiece().getPieceColor() == PieceColor.WHITE){
                                        ((Pawn) board.getSquare((7 - newX), leftY).getPiece()).enableEnPassantRight();
                                    }
                                    if(rightY < 8 && board.getSquare((7 - newX), rightY).containsPiece() && board.getSquare((7 - newX), rightY).getPiece() instanceof Pawn && board.getSquare((7 - newX), rightY).getPiece().getPieceColor() == PieceColor.WHITE){
                                        ((Pawn) board.getSquare((7 - newX), rightY).getPiece()).enableEnPassantLeft();
                                    }
                                }
                            }

                            // Remove piece affected by en passant.
                            if(currentSquare.getY() != newY && !board.getSquare((7 - newX), newY).containsPiece()){
                                board.getSquare(currentSquare.getX(), newY).removePiece();
                            }
                        }

                        // Only update if pawn promotion not executed.
                        if(!pawnPromotionMenu){
                            board.getSquare((7 - newX), newY).setPiece(currentSquare.getPiece());
                            nextTurn(board);
                        }
                    }
                }
                frame.repaint();
            }

            @Override
            public void mouseEntered(MouseEvent e){
                // None
            }

            @Override
            public void mouseExited(MouseEvent e){
                // None
            }
        });
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    // Returns the selected square if it contains a piece, otherwise returns null.
    private static Square getPiece(int x, int y, Board board){
        int xp = x/squareSize;
        int yp = 7-(y/squareSize);
        if(!board.inBounds(yp, xp) || board.getSquare(yp, xp).getPiece() == null)
            return null;
        else{
            return board.getSquare(yp, xp);
        }
    }

    // Returns the selected square regardless of whether it contains a piece.
    private static Square getSquare(int x, int y, Board board){
        int xp = x/squareSize;
        int yp = 7-(y/squareSize);
        return board.getSquare(yp, xp);
    }

    // Use to display winner by checking current color.
    // public static boolean checkKing(PieceColor color, Board board)

    // Determine whether game ends in a checkmate or stalemate.
    private static void determineResult(boolean check){
        if(!check){
            stalemate = true;
        }
        else{
            checkmate = true;
        }
    }
}


