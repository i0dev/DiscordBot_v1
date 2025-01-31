import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
public class Extra_BugReport extends ListenerAdapter {
    public String BotPrefix = Bot.BotPrefix;
    public String BotName = Bot.BotName;
    public ZonedDateTime LocalTime = ZonedDateTime.now();
    public ZonedDateTime now = ZonedDateTime.now();
    public String ColorHexCode = Bot.ColorHexCode;
    public String BotLogo = Bot.BotLogo;
    public String TicketCreateChannelID = Bot.TicketCreateChannelID;
    public String TicketCreateCategoryChannelID = Bot.TicketCreateCategoryChannelID;
    public String BugReportChannelID = Bot.SuggestionChannelID;
    public ArrayList<String> SuggestionGameModes = new ArrayList<String>();

    public String SupportTeamRoleID = Bot.SupportTeamRoleID;
    public String AdminRoleID = Bot.AdminRoleID;
    public String MemberRoleID = Bot.MemberRoleID;

    public boolean StaffPingEnabled = Bot.StaffPingEnabled;

    public String ChannelID = "";
    public String ChannelName = "";
    public String CategoryID = "";
    public String UsersID = "";
    public String UsersTag = "";
    public String UsersAvatarURL = "";
    public boolean AdminOnlyMode;
    public ArrayList<String> SubUsers = new ArrayList<String>();


    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        try {
            JSONObject json = (JSONObject) new JSONParser().parse(new FileReader(new File("Tickets/config.json")));
            BotPrefix = ((HashMap<String, String>) json.get("GeneralConfig")).get("BotPrefix");
            BotName = ((HashMap<String, String>) json.get("GeneralConfig")).get("BotName");
            BotLogo = ((HashMap<String, String>) json.get("GeneralConfig")).get("BotLogo");
            ColorHexCode = ((HashMap<String, String>) json.get("GeneralConfig")).get("ColorHexCode");
SuggestionGameModes = ((HashMap<String, ArrayList>) json.get("GeneralConfig")).get("SuggestionGameModes");
            TicketCreateChannelID = ((HashMap<String, String>) json.get("ChannelIDS")).get("TicketCreateChannelID");
            TicketCreateCategoryChannelID = ((HashMap<String, String>) json.get("ChannelIDS")).get("TicketCreateCategoryChannelID");
            BugReportChannelID = ((HashMap<String, String>) json.get("ChannelIDS")).get("BugReportChannelID");
            MemberRoleID = ((HashMap<String, String>) json.get("RoleIDS")).get("MemberRoleID");
            SupportTeamRoleID = ((HashMap<String, String>) json.get("RoleIDS")).get("SupportTeamRoleID");
            AdminRoleID = ((HashMap<String, String>) json.get("RoleIDS")).get("AdminRoleID");

        } catch (Exception ee) {
            ee.printStackTrace();
        }

        Color Color = java.awt.Color.decode(Bot.ColorHexCode);
        MessageChannel channel = e.getChannel();
        String[] message = e.getMessage().getContentRaw().split(" ");
        if (message.length > 0 && message[0].equalsIgnoreCase(Bot.BotPrefix + "bug")) {
            ArrayList<String> BlacklistedGet = new ArrayList<>(); if (new File("BlacklistedUsers.json").exists()) { try { JSONObject json = (JSONObject) new JSONParser().parse(new FileReader(new File("BlacklistedUsers.json"))); BlacklistedGet = (ArrayList<String>) json.get("BlacklistedUsers"); } catch (Exception ee) { ee.printStackTrace(); } } boolean isBlacklisted = false; for (String s : BlacklistedGet) { if (e.getAuthor().getId().equals(s)) { isBlacklisted = true; } } if (isBlacklisted) { EmbedBuilder UserBlacklisted = new EmbedBuilder() .setTitle("Error") .setThumbnail(Bot.BotLogo) .setFooter(Bot.WaterMark, Bot.BotLogo) .setTimestamp(Bot.now) .setColor(Color.RED) .setDescription("**" + e.getAuthor().getAsTag() + "**, *You are blacklisted from using all commands, \n" + "If you think this is an error please contact a staff member!*"); e.getChannel().sendMessage(UserBlacklisted.build()).queue(message3 -> { e.getMessage().delete().queue(); message3.addReaction("❌").queue(); message3.delete().queueAfter(10, TimeUnit.SECONDS); }); return; }
        }
        boolean isAllowed = true;

        if (message.length == 1 && message[0].equalsIgnoreCase(Bot.BotPrefix + "bug")) {
            if (isAllowed) {
                EmbedBuilder EmbedRules = new EmbedBuilder();
                EmbedRules.setTitle("Incorrect Format");
                EmbedRules.setColor(Color);
                EmbedRules.addField("Format:", "" + Bot.BotPrefix + "bug [Gamemode] [Bug]", false);
                String Gamemodes = "";
                if (SuggestionGameModes.size() < 1) {
                    Gamemodes = "No gamemodes to display!";
                } else {
                    for (int i = 0; i < SuggestionGameModes.size(); i++) {
                        Gamemodes = Gamemodes +  SuggestionGameModes.get(i)+", ";
                    }
                    Gamemodes = Gamemodes.substring(0,Gamemodes.length()-2);
                }
                EmbedRules.addField("Gamemodes:", "`" + Gamemodes + "`", false);
                EmbedRules.setTimestamp(now);
                EmbedRules.setFooter(Bot.WaterMark, BotLogo);
                channel.sendMessage(EmbedRules.build()).queue(message1 -> {
                    e.getMessage().delete().queue();
                });
            } else {
                EmbedBuilder EmbedRules = new EmbedBuilder();
                EmbedRules.setTitle("Insufficient Permissions");
                EmbedRules.setColor(Color);
                EmbedRules.addField("Error:", "You do not have permission to run this command!", false);
                EmbedRules.setTimestamp(now);
                EmbedRules.setFooter(Bot.WaterMark, BotLogo);
                channel.sendMessage(EmbedRules.build()).queue(message1 -> {
                    e.getMessage().delete().queue();
                });
            }
        }
        if (message.length == 2 && message[0].equalsIgnoreCase(Bot.BotPrefix + "bug")) {
            if (isAllowed) {
                EmbedBuilder EmbedRules = new EmbedBuilder();
                EmbedRules.setTitle("Incorrect Format");
                EmbedRules.setColor(Color);
                EmbedRules.addField("Format:", "" + Bot.BotPrefix + "bug [Gamemode] [Bug]", false);
                String Gamemodes = "";
                if (SuggestionGameModes.size() < 1) {
                    Gamemodes = "No gamemodes to display!";
                } else {
                    for (int i = 0; i < SuggestionGameModes.size(); i++) {
                        Gamemodes = Gamemodes +  SuggestionGameModes.get(i)+", ";
                    }
                    Gamemodes = Gamemodes.substring(0,Gamemodes.length()-2);
                }
                EmbedRules.addField("Gamemodes:", "`" + Gamemodes + "`", false);
                EmbedRules.setTimestamp(now);
                EmbedRules.setFooter(Bot.WaterMark, BotLogo);
                channel.sendMessage(EmbedRules.build()).queue(message1 -> {
                    e.getMessage().delete().queue();
                });
            } else {
                EmbedBuilder EmbedRules = new EmbedBuilder();
                EmbedRules.setTitle("Insufficient Permissions");
                EmbedRules.setColor(Color);
                EmbedRules.addField("Error:", "You do not have permission to run this command!", false);
                EmbedRules.setTimestamp(now);
                EmbedRules.setFooter(Bot.WaterMark, BotLogo);
                channel.sendMessage(EmbedRules.build()).queue(message1 -> {
                    e.getMessage().delete().queue();
                });
            }
        }
        if (message.length > 2 && message[0].equalsIgnoreCase(Bot.BotPrefix + "bug")) {
            if (isAllowed) {

                String Gamemode = message[1];
                ArrayList<String> SuggestionListLOWERCASE = new ArrayList<String>();
                for (int i = 0; i < SuggestionGameModes.size(); i++) {
                    SuggestionListLOWERCASE.add(SuggestionGameModes.get(i).toLowerCase());
                }
                if (SuggestionListLOWERCASE.contains(Gamemode.toLowerCase())) {
                    String GamemodeNew = "";
                    GamemodeNew = Gamemode.toLowerCase();
                    String GamemodeNewFirst = GamemodeNew.substring(0, 1);
                    String GamemodeNewLast = GamemodeNew.substring(1);
                    GamemodeNewFirst = GamemodeNewFirst.toUpperCase();
                    Gamemode = GamemodeNewFirst + GamemodeNewLast;

                    String bug = "";
                    for (int k = 2; k < message.length; k++) {
                        bug = bug + " " + message[k] + " ";
                    }

                    EmbedBuilder EmbedRules = new EmbedBuilder();
                    EmbedRules.setTitle("Successfully Sent your Bug Report, " + e.getAuthor().getAsTag());
                    EmbedRules.setColor(Color);
                    EmbedRules.addField("Success", e.getMember().getUser().getAsTag() + ", you have sent a Bug Report for the gamemode: `" + Gamemode + "`", false);
                    EmbedRules.addField("Bug", bug, false);
                    EmbedRules.setTimestamp(now);
                    EmbedRules.setFooter(Bot.WaterMark, BotLogo);
                    channel.sendMessage(EmbedRules.build()).queue();


                    EmbedBuilder Embed = new EmbedBuilder()
                            .setTitle("Incoming Bug Report from " + e.getAuthor().getAsTag())
                            .setColor(Color)
                            .addField("Bug Report for " + Gamemode, bug, false)
                            .setTimestamp(now)
                            .setFooter(Bot.WaterMark, BotLogo);
                    e.getJDA().getTextChannelById(BugReportChannelID).sendMessage(Embed.build()).queue(message1 -> {
                        e.getMessage().delete().queue();
                    });
                    SuggestionListLOWERCASE.clear();
                } else {
                    EmbedBuilder EmbedRules = new EmbedBuilder();
                    EmbedRules.setTitle("Incorrect Gamemode");
                    EmbedRules.setColor(Color);
                    EmbedRules.addField("Error:", "You entered a non-existent gamemode!", false);
                    String Gamemodes = "";
                    if (SuggestionGameModes.size() < 1) {
                        Gamemodes = "No gamemodes to display!";
                    } else {
                        for (int i = 0; i < SuggestionGameModes.size(); i++) {
                            Gamemodes = Gamemodes +  SuggestionGameModes.get(i)+", ";
                        }
                        Gamemodes = Gamemodes.substring(0,Gamemodes.length()-2);
                    }
                    EmbedRules.addField("Gamemodes:", "`" + Gamemodes + "`", false);
                    EmbedRules.setTimestamp(now);
                    EmbedRules.setFooter(Bot.WaterMark, BotLogo);
                    channel.sendMessage(EmbedRules.build()).queue(message1 -> {
                        e.getMessage().delete().queue();
                    });
                    SuggestionListLOWERCASE.clear();

                }

            } else {
                EmbedBuilder EmbedRules = new EmbedBuilder();
                EmbedRules.setTitle("Insufficient Permissions");
                EmbedRules.setColor(Color);
                EmbedRules.addField("Error:", "You do not have permission to run this command!", false);
                EmbedRules.setTimestamp(now);
                EmbedRules.setFooter(Bot.WaterMark, BotLogo);
                channel.sendMessage(EmbedRules.build()).queue(message1 -> {
                    e.getMessage().delete().queue();
                });
            }
        }

    }
}