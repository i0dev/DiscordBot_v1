import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
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
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
public class TEST_Apps extends ListenerAdapter {
    public String BotPrefix = Bot.BotPrefix;
    public String BotName = Bot.BotName;
    public ZonedDateTime LocalTime = Bot.LocalTime;
    public String ColorHexCode = Bot.ColorHexCode;
    public String BotLogo = Bot.BotLogo;
    public String LogsChannelID = Bot.LogsChannelID;
    public static ArrayList<String> AllowedRoles = new ArrayList<String>();
    public String AdminRoleID = Bot.AdminRoleID;
    public User applicant;
    Member applicantMember;
    PrivateChannel DMS;
    public String userID;
    public ArrayList<String> responses = new ArrayList<>();
    public ArrayList<String> Questions = new ArrayList<>();
    public boolean AppsEnabled = true;
    public int SecondsPassed = 0;
    int identifier = 0;
    int CurrentQuestion = 0;


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
            AppsEnabled = ((HashMap<String, Boolean>) json.get("GeneralConfig")).get("AppsEnabled");

        } catch (Exception ee) {
            ee.printStackTrace();
        }
        Color Color = java.awt.Color.decode(Bot.ColorHexCode);
        MessageChannel channel = e.getChannel();
        String[] message = e.getMessage().getContentRaw().split(" ");
        if (message.length > 0 && message[0].equalsIgnoreCase(Bot.BotPrefix + "Apply")) {
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
        if (message.length > 0 && message[0].equalsIgnoreCase(Bot.BotPrefix + "CancelApps")) {
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

        if (message.length == 1 && message[0].equalsIgnoreCase(Bot.BotPrefix + "CancelApps") && !e.getAuthor().isBot()) {
            if (isAllowed) {
                if (CurrentQuestion == -1) {
                    EmbedBuilder EmbedRules = new EmbedBuilder();
                    EmbedRules.setTitle("Applications");
                    EmbedRules.setColor(Color);
                    EmbedRules.addField("Error:", e.getAuthor().getAsTag() + ", There is currently no one submitting an application.", false);
                    EmbedRules.setTimestamp(Bot.now);
                    EmbedRules.setFooter(Bot.WaterMark, Bot.Logo);
                    channel.sendMessage(EmbedRules.build()).queue(message1 -> {
                        e.getMessage().delete().queue();
                    });
                } else {
                    EmbedBuilder EmbedRules = new EmbedBuilder();
                    EmbedRules.setTitle("Applications");
                    EmbedRules.setColor(Color);
                    EmbedRules.addField("Success:", e.getAuthor().getAsTag() + ", You have canceled all current applications", false);
                    EmbedRules.setTimestamp(Bot.now);
                    EmbedRules.setFooter(Bot.WaterMark, Bot.Logo);
                    channel.sendMessage(EmbedRules.build()).queue(message1 -> {
                        e.getMessage().delete().queue();
                    });
                    CurrentQuestion = -1;
                    SecondsPassed = 900000000;
                }
            } else {
                EmbedBuilder EmbedRules = new EmbedBuilder();
                EmbedRules.setTitle("Insufficient Permissions");
                EmbedRules.setColor(Color);
                EmbedRules.addField("Error:", "You do not have permission to run this command!", false);
                EmbedRules.setTimestamp(Bot.now);
                EmbedRules.setFooter(Bot.WaterMark, Bot.Logo);
                channel.sendMessage(EmbedRules.build()).queue(message1 -> {
                    e.getMessage().delete().queue();
                });
            }
        }
        if (message.length == 1 && message[0].equalsIgnoreCase(Bot.BotPrefix + "apply") && !e.getAuthor().isBot()) {
            if (CurrentQuestion == -1 || CurrentQuestion == 0) {
                if (!AppsEnabled) {
                    EmbedBuilder EmbedRules = new EmbedBuilder();
                    EmbedRules.setTitle(BotName + " Applications");
                    EmbedRules.setColor(Color);
                    EmbedRules.addField("Error:", "Applications are currently disabled.", false);
                    EmbedRules.setTimestamp(Bot.now);
                    EmbedRules.setFooter(Bot.WaterMark, Bot.Logo);
                    channel.sendMessage(EmbedRules.build()).queue(message1 -> {
                        e.getMessage().delete().queue();
                    });
                } else {
                    ArrayList<ArrayList<String>> ApplicationsList = new ArrayList<>();
                    if (new File("Applications.json").exists()) {
                        try {
                            JSONObject json = (JSONObject) new JSONParser().parse(new FileReader(new File("Applications.json")));
                            ApplicationsList = (ArrayList<ArrayList<String>>) json.get("ApplicationsList");
                        } catch (Exception ee) {
                            ee.printStackTrace();
                        }
                    }
                    if (ApplicationsList.size() == 0) {
                        this.applicant = e.getMember().getUser();
                        this.applicantMember = e.getMember();
                        final EmbedBuilder EmbedRules = new EmbedBuilder();
                        this.userID = e.getAuthor().getId();
                        EmbedRules.setTitle("Application");
                        EmbedRules.setDescription("Hey **" + e.getMember().getUser().getAsTag() + "**, Please look at your Direct messages");
                        EmbedRules.setColor(Color);
                        EmbedRules.setTimestamp(Bot.now);
                        EmbedRules.setFooter(Bot.WaterMark, Bot.Logo);
                        channel.sendMessage(EmbedRules.build()).queue(message1 -> e.getMessage().delete().queueAfter(3, TimeUnit.SECONDS));
                        this.DMS = e.getMember().getUser().openPrivateChannel().complete();
                        final EmbedBuilder DMSHeader = new EmbedBuilder();
                        DMSHeader.setDescription("**Application Started!** \n\n Type **.cancel** at any time to stop the application");
                        DMSHeader.setColor(java.awt.Color.GREEN);
                        DMSHeader.setFooter(Bot.WaterMark, Bot.Logo);
                        this.DMS.sendMessage(DMSHeader.build()).queue();

                        userID = e.getAuthor().getId();


                        final EmbedBuilder Question1 = new EmbedBuilder();
                        Questions = Bot.ApplicationQuestions;
                        Questions.add("Type **Submit** to submit your application");
                        Question1.setTitle("Question " + 1 + "/" + (Questions.size() - 1));

                        Question1.setDescription(Questions.get(0));
                        Question1.setColor(Color);
                        Question1.setFooter(Bot.WaterMark, Bot.Logo);
                        applicant = e.getAuthor();
                        DMS.sendMessage(Question1.build()).queue();
                        CurrentQuestion = 1;
                        responses.clear();

                        SecondsPassed = 0;
                        Timer myTimer = new Timer();
                        TimerTask task = new TimerTask() {
                            public void run() {
                                SecondsPassed++;

                                while (SecondsPassed > (int) (Bot.ApplicationTimeoutTimeSECONDS) && SecondsPassed < 900000000) {
                                    SecondsPassed = 0;
                                    CurrentQuestion = -1;
                                    EmbedBuilder ember = new EmbedBuilder()
                                            .setTitle("Application")
                                            .setDescription("Your current application has timed out!, after `" + Bot.ApplicationTimeoutTimeSECONDS + "` seconds of inactivity.")
                                            .setFooter(Bot.WaterMark, Bot.Logo)
                                            .setColor(Color.RED);
                                    Bot.jda.getUserById(applicant.getId()).openPrivateChannel().complete().sendMessage(ember.build()).queue();
                                    myTimer.cancel();
                                }
                                while (SecondsPassed >= 900000000) {
                                    SecondsPassed = 0;
                                    CurrentQuestion = -1;
                                    myTimer.cancel();
                                    responses.clear();
                                    Questions.remove(Questions.size() - 1);

                                }
                            }
                        };
                        myTimer.scheduleAtFixedRate(task, 0, 1000);
                    } else {

                        boolean StopAllForLoops = false;
                        for (int j = 0; j < ApplicationsList.size(); j++) {
                            boolean isAlreadyApplied = false;
                            for (int f = 0; f < ApplicationsList.size(); f++) {
                                if (e.getMember().getId().equals(ApplicationsList.get(f).get(0))) {
                                    isAlreadyApplied = true;
                                }
                            }
                            if (isAlreadyApplied == false && StopAllForLoops == false) {
                                this.applicant = e.getMember().getUser();
                                this.applicantMember = e.getMember();
                                final EmbedBuilder EmbedRules = new EmbedBuilder();
                                this.userID = e.getAuthor().getId();
                                EmbedRules.setTitle("Application");
                                EmbedRules.setDescription("Hey **" + e.getMember().getUser().getAsTag() + "**, Please look at your Direct messages");
                                EmbedRules.setColor(Color);
                                EmbedRules.setTimestamp(Bot.now);
                                EmbedRules.setFooter(Bot.WaterMark, Bot.Logo);
                                channel.sendMessage(EmbedRules.build()).queue(message1 -> e.getMessage().delete().queueAfter(3, TimeUnit.SECONDS));
                                this.DMS = e.getMember().getUser().openPrivateChannel().complete();
                                final EmbedBuilder DMSHeader = new EmbedBuilder();
                                DMSHeader.setDescription("**Application Started!** \n\n Type **.cancel** at any time to stop the application");
                                DMSHeader.setColor(java.awt.Color.GREEN);
                                DMSHeader.setFooter(Bot.WaterMark, Bot.Logo);
                                this.DMS.sendMessage(DMSHeader.build()).queue();

                                userID = e.getAuthor().getId();


                                final EmbedBuilder Question1 = new EmbedBuilder();
                                Questions = Bot.ApplicationQuestions;
                                Questions.add("Type **Submit** to submit your application");
                                Question1.setTitle("Question " + 1 + "/" + (Questions.size() - 1));

                                Question1.setDescription(Questions.get(0));
                                Question1.setColor(Color);
                                Question1.setFooter(Bot.WaterMark, Bot.Logo);
                                applicant = e.getAuthor();
                                DMS.sendMessage(Question1.build()).queue();
                                CurrentQuestion = 1;
                                responses.clear();


                                SecondsPassed = 0;
                                Timer myTimer = new Timer();
                                TimerTask task = new TimerTask() {
                                    public void run() {
                                        SecondsPassed++;

                                        while (SecondsPassed > (int) (Bot.ApplicationTimeoutTimeSECONDS) && SecondsPassed < 900000000) {
                                            SecondsPassed = 0;
                                            CurrentQuestion = -1;
                                            EmbedBuilder ember = new EmbedBuilder()
                                                    .setTitle("Application")
                                                    .setDescription("Your current application has timed out!, after `" + Bot.ApplicationTimeoutTimeSECONDS + "` seconds of inactivity.")
                                                    .setFooter(Bot.WaterMark, Bot.Logo)
                                                    .setColor(Color.RED);
                                            Bot.jda.getUserById(applicant.getId()).openPrivateChannel().complete().sendMessage(ember.build()).queue();
                                            myTimer.cancel();
                                        }
                                        while (SecondsPassed >= 900000000) {
                                            SecondsPassed = 0;
                                            CurrentQuestion = -1;
                                            myTimer.cancel();
                                            responses.clear();
                                            Questions.remove(Questions.size() - 1);

                                        }
                                    }
                                };
                                myTimer.scheduleAtFixedRate(task, 0, 1000);
                                StopAllForLoops = true;
                                j = ApplicationsList.size();

                            } else {
                                EmbedBuilder EmbedRules = new EmbedBuilder();
                                EmbedRules.setTitle(BotName + " Applications");
                                EmbedRules.setColor(Color);
                                EmbedRules.addField("Error:", "**" + e.getMember().getUser().getAsTag() + "**, You already have an application submitted!", false);
                                EmbedRules.setTimestamp(Bot.now);
                                EmbedRules.setFooter(Bot.WaterMark, Bot.Logo);
                                channel.sendMessage(EmbedRules.build()).queue(message1 -> {
                                });
                                StopAllForLoops = true;
                                j = ApplicationsList.size();

                            }
                        }
                    }
                }
            } else {
                final EmbedBuilder EmbedRules = new EmbedBuilder();
                this.userID = e.getAuthor().getId();
                EmbedRules.setTitle("On-Going Application");
                EmbedRules.setDescription("**Hey " + e.getMember().getUser().getAsTag() + ", Someone is already creating a ticket, Their app" +
                        " will expire in `" + (Bot.ApplicationTimeoutTimeSECONDS - SecondsPassed) + "` seconds, if they do not answer a question within that time.**");
                EmbedRules.setColor(Color);
                EmbedRules.setTimestamp(Bot.now);
                EmbedRules.setFooter(Bot.WaterMark, Bot.Logo);
                channel.sendMessage(EmbedRules.build()).queue(message1 -> e.getMessage().delete().queueAfter(3L, TimeUnit.SECONDS));
            }
        }
    }

    @Override
    public void onPrivateMessageReceived(final PrivateMessageReceivedEvent e) {
        final Color Color = java.awt.Color.decode(Bot.ColorHexCode);
        if (e.getMessage().getContentRaw().equalsIgnoreCase(Bot.BotPrefix + "cancel") && !e.getAuthor().isBot() && e.getChannel().getUser().equals(this.applicant) && this.CurrentQuestion != -1) {
            final EmbedBuilder EmbedRules = new EmbedBuilder();
            this.userID = e.getAuthor().getId();
            EmbedRules.setTitle("Application");
            EmbedRules.setDescription("**you canceled your application**");
            EmbedRules.setColor(Color);
            EmbedRules.setTimestamp(Bot.now);
            EmbedRules.setFooter(Bot.WaterMark);
            e.getChannel().sendMessage(EmbedRules.build()).queue();
            this.CurrentQuestion = -1;
            this.responses.clear();
            Questions.remove(Questions.size() - 1);


            SecondsPassed = 0;


        }

        if (CurrentQuestion != -1 && e.getAuthor().getId().equals(userID) && !e.getAuthor().isBot()) {
            if (CurrentQuestion < Questions.size()) {
                EmbedBuilder Question = new EmbedBuilder();
                if (CurrentQuestion < Questions.size() - 1) {
                    Question.setTitle("Question " + (CurrentQuestion + 1) + "/" + (Questions.size() - 1));
                }
                Question.setDescription(Questions.get(CurrentQuestion));
                Question.setFooter(Bot.WaterMark, Bot.Logo);
                Question.setColor(Color);
                e.getChannel().sendMessage(Question.build()).queue();
                SecondsPassed = 0;
                CurrentQuestion++;
                responses.add(e.getMessage().getContentRaw());

            } else if (CurrentQuestion == Questions.size()) {
                SecondsPassed = 900000000;

                EmbedBuilder QuestionFinished = new EmbedBuilder();
                QuestionFinished.setDescription("**Application Submitted**");
                QuestionFinished.setColor(Color);
                e.getChannel().sendMessage(QuestionFinished.build()).queue();
                responses.add(e.getMessage().getContentRaw());


                String ResponsesStringLong = "";
                for (int k = 0; k < responses.size(); k++) {
                    ResponsesStringLong = ResponsesStringLong + responses.get(k);
                }
                EmbedBuilder Embed = new EmbedBuilder();
                EmbedBuilder EmbedBIG = new EmbedBuilder();
                boolean isBig = false;

                if (ResponsesStringLong.length() > 1000) {
                    EmbedBIG.setColor(Color);
                    isBig = true;
                    for (int i = (responses.size() / 2) - 1; i < responses.size() - 1; i++) {

                        EmbedBIG.addField(Questions.get(i), responses.get(i), true);
                    }
                    for (int i = 0; i < (responses.size() - responses.size() / 2) - 1; i++) {

                        Embed.addField(Questions.get(i), responses.get(i), true);
                    }
                    EmbedBIG.setTimestamp(LocalTime);
                    EmbedBIG.setFooter(Bot.WaterMark, Bot.Logo);
                } else {
                    for (int i = 0; i < responses.size() - 1; i++) {
                        Embed.addField(Questions.get(i), responses.get(i), true);
                    }
                }

                Embed.setColor(Color);
                Embed.setThumbnail(applicant.getAvatarUrl());
                Embed.setTimestamp(LocalTime);
                Embed.setFooter(Bot.WaterMark, Bot.Logo);
                final String Month1 = Bot.now.getMonth().toString().toLowerCase();
                final String Monthfirst = Month1.substring(0, 1).toUpperCase();
                final String Month2 = Monthfirst + Month1.substring(1);
                final String Week1 = Bot.now.getDayOfWeek().toString().toLowerCase();
                final String Weekfirst = Week1.substring(0, 1).toUpperCase();
                final String Week2 = Weekfirst + Week1.substring(1);
                Embed.setDescription("**Applicant: **" + this.applicant.getAsTag() + "\n**Time Submitted:** " + Week2 + ", " + Month2 + " " + Bot.LocalTime.getDayOfMonth() + ", " + Bot.LocalTime.getYear());
                if (isBig) {
                    e.getChannel().sendMessage(Embed.build()).queue();
                    e.getChannel().sendMessage(EmbedBIG.build()).queue();
                    Message INFO2 = e.getJDA().getGuildById(Bot.GuildID).getTextChannelById(Bot.ApplicationChannelID).sendMessage(Embed.build()).complete();
                    Message INFO23 = e.getJDA().getGuildById(Bot.GuildID).getTextChannelById(Bot.ApplicationChannelID).sendMessage(EmbedBIG.build()).complete();
                } else {
                    e.getChannel().sendMessage(Embed.build()).queue();
                    Message INFO2 = e.getJDA().getGuildById(Bot.GuildID).getTextChannelById(Bot.ApplicationChannelID).sendMessage(Embed.build()).complete();
                }
                EmbedBuilder EmbedAccept = new EmbedBuilder()
                        .setTitle("Application How-To")
                        .addField("To **Accept** this application, please type: ", "`" + Bot.BotPrefix + "accept " + applicant.getAsMention() + " [@Role-To-Give]`", false)
                        .addField("To **Deny** this application, please type:  ", "`" + Bot.BotPrefix + "deny " + applicant.getAsMention() + " [Reason]`", false)
                        .setColor(Color)
                        .setTimestamp(Bot.now)
                        .setFooter(Bot.WaterMark, Bot.Logo);
                Message INFO = e.getJDA().getGuildById(Bot.GuildID).getTextChannelById(Bot.ApplicationChannelID).sendMessage(EmbedAccept.build()).complete();
                e.getJDA().getGuildById(Bot.GuildID).addRoleToMember(applicantMember, e.getJDA().getGuildById(Bot.GuildID).getRoleById(Bot.PendingRoleID)).queue();


                ArrayList<ArrayList<String>> ApplicationsList = new ArrayList<>();
                ArrayList<String> ApplicationInfo = new ArrayList<>();

                if (new File("Applications.json").exists()) {
                    try {
                        JSONObject json = (JSONObject) new JSONParser().parse(new FileReader(new File("Applications.json")));
                        ApplicationsList = (ArrayList<ArrayList<String>>) json.get("ApplicationsList");
                    } catch (Exception ee) {
                        ee.printStackTrace();
                    }
                    ApplicationInfo.add(userID);
                    for (int k = 0; k < responses.size()-1; k++) {
                        ApplicationInfo.add(responses.get(k));
                    }
                    ApplicationsList.add(ApplicationInfo);


                    if (new File("Applications.json").exists()) {
                        try {

                            JSONObject all = new JSONObject();
                            all.put("ApplicationsList", ApplicationsList);

                            try {
                                Files.write(Paths.get("Applications.json"), all.toJSONString().getBytes());
                            } catch (Exception ef) {
                                ef.printStackTrace();
                            }
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }

                    ApplicationInfo.clear();
                    ApplicationsList.clear();
                    Questions.remove(Questions.size() - 1);
                    responses.clear();
                    applicant = null;
                    CurrentQuestion = -1;

                }

            }


        }
    }
}