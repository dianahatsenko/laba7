package ua.onlinecourses.model;

import java.util.Comparator;
import java.util.Objects;
import ua.onlinecourses.exception.InvalidDataException;
import ua.onlinecourses.util.ModuleUtils;
import java.util.logging.Logger;
import java.util.logging.Level;

public record myModule(String title, String content) implements Comparable<myModule> {

    private static final Logger logger = Logger.getLogger(myModule.class.getName());
    public static final Comparator<myModule> BY_TITLE =
            Comparator.comparing(myModule::title);
    public static final Comparator<myModule> BY_CONTENT =
            Comparator.comparing(myModule::content);
    public static final Comparator<myModule> BY_CONTENT_LENGTH =
            Comparator.comparingInt((myModule m) -> m.content().length())
                    .thenComparing(myModule::title);

    public myModule(String title, String content) {
        if (ModuleUtils.isValidTitle(title) && ModuleUtils.isValidContent(content)) {
            this.title = title;
            this.content = content;
        } else {
            throw new InvalidDataException("Invalid parameters");
        }
        logger.log(Level.INFO, "Module created successfully: {0}, {1}",
                new Object[]{title, content});
    }

    static myModule createModule(String title, String content) {
        if (ModuleUtils.isValidTitle(title) && ModuleUtils.isValidContent(content)) {
            logger.log(Level.INFO, "Module created successfully: {0}, {1}",
                    new Object[]{title, content});
            return new myModule(title, content);
        }
        throw new InvalidDataException("Invalid parameters");
    }

    @com.fasterxml.jackson.annotation.JsonIgnore
    public String getFullName() {
        if (title.length() < 3 || content.length() < 3) {
            String errorMsg = "Cannot create full name";
            throw new InvalidDataException(errorMsg);
        }
        String fullName = title.substring(0, 3).toUpperCase() + "-" +
                content.substring(0, 3).toUpperCase();
        return fullName;
    }

    @Override
    public int compareTo(myModule other) {
        return this.getFullName().compareTo(other.getFullName());
    }
}