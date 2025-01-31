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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
public class Extra_GetWarns extends ListenerAdapter {
    public String BotPrefix = Bot.BotPrefix;
    public String BotName = Bot.BotName;
    public ZonedDateTime LocalTime = Bot.LocalTime;
    public String ColorHexCode = Bot.ColorHexCode;
    public String BotLogo = Bot.BotLogo;
    public String LogsChannelID = "";
    public static ArrayList<String> AllowedRoles = new ArrayList<String>();
    public String AdminRoleID = Bot.AdminRoleID;

    public static ArrayList<Object> AllWarnings = new ArrayList<Object>();

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        try {
            JSONObject json = (JSONObject) new JSONParser().parse(new FileReader(new File("Tickets/config.json")));
            BotPrefix = ((HashMap<String, String>) json.get("GeneralConfig")).get("BotPrefix");
            BotName = ((HashMap<String, String>) json.get("GeneralConfig")).get("BotName");
            BotLogo = ((HashMap<String, String>) json.get("GeneralConfig")).get("BotLogo");
            ColorHexCode = ((HashMap<String, String>) json.get("GeneralConfig")).get("ColorHexCode");
            AllowedRoles = ((HashMap<String, ArrayList>) json.get("Extras")).get("AllowedRoles");
            LogsChannelID = ((HashMap<String, String>) json.get("Extras")).get("LogsChannelID");
            AdminRoleID = ((HashMap<String, String>) json.get("RoleIDS")).get("AdminRoleID");

        } catch (Exception ee) {
            ee.printStackTrace();
        }
        Color Color = java.awt.Color.decode(Bot.ColorHexCode);
        MessageChannel channel = e.getChannel();
        String[] message = e.getMessage().getContentRaw().split(" ");
        if (message.length > 0 && message[0].equalsIgnoreCase(Bot.BotPrefix + "getWarns")) {
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
        boolean isAllowed = false;
        for (int i = 0; i < AllowedRoles.size(); i++) {
            if (e.getMember().getRoles().contains(e.getGuild().getRoleById(AllowedRoles.get(i)))) {

                isAllowed = true;
            }
        }
        if (e.getMember().getPermissions().contains(Permission.ADMINISTRATOR)
                || e.getMember().getRoles().contains(e.getGuild().getRoleById(AdminRoleID))) {
            isAllowed = true;
        }
        for (int i = 0; i < Bot.LightAllowedRoleIDS.size(); i++) {
            if (e.getMember().getRoles().contains(e.getGuild().getRoleById(Bot.LightAllowedRoleIDS.get(i))) || e.getMember().getPermissions().contains(Permission.ADMINISTRATOR)) {
                isAllowed = true;
            }
        }
        if (message.length == 1 && message[0].equalsIgnoreCase(Bot.BotPrefix + "getWarns")) {
            if (isAllowed) {
                EmbedBuilder EmbedRules = new EmbedBuilder();
                EmbedRules.setTitle("Incorrect Format");
                EmbedRules.setColor(Color);
                EmbedRules.addField("Format:", "" + Bot.BotPrefix + "getWarns [@user]", false);
                EmbedRules.setTimestamp(LocalTime);
                EmbedRules.setFooter(Bot.WaterMark, BotLogo);
                channel.sendMessage(EmbedRules.build()).queue(message1 -> {
                    e.getMessage().delete().queue();
                });
            } else {
                EmbedBuilder EmbedRules = new EmbedBuilder();
                EmbedRules.setTitle("Insufficient Permissions");
                EmbedRules.setColor(Color);
                EmbedRules.addField("Error:", "You do not have permission to run this command!", false);
                EmbedRules.setTimestamp(LocalTime);
                EmbedRules.setFooter(Bot.WaterMark, BotLogo);
                channel.sendMessage(EmbedRules.build()).queue(message1 -> {
                    e.getMessage().delete().queue();
                });
            }
        }
        if (message.length == 2 && message[0].equalsIgnoreCase(Bot.BotPrefix + "getWarns")) {
            if (isAllowed) {
                User MentionedUser = e.getMessage().getMentionedUsers().get(0);
                Member MentionedMember = e.getMessage().getMentionedMembers().get(0);

                long CurrnetWarningCount = 1;
                ArrayList<ArrayList<Long>> IDMatcher = new ArrayList<>();
                ArrayList<Long> IDMatcherInt = new ArrayList<>();

                if (new File("Warnings.json").exists()) {
                    try {
                        JSONObject json = (JSONObject) new JSONParser().parse(new FileReader(new File("Warnings.json")));
                        IDMatcher = (ArrayList<ArrayList<Long>>) json.get("WarningsList");
                    } catch (Exception ee) {
                        ee.printStackTrace();
                    }
                    for (int i = 0; i < IDMatcher.size(); i++) {
                        if (IDMatcher.get(i).get(0).equals(MentionedMember.getIdLong())) {
                            CurrnetWarningCount = IDMatcher.get(i).get(1);
                        }
                    }

                    IDMatcherInt.add(MentionedMember.getIdLong());
                    IDMatcherInt.add(CurrnetWarningCount);
                    IDMatcher.add(IDMatcherInt);


                    EmbedBuilder EmbedRules = new EmbedBuilder();
                    EmbedRules.setTitle("Check Warnings");
                    EmbedRules.setColor(Color);
                    EmbedRules.addField("Warnings", "**" + MentionedMember.getUser().getAsTag() + "** has `" + CurrnetWarningCount + "` Warnings.", false);

                    EmbedRules.setTimestamp(LocalTime);
                    EmbedRules.setFooter(Bot.WaterMark, BotLogo);
                    channel.sendMessage(EmbedRules.build()).queue(message1 -> {
                        e.getMessage().delete().queue();
                    });


                    IDMatcher.clear();
                    IDMatcherInt.clear();
                    CurrnetWarningCount = 1;
                }


            } else {
                EmbedBuilder EmbedRules = new EmbedBuilder();
                EmbedRules.setTitle("Insufficient Permissions");
                EmbedRules.setColor(Color);
                EmbedRules.addField("Error:", "You do not have permission to run this command!", false);
                EmbedRules.setTimestamp(LocalTime);
                EmbedRules.setFooter(Bot.WaterMark, BotLogo);
                channel.sendMessage(EmbedRules.build()).queue(message1 -> {
                    e.getMessage().delete().queue();
                });
            }
        }

    }
}