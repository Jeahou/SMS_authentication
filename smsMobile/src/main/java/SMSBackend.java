import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import static spark.Spark.*;

public class SMSBackend {
    public static final String ACCOUNT_SID = "ACdb62c7743bbf04a2379fcd885137350a";
    public static final String AUTH_TOKEN = "";

    public static void main(String[] args){
        get("/", (req, res) -> "Hello, ';;;;");

        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        post("/sms", (req, res) ->{
            System.out.println("ok");
           String body = req.queryParams("Body");
           String to = "+82"+req.queryParams("To");
           String from = "+19388883453";
           Message message = Message.creator(
                            new PhoneNumber(to),
                            new PhoneNumber(from),
                   body).create();
            System.out.println(message.getSid());
           return message.getSid();
        });

    }
}
