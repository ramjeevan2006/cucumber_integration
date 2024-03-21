
import java.util.*;
import java.util.stream.Collectors;

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

    public void printFields() {
        fields.forEach((key, value) -> System.out.println(key + ": " + value));
    }
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
                        HashMap::new // Collector's target container
                ));
    }

    public static void main(String[] args) {
        // Dummy data generation
        Map<String, Object> mainFrameData = new HashMap<>();
        mainFrameData.put("name", "John");
        mainFrameData.put("age", 30);
        mainFrameData.put("city", "New York");

        Map<String, Object> cassandraData = new HashMap<>();
        cassandraData.put("name", "Alice");
        cassandraData.put("age", 25);
        cassandraData.put("country", "Canada");

        // Creating instances of responses
        MainFrameResponse mainFrameResponse = new MainFrameResponse(mainFrameData);
        CassandraResponse cassandraResponse = new CassandraResponse(cassandraData);

        // Defining the list of selected fields and other flags
        List<String> userSelectedFieldsList = Arrays.asList("name", "age", "city", "country");
        List<String> mainFrameFieldsList = Arrays.asList("name", "age", "city");
        List<String> elseFieldsList = Arrays.asList("country");
        boolean everythingFromMainFrames = false;
        boolean cardOpenedWithinSixDays = true;

        // Using MappingAlgorithm to map responses
        MappingAlgorithm mapper = new MappingAlgorithm();
        FinalVo finalVo = mapper.mapResponses(userSelectedFieldsList, mainFrameFieldsList, elseFieldsList,
                everythingFromMainFrames, cardOpenedWithinSixDays, mainFrameResponse, cassandraResponse);

        // Printing the result
        finalVo.printFields();
    }
}
