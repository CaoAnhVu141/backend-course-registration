package global.security;

public final class PermissionConstants {

    private PermissionConstants() {
    }

    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_LECTURER = "LECTURER";
    public static final String ROLE_STUDENT = "STUDENT";

    public static final String HAS_ROLE_ADMIN = "hasRole('" + ROLE_ADMIN + "')";
    public static final String HAS_ROLE_LECTURER = "hasRole('" + ROLE_LECTURER + "')";
    public static final String HAS_ROLE_STUDENT = "hasRole('" + ROLE_STUDENT + "')";

    public static final String HAS_ANY_ROLE_ADMIN_LECTURER = "hasAnyRole('" + ROLE_ADMIN + "', '" + ROLE_LECTURER
            + "')";

    public static final String HAS_ANY_ROLE_ADMIN_LECTURER_STUDENT = "hasAnyRole('" + ROLE_ADMIN + "', '"
            + ROLE_LECTURER + "', '" + ROLE_STUDENT + "')";
}