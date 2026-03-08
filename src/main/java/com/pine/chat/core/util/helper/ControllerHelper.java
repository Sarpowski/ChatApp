package com.pine.chat.core.util.helper;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import java.util.UUID;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

public class ControllerHelper {
 public static UUID getCurrnetUserId() {
   var auth = SecurityContextHolder.getContext().getAuthentication();
   if (auth == null || auth.getPrincipal() == null) {
     throw new ResponseStatusException(UNAUTHORIZED, "not authentiacted");
   }
  return UUID.fromString((String) auth.getPrincipal());
 }
}
