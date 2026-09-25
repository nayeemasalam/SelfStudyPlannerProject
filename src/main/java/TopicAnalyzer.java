import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TopicAnalyzer {

    private static final String API_URL =
            "https://api.openai.com/v1/responses";

    private static final String API_KEY =
            System.getenv("OPENAI_API_KEY");

    private static final String MODEL =
            "gpt-5.6-luna";


    // =========================================================
    // MAIN METHOD
    // =========================================================

    public static Difficulty analyze(String topicName) throws Exception {

        if (topicName == null || topicName.trim().isEmpty()) {
            return Difficulty.MEDIUM;
        }

        topicName = topicName.trim();

        // Try OpenAI first
        if (API_KEY != null && !API_KEY.trim().isEmpty()) {

            try {
                return analyzeWithOpenAI(topicName);

            } catch (Exception e) {

                // If API fails, especially 429,
                // use local analyzer instead.
                System.out.println(
                        "OpenAI unavailable. Using local analyzer."
                );
                System.out.println(e.getMessage());
            }
        }

        // Fallback analyzer
        return analyzeLocally(topicName);
    }


    // =========================================================
    // OPENAI ANALYZER
    // =========================================================

    private static Difficulty analyzeWithOpenAI(String topicName)
            throws Exception {

        String prompt =
                "Analyze the academic difficulty of this study topic.\n"
                        + "Topic: " + topicName + "\n\n"
                        + "Consider:\n"
                        + "- conceptual complexity\n"
                        + "- prerequisite knowledge\n"
                        + "- mathematical or logical difficulty\n"
                        + "- implementation difficulty\n"
                        + "- typical university learning difficulty\n\n"
                        + "Return ONLY one word:\n"
                        + "Easy\n"
                        + "Medium\n"
                        + "Difficult";

        String escapedPrompt = escapeJson(prompt);

        String json =
                "{"
                        + "\"model\":\"" + MODEL + "\","
                        + "\"input\":\"" + escapedPrompt + "\","
                        + "\"max_output_tokens\":10"
                        + "}";

        URL url = new URL(API_URL);

        HttpURLConnection connection =
                (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");

        connection.setRequestProperty(
                "Authorization",
                "Bearer " + API_KEY
        );

        connection.setRequestProperty(
                "Content-Type",
                "application/json"
        );

        connection.setDoOutput(true);
        connection.setConnectTimeout(15000);
        connection.setReadTimeout(30000);

        try (OutputStream output =
                     connection.getOutputStream()) {

            output.write(
                    json.getBytes(StandardCharsets.UTF_8)
            );
        }

        int responseCode =
                connection.getResponseCode();

        InputStream inputStream;

        if (responseCode >= 200 && responseCode < 300) {

            inputStream =
                    connection.getInputStream();

        } else {

            inputStream =
                    connection.getErrorStream();

            String errorBody =
                    readStream(inputStream);

            throw new IOException(
                    "AI request failed. HTTP "
                            + responseCode
                            + "\n"
                            + errorBody
            );
        }

        String response =
                readStream(inputStream);

        connection.disconnect();

        String result =
                extractOutputText(response);

        return convertToDifficulty(result);
    }


    // =========================================================
    // LOCAL FALLBACK ANALYZER
    // =========================================================

    private static Difficulty analyzeLocally(String topic) {

        String text =
                topic.toLowerCase(Locale.ENGLISH);

        int score = 0;

        // -----------------------------------------------------
        // Very basic topics
        // -----------------------------------------------------

        String[] easyWords = {

                "introduction",
                "basic",
                "basics",
                "variable",
                "variables",
                "constant",
                "constants",
                "data type",
                "data types",
                "input",
                "output",
                "print",
                "printing",
                "syntax",
                "operator",
                "operators",
                "if else",
                "if-else",
                "for loop",
                "while loop",
                "do while",
                "array",
                "arrays",
                "string",
                "strings",
                "class",
                "object",
                "method",
                "methods"
        };

        for (String word : easyWords) {

            if (text.contains(word)) {
                score -= 2;
            }
        }


        // -----------------------------------------------------
        // Medium topics
        // -----------------------------------------------------

        String[] mediumWords = {

                "inheritance",
                "interface",
                "exception handling",
                "file handling",
                "thread",
                "threading",
                "multithreading",
                "linked list",
                "stack",
                "queue",
                "sorting",
                "searching",
                "recursion",
                "database",
                "sql",
                "normalization",
                "erd",
                "computer network",
                "networking",
                "tcp",
                "udp",
                "http",
                "operating system",
                "process",
                "deadlock",
                "synchronization"
        };

        for (String word : mediumWords) {

            if (text.contains(word)) {
                score += 3;
            }
        }


        // -----------------------------------------------------
        // Difficult topics
        // -----------------------------------------------------

        String[] hardWords = {

                "dynamic programming",
                "graph algorithm",
                "graph algorithms",
                "dijkstra",
                "bellman ford",
                "floyd warshall",
                "minimum spanning tree",
                "kruskal",
                "prim",
                "backtracking",
                "divide and conquer",
                "compiler design",
                "automata",
                "formal language",
                "context free grammar",
                "machine learning",
                "neural network",
                "deep learning",
                "cryptography",
                "encryption",
                "public key cryptography",
                "rsa",
                "diffie hellman",
                "operating system kernel",
                "concurrency",
                "distributed system",
                "distributed systems",
                "computer architecture",
                "assembly language",
                "calculus",
                "differential equation",
                "linear algebra"
        };

        for (String word : hardWords) {

            if (text.contains(word)) {
                score += 6;
            }
        }


        // -----------------------------------------------------
        // Length / complexity adjustment
        // -----------------------------------------------------

        String[] words =
                text.split("\\s+");

        if (words.length >= 5) {
            score += 2;
        }

        if (words.length >= 8) {
            score += 2;
        }


        // -----------------------------------------------------
        // Technical keyword adjustment
        // -----------------------------------------------------

        String[] technicalWords = {

                "algorithm",
                "complexity",
                "recursion",
                "pointer",
                "memory",
                "concurrency",
                "probability",
                "mathematics",
                "mathematical",
                "optimization",
                "architecture",
                "protocol",
                "security",
                "implementation"
        };

        int technicalCount = 0;

        for (String word : technicalWords) {

            if (text.contains(word)) {
                technicalCount++;
            }
        }

        score += technicalCount;


        // -----------------------------------------------------
        // Final decision
        // -----------------------------------------------------

        if (score >= 7) {
            return Difficulty.HARD;
        }

        if (score >= 2) {
            return Difficulty.MEDIUM;
        }

        return Difficulty.EASY;
    }


    // =========================================================
    // CONVERT AI RESPONSE
    // =========================================================

    private static Difficulty convertToDifficulty(
            String answer) {

        if (answer == null) {
            return Difficulty.MEDIUM;
        }

        String text =
                answer.trim().toLowerCase();

        if (text.contains("difficult")
                || text.contains("hard")) {

            return Difficulty.HARD;
        }

        if (text.contains("easy")) {

            return Difficulty.EASY;
        }

        return Difficulty.MEDIUM;
    }


    // =========================================================
    // EXTRACT OUTPUT TEXT
    // =========================================================

    private static String extractOutputText(
            String json) {

        if (json == null || json.isEmpty()) {
            return "";
        }

        // Try output_text if available
        Pattern pattern =
                Pattern.compile(
                        "\"output_text\"\\s*:\\s*\"(.*?)\"",
                        Pattern.DOTALL
                );

        Matcher matcher =
                pattern.matcher(json);

        if (matcher.find()) {

            return matcher.group(1)
                    .replace("\\\"", "\"")
                    .replace("\\n", " ")
                    .trim();
        }


        // Try text.value from output content
        Pattern textPattern =
                Pattern.compile(
                        "\"text\"\\s*:\\s*\\{\\s*"
                                + "\"value\"\\s*:\\s*\"(.*?)\"",
                        Pattern.DOTALL
                );

        Matcher textMatcher =
                textPattern.matcher(json);

        if (textMatcher.find()) {

            return textMatcher.group(1)
                    .replace("\\\"", "\"")
                    .replace("\\n", " ")
                    .trim();
        }

        return "";
    }


    // =========================================================
    // READ STREAM
    // =========================================================

    private static String readStream(
            InputStream stream)
            throws IOException {

        if (stream == null) {
            return "";
        }

        StringBuilder result =
                new StringBuilder();

        try (BufferedReader reader =
                     new BufferedReader(
                             new InputStreamReader(
                                     stream,
                                     StandardCharsets.UTF_8
                             )
                     )) {

            String line;

            while ((line = reader.readLine()) != null) {

                result.append(line);
            }
        }

        return result.toString();
    }


    // =========================================================
    // JSON ESCAPE
    // =========================================================

    private static String escapeJson(
            String text) {

        return text
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\r", "\\r")
                .replace("\n", "\\n")
                .replace("\t", "\\t");
    }


    // =========================================================
    // DISPLAY TEXT
    // =========================================================

    public static String getDifficultyText(
            Difficulty difficulty) {

        if (difficulty == null) {
            return "Medium";
        }

        switch (difficulty) {

            case EASY:
                return "Easy";

            case HARD:
                return "Difficult";

            case MEDIUM:
            default:
                return "Medium";
        }
    }
}