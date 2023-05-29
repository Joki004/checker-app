package checkerBoard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;



public class CheckerGame {
    private JFrame frame;
    private JPanel boardPanel;
    private JButton[][] squares;
    private int[][] board;
    private int selectedRow = -1;
    private int selectedCol = -1;
    private int currentPlayer = 1;



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CheckerGame game = new CheckerGame();
            game.createAndShowGUI();
        });
    }

    public CheckerGame() {
        // Initialize the board
        board = new int[8][8];
        // 0 represents an empty square, 1 represents a red piece, 2 represents a black piece

        // Initialize the frame and board panel
        frame = new JFrame("Checker Game");
        boardPanel = new JPanel(new GridLayout(8, 8));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 700);

        // Initialize the squares array
        squares = new JButton[8][8];

        // Create the board
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                squares[row][col] = new JButton();
                squares[row][col].setBackground((row + col) % 2 == 0 ? Color.WHITE : Color.GRAY);
                if((row+col)%2==0){
                    board[row][col]=3;
                }
                squares[row][col].addActionListener(new SquareActionListener(row, col));
                boardPanel.add(squares[row][col]);
            }
        }

        // Add the board panel to the frame
        frame.add(boardPanel);
    }

    public void createAndShowGUI() {
        SwingUtilities.invokeLater(() -> {
            // Add red pawns
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 8; col++) {
                    if ((row + col) % 2 != 0) {
                        squares[row][col].setIcon(createIcon(Color.BLACK));
                        board[row][col] = 1; // Red piece
                    }
                }
            }

            // Add black pawns
            for (int row = 5; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    if ((row + col) % 2 != 0) {
                        squares[row][col].setIcon(createIcon(Color.RED));
                        board[row][col] = 2; // Black piece
                    }
                }
            }

            frame.setVisible(true);
        });
    }

    private Icon createIcon(Color color) {
        int width = 70;
        int height = 70;
        int borderSize = 10;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setColor(color);
        g.fillOval(borderSize, borderSize, width - borderSize * 2, height - borderSize * 2);
        g.dispose();
        return new ImageIcon(image);
    }



    private void resetHighlightedSquares() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                squares[row][col].setBackground((row + col) % 2 == 0 ? Color.WHITE : Color.GRAY);
            }
        }
    }


    private class SquareActionListener implements ActionListener {
        private int row;
        private int col;

        public SquareActionListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton clickedButton = (JButton) e.getSource();
            int clickedRow = -1;
            int clickedCol = -1;
            int selectionState  = 0;
            // Find the clicked button's position
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    if (squares[row][col] == clickedButton) {
                        clickedRow = row;
                        clickedCol = col;
                        break;
                    }
                }
                if (clickedRow != -1 && clickedCol != -1) {
                    break;
                }
            }

            if(board[clickedRow][clickedCol]==currentPlayer){

            }
            // If the clicked pawn is the currently selected pawn, deselect it
            if (selectedRow == clickedRow && selectedCol == clickedCol) {
                deselectPawn();
            } else {
                // If a pawn is already selected, confirm the move
                if (selectedRow != -1 && selectedCol != -1 && board[clickedRow][clickedCol]==0) {
                    validateMove(selectedRow, selectedCol, clickedRow, clickedCol);
                }
                else if (selectedRow != -1 && selectedCol != -1 &&
                        board[clickedRow][clickedCol]==currentPlayer){
                    //System.out.println(board[clickedRow][clickedCol] + " = "+currentPlayer);


                    selectPawn(clickedRow, clickedCol);
                }
                    else {
                    // No pawn is currently selected, so select the clicked pawn
                   selectionState =  selectPawn(clickedRow, clickedCol);
                   if(selectionState==-1)deselectPawn();
                }
            }
        }

        private void deselectPawn() {
            resetHighlightedSquares();
            selectedRow = -1;
            selectedCol = -1;
        }


        private void movePawn(int currentRow, int currentCol, int targetRow, int targetCol) {

            int pawn = board[currentRow][currentCol];



          //  System.out.println(pawn);
            if(pawn==currentPlayer){
                // Move the pawn in the board array
                board[currentRow][currentCol] = 0;
                board[targetRow][targetCol] = pawn;
                // Move the pawn in the GUI
                Icon pawnIcon = squares[currentRow][currentCol].getIcon();
                squares[targetRow][targetCol].setIcon(pawnIcon);
                squares[currentRow][currentCol].setIcon(null);
                currentPlayer = currentPlayer == 1 ? 2 : 1;
            }

        }
        private int selectPawn(int row, int col) {
            // Check if the clicked square contains a pawn of the current player's color
            int pawnColor = board[row][col];
            int flag = 0;
            if (pawnColor == 1 || pawnColor == 2) {
                // Reset the highlighted squares
                resetHighlightedSquares();
                int leftTargetCol = col - 1;
                int rightTargetCol = col + 1;
                // Highlight available squares for movement
                if (pawnColor == 1 && currentPlayer ==1) { // Red pawn
                    // Implement the logic to highlight available squares for red pawns
                   int targetRow = row+1;
                    if (targetRow < 8) {
                        flag = getFlag(flag, targetRow, leftTargetCol, rightTargetCol);
                    }
                } else if (pawnColor == 2 && currentPlayer ==2) { // Black pawn
                    // Implement the logic to highlight available squares for black pawns
                    int targetRow = row-1;
                    if (targetRow >= 0) {
                        flag = getFlag(flag, targetRow, leftTargetCol, rightTargetCol);
                    }
                }

                // Update the selected pawn coordinates
                selectedRow = row;
                selectedCol = col;
            }
            if(flag>0)return 0;
            else {

                selectedRow = -1;
                selectedCol = -1;
                resetHighlightedSquares();
                return -1;
            }
        }

        private int getFlag(int flag, int targetRow, int leftTargetCol, int rightTargetCol) {

            boolean possibilityToTake=false;
            boolean pawnAroundLeft = false;
            boolean pawnAroundRight = false;
            int otherPlayer;
            if(currentPlayer==1)otherPlayer=2;
            else otherPlayer=1;

            System.out.println("Current Player : "+currentPlayer);
            System.out.println("otherPlayer : "+ otherPlayer);
            if(currentPlayer==1){
                if (leftTargetCol >= 0 && board[targetRow][leftTargetCol] == otherPlayer ) {
                   pawnAroundLeft=true;
                }
                if (rightTargetCol < 8 && board[targetRow][rightTargetCol] == otherPlayer ) {
                   pawnAroundRight=true;
                }
                if(pawnAroundLeft){
                    if (board[targetRow][leftTargetCol] != currentPlayer && leftTargetCol - 1 >= 0 && board[targetRow + 1][leftTargetCol - 1] == 0) {
                        squares[targetRow + 1][leftTargetCol - 1].setBackground(Color.GREEN);
                        possibilityToTake= true;
                        flag++;
                    }

                }
                if(pawnAroundRight){
                    if (board[targetRow][rightTargetCol] != currentPlayer && rightTargetCol + 1 < 8 && board[targetRow + 1][rightTargetCol + 1] == 0) {
                        squares[targetRow + 1][rightTargetCol + 1].setBackground(Color.GREEN);
                        possibilityToTake= true;
                        flag++;
                    }
                }
                flag += displayPossibleMovement(flag, targetRow, leftTargetCol, rightTargetCol, possibilityToTake);


            }
            else if(currentPlayer==2){
                if (leftTargetCol >= 0 && board[targetRow][leftTargetCol] == 1 ) {
                    pawnAroundLeft=true;
                }
                if (rightTargetCol < 8 && board[targetRow][rightTargetCol] == 1 ) {
                    pawnAroundRight=true;
                }
                if(pawnAroundLeft){
                    if (board[targetRow][leftTargetCol] != currentPlayer && leftTargetCol - 1 >= 0 && board[targetRow - 1][leftTargetCol - 1] == 0) {
                        squares[targetRow - 1][leftTargetCol - 1].setBackground(Color.GREEN);
                        possibilityToTake= true;
                        flag++;
                    }

                }

                if(pawnAroundRight){
                    if (board[targetRow][rightTargetCol] != currentPlayer && rightTargetCol + 1 < 8 && board[targetRow - 1][rightTargetCol + 1] == 0) {
                        squares[targetRow - 1][rightTargetCol + 1].setBackground(Color.GREEN);
                        possibilityToTake= true;
                        flag++;
                    }
                }
                flag += displayPossibleMovement(flag, targetRow, leftTargetCol, rightTargetCol, possibilityToTake);
            }

            return flag;
        }

        private int displayPossibleMovement(int flag, int targetRow, int leftTargetCol, int rightTargetCol, boolean possibilityToTake) {
            if(!possibilityToTake){
                if (leftTargetCol >= 0 && board[targetRow][leftTargetCol] == 0) {
                    squares[targetRow][leftTargetCol].setBackground(Color.GREEN);
                    flag++;
                }
                if (rightTargetCol < 8 && board[targetRow][rightTargetCol] == 0) {
                    squares[targetRow][rightTargetCol].setBackground(Color.GREEN);
                    flag++;
                }
            }
            return flag;
        }


        private void validateMove(int fromRow, int fromCol, int toRow, int toCol) {
            // Check if the target square is a valid move for the selected pawn
            boolean isValidMove = false;

            // Check if the target square is a dark square
            if ((toRow + toCol) % 2 != 0) {

                // Check if the target square is in the forward direction
                if (currentPlayer == 1 && toRow > fromRow) {
                    // Check if the target square is diagonally to the right or left
                    if ((toCol == fromCol + 1 || toCol == fromCol - 1) && board[toRow][toCol] == 0) {
                        isValidMove = true;
                    } else if (toCol == fromCol + 2 && board[toRow][toCol] == 0 && board[fromRow + 1][fromCol + 1] == 2) {
                        // Check if the target square is a jump to the right
                        isValidMove = true;
                        int jumpedRow = fromRow + 1;
                        int jumpedCol = fromCol + 1;
                        removePiece(jumpedRow, jumpedCol);
                    } else if (toCol == fromCol - 2 && board[toRow][toCol] == 0 && board[fromRow + 1][fromCol - 1] == 2) {
                        // Check if the target square is a jump to the left
                        isValidMove = true;
                        int jumpedRow = fromRow + 1;
                        int jumpedCol = fromCol - 1;
                        removePiece(jumpedRow, jumpedCol);
                    }
                } else if (currentPlayer == 2 && toRow < fromRow) {
                    // Check if the target square is diagonally to the right or left
                    if ((toCol == fromCol + 1 || toCol == fromCol - 1) && board[toRow][toCol] == 0) {
                        isValidMove = true;
                    } else if (toCol == fromCol + 2 && board[toRow][toCol] == 0 && board[fromRow - 1][fromCol + 1] == 1) {
                        // Check if the target square is a jump to the right
                        isValidMove = true;
                        int jumpedRow = fromRow - 1;
                        int jumpedCol = fromCol + 1;
                        removePiece(jumpedRow, jumpedCol);
                    } else if (toCol == fromCol - 2 && board[toRow][toCol] == 0 && board[fromRow - 1][fromCol - 1] == 1) {
                        // Check if the target square is a jump to the left
                        isValidMove = true;
                        int jumpedRow = fromRow - 1;
                        int jumpedCol = fromCol - 1;
                        removePiece(jumpedRow, jumpedCol);
                    }
                }
            }

            if (isValidMove) {
                // Move the pawn
                movePawn(fromRow, fromCol, toRow, toCol);

                // Reset the selected pawn coordinates
                selectedRow = -1;
                selectedCol = -1;
                resetHighlightedSquares();
            } else {
                // Invalid move, show an error message or perform some other action
                // ...
            }
        }

        private void removePiece(int row, int col) {
            // Remove the piece from the board array
            board[row][col] = 0;

            // Remove the piece from the UI (e.g., update the button's appearance)
            JButton square = squares[row][col];
            square.setIcon(null);
        }



    }
}

