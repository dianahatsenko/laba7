package ua.onlinecourses.model;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.logging.Level;
import ua.onlinecourses.exception.InvalidDataException;
import ua.onlinecourses.util.AssignmentUtils;

public record Assignment(myModule module, LocalDate dueDate, int maxPoints, Mark mark) implements Comparable<Assignment> {

    private static final Logger logger = Logger.getLogger(Assignment.class.getName());
    public static final Comparator<Assignment> BY_DUE_DATE =
            Comparator.comparing(Assignment::dueDate);
    public static final Comparator<Assignment> BY_MAX_POINTS =
            Comparator.comparingInt(Assignment::maxPoints).reversed();
    public static final Comparator<Assignment> BY_MARK =
            Comparator.comparing(Assignment::mark);
    public static final Comparator<Assignment> BY_MODULE_AND_DATE =
            Comparator.comparing((Assignment a) -> a.module().getFullName())
                    .thenComparing(Assignment::dueDate);

    public Assignment(myModule module, LocalDate dueDate, int maxPoints, Mark mark) {
        if (AssignmentUtils.isValidMaxPoints(maxPoints) && AssignmentUtils.isValidDueDate(dueDate)) {
            this.module = module;
            this.dueDate = dueDate;
            this.maxPoints = maxPoints;
            this.mark = mark;
        } else {
            throw new InvalidDataException("Invalid parameters");
        }
        logger.log(Level.INFO, "Assignment created successfully: {0}, {1}, {2}, {3}",
                new Object[]{module, dueDate, maxPoints, mark});
    }

    static Assignment createAssignment(myModule module, LocalDate dueDate, int maxPoints, Mark mark) {
        if (AssignmentUtils.isValidDueDate(dueDate) && AssignmentUtils.isValidMaxPoints(maxPoints)) {
            logger.log(Level.INFO, "Assignment created successfully: {0}, {1}, {2}, {3}",
                    new Object[]{module, dueDate, maxPoints, mark});
            return new Assignment(module, dueDate, maxPoints, mark);
        }
        throw new InvalidDataException("Invalid parameters");
    }

    @com.fasterxml.jackson.annotation.JsonIgnore
    public String getMark() {
        return switch(this.mark) {
            case EXCELLENT -> "Your mark is excellent";
            case GOOD -> "Your mark is good.";
            case PASSED -> "You passed the exam";
            case SATISFACTORY -> "Your mark is satisfactory.";
            case LOW -> "Your mark is low.";
            case NOT_PASSED -> "You did not pass the exam.";
            default -> "Exam has not happened.";
        };
    }

    @com.fasterxml.jackson.annotation.JsonIgnore
    public String getIdentity() {
        return module.getFullName() + "-" + dueDate.toString();
    }

    @Override
    public int compareTo(Assignment other) {
        return this.dueDate.compareTo(other.dueDate);
    }
}