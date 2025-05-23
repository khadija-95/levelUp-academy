package com.levelup.levelup_academy.Controller;


import com.levelup.levelup_academy.Api.ApiResponse;
import com.levelup.levelup_academy.Model.User;
import com.levelup.levelup_academy.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class
UserController {

    private final UserService userService;

    @PostMapping("generate/{adminId}/{moderatorId}")
    public ResponseEntity generateModeratorLogin(@PathVariable Integer adminId, @PathVariable Integer moderatorId){
        userService.generateModeratorLogin(adminId, moderatorId);
        return ResponseEntity.status(200).body(new ApiResponse("Username and Password generated for moderator"));
    }

    @GetMapping("/get-all")
    public ResponseEntity getAllSubscriptions(@AuthenticationPrincipal User adminId) {
        return ResponseEntity.status(200).body(userService.getAllSubscriptions(adminId.getId()));
    }



}
