package it.cascella.quizzer.entities;

public enum Role {
    ADMIN,
    USER;

    public static Role getRole(String role) {
        switch (role) {
            case "ROLE_ADMIN":
                return ADMIN;
            case "ROLE_USER":
                return USER;
            default:
                return null;
        }
    }
}
