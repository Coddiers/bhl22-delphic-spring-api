package pl.edu.wat.repo.api.utils;

import pl.edu.wat.repo.api.entities.User;

public class UserUtils {

    public static boolean isNotModOrAdmin(User user) {
        return !isModOrAdmin(user);
    }

    public static boolean isModOrAdmin(User user) {
        return isMod(user) || isAdmin(user);
    }

    public static boolean isMod(User user) {
        return user.getRoles()
                .stream()
                .anyMatch(it -> it == User.ERole.ROLE_MODERATOR);
    }


    public static boolean isAdmin(User user) {
        return user.getRoles()
                .stream()
                .anyMatch(it -> it == User.ERole.ROLE_ADMIN);
    }
}
