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
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@SuppressWarnings("unused")

public class Ticket_TicketTop extends ListenerAdapter {
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


        if (e.getMessage().getContentRaw().equalsIgnoreCase(BotPrefix + "TicketTop")) {
            Color Color = java.awt.Color.decode(ColorHexCode);
            boolean isAllowed = false;
            for (int i = 0; i < Bot.AllowedRoles.size(); i++) {
                if (e.getMember().getRoles().contains(e.getGuild().getRoleById(Bot.AllowedRoles.get(i))) || e.getMember().getPermissions().contains(Permission.ADMINISTRATOR)) {
                    isAllowed = true;
                }
            }
            for (int i = 0; i < Bot.LightAllowedRoleIDS.size(); i++) {
                if (e.getMember().getRoles().contains(e.getGuild().getRoleById(Bot.LightAllowedRoleIDS.get(i))) || e.getMember().getPermissions().contains(Permission.ADMINISTRATOR)) {
                    isAllowed = true;
                }
            }
            if (!isAllowed) {
                EmbedBuilder EmbedRules = new EmbedBuilder();
                EmbedRules.setTitle("Insufficient Permissions");
                EmbedRules.setColor(Color);
                EmbedRules.addField("Error:", "You do not have permission to run this command!", false);
                EmbedRules.setTimestamp(Bot.now);
                EmbedRules.setFooter(Bot.WaterMark, Bot.Logo);
                e.getChannel().sendMessage(EmbedRules.build()).queue(message1 -> {
                    e.getMessage().delete().queue();
                });
            } else {

                ArrayList<ArrayList<Long>> IDMatcher = new ArrayList<>();

                if (new File("Tickets/TicketTop.json").exists()) {
                    try {
                        JSONObject json = (JSONObject) new JSONParser().parse(new FileReader(new File("Tickets/TicketTop.json")));
                        IDMatcher = (ArrayList<ArrayList<Long>>) json.get("TicketTop");
                    } catch (Exception ee) {
                        ee.printStackTrace();
                    }
                    IDMatcher = (ArrayList<ArrayList<Long>>) IDMatcher.stream().sorted((list1, list2) -> list2.get(1).compareTo(list1.get(1))).collect(Collectors.toList());
                    EmbedBuilder EmbedRules = new EmbedBuilder();
                    for (int i = 0; i < IDMatcher.size(); i++) {
                        EmbedRules.addField("#" + (i + 1) + "", "**" + e.getGuild().getMemberById(IDMatcher.get(i).get(0)).getUser().getAsTag() + "** - `" + IDMatcher.get(i).get(1) + "`", false);

                    }
                    EmbedRules.setTitle("Top Tickets Closed!")
                            .setDescription("Every time you close a ticket, you will get a \"point\" and the leaderboard of top ticket closed staff members are here!");
                    EmbedRules.setColor(Color);
                    EmbedRules.setTimestamp(LocalTime);
                    EmbedRules.setFooter(Bot.WaterMark, BotLogo);
                    e.getChannel().sendMessage(EmbedRules.build()).queue(message1 -> {
                        e.getMessage().delete().queue();
                    });
                    IDMatcher.clear();
                }
            }
        }
    }
}
