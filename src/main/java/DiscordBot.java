import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.util.DiscordException;

import java.util.HashMap;

import java.time.LocalDateTime;
/**
 * Created by vivek on 2016-07-07.
 */
public class DiscordBot {
    static IDiscordClient client;
    public static HashMap<String, String> characters = new HashMap<String,String>();
    private static HashMap<String, LocalDateTime> dailies = new HashMap<String, LocalDateTime>();
    private static HashMap<String, Integer> credits = new HashMap<String, Integer>();
    private static HashMap<String,Integer> crits = new HashMap<String,Integer>();
    private static HashMap<String, Integer> critFails = new HashMap<String,Integer>();
    private static LocalDateTime time = LocalDateTime.now();

    public static void main(String[] args) throws DiscordException {
        characters.put("4277","Oskar");
        characters.put("2966","Astol");
        characters.put("5510","Grald");
        characters.put("0810","Fosco");
        characters.put("0484","Khiron");
        characters.put("5473","Mightus");
        characters.put("7284","Strymash");
        characters.put("2866","Kriv");
        characters.put("4343","DM");
        characters.put("5645","Sunshine");

        dailies.put("4277",LocalDateTime.now().minusDays(1));
        dailies.put("2966",LocalDateTime.now().minusDays(1));
        dailies.put("5510",LocalDateTime.now().minusDays(1));
        dailies.put("0810",LocalDateTime.now().minusDays(1));
        dailies.put("0484",LocalDateTime.now().minusDays(1));
        dailies.put("5473",LocalDateTime.now().minusDays(1));
        dailies.put("7284",LocalDateTime.now().minusDays(1));
        dailies.put("2866",LocalDateTime.now().minusDays(1));
        dailies.put("4343",LocalDateTime.now().minusDays(1));
        dailies.put("5645",LocalDateTime.now().minusDays(1));

        credits.put("4277",0);
        credits.put("2966",0);
        credits.put("5510",0);
        credits.put("0810",0);
        credits.put("0484",0);
        credits.put("5473",0);
        credits.put("7284",0);
        credits.put("2866",0);
        credits.put("4343",0);
        credits.put("5645",0);

        crits.put("4277",0);
        crits.put("2966",0);
        crits.put("5510",0);
        crits.put("0810",0);
        crits.put("0484",0);
        crits.put("5473",0);
        crits.put("7284",0);
        crits.put("2866",0);
        crits.put("4343",0);
        crits.put("5645",0);

        critFails.put("4277",0);
        critFails.put("2966",0);
        critFails.put("5510",0);
        critFails.put("0810",0);
        critFails.put("0484",0);
        critFails.put("5473",0);
        critFails.put("7284",0);
        critFails.put("2866",0);
        critFails.put("4343",0);
        critFails.put("5645",0);
        client = new ClientBuilder().withToken(/*BOT TOKEN*/).login();
        client.getDispatcher().registerListener(new AnnotationListener(characters,dailies,credits,crits,critFails));

    }
}
