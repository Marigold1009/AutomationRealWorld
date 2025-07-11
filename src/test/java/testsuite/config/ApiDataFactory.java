package testsuite.config;

import testsuite.model.user.MUserDetail;

import java.util.ArrayList;
import java.util.List;

public class ApiDataFactory {
    public static final String API_URL = "https://realworld-api.ap.ngrok.io/api";
    public static final String USER_001 = "nguyenthucuc996+1009@gmail.com";
    public static final List<MUserDetail> USERS = loadUsers();

    private static List<MUserDetail> loadUsers() {
        return new ArrayList<>() {
            {
                MUserDetail user = new MUserDetail();
                user.setUsername("marigold09");
                user.setEmail("nguyenthucuc996+1009@gmail.com");
                user.setPassword("123456");
                add(user);
            }
        };
    }

    public static MUserDetail getUser(String username) {
        for (MUserDetail user : USERS) {
            if (user.getUsername().equalsIgnoreCase(username)) return user;
        }
        return null;
    }

    public static MUserDetail getUserByEmail(String email) {
        for (MUserDetail user : USERS) {
            if (user.getEmail().equalsIgnoreCase(email)) return user;
        }
        return null;
    }
}
