import java.util.*;
import java.util.stream.Collectors;

public class MappingAlgorithm {

    public FinalVo mapResponses(List<String> userSelectedFieldsList,
                                List<String> mainFrameFieldsList,
                                List<String> elseFieldsList,
                                boolean everythingFromMainFrames,
                                boolean cardOpenedWithinSixDays,
                                MainFrameResponse mainFrameResponse,
                                CassandraResponse cassandraResponse) {

        Map<String, Object> resultMap = userSelectedFieldsList.stream()
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

        return new FinalVo(resultMap);
    }
}
