package cinepick.manager;

import cinepick.model.Admin;
import cinepick.model.User;

import java.util.ArrayList;

public class UserManager {
    private ArrayList<User> users;

    public UserManager() {
        users = new ArrayList<>();

        // 기본 관리자 계정
        users.add(new Admin("admin", "1234", "관리자", 0, "ADMIN_CODE"));
    }

    // 회원가입
    public boolean registerUser(String userId, String password, String name) {
        if (isDuplicateId(userId)) {
            return false;
        }

        User user = new User(userId, password, name, 0, "USER");
        users.add(user);
        return true;
    }

    // 아이디 중복 검사
    public boolean isDuplicateId(String userId) {
        for (User user : users) {
            if (user.getUserId().equalsIgnoreCase(userId)) {
                return true;
            }
        }
        return false;
    }

    // 로그인
    public User login(String userId, String password) {
        for (User user : users) {
            if (user.getUserId().equalsIgnoreCase(userId) &&
                    user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    // 회원 아이디로 검색
    public User findUserById(String userId) {
        for (User user : users) {
            if (user.getUserId().equals(userId)) {
                return user;
            }
        }
        return null;
    }

    // 전체 회원 목록 반환
    public ArrayList<User> getUsers() {
        return users;
    }

    // 회원 목록 출력
    public void printUsers() {
        if (users.isEmpty()) {
            System.out.println("등록된 회원이 없습니다.");
            return;
        }

        for (User user : users) {
            System.out.println(user);
        }
    }

    public void replaceUsers(ArrayList<User> loadedUsers) {
        users.clear();
        users.addAll(loadedUsers);
    }

    // 회원 삭제
    public boolean deleteUser(String userId) {
        User user = findUserById(userId);

        if (user == null) {
            return false;
        }

        if (user.getRole().equalsIgnoreCase("ADMIN")) {
            return false;
        }

        users.remove(user);
        return true;
    }

    public boolean updateUserPoint(String userId, int point) {
        User user = findUserById(userId);

        if (user == null) {
            return false;
        }

        if (user.getRole().equalsIgnoreCase("ADMIN")) {
            return false;
        }

        user.setPoint(point);
        return true;
    }

    public boolean updateEmployeeStatus(String userId, boolean employee) {
        User user = findUserById(userId);

        if (user == null) {
            return false;
        }

        if (user.getRole().equalsIgnoreCase("ADMIN")) {
            return false;
        }

        user.setEmployee(employee);
        return true;
    }
}