import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/** Marks a field that must not be null or blank. */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface NotBlank {
}

/** Marks a field whose text must be at most {@link #value()} characters. */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface MaxLength {
    int value();
}

/** The form whose fields we validate. */
class SignupForm {
    @NotBlank
    @MaxLength(20)
    private String name;

    @NotBlank
    @MaxLength(50)
    private String email;

    @NotBlank
    @MaxLength(20)
    private String password;

    @MaxLength(100)
    private String bio;

    SignupForm(String name, String email, String password, String bio) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.bio = bio;
    }
}

/** Reflection-based validator. Add new annotations here as the form grows. */
public class FormValidator {

    public static List<String> validate(Object form) throws IllegalAccessException {
        List<String> errors = new ArrayList<>();
        for (Field field : form.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Object value = field.get(form);
            String label = field.getName();

            if (field.isAnnotationPresent(NotBlank.class)
                    && (value == null || value.toString().isBlank())) {
                errors.add(label + ": must not be blank");
                continue; // no point checking length of a blank value
            }

            MaxLength max = field.getAnnotation(MaxLength.class);
            if (max != null && value != null && value.toString().length() > max.value()) {
                errors.add(label + ": must be at most " + max.value()
                        + " characters (got " + value.toString().length() + ")");
            }
        }
        return errors;
    }

    public static void main(String[] args) throws IllegalAccessException {
        SignupForm[] forms = {
                new SignupForm("Rushang", "rushang@example.com", "secret123", "Hi there"),
                new SignupForm("", "rushang@example.com", "secret123", null),
                new SignupForm("Rushang", "rushang@example.com", "   ",
                        "A very long bio that is definitely going to be too long for the one hundred "
                                + "character limit, so the validator must report it as an error.")
        };

        for (int i = 0; i < forms.length; i++) {
            List<String> errors = validate(forms[i]);
            System.out.println("Form " + (i + 1) + ": " + (errors.isEmpty() ? "VALID" : errors.size() + " error(s)"));
            for (String error : errors) {
                System.out.println("  - " + error);
            }
        }
    }
}
