package ng.edu.aun.sca.service;

import java.util.List;
import java.util.Optional;
// import java.util.Set;

import ng.edu.aun.sca.model.Course;

public interface CourseService {
    Course createCourse(Course course);

    Course update(Course course);

    List<Course> findAll();

    Optional<Course> findById(Long id);

    List<Course> findCoursesNotInHistory(Long userId);
}
