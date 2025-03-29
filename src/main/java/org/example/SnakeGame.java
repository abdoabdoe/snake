package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver)
        {
            gameLoop.stop();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (gameOver && e.getKeyCode() == KeyEvent.VK_ENTER) {
            resetGame();
        }
        if(e.getKeyCode() == KeyEvent.VK_UP && velocityY != -1 )
        {
            velocityX = 0;
            velocityY = -1;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1) {
            velocityX = 0;
            velocityY = 1;

        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT && velocityX!= -1) {
            velocityX = -1;
            velocityY = 0;

        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1) {
            velocityX = 1;
            velocityY = 0;

        }


    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    private static class tile{
        int x;
        int y;
        tile (int x, int y)
        {
            this.x = x;
            this.y = y;
        }
    }

    int boardWidth;
    int boardHeight;
    int tileSize = 25;
    tile snakeHead;
    tile food;
    Random random;
    int velocityX;
    int velocityY;
    ArrayList<tile> body;

    boolean gameOver = false;


    Timer gameLoop;


    SnakeGame(int boardWidth, int boardHeight)
    {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);

        snakeHead = new tile(5, 5);
        food = new tile(10, 10);
        random = new Random();
        placeFood();

        velocityX = 0;
        velocityY = 0;

        gameLoop = new Timer(100, this);
        gameLoop.start();
        body = new ArrayList<tile>();

    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g)
    {
        for(int i = 0; i< boardWidth/tileSize; i++)
        {
            g.drawLine(i*tileSize,0, i*tileSize, boardHeight);
            g.drawLine(0, i*tileSize, boardWidth,i*tileSize);
        }

        g.setColor(Color.red);
        g.fillRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize);

        g.setColor(Color.green);
        g.fillRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize);

        for (int i = 0; i < body.size() ; i ++)
        {
            tile snakePart = body.get(i);
            g.fillRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize);
        }

        g.setFont(new Font("Arial", Font.PLAIN, 16));
        if (gameOver)
        {
            if (gameOver) {
                g.setFont(new Font("Arial", Font.BOLD, 50));
                g.setColor(Color.WHITE);

                String gameOverText = "GAME OVER!";
                String restartText = "Press ENTER to Restart";

                FontMetrics gameOverMetrics = g.getFontMetrics(g.getFont());
                int gameOverX = (boardWidth - gameOverMetrics.stringWidth(gameOverText)) / 2;
                int gameOverY = boardHeight / 2 - 20;  // Centering it properly

                g.drawString(gameOverText, gameOverX, gameOverY);

                // Change font for restart text
                g.setFont(new Font("Arial", Font.BOLD, 20));
                FontMetrics restartMetrics = g.getFontMetrics(g.getFont());

                int restartX = (boardWidth - restartMetrics.stringWidth(restartText)) / 2;
                int restartY = gameOverY + gameOverMetrics.getHeight() + 50;  // Adjusted positioning

                g.drawString(restartText, restartX, restartY);
            }

        }


    }
    public void placeFood()
    {
        food.x = random.nextInt(boardWidth/tileSize);
        food.y = random.nextInt(boardHeight/tileSize);
    }

    public void move()
    {
        if(collision(snakeHead, food))
        {
            body.add(new tile(food.x, food.y));
            placeFood();
        }

        for(int i = body.size() - 1; i>=0; i--)
        {
            tile snakePart = body.get(i);
            if (i == 0)
            {
               snakePart.x = snakeHead.x;
               snakePart.y = snakeHead.y;
            }
            else {
                tile prevSnakePart = body.get(i-1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        for(int i =0; i < body.size(); i ++)
        {

            tile snakePart = body.get(i);
            if (collision(snakeHead, snakePart))
            {
                gameOver = true;
            }
        }

        if(snakeHead.x*tileSize < 0 ||snakeHead.x*tileSize > boardWidth || snakeHead.y*tileSize < 0 ||snakeHead.x*tileSize > boardHeight)
        {
            gameOver = true;
        }

    }

    public boolean collision(tile tile1,tile tile2)
    {
        return tile1.x  == tile2.x && tile1.y == tile2.y;
    }
    public void resetGame() {
        snakeHead = new tile(5, 5);
        body.clear(); // Clear snake's body
        placeFood();  // Generate a new food position
        velocityX = 0;
        velocityY = 0;
        gameOver = false;
        gameLoop.start(); // Restart the game loop
        repaint();
    }

}
