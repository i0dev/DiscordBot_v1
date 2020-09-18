import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;
import java.time.temporal.TemporalAccessor;

import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.Color;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class Applications_New extends ListenerAdapter {
    User applicant;
    Member applicantMember;
    PrivateChannel DMS;
    private String userID;
    private int CurrentQuestion;
    private ArrayList<String> responses;

    public int SecondsPassed = 0;


    @Override
    public void onGuildMessageReceived(final GuildMessageReceivedEvent e) {
        final Color Color = java.awt.Color.decode(Bot.ColorHexCode);
        final User author = e.getAuthor();
        final Message msg = e.getMessage();
        final MessageChannel channel = e.getChannel();
        final String[] message = e.getMessage().getContentRaw().split(" ");
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
            if (this.CurrentQuestion == -1) {
                this.applicant = e.getMember().getUser();
                this.applicantMember = e.getMember();
                final EmbedBuilder EmbedRules = new EmbedBuilder();
                this.userID = e.getAuthor().getId();
                EmbedRules.setTitle("Application");
                EmbedRules.setDescription("Hey **" + e.getMember().getUser().getAsTag() + "**, Please look at your Direct messages");
                EmbedRules.setColor(Color);
                EmbedRules.setTimestamp(Bot.now);
                EmbedRules.setFooter(Bot.WaterMark, Bot.Logo);
                channel.sendMessage(EmbedRules.build()).queue(message1 -> e.getMessage().delete().queueAfter(3L, TimeUnit.SECONDS));
                this.DMS = e.getMember().getUser().openPrivateChannel().complete();
                final EmbedBuilder DMSHeader = new EmbedBuilder();
                DMSHeader.setDescription("**Application Started!** \n\n Type **.cancel** at any time to stop the application");
                DMSHeader.setColor(java.awt.Color.GREEN);
                DMSHeader.setFooter(Bot.WaterMark, Bot.Logo);
                this.DMS.sendMessage(DMSHeader.build()).queue();
                final EmbedBuilder Question1 = new EmbedBuilder();
                Question1.setDescription("Discord Tag (Ex: IgnMidget#1300)");
                Question1.setColor(Color);
                Question1.setFooter(Bot.WaterMark);
                this.DMS.sendMessage(Question1.build()).queue();
                this.CurrentQuestion = 1;
                this.responses.clear();
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
                        }
                    }
                };
                myTimer.scheduleAtFixedRate(task, 0, 1000);


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


            SecondsPassed = 0;


        }
        if (e.getMessage().getAuthor().getId().equals(this.userID) && this.CurrentQuestion == 1 && !e.getAuthor().isBot()) {
            final EmbedBuilder Question = new EmbedBuilder();
            Question.setDescription("What is your IGN?");
            Question.setFooter(Bot.WaterMark, Bot.Logo);
            Question.setColor(Color);
            e.getChannel().sendMessage(Question.build()).queue();
            this.CurrentQuestion = 2;
            this.responses.add(e.getMessage().getContentRaw());


            SecondsPassed = 0;


        } else if (e.getMessage().getAuthor().getId().equals(this.userID) && this.CurrentQuestion == 2 && !e.getAuthor().isBot()) {
            final EmbedBuilder Question = new EmbedBuilder();
            Question.setDescription("What is your TimeZone");
            Question.setColor(Color);
            e.getChannel().sendMessage(Question.build()).queue();
            this.CurrentQuestion = 3;
            this.responses.add(e.getMessage().getContentRaw());


            SecondsPassed = 0;


        } else if (e.getMessage().getAuthor().getId().equals(this.userID) && this.CurrentQuestion == 3 && !e.getAuthor().isBot()) {
            final EmbedBuilder Question = new EmbedBuilder();
            Question.setDescription("Old faction(s) and Server");
            Question.setColor(Color);
            e.getChannel().sendMessage(Question.build()).queue();
            this.responses.add(e.getMessage().getContentRaw());
            this.CurrentQuestion = 4;


            SecondsPassed = 0;


        } else if (e.getMessage().getAuthor().getId().equals(this.userID) && this.CurrentQuestion == 4 && !e.getAuthor().isBot()) {
            final EmbedBuilder Question = new EmbedBuilder();
            Question.setDescription("How many hours can you play each day");
            Question.setColor(Color);
            e.getChannel().sendMessage(Question.build()).queue();
            this.responses.add(e.getMessage().getContentRaw());
            this.CurrentQuestion = 5;

            SecondsPassed = 0;


        } else if (e.getMessage().getAuthor().getId().equals(this.userID) && this.CurrentQuestion == 5 && !e.getAuthor().isBot()) {
            final EmbedBuilder Question = new EmbedBuilder();
            Question.setDescription("Do you have schematica and know how to use it? (If not be willing to download)");
            Question.setColor(Color);
            e.getChannel().sendMessage(Question.build()).queue();
            this.responses.add(e.getMessage().getContentRaw());
            this.CurrentQuestion = 6;


            SecondsPassed = 0;


        } else if (e.getMessage().getAuthor().getId().equals(this.userID) && this.CurrentQuestion == 6 && !e.getAuthor().isBot()) {
            final EmbedBuilder Question = new EmbedBuilder();
            Question.setDescription("Do you have Breadcrumbs and know how to use it? (If not be willing to download)");
            Question.setColor(Color);
            e.getChannel().sendMessage(Question.build()).queue();
            this.responses.add(e.getMessage().getContentRaw());
            this.CurrentQuestion = 7;


            SecondsPassed = 0;


        } else if (e.getMessage().getAuthor().getId().equals(this.userID) && this.CurrentQuestion == 7 && !e.getAuthor().isBot()) {
            final EmbedBuilder Question = new EmbedBuilder();
            Question.setDescription("What is your main skill set");
            Question.setColor(Color);
            e.getChannel().sendMessage(Question.build()).queue();
            this.responses.add(e.getMessage().getContentRaw());
            this.CurrentQuestion = 8;


            SecondsPassed = 0;


        } else if (e.getMessage().getAuthor().getId().equals(this.userID) && this.CurrentQuestion == 8 && !e.getAuthor().isBot()) {
            final EmbedBuilder Question = new EmbedBuilder();
            Question.setDescription("Rate your Cannoning skills (0-10)");
            Question.setColor(Color);
            e.getChannel().sendMessage(Question.build()).queue();
            this.responses.add(e.getMessage().getContentRaw());
            this.CurrentQuestion = 9;


            SecondsPassed = 0;


        } else if (e.getMessage().getAuthor().getId().equals(this.userID) && this.CurrentQuestion == 9 && !e.getAuthor().isBot()) {
            final EmbedBuilder Question = new EmbedBuilder();
            Question.setDescription("Rate your PVP skills (0-10)");
            Question.setColor(Color);
            e.getChannel().sendMessage(Question.build()).queue();
            this.responses.add(e.getMessage().getContentRaw());
            this.CurrentQuestion = 10;


            SecondsPassed = 0;


        } else if (e.getMessage().getAuthor().getId().equals(this.userID) && this.CurrentQuestion == 10 && !e.getAuthor().isBot()) {
            final EmbedBuilder Question = new EmbedBuilder();
            Question.setDescription("Explain in detail how to patch an Anti patch cannon and a normal cannon");
            Question.setColor(Color);
            e.getChannel().sendMessage(Question.build()).queue();
            this.responses.add(e.getMessage().getContentRaw());
            this.CurrentQuestion = 11;

            SecondsPassed = 0;


        } else if (e.getMessage().getAuthor().getId().equals(this.userID) && this.CurrentQuestion == 11 && !e.getAuthor().isBot()) {
            final EmbedBuilder Question = new EmbedBuilder();
            Question.setDescription("Are you willing to open anydesk and ss");
            Question.setColor(Color);
            e.getChannel().sendMessage(Question.build()).queue();
            this.responses.add(e.getMessage().getContentRaw());
            this.CurrentQuestion = 12;
            SecondsPassed = 0;


        } else if (e.getMessage().getAuthor().getId().equals(this.userID) && this.CurrentQuestion == 12 && !e.getAuthor().isBot()) {
            final EmbedBuilder Question = new EmbedBuilder();
            Question.setDescription("How would you contribute to the faction");
            Question.setColor(Color);
            e.getChannel().sendMessage(Question.build()).queue();
            this.responses.add(e.getMessage().getContentRaw());
            this.CurrentQuestion = 13;

            SecondsPassed = 0;


        } else if (e.getMessage().getAuthor().getId().equals(this.userID) && this.CurrentQuestion == 13 && !e.getAuthor().isBot()) {
            final EmbedBuilder Question = new EmbedBuilder();
            Question.setDescription("Can you water a cannon");
            Question.setColor(Color);
            e.getChannel().sendMessage(Question.build()).queue();
            this.responses.add(e.getMessage().getContentRaw());
            this.CurrentQuestion = 14;

            SecondsPassed = 0;


        } else if (e.getMessage().getAuthor().getId().equals(this.userID) && this.CurrentQuestion == 14 && !e.getAuthor().isBot()) {
            final EmbedBuilder Question = new EmbedBuilder();
            Question.setDescription("Can anyone in the fac vouch for you");
            Question.setColor(Color);
            e.getChannel().sendMessage(Question.build()).queue();
            this.responses.add(e.getMessage().getContentRaw());
            this.CurrentQuestion = 16;

            SecondsPassed = 0;


        } else if (e.getMessage().getAuthor().getId().equals(this.userID) && this.CurrentQuestion == 16 && !e.getAuthor().isBot()) {
            final EmbedBuilder Question = new EmbedBuilder();
            Question.setDescription("what should you do when getting attempted? \n Please choose one of the options below!\n\n```a. WeeWoo and wait until others get online to patch\nb. WeeWoo and start patching instantly\nc. Don't WeeWoo and just start Patching WeeWoo and Counter```");
            Question.setColor(Color);
            e.getChannel().sendMessage(Question.build()).queue();
            this.responses.add(e.getMessage().getContentRaw());
            this.CurrentQuestion = 17;


            SecondsPassed = 0;


        } else if (e.getMessage().getAuthor().getId().equals(this.userID) && this.CurrentQuestion == 17 && !e.getAuthor().isBot()) {
            final EmbedBuilder Question = new EmbedBuilder();
            Question.setDescription("Type **Submit** to submit application!");
            Question.setColor(Color);
            e.getChannel().sendMessage(Question.build()).queue();
            this.responses.add(e.getMessage().getContentRaw());
            this.CurrentQuestion = 99;


            SecondsPassed = 0;


        } else if (e.getMessage().getAuthor().getId().equals(this.userID) && this.CurrentQuestion == 99 && !e.getAuthor().isBot()) {
            final EmbedBuilder QuestionFinished = new EmbedBuilder();
            QuestionFinished.setDescription("**Review Submitted**");
            QuestionFinished.setColor(Color);
            e.getChannel().sendMessage(QuestionFinished.build()).queue();
            final EmbedBuilder CompletedResponses1 = new EmbedBuilder();
            final EmbedBuilder CompletedResponses2 = new EmbedBuilder();
            final EmbedBuilder CompletedResponses3 = new EmbedBuilder();
            CompletedResponses1.setTitle("New Application from: " + this.applicant.getAsTag());
            CompletedResponses1.setColor(Color);
            CompletedResponses2.setColor(Color);
            CompletedResponses3.setColor(Color);
            CompletedResponses1.setThumbnail(this.applicant.getAvatarUrl());
            CompletedResponses1.addField("Discord: ", this.responses.get(0), true);
            CompletedResponses1.addField("IGN: ", this.responses.get(1), true);
            CompletedResponses1.addField("Timezone: ", this.responses.get(2), true);
            CompletedResponses1.addField("Hours per Day:", this.responses.get(4), true);
            CompletedResponses1.addField("Schematica: ", this.responses.get(5), true);
            CompletedResponses1.addField("Breadcrumbs: ", this.responses.get(6), true);
            CompletedResponses1.addField("Willing to ss:", this.responses.get(11), true);
            CompletedResponses1.addField("Can Water Cannon:", this.responses.get(13), true);
            CompletedResponses1.addField("Cannoning Skills", this.responses.get(8), true);
            CompletedResponses1.addField("PvP Skills", this.responses.get(9), true);
            CompletedResponses1.addField("What to do when getting raided A,B,C", this.responses.get(15), true);


            SecondsPassed = 900000000;


            CompletedResponses3.addField("Previous Factions:", this.responses.get(3), true);
            CompletedResponses3.addField("Faction vouches:", this.responses.get(14), true);
            CompletedResponses3.addField("Main Skill-Set", this.responses.get(7), true);
            CompletedResponses3.addField("Explanation on how to patch:", this.responses.get(10), true);
            CompletedResponses3.addField("How Would Contribute", this.responses.get(12), true);
            final String Month1 = Bot.now.getMonth().toString().toLowerCase();
            final String Monthfirst = Month1.substring(0, 1).toUpperCase();
            final String Month2 = Monthfirst + Month1.substring(1);
            final String Week1 = Bot.now.getDayOfWeek().toString().toLowerCase();
            final String Weekfirst = Week1.substring(0, 1).toUpperCase();
            final String Week2 = Weekfirst + Week1.substring(1);
            CompletedResponses3.setFooter(Bot.WaterMark, Bot.Logo);

            CompletedResponses1.setDescription("**Applicant: **" + this.applicant.getAsTag() + "\n**Time Submitted:** " + Week2 + ", " + Month2 + " " + Bot.LocalTime.getDayOfMonth() + ", " + Bot.LocalTime.getYear());
            e.getChannel().sendMessage(CompletedResponses1.build()).queue();
            e.getChannel().sendMessage(CompletedResponses3.build()).queue();
            final Message reviewC1 = e.getJDA().getGuildById(Bot.GuildID).getTextChannelById(Bot.ApplicationChannelID).sendMessage(CompletedResponses1.build()).complete();
            //  final Message reviewC2 = e.getJDA().getGuildById(Bot.GuildID).getTextChannelById(Bot.ApplicationChannelID).sendMessage(CompletedResponses2.build()).complete();
            final Message reviewC3 = e.getJDA().getGuildById(Bot.GuildID).getTextChannelById(Bot.ApplicationChannelID).sendMessage(CompletedResponses3.build()).complete();
            // reviewC3.addReaction("\u2705").queue();
            //reviewC3.addReaction("\u274c").queue();
            EmbedBuilder EmbedAccept = new EmbedBuilder()
                    .setTitle("Application How-To")
                    .addField("To **Accept** this application, please type: ", "`" + Bot.BotPrefix + "accept " + applicant.getAsMention() + " [@Role-To-Give]`", false)
                    .addField("To **Deny** this application, please type:  ", "`" + Bot.BotPrefix + "deny " + applicant.getAsMention() + " [Reason]`", false)
                    .setColor(Color)
                    .setTimestamp(Bot.now)
                    .setFooter(Bot.WaterMark, Bot.Logo);


            ArrayList<ArrayList<String>> ApplicationsList = new ArrayList<>();
            ArrayList<String> ApplicationInfo = new ArrayList<>();

            if (new File("Applications.json").exists()) {
                try {
                    JSONObject json = (JSONObject) new JSONParser().parse(new FileReader(new File("Applications.json")));
                    ApplicationsList = (ArrayList<ArrayList<String>>) json.get("ApplicationsList");
                } catch (Exception ee) {
                    ee.printStackTrace();
                }

                //0 is Applicant ID
                // 1 is Application answers

                ApplicationInfo.add(applicant.getId());
                ApplicationInfo.add(this.responses.get(0));
                ApplicationInfo.add(this.responses.get(1));
                ApplicationInfo.add(this.responses.get(2));
                ApplicationInfo.add(this.responses.get(3));
                ApplicationInfo.add(this.responses.get(4));
                ApplicationInfo.add(this.responses.get(5));
                ApplicationInfo.add(this.responses.get(6));
                ApplicationInfo.add(this.responses.get(7));
                ApplicationInfo.add(this.responses.get(8));
                ApplicationInfo.add(this.responses.get(9));
                ApplicationInfo.add(this.responses.get(10));
                ApplicationInfo.add(this.responses.get(11));
                ApplicationInfo.add(this.responses.get(12));
                ApplicationInfo.add(this.responses.get(13));
                ApplicationInfo.add(this.responses.get(14));
                ApplicationInfo.add(this.responses.get(15));
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


            }

            final Message INFO = e.getJDA().getGuildById(Bot.GuildID).getTextChannelById(Bot.ApplicationChannelID).sendMessage(EmbedAccept.build()).complete();


            e.getJDA().getGuildById(Bot.GuildID).addRoleToMember(applicantMember, e.getJDA().getGuildById(Bot.GuildID).getRoleById(Bot.PendingRoleID)).queue();
            this.responses.clear();
            this.applicant = null;
            this.CurrentQuestion = -1;
        }
    }
}