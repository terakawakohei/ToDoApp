package jp.kobespiral.odajin.todo.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class LoginForm {
   @NotBlank
   String mid;

   @NotBlank
   @Size(min = 8)
   String password;
}
