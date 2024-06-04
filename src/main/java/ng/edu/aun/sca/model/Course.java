package ng.edu.aun.sca.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long course_id;
    private String course_code;
    private String course_title;
    private String required_standing;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(mappedBy = "courses")
    private Set<Catalog> catalog = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "course_prerequisite", joinColumns = @JoinColumn(name = "course_id"), inverseJoinColumns = @JoinColumn(name = "prerequisite_id"))
    private Set<Course> prerequisites = new HashSet<>();

    public Course() {
    }

    public Course(String course_code, String course_title, String required_standing, User user, Set<Catalog> catalog,
            Set<Course> prerequisites) {
        this.course_code = course_code;
        this.course_title = course_title;
        this.required_standing = required_standing;
        this.user = user;
        this.catalog = catalog;
        this.prerequisites = prerequisites;
    }

    public Long getCourse_id() {
        return course_id;
    }

    public void setCourse_id(Long course_id) {
        this.course_id = course_id;
    }

    public String getCourse_code() {
        return course_code;
    }

    public void setCourse_code(String course_code) {
        this.course_code = course_code;
    }

    public String getRequired_standing() {
        return required_standing;
    }

    public void setRequired_standing(String required_standing) {
        this.required_standing = required_standing;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Catalog> getCatalog() {
        return catalog;
    }

    public void setCatalog(Set<Catalog> catalog) {
        this.catalog = catalog;
    }

    public Set<Course> getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(Set<Course> prerequisites) {
        this.prerequisites = prerequisites;
    }

    public String getCourse_title() {
        return course_title;
    }

    public void setCourse_title(String course_title) {
        this.course_title = course_title;
    }

    @Override
    public String toString() {
        return "Course [course_id=" + course_id + ", course_code=" + course_code + ", course_title=" + course_title
                + ", required_standing=" + required_standing + "]";
    }

}
