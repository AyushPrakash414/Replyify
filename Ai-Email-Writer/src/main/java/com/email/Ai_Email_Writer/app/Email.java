package com.email.Ai_Email_Writer.app;

import lombok.Data;

@Data      //--->Help to Create the Getter And Setter for Email Class (lombok)
public class Email {
     private String emailContent;
     private String tone;
}
