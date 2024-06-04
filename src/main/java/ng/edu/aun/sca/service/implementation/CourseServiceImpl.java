package ng.edu.aun.sca.service.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ng.edu.aun.sca.model.Course;
import ng.edu.aun.sca.model.User;
import ng.edu.aun.sca.repository.CourseRepository;
import ng.edu.aun.sca.repository.UserRepository;
import ng.edu.aun.sca.service.CourseService;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    @Override
    public Optional<Course> findById(Long id) {
        return courseRepository.findById(id);
    }

    @Override
    public List<Course> findCoursesNotInHistory(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Set<Course> courseHistory = user.getCourseHistory();
        Set<Course> allCourses = user.getSelectedCatalog().getCourses();
        List<Course> coursesNotInHistory = new ArrayList<>();

        for (Course course : allCourses) {
            if (!courseHistory.contains(course)) {
                coursesNotInHistory.add(course);
            }
        }

        return coursesNotInHistory;
    }

    @Override
    public Course update(Course course) {
        return courseRepository.save(course);
    }

}
