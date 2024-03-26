public class MainframeResponseVo {
    private String accountId;
    private String name;
    private int age;
    private String dob; // Assume YYYY-MM-DD format
    private int creditScore;
    private String city;
    private String zipCode;
    private double amount;

    // Getters and Setters
    // Generate using your IDE (e.g., IntelliJ or Eclipse) to save space here
}



public class CasandraResponseVo {
    private String accountId;
    private String name;
    private int age;
    private String dob; // Assume YYYY-MM-DD format
    private int creditScore;
    private String city;
    private String ssn;

    // Getters and Setters
    // Generate using your IDE
}



public class FinalVo {
    private String accountId;
    private String name;
    private int age;
    private String dob;
    private Integer creditScore; // Use Integer to handle nulls
    private String city;
    private String zipCode;
    private Double amount;
    private String ssn; // Store only the last 4 digits

    // Getters and Setters
    // Generate using your IDE
}



import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FieldFormatter {
    public static String formatDob(String dob) {
        LocalDate date = LocalDate.parse(dob);
        return date.format(DateTimeFormatter.ofPattern("MMddyy"));
    }

    public static String formatSsn(String ssn) {
        if (ssn != null && ssn.length() > 4) {
            return ssn.substring(ssn.length() - 4);
        }
        return ssn;
    }
}






import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class FieldMapper {

    private static final Map<String, Function<Object, Object>> fieldTransformations = new HashMap<>();

    static {
        fieldTransformations.put("dob", value -> FieldFormatter.formatDob((String) value));
        fieldTransformations.put("ssn", value -> FieldFormatter.formatSsn((String) value));
    }

    public static FinalVo mapFields(MainframeResponseVo mainFrameResponse,
                                     CasandraResponseVo casandraResponse,
                                     List<String> userSelectedFieldsList,
                                     List<String> mainFrameFields,
                                     boolean everythingFromMainFrames,
                                     boolean cardOpenedWithinSixDays) {
        FinalVo finalVo = new FinalVo();

        for (String fieldName : userSelectedFieldsList) {
            try {
                Object value;
                if (everythingFromMainFrames || cardOpenedWithinSixDays || mainFrameFields.contains(fieldName)) {
                    Field sourceField = MainframeResponseVo.class.getDeclaredField(fieldName);
                    sourceField.setAccessible(true);
                    value = sourceField.get(mainFrameResponse);
                } else {
                    Field sourceField = CasandraResponseVo.class.getDeclaredField(fieldName);
                    sourceField.setAccessible(true);
                    value = sourceField.get(casandraResponse);
                }
                setFinalVoField(finalVo, fieldName, value);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                System.err.println("Error accessing field: " + fieldName);
            }
        }
        return finalVo;
    }

    private static void setFinalVoField(FinalVo finalVo, String fieldName, Object value) {
        try {
            Field finalField = FinalVo.class.getDeclaredField(fieldName);
            finalField.setAccessible(true);

            if (fieldTransformations.containsKey(fieldName)) {
                value = fieldTransformations.get(fieldName).apply(value);
            }

            finalField.set(finalVo, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.err.println("Error setting field: " + fieldName);
        }
    }
}



import java.util.Arrays;
import java.util.List;

public class MainProgram {

    public static void main(String[] args) {
        // Sample data for MainframeResponseVo
        MainframeResponseVo mainFrameResponse1 = new MainframeResponseVo("123", "John Doe", 30, "1990-01-15", 720, "New York", "10001", 1000.0);
        MainframeResponseVo mainFrameResponse2 = new MainframeResponseVo("456", "Jane Doe", 25, "1995-02-20", 680, "Los Angeles", "90001", 1500.0);
        MainframeResponseVo mainFrameResponse3 = new MainframeResponseVo("789", "Jim Beam", 40, "1980-03-25", 700, "Chicago", "60001", 2000.0);

        // Sample data for CasandraResponseVo
        CasandraResponseVo casandraResponse1 = new CasandraResponseVo("123", "John Doe", 30, "1990-01-15", 720, "New York", "9876");
        CasandraResponseVo casandraResponse2 = new CasandraResponseVo("456", "Jane Doe", 25, "1995-02-20", 680, "Los Angeles", "5432");
        CasandraResponseVo casandraResponse3 = new CasandraResponseVo("789", "Jim Beam", 40, "1980-03-25", 700, "Chicago", "1010");

        // User selected fields and mainframe fields
        List<String> userSelectedFieldsList = Arrays.asList("accountId", "name", "age", "dob", "creditScore", "city", "ssn");
        List<String> mainFrameFields = Arrays.asList("accountId", "name", "age", "dob", "creditScore", "city", "zipCode", "amount");

        // Flags
        boolean everythingFromMainFrames = true;
        boolean cardOpenedWithinSixDays = false;

        // Map and print FinalVo for each set of records
        FinalVo finalVo1 = FieldMapper.mapFields(mainFrameResponse1, casandraResponse1, userSelectedFieldsList, mainFrameFields, everythingFromMainFrames, cardOpenedWithinSixDays);
        printFinalVo(finalVo1);

        FinalVo finalVo2 = FieldMapper.mapFields(mainFrameResponse2, casandraResponse2, userSelectedFieldsList, mainFrameFields, everythingFromMainFrames, cardOpenedWithinSixDays);
        printFinalVo(finalVo2);

        FinalVo finalVo3 = FieldMapper.mapFields(mainFrameResponse3, casandraResponse3, userSelectedFieldsList, mainFrameFields, everythingFromMainFrames, cardOpenedWithinSixDays);
        printFinalVo(finalVo3);
    }

    private static void printFinalVo(FinalVo finalVo) {
        System.out.println("FinalVo {");
        System.out.println("  accountId='" + finalVo.getAccountId() + "',");
        System.out.println("  name='" + finalVo.getName() + "',");
        System.out.println("  age=" + finalVo.getAge() + ",");
        System.out.println("  dob='" + finalVo.getDob() + "',");
        System.out.println("  creditScore=" + finalVo.getCreditScore() + ",");
        System.out.println("  city='" + finalVo.getCity() + "',");
        System.out.println("  ssn='" + finalVo.getSsn() + "'");
        System.out.println("}\n");
    }
}

