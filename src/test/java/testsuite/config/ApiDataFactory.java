package testsuite.config;

import java.util.ArrayList;
import java.util.List;

import model.user.MUserDetail;

public class ApiDataFactory {
    public static final String API_URL = "https://realworld-api.ap.ngrok.io/api";
    public static List<MUserDetail> USERS = loadUser() ;
    public static final String USER_001 = "nguyenthucuc996+1009@gmail.com";
    public static final String expired_token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6Im1hcmlnb2xkMDkiLCJleHAiOjE3NDY5NzMzNjYsInN1YiI6ImFjY2VzcyJ9.w3-XXsq1yZX6ItP3aBOFBjiYWPeTQ72pADYH504hV-Y";
    public static final String invalid_token = "abgabsgdgdgdgggggggggggggggsgsgsg";
    public static final String existing_token = "";

    private static List<MUserDetail> loadUser(){
        return new ArrayList<>(){
            {
                MUserDetail user = new MUserDetail();
                user.setUsername("marigold09");
                user.setEmail("nguyenthucuc996+1009@gmail.com");
                user.setPassword("123456");
                add(user);
            }
        };
    }

    private static MUserDetail getUserByUsername(String username){
        for(MUserDetail user: USERS){
            if(user.getUsername().equalsIgnoreCase(username)){
                return user;
            }
        }
        return null;
    }

    public static MUserDetail getUserByEmail(String email){
        for(MUserDetail user: USERS){
            if(user.getEmail().equalsIgnoreCase(email)){
                return user;
            }
        }
        return null;
    }
}
