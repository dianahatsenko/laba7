package ua.onlinecourses;

import ua.onlinecourses.config.AppConfig;
import ua.onlinecourses.exception.DataSerializationException;
import ua.onlinecourses.model.*;
import ua.onlinecourses.persistence.PersistenceManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {

        try {
            AppConfig config = new AppConfig();
            PersistenceManager manager = new PersistenceManager(config);

            int testDataCount = config.getIntProperty("test.data.count", 5);
            logger.log(Level.INFO, "Configuration loaded from config.properties");
            logger.log(Level.INFO, "Number of test objects: " + testDataCount);
            logger.log(Level.INFO, "Base data path: " + config.getBaseDataPath());
            logger.log(Level.INFO, "");

            demonstrateStudentPersistence(config, manager);
            demonstrateCoursePersistence(config, manager);
            demonstrateInstructorPersistence(config, manager);
            demonstrateExceptionHandling(manager);

        } catch (DataSerializationException e) {
            logger.log(Level.SEVERE, "Serialization error: " + e.getMessage());
            logger.log(Level.SEVERE, "", e);
        }
    }

    private static void demonstrateStudentPersistence(AppConfig config, PersistenceManager manager)
            throws DataSerializationException {

        List<Student> students = new ArrayList<>();
        students.add(new Student("Lesia", "Melnyk", "lesia.melnyk@chnu.edu.ua",
                LocalDate.of(2023, 9, 1)));
        students.add(new Student("Liliya", "Fivko", "liliya.fivko@student.ua",
                LocalDate.of(2023, 9, 5)));
        students.add(new Student("Ivan", "Bondaryk", "ivan.bodnaryk@chnu.edu.ua",
                LocalDate.of(2023, 9, 3)));
        students.add(new Student("Daniel", "Lula", "daniel.lula@chnu.edu.ua",
                LocalDate.of(2023, 9, 2)));
        students.add(new Student("Dmytro", "Vasylyk", "dmytro.vasylyk@chnu.edu.ua",
                LocalDate.of(2023, 9, 10)));

        logger.log(Level.INFO, "Created " + students.size() + " students:");
        for (Student s : students) {
            logger.log(Level.INFO, "  " + s.firstName() + " " + s.lastName() + " - " + s.email());
        }

        logger.log(Level.INFO, "\nSaving to JSON...");
        manager.save(students, "students", Student.class, "JSON");
        logger.log(Level.INFO, "Saved to: " + config.getJsonFilePath("students"));

        logger.log(Level.INFO, "\nSaving to YAML...");
        manager.save(students, "students", Student.class, "YAML");
        logger.log(Level.INFO, "Saved to: " + config.getYamlFilePath("students"));

        logger.log(Level.INFO, "\nLoading from JSON...");
        List<Student> loadedFromJson = manager.load("students", Student.class, "JSON");
        logger.log(Level.INFO, "Loaded " + loadedFromJson.size() + " students from JSON");

        logger.log(Level.INFO, "\nLoading from YAML...");
        List<Student> loadedFromYaml = manager.load("students", Student.class, "YAML");
        logger.log(Level.INFO, "Loaded " + loadedFromYaml.size() + " students from YAML");

        logger.log(Level.INFO, "\nData comparison:");
        boolean jsonMatch = compareStudents(students, loadedFromJson);
        boolean yamlMatch = compareStudents(students, loadedFromYaml);
        logger.log(Level.INFO, "JSON matches original: " + jsonMatch);
        logger.log(Level.INFO, "YAML matches original: " + yamlMatch);
    }

    private static void demonstrateCoursePersistence(AppConfig config, PersistenceManager manager)
            throws DataSerializationException {

        List<Course> courses = new ArrayList<>();
        courses.add(new Course("Java Programming", "Java Basics", 5,
                LocalDate.of(2025, 1, 15)));
        courses.add(new Course("Data Structures", "Algorithms", 3,
                LocalDate.of(2025, 2, 1)));
        courses.add(new Course("Web Development", "HTML and CSS", 4,
                LocalDate.of(2025, 1, 20)));
        courses.add(new Course("Databases", "SQL Basics", 5,
                LocalDate.of(2025, 3, 1)));

        logger.log(Level.INFO, "Created " + courses.size() + " courses:");
        for (Course c : courses) {
            logger.log(Level.INFO, "  " + c.title() + " - " + c.credits() + " credits");
        }

        logger.log(Level.INFO, "\nSaving in both formats...");
        manager.saveAllFormats(courses, "courses", Course.class);

        logger.log(Level.INFO, "\nLoading from JSON...");
        List<Course> loadedFromJson = manager.load("courses", Course.class, "JSON");
        logger.log(Level.INFO, "Loaded " + loadedFromJson.size() + " courses from JSON");

        logger.log(Level.INFO, "\nLoading from YAML...");
        List<Course> loadedFromYaml = manager.load("courses", Course.class, "YAML");
        logger.log(Level.INFO, "Loaded " + loadedFromYaml.size() + " courses from YAML");

        logger.log(Level.INFO, "\nData comparison:");
        boolean jsonMatch = compareCourses(courses, loadedFromJson);
        boolean yamlMatch = compareCourses(courses, loadedFromYaml);
        logger.log(Level.INFO, "JSON matches original: " + jsonMatch);
        logger.log(Level.INFO, "YAML matches original: " + yamlMatch);
    }

    private static void demonstrateInstructorPersistence(AppConfig config, PersistenceManager manager)
            throws DataSerializationException {

        List<Instructor> instructors = new ArrayList<>();
        instructors.add(new Instructor("Igor", "Bylat", 34));
        instructors.add(new Instructor("Denys", "Malyk", 20));
        instructors.add(new Instructor("Inessa", "Kir", 39));
        instructors.add(new Instructor("Alina", "Skrypa", 12));

        logger.log(Level.INFO, "Created " + instructors.size() + " instructors:");
        for (Instructor i : instructors) {
            logger.log(Level.INFO,
                    "  " + i.firstName() + " " + i.lastName() + " - expertise " + i.expertise());
        }

        logger.log(Level.INFO, "\nSaving in both formats...");
        manager.saveAllFormats(instructors, "instructors", Instructor.class);

        logger.log(Level.INFO, "\nLoading from JSON...");
        List<Instructor> loadedFromJson = manager.load("instructors", Instructor.class, "JSON");
        logger.log(Level.INFO, "Loaded " + loadedFromJson.size() + " instructors from JSON");

        logger.log(Level.INFO, "\nLoading from YAML...");
        List<Instructor> loadedFromYaml = manager.load("instructors", Instructor.class, "YAML");
        logger.log(Level.INFO, "Loaded " + loadedFromYaml.size() + " instructors from YAML");

        logger.log(Level.INFO, "\nData comparison:");
        boolean jsonMatch = compareInstructors(instructors, loadedFromJson);
        boolean yamlMatch = compareInstructors(instructors, loadedFromYaml);
        logger.log(Level.INFO, "JSON matches original: " + jsonMatch);
        logger.log(Level.INFO, "YAML matches original: " + yamlMatch);
    }

    private static void demonstrateExceptionHandling(PersistenceManager manager) {

        try {
            logger.log(Level.INFO, "Attempt to save null list...");
            manager.save(null, "test", Student.class, "JSON");
        } catch (DataSerializationException e) {
            logger.log(Level.SEVERE, "Caught exception: " + e.getMessage());
        }

        try {
            logger.log(Level.INFO, "\nAttempt to use unknown format...");
            List<Student> test = new ArrayList<>();
            manager.save(test, "test", Student.class, "XML");
        } catch (DataSerializationException e) {
            logger.log(Level.SEVERE, "Caught exception: " + e.getMessage());
        }

        try {
            logger.log(Level.INFO, "\nAttempt to load from non-existent file...");
            manager.load("nonexistent", Student.class, "JSON");
            logger.log(Level.INFO, "Loaded empty list (file does not exist)");
        } catch (DataSerializationException e) {
            logger.log(Level.SEVERE, "Caught exception: " + e.getMessage());
        }
    }

    private static boolean compareStudents(List<Student> original, List<Student> loaded) {
        if (original.size() != loaded.size()) {
            return false;
        }

        for (int i = 0; i < original.size(); i++) {
            Student o = original.get(i);
            Student l = loaded.get(i);
            if (!o.email().equals(l.email()) ||
                    !o.firstName().equals(l.firstName()) ||
                    !o.lastName().equals(l.lastName())) {
                return false;
            }
        }
        return true;
    }

    private static boolean compareCourses(List<Course> original, List<Course> loaded) {
        if (original.size() != loaded.size()) {
            return false;
        }

        for (int i = 0; i < original.size(); i++) {
            Course o = original.get(i);
            Course l = loaded.get(i);
            if (!o.title().equals(l.title()) || o.credits() != l.credits()) {
                return false;
            }
        }
        return true;
    }

    private static boolean compareInstructors(List<Instructor> original, List<Instructor> loaded) {
        if (original.size() != loaded.size()) {
            return false;
        }

        for (int i = 0; i < original.size(); i++) {
            Instructor o = original.get(i);
            Instructor l = loaded.get(i);
            if (!o.firstName().equals(l.firstName()) ||
                    !o.lastName().equals(l.lastName()) ||
                    o.expertise() != l.expertise()) {
                return false;
            }
        }
        return true;
    }
}
