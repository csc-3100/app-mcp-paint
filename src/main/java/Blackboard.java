import java.awt.*;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

/**
 * Stores the shared drawing state and notifies observers when it changes.
 *
 * @author Javier Goonzalez-Sanchez (javiergs)
 * @version 1.0
 */
final class Blackboard extends PropertyChangeSupport {

  private static final Blackboard INSTANCE = new Blackboard();

  public static final int MAX_X = 599;
  public static final int MAX_Y = 399;
  private final List<List<Point>> strokes = new ArrayList<>();
  private Point position = new Point(300, 200);
  private boolean penDown;

  private Blackboard() {
    super("");
  }

  public static Blackboard getInstance() {
    return INSTANCE;
  }

  public synchronized Point getPosition() {
    return new Point(position);
  }

  public synchronized boolean isPenDown() {
    return penDown;
  }

  public synchronized List<List<Point>> getStrokes() {
    List<List<Point>> snapshot = new ArrayList<>();
    for (List<Point> stroke : strokes) {
      List<Point> copy = new ArrayList<>();
      for (Point point : stroke) {
        copy.add(new Point(point));
      }
      snapshot.add(copy);
    }
    return snapshot;
  }

  public synchronized void moveTo(int x, int y) {
    position = new Point(x, y);
    if (penDown) {
      strokes.get(strokes.size() - 1).add(position);
    }
    firePropertyChange("drawing", null, new Point(position));
  }

  public synchronized void setPen(boolean down) {
    if (penDown == down) {
      return;
    }
    penDown = down;
    if (down) {
      List<Point> stroke = new ArrayList<>();
      stroke.add(position);
      strokes.add(stroke);
    }
    firePropertyChange("drawing", null, new Point(position));
  }

  public void report(String message) {
    firePropertyChange("activity", null, message);
  }

}
