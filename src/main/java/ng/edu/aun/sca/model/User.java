package ng.edu.aun.sca.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;
    private String firstname;
    private String lastname;
    private String email;
    private String phone_number;
    private String password;
    private String year_standing;

    @Column(nullable = false)
    private String verificationToken;

    @Column(nullable = false)
    private boolean verified;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = {
            @JoinColumn(name = "role_id"), })
    Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Course> courses = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Catalog> catalogs = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "selected_catalog_id")
    private Catalog selectedCatalog;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_course_history", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "course_id"))
    private Set<Course> courseHistory = new HashSet<>();

    public User() {
    }

    public User(String firstname, String lastname, String email, String phone_number, String password,
            String year_standing, String verificationToken, boolean verified, Set<Role> roles, Set<Course> courses,
            Set<Catalog> catalogs, Catalog selectedCatalog, Set<Course> courseHistory) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phone_number = phone_number;
        this.password = password;
        this.year_standing = year_standing;
        this.verificationToken = verificationToken;
        this.verified = verified;
        this.roles = roles;
        this.courses = courses;
        this.catalogs = catalogs;
        this.selectedCatalog = selectedCatalog;
        this.courseHistory = courseHistory;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    public Set<Catalog> getCatalogs() {
        return catalogs;
    }

    public void setCatalogs(Set<Catalog> catalogs) {
        this.catalogs = catalogs;
    }

    public Catalog getSelectedCatalog() {
        return selectedCatalog;
    }

    public void setSelectedCatalog(Catalog selectedCatalog) {
        this.selectedCatalog = selectedCatalog;
    }

    public Set<Course> getCourseHistory() {
        return courseHistory;
    }

    public void setCourseHistory(Set<Course> courseHistory) {
        this.courseHistory = courseHistory;
    }

    public String getYear_standing() {
        return year_standing;
    }

    public void setYear_standing(String year_standing) {
        this.year_standing = year_standing;
    }

    @Override
    public String toString() {
        return "User [user_id=" + user_id + ", firstname=" + firstname + ", lastname=" + lastname + ", email=" + email
                + ", phone_number=" + phone_number + ", password=" + password + ", year_standing=" + year_standing
                + "]";
    }

}
