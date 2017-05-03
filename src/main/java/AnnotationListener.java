import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IPrivateChannel;
import sx.blah.discord.handle.obj.Status;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;

/**
 * Created by vivek on 2016-07-07.
 */
public class AnnotationListener {
    private Quote quote;

    private static HashMap<String, String> characters;
    private static HashMap<String, LocalDateTime> dailies;
    private static HashMap<String, Integer> credits;
    private static HashMap<Integer, Quote> quotes;
    private static HashMap<String, Integer> crits;
    private static HashMap<String, Integer> critFails;
    private static String message;
    private static String light;
    private static IPrivateChannel channel;
    private static int num;
    private static int rolls;
    private static int sides;
    private static int roll;
    private static int sum;
    private static String output;
    private static String[] input;
    private static int amount;
    private static long hours, minutes;
    private static Duration duration;
    private static int index;

    private enum Bet {ON, OFF}

    private static Bet bet = Bet.OFF;
    private static String better;
    private static int reward;
    private static String spell;

    public AnnotationListener(HashMap<String, String> characters, HashMap<String, LocalDateTime> dailies, HashMap<String, Integer> credits, HashMap<String, Integer> crits, HashMap<String, Integer> critFails) {

        quotes = new HashMap<Integer, Quote>();

        quotes.put(0, new Quote());
        AnnotationListener.characters = characters;
        AnnotationListener.dailies = dailies;
        AnnotationListener.credits = credits;
        AnnotationListener.crits = crits;
        AnnotationListener.critFails = critFails;


    }

    //Greeting users
    private String greeting(String name) {
        String reply;
        switch ((int) (Math.random() * 4)) {
            case 0:
                reply = "Hey there, " + name + "!";
                break;
            case 1:
                reply = "Hello, " + name + ".";
                break;
            case 2:
                reply = "What's up, " + name + "?";
                break;
            case 3:
                reply = "Greetings, " + name + ".";
                break;
            default:
                reply = "Hi.";
        }
        return reply;
    }

    //Reformatting duration to user-friendly format
    private String timeString(Duration duration) {
        hours = duration.toHours();
        minutes = duration.minusHours(hours).toMinutes();
        return hours + " hours " + minutes + " minutes";
    }

    //Converts user unique discriminator to user's title/name of character
    private String getName(IMessage message) {
        return characters.get(message.getAuthor().getDiscriminator());
    }


    //On start
    @EventSubscriber
    public void onReady(ReadyEvent event) {
        try {
            DiscordBot.client.changeUsername("Robo-Gary");
            DiscordBot.client.changeStatus(Status.game("D&D!"));
        } catch (RateLimitException | DiscordException e) {
            e.printStackTrace();
        }
    }

