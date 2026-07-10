package exeptions;

public class ResourceNotFoundException extends ResourceLoadException {
    public ResourceNotFoundException(String resourceName) {
        super("Resource not found: " + resourceName);
    }
}