import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageChannel;
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

public class Ticket_Help extends ListenerAdapter {
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

    public boolean StaffPingEnabled = Bot.StaffPingEnabled;
    public String WaterMark = Bot.WaterMark;


    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        String[] messageWithSplit = e.getMessage().getContentRaw().split(" ");
        if (messageWithSplit.length > 0 && messageWithSplit[0].equalsIgnoreCase(Bot.BotPrefix + "help")) {
            ArrayList<String> BlacklistedGet = new ArrayList<>(); if (new File("BlacklistedUsers.json").exists()) { try { JSONObject json = (JSONObject) new JSONParser().parse(new FileReader(new File("BlacklistedUsers.json"))); BlacklistedGet = (ArrayList<String>) json.get("BlacklistedUsers"); } catch (Exception ee) { ee.printStackTrace(); } } boolean isBlacklisted = false; for (String s : BlacklistedGet) { if (e.getAuthor().getId().equals(s)) { isBlacklisted = true; } } if (isBlacklisted) { EmbedBuilder UserBlacklisted = new EmbedBuilder() .setTitle("Error") .setThumbnail(Bot.BotLogo) .setFooter(Bot.WaterMark, Bot.BotLogo) .setTimestamp(Bot.now) .setColor(Color.RED) .setDescription("**" + e.getAuthor().getAsTag() + "**, *You are blacklisted from using all commands, \n" + "If you think this is an error please contact a staff member!*"); e.getChannel().sendMessage(UserBlacklisted.build()).queue(message3 -> { e.getMessage().delete().queue(); message3.addReaction("❌").queue(); message3.delete().queueAfter(10, TimeUnit.SECONDS); }); return; }
        }
        try {
            JSONObject json = (JSONObject) new JSONParser().parse(new FileReader(new File("Tickets/config.json")));
            BotPrefix = ((HashMap<String, String>) json.get("GeneralConfig")).get("BotPrefix");
            BotName = ((HashMap<String, String>) json.get("GeneralConfig")).get("BotName");
            BotLogo = ((HashMap<String, String>) json.get("GeneralConfig")).get("BotLogo");
            ColorHexCode = ((HashMap<String, String>) json.get("GeneralConfig")).get("ColorHexCode");
            StaffPingEnabled = ((HashMap<String, Boolean>) json.get("GeneralConfig")).get("StaffPingEnabled");

            TicketCreateChannelID = ((HashMap<String, String>) json.get("ChannelIDS")).get("TicketCreateChannelID");
            TicketCreateCategoryChannelID = ((HashMap<String, String>) json.get("ChannelIDS")).get("TicketCreateCategoryChannelID");


            MemberRoleID = ((HashMap<String, String>) json.get("RoleIDS")).get("MemberRoleID");
            SupportTeamRoleID = ((HashMap<String, String>) json.get("RoleIDS")).get("SupportTeamRoleID");
            AdminRoleID = ((HashMap<String, String>) json.get("RoleIDS")).get("AdminRoleID");
        } catch (Exception ee) {
            ee.printStackTrace();
        }
        Color Color = java.awt.Color.decode(ColorHexCode);

        boolean isAllowed = true;
        if (e.getMember().getRoles().contains(e.getGuild().getRoleById(AdminRoleID)) ||
                e.getMember().getRoles().contains(e.getGuild().getRoleById(SupportTeamRoleID)) ||
                e.getMember().getPermissions().contains(Permission.ADMINISTRATOR)) {
            isAllowed = true;
        }

