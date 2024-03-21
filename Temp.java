import java.util.*;

// Dummy class for MainFrameResponse
class MainFrameResponse {
    private Map<String, Object> fields;

    public MainFrameResponse(Map<String, Object> fields) {
        this.fields = fields;
    }

    public Object getFieldValue(String fieldName) {
        return fields.get(fieldName);
    }
}

// Dummy class for CassandraResponse
class CassandraResponse {
    private Map<String, Object> fields;

    public CassandraResponse(Map<String, Object> fields) {
        this.fields = fields;
    }

    public Object getFieldValue(String fieldName) {
        return fields.get(fieldName);
    }
}

// Dummy class for FinalVo
class FinalVo {
    private Map<String, Object> fields;

    public FinalVo(Map<String, Object> fields) {
        this.fields = fields;
    }

    // You can add any additional methods as needed
}

// Class for MappingAlgorithm
public class MappingAlgorithm {

    public FinalVo mapResponses(List<String> userSelectedFieldsList,
                                List<String> mainFrameFieldsList,
                                List<String> elseFieldsList,
                                boolean everythingFromMainFrames,
                                boolean cardOpenedWithinSixDays,
                                MainFrameResponse mainFrameResponse,
                                CassandraResponse cassandraResponse) {

        return userSelectedFieldsList.stream()
                .filter(fieldName -> {
                    if (mainFrameFieldsList.contains(fieldName) || everythingFromMainFrames || cardOpenedWithinSixDays) {
                        return true; // Include fields from mainFrameResponse
                    } else if (elseFieldsList.contains(fieldName)) {
                        return true; // Include fields from cassandraResponse
                    } else {
                        return false; // Exclude this field
                    }
                })
                .collect(Collectors.toMap(
                        fieldName -> fieldName,
                        fieldName -> {
                            if (mainFrameFieldsList.contains(fieldName) || everythingFromMainFrames || cardOpenedWithinSixDays) {
                                return mainFrameResponse.getFieldValue(fieldName);
                            } else {
                                return cassandraResponse.getFieldValue(fieldName);
                            }
                        },
                        (value1, value2) -> value1, // If duplicate keys, keep value from mainFrameResponse
                        FinalVo::new // Collector's target container
                ));
    }

    // Define MainFrameResponse and CassandraResponse classes and FinalVo class as needed
}
