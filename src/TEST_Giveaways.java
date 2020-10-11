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
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.*;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
public class TEST_Giveaways extends ListenerAdapter {
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
        if (message.length > 0 && message[0].equalsIgnoreCase(Bot.BotPrefix + "gstart")) {
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
        if (message.length == 1 && message[0].equalsIgnoreCase(Bot.BotPrefix + "CancelGiveaways") && !e.getAuthor().isBot()) {
            if (isAllowed) {
                if (CurrentQuestion == -1) {
                    EmbedBuilder EmbedRules = new EmbedBuilder();
                    EmbedRules.setTitle("Giveaway-Creator");
                    EmbedRules.setColor(Color);
                    EmbedRules.addField("Error:", e.getAuthor().getAsTag() + ", There is currently no one creating a giveaway.", false);
                    EmbedRules.setTimestamp(Bot.now);
                    EmbedRules.setFooter(Bot.WaterMark, Bot.Logo);
                    channel.sendMessage(EmbedRules.build()).queue(message1 -> {
                        e.getMessage().delete().queue();
                    });
                } else {
                    EmbedBuilder EmbedRules = new EmbedBuilder();
                    EmbedRules.setTitle("Giveaway-Creator");
                    EmbedRules.setColor(Color);
                    EmbedRules.addField("Success:", e.getAuthor().getAsTag() + ", You have canceled all current giveaways", false);
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

        if (message.length == 1 && message[0].equalsIgnoreCase(Bot.BotPrefix + "gstart") && !e.getAuthor().isBot()) {
            if (!isAllowed) {
                EmbedBuilder EmbedRules = new EmbedBuilder();
                EmbedRules.setTitle("Insufficient Permissions");
                EmbedRules.setColor(Color);
                EmbedRules.addField("Error:", "You do not have permission to run this command!", false);
                EmbedRules.setTimestamp(LocalTime);
                EmbedRules.setFooter(Bot.WaterMark, BotLogo);
                channel.sendMessage(EmbedRules.build()).queue(message1 -> {
                    e.getMessage().delete().queue();
                });
            } else {
                if (CurrentQuestion == -1 || CurrentQuestion == 0) {
                    this.applicant = e.getMember().getUser();
                    applicantMember = e.getMember();
                    EmbedBuilder EmbedRules = new EmbedBuilder();
                    userID = e.getAuthor().getId();
                    EmbedRules.setTitle("Giveaway-Creator");
                    EmbedRules.setDescription("Hey **" + e.getMember().getUser().getAsTag() + "**, Please look at your Direct messages to fill out the giveaway!");
                    EmbedRules.setColor(Color);
                    EmbedRules.setTimestamp(Bot.now);
                    EmbedRules.setFooter(Bot.WaterMark, Bot.Logo);
                    channel.sendMessage(EmbedRules.build()).queue(message1 -> e.getMessage().delete().queueAfter(3, TimeUnit.SECONDS));
                    DMS = e.getMember().getUser().openPrivateChannel().complete();
                    final EmbedBuilder DMSHeader = new EmbedBuilder();
                    DMSHeader.setDescription("**Giveaway Wizard Started!** \n\n Type **.cancel** at any time to stop the giveaway creator");
                    DMSHeader.setColor(java.awt.Color.GREEN);
                    DMSHeader.setFooter(Bot.WaterMark, Bot.Logo);
                    DMS.sendMessage(DMSHeader.build()).queue();
                    userID = e.getAuthor().getId();
                    Questions.add("Please mention the channel you wish to create the giveaway in. `<#ChannelID>`:");
                    Questions.add("Please enter the giveaway reward(s):");
                    Questions.add("Please enter the giveaway time:");
                    Questions.add("Please enter how many winners there will be:");
                    Questions.add("Type **Submit** to complete your giveaway");
                    EmbedBuilder Question1 = new EmbedBuilder();
                    Question1.setTitle("Question " + 1 + "/" + (Questions.size() - 1));
                    Question1.setDescription(Questions.get(0));
                    Question1.setColor(Color);
                    Question1.setFooter(Bot.WaterMark, Bot.Logo);
                    this.applicant = e.getAuthor();
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
                                        .setTitle("Giveaway-Creator")
                                        .setDescription("Your current giveaway-creation has timed out!, after `" + 300 + "` seconds of inactivity.")
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
                    EmbedRules.setTitle("On-Going Giveaway-Creator");
                    EmbedRules.setDescription("**Hey " + e.getMember().getUser().getAsTag() + ", Someone is already creating a giveaway, Their giveaway" +
                            " will expire in `" + (300 - SecondsPassed) + "` seconds, if they do not answer a question within that time.** \nYou can use .cancelGiveaways if you wish to stop all polls being created");
                    EmbedRules.setColor(Color);
                    EmbedRules.setTimestamp(Bot.now);
                    EmbedRules.setFooter(Bot.WaterMark, Bot.Logo);
                    channel.sendMessage(EmbedRules.build()).queue(message1 -> e.getMessage().delete().queueAfter(3L, TimeUnit.SECONDS));
                }
            }
        }
    }

    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent e) {
        Color Color = java.awt.Color.decode(Bot.ColorHexCode);
        if (e.getMessage().getContentRaw().equalsIgnoreCase(Bot.BotPrefix + "cancel") && !e.getAuthor().isBot() && e.getChannel().getUser().equals(this.applicant) && this.CurrentQuestion != -1) {
            EmbedBuilder EmbedRules = new EmbedBuilder();
            userID = e.getAuthor().getId();
            EmbedRules.setTitle("Poll-Creator");
            EmbedRules.setDescription("**You canceled your current Giveaway-Creator Session**");
            EmbedRules.setColor(Color);
            EmbedRules.setTimestamp(Bot.now);
            EmbedRules.setFooter(Bot.WaterMark);
            e.getChannel().sendMessage(EmbedRules.build()).queue();
            CurrentQuestion = -1;
            responses.clear();
            SecondsPassed = 0;
            responses.clear();
            applicant = null;
            CurrentQuestion = -1;
            EmojiORDER.clear();
            Questions.clear();
        }
        if (applicant == null) {
            return;
        }

        if (CurrentQuestion != -1) {
            if (!e.getAuthor().isBot()) {
                if (CurrentQuestion < Questions.size()) {
                    if (e.getAuthor().getId().equals(applicant.getId())) {
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
                    }
                } else if (CurrentQuestion == Questions.size() && CurrentQuestion != -1) {
                    SecondsPassed = 900000000;

                    EmbedBuilder QuestionFinished = new EmbedBuilder();
                    QuestionFinished.setDescription("**Giveaway Created!** in the " + responses.get(0) + " Channel!!");
                    QuestionFinished.setColor(Color);
                    e.getChannel().sendMessage(QuestionFinished.build()).queue();
                    responses.add(e.getMessage().getContentRaw());


                    String ChannelFormatted = responses.get(0);
                    String Rewards = responses.get(1);
                    String TimeLong = responses.get(2);
                    String WinnersAmount = responses.get(3);

                    int TimeSeconds = getTime(TimeLong);
                    long CurrentTimeMilis = System.currentTimeMillis();
                    long EndTimeMilis = CurrentTimeMilis + (TimeSeconds * 1000);
                    Instant EndInstant = Instant.ofEpochMilli(EndTimeMilis);
                    ZonedDateTime FinalDate = ZonedDateTime.ofInstant(EndInstant, ZoneId.systemDefault());

                    EmbedBuilder Embed = new EmbedBuilder()
                            .setTitle(":tada: NEW GIVEAWAY :tada:")
                            .setThumbnail(BotLogo)
                            .setColor(Color)
                            .setDescription("**React with " + "🎉" + " To enter!**")
                            .addField(":gift: Prize:", "`" + Rewards + "`", false)
                            .addField(":alarm_clock: Giveaway Length: ", "`" + TimeLong + "`", false)
                            .addField(":bust_in_silhouette: Hosted by ", applicant.getAsMention(), false)
                            .setTimestamp(FinalDate)
                            .setFooter("Ends at ", BotLogo);
                    Message Poll = null;
                    Message PollTitle = null;
                    try {
                        Poll = e.getJDA().getGuildById(PollChannel.getGuild().getId()).getTextChannelById(PollChannel.getId()).sendMessage(Embed.build()).complete();
                    } catch (Exception fe) {
                        fe.printStackTrace();
                    }
                    Poll.addReaction("🎉").queue();


                    ArrayList<JSONObject> ArrayOfGiveaways = new ArrayList<>();
                    try {
                        JSONObject json = (JSONObject) new JSONParser().parse(new FileReader(new File("Giveaways.json")));
                        ArrayOfGiveaways = (ArrayList<JSONObject>) json.get("Giveaways");
                    } catch (Exception ee) {
                        ee.printStackTrace();
                    }

                    JSONObject CurrentGiveawayObject = new JSONObject();

                    CurrentGiveawayObject.put("ChannelID", PollChannel.getId());
                    CurrentGiveawayObject.put("GuildID", PollChannel.getGuild().getId());
                    CurrentGiveawayObject.put("WinnersAmount", WinnersAmount);
                    CurrentGiveawayObject.put("MessageID", Poll.getId());
                    CurrentGiveawayObject.put("EndTimeMillis", EndTimeMilis);
                    CurrentGiveawayObject.put("EndTimeFormat", TimeLong);
                    try {
                        CurrentGiveawayObject.put("HostID", applicant.getId());
                    } catch (Exception error) {
                        CurrentGiveawayObject.put("HostID", Bot.jda.getSelfUser().getId());

                    }
                    ArrayList<JSONObject> EmptyArray = new ArrayList<>();
                    CurrentGiveawayObject.put("TotalReactedMemberIDS", EmptyArray);
                    CurrentGiveawayObject.put("WinnersID", EmptyArray);

                    CurrentGiveawayObject.put("GiveawayEnded", false);
                    CurrentGiveawayObject.put("Reward", Rewards);
                    CurrentGiveawayObject.put("BotLogo", BotLogo);
                    CurrentGiveawayObject.put("CurrentColorHexCode", ColorHexCode);
                    ArrayOfGiveaways.add(CurrentGiveawayObject);

                    JSONObject all = new JSONObject();
                    all.put("Giveaways", ArrayOfGiveaways);

                    try {
                        Files.write(Paths.get("Giveaways.json"), all.toJSONString().getBytes());
                    } catch (Exception ef) {
                        ef.printStackTrace();
                    }


                    applicant = null;
                    responses.clear();
                    CurrentQuestion = -1;
                    Questions.clear();
                    EmojiInOrder.clear();
                    EmojiORDER.clear();
                    OptionInOrder.clear();
                }

            }
        }
    }

    public static int getTime(String input) {
        input = input.toLowerCase();
        if (input.isEmpty()) return -1;
        int time = 0;
        StringBuilder number = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (isInt(String.valueOf(c))) {
                number.append(c);
                continue;
            }
            if (number.toString().isEmpty()) return -1;
            int add = Integer.parseInt(number.toString());
            switch (c) {
                case 'w':
                    add *= 7;
                case 'd':
                    add *= 24;
                case 'h':
                    add *= 60;
                case 'm':
                    add *= 60;
                case 's':
                    time += add;
                    number.setLength(0);
                    break;
                default:
                    return -1;
            }
        }
        return time;
    }

    public static boolean isInt(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
