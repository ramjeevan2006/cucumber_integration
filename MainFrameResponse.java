import java.util.Map;

public class MainFrameResponse {
    private Map<String, Object> fields;

    public MainFrameResponse(Map<String, Object> fields) {
        this.fields = fields;
    }

    public Object getFieldValue(String fieldName) {
        return fields.get(fieldName);
    }
}
