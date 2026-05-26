package cinepick.model;

public class Admin extends User {
    private String adminCode;

    public Admin(String userId, String password, String name, int point, String adminCode) {
        super(userId, password, name, point, "ADMIN");
        this.adminCode = adminCode;
    }

    public String getAdminCode() {
        return adminCode;
    }

    public void setAdminCode(String adminCode) {
        this.adminCode = adminCode;
    }

    @Override
    public String toString() {
        return "관리자 아이디: " + getUserId() +
                ", 이름: " + getName() +
                ", 관리자 코드: " + adminCode;
    }
}