import sx.blah.discord.handle.obj.IMessage;

import java.time.LocalDateTime;


/**
 * Created by vivek on 2016-07-13.
 */
public class Quote {
    public static int number = 0;
    private String text;
    private String channel;
    private LocalDateTime time;

    public Quote(IMessage message) {
        text = message.getContent().substring(12);
        channel = message.getChannel().getName();
        time = message.getTimestamp();
    }

    //test
    public Quote() {
        text = "Now I am become Death, the destroyer of Worlds";
        channel = "Bhagavad Gita";
        time = LocalDateTime.MAX;
    }

    @Override
    public String toString() {
        return "Quote " + number + ":" + text + "\n" + " Created " + time.toString() + " in " + channel;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
