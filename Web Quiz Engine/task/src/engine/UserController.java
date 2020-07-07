package engine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    CompletedQuizService completedQuizService;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping(path = "/api/register")
    public ResponseEntity<String> register(@Valid @RequestBody UserDao userDao) {
        if (userService.existsByEmail(userDao.getEmail())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setEmail(userDao.getEmail());
        user.setPassword(passwordEncoder.encode(userDao.getPassword()));
        userService.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
