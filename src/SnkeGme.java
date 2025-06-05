import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class SnkeGme extends JPanel implements ActionListener ,KeyListener
{
    private class Tile
    {
        private int x_, y_;
        Tile(int x, int y)
        {
            this.x_ = x;
            this.y_ = y;

        }
    }

    private int boardWidth_;
    private int boardHeight_;
    private int tileSize_ = 25;

    private int velX = 1;
    private int velY = 0;

    private Tile snkeHead_;
    private ArrayList<Tile> snkebody_;
    private Tile food_;
    Random random_;
    Timer gameLoop_;
    boolean isColliBody, isColliWall ;

    SnkeGme(int boardWidth, int boardHeight)
    {
        this.boardWidth_ = boardWidth;
        this.boardHeight_ = boardHeight;

        setPreferredSize(new Dimension(this.boardWidth_, this.boardHeight_));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);

        this.snkeHead_ = new Tile(5, 5);
        this.snkebody_ = new ArrayList<Tile>();

        random_ = new Random();
//        this.food_ = new Tile(random_.nextInt(boardWidth_/tileSize_),
//                random_.nextInt(boardWidth_/tileSize_));
        this.food_ = new Tile(0, 0);
        placeFood();

        gameLoop_ =  new Timer(100, this);
        gameLoop_.start();
    }

    private void draw(Graphics g)
    {
        //Grid
//        for (int i = 0; i < boardWidth_/tileSize_; i++)
//        {
//            g.drawLine(i * this.tileSize_, 0, i * this.tileSize_, this.boardHeight_);
//            g.drawLine(0, i * this.tileSize_, this.boardWidth_, i * this.tileSize_);
//        }
        // Snke
        g.setColor(Color.GREEN);
//        g.fillRect(this.snkeHead_.x_ * tileSize_, this.snkeHead_.y_ * tileSize_, tileSize_, tileSize_);
        g.fill3DRect(this.snkeHead_.x_ * tileSize_, this.snkeHead_.y_ * tileSize_, tileSize_, tileSize_, true);

        //Body
        for (Tile snkePart : snkebody_) {
            g.setColor(Color.GREEN);
//            g.fillRect(snkePart.x_ * tileSize_, snkePart.y_ * tileSize_, tileSize_, tileSize_);
            g.fill3DRect(snkePart.x_ * tileSize_, snkePart.y_ * tileSize_, tileSize_, tileSize_, true);
        }

        // Food
        g.setColor(Color.RED);
//        g.fillRect(this.food_.x_ * tileSize_, this.food_.y_ * tileSize_, tileSize_, tileSize_);
        g.fill3DRect(this.food_.x_ * tileSize_, this.food_.y_ * tileSize_, tileSize_, tileSize_, true);

        //Score
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        if (isColliBody || isColliWall)
        {
            g.setColor(Color.RED);
            g.drawString("Gamer Over: " + String.valueOf(snkebody_.size()), tileSize_ - 16, tileSize_);
        }
        else
        {
            g.setColor(Color.BLUE);
            g.drawString("Score: " + String.valueOf(snkebody_.size()), tileSize_ - 16, tileSize_);
        }


    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        draw(g);
    }

    void reset()
    {
        if (gameLoop_.isRunning())
        {
            return;
        }
        else
        {
            isColliWall = false;
            isColliBody = false;

            snkeHead_.x_ = 5;
            snkeHead_.y_ = 5;

            placeFood();
            snkebody_.clear();
            gameLoop_.start();
        }


    }
    public boolean colision(Tile tile1, Tile tile2)
    {
        boolean isCollision;
        isCollision = (tile1.x_ == tile2.x_) && (tile1.y_ == tile2.y_);
        return isCollision;
    }
    public void move()
    {
        // eating event
        if (colision(snkeHead_, food_))
        {
            snkebody_.add(new Tile(food_.x_, food_.y_));
            placeFood();
        }

        for (int i = snkebody_.size() - 1; i >= 0; i--)
        {
            Tile currPart = snkebody_.get(i);
            if (i == 0)
            {
                currPart.x_ = snkeHead_.x_;
                currPart.y_ = snkeHead_.y_;
            }
            else
            {
                Tile prevPart = snkebody_.get(i -1);
                currPart.x_ = prevPart.x_;
                currPart.y_ = prevPart.y_;
            }
        }
        snkeHead_.x_ += velX;
        snkeHead_.y_ += velY;

        for (Tile snkePart: snkebody_)
        {
            if (colision(snkePart, snkeHead_))
            {
                isColliBody = true;
            }
        }

        if (snkeHead_.x_ * tileSize_ < 0 || snkeHead_.x_ * tileSize_ > boardWidth_
        || snkeHead_.y_ * tileSize_ < 0 || snkeHead_.y_ * tileSize_ > boardWidth_)
        {
            isColliWall = true;
        }
    }

    public void placeFood()
    {
        this.food_.x_ = random_.nextInt(boardWidth_/tileSize_);
        this.food_.y_ = random_.nextInt(boardWidth_/tileSize_);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode())
        {
            case KeyEvent.VK_UP:
                if (velY == 1) {break;}
                velX = 0;
                velY = -1;
                break;
            case KeyEvent.VK_DOWN:
                if (velY == -1) {break;}
                velX = 0;
                velY = 1;
                break;
            case KeyEvent.VK_LEFT:
                if (velX == 1) {break;}
                velX = -1;
                velY = 0;
                break;
            case KeyEvent.VK_RIGHT:
                if (velX == -1) {break;}
                velX = 1;
                velY = 0;
                break;
            case KeyEvent.VK_SPACE:
                reset();
                break;
            default:
                velX = 0;
                velY = 0;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(isColliBody || isColliWall)
        {
            gameLoop_.stop();
            System.out.println("Game over!!!");
        }

    }
}
