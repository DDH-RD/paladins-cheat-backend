package dev.luzifer.spring;

import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("error")
public class ErrorController {

    @RequestMapping("/")
    public String error() {
        return "error, bra. 040 or soemthng idk";
    }

}
