package exeptions;

public class ResourceReadException extends ResourceLoadException {
    public ResourceReadException(String resourceName, Throwable cause) {
        super("Failed to read resource: " + resourceName, cause);
    }
}