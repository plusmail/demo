package kroryi.demo.controller;

import kroryi.demo.Service.EmployeeService;
import kroryi.demo.Service.UserService;
import kroryi.demo.domain.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@Log4j2
public class HomeController {

    @Autowired
    EmployeeService employeeService;

    @Autowired
    UserService userService;

    @GetMapping("")
    public String home() {

        return "redirect:/board/list";
    }

    @GetMapping("/userfindall")
    public String userfindall(Model model) {

        List<User> userList = userService.findAll();
        userList.forEach((list)->{
            log.info("{}:{}", list.getId(), list.getUsername());
        });

        model.addAttribute("list", userList);
        return "home";
    }



    @GetMapping("/user")
    public String userUpdate() {
        User user = new User();
        user.setId(1L);
        user.setUsername("강감찬");
        user.setPassword("123456");
        user.setEmail("aaaa@abc.co.kr");
        user.setAddress("서울 특별시");
        User userUpdate = userService.updateUser(user);

       return "home";
    }

    @GetMapping("/userdelete")
    public String userDelete(){

        userService.deleteUser(1L);
        return "home";
    }

    @GetMapping("/saveDummyUsers")
    public String saveDummyUsers() {
        userService.saveDummyUser();
        return "home";
    }

//    @GetMapping("/findUserName")
//    public String findUserName() {
//        List<User> users = userService.findByName("사용자1");
//        log.info(users.toString());
//
//        return "home";
//    }

    @GetMapping("/findNameOrEmail")
    public String findNameOrEmail() {
        List<User> users = userService.findByUsernameOrEmail("사용자2", "user3@yi.or.kr");
        log.info(users.toString());
        return "home";
    }

    @GetMapping("/findByUsernameLikeOrderByIdDesc")
    public String findByUsernameLikeOrderByIdDesc(
            @RequestParam String page,
            @RequestParam String keyword,
            Model model
    ) {
        Pageable pageable = PageRequest.of(
                Integer.parseInt(page),
                10,
                Sort.by("id").descending());

        List<User> userList = userService.findByUsernameLikeOrderByIdDesc("%"+keyword+"%",pageable);
        userList.forEach((list)->{
          log.info("{}:{}", list.getId(), list.getUsername());
        });

        model.addAttribute("list", userList);

        return "home";
    }

    @GetMapping("/userInsertTr")
    public String userInsertTr() {

        try{
//            userService.createUsersWithTransaction();
            userService.createUsersWithoutTransaction();
        }catch (RuntimeException e){
            return "롤백발생 :" + e.getMessage();
        }


        return "home";
    }

}
