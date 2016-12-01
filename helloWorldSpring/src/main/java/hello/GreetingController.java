package hello;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class GreetingController {

    private static String TEMPLATE = "Hello %s!";
    private final AtomicInteger counter = new AtomicInteger();

    @RequestMapping(method = RequestMethod.GET)
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        String msg = String.format(TEMPLATE, name);
        return new Greeting(counter.addAndGet(1), msg);
    }
}
