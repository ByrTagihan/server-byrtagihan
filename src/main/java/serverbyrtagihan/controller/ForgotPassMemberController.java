package serverbyrtagihan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import serverbyrtagihan.dto.MessageMemberDTO;
import serverbyrtagihan.impl.ForgotPasswordMemberImpl;

import java.util.regex.Pattern;


@RestController
public class ForgotPassMemberController  {

    @Autowired
    private ForgotPasswordMemberImpl forgotPasswordMember;


    @PostMapping("/member/forgot_password")
    public ResponseEntity<MessageMemberDTO> forgotPassword(@RequestParam("hp") String hp) {
        forgotPasswordMember.forgotPassword(hp);
        MessageMemberDTO messageDTO = new MessageMemberDTO();
        messageDTO.setContent("Reset code has been sent to your phone number.");
        return ResponseEntity.ok(messageDTO);
    }
}