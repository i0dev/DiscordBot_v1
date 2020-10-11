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
import java.util.Random;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
public class TEST_GiveawaysList extends ListenerAdapter {
    public String BotPrefix = Bot.BotPrefix;
    public String BotName = Bot.BotName;
    public ZonedDateTime LocalTime = ZonedDateTime.now();
    public ZonedDateTime now = ZonedDateTime.now();
    public String ColorHexCode = Bot.ColorHexCode;
    public String BotLogo = Bot.BotLogo;
    public String TicketCreateChannelID = Bot.TicketCreateChannelID;
    public String TicketCreateCategoryChannelID = Bot.TicketCreateCategoryChannelID;
    public String StaffMovementsChannelID = "";
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
    public ArrayList<String> StaffRoleIDS = new ArrayList<String>();

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        try {
            JSONObject json = (JSONObject) new JSONParser().parse(new FileReader(new File("Tickets/config.json")));
            BotPrefix = ((HashMap<String, String>) json.get("GeneralConfig")).get("BotPrefix");
            BotName = ((HashMap<String, String>) json.get("GeneralConfig")).get("BotName");
            ColorHexCode = ((HashMap<String, String>) json.get("GeneralConfig")).get("ColorHexCode");
            BotLogo = ((HashMap<String, String>) json.get("GeneralConfig")).get("BotLogo");
            StaffMovementsChannelID = ((HashMap<String, String>) json.get("ChannelIDS")).get("StaffMovementsChannelID");
            AdminRoleID = ((HashMap<String, String>) json.get("RoleIDS")).get("AdminRoleID");
            StaffRoleIDS = ((HashMap<String, ArrayList>) json.get("GeneralConfig")).get("StaffRoleIDS");

        } catch (Exception ee) {
            ee.printStackTrace();
        }

        Color Color = java.awt.Color.decode(ColorHexCode);
        MessageChannel channel = e.getChannel();
        String[] message = e.getMessage().getContentRaw().split(" ");
        if (message.length > 0 && message[0].equalsIgnoreCase(Bot.BotPrefix + "glist")) {
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
        for (int i = 0; i < Bot.AllowedRoles.size(); i++) {
            if (e.getMember().getRoles().contains(e.getGuild().getRoleById(Bot.AllowedRoles.get(i))) || e.getMember().getPermissions().contains(Permission.ADMINISTRATOR)) {
                isAllowed = true;
            }
        }

        if ((message.length == 1) && message[0].equalsIgnoreCase(Bot.BotPrefix + "glist")) {
            if (isAllowed) {

                ArrayList<JSONObject> ArrayOfGiveaways = new ArrayList<>();
                try {
                    JSONObject json = (JSONObject) new JSONParser().parse(new FileReader(new File("Giveaways.json")));
                    ArrayOfGiveaways = (ArrayList<JSONObject>) json.get("Giveaways");
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
                JSONObject CurrentGiveaway = new JSONObject();

                EmbedBuilder EmbedList = new EmbedBuilder()
                        .setTitle(BotName + " Giveaways")
                        .setTimestamp(LocalTime)
                        .setThumbnail(BotLogo)
                        .setColor(Color)
                        .setFooter(Bot.WaterMark, BotLogo);

                ArrayList<String> ArrayOfActiveGiveaways = new ArrayList<>();

                for (int i = 0; i < ArrayOfGiveaways.size(); i++) {
                    if (!(boolean) ArrayOfGiveaways.get(i).get("GiveawayEnded")) {

                        String CurrentGuildID = ArrayOfGiveaways.get(i).get("GuildID").toString();
                        String CurrentChannelID = ArrayOfGiveaways.get(i).get("ChannelID").toString();
                        String CurrentMessageID = ArrayOfGiveaways.get(i).get("MessageID").toString();
                        String CurrentReward = ArrayOfGiveaways.get(i).get("Reward").toString();
                        String CurrentWinnersAmount = ArrayOfGiveaways.get(i).get("WinnersAmount").toString();


                        ArrayOfActiveGiveaways.add(":gift: **" + CurrentReward + "**" + " | <#" + e.getGuild().getTextChannelById(CurrentChannelID).getId() + ">" + " | " + "[Click-to-View](https://discordapp.com/channels/" + CurrentGuildID + "/" + CurrentChannelID + "/" + CurrentMessageID + ")");
                    }
                }
                if (ArrayOfActiveGiveaways.size() == 0) {
                    EmbedList.addField("Active Giveaways:", "`There are no active giveaways!`", true);
                    e.getChannel().sendMessage(EmbedList.build()).queue();
                } else {
                    String ActiveGiveawaysString = "";
                    for (int k = 0; k < ArrayOfActiveGiveaways.size(); k++) {
                        ActiveGiveawaysString = ActiveGiveawaysString + ArrayOfActiveGiveaways.get(k) + "\n";
                    }
                    EmbedList.addField("Active Giveaways:", ActiveGiveawaysString, true);
                    e.getChannel().sendMessage(EmbedList.build()).queue();
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