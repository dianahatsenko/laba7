package ua.onlinecourses.model;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Objects;
import ua.onlinecourses.exception.InvalidDataException;
import ua.onlinecourses.util.CourseUtils;
import java.util.logging.Logger;
import java.util.logging.Level;

public record Course(String title, String description, int credits, LocalDate startDate) implements Comparable<Course> {

    private static final Logger logger = Logger.getLogger(Course.class.getName());

    public static final Comparator<Course> BY_CREDITS =
            Comparator.comparingInt(Course::credits);

    public static final Comparator<Course> BY_START_DATE =
            Comparator.comparing(Course::startDate);

    public static final Comparator<Course> BY_TITLE =
            Comparator.comparing(Course::title)
                    .thenComparing(Course::description);

    public Course(String title, String description, int credits, LocalDate startDate) {
        if (CourseUtils.isValidDescription(description) && CourseUtils.isValidCredit(credits) &&
                CourseUtils.isValidstartDate(startDate) && CourseUtils.isValidTitle(title)) {
            this.title = title;
            this.description = description;
            this.credits = credits;
            this.startDate = startDate;
        } else {
            throw new InvalidDataException("Invalid parameters");
        }
        logger.log(Level.INFO, "Course created successfully: {0}, {1}, {2}, {3}",
                new Object[]{title, description, credits, startDate});
    }

    static Course createCourse(String title, String description, int credits, LocalDate startDate) {
        if (CourseUtils.isValidTitle(title) && CourseUtils.isValidDescription(description) &&
                CourseUtils.isValidCredit(credits) && CourseUtils.isValidstartDate(startDate)) {
            logger.log(Level.INFO, "Course created successfully: {0}, {1}, {2}, {3}",
                    new Object[]{title, description, credits, startDate});
            return new Course(title, description, credits, startDate);
        }
        throw new InvalidDataException("Invalid parameters");
    }

    @com.fasterxml.jackson.annotation.JsonIgnore
    public String getFullName() {
        if (title.length() < 3 || description.length() < 3) {
            String errorMsg = "Cannot create full name";
            throw new InvalidDataException(errorMsg);
        }
        String fullName = title.substring(0, 3).toUpperCase() + "-" +
                description.substring(0, 3).toUpperCase() + "-" +
                credits + startDate.toString();
        return fullName;
    }

    @Override
    public int compareTo(Course other) {
        return this.getFullName().compareTo(other.getFullName());
    }
}