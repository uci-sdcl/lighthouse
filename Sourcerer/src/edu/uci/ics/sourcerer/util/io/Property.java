package edu.uci.ics.sourcerer.util.io;

public enum Property {
  // -- Begin General Properties --
  PROPERTIES_FILE("properties"),
  INPUT("input"),
  OUTPUT("output"),
  // -- End General Properties --
  
  // -- Begin Logging Propeties --
  INFO_LOG("info-log", "info.log"),
  ERROR_LOG("error-log", "error.log"),
  RESUME_LOG("resume-log", "resume.log"),

  PROMPT_MISSING("prompt-missing", true),
  REPORT_TO_CONSOLE("report-to-console", true),
  // -- End Logging Propeties --

  // -- Begin Extractor Properties --
  REPO_MODE("repo-mode", true),
  JAR_ONLY("jar-only", true),
  
  PPA("ppa", true),
  
  NOT_JAR("not-jar", true),
  
  IMPORT_WRITER("import-writer", null),
  ENTITY_WRITER("entity-writer", null),
  JAR_ENTITY_WRITER("jar-entity-writer", null),
  RELATION_WRITER("relation-writer", null),
  COMMENT_WRITER("comment-writer", null),
  FILE_WRITER("file-writer", null),
  JAR_FILE_WRITER("jar-file-writer", null),
  
  IMPORT_FILE("import-file", "imports.txt"),
  ENTITY_FILE("entity-file", "entities.txt"),
  RELATION_FILE("relations-file", "relations.txt"),
  COMMENT_FILE("comment-file", "comments.txt"),
  FILE_FILE("file-file", "files.txt"),
  JAR_FILE_FILE("jar-file-file", "jar-files.txt"),
  
  // -- End Extractor Properties --
  
  // -- Begin Repository Manager Properties --
  BUILD_JAR_INDEX("build-jar-index", true),
  CALCULATE_JAR_NAME_SIMILARITY("calculate-jar-name-similarity", true),
  
  REPO_ROOT("repo-root"),
  // -- End Repository Manager Properties --
  
  // -- Begin Repository File Server Properties --
  REPO_FILE_SERVER_URL("repo-file-server-url", "http://nile.ics.uci.edu:9180/repofileserver/"),
  // -- End Repository File Server Properties --
  
  // -- Begin Slicer File Server Properties --
  SLICING_FILE_SERVER_URL("slicing-file-server-url", "http://nile.ics.uci.edu:9180/slicingserver/"),
  // -- End Slicer File Server Properties --
  
  // -- Begin Database Properties --
  DATABASE_URL("db-url", "jdbc:mysql://tagus.ics.uci.edu:3306/sourcerer"),
  DATABASE_USER("ub-user", "sourcerer"),
  DATABASE_PASSWORD("db-passwd", "sourcerer4us"),
  INITIALIZE_DATABASE("initialize-db", true),
  // -- End Database Properties --

  
  // -- Begin Add Project Properties --
  PROJECT_NAME("project-name"),
  // -- End Add Project Properties --
  
  // -- Begin Statistics Properties --
  REPO_STATS_FILE("repo-stats-file", "repo-stats.txt"),
  JDK_STATS_FILE("jdk-stats-file", "jdk-stats.txt"),
  POPULAR_JARS_FILE("popular-jars-file", "popular-jars.txt"),
  NAME_STATS_FILE("name-stats-file", "name-stats.txt"),
  // -- End Statistics Properties --
  
  // -- Begin Automated Slice Tester Properties --
  COUNT("count", "100"),
  RESULTS("results", "results.txt"),
  // -- End Automated Slice Tester Properties --
  ;
    
  private String name;
  private String defaultValue;
  private boolean hasDefaultValue = false;
  private boolean isFlag = false;
  
  private Property(String name) {
    this.name = name;
  }
  
  private Property(String name, String defaultValue) {
    this.name = name;
    this.defaultValue = defaultValue;
    this.hasDefaultValue = true;
  }
  
  private Property(String name, boolean isFlag) {
    this.name = name;
    this.isFlag = isFlag;
  }
  
  public String getName() {
    return name;
  }
  
  public boolean hasDefaultValue() {
    return hasDefaultValue;
  }
  
  public String getDefaultValue() {
    return defaultValue;
  }
  
  public boolean isFlag() {
    return isFlag;
  }
  
  public static Property parse(String s) {
    for (Property prop : values()) {
      if (prop.getName().equals(s)) {
        return prop;
      }
    }
    return null;
  }
}
