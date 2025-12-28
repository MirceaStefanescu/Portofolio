package com.example.realtimechat.controller;

import java.util.Map;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {
  @GetMapping("/me")
  public UserProfile me(OAuth2AuthenticationToken authentication) {
    Map<String, Object> attributes = authentication.getPrincipal().getAttributes();
    String name = attributeAsString(attributes, "name");
    String login = attributeAsString(attributes, "login");
    String email = attributeAsString(attributes, "email");

    return new UserProfile(
        name != null ? name : login,
        login,
        email
    );
  }

  private String attributeAsString(Map<String, Object> attributes, String key) {
    Object value = attributes.get(key);
    return value == null ? null : value.toString();
  }

  public record UserProfile(String displayName, String username, String email) {
  }
}
