import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema.CallToolRequest;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import io.modelcontextprotocol.spec.McpSchema.TextContent;
import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * Exposes cursor and pen operations as MCP tools using the shared blackboard.
 *
 * @author Javier Goonzalez-Sanchez (javiergs)
 * @version 1.0
 */
public class ToolsHandler {

  public CallToolResult handleGetPosition(McpSyncServerExchange exchange, CallToolRequest request) {
    synchronized (Blackboard.getInstance()) {
      return result("get_position()", position(), false);
    }
  }

  public CallToolResult handleMove(McpSyncServerExchange exchange, CallToolRequest request) {
    Map<String, Object> arguments = request.arguments();
    String call = "move " + arguments;
    synchronized (Blackboard.getInstance()) {
      try {
        return result(call, move(arguments), false);
      } catch (IllegalArgumentException e) {
        return result(call, e.getMessage(), true);
      }
    }
  }

  public CallToolResult handleSetPen(McpSyncServerExchange exchange, CallToolRequest request) {
    Map<String, Object> arguments = request.arguments();
    String call = "set_pen " + arguments;
    synchronized (Blackboard.getInstance()) {
      try {
        return result(call, setPen(arguments), false);
      } catch (IllegalArgumentException e) {
        return result(call, e.getMessage(), true);
      }
    }
  }

  private String move(Map<String, Object> arguments) {
    Point start = Blackboard.getInstance().getPosition();
    long x = (long) start.x + offset(arguments.get("dx"));
    long y = (long) start.y + offset(arguments.get("dy"));
    if (x < 0 || x > Blackboard.MAX_X || y < 0 || y > Blackboard.MAX_Y) {
      throw new IllegalArgumentException("Move would leave the canvas. " + position());
    }
    Blackboard.getInstance().moveTo((int) x, (int) y);
    return position();
  }

  private int offset(Object value) {
    if (value instanceof Number) {
      Number number = (Number) value;
      double offset = number.doubleValue();
      if (offset >= Integer.MIN_VALUE && offset <= Integer.MAX_VALUE && offset == Math.rint(offset)) {
        return (int) offset;
      }
    }
    throw new IllegalArgumentException("dx and dy must be integers.");
  }

  private String setPen(Map<String, Object> arguments) {
    Object down = arguments.get("down");
    if (!(down instanceof Boolean)) {
      throw new IllegalArgumentException("down must be true or false.");
    }
    Blackboard.getInstance().setPen((Boolean) down);
    return position();
  }

  private String position() {
    Point point = Blackboard.getInstance().getPosition();
    return "Position (" + point.x + ", " + point.y + "), pen "
        + (Blackboard.getInstance().isPenDown() ? "down" : "up")
        + ". Bounds: x 0–" + Blackboard.MAX_X + ", y 0–" + Blackboard.MAX_Y + ".";
  }

  private CallToolResult result(String call, String message, boolean isError) {
    Blackboard.getInstance().report("MCP | " + call + " → " + message);
    return CallToolResult.builder()
        .content(List.of(new TextContent(message)))
        .isError(isError)
        .build();
  }

}
