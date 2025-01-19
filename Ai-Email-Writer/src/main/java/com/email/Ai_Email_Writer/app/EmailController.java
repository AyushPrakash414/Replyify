package com.email.Ai_Email_Writer.app;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://127.0.0.1:5500")
@RestController
@RequestMapping("/api/email")             //  /api/email/generate
@AllArgsConstructor
public class EmailController {
 @Autowired
 private EmailService emailService;
    @PostMapping("/generate")
     public ResponseEntity<String> generateEmail(@RequestBody Email email)
     {
         String response= emailService.GenerateReply(email);
         return ResponseEntity.ok(response);
     }
}
