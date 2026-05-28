package cinepick.model;

public class User {
    private String userId;
    private String password;
    private String name;
    private int point;
    private String role; // USER 또는 ADMIN

    public User(String userId, String password, String name, int point, String role) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.point = point;
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void addPoint(int amount) {
        this.point += amount;
    }

    public String getMembershipGrade() {
        if (role.equalsIgnoreCase("ADMIN")) {
            return "STAFF";
        }

        if (point >= 1000) {
            return "GOLD";
        } else if (point >= 500) {
            return "SILVER";
        } else {
            return "BRONZE";
        }
    }

    public int getDiscountRate() {
        if (role.equalsIgnoreCase("ADMIN")) {
            return 20;
        }

        if (point >= 1000) {
            return 10;
        } else if (point >= 500) {
            return 5;
        } else {
            return 0;
        }
    }

    public void subtractPoint(int amount) {
        this.point -= amount;

        if (this.point < 0) {
            this.point = 0;
        }
    }

    @Override
    public String toString() {
        return "아이디: " + userId +
                ", 이름: " + name +
                ", 포인트: " + point +
                ", 권한: " + role;
    }
}