package ng.edu.aun.sca.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
// import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ng.edu.aun.sca.model.Course;
import ng.edu.aun.sca.model.User;
import ng.edu.aun.sca.service.CourseService;
import ng.edu.aun.sca.service.UserService;

@Controller
public class StudentController {
    @Autowired
    private UserService userService;

    @Autowired
    private CourseService courseService;

    @GetMapping("/student/index")
    private String studentIndex(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.findByUsername(userDetails.getUsername());
        model.addAttribute("firstName", currentUser.getFirstname());
        model.addAttribute("lastName", currentUser.getLastname());
        return "student-index";
    }

    @GetMapping("/student/course/history/add")
    public String showAddCourseToHistoryForm(Model model) {
        List<Course> allCourses = courseService.findAll();
        model.addAttribute("courses", allCourses);
        return "course-history-form";
    }

    @PostMapping("/student/course/history/add")
    public String addCourseToHistory(@RequestParam("courseIds") List<Long> courseIds,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {
        User currentUser = userService.findByUsername(userDetails.getUsername());

        for (Long courseId : courseIds) {
            Course course = courseService.findById(courseId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid Course"));

            if (currentUser.getCourseHistory().contains(course)) {
                String courseName = course.getCourse_code();
                redirectAttributes.addFlashAttribute("error", courseName + " is already in the course history.");
                return "redirect:/student/course/history";
            } else {
                currentUser.getCourseHistory().add(course);
            }
        }
        userService.update(currentUser);

        return "redirect:/student/course/history";
    }

    @PostMapping("/student/suggest-courses")
    public String suggestCourses(@RequestParam("numCourses") int numCourses, Model model) {
        User currentUser = userService.getCurrentUser();
        String yearStanding = currentUser.getYear_standing();

        List<Course> coursesNotInHistory = courseService.findCoursesNotInHistory(currentUser.getUser_id());

        List<Course> suggestedCourses = new ArrayList<>();
        for (Course course : coursesNotInHistory) {
            int requiredStanding = Integer.parseInt(course.getRequired_standing());
            if (requiredStanding <= Integer.parseInt(yearStanding)) {
                if (course.getPrerequisites().isEmpty() || prerequisitesMet(currentUser, course)) {
                    suggestedCourses.add(course);
                }
            }
        }

        Collections.shuffle(suggestedCourses);
        suggestedCourses = suggestedCourses.subList(0, Math.min(numCourses, suggestedCourses.size()));

        model.addAttribute("suggestedCourses", suggestedCourses);
        return "suggested-courses";
    }

    private boolean prerequisitesMet(User user, Course course) {
        for (Course prerequisite : course.getPrerequisites()) {
            if (!user.getCourseHistory().contains(prerequisite)) {
                return false;
            }
        }
        return true;
    }

    @GetMapping("/student/course/history")
    public String viewCourseHistory(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        Set<Course> courseHistory = user.getCourseHistory();
        model.addAttribute("courseHistory", courseHistory);
        model.addAttribute("user", user);
        return "course-history";
    }

    @PostMapping("/student/course/history/delete/{id}")
    public String removeCourse(
            @PathVariable("id") Long courseId,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {
        User user = userService.findByUsername(userDetails.getUsername());
        Course course = courseService.findById(courseId).orElse(null);

        if (!user.getCourseHistory().contains(course)) {
            redirectAttributes.addFlashAttribute("error", "Course is not in the course history.");
            return "redirect:/student/course/history";
        }

        user.getCourseHistory().remove(course);
        userService.update(user);

        return "redirect:/student/course/history";
    }

    @GetMapping("/student/course/history/add/{id}")
    public String showAddCourses(Model model, @PathVariable("id") Long id) {
        model.addAttribute("courses", courseService.findCoursesNotInHistory(id));
        return "student-add-course";
    }
}