    //List of possible commands with a message is sent in the server
    @EventSubscriber
    public void onMessageEvent(MessageReceivedEvent event) {
        try {
            message = event.getMessage().getContent().toLowerCase();
            //Making sure message is a command
            if (message.substring(0, 2).startsWith(";")) {
                //Output list of commands
                if (message.contains(";help")) {
                    new MessageBuilder(DiscordBot.client).withChannel(event.getMessage().getChannel()).withContent(";greetings\n;guidance \n;orison \n;light [object]\n;dailies\ncredits\n;1d20\n;quote 0\n0!quote add \n;quote edit \n;quote del +\n;coin + \n;bet").build();
                    System.out.println(event.getMessage().getAuthor().getID());
                }
                //Daily rewards users may collect
                else if (message.contains(";dailies")) {
                    if (LocalDateTime.now().isAfter((dailies.get(event.getMessage().getAuthor().getDiscriminator()).plusDays(1)))) {
                        amount = credits.get(event.getMessage().getAuthor().getDiscriminator()) + 200;
                        credits.put(event.getMessage().getAuthor().getDiscriminator(), amount);
                        new MessageBuilder(DiscordBot.client).withChannel(":atm: " + event.getMessage().getChannel()).withContent(getName(event.getMessage()) + ",:dollar:  $200 has been added.").build();
                        dailies.put(event.getMessage().getAuthor().getDiscriminator(), LocalDateTime.now());
                    } else

                    {
                        new MessageBuilder(DiscordBot.client).withChannel(event.getMessage().getChannel()).withContent("You can get more credits in another " + timeString(Duration.between(LocalDateTime.now().minusDays(1), LocalDateTime.now()).minus(Duration.between(dailies.get(event.getMessage().getAuthor().getDiscriminator()), LocalDateTime.now())))).build();


                    }
                }
                //Dice rolls
                else if (Character.isDigit(message.charAt(2)) && message.indexOf("d") != -1) {
                    try {
                        input = message.split(" ");
                        //Addressing dice roller
                        output = getName(event.getMessage()) + " you rolled ";
                        sum = 0;
                        //parsing input to get number of dice rolls and number of sides on the dice
                        rolls = Integer.parseInt(message.substring(2, message.indexOf("d")));
                        if (input.length > 2) {
                            sides = Integer.parseInt(message.substring(message.indexOf("d") + 1, message.indexOf(" ")));
                        } else
                            sides = Integer.parseInt(message.substring(message.indexOf("d") + 1));
                        System.out.println(sides);
                        //For multiple dice rolls
                        for (int i = 0; i < rolls - 1; i++) {
                            roll = (int) (Math.random() * sides) + 1;
                            System.out.println("Sides:" + sides + " Roll:" + roll);
                            if (sides == 20) {

                                if (roll == 20) {
                                    num = crits.get(event.getMessage().getAuthor().getDiscriminator());
                                    crits.put(event.getMessage().getAuthor().getDiscriminator(), num + 1);
                                } else if (roll == 1) {
                                    num = critFails.get(event.getMessage().getAuthor().getDiscriminator());
                                    critFails.put(event.getMessage().getAuthor().getDiscriminator(), num + 1);
                                }
                            }
                            output += roll + ", ";
                            sum += roll;
                        }
                        roll = (int) (Math.random() * sides) + 1;
                        //Add to the criticals record if necessary
                        if (sides == 20) {

                            if (roll == 20) {
                                num = crits.get(event.getMessage().getAuthor().getDiscriminator());
                                crits.put(event.getMessage().getAuthor().getDiscriminator(), num + 1);

                            } else if (roll == 1) {
                                num = critFails.get(event.getMessage().getAuthor().getDiscriminator());
                                critFails.put(event.getMessage().getAuthor().getDiscriminator(), num + 1);

                            }
                        }
                        output += roll + " ";
                        sum += roll;
                        System.out.println(input.length);
                        //In case any dice modifiers are added
                        if (input.length > 2) {

                            if (input[1].equals("+")) {
                                output += "+ " + input[2] + " ";
                                sum += Integer.parseInt(input[2]);
                            } else if (input[1].equals("-")) {
                                output += "- " + input[2] + " ";
                                sum -= Integer.parseInt(input[2]);
                            }
                        }
                        //Output total
                        output += "for a total of " + sum;
                        new MessageBuilder(DiscordBot.client).withChannel(event.getMessage().getChannel()).withContent(output).build();

                    } catch (Exception e) {
                        new MessageBuilder(DiscordBot.client).withChannel(event.getMessage().getChannel()).withContent("ERROR ERROR SELF DESTRUCTION IMMINENT (Invalid Input)").build();
                    }
                }
                //Outputs user's number of points
                else if (message.contains(";credits")) {
                    new MessageBuilder(DiscordBot.client).withChannel(event.getMessage().getChannel()).withContent(getName(event.getMessage()) + ", you have :dollar: $" + credits.get(event.getMessage().getAuthor().getDiscriminator())).build();
                }
                //outputs the randomized greeting
                else if (message.contains(";greeting")) {
                    new MessageBuilder(DiscordBot.client).withChannel(event.getMessage().getChannel()).withContent(greeting(getName(event.getMessage()))).build();
                }
                //Parses a webpage of an online library of D&D 5e spells for the spell description and outputs it
                else if (message.contains(";spell")) {
                    String text = "";
                    if (message.charAt(6) == ' ') {
                        try {
                            spell = message.substring(message.indexOf(" ") + 1);
                            Document doc = Jsoup.connect("https://open5e.com/Spellcasting/spells_a-z/" + spell.charAt(0) + "/" + spell.replace(" ", "-") + ".html").get();
                            Elements spellDesc = doc.select("div.section p");
                            text = "**" + spell.toUpperCase() + "**\n\n";
                            for (int i = 0; i < spellDesc.size(); i++) {
                                if (!spellDesc.get(i).text().contains(":")) {
                                    index = i;
                                    break;
                                }
                            }
                            for (int i = 0; i < index; i++) {
                                text += "*" + spellDesc.get(i).text().replace(":", ":*") + "\n";
                            }
                            text += "\n";
                            for (int i = index; i < spellDesc.size(); i++) {
                                text += spellDesc.get(i).text() + "\n\n";
                            }

                            new MessageBuilder(DiscordBot.client).withChannel(event.getMessage().getChannel()).withContent(text).build();
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println(text);
                            new MessageBuilder(DiscordBot.client).withChannel(event.getMessage().getChannel()).withContent("Invalid Input").build();
                        }
                    } else if (message.startsWith(";spells ")) {
                        try {
                            String[] arguments = message.split("\\s+");
                            Document doc = Jsoup.connect("https://open5e.com/Spellcasting/by-class/" + arguments[1] + ".html").get();
                            Elements spells = doc.select("div.section");
                            Element level = spells.get(Integer.parseInt(arguments[2]) + 1);
                            Elements spellList = level.select("li");
                            text = "**LEVEL " + arguments[2] + " " + arguments[1].toUpperCase() + " SPELLS**\n";
                            for (int i = 0; i < spellList.size(); i++) {
                                System.out.println(spellList.get(i).text());
                                text += spellList.get(i).text() + "\n";
                            }
                            new MessageBuilder(DiscordBot.client).withChannel(event.getMessage().getChannel()).withContent(text).build();
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println(text);
                            new MessageBuilder(DiscordBot.client).withChannel(event.getMessage().getChannel()).withContent("Invalid Input").build();
                        }
                    }
                }
                //D&D custom commands for my particular character
                else if (message.contains(";guidance")) {
                    new MessageBuilder(DiscordBot.client).withChannel(event.getMessage().getChannel()).withContent("Oskar casts guidance on " + getName(event.getMessage()) + ".").build();
                } else if (message.contains(";orison")) {
                    new MessageBuilder(DiscordBot.client).withChannel(event.getMessage().getChannel()).withContent("Oskar casts orison on " + getName(event.getMessage()) + ".").build();
                } else if (message.contains(";light")) {
                    light = message.substring(8);
                    new MessageBuilder(DiscordBot.client).withChannel(event.getMessage().getChannel()).withContent("Oskar casts light on " + getName(event.getMessage()) + "'s " + light + ".").build();
                }
                //User can see the critical records
                else if (message.contains(";crits")) {
                    new MessageBuilder(DiscordBot.client).withChannel(event.getMessage().getChannel()).withContent("Oskar casts light on " + getName(event.getMessage()) + "'s " + light + ".").build();
                }
                //Chat quote management system, where users can save quotes of chat messages and view, edit or delete them later
                else if (message.contains(";quote")) {
                    if (message.contains(";quote add ")) {
                        quote = new Quote(event.getMessage());
                        Quote.number++;
                        quotes.put(Quote.number, quote);
                        new MessageBuilder(DiscordBot.client).withChannel(event.getMessage().getChannel()).withContent("Quote " + Quote.number + " added.").build();
                    } else if (message.contains(";quote edit ")) {
                        num = Integer.parseInt(message.split(" ")[2]);
                        quotes.get(num).setText(event.getMessage().getContent().substring(12));
                        new MessageBuilder(DiscordBot.client).withChannel(event.getMessage().getChannel()).withContent("Quote " + num + " edited.").build();
                    } else if (message.contains(";quote del ")) {
                        num = Integer.parseInt(message.split(" ")[2]);
                        quotes.get(num).setText("This quote no longer exists");
                        new MessageBuilder(DiscordBot.client).withChannel(event.getMessage().getChannel()).withContent("Quote " + num + " removed.").build();
                    } else {
                        num = Integer.parseInt(message.split(" ")[1]);
                        new MessageBuilder(DiscordBot.client).withChannel(event.getMessage().getChannel()).withContent(quotes.get(num).getText()).build();

                    }
                }
                //Messages the result of a coin toss
                else if (message.contains(";coin")) {
                    if ((int) (Math.random() * 2) == 1)
                        new MessageBuilder(DiscordBot.client).withChannel(event.getMessage().getChannel()).withContent("Heads").build();
                    else
                        new MessageBuilder(DiscordBot.client).withChannel(event.getMessage().getChannel()).withContent("Tails").build();
                }
                //Two users may bet against each other on a coin toss and exchange credits according to the result
                else if (message.contains(";bet")) {
                    if (bet == Bet.OFF) {
                        bet = Bet.ON;
                        better = event.getMessage().getAuthor().getDiscriminator();
                    } else if (bet == Bet.ON) {
                        if (Math.min(credits.get(event.getMessage().getAuthor().getDiscriminator()), credits.get(better)) >= 50) {
                            reward = 50;
                        } else
                            reward = credits.get(event.getMessage().getAuthor().getDiscriminator());
                        if ((int) (Math.random() * 2) == 1) {
                            new MessageBuilder(DiscordBot.client).withChannel(event.getMessage().getChannel()).withContent(getName(event.getMessage()) + " you won the coin toss. You have been rewarded $" + reward + " of " + characters.get(better) + " credits.").build();
                            credits.put(event.getMessage().getAuthor().getDiscriminator(), credits.get(event.getMessage().getAuthor().getDiscriminator()) + reward);
                            credits.put(better, credits.get(better) - reward);
                        } else {
                            new MessageBuilder(DiscordBot.client).withChannel(event.getMessage().getChannel()).withContent(characters.get(better) + " you won the coin toss. You have been rewarded $" + reward + " of " + getName(event.getMessage()) + " credits.").build();
                            credits.put(event.getMessage().getAuthor().getDiscriminator(), credits.get(event.getMessage().getAuthor().getDiscriminator()) - reward);
                            credits.put(better, credits.get(better) + reward);

                        }
                        bet = Bet.OFF;
                    }
                } else {
                    new MessageBuilder(DiscordBot.client).withChannel(event.getMessage().getChannel()).withContent("Invalid Input").build();
                }

            }
        } catch (RateLimitException | DiscordException | MissingPermissionsException e) {
            e.printStackTrace();

        }
    }


}