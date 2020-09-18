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
public class TEST_Polls extends ListenerAdapter {
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
    public TextChannel PollChannel;
    public String userID;
    public ArrayList<String> EmojiInOrder = new ArrayList<>();
    public ArrayList<String> OptionInOrder = new ArrayList<>();
    public ArrayList<String> responses = new ArrayList<>();
    public ArrayList<String> Questions = new ArrayList<>();
    public ArrayList<Emote> EmojiORDER = new ArrayList<>();
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

        } catch (Exception ee) {
            ee.printStackTrace();
        }
        Color Color = java.awt.Color.decode(Bot.ColorHexCode);
        MessageChannel channel = e.getChannel();
        String[] message = e.getMessage().getContentRaw().split(" ");

        boolean isAllowed = false;
        for (int i = 0; i < Bot.AllowedRoles.size(); i++) {
            if (e.getMember().getRoles().contains(e.getGuild().getRoleById(Bot.AllowedRoles.get(i))) || e.getMember().getPermissions().contains(Permission.ADMINISTRATOR)) {
                isAllowed = true;
            }
        }
        if (message.length == 1 && message[0].equalsIgnoreCase(Bot.BotPrefix + "CancelPolls") && !e.getAuthor().isBot()) {
            if (isAllowed) {
                if (CurrentQuestion == -1) {
                    EmbedBuilder EmbedRules = new EmbedBuilder();
                    EmbedRules.setTitle("Poll-Creator");
                    EmbedRules.setColor(Color);
                    EmbedRules.addField("Error:", e.getAuthor().getAsTag() + ", There is currently no one creating a poll.", false);
                    EmbedRules.setTimestamp(Bot.now);
                    EmbedRules.setFooter(Bot.WaterMark, Bot.Logo);
                    channel.sendMessage(EmbedRules.build()).queue(message1 -> {
                        e.getMessage().delete().queue();
                    });
                } else {
                    EmbedBuilder EmbedRules = new EmbedBuilder();
                    EmbedRules.setTitle("Poll-Creator");
                    EmbedRules.setColor(Color);
                    EmbedRules.addField("Success:", e.getAuthor().getAsTag() + ", You have canceled all current polls", false);
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

        if (message.length == 1 && message[0].equalsIgnoreCase(Bot.BotPrefix + "poll") && !e.getAuthor().isBot()) {
            if (CurrentQuestion == -1 || CurrentQuestion == 0) {
                if (isAllowed) {
                    EmbedBuilder EmbedRules = new EmbedBuilder();
                    EmbedRules.setTitle("Incorrect Format");
                    EmbedRules.setColor(Color);
                    EmbedRules.addField("Format:", "" + Bot.BotPrefix + "poll [Number Of Options]", false);
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

            } else {
                final EmbedBuilder EmbedRules = new EmbedBuilder();
                this.userID = e.getAuthor().getId();
                EmbedRules.setTitle("On-Going Poll-Creator");
                EmbedRules.setDescription("**Hey " + e.getMember().getUser().getAsTag() + ", Someone is already creating a poll, Their poll" +
                        " will expire in `" + (300 - SecondsPassed) + "` seconds, if they do not answer a question within that time.** \nYou can use .cancelPolls if you wish to stop all polls being created");
                EmbedRules.setColor(Color);
                EmbedRules.setTimestamp(Bot.now);
                EmbedRules.setFooter(Bot.WaterMark, Bot.Logo);
                channel.sendMessage(EmbedRules.build()).queue(message1 -> e.getMessage().delete().queueAfter(3L, TimeUnit.SECONDS));
            }
        }
        if (message.length == 2 && message[0].equalsIgnoreCase(Bot.BotPrefix + "poll") && !e.getAuthor().isBot()) {
            if (CurrentQuestion == -1 || CurrentQuestion == 0) {
                this.applicant = e.getMember().getUser();
                this.applicantMember = e.getMember();
                final EmbedBuilder EmbedRules = new EmbedBuilder();
                this.userID = e.getAuthor().getId();
                EmbedRules.setTitle("Poll-Creator");
                EmbedRules.setDescription("Hey **" + e.getMember().getUser().getAsTag() + "**, Please look at your Direct messages to fill out the poll!");
                EmbedRules.setColor(Color);
                EmbedRules.setTimestamp(Bot.now);
                EmbedRules.setFooter(Bot.WaterMark, Bot.Logo);
                channel.sendMessage(EmbedRules.build()).queue(message1 -> e.getMessage().delete().queueAfter(3, TimeUnit.SECONDS));
                this.DMS = e.getMember().getUser().openPrivateChannel().complete();
                final EmbedBuilder DMSHeader = new EmbedBuilder();
                DMSHeader.setDescription("**Poll Wizard Started!** \n\n Type **.cancel** at any time to stop the poll creator");
                DMSHeader.setColor(java.awt.Color.GREEN);
                DMSHeader.setFooter(Bot.WaterMark, Bot.Logo);
                this.DMS.sendMessage(DMSHeader.build()).queue();
                userID = e.getAuthor().getId();
                Questions.add("Please mention the channel you wish to create the poll in. `<#ChannelID>`");
                Questions.add("What is the poll about or the general description?");
                for (int i = 1; i < (Integer.parseInt(message[1])) + 1; i++) {
                    String FormattedI = "Extra, Beyond set formatting";
                    switch (i) {
                        case 1:
                            FormattedI = "First";
                            break;
                        case 2:
                            FormattedI = "Second";
                            break;
                        case 3:
                            FormattedI = "Third";
                            break;
                        case 4:
                            FormattedI = "Fourth";
                            break;
                        case 5:
                            FormattedI = "Fifth";
                            break;
                        case 6:
                            FormattedI = "Sixth";
                            break;
                        case 7:
                            FormattedI = "Seventh";
                            break;
                        case 8:
                            FormattedI = "Eighth";
                            break;
                    }
                    Questions.add("What is the `" + FormattedI + "` option in the poll");
                    Questions.add("Enter the emoji you want to use for the `" + FormattedI + "` poll");
                }
                Questions.add("Type **Submit** to complete your poll");

                final EmbedBuilder Question1 = new EmbedBuilder();
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

                        while (SecondsPassed > (300) && SecondsPassed < 900000000) {
                            SecondsPassed = 0;
                            CurrentQuestion = -1;
                            EmbedBuilder ember = new EmbedBuilder()
                                    .setTitle("Poll-Creator")
                                    .setDescription("Your current poll-creation has timed out!, after `" + 300 + "` seconds of inactivity.")
                                    .setFooter(Bot.WaterMark, Bot.Logo)
                                    .setColor(Color);
                            Bot.jda.getUserById(applicant.getId()).openPrivateChannel().complete().sendMessage(ember.build()).queue();
                            myTimer.cancel();
                            Questions.clear();
                            responses.clear();
                            applicant = null;
                            CurrentQuestion = -1;
                            Questions.clear();
                            EmojiORDER.clear();

                        }
                        while (SecondsPassed >= 900000000) {
                            SecondsPassed = 0;
                            CurrentQuestion = -1;
                            myTimer.cancel();
                            responses.clear();
                            Questions.clear();
                            responses.clear();
                            applicant = null;
                            CurrentQuestion = -1;
                            Questions.clear();
                            EmojiORDER.clear();
                        }
                    }
                };
                myTimer.scheduleAtFixedRate(task, 0, 1000);


            } else {
                final EmbedBuilder EmbedRules = new EmbedBuilder();
                this.userID = e.getAuthor().getId();
                EmbedRules.setTitle("On-Going Poll-Creator");
                EmbedRules.setDescription("**Hey " + e.getMember().getUser().getAsTag() + ", Someone is already creating a poll, Their poll" +
                        " will expire in `" + (300 - SecondsPassed) + "` seconds, if they do not answer a question within that time.** \nYou can use .cancelPolls if you wish to stop all polls being created");
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
            EmbedRules.setTitle("Poll-Creator");
            EmbedRules.setDescription("**You canceled your current Poll-Creator Session**");
            EmbedRules.setColor(Color);
            EmbedRules.setTimestamp(Bot.now);
            EmbedRules.setFooter(Bot.WaterMark);
            e.getChannel().sendMessage(EmbedRules.build()).queue();
            this.CurrentQuestion = -1;
            this.responses.clear();
            SecondsPassed = 0;
            responses.clear();
            applicant = null;
            CurrentQuestion = -1;
            Questions.clear();
            EmojiORDER.clear();
        }

        if (CurrentQuestion != -1 && e.getAuthor().getId().equals(applicant.getId()) && !e.getAuthor().isBot()) {
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
                if (e.getMessage().getMentionedChannels().size() > 0 && CurrentQuestion == 2) {
                    PollChannel = e.getMessage().getMentionedChannels().get(0);
                }
                if (e.getMessage().getEmotes().size() > 0) {
                    EmojiORDER.add(e.getMessage().getEmotes().get(0));
                }

            } else if (CurrentQuestion == Questions.size()) {
                SecondsPassed = 900000000;

                EmbedBuilder QuestionFinished = new EmbedBuilder();
                QuestionFinished.setDescription("**Poll Created!** in the " + responses.get(0) + " Channel!!");
                QuestionFinished.setColor(Color);
                e.getChannel().sendMessage(QuestionFinished.build()).queue();
                responses.add(e.getMessage().getContentRaw());

                EmbedBuilder Embed = new EmbedBuilder();
                String Description = "";
                Description = Description + responses.get(1);
                Description = Description + "";
                Description = Description + "\n\n";

                for (int i = 2; i < responses.size(); i = i + 2) {
                    OptionInOrder.add(responses.get(i));
                }
                for (int i = 3; i < responses.size(); i = i + 2) {
                    EmojiInOrder.add(responses.get(i));
                }
                for (int d = 0; d < EmojiInOrder.size(); d++) {
                    Description = Description + EmojiInOrder.get(d) + " » **" + OptionInOrder.get(d) + "**";
                    Description = Description + "\n";
                }
                Description = Description + "\n";

                Embed.setTitle("Incoming Poll!")
                        .setColor(Color)
                        .setThumbnail(BotLogo)
                        .setTimestamp(LocalTime)
                        .setFooter(Bot.WaterMark, BotLogo)
                        .setDescription(Description);
                Message Poll = null;
                Message Pol2 = e.getChannel().sendMessage(Embed.build()).complete();
                try {
                    Poll = e.getJDA().getGuildById(PollChannel.getGuild().getId()).getTextChannelById(PollChannel.getId()).sendMessage(Embed.build()).complete();
                } catch (Exception fe) {
                    fe.printStackTrace();
                }

                for (int i = 0; i < EmojiInOrder.size(); i++) {
                    String Emoji = EmojiInOrder.get(i);
                    if (Emoji.contains("<") && Emoji.contains(">")) {
                        Emoji = Emoji.substring(0, (Emoji.length() - 1));
                    } else {
                        if (Emoji.contains(":")) {
                            Emoji = Emoji.substring(1, Emoji.length() - 1);

                        } else {
                            Emoji = EmojiInOrder.get(i);
                        }
                    }
                    Pol2.addReaction(Emoji).queue();
                    Poll.addReaction(Emoji).queue();
                }

                responses.clear();
                applicant = null;
                CurrentQuestion = -1;
                Questions.clear();
                EmojiInOrder.clear();
                EmojiORDER.clear();
                OptionInOrder.clear();
            }

        }
    }
}
