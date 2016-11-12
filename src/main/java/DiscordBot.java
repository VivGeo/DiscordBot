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
        String name[][] = new String[10][2] =
        {
            ["4277","Oskar"],         
            ["2966","Astol"],
            ["5510","Grald"],
            ["0810","Fosco"],
            ["0484","Khiron"],
            ["5473","Mightus"],
            ["7284","Strymash"],
            ["2866","Kriv"],
            ["4343","DM"],
            ["5645","Sunshine"]
        }

        for (int i = 0; i < name.length; i++)
        {
            characters.put(name[i][0],name[i][0]);   
            dailies.put(name[i][0],LocalDateTime.now().minusDays(1));
            credits.put(name[i][0], 0);
            crits.put(name[i][0],0);
            critFails.put(name[i][0],0);
            
        }
        
        client = new ClientBuilder().withToken(/*BOT TOKEN*/).login();
        client.getDispatcher().registerListener(new AnnotationListener(characters,dailies,credits,crits,critFails));

    }
}
