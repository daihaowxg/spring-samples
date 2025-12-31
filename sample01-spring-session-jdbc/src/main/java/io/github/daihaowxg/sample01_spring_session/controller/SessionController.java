package io.github.daihaowxg.sample01_spring_session.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/session")
public class SessionController {

    @PostMapping("/set")
    public Map<String, Object> setAttribute(
            @RequestParam String key,
            @RequestParam String value,
            HttpSession session) {

        session.setAttribute(key, value);

        Map<String, Object> result = new HashMap<>();
        result.put("sessionId", session.getId());
        result.put("key", key);
        result.put("value", value);
        return result;
    }

    @GetMapping("/get")
    public Map<String, Object> getAttribute(
            @RequestParam String key,
            HttpSession session) {

        Object value = session.getAttribute(key);

        Map<String, Object> result = new HashMap<>();
        result.put("sessionId", session.getId());
        result.put("key", key);
        result.put("value", value);
        return result;
    }

    @DeleteMapping("/invalidate")
    public Map<String, Object> invalidateSession(HttpSession session) {
        String sessionId = session.getId();
        session.invalidate();

        Map<String, Object> result = new HashMap<>();
        result.put("message", "Session invalidated");
        result.put("oldSessionId", sessionId);
        return result;
    }
}
