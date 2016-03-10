import java.io.File;

public class saveData implements java.io.Serializable {
  private static final long serialVersionUID = 1L;

  // This allows the user to save a path so that same path opens next time.
  public File musicPath;
  boolean didLoad;

  // put saved alarms and saved schedules into this class

  public saveData(boolean loaded) {
    didLoad = loaded;
  }

  public void setPath(File path) {
    musicPath = path;
  }

}
