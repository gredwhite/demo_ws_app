package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.HtmlUtils;

import java.security.Principal;

@Controller
@EnableScheduling
public class GreetingController {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(@Payload HelloMessage message, Principal principal) throws Exception {
        Thread.sleep(1000); // simulated delay
        simpMessagingTemplate.convertAndSendToUser(principal.getName(), "/queue/greetings", new Greeting("Ololo"));
        return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
    }

    static int counter = 1;

    //@Scheduled(fixedRate = 5000)
    public void test() {
        simpMessagingTemplate.convertAndSend("/topic/greetings", new Greeting("Fixed rate:" + counter++));
        simpMessagingTemplate.convertAndSendToUser("user1", "/topic/greetings", new Greeting("Fixed rate:" + counter));

    }

   /* @RequestMapping("/sender")
    public String sender() {
        return "sender";
    }*/
}
