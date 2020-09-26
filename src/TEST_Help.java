import com.sun.media.sound.EmergencySoundbank;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.EmbedType;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")

public class TEST_Help extends ListenerAdapter {
    public String BotPrefix = Bot.BotPrefix;
    public String BotName = Bot.BotName;
    public ZonedDateTime LocalTime = ZonedDateTime.now();
    public String ColorHexCode = Bot.ColorHexCode;
    public String BotLogo = Bot.BotLogo;
    public String TicketCreateChannelID = Bot.TicketCreateChannelID;
    public String TicketCreateCategoryChannelID = Bot.TicketCreateCategoryChannelID;
    public String SupportTeamRoleID = Bot.SupportTeamRoleID;
    public String AdminRoleID = Bot.AdminRoleID;
    public String MemberRoleID = Bot.MemberRoleID;
    public String HelpMessageID = "";
    public boolean StaffPingEnabled = Bot.StaffPingEnabled;
    public String WaterMark = Bot.WaterMark;
    int SecondsPassed = 0;

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        String[] message = e.getMessage().getContentRaw().split(" ");
        if (message.length > 0 && message[0].equalsIgnoreCase(Bot.BotPrefix + "help")) {
            ArrayList<String> BlacklistedGet = new ArrayList<>();
            if (new File("BlacklistedUsers.json").exists()) {
                try {
                    JSONObject json = (JSONObject) new JSONParser().parse(new FileReader(new File("BlacklistedUsers.json")));
                    BlacklistedGet = (ArrayList<String>) json.get("BlacklistedUsers");
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
            }
            boolean isBlacklisted = false;
            for (String s : BlacklistedGet) {
                if (e.getAuthor().getId().equals(s)) {
                    isBlacklisted = true;
                }
            }
            if (isBlacklisted) {
                EmbedBuilder UserBlacklisted = new EmbedBuilder().setTitle("Error").setThumbnail(Bot.BotLogo).setFooter(Bot.WaterMark, Bot.BotLogo).setTimestamp(Bot.now).setColor(Color.RED).setDescription("**" + e.getAuthor().getAsTag() + "**, *You are blacklisted from using all commands, \n" + "If you think this is an error please contact a staff member!*");
                e.getChannel().sendMessage(UserBlacklisted.build()).queue(message3 -> {
                    e.getMessage().delete().queue();
                    message3.addReaction("❌").queue();
                    message3.delete().queueAfter(10, TimeUnit.SECONDS);
                });
                return;
            }
        }
        try {
            JSONObject json = (JSONObject) new JSONParser().parse(new FileReader(new File("Tickets/config.json")));
            BotPrefix = ((HashMap<String, String>) json.get("GeneralConfig")).get("BotPrefix");
            BotName = ((HashMap<String, String>) json.get("GeneralConfig")).get("BotName");
            BotLogo = ((HashMap<String, String>) json.get("GeneralConfig")).get("BotLogo");
            ColorHexCode = ((HashMap<String, String>) json.get("GeneralConfig")).get("ColorHexCode");

            TicketCreateChannelID = ((HashMap<String, String>) json.get("ChannelIDS")).get("TicketCreateChannelID");
            TicketCreateCategoryChannelID = ((HashMap<String, String>) json.get("ChannelIDS")).get("TicketCreateCategoryChannelID");
        } catch (Exception ee) {
            ee.printStackTrace();
        }
        Color Color = java.awt.Color.decode(ColorHexCode);

        if (e.getMessage().getContentRaw().equalsIgnoreCase(BotPrefix + "help")) {


            EmbedBuilder EmbedRules = new EmbedBuilder();

            EmbedRules.setTitle(BotName + " Help Page")
                    .setDescription("**React to the corresponding emoji to get more help on that subject!**\n\n" +
                            ":one: » **Basic Commands**\n" +
                            ":two: » **Moderation Commands**\n" +
                            ":three: » **Ticket Commands**\n" +
                            ":four: » **Application Commands**\n" +
                            ":five: » **Fun Commands**\n");


            EmbedRules.setColor(Color);
            EmbedRules.setTimestamp(LocalTime);
            EmbedRules.setFooter(BotName, BotLogo);
            e.getChannel().sendMessage(EmbedRules.build()).queue(message1 -> {
                e.getMessage().delete().queueAfter(3, TimeUnit.SECONDS);
                message1.addReaction("1️⃣").queue();
                message1.addReaction("2️⃣").queue();
                message1.addReaction("3️⃣").queue();
                message1.addReaction("4️⃣").queue();
                message1.addReaction("5️⃣").queue();
                HelpMessageID = message1.getId();
            });
            Timer myTimer = new Timer();
            TimerTask task = new TimerTask() {
                public void run() {
                    SecondsPassed++;
                    while (SecondsPassed > 60 && SecondsPassed < 900000000) {
                        SecondsPassed = 0;
                        e.getChannel().clearReactionsById(HelpMessageID).queue();
                        myTimer.cancel();
                        HelpMessageID = "";
                        e.getChannel().deleteMessageById(HelpMessageID);

                    }
                    while (SecondsPassed >= 900000000) {
                        SecondsPassed = 0;
                        myTimer.cancel();
                        e.getChannel().clearReactionsById(HelpMessageID).queue();
                        HelpMessageID = "";
                        e.getChannel().deleteMessageById(HelpMessageID);

                    }
                }
            };
            myTimer.scheduleAtFixedRate(task, 0, 1000);
        }
    }

