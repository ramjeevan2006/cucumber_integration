import java.util.*;

public class Main {
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
        System.out.println(finalVo);
    }
}
