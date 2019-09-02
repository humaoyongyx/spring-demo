package issac.study.springdemo.web.message;

import issac.study.springdemo.core.config.websocket.MyWebSocketHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author humy6
 */
@Component
public class MessageReceiver {

    public void onMessage(String msg) throws IOException {
        System.out.println("msg:"+msg);
        MyWebSocketHandler.sendMessage("Tony",msg);
    }
}