    @Override
    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent e) {
        java.awt.Color Color = java.awt.Color.decode(ColorHexCode);

        if (e.getReactionEmote().getName().equals("1️⃣") && e.getMessageId().equals(HelpMessageID) && !e.getUser().isBot()) {

            EmbedBuilder Embed = new EmbedBuilder()
                    .setColor(Color)
                    .setTitle("General Commands Help Page")
                    .setDescription("React with :arrow_left: to return to the main help menu")
                    .addField("",
                            "**" + Bot.BotPrefix + "help** > Sends the help selector. \n" +
                                    "**" + Bot.BotPrefix + "FullHelp** > Sends the full list of the help page\n" +
                                    "**" + Bot.BotPrefix + "roles** > Display all the server roles.\n" +
                                    "**" + Bot.BotPrefix + "roleinfo** [role] > Sends in depth information about that role.\n" +
                                    "**" + Bot.BotPrefix + "serverinfo** > Sends in depth information about the server.\n" +
                                    "**" + Bot.BotPrefix + "avatar** <user> > Gets the avatar of the mentioned user\n" +
                                    "**" + Bot.BotPrefix + "invites** <user> > Gets their or yours invites to the server \n" +
                                    "**" + Bot.BotPrefix + "whois** [user] > Gets a list of that users info\n" +
                                    "**" + BotPrefix + "Suggest [Gamemode] [Suggestion]** **→** Sends a suggestion\n" +
                                    "**" + Bot.BotPrefix + "membercount** > Sends the member count\n" +
                                    "**" + Bot.BotPrefix + "ip** > Sends the current servers IP.\n" +
                                    "**" + BotPrefix + "Bug [Gamemode] [Bug]** **→** Sends a Bug Report\n"

                            , false)
                    .setTimestamp(LocalTime)
                    .setFooter(BotName, BotLogo);
            e.getChannel().clearReactionsById(HelpMessageID).queue();

            e.getChannel().editMessageById(HelpMessageID, Embed.build()).queue(message69 -> {
                message69.addReaction("⬅️").queue();
            });
            SecondsPassed = 0;
        }
        if ((e.getReactionEmote().getName().equals("2️⃣") || e.getReactionEmote().getName().equals("◀️")) && e.getMessageId().equals(HelpMessageID) && !e.getUser().isBot()) {
            SecondsPassed = 0;
            EmbedBuilder Embed2 = new EmbedBuilder()
                    .setColor(Color)
                    .setTitle("Moderation Commands Help Page 1")
                    .setDescription("React with :arrow_left: to return to the main help menu\n" +
                            "React with ▶️️ to go to page 2 for Moderation")
                    .addField("",

                            "**" + Bot.BotPrefix + "ban** [user] > Bans the user.\n" +
                                    "**" + Bot.BotPrefix + "kick** [user] > Kicks the user.\n" +
                                    "**" + Bot.BotPrefix + "members** [@Role] > Lists all members with that role.\n" +
                                    "**" + Bot.BotPrefix + "unban** [user] > Un-Bans the user.\n" +
                                    "**" + Bot.BotPrefix + "prune** [messages] <@user> > Deletes that many amount of messages in that channel\n" +
                                    "**" + Bot.BotPrefix + "mute** [user] [reason] > mutes the user.\n" +
                                    "**" + Bot.BotPrefix + "unmute** [user] [reason] > unmutes the user.\n" +
                                    "**" + Bot.BotPrefix + "announce** [#channel] [announcement] > sends an announcement to that channel\n" +
                                    "**" + Bot.BotPrefix + "say** [#channel] [announcement] > sends a silent-announcement to that channel\n" +
                                    "**" + Bot.BotPrefix + "getMuted** > Gets all muted users\n" +
                                    "**" + Bot.BotPrefix + "Blacklist [@User]** > Blacklists that user from using ALL commands\n" +
                                    "**" + Bot.BotPrefix + "UnBlacklist [@User]** > UnBlacklists that user from using ALL commands\n" +
                                    "**" + Bot.BotPrefix + "GetBlacklisted** > Lists all the currently blacklisted users\n" +
                                    "**" + Bot.BotPrefix + "Strike [Faction Name] [Points Given] [Value Removed] [Total Points] [Reason]** > Strikes that faction\n" +
                                    "**" + Bot.BotPrefix + "ss add [Ign] [reason]** > Adds that IGN to the SS List\n" +
                                    "**" + Bot.BotPrefix + "ss remove [Ign]** > Removes that IGN to the SS List\n" +
                                    "**" + Bot.BotPrefix + "ss list** > Shows the current SS List\n"

                            , false)
                    .setTimestamp(LocalTime)
                    .setFooter(BotName, BotLogo);
            e.getChannel().clearReactionsById(HelpMessageID).queue();
            SecondsPassed = 0;
            e.getChannel().editMessageById(HelpMessageID, Embed2.build()).queue(message69 -> {
                message69.addReaction("⬅️").queue();
                message69.addReaction("▶️").queue();
            });

        }
        if (e.getReactionEmote().getName().equals("▶️") && e.getMessageId().equals(HelpMessageID) && !e.getUser().isBot()) {
            SecondsPassed = 0;
            EmbedBuilder Embed2 = new EmbedBuilder()
                    .setColor(Color)
                    .setTitle("Moderation Commands Help Page 2")
                    .setDescription("React with :arrow_left: to return to the main help menu\n" +
                            "React with ◀️️️ to go to page 1 for Moderation")

                    .addField("",
                            "**" + BotPrefix + "Warn [@User] [Warning] ** **→** Warns the user.\n" +
                                    "**" + BotPrefix + "getWarns [@User] ** **→** Gets the users warn count.\n" +
                                    "**" + BotPrefix + "Promote [@User] <-s>** **→** Promotes that user to the next rank.\n" +
                                    "**" + BotPrefix + "Assign [@User] [@Role] <-s>** **→** Assigns that user a role\n" +
                                    "**" + BotPrefix + "Demote [@User] <-s>** **→** Demotes that user to the next lowest rank.\n" +
                                    "**" + BotPrefix + "StaffClear [@User] <-s>** **→** Demotes that user and clears their staff roles\n" +
                                    "**" + BotPrefix + "Resign [@User] <-s>** **→** Resigns that user and clears their staff roles\n" +
                                    "**" + BotPrefix + "FacLeader [@User]** **→** Gives that user Faction Leader Role\n" +
                                    "**" + BotPrefix + "Confirm [@Leader] [Faction Name] [Roster Size]** **→** Confirms that faction as playing\n" +
                                    "**" + BotPrefix + "Poll [Number Of Options]** **→** Starts the Poll-Creator in DMS\n" +
                                    "**" + BotPrefix + "Clear** **→** Clears nearly all messages in a channel\n" +
                                    "**" + BotPrefix + "TicketTop** **→** Get's the top closed tickets!\n"


                            , false)
                    .setTimestamp(LocalTime)
                    .setFooter(BotName, BotLogo);
            e.getChannel().clearReactionsById(HelpMessageID).queue();
            e.getChannel().editMessageById(HelpMessageID, Embed2.build()).queue(message69 -> {
                message69.addReaction("⬅️").queue();
                message69.addReaction("◀️").queue();
            });

        }
        if (e.getReactionEmote().getName().equals("3️⃣") && e.getMessageId().equals(HelpMessageID) && !e.getUser().isBot()) {

            EmbedBuilder Embed = new EmbedBuilder()
                    .setColor(Color)
                    .setTitle("Ticket Commands Help Page")
                    .setDescription("React with :arrow_left: to return to the main help menu")
                    .addField("",
                            "**" + BotPrefix + "Close [Reason]** **→** Closes the ticket.\n" +
                                    "**" + BotPrefix + "AdminOnly** **→** Makes the ticket Admin Only.\n" +
                                    "**" + BotPrefix + "TicketInfo** **→** Lists all information about the ticket.\n" +
                                    "**" + BotPrefix + "Rename [name]** **→** Renames the ticket.\n" +
                                    "**" + BotPrefix + "Add [@User]** **→** Adds that user as a sub-user to the ticket.\n" +
                                    "**" + BotPrefix + "Remove [@User]** **→** Removes that user as a sub-user from the ticket.\n" +
                                    "**" + BotPrefix + "Ticket_Panel** **→** Sends the Ticket Panel.\n"
                            , false)
                    .setTimestamp(LocalTime)
                    .setFooter(BotName, BotLogo);
            e.getChannel().clearReactionsById(HelpMessageID).queue();
            SecondsPassed = 0;
            e.getChannel().editMessageById(HelpMessageID, Embed.build()).queue(message69 -> {
                message69.addReaction("⬅️").queue();
            });
        }
        if (e.getReactionEmote().getName().equals("4️⃣") && e.getMessageId().equals(HelpMessageID) && !e.getUser().isBot()) {

            EmbedBuilder Embed = new EmbedBuilder()
                    .setColor(Color)
                    .setTitle("General Commands Help Page")
                    .setDescription("React with :arrow_left: to return to the main help menu")
                    .addField("Applications",
                            "**" + Bot.BotPrefix + "apply** > Apply to join the staff team\n" +
                                    "**" + Bot.BotPrefix + "accept** [@user] [Role-To-Give] > Accepts an application\n" +
                                    "**" + Bot.BotPrefix + "deny** [@user] [Reason] > Denies an applcation\n" +
                                    "**" + Bot.BotPrefix + "review** [@user] > Sends their application\n" +
                                    "**" + Bot.BotPrefix + "CurrentApps** > Sends a list of all pending applications\n" +
                                    "**" + Bot.BotPrefix + "CancelApps** > Cancels all current applications\n"
                            , false)
                    .setTimestamp(LocalTime)
                    .setFooter(BotName, BotLogo);
            e.getChannel().clearReactionsById(HelpMessageID).queue();
            SecondsPassed = 0;
            e.getChannel().editMessageById(HelpMessageID, Embed.build()).queue(message69 -> {
                message69.addReaction("⬅️").queue();
            });
        }
        if (e.getReactionEmote().getName().equals("5️⃣") && e.getMessageId().equals(HelpMessageID) && !e.getUser().isBot()) {

            EmbedBuilder Embed = new EmbedBuilder()
                    .setColor(Color)
                    .setTitle("Fun Commands Help Page")
                    .setDescription("React with :arrow_left: to return to the main help menu")
                    .addField("",
                            "**" + BotPrefix + "Slap [@User]** **→** Slaps them!\n" +
                                    "**" + BotPrefix + "Pat [@User]** **→** Pats them!\n" +
                                    "**" + BotPrefix + "hi** **→** Says Hello back!\n" +
                                    "**" + BotPrefix + "Dice** **→** Rolls a die!\n" +
                                    "**" + BotPrefix + "cf** **→** Flips a coin!\n"
                            , false)
                    .setTimestamp(LocalTime)
                    .setFooter(BotName, BotLogo);
            e.getChannel().clearReactionsById(HelpMessageID).queue();
            SecondsPassed = 0;
            e.getChannel().editMessageById(HelpMessageID, Embed.build()).queue(message69 -> {
                message69.addReaction("⬅️").queue();
            });
        }
        if (e.getReactionEmote().getName().equals("⬅️") && e.getMessageId().equals(HelpMessageID) && !e.getUser().isBot()) {

            EmbedBuilder EmbedRules = new EmbedBuilder();

            EmbedRules.setTitle(BotName + " Help Page");

            EmbedRules.setColor(Color)
                    .setDescription("**React to the corresponding emoji to get more help on that subject!**\n\n" +
                            ":one: » **Basic Commands**\n" +
                            ":two: » **Moderation Commands**\n" +
                            ":three: » **Ticket Commands**\n" +
                            ":four: » **Application Commands**\n" +
                            ":five: » **Fun Commands**\n");

            EmbedRules.setTimestamp(LocalTime);
            EmbedRules.setFooter(BotName, BotLogo);
            e.getChannel().clearReactionsById(HelpMessageID).queue();
            SecondsPassed = 0;
            e.getChannel().editMessageById(HelpMessageID, EmbedRules.build()).queue(message1 -> {
                message1.addReaction("1️⃣").queue();
                message1.addReaction("2️⃣").queue();
                message1.addReaction("3️⃣").queue();
                message1.addReaction("4️⃣").queue();
                message1.addReaction("5️⃣").queue();

            });


        }
    }
}