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
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
public class TEST_GiveawayEnd extends ListenerAdapter {
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
        if (message.length > 0 && message[0].equalsIgnoreCase(Bot.BotPrefix + "gend")) {
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
        if (message.length == 1 && message[0].equalsIgnoreCase(Bot.BotPrefix + "gend")) {
            if (isAllowed) {
                EmbedBuilder EmbedRules = new EmbedBuilder();
                EmbedRules.setTitle("Incorrect Format");
                EmbedRules.setColor(Color);
                EmbedRules.addField("Format:", "" + Bot.BotPrefix + "gend [Message ID]", false);
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

        if ((message.length == 2) && message[0].equalsIgnoreCase(Bot.BotPrefix + "gend")) {
            if (isAllowed) {

                ArrayList<JSONObject> ArrayOfGiveaways = new ArrayList<>();
                try {
                    JSONObject json = (JSONObject) new JSONParser().parse(new FileReader(new File("Giveaways.json")));
                    ArrayOfGiveaways = (ArrayList<JSONObject>) json.get("Giveaways");
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
                boolean GiveawayExists = false;
                boolean GiveawayEnded = false;
                String MessageID = message[1];
                JSONObject CurrentGiveaway = new JSONObject();
                for (int i = 0; i < ArrayOfGiveaways.size(); i++) {
                    if (ArrayOfGiveaways.get(i).get("MessageID").toString().equalsIgnoreCase(MessageID)) {
                        GiveawayExists = true;
                        CurrentGiveaway = ArrayOfGiveaways.get(i);
                        if ((boolean) ArrayOfGiveaways.get(i).get("GiveawayEnded")) {
                            GiveawayEnded = true;
                            ArrayOfGiveaways.remove(i);
                        }
                    }
                }

                if (!GiveawayExists) {
                    EmbedBuilder EmbedFirst = new EmbedBuilder()
                            .setTitle(BotName + " Giveaways")
                            .setColor(java.awt.Color.RED)
                            .addField("Error", "**" + e.getMember().getUser().getAsTag() + "**, That giveaway cannot be found!", false)
                            .setTimestamp(now)
                            .setFooter(Bot.WaterMark, BotLogo);
                    channel.sendMessage(EmbedFirst.build()).queue(message1 -> {
                        e.getMessage().delete().queue();
                    });
                } else {
                    if (GiveawayEnded) {
                        EmbedBuilder EmbedFirst = new EmbedBuilder()
                                .setTitle(BotName + " Giveaways")
                                .setColor(java.awt.Color.RED)
                                .addField("Error", "**" + e.getMember().getUser().getAsTag() + "**, That giveaway is already ended! You can only end running giveaways!", false)
                                .setTimestamp(now)
                                .setFooter(Bot.WaterMark, BotLogo);
                        channel.sendMessage(EmbedFirst.build()).queue(message1 -> {
                            e.getMessage().delete().queue();
                        });
                    } else {

                        EmbedBuilder EmbedFirst = new EmbedBuilder()
                                .setTitle(BotName + " Giveaways")
                                .setColor(Color)
                                .addField("Success", "**" + e.getMember().getUser().getAsTag() + "**, You ended that giveaway", false)
                                .setTimestamp(now)
                                .setFooter(Bot.WaterMark, BotLogo);
                        channel.sendMessage(EmbedFirst.build()).queue(message1 -> {
                            e.getMessage().delete().queue();
                        });


                        String CurrentGiveawayEndTime = CurrentGiveaway.get("EndTimeMillis").toString();
                        long longEndTime = Long.parseLong(CurrentGiveawayEndTime);
                        boolean GiveawayFinished = (boolean) CurrentGiveaway.get("GiveawayEnded");


                        String CurrentGuildID = CurrentGiveaway.get("GuildID").toString();
                        String CurrentChannelID = CurrentGiveaway.get("ChannelID").toString();
                        String CurrentMessageID = CurrentGiveaway.get("MessageID").toString();
                        String CurrentHostID = CurrentGiveaway.get("HostID").toString();
                        String CurrentReward = CurrentGiveaway.get("Reward").toString();
                        String CurrentWinnersAmount = CurrentGiveaway.get("WinnersAmount").toString();
                        String CurrentBotLogo = CurrentGiveaway.get("BotLogo").toString();
                        String CurrentColorHexCode = CurrentGiveaway.get("CurrentColorHexCode").toString();
                        String CurrentGiveawayEndTimeFormat = CurrentGiveaway.get("EndTimeFormat").toString();

                        Color CurrentColor = java.awt.Color.decode(CurrentColorHexCode);

                        java.util.List<User> MembersReacted = new ArrayList<>();
                        List<User> MembersReactedInitial = new ArrayList<>();
                        Message Giveawaymessage = Bot.jda.getGuildById(CurrentGuildID).getTextChannelById(CurrentChannelID).retrieveMessageById(CurrentMessageID).complete();
                        MembersReactedInitial = Giveawaymessage.retrieveReactionUsers("🎉").complete();

                        for (int f = 0; f < MembersReactedInitial.size(); f++) {
                            if (!MembersReactedInitial.get(f).isBot()) {
                                MembersReacted.add(MembersReactedInitial.get(f));
                            }
                        }
                        String WinnersString = "";
                        ArrayList<User> Winners = new ArrayList<>();
                        if (MembersReacted.size() == 0) {
                            WinnersString = "`No Winners :cry:`";
                        } else {
                            Random random = new Random();
                            for (int j = 0; j < Integer.parseInt(CurrentWinnersAmount); j++) {
                                int randomint = random.nextInt(MembersReacted.size());
                                try {
                                    Winners.add(MembersReacted.get(randomint));
                                } catch (Exception error) {

                                }
                            }
                            for (int k = 0; k < Winners.size(); k++) {
                                WinnersString = WinnersString + Winners.get(k).getAsMention() + ", ";
                            }
                            WinnersString = WinnersString.substring(0, WinnersString.length() - 2);
                        }

                        EmbedBuilder EndGiveaway = new EmbedBuilder()
                                .setTitle(":tada: GIVEAWAY ENDED :tada:")
                                .setThumbnail(CurrentBotLogo)
                                .setColor(CurrentColor)
                                .addField(":gift: Prize:", "`" + CurrentReward + "`", false)
                                .addField(":trophy: Winner(s):", WinnersString, false)
                                .addField(":information_source: Total Entries: ", "`" + MembersReacted.size() + "`", false)
                                .addField(":alarm_clock: Giveaway Length: ", "`" + CurrentGiveawayEndTimeFormat + "`", false)
                                .addField(":bust_in_silhouette: Hosted by ", Bot.jda.getGuildById(CurrentGuildID).getMemberById(CurrentHostID).getAsMention(), false)
                                .setTimestamp(LocalTime)
                                .setFooter("Ended at ", CurrentBotLogo);
                        Bot.jda.getGuildById(CurrentGuildID).getTextChannelById(CurrentChannelID).editMessageById(CurrentMessageID, EndGiveaway.build()).queue();
                        Bot.jda.getGuildById(CurrentGuildID).getTextChannelById(CurrentChannelID).clearReactionsById(CurrentMessageID).queue();


                        EmbedBuilder EmbedWinners = new EmbedBuilder()
                                .setTitle(":trophy: GIVEAWAY WINNERS :trophy")
                                .setThumbnail(CurrentBotLogo)
                                .setColor(CurrentColor)
                                .setTimestamp(LocalTime)
                                .setDescription("[Click to view original giveaway](https://discordapp.com/channels/" + CurrentGuildID + "/" + CurrentChannelID + "/" + CurrentMessageID + ")")
                                .addField(":trophy: Winner(s):", WinnersString, false)
                                .setFooter("Ended at ", CurrentBotLogo);
                        Bot.jda.getGuildById(CurrentGuildID).getTextChannelById(CurrentChannelID).sendMessage(EmbedWinners.build()).queue();


                        CurrentGiveaway.put("GiveawayEnded", true);
                        ArrayList<String> TotalReactedMemberIDS = new ArrayList<>();
                        for (int a = 0; a < MembersReacted.size(); a++) {
                            TotalReactedMemberIDS.add(MembersReacted.get(a).getId());
                        }
                        CurrentGiveaway.put("TotalReactedMemberIDS", TotalReactedMemberIDS);

                        ArrayList<String> ArrayOfWinnersID = new ArrayList<>();
                        for (int k = 0; k < Winners.size(); k++) {
                            ArrayOfWinnersID.add(Winners.get(k).getId());
                        }

                        CurrentGiveaway.put("WinnersID", ArrayOfWinnersID);


                        JSONObject all = new JSONObject();
                        all.put("Giveaways", ArrayOfGiveaways);

                        try {
                            Files.write(Paths.get("Giveaways.json"), all.toJSONString().getBytes());
                        } catch (Exception ef) {
                            ef.printStackTrace();
                        }


                    }

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