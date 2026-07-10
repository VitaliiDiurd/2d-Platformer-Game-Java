package exeptions;

public class LevelLoadException extends ResourceLoadException {
    public LevelLoadException(int levelNumber, Throwable cause) {
        super("Failed to load level " + levelNumber, cause);
    }
}
