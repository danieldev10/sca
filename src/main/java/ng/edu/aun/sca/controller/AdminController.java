package ng.edu.aun.sca.controller;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ng.edu.aun.sca.model.Catalog;
import ng.edu.aun.sca.model.Course;
import ng.edu.aun.sca.model.User;
import ng.edu.aun.sca.service.CatalogService;
import ng.edu.aun.sca.service.CourseService;
import ng.edu.aun.sca.service.UserService;

@Controller
public class AdminController {
    @Autowired
    private UserService userService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private CatalogService catalogService;

    @GetMapping("/admin/users/delete")
    public String showUsersToDelete(Model model) {
        List<User> users = userService.get();
        model.addAttribute("users", users);
        return "redirect:/admin/users";
    }

    @PostMapping("/admin/users/delete")
    public String deleteUser(@RequestParam("userId") Long userId) {
        userService.deleteById(userId);
        return "redirect:/admin/users/delete";
    }

    @GetMapping("/admin/index")
    private String studentIndex(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.findByUsername(userDetails.getUsername());
        List<Course> courses = courseService.findAll();
        model.addAttribute("firstName", currentUser.getFirstname());
        model.addAttribute("lastName", currentUser.getLastname());
        model.addAttribute("course", courses);
        return "index";
    }

    @GetMapping("/admin/courses/new")
    public String showCourseForm(Model model) {
        model.addAttribute("course", new Course());
        model.addAttribute("requisites", courseService.findAll());
        return "course-form";
    }

    @PostMapping("/admin/courses/new")
    public String createCourse(@ModelAttribute("course") Course course) {
        courseService.createCourse(course);
        return "redirect:/admin/courses/new";
    }

    @GetMapping("/admin/catalog/new")
    public String showCatalogForm(Model model) {
        model.addAttribute("catalog", new Catalog());
        model.addAttribute("courses", courseService.findAll());
        return "catalog-form";
    }

    @PostMapping("/admin/catalog/new")
    public String createCatalog(@ModelAttribute("catalog") Catalog catalog) {
        catalogService.createCatalog(catalog);
        return "redirect:/admin/catalog";
    }

    @GetMapping("/admin/catalog")
    public String showCatalog(Model model) {
        model.addAttribute("catalogs", catalogService.findAll());
        return "admin-catalog";
    }

    @GetMapping("/admin/course/{id}/edit")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Optional<Course> courseOptional = courseService.findById(id);
        if (courseOptional.isPresent()) {
            Course course = courseOptional.get();
            model.addAttribute("course", course);
            model.addAttribute("prerequisites", course.getPrerequisites());
            model.addAttribute("allCourses", courseService.findAll());

            List<Integer> standings = Arrays.asList(1, 2, 3, 4);
            model.addAttribute("standings", standings);

            return "admin-edit-course";
        } else {
            return "redirect:/admin/index";
        }
    }

    @PostMapping("/admin/course/{id}/edit")
    public String editCourseInHistory(@PathVariable("id") Long id, @ModelAttribute("course") Course updatedCourse,
            RedirectAttributes redirectAttributes) {
        Optional<Course> optionalCourse = courseService.findById(id);
        if (optionalCourse.isPresent()) {
            Course existingCourse = optionalCourse.get();
            existingCourse.setCourse_code(updatedCourse.getCourse_code());
            existingCourse.setCourse_title(updatedCourse.getCourse_title());
            existingCourse.setRequired_standing(updatedCourse.getRequired_standing());
            existingCourse.setPrerequisites(updatedCourse.getPrerequisites());

            courseService.createCourse(existingCourse);
            return "redirect:/admin/index";
        } else {
            return "redirect:/admin/index";
        }

    }

    @GetMapping("/admin/catalog/{id}/edit")
    public String editCatalogForm(@PathVariable("id") Long id, Model model) {
        Optional<Catalog> catalogOptional = catalogService.findById(id);
        if (catalogOptional.isPresent()) {
            model.addAttribute("catalog", catalogOptional.get());
            model.addAttribute("courses", courseService.findAll());
            return "admin-edit-catalog";
        } else {
            return "redirect:/admin/catalog";
        }
    }

    @PostMapping("/admin/catalog/{id}/edit")
    public String editCatalogg(@PathVariable("id") Long id, @ModelAttribute("catalog") Catalog updatedCatalog) {
        Optional<Catalog> existingCatalogOptional = catalogService.findById(id);
        if (existingCatalogOptional.isPresent()) {
            Catalog existinCatalog = existingCatalogOptional.get();
            existinCatalog.setCatalog_name(updatedCatalog.getCatalog_name());
            existinCatalog.setCourses(updatedCatalog.getCourses());

            catalogService.createCatalog(existinCatalog);
            return "redirect:/admin/catalog";
        } else {
            return "redirect:/admin/catalog";
        }
    }

    @GetMapping("/admin/users")
    public String showUsers(Model model) {
        model.addAttribute("users", userService.findUsersWithoutRoles());
        return "admin-users";
    }

}
