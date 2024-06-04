package ng.edu.aun.sca.service.implementation;

import ng.edu.aun.sca.model.User;
import ng.edu.aun.sca.repository.UserRepository;
import ng.edu.aun.sca.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
// import org.springframework.mail.javamail.JavaMailSender;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private UserRepository userRepository;

    // @Autowired
    // private JavaMailSender javaMailSender;

    @Override
    public void save(User user) {
        String encodedPassword = encoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        String token = UUID.randomUUID().toString();
        user.setVerificationToken(token);

        userRepository.save(user);

        // sendVerificationEmail(user.getEmail(), token);
    }

    @Override
    public void update(User user) {
        userRepository.save(user);
    }

    @Override
    public void saveAfterVerification(User user) {
        userRepository.save(user);
    }

    @Override
    public User findByUsername(String un) {
        return userRepository.findByEmail(un);
    }

    @Override
    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(username);
    }

    @Override
    public List<User> get() {
        return userRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.getCourseHistory().clear();
        userRepository.save(user);
        userRepository.deleteById(id);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public long countUsers() {
        return userRepository.count();
    }

    // private void sendVerificationEmail(String userEmail, String token) {
    // String verificationUrl = "http://localhost:8080/verify?token=" + token;

    // SimpleMailMessage message = new SimpleMailMessage();
    // message.setTo(userEmail);
    // message.setSubject("Email Verification");
    // message.setText("Please click the link below to verify your email address:\n"
    // + verificationUrl);

    // javaMailSender.send(message);
    // }

    @Override
    public User findByVerificationToken(String token) {
        return userRepository.findByVerificationToken(token);
    }

    @Override
    public List<User> findUsersWithoutRoles() {
        return userRepository.findByRolesEmpty();
    }
}
