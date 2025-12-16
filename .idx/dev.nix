{ pkgs, ... }: {
  # Which nixpkgs channel to use
  channel = "stable-24.05"; # or "unstable"

  # Packages to install in the dev environment
  packages = [
    pkgs.openjdk17        # Java 17, compatible with Spring Boot 3.x
    pkgs.maven            # Maven build tool
    # pkgs.gradle          # Uncomment if you use Gradle instead
  ];

  # Environment variables for the workspace
  env = {};

  idx = {
    # Extensions for VS Code
    extensions = [
      "vscjava.vscode-java-pack"         # Java support
      "pivotal.vscode-spring-boot"       # Spring Boot support
      "vscjava.vscode-maven"             # Maven support
    ];

    workspace = {
      # Runs when workspace is first created
      onCreate = {
        # Example: run Maven install
        maven-install = "mvn clean install -DskipTests";
        # Open main Spring Boot class by default (if exists)
        default.openFiles = [ "src/main/java/com/example/demo/DemoApplication.java" ];
      };

      # Runs on every workspace start (optional)
      onStart = {
        spring-boot-run = "mvn spring-boot:run";
      };
    };
  };
}
