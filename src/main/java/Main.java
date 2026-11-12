import javax.swing.*;
import java.awt.*;

/**
 * Drawing app with mouse and MCP modes.
 *
 * @author Javier Goonzalez-Sanchez (javiergs)
 * @version 1.0
 */
public final class Main extends JFrame {

  public static void main(String[] args) {
    MCPHandler server = new MCPHandler();
    server.run();

    Main main = new Main();
    main.setTitle("MCP Paint");
    main.setResizable(false);
    main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    main.setLocationRelativeTo(null);
    main.pack();
    main.setVisible(true);
  }

  Main() {
    DrawingPanel drawingPanel = new DrawingPanel();
    setLayout(new GridLayout(1, 1));
    add(drawingPanel);
  }

}