        if (e.getMessage().getContentRaw().equalsIgnoreCase(BotPrefix + "Fullhelp")) {
            if (isAllowed) {

                EmbedBuilder Embed = new EmbedBuilder()
                        .setColor(Color)
                        .addField("General",
                                "**" + Bot.BotPrefix + "help** > Sends the help page \n" +
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
                        .addField("Applications",
                                "**" + Bot.BotPrefix + "apply** > Apply to join the staff team\n" +
                                        "**" + Bot.BotPrefix + "accept** [@user] [Role-To-Give] > Accepts an application\n" +
                                        "**" + Bot.BotPrefix + "deny** [@user] [Reason] > Denies an applcation\n" +
                                        "**" + Bot.BotPrefix + "review** [@user] > Sends their application\n" +
                                        "**" + Bot.BotPrefix + "CurrentApps** > Sends a list of all pending applications\n" +
                                        "**" + Bot.BotPrefix + "CancelApps** > Cancels all current applications\n"
                                , false)
                        .addField("Moderation",

                                "**" + Bot.BotPrefix + "ban** [user] > Bans the user.\n" +
                                        "**" + Bot.BotPrefix + "kick** [user] > Kicks the user.\n" +
                                        "**" + Bot.BotPrefix + "members** [@Role] > Lists all members with that role.\n" +
                                        "**" + Bot.BotPrefix + "unban** [user] > Un-Bans the user.\n" +
                                        "**" + Bot.BotPrefix + "prune** <user> [messages] > Deletes that many amount of messages in that channel\n" +
                                        "**" + Bot.BotPrefix + "mute** [user] [reason] > mutes the user.\n" +
                                        "**" + Bot.BotPrefix + "unmute** [user] [reason] > unmutes the user.\n" +
                                        "**" + Bot.BotPrefix + "announce** [#channel] [announcement] > sends an announcement to that channel\n" +
                                        "**" + Bot.BotPrefix + "say** [#channel] [announcement] > sends a silent-announcement to that channel\n" +
                                        "**" + Bot.BotPrefix + "getMuted** > Gets all muted users\n" +
                                        "**" + BotPrefix + "Warn [@User] [Warning] ** **→** Warns the user.\n" +
                                        "**" + BotPrefix + "getWarns [@User] ** **→** Gets the users warn count.\n" +
                                        "**" + BotPrefix + "Promote [@User] [@Role]** **→** Promotes that user \n" +
                                        "**" + BotPrefix + "Demote [@User]** **→** Demotes that user and clears their staff roles\n" +
                                        "**" + BotPrefix + "Resign [@User]** **→** Resigns that user and clears their staff roles\n" +
                                        "**" + BotPrefix + "FacLeader [@User]** **→** Gives that user Faction Leader Role\n"


                                , false)
                        .addField("Ticket Help Commands",
                                "**" + BotPrefix + "Close [Reason]** **→** Closes the ticket.\n" +
                                        "**" + BotPrefix + "AminOnly** **→** Makes the ticket Admin Only.\n" +
                                        "**" + BotPrefix + "TicketInfo** **→** Lists all information about the ticket.\n" +
                                        "**" + BotPrefix + "Rename [name]** **→** Renames the ticket.\n" +
                                        "**" + BotPrefix + "Add [@User]** **→** Adds that user as a sub-user to the ticket.\n" +
                                        "**" + BotPrefix + "Remove [@User]** **→** Removes that user as a sub-user from the ticket.\n" +
                                        "**" + BotPrefix + "Ticket_Panel** **→** Sends the Ticket Panel.\n"

                                , false)
                        .setTimestamp(LocalTime)
                        .setFooter(BotName, BotLogo);
                e.getChannel().sendMessage(Embed.build()).queue(message1 -> {
                    e.getMessage().delete().queue();
                });
            } else {
                EmbedBuilder Embed = new EmbedBuilder()
                        .setColor(Color)
                        .addField("General",
                                "**" + Bot.BotPrefix + "help** > Sends the help page \n" +
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
                        .addField("Applications",
                                "**" + Bot.BotPrefix + "apply** > Apply to join the staff team"
                                , false)

                        .setTimestamp(LocalTime)
                        .setFooter(BotName, BotLogo);
                e.getChannel().sendMessage(Embed.build()).queue(message1 -> {
                    e.getMessage().delete().queue();
                });
            }
        }

    }
}