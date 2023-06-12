package checkerBoard;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
public class TimerActionListener {
    public void actionPerformed(ActionEvent e) {

    }

    public void setPlayerTurn(int playerTurn,Timer player1Timer,Timer player2Timer) {
        if (playerTurn == 1) {
            player1Timer.start();
            player2Timer.stop();
        } else if (playerTurn == 2) {
            player1Timer.stop();
            player2Timer.start();
        }
    }
}
