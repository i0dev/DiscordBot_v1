import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")

public class Ticket_Info extends ListenerAdapter {
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
        String[] messageWithSplit = e.getMessage().getContentRaw().split(" ");
        if (messageWithSplit.length > 0 && messageWithSplit[0].equalsIgnoreCase(Bot.BotPrefix + "ticketinfo")) {
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

            MemberRoleID = ((HashMap<String, String>) json.get("RoleIDS")).get("MemberRoleID");
            SupportTeamRoleID = ((HashMap<String, String>) json.get("RoleIDS")).get("SupportTeamRoleID");
            AdminRoleID = ((HashMap<String, String>) json.get("RoleIDS")).get("AdminRoleID");

        } catch (Exception ee) {
            ee.printStackTrace();
        }
        if (new File("Tickets/Storage/" + e.getChannel().getId() + ".json").exists()) {

            java.awt.Color Color = null;
            try {
                JSONObject json = (JSONObject) new JSONParser().parse(new FileReader(new File("Tickets/Storage/" + e.getChannel().getId() + ".json")));
                ChannelID = (String) json.get("ChannelID");
                ChannelName = (String) json.get("ChannelName");
                CategoryID = (String) json.get("CategoryID");
                UsersID = (String) json.get("UsersID");
                UsersTag = (String) json.get("UsersTag");
                UsersAvatarURL = (String) json.get("UsersAvatarURL");
                AdminOnlyMode = (boolean) json.get("AdminOnlyMode");
                SubUsers = (ArrayList<String>) json.get("SubUsers");

                Color = java.awt.Color.decode(ColorHexCode);


                MessageChannel channel = e.getChannel();
                GuildChannel permChan = e.getChannel();
                Role supportRole = e.getGuild().getRoleById(SupportTeamRoleID);

                if (e.getMessage().getContentRaw().equalsIgnoreCase(BotPrefix + "ticketinfo")) {

                    String SubUsersString = "";
                    for (int i = 0; i < SubUsers.size(); i++) {
                        String Member = e.getGuild().getMemberById(SubUsers.get(i)).getUser().getAsTag();
                        SubUsersString = SubUsersString + Member.toString() + ", ";
                    }
                    if (SubUsers.size() >= 1) {
                        SubUsersString.substring(0, SubUsersString.length() - 2);
                    }
                    String AdminOnlyStatus = "";
                    if (AdminOnlyMode) {
                        AdminOnlyStatus = "Admin-Only Mode";
                    } else {
                        AdminOnlyStatus = "Not in Admin-Only Mode";
                    }


                    EmbedBuilder EmbedRules = new EmbedBuilder()

                            .setTitle("Ticket information from from " + ChannelName)
                            .addField("Ticket Category ID", CategoryID, true)
                            .addField("Ticket Channel ID", ChannelID, true)
                            .addField("Ticket Name", ChannelName, true)
                            .addField("Ticket Owners ID", UsersID, true)
                            .addField("Ticket Owners Tag", UsersTag, true)
                            .addField("Admin Only Status", AdminOnlyStatus, true)
                            .setThumbnail(UsersAvatarURL)
                            .setColor(Color)
                            .setTimestamp(LocalTime)
                            .setFooter("Request From " + e.getAuthor().getAsTag(), BotLogo);
                    if (SubUsers.size() <= 0) {
                    } else {
                        EmbedRules.addField("List of Sub-Users", SubUsersString, true);
                    }
                    channel.sendMessage(EmbedRules.build()).queue(message1 -> {
                        e.getMessage().delete().queueAfter(3, TimeUnit.SECONDS);
                    });


                }
            } catch (Exception ee) {
                ee.printStackTrace();
                EmbedBuilder EmbedRules = new EmbedBuilder();

                EmbedRules.setTitle("Error");
                EmbedRules.setDescription("This command can only be executed in a ticket");

                EmbedRules.setColor(Color);
                EmbedRules.setTimestamp(LocalTime);
                EmbedRules.setFooter("Request From " + e.getAuthor().getAsTag(), BotLogo);
                e.getChannel().sendMessage(EmbedRules.build()).queue(message1 -> {
                    e.getMessage().delete().queueAfter(3, TimeUnit.SECONDS);
                });
            }
        }


    }
}
