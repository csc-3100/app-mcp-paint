import io.modelcontextprotocol.json.jackson3.JacksonMcpJsonMapper;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpServer.SingleSessionSyncSpecification;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema.ServerCapabilities;
import io.modelcontextprotocol.spec.McpSchema.JsonSchema;
import io.modelcontextprotocol.spec.McpSchema.Tool;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;
import java.util.Map;

/**
 * MCP server over standard input and output.
 *
 * @author Javier Goonzalez-Sanchez (javiergs)
 * @version 1.0
 */
public class MCPHandler {

  public void run() {
    JsonMapper jsonMapper = JsonMapper.builder().build();
    JacksonMcpJsonMapper jacksonMcpJsonMapper = new JacksonMcpJsonMapper(jsonMapper);
    StdioServerTransportProvider transport = new StdioServerTransportProvider(jacksonMcpJsonMapper);
    SingleSessionSyncSpecification session = McpServer.sync(transport);
    session
        .serverInfo("MCP", "1.0.0")
        .capabilities(ServerCapabilities.builder().tools(true).build());
    registerTools(session);
    session.build();
  }

  private void registerTools(SingleSessionSyncSpecification session) {
    Tool positionTool = createTool(
        "get_position",
        "Read the cursor position, pen state, and canvas bounds.",
        Map.of());
    Tool moveTool = createTool(
        "move",
        "Move by dx and dy pixels. Positive dx goes right and positive dy goes up. "
            + "Draw a line if the pen is down.",
        Map.of(
            "dx", Map.of("type", "integer"),
            "dy", Map.of("type", "integer")));
    Tool penTool = createTool(
        "set_pen",
        "Set down to true to draw when moving, or false to move without drawing.",
        Map.of("down", Map.of("type", "boolean")));
    ToolsHandler handlers = new ToolsHandler();
    session.toolCall(positionTool, handlers::handleGetPosition);
    session.toolCall(moveTool, handlers::handleMove);
    session.toolCall(penTool, handlers::handleSetPen);
  }

  private Tool createTool(String name, String description, Map<String, Object> properties) {
    JsonSchema schema = new JsonSchema("object", properties, List.copyOf(properties.keySet()), false, null, null);
    return Tool.builder().name(name).description(description).inputSchema(schema).build();
  }

}
