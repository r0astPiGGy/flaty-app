package hcmute.edu.vn.phamdinhquochoa.flatyapp.beans;

import java.io.Serializable;
import java.util.function.Consumer;

public class User implements Serializable {
    private Integer id;
    private String name;
    private String gender;
    private String dateOfBirth;
    private String phone;
    private String username;
    private String password;
    private Role role;

    public User(Integer id, String name, String gender, String dateOfBirth, String phone, String username, String password) {
        this(id, name, gender, dateOfBirth, phone, username, password, Role.USER);
    }

    public User(Integer id, String name, String gender, String dateOfBirth, String phone, String username, String password, String role) {
        this(id, name, gender, dateOfBirth, phone, username, password, Role.valueOf(role));
    }

    public User(Integer id, String name, String gender, String dateOfBirth, String phone, String username, String password, Role role) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.phone = phone;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public User copyAndApply(Consumer<User> applyOnCopy) {
        User newUser = new User(id, name, gender, dateOfBirth, phone, username, password, role);

        applyOnCopy.accept(newUser);

        return newUser;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Role getRole() {
        return role;
    }

    public boolean isAdmin() {
        return role == Role.ADMIN;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", phone='" + phone + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
    
    public enum Role {
        ADMIN,
        USER
    }
}
