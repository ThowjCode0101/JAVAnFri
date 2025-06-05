import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception
    {
        int boardWidth = 600;  // Fixed typo in variable name
        //int boardHeight = boardWidth;  // Fixed typo in variable name

        JFrame frame = new JFrame("Snake Game");  // Added title
        frame.setVisible(false);  // Move this to after adding components
        frame.setSize(boardWidth, boardWidth);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SnkeGme snkeGme = new SnkeGme(boardWidth, boardWidth);
        frame.add(snkeGme);
        snkeGme.requestFocus();
        frame.pack();  // This is correct - it sizes the frame to fit components
        frame.setVisible(true);  // Moved to after all components are added
    }
}