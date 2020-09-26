import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")

public class Ticket_Close extends ListenerAdapter {
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

    public String AdminLogsChannelID = "";
    public String TicketLogsChannelID = "";

    String ClosedReason = "No Reason Provided.";

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        try {
            JSONObject json = (JSONObject) new JSONParser().parse(new FileReader(new File("Tickets/config.json")));
            BotPrefix = ((HashMap<String, String>) json.get("GeneralConfig")).get("BotPrefix");
            BotName = ((HashMap<String, String>) json.get("GeneralConfig")).get("BotName");
            BotLogo = ((HashMap<String, String>) json.get("GeneralConfig")).get("BotLogo");
            ColorHexCode = ((HashMap<String, String>) json.get("GeneralConfig")).get("ColorHexCode");
            TicketCreateChannelID = ((HashMap<String, String>) json.get("ChannelIDS")).get("TicketCreateChannelID");
            TicketCreateCategoryChannelID = ((HashMap<String, String>) json.get("ChannelIDS")).get("TicketCreateCategoryChannelID");
            AdminLogsChannelID = ((HashMap<String, String>) json.get("ChannelIDS")).get("AdminLogsChannelID");
            TicketLogsChannelID = ((HashMap<String, String>) json.get("ChannelIDS")).get("TicketLogsChannelID");

            MemberRoleID = ((HashMap<String, String>) json.get("RoleIDS")).get("MemberRoleID");
            SupportTeamRoleID = ((HashMap<String, String>) json.get("RoleIDS")).get("SupportTeamRoleID");
            AdminRoleID = ((HashMap<String, String>) json.get("RoleIDS")).get("AdminRoleID");
        } catch (Exception ee) {
            ee.printStackTrace();
        }
        if (new File("Tickets/Storage/" + e.getChannel().getId() + ".json").exists()) {

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

            } catch (Exception ee) {
                ee.printStackTrace();
            }
        }
        java.awt.Color Color = java.awt.Color.decode(ColorHexCode);


        MessageChannel channel = e.getChannel();
        GuildChannel permChan = e.getChannel();
        Role supportRole = e.getGuild().getRoleById(SupportTeamRoleID);
        String[] message = e.getMessage().getContentDisplay().split(" ");
        String[] messageWithSplit = e.getMessage().getContentRaw().split(" ");
        if (messageWithSplit.length > 0 && messageWithSplit[0].equalsIgnoreCase(Bot.BotPrefix + "close")) {
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
        if (message[0].equalsIgnoreCase(BotPrefix + "close")) {

            if (ChannelID.equals("")) {
                EmbedBuilder EmbedRules = new EmbedBuilder();

                EmbedRules.setTitle("Error");
                EmbedRules.setDescription("This command can only be executed in a ticket");

                EmbedRules.setColor(Color);
                EmbedRules.setTimestamp(LocalTime);
                EmbedRules.setFooter("Request From " + e.getAuthor().getAsTag(), BotLogo);
                channel.sendMessage(EmbedRules.build()).queue(message1 -> {
                    e.getMessage().delete().queueAfter(3, TimeUnit.SECONDS);
                });

            } else {


                MessageHistory history = e.getChannel().getHistoryFromBeginning(100).complete();
                List<Message> messageHistory = history.getRetrievedHistory();
                if (message.length != 1) {
                    ClosedReason = "";
                    String[] ClosedReasonArray = new String[message.length];
                    for (int i = 1; i < message.length; i++) {
                        ClosedReason = ClosedReason + " " + message[i];
                    }
                    if (ClosedReason.substring(ClosedReason.length() - 1).equals(".")) {

                    } else {
                        ClosedReason = ClosedReason + ".";
                    }
                }
                String SubUsersString = "";
                for (int i = 0; i < SubUsers.size(); i++) {
                    String Member = e.getGuild().getMemberById(SubUsers.get(i)).getUser().getAsTag();
                    SubUsersString = SubUsersString + Member.toString() + ", ";
                }
                if (SubUsers.size() >= 1) {
                    SubUsersString.substring(0, SubUsersString.length() - 2);
                }
                String toFile = "Ticket Information\n\n" +
                        "Channel ID = " + ChannelID + "\n" +
                        "Channel Name = " + ChannelName + "\n" +
                        "Category ID = " + CategoryID + "\n" +
                        "Ticket Owner ID = " + UsersID + "\n" +
                        "Ticket Owner Tag = " + UsersTag + "\n" +
                        "Ticket Owner Avatar URL = " + UsersAvatarURL + "\n" +
                        "Admin Only Status = " + AdminOnlyMode + "\n" +
                        "SubUsers ID List = " + SubUsersString + "\n" +
                        "Ticket Closed Reason = " + ClosedReason + "\n" +
                        "Ticket Closer User ID = " + e.getMember().getId() + "\n" +
                        "Ticket Closer User Tag = " + e.getMember().getUser().getAsTag() +
                        "\n    -- Ticket Logs Below  -- \n\n\n\n";

                for (int i = messageHistory.size() - 1; i > 0; i--) {

                    String DayMonthMessage = messageHistory.get(i).getTimeCreated().toZonedDateTime().getMonth().name()
                            + "";
                    String DayYearMessage = messageHistory.get(i).getTimeCreated().toZonedDateTime().getDayOfMonth() + "";
                    String YearMessage = messageHistory.get(i).getTimeCreated().toZonedDateTime().getYear() + "";
                    String HourMessage = messageHistory.get(i).getTimeCreated()
                            .atZoneSameInstant(ZoneId.of("America/New_York")).getHour() + "";
                    String MinMessage = messageHistory.get(i).getTimeCreated().toZonedDateTime().getMinute() + "";
                    String ZoneMessage = messageHistory.get(i).getTimeCreated().toZonedDateTime().getZone() + "";
                    String FinalDateFormat = "[" + DayMonthMessage + ", " + DayYearMessage + ", " + YearMessage + "] at ["
                            + HourMessage + ":" + MinMessage + " " + "EST" + "] ";

                    try {
                        messageHistory.get(i).getMember().getUser().getAsTag();
                        toFile = toFile + FinalDateFormat + messageHistory.get(i).getMember().getUser().getAsTag() + ""
                                + " : " + messageHistory.get(i).getContentRaw() + "\n\n";
                        String AuthorName = e.getMember().getUser().getAsTag();

                    } catch (Exception ff) {
                        toFile = toFile + FinalDateFormat + UsersTag + " → " + messageHistory.get(i).getContentRaw()
                                + "\n\n";
                        String AuthorName = UsersTag;

                    }
                }
                ZonedDateTime Time = Bot.LocalTime;
                File file = new File("Tickets/Logs/" + ChannelName + ".txt");

                try {
                    Files.write(Paths.get(file.getAbsolutePath()), toFile.getBytes());

                } catch (IOException e1) {
                    e1.printStackTrace();
                }


                Member MentionedMember = e.getMember();
                long CurrnetWarningCount = 1;
                ArrayList<ArrayList<Long>> IDMatcher = new ArrayList<>();
                ArrayList<Long> IDMatcherInt = new ArrayList<>();

                if (new File("Tickets/TicketTop.json").exists()) {
                    try {
                        JSONObject json = (JSONObject) new JSONParser().parse(new FileReader(new File("Tickets/TicketTop.json")));
                        IDMatcher = (ArrayList<ArrayList<Long>>) json.get("TicketTop");
                    } catch (Exception ee) {
                        ee.printStackTrace();
                    }
                    for (int i = 0; i < IDMatcher.size(); i++) {
                        if (IDMatcher.get(i).get(0).equals(MentionedMember.getIdLong())) {
                            CurrnetWarningCount = IDMatcher.get(i).get(1);
                            CurrnetWarningCount++;
                            IDMatcher.remove(i);
                        }
                    }
                    IDMatcherInt.add(MentionedMember.getIdLong());
                    IDMatcherInt.add(CurrnetWarningCount);
                    IDMatcher.add(IDMatcherInt);


                    if (new File("Tickets/TicketTop.json").exists()) {
                        try {

                            JSONObject all = new JSONObject();
                            all.put("TicketTop", IDMatcher);

                            try {
                                Files.write(Paths.get("Tickets/TicketTop.json"), all.toJSONString().getBytes());
                            } catch (Exception ef) {
                                ef.printStackTrace();
                            }
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }

                }


                try {
                    MessageChannel memberPrivate = e.getGuild().getMemberById(UsersID).getUser().openPrivateChannel().complete();
                    EmbedBuilder EmbedRules = new EmbedBuilder();
                    EmbedRules.setTitle("Your Ticket was closed");
                    EmbedRules.setColor(Color);
                    EmbedRules.setThumbnail(UsersAvatarURL);
                    EmbedRules.addField("Ticket ID:", "**" + ChannelName + "**", false);
                    EmbedRules.addField("Closed By:", "**" + e.getAuthor().getAsTag() + "**", false);
                    EmbedRules.addField("Close Reason:", "**" + ClosedReason + "**", false);

                    EmbedRules.setTimestamp(Bot.LocalTime);
                    EmbedRules.setFooter("Closed By " + e.getAuthor().getAsTag(), e.getMember().getUser().getAvatarUrl());
                    memberPrivate.sendMessage(EmbedRules.build()).queue();
                    memberPrivate.sendFile(file).queue();

                } catch (Exception pp) {
                    pp.printStackTrace();
                    EmbedBuilder INFO = new EmbedBuilder()
                            .setColor(Color)
                            .setDescription("**[INFO]** The user left the discord, so they did not receive ticket logs");
                    e.getChannel().sendMessage(INFO.build()).queue();
                }


                EmbedBuilder EmbedRules1 = new EmbedBuilder();
                EmbedRules1.setTitle(UsersTag + "'s Ticket was closed");
                EmbedRules1.setThumbnail(UsersAvatarURL);
                EmbedRules1.setColor(Color);
                EmbedRules1.addField("Ticket ID:", "**" + ChannelName + "**", false);
                EmbedRules1.addField("Closed By:", "**" + e.getAuthor().getAsTag() + "**", false);
                EmbedRules1.addField("Close Reason:", "**" + ClosedReason + "**", false);
                ClosedReason = "No Reason Provided.";
                EmbedRules1.setTimestamp(Bot.LocalTime);
                EmbedRules1.setFooter("Closed By " + e.getAuthor().getAsTag(), e.getMember().getUser().getAvatarUrl());


                if (AdminOnlyMode) {
                    e.getGuild().getTextChannelById(AdminLogsChannelID).sendMessage(EmbedRules1.build()).queueAfter(5, TimeUnit.SECONDS);
                    e.getGuild().getTextChannelById(AdminLogsChannelID).sendFile(file).queueAfter(5, TimeUnit.SECONDS);
                    EmbedBuilder CounntDown = new EmbedBuilder()
                            .setColor(Color)
                            .setDescription(ChannelName + " will close in 5 seconds");
                    e.getChannel().sendMessage(CounntDown.build()).queue();
                    e.getChannel().delete().queueAfter(5, TimeUnit.SECONDS);

                    new File("Tickets/Storage/" + ChannelID + ".json").delete();

                } else {
                    e.getGuild().getTextChannelById(TicketLogsChannelID).sendMessage(EmbedRules1.build()).queueAfter(5, TimeUnit.SECONDS);
                    e.getGuild().getTextChannelById(TicketLogsChannelID).sendFile(file).queueAfter(5, TimeUnit.SECONDS);
                    EmbedBuilder CounntDown = new EmbedBuilder()
                            .setColor(Color)
                            .setDescription(ChannelName + " will close in 5 seconds");
                    e.getChannel().sendMessage(CounntDown.build()).queue();
                    e.getChannel().delete().queueAfter(5, TimeUnit.SECONDS);

                    new File("Tickets/Storage/" + ChannelID + ".json").delete();

                }
            }
        }
    }

    @Override
    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent e) {
        try {
            JSONObject json = (JSONObject) new JSONParser().parse(new FileReader(new File("Tickets/config.json")));
            BotPrefix = ((HashMap<String, String>) json.get("GeneralConfig")).get("BotPrefix");
            BotName = ((HashMap<String, String>) json.get("GeneralConfig")).get("BotName");
            BotLogo = ((HashMap<String, String>) json.get("GeneralConfig")).get("BotLogo");
            ColorHexCode = ((HashMap<String, String>) json.get("GeneralConfig")).get("ColorHexCode");
            TicketCreateChannelID = ((HashMap<String, String>) json.get("ChannelIDS")).get("TicketCreateChannelID");
            TicketCreateCategoryChannelID = ((HashMap<String, String>) json.get("ChannelIDS")).get("TicketCreateCategoryChannelID");
            AdminLogsChannelID = ((HashMap<String, String>) json.get("ChannelIDS")).get("AdminLogsChannelID");
            TicketLogsChannelID = ((HashMap<String, String>) json.get("ChannelIDS")).get("TicketLogsChannelID");

            MemberRoleID = ((HashMap<String, String>) json.get("RoleIDS")).get("MemberRoleID");
            SupportTeamRoleID = ((HashMap<String, String>) json.get("RoleIDS")).get("SupportTeamRoleID");
            AdminRoleID = ((HashMap<String, String>) json.get("RoleIDS")).get("AdminRoleID");
        } catch (Exception ee) {
            ee.printStackTrace();
        }
        if (new File("Tickets/Storage/" + e.getChannel().getId() + ".json").exists()) {

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

            } catch (Exception ee) {
                ee.printStackTrace();
            }
        }
        java.awt.Color Color = java.awt.Color.decode(ColorHexCode);


        MessageChannel channel = e.getChannel();
        GuildChannel permChan = e.getChannel();
        Role supportRole = e.getGuild().getRoleById(SupportTeamRoleID);

        if (e.getReactionEmote().getName().equals("🗑️") && (e.getMember().getRoles().contains(supportRole) || e.getMember().getRoles().contains(e.getGuild().getRoleById(AdminRoleID)))) {

            if (ChannelID.equals("")) {
                EmbedBuilder EmbedRules = new EmbedBuilder();

                EmbedRules.setTitle("Error");
                EmbedRules.setDescription("You can only close a ticket, in an actual ticket");

                EmbedRules.setColor(Color);
                EmbedRules.setTimestamp(LocalTime);
                EmbedRules.setFooter("Request From " + e.getUser().getAsTag(), BotLogo);
                channel.sendMessage(EmbedRules.build()).queue();

            } else {


                MessageHistory history = e.getChannel().getHistoryFromBeginning(100).complete();
                List<Message> messageHistory = history.getRetrievedHistory();

                ClosedReason = "Closed By React";
                String SubUsersString = "";
                for (int i = 0; i < SubUsers.size(); i++) {
                    String Member = e.getGuild().getMemberById(SubUsers.get(i)).getUser().getAsTag();
                    SubUsersString = SubUsersString + Member.toString() + ", ";
                }
                if (SubUsers.size() >= 1) {
                    SubUsersString.substring(0, SubUsersString.length() - 2);
                }
                String toFile = "Ticket Information\n\n" +
                        "Channel ID = " + ChannelID + "\n" +
                        "Channel Name = " + ChannelName + "\n" +
                        "Category ID = " + CategoryID + "\n" +
                        "Ticket Owner ID = " + UsersID + "\n" +
                        "Ticket Owner Tag = " + UsersTag + "\n" +
                        "Ticket Owner Avatar URL = " + UsersAvatarURL + "\n" +
                        "Admin Only Status = " + AdminOnlyMode + "\n" +
                        "SubUsers ID List = " + SubUsersString + "\n" +
                        "Ticket Closed Reason = " + ClosedReason + "\n" +
                        "Ticket Closer User ID = " + e.getMember().getId() + "\n" +
                        "Ticket Closer User Tag = " + e.getMember().getUser().getAsTag() +
                        "\n    -- Ticket Logs Below  -- \n\n\n\n";


                for (int i = messageHistory.size() - 1; i > 0; i--) {
                    String DayMonthMessage = messageHistory.get(i).getTimeCreated().toZonedDateTime().getMonth().name()
                            + "";
                    String DayYearMessage = messageHistory.get(i).getTimeCreated().toZonedDateTime().getDayOfMonth() + "";
                    String YearMessage = messageHistory.get(i).getTimeCreated().toZonedDateTime().getYear() + "";
                    String HourMessage = messageHistory.get(i).getTimeCreated()
                            .atZoneSameInstant(ZoneId.of("America/New_York")).getHour() + "";
                    String MinMessage = messageHistory.get(i).getTimeCreated().toZonedDateTime().getMinute() + "";
                    String ZoneMessage = messageHistory.get(i).getTimeCreated().toZonedDateTime().getZone() + "";
                    String FinalDateFormat = "[" + DayMonthMessage + ", " + DayYearMessage + ", " + YearMessage + "] at ["
                            + HourMessage + ":" + MinMessage + " " + "EST" + "] ";

                    try {
                        messageHistory.get(i).getMember().getUser().getAsTag();
                        toFile = toFile + FinalDateFormat + messageHistory.get(i).getMember().getUser().getAsTag() + ""
                                + " : " + messageHistory.get(i).getContentRaw() + "\n\n";
                        String AuthorName = e.getMember().getUser().getAsTag();

                    } catch (Exception ff) {
                        toFile = toFile + FinalDateFormat + UsersTag + " → " + messageHistory.get(i).getContentRaw()
                                + "\n\n";
                        String AuthorName = UsersTag;

                    }
                }
                ZonedDateTime Time = Bot.LocalTime;
                File file = new File("Tickets/Logs/" + ChannelName + ".txt");

                try {
                    Files.write(Paths.get(file.getAbsolutePath()), toFile.getBytes());

                } catch (IOException e1) {
                    e1.printStackTrace();
                }


                try {
                    MessageChannel memberPrivate = e.getGuild().getMemberById(UsersID).getUser().openPrivateChannel().complete();
                    EmbedBuilder EmbedRules = new EmbedBuilder();
                    EmbedRules.setTitle("Your Ticket was closed");
                    EmbedRules.setColor(Color);
                    EmbedRules.setThumbnail(UsersAvatarURL);
                    EmbedRules.addField("Ticket ID:", "**" + ChannelName + "**", false);
                    EmbedRules.addField("Closed By:", "**" + e.getUser().getAsTag() + "**", false);
                    EmbedRules.addField("Close Reason:", "**" + ClosedReason + "**", false);

                    EmbedRules.setTimestamp(Bot.LocalTime);
                    EmbedRules.setFooter("Closed By " + e.getUser().getAsTag(), e.getMember().getUser().getAvatarUrl());
                    memberPrivate.sendMessage(EmbedRules.build()).queue();
                    memberPrivate.sendFile(file).queue();

                } catch (Exception pp) {
                    pp.printStackTrace();
                    EmbedBuilder INFO = new EmbedBuilder()
                            .setColor(Color)
                            .setDescription("**[INFO]** The user left the discord, so they did not receive ticket logs");
                    e.getChannel().sendMessage(INFO.build()).queue();
                }


                EmbedBuilder EmbedRules1 = new EmbedBuilder();
                EmbedRules1.setTitle(UsersTag + "'s Ticket was closed");
                EmbedRules1.setThumbnail(UsersAvatarURL);
                EmbedRules1.setColor(Color);
                EmbedRules1.addField("Ticket ID:", "**" + ChannelName + "**", false);
                EmbedRules1.addField("Closed By:", "**" + e.getUser().getAsTag() + "**", false);
                EmbedRules1.addField("Close Reason:", "**" + ClosedReason + "**", false);
                ClosedReason = "No Reason Provided.";
                EmbedRules1.setTimestamp(Bot.LocalTime);
                EmbedRules1.setFooter("Closed By " + e.getUser().getAsTag(), e.getMember().getUser().getAvatarUrl());


                Member MentionedMember = e.getMember();
                long CurrnetWarningCount = 1;
                ArrayList<ArrayList<Long>> IDMatcher = new ArrayList<>();
                ArrayList<Long> IDMatcherInt = new ArrayList<>();

                if (new File("Tickets/TicketTop.json").exists()) {
                    try {
                        JSONObject json = (JSONObject) new JSONParser().parse(new FileReader(new File("Tickets/TicketTop.json")));
                        IDMatcher = (ArrayList<ArrayList<Long>>) json.get("TicketTop");
                    } catch (Exception ee) {
                        ee.printStackTrace();
                    }
                    for (int i = 0; i < IDMatcher.size(); i++) {
                        if (IDMatcher.get(i).get(0).equals(MentionedMember.getIdLong())) {
                            CurrnetWarningCount = IDMatcher.get(i).get(1);
                            CurrnetWarningCount++;
                            IDMatcher.remove(i);
                        }
                    }
                    IDMatcherInt.add(MentionedMember.getIdLong());
                    IDMatcherInt.add(CurrnetWarningCount);
                    IDMatcher.add(IDMatcherInt);


                    if (new File("Tickets/TicketTop.json").exists()) {
                        try {

                            JSONObject all = new JSONObject();
                            all.put("TicketTop", IDMatcher);

                            try {
                                Files.write(Paths.get("Tickets/TicketTop.json"), all.toJSONString().getBytes());
                            } catch (Exception ef) {
                                ef.printStackTrace();
                            }
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }

                }


                if (AdminOnlyMode) {
                    e.getGuild().getTextChannelById(AdminLogsChannelID).sendMessage(EmbedRules1.build()).queueAfter(5, TimeUnit.SECONDS);
                    e.getGuild().getTextChannelById(AdminLogsChannelID).sendFile(file).queueAfter(5, TimeUnit.SECONDS);
                    EmbedBuilder CounntDown = new EmbedBuilder()
                            .setColor(Color)
                            .setDescription(ChannelName + " will close in 5 seconds");
                    e.getChannel().sendMessage(CounntDown.build()).queue();
                    e.getChannel().delete().queueAfter(5, TimeUnit.SECONDS);
                    new File("Tickets/Storage/" + ChannelID + ".json").delete();


                } else {
                    e.getGuild().getTextChannelById(TicketLogsChannelID).sendMessage(EmbedRules1.build()).queueAfter(5, TimeUnit.SECONDS);
                    e.getGuild().getTextChannelById(TicketLogsChannelID).sendFile(file).queueAfter(5, TimeUnit.SECONDS);
                    EmbedBuilder CounntDown = new EmbedBuilder()
                            .setColor(Color)
                            .setDescription(ChannelName + " will close in 5 seconds");
                    e.getChannel().sendMessage(CounntDown.build()).queue();
                    e.getChannel().delete().queueAfter(5, TimeUnit.SECONDS);
                    new File("Tickets/Storage/" + ChannelID + ".json").delete();

                }
            }
        }
    }
}