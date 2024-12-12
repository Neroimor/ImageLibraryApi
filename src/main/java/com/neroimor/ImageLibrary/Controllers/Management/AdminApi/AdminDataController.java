package com.neroimor.ImageLibrary.Controllers.Management.AdminApi;

import com.neroimor.ImageLibrary.Models.PageData.PagesDataInfo;
import com.neroimor.ImageLibrary.Models.UsersModels.UserData;
import com.neroimor.ImageLibrary.Services.Admin.GetUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/data")
@Slf4j
public class AdminDataController {

    private final GetUserService getUserService;

    @Autowired
    public AdminDataController(GetUserService getUserService) {
        this.getUserService = getUserService;
    }

    @GetMapping("/getUser/{email}")
    public ResponseEntity<String> getUserByEmail(@PathVariable String email){
        log.info("Получение пользователя по email");
        return getUserService.getUser(email);
    }

    @GetMapping("/getAllUser")
    public List<UserData> getAllUserData(@RequestParam int page, @RequestParam int size){
        log.info("Начато получение всех пользователей");
        return getUserService.getAllUsers(page, size);
    }

    @GetMapping("/getPages")
    public PagesDataInfo getPageData(@RequestParam int page, @RequestParam int size){
        log.info("Начато получение данных о количестве страниц и данных");
        return getUserService.getPagesDataInfo(page,size);
    }

    @GetMapping("/test")
    public ResponseEntity<String> getPageData(){
        return ResponseEntity.ok().body("test");
    }



}
