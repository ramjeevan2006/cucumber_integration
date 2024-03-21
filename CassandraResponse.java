import java.util.Map;

public class CassandraResponse {
    private Map<String, Object> fields;

    public CassandraResponse(Map<String, Object> fields) {
        this.fields = fields;
    }

    public Object getFieldValue(String fieldName) {
        return fields.get(fieldName);
    }
}
