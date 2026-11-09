import javax.swing.*;
import java.awt.*;

/**
 * Drawing app with mouse and MCP modes.
 *
 * @author Javier Goonzalez-Sanchez (javiergs)
 * @version 1.0
 */
public final class Main extends JFrame {

  public static void main(String[] args) throws Exception {
    // server
    MCPServer server = new MCPServer();
    server.run();
    // view
    Main main = new Main();
    main.setTitle("MCP Paint");
    main.setResizable(false);
    main.setLocationByPlatform(true);
    main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    main.pack();
    main.setVisible(true);
  }

  Main() {
    DrawingPanel drawingPanel = new DrawingPanel();
    DrawingMouseListener mouse = new DrawingMouseListener();
    // view
    setLayout(new GridLayout(1, 1));
    add(drawingPanel);
    // controller
    drawingPanel.addMouseListener(mouse);
    drawingPanel.addMouseMotionListener(mouse);
    Blackboard.getInstance().addPropertyChangeListener(drawingPanel);
  }

}
