import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * Displays the drawing state and cursor position.
 *
 * @author Javier Goonzalez-Sanchez (javiergs)
 * @version 1.0
 */
public class DrawingPanel extends JPanel implements PropertyChangeListener {

  public DrawingPanel() {
    setPreferredSize(new Dimension(Blackboard.MAX_X + 1, Blackboard.MAX_Y + 1));
    setBackground(new Color(0xFF8EB8F8, true));
    DrawingListener mouse = new DrawingListener();
    addMouseListener(mouse);
    addMouseMotionListener(mouse);
    Blackboard.getInstance().addPropertyChangeListener(this);
  }

  @Override
  public void propertyChange(PropertyChangeEvent event) {
    repaint();
  }

  @Override
  public void paintComponent(Graphics graphics) {
    super.paintComponent(graphics);
    // draw a grid
    graphics.setColor(new Color(0x6395FF));
    for (int x = 0; x <= Blackboard.MAX_X; x += 10)
      graphics.drawLine(x, 0, x, Blackboard.MAX_Y);
    for (int y = 0; y <= Blackboard.MAX_Y; y += 10)
      graphics.drawLine(0, y, Blackboard.MAX_X, y);
    // draw the strokes and cursor
    graphics.setColor(Color.BLACK);
    for (List<Point> stroke : Blackboard.getInstance().getStrokes()) {
      Point previous = stroke.get(0);
      graphics.fillRect(previous.x, Blackboard.MAX_Y - previous.y, 1, 1);
      for (Point point : stroke) {
        graphics.drawLine(previous.x, Blackboard.MAX_Y - previous.y, point.x, Blackboard.MAX_Y - point.y);
        previous = point;
      }
    }
    Point cursor = Blackboard.getInstance().getPosition();
    graphics.setColor(Blackboard.getInstance().isPenDown() ? Color.RED : Color.BLUE);
    graphics.fillRect(cursor.x - 4, Blackboard.MAX_Y - cursor.y - 4, 9, 9);
  }

}

