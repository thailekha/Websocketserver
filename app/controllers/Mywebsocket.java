package controllers;

import static play.libs.F.Matcher.*;
import static play.mvc.Http.WebSocketEvent.*;

import play.Logger;
import play.mvc.Http.WebSocketClose;
import play.mvc.Http.WebSocketEvent;
import play.mvc.WebSocketController;

public class Mywebsocket extends WebSocketController {
	 
    public static void hello(String name) {
    	Logger.info("Socket method invoked");
        outbound.send("Hello! %s", name);
        
        while(inbound.isOpen()) {
            WebSocketEvent e = await(inbound.nextEvent());
            
            for(String quit: TextFrame.and(Equals("quit")).match(e)) {
                outbound.send("Bye!");
                disconnect();
            }
    
            for(String message: TextFrame.match(e)) {
            	Logger.info("Received msg");
                outbound.send("Echo: %s", message);
            }
            
            for(WebSocketClose closed: SocketClosed.match(e)) {
                Logger.info("Socket closed!");
            }
       }
        Logger.info("Socket closed!");
    }
}
