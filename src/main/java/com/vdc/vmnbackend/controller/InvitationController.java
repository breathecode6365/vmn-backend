package com.vdc.vmnbackend.controller;

import com.vdc.vmnbackend.dao.InvitationDAO;
import com.vdc.vmnbackend.dao.UserDAO;
import com.vdc.vmnbackend.dto.req.InviteBasedUserReqDTO;
import com.vdc.vmnbackend.dto.req.UserInviteReqDTO;
import com.vdc.vmnbackend.dto.res.BasicResDTO;
import com.vdc.vmnbackend.dto.res.ResponseDTO;
import com.vdc.vmnbackend.enumerators.Roles;
import com.vdc.vmnbackend.service.InvitationService;
import com.vdc.vmnbackend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("invite")
public class InvitationController {
    public InvitationService invitationService;
    public UserService userService;
    //dependency Injection
    public InvitationController(InvitationService invitationService, UserService userService){
        this.invitationService = invitationService;
        this.userService = userService;
    }
    @PostMapping("new")
    public BasicResDTO createInvite(Authentication authentication, @RequestBody @Valid UserInviteReqDTO userInviteReqDTO){
        if(userInviteReqDTO.getRole().toString()!= Roles.USER.toString() && userInviteReqDTO.getRole().toString()!= Roles.ADMIN.toString()){
            return new BasicResDTO("Invalid Role - Selected role cannot be invited", HttpStatus.BAD_REQUEST);
        }
        UserDAO userDAO = userService.getByEmail(authentication.getName());
        return invitationService.createInvite(userInviteReqDTO, userDAO);
    }

    @GetMapping("verify-invite")
    public ResponseDTO<InvitationDAO> verifyInvite(@RequestParam("token") UUID token){
        return invitationService.verifyInvitation(token);
    }
    @PostMapping("accecpt-invitation")
    public void createUserByInvitation(@RequestBody @Valid InviteBasedUserReqDTO inviteBasedUserReqDTO){
        invitationService.createUserByInvitation(inviteBasedUserReqDTO);
    }


}