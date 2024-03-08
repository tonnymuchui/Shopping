package org.shopping.site.admin.user.controller;


import jakarta.servlet.http.HttpServletResponse;
import org.shopping.entity.Role;
import org.shopping.entity.User;
import org.shopping.site.admin.export.UserCsvExporter;
import org.shopping.site.admin.export.UserExcelExporter;
import org.shopping.site.admin.export.UserPdfExporter;
import org.shopping.site.admin.paging.PagingAndSortingHelper;
import org.shopping.site.admin.paging.PagingAndSortingParam;
import org.shopping.site.admin.user.UserNotFoundException;
import org.shopping.site.admin.user.UserService;
import org.shopping.site.admin.util.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
public class UserController {
    private final String defaultRedirectURL = "redirect:/users/page/1?sortField=firstName&sortDir=asc";
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public String listFirstPage() {
        return defaultRedirectURL;
    }
    @GetMapping("/users/page/{pageNum}")
    public String listByPage(
            @PagingAndSortingParam(listName = "listUsers", moduleURL = "/users") PagingAndSortingHelper helper,
            @PathVariable(name = "pageNum") int pageNum) {
        userService.listByPage(pageNum, helper);

        return "users/users";
    }
    private String getRedirectURLtoAffectedUser(User user) {
        String firstPartOfEmail = user.getEmail().split("@")[0];
        return "redirect:/users/page/1?sortField=id&sortDir=asc&keyword=" + firstPartOfEmail;
    }
    @PostMapping("/users/save")
    public String saveUser(User user, RedirectAttributes redirectAttributes, @RequestParam("image") MultipartFile multipartFile) throws Exception {
        if (!multipartFile.isEmpty()){
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            user.setPhotos(fileName);
            User savedUser = userService.save(user);
            String uploadDir = "user-photos" + savedUser.getId();
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir,fileName,multipartFile);
        } else {
            if (user.getPhotos().isEmpty()) user.setPhotos(null);
            userService.save(user);
        }
        redirectAttributes.addFlashAttribute("message", "The user has been saved successfully.");

        return getRedirectURLtoAffectedUser(user);
    }

    @GetMapping("/users/new")
    public String newUser(Model model) {
        List<Role> listRoles = userService.listAllRoles();

        User user = new User();
        user.setEnabled(true);

        model.addAttribute("user", user);
        model.addAttribute("listRoles", listRoles);
        model.addAttribute("pageTitle", "Create New User");

        return "users/user_form";
    }
    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable(name = "id") Integer id,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        try {
            Optional<User> user = userService.get(id);
            List<Role> listRoles = userService.listAllRoles();

            model.addAttribute("user", user);
            model.addAttribute("pageTitle", "Edit User (ID: " + id + ")");
            model.addAttribute("listRoles", listRoles);

            return "users/user_form";
        } catch (UserNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return "users/user_form";
        }
    }
    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable(name = "id") Integer id,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        try {
            userService.delete(id);
            redirectAttributes.addFlashAttribute("message",
                    "The user ID " + id + " has been deleted successfully");
        } catch (UserNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
        }

        return "users/user_form";
    }
    @GetMapping("/users/{id}/enabled/{status}")
    public String updateUserEnabledStatus(@PathVariable("id") Integer id,
                                          @PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes) {
        userService.updateUserEnabledStatus(id, enabled);
        String status = enabled ? "enabled" : "disabled";
        String message = "The user ID " + id + " has been " + status;
        redirectAttributes.addFlashAttribute("message", message);

        return "users/user_form";
    }

    @GetMapping("/users/export/csv")
    public void exportToCSV(HttpServletResponse response) throws Exception {
        List<User> listUsers = userService.listAllUsers();
        UserCsvExporter exporter = new UserCsvExporter();
        exporter.export(listUsers, response);
    }
    @GetMapping("/users/export/excel")
    public void exportToExcel(HttpServletResponse response) throws Exception {
        List<User> listUsers = userService.listAllUsers();

        UserExcelExporter exporter = new UserExcelExporter();
        exporter.export(listUsers, response);
    }

    @GetMapping("/users/export/pdf")
    public void exportToPDF(HttpServletResponse response) throws Exception {
        List<User> listUsers = userService.listAllUsers();

        UserPdfExporter exporter = new UserPdfExporter();
        exporter.export(listUsers, response);
    }
}
