package ua.onlinecourses.model;

import java.util.Comparator;
import java.util.Objects;
import ua.onlinecourses.exception.InvalidDataException;
import ua.onlinecourses.util.InstructorUtils;
import java.util.logging.Logger;
import java.util.logging.Level;

public record Instructor(String firstName, String lastName, int expertise) implements Comparable<Instructor> {

    private static final Logger logger = Logger.getLogger(Instructor.class.getName());

    public static final Comparator<Instructor> BY_EXPERTISE =
            Comparator.comparingInt(Instructor::expertise).reversed();

    public static final Comparator<Instructor> BY_LAST_NAME =
            Comparator.comparing(Instructor::lastName)
                    .thenComparing(Instructor::firstName);

    public static final Comparator<Instructor> BY_FIRST_NAME =
            Comparator.comparing(Instructor::firstName)
                    .thenComparing(Instructor::lastName);

    public Instructor(String firstName, String lastName, int expertise) {
        if (InstructorUtils.isValidName(firstName) && InstructorUtils.isValidName(lastName) &&
                InstructorUtils.isValidExpertise(expertise)) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.expertise = expertise;
        } else {
            throw new InvalidDataException("Invalid parameters");
        }
        logger.log(Level.INFO, "Instructor created successfully: {0}, {1}, {2}",
                new Object[]{firstName, lastName, expertise});
    }

    static Instructor createInstructor(String firstName, String lastName, int expertise) {
        if (InstructorUtils.isValidName(firstName) && InstructorUtils.isValidName(lastName) &&
                InstructorUtils.isValidExpertise(expertise)) {
            logger.log(Level.INFO, "Instructor created successfully: {0}, {1}, {2}",
                    new Object[]{firstName, lastName, expertise});
            return new Instructor(firstName, lastName, expertise);
        }
        throw new InvalidDataException("Invalid parameters");
    }

    @com.fasterxml.jackson.annotation.JsonIgnore
    public String getFullName() {
        if (firstName.length() < 3 || lastName.length() < 3) {
            String errorMsg = "Cannot create full name";
            throw new InvalidDataException(errorMsg);
        }
        String fullName = firstName.substring(0, 3).toUpperCase() +
                lastName.substring(0, 3).toUpperCase() + "-" + expertise;
        return fullName;
    }

    @Override
    public int compareTo(Instructor other) {
        return this.getFullName().compareTo(other.getFullName());
    }
}