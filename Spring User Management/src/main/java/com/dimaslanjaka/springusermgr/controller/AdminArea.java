package com.dimaslanjaka.springusermgr.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dimaslanjaka.springusermgr.entities.User;
import com.dimaslanjaka.springusermgr.entities.UserDto;
import com.dimaslanjaka.springusermgr.entities.UserService;

import jakarta.validation.Valid;

@Controller
public class AdminArea {
  @Autowired
  private UserService userService;

  public AdminArea(UserService userService) {
    this.userService = userService;
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/add/save")
  public String addUser(
      @Valid @ModelAttribute("user") UserDto userDto,
      BindingResult result,
      Model model) {
    User existingUser = userService.findUserByEmail(userDto.getEmail());

    if (existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()) {
      result.rejectValue("email", null,
          "There is already an account registered with the same email");
    }

    if (!userDto.getPassword().isEmpty()) {
      if (userDto.getPassword().length() < 7) {
        result.rejectValue("password", "field.min.length", "Password should have at least 7 characters");
      }
    } else {
      result.rejectValue("password", "field.min.length", "Password should not be empty.");
    }

    if (result.hasErrors()) {
      model.addAttribute("user", userDto);
      return "add";
    }

    userService.saveUser(userDto);
    return "redirect:/users?success=true";
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/add")
  public String showUserAddForm(Model model) {
    UserDto user = new UserDto();
    model.addAttribute("user", user);
    return "add";
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/users")
  public String users(Model model) {
    List<UserDto> users = userService.findAllUsers();
    model.addAttribute("users", users);
    return "users";
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/edit/{id}")
  public String editUser(
      @PathVariable Long id,
      Model model) {
    UserDto user = userService.findUserById(id);
    model.addAttribute("user", user);
    return "edit";
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/edit/{id}")
  public String updateUserById(
      @Valid @ModelAttribute("user") UserDto updatedUserDto,
      BindingResult result,
      @PathVariable Long id,
      Model model) {

    if (!updatedUserDto.getPassword().isEmpty()) {
      if (updatedUserDto.getPassword().length() < 7) {
        result.rejectValue("password", "field.min.length", "Password should have at least 7 characters");
      }
    }

    if (result.hasErrors()) {
      model.addAttribute("user", updatedUserDto);
      return "edit";
    }

    userService.editUser(updatedUserDto, id);
    return "redirect:/users";
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/delete/{id}")
  public String deleteUser(
      @RequestParam(name = "error", required = false) String error,
      @RequestParam(name = "success", required = false) String success,
      RedirectAttributes redirectAttributes,
      @PathVariable Long id,
      Principal principal,
      Model model) {

    String loggedInUsername = principal.getName();

    User loggedInUser = userService.findUserByEmail(loggedInUsername);

    if (loggedInUser != null && loggedInUser.getId().equals(id)) {
      if (error != null) {
        redirectAttributes.addFlashAttribute("error", "You cannot delete yourself.");
      }
    } else {
      if (userService.doesUserExist(id)) {
        userService.deleteUserById(id);
        if (success != null) {
          redirectAttributes.addFlashAttribute("success", "User has been deleted successfully");
        }
      } else {
        if (error != null) {
          redirectAttributes.addFlashAttribute("error", "User does not exist");
        }
      }
    }
    return "redirect:/users";
  }
}
