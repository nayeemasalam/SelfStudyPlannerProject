import java.util.HashMap;
import java.util.Map;

public class StudyTimeStorage {

    private static final Map<String, Double> studyHours =
            new HashMap<>();

    private StudyTimeStorage() {
    }

    public static void setHours(
            User user,
            double hours
    ) {

        studyHours.put(
                user.username,
                hours
        );
    }

    public static double getHours(
            User user
    ) {

        return studyHours.getOrDefault(
                user.username,
                2.0
        );
    }
}