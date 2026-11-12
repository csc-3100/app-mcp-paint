import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Translates mouse presses, drags, and releases into drawing operations.
 *
 * @author Javier Goonzalez-Sanchez (javiergs)
 * @version 1.0
 */
public class DrawingListener implements MouseListener, MouseMotionListener {

  private boolean drawing;

  @Override
  public void mousePressed(MouseEvent event) {
    if (SwingUtilities.isLeftMouseButton(event)) {
      drawing = true;
      Blackboard.getInstance().setPen(false);
      moveToMouse(event);
      Blackboard.getInstance().setPen(true);
      Blackboard.getInstance().report("Mouse | pen down");
    }
  }

  @Override
  public void mouseDragged(MouseEvent event) {
    if (drawing) {
      moveToMouse(event);
    }
  }

  @Override
  public void mouseReleased(MouseEvent event) {
    if (drawing && SwingUtilities.isLeftMouseButton(event)) {
      moveToMouse(event);
      Blackboard.getInstance().setPen(false);
      drawing = false;
      Blackboard.getInstance().report("Mouse | pen up");
    }
  }

  private void moveToMouse(MouseEvent event) {
    int x = Math.max(0, Math.min(event.getX(), Blackboard.MAX_X));
    int y = Math.max(0, Math.min(event.getY(), Blackboard.MAX_Y));
    Blackboard.getInstance().moveTo(x, Blackboard.MAX_Y - y);
  }

  @Override
  public void mouseClicked(MouseEvent e) {}

  @Override
  public void mouseEntered(MouseEvent e) {}

  @Override
  public void mouseExited(MouseEvent e) {}

  @Override
  public void mouseMoved(MouseEvent e) {}

}

