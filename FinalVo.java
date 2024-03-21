import java.util.Map;

public class FinalVo {
    private Map<String, Object> fields;

    public FinalVo(Map<String, Object> fields) {
        this.fields = fields;
    }

    public void printFields() {
        fields.forEach((key, value) -> System.out.println(key + ": " + value));
    }
}
