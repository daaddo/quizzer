package it.cascella.quizzer.entities;

public enum Role {
    ADMIN,
    USER;

    public static Role getRole(String role) {
        return switch (role) {
            case "ROLE_ADMIN" -> ADMIN;
            case "ROLE_USER" -> USER;
            default -> null;
        };
    }

    public String getName() {

        return switch (this) {
            case ADMIN -> "ADMIN";
            case USER -> "USER";
        };
    }
}
