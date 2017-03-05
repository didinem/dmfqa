package org.didinem.web.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by didinem on 3/5/2017.
 */
@RestController
public class TestController {

    @RequestMapping("/test")
    public String test() {
        return "success";
    }

}
