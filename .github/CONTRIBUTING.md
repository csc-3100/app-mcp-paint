# Contributing to MCP Paint

MCP Paint is a Java Swing teaching example. Contributions that make the drawing behavior, tool calls, or setup easier for students to understand are welcome.

1. Open a bug, documentation, or feature issue if useful. Discuss new tools or architectural changes before implementing them.
2. Create a focused branch in a fork or in this repository if you have write access.
3. Keep each Java class in its own file. Use explicit types and keep tool registration, tool handlers, and drawing state separate.
4. Run `mvn clean verify`. For drawing changes, try drawing with the mouse. For MCP changes, check a tool call in the Paint window Claude Desktop launches.
5. Open a pull request against `main`. Describe what changed, how you checked it, and anything you couldn't test.

Keep credentials, personal data, generated JARs, and IDE settings out of commits and issue reports.

## Publish a release

Maintainers set the version in `pom.xml`, merge it to `main`, and push a matching tag. For example, for Maven version `1.0`:

```sh
git switch main
git pull --ff-only
git tag -a v1.0 -m "MCP Paint 1.0"
git push origin v1.0
```

The Release workflow builds and verifies the tagged version, checks that the JAR name matches the tag, and publishes a GitHub release with generated notes and the runnable JAR. Check the workflow result and confirm that the JAR is attached on the Releases page.
