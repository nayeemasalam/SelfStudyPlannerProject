import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


// ===============================
// USER
// ===============================

class User implements Serializable {

    String username;
    String password;
    String fullName;
    String email;

    List<Subject> subjects = new ArrayList<>();
    List<StudyTask> tasks = new ArrayList<>();

    User(String fullName,
         String username,
         String email,
         String password) {

        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.password = password;
    }
}


// ===============================
// SUBJECT
// ===============================

class Subject implements Serializable {

    String name;

    // Class date
    String classDate;

    // Exam date
    String examDate;

    List<Topic> topics = new ArrayList<>();

    Subject(String name,
            String classDate,
            String examDate) {

        this.name = name;
        this.classDate = classDate;
        this.examDate = examDate;
    }
}


// ===============================
// TOPIC
// ===============================

class Topic implements Serializable {

    String name;

    Difficulty difficulty =
            Difficulty.MEDIUM;

    boolean completed = false;

    Topic(String name) {
        this.name = name;
    }
}


// ===============================
// DIFFICULTY
// ===============================

enum Difficulty {

    EASY,
    MEDIUM,
    HARD
}


// ===============================
// STUDY TASK
// ===============================

class StudyTask implements Serializable {

    String topic;
    String subject;
    String date;

    double hours;

    boolean completed;
    boolean missed;

    StudyTask(String topic,
              String subject,
              String date,
              double hours) {

        this.topic = topic;
        this.subject = subject;
        this.date = date;
        this.hours = hours;

        this.completed = false;
        this.missed = false;
    }
}