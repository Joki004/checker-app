package checkerBoard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Timer;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import Timer.TimerPanel;

public class CheckerGame {
    public static final int NUM_SQUARES_PER_LINE = 8;
    public static final int WIDTH = 1000;
    public static final int HEIGTH = 800;
    public static JLabel timerLabel1;
    public static JLabel timerLabel2;
    public static Timer player1Timer;
    public static Timer player2Timer;
    public static int player1Seconds = 0;
    public static int player2Seconds = 0;
    public static boolean isPlayer1Active = false;
    public static boolean isPlayer2Active = false;
    private JFrame frame;
    private JPanel boardPanel;
    private JButton[][] squares;
    private JLabel turnLabel;
    private JPanel gameStatus;
    private JPanel player1Panel;
    private JPanel player2Panel;

    private int[][] board;
    private int selectedRow = -1;
    private int selectedCol = -1;
    private int currentPlayer = 1;
    private TimerPanel timerPanel;

    public CheckerGame() {
        timerPanel = new TimerPanel();
        board = new int[NUM_SQUARES_PER_LINE][NUM_SQUARES_PER_LINE];
        // 0 represents an empty square, 1 represents a red piece, 2 represents a black piece
        frame = new JFrame("Checker Game");
        boardPanel = new JPanel(new GridLayout(NUM_SQUARES_PER_LINE, NUM_SQUARES_PER_LINE));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH, HEIGTH);

        createBoard();
        player1Timer = new Timer();
        player2Timer = new Timer();


        // Add the board panel to the frame
        frame.add(boardPanel);
        setGameStatus();

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CheckerGame game = new CheckerGame();
            game.createAndShowGUI();
        });
    }

    public Timer getplayer1Timer() {
        return player1Timer;
    }

    private void createBoard() {

        squares = new JButton[NUM_SQUARES_PER_LINE][NUM_SQUARES_PER_LINE];
        for (int row = 0; row < NUM_SQUARES_PER_LINE; row++) {
            for (int col = 0; col < NUM_SQUARES_PER_LINE; col++) {
                squares[row][col] = new JButton();
                squares[row][col].setBackground((row + col) % 2 == 0 ? Color.WHITE : Color.GRAY);
                if ((row + col) % 2 == 0) {
                    board[row][col] = 3;
                }
                squares[row][col].addActionListener(new SquareActionListener(row, col));
                boardPanel.add(squares[row][col]);
            }
        }
    }

    private void setGameStatus() {
        gameStatus = new JPanel();
        gameStatus.setLayout(new BoxLayout(gameStatus, BoxLayout.Y_AXIS));
        gameStatus.setBackground(Color.GRAY);
        gameStatus.setAlignmentX(Component.CENTER_ALIGNMENT);

        player1Panel = createPlayerPanel("Player 1");
        player2Panel = createPlayerPanel("Player 2");

        timerLabel1 = createTimerLabel();
        timerLabel2 = createTimerLabel();

        setPlayerTurn(currentPlayer);

        player1Panel.add(timerLabel1);
        player2Panel.add(timerLabel2);

        gameStatus.add(player1Panel);
        gameStatus.add(player2Panel);

        turnLabel = createTurnLabel("Player 1's Turn");

        frame.add(turnLabel, BorderLayout.SOUTH);
        frame.add(gameStatus, BorderLayout.EAST);
    }

    private JPanel createPlayerPanel(String playerName) {
        JPanel playerPanel = new JPanel();
        playerPanel.setBackground(Color.GRAY);
        playerPanel.setPreferredSize(new Dimension(100, 100));
        playerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel playerLabel = new JLabel(playerName);
        playerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        playerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        playerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        playerPanel.add(playerLabel);

        return playerPanel;
    }

    private JLabel createTimerLabel() {
        JLabel timerLabel = new JLabel("00:00:00");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        timerLabel.setVerticalAlignment(SwingConstants.CENTER);
        timerLabel.setPreferredSize(new Dimension(150, 50));
        timerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        return timerLabel;
    }

    private JLabel createTurnLabel(String turnText) {
        JLabel turnLabel = new JLabel(turnText);
        turnLabel.setHorizontalAlignment(SwingConstants.CENTER);
        turnLabel.setFont(new Font("Arial", Font.BOLD, 20));

        return turnLabel;
    }


    private void updateTurnLabel() {
        String player = currentPlayer == 1 ? "Player 1" : "Player 2";
        turnLabel.setText(player + "'s Turn");
    }

    public void setPlayerTurn(int playerTurn) {
        if (playerTurn == 1) {

            isPlayer2Active = false;
            player2Panel.setBackground(Color.GRAY);
            player1Panel.setBackground(Color.GREEN);
            isPlayer1Active = true;
            TimerPanel.startPlayer1Timer();

        } else if (playerTurn == 2) {
            player1Panel.setBackground(Color.GRAY);
            player2Panel.setBackground(Color.GREEN);
            isPlayer1Active = false;
            isPlayer2Active = true;
            TimerPanel.startPlayer2Timer();
        }
    }

    public void createAndShowGUI() {
        SwingUtilities.invokeLater(() -> {
            // Add red pawns
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < NUM_SQUARES_PER_LINE; col++) {
                    if ((row + col) % 2 != 0) {
                        squares[row][col].setIcon(createIcon(Color.BLACK));
                        board[row][col] = 1; // Red piece
                    }
                }
            }

            // Add black pawns
            for (int row = 5; row < NUM_SQUARES_PER_LINE; row++) {
                for (int col = 0; col < NUM_SQUARES_PER_LINE; col++) {
                    if ((row + col) % 2 != 0) {
                        squares[row][col].setIcon(createIcon(Color.RED));
                        board[row][col] = 2; // Black piece
                    }
                }
            }
            updateTurnLabel();
            frame.setVisible(true);
        });
    }

    private Icon createIcon(Color color) {
        int width = HEIGTH / 10;
        int height = HEIGTH / 10;
        int borderSize = 10;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setColor(color);
        g.fillOval(borderSize, borderSize, width - borderSize * 2, height - borderSize * 2);
        g.dispose();
        return new ImageIcon(image);
    }

    private void resetHighlightedSquares() {
        for (int row = 0; row < NUM_SQUARES_PER_LINE; row++) {
            for (int col = 0; col < NUM_SQUARES_PER_LINE; col++) {
                squares[row][col].setBackground((row + col) % 2 == 0 ? Color.WHITE : Color.GRAY);
            }
        }
    }

    private boolean isValidSquare(int row, int col) {
        return row >= 0 && row < NUM_SQUARES_PER_LINE && col >= 0 && col < NUM_SQUARES_PER_LINE;
    }

    private Icon getKingIcon(int pawn) {
        Color color;
        if (pawn == 1) {
            // Red king icon
            color = Color.BLACK;
            int width = HEIGTH/10;
            int height = HEIGTH/10;
            int borderSize = 10;
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = image.createGraphics();

            // Draw the circle border
            g.setColor(color);
            g.fillOval(borderSize, borderSize, width - borderSize * 2, height - borderSize * 2);

            // Draw the crown on top of the circle
            g.setColor(Color.YELLOW); // Change the crown color as needed
            int[] crownXPoints = {width / 2 - 20, width / 2, width / 2 + 20, width / 2};
            int[] crownYPoints = {height / 2 - 10, height / 2 - 20, height / 2 - 10, height / 2 - 5};
            g.fillPolygon(crownXPoints, crownYPoints, 4);

            g.dispose();
            return new ImageIcon(image);// Replace with the path to your red king icon image
        } else if (pawn == 2) {
            // Black king icon
            color = Color.RED;
            int width = 70;
            int height = 70;
            int borderSize = 10;
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = image.createGraphics();

            // Draw the circle border
            g.setColor(color);
            g.fillOval(borderSize, borderSize, width - borderSize * 2, height - borderSize * 2);

            // Draw the crown on top of the circle
            g.setColor(Color.YELLOW); // Change the crown color as needed
            int[] crownXPoints = {width / 2 - 20, width / 2, width / 2 + 20, width / 2};
            int[] crownYPoints = {height / 2 - 10, height / 2 - 20, height / 2 - 10, height / 2 - 5};
            g.fillPolygon(crownXPoints, crownYPoints, 4);

            g.dispose();
            return new ImageIcon(image); // Replace with the path to your black king icon image
        } else {
            return null;
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
            int selectionState = 0;
            // Find the clicked button's position
            for (int row = 0; row < NUM_SQUARES_PER_LINE; row++) {
                for (int col = 0; col < NUM_SQUARES_PER_LINE; col++) {
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

            if (board[clickedRow][clickedCol] == currentPlayer) {

            }
            // If the clicked pawn is the currently selected pawn, deselect it
            if (selectedRow == clickedRow && selectedCol == clickedCol) {
                deselectPawn();
            } else {
                // If a pawn is already selected, confirm the move
                if (selectedRow != -1 && selectedCol != -1 && board[clickedRow][clickedCol] == 0) {
                    validateMove(selectedRow, selectedCol, clickedRow, clickedCol);
                } else if (selectedRow != -1 && selectedCol != -1 &&
                        board[clickedRow][clickedCol] == currentPlayer) {
                    //System.out.println(board[clickedRow][clickedCol] + " = "+currentPlayer);
                    selectPawn(clickedRow, clickedCol);
                } else {
                    // No pawn is currently selected, so select the clicked pawn
                    selectionState = selectPawn(clickedRow, clickedCol);
                    if (selectionState == -1) deselectPawn();
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

            if (pawn == currentPlayer) {
                // Check if the move is valid
                if (isValidMove(currentRow, currentCol, targetRow, targetCol)) {
                    // Move the pawn in the board array
                    board[currentRow][currentCol] = 0;
                    board[targetRow][targetCol] = pawn;
                    // Move the pawn in the GUI
                    Icon pawnIcon = squares[currentRow][currentCol].getIcon();
                    squares[targetRow][targetCol].setIcon(pawnIcon);
                    squares[currentRow][currentCol].setIcon(null);

                    // Check if the pawn needs to be promoted to a king
                    if (shouldPromoteToKing(targetRow, targetCol)) {
                        promoteToKing(targetRow, targetCol);
                    }

                    // Check if capturing is possible and mandatory
                    if (canCapture(targetRow, targetCol)) {
                        // Remove the captured pawn
                        int capturedRow = (currentRow + targetRow) / 2;
                        int capturedCol = (currentCol + targetCol) / 2;
                        board[capturedRow][capturedCol] = 0;
                        squares[capturedRow][capturedCol].setIcon(null);

                        // Check if the capturing pawn can continue capturing
                        if (canContinueCapture(targetRow, targetCol)) {
                            // Update the selected pawn coordinates
                            selectedRow = targetRow;
                            selectedCol = targetCol;
                            return;
                        }
                    }

                    // Switch to the next player's turn
                    currentPlayer = currentPlayer == 1 ? 2 : 1;
                    setPlayerTurn(currentPlayer);
                    updateTurnLabel();
                }
            }
        }

        private boolean shouldPromoteToKing(int row, int col) {
            int pawn = board[row][col];
            if (pawn == 1 && row == NUM_SQUARES_PER_LINE - 1) {
                return true; // Red pawn reaches the last row
            }
            if (pawn == 2 && row == 0) {
                return true; // Blue pawn reaches the first row
            }
            return false;
        }

        private boolean isValidMove(int currentRow, int currentCol, int targetRow, int targetCol) {
            // Check if the target square is empty
            if (board[targetRow][targetCol] != 0) {
                return false;
            }

            // Check if the move is diagonal
            int rowDiff = Math.abs(targetRow - currentRow);
            int colDiff = Math.abs(targetCol - currentCol);
            if (rowDiff != colDiff) {
                return false;
            }

            // Check if the move is only one square in any direction
            if (rowDiff != 1) {
                // Check if it's a capture move
                if (rowDiff == 2 && colDiff == 2) {
                    int capturedPawnRow = (targetRow + currentRow) / 2;
                    int capturedPawnCol = (targetCol + currentCol) / 2;
                    int capturedPawn = board[capturedPawnRow][capturedPawnCol];

                    // Check if the captured pawn is of the opposite color
                    int pawn = board[currentRow][currentCol];
                    if (pawn == 1 && capturedPawn == 2) {
                        removePiece(capturedPawnRow,capturedPawnCol);
                        return true; // Valid capture move for red pawn
                    }
                    if (pawn == 2 && capturedPawn == 1) {
                        removePiece(capturedPawnRow,capturedPawnCol);
                        return true; // Valid capture move for black pawn
                    }
                }
                return false;
            }

            // Check if the move is in the correct direction for non-kings
            int pawn = board[currentRow][currentCol];
            if (pawn == 1 && targetRow < currentRow) {
                return false; // Red pawns can only move down
            }
            if (pawn == 2 && targetRow > currentRow) {
                return false; // Black pawns can only move up
            }

            return true;
        }

        private void promoteToKing(int row, int col) {
            int pawn = board[row][col];
            board[row][col] = pawn + 2; // Promote the pawn to king
            Icon kingIcon = getKingIcon(pawn);
            squares[row][col].setIcon(kingIcon);
        }

        private boolean canCapture(int row, int col) {
            int pawn = board[row][col];
            int opponentPawn = (pawn == 1) ? 2 : 1; // Determine the opponent's pawn

            // Check if capturing is possible in any direction
            boolean canCaptureUpLeft = isValidCapture(row, col, row - 1, col - 1, opponentPawn);
            boolean canCaptureUpRight = isValidCapture(row, col, row - 1, col + 1, opponentPawn);
            boolean canCaptureDownLeft = isValidCapture(row, col, row + 1, col - 1, opponentPawn);
            boolean canCaptureDownRight = isValidCapture(row, col, row + 1, col + 1, opponentPawn);

            // Return true if capturing is possible in any direction
            return canCaptureUpLeft || canCaptureUpRight || canCaptureDownLeft || canCaptureDownRight;
        }

        private boolean isValidCapture(int currentRow, int currentCol, int targetRow, int targetCol, int opponentPawn) {
            // Check if the target square is within the board boundaries
            if (!isValidSquare(targetRow, targetCol)) {
                return false;
            }

            // Check if the target square is occupied by an opponent's pawn
            if (board[targetRow][targetCol] != opponentPawn) {
                return false;
            }

            // Check if the capturing move is diagonal
            int rowDiff = Math.abs(targetRow - currentRow);
            int colDiff = Math.abs(targetCol - currentCol);
            if (rowDiff != colDiff) {
                return false;
            }

            // Check if the captured pawn can be replaced with the capturing pawn
            int capturedRow = (currentRow + targetRow) / 2;
            int capturedCol = (currentCol + targetCol) / 2;
            if (board[capturedRow][capturedCol] != 0) {
                return false;
            }

            return true;
        }

        private boolean canContinueCapture(int row, int col) {
            // Check if capturing is possible in any direction from the current position
            boolean canCaptureUpLeft = canCapture(row, col, row - 2, col - 2);
            boolean canCaptureUpRight = canCapture(row, col, row - 2, col + 2);
            boolean canCaptureDownLeft = canCapture(row, col, row + 2, col - 2);
            boolean canCaptureDownRight = canCapture(row, col, row + 2, col + 2);

            // Return true if capturing is possible in any direction
            return canCaptureUpLeft || canCaptureUpRight || canCaptureDownLeft || canCaptureDownRight;
        }

        private boolean canCapture(int currentRow, int currentCol, int targetRow, int targetCol) {
            // Check if the target square is within the board boundaries
            if (!isValidSquare(targetRow, targetCol)) {
                return false;
            }

            // Check if the target square is empty
            if (board[targetRow][targetCol] != 0) {
                return false;
            }

            // Check if the capturing move is diagonal
            int rowDiff = Math.abs(targetRow - currentRow);
            int colDiff = Math.abs(targetCol - currentCol);
            if (rowDiff != colDiff) {
                return false;
            }

            // Check if the captured pawn can be replaced with the capturing pawn
            int capturedRow = (currentRow + targetRow) / 2;
            int capturedCol = (currentCol + targetCol) / 2;
            int opponentPawn = (board[currentRow][currentCol] == 1) ? 2 : 1;
            if (board[capturedRow][capturedCol] != opponentPawn) {
                return false;
            }

            return true;
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
                if (pawnColor == 1 && currentPlayer == 1) { // Red pawn
                    // Implement the logic to highlight available squares for red pawns
                    int targetRow = row + 1;
                    if (targetRow < NUM_SQUARES_PER_LINE) {
                        flag = getFlag(flag, targetRow, leftTargetCol, rightTargetCol);
                    }
                } else if (pawnColor == 2 && currentPlayer == 2) { // Black pawn
                    // Implement the logic to highlight available squares for black pawns
                    int targetRow = row - 1;
                    if (targetRow >= 0) {
                        flag = getFlag(flag, targetRow, leftTargetCol, rightTargetCol);
                    }
                }

                // Update the selected pawn coordinates
                selectedRow = row;
                selectedCol = col;
            }
            if (flag > 0) return 0;
            else {

                selectedRow = -1;
                selectedCol = -1;
                resetHighlightedSquares();
                return -1;
            }
        }

        private int getFlag(int flag, int targetRow, int leftTargetCol, int rightTargetCol) {

            boolean possibilityToTake = false;
            boolean pawnAroundLeft = false;
            boolean pawnAroundRight = false;
            int otherPlayer;
            if (currentPlayer == 1) otherPlayer = 2;
            else otherPlayer = 1;


            if (currentPlayer == 1) {
                if (leftTargetCol >= 0 && board[targetRow][leftTargetCol] == otherPlayer) {
                    pawnAroundLeft = true;
                }
                if (rightTargetCol < NUM_SQUARES_PER_LINE && board[targetRow][rightTargetCol] == otherPlayer) {
                    pawnAroundRight = true;
                }
                if (pawnAroundLeft) {
                    if (board[targetRow][leftTargetCol] != currentPlayer && leftTargetCol - 1 >= 0 && board[targetRow + 1][leftTargetCol - 1] == 0) {
                        squares[targetRow + 1][leftTargetCol - 1].setBackground(Color.GREEN);
                        possibilityToTake = true;
                        flag++;

                    }

                }
                if (pawnAroundRight) {
                    if (board[targetRow][rightTargetCol] != currentPlayer && rightTargetCol + 1 < NUM_SQUARES_PER_LINE && board[targetRow + 1][rightTargetCol + 1] == 0) {
                        squares[targetRow + 1][rightTargetCol + 1].setBackground(Color.GREEN);
                        possibilityToTake = true;
                        flag++;
                    }
                }
                flag += displayPossibleMovement(flag, targetRow, leftTargetCol, rightTargetCol, possibilityToTake);


            } else if (currentPlayer == 2) {
                if (leftTargetCol >= 0 && board[targetRow][leftTargetCol] == 1) {
                    pawnAroundLeft = true;
                }
                if (rightTargetCol < NUM_SQUARES_PER_LINE && board[targetRow][rightTargetCol] == 1) {
                    pawnAroundRight = true;
                }
                if (pawnAroundLeft) {
                    if (targetRow - 1 != -1) {
                        if (board[targetRow][leftTargetCol] != currentPlayer && leftTargetCol - 1 >= 0 && board[targetRow - 1][leftTargetCol - 1] == 0) {
                            squares[targetRow - 1][leftTargetCol - 1].setBackground(Color.GREEN);
                            possibilityToTake = true;
                            flag++;
                        }
                    }


                }

                if (pawnAroundRight) {
                    if (targetRow - 1 != -1) {
                        if (board[targetRow][rightTargetCol] != currentPlayer && rightTargetCol + 1 < NUM_SQUARES_PER_LINE && board[targetRow - 1][rightTargetCol + 1] == 0) {
                            squares[targetRow - 1][rightTargetCol + 1].setBackground(Color.GREEN);
                            possibilityToTake = true;
                            flag++;
                        }
                    }

                }
                flag += displayPossibleMovement(flag, targetRow, leftTargetCol, rightTargetCol, possibilityToTake);
            }

            return flag;
        }

        private int displayPossibleMovement(int flag, int targetRow, int leftTargetCol, int rightTargetCol, boolean possibilityToTake) {
            if (!possibilityToTake) {
                if (leftTargetCol >= 0 && board[targetRow][leftTargetCol] == 0) {
                    squares[targetRow][leftTargetCol].setBackground(Color.GREEN);
                    flag++;
                }
                if (rightTargetCol < NUM_SQUARES_PER_LINE && board[targetRow][rightTargetCol] == 0) {
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

                    } else if (toCol == fromCol - 2 && board[toRow][toCol] == 0 && board[fromRow + 1][fromCol - 1] == 2) {
                        // Check if the target square is a jump to the left
                        isValidMove = true;
                        int jumpedRow = fromRow + 1;
                        int jumpedCol = fromCol - 1;

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

                    } else if (toCol == fromCol - 2 && board[toRow][toCol] == 0 && board[fromRow - 1][fromCol - 1] == 1) {
                        // Check if the target square is a jump to the left
                        isValidMove = true;
                        int jumpedRow = fromRow - 1;
                        int jumpedCol = fromCol - 1;

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

