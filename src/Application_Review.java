
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net  .dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

@SuppressWarnings("unused")
public class Application_Review extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {

        Color Color = java.awt.Color.decode(Bot.ColorHexCode);
        MessageChannel channel = e.getChannel();
        String[] message = e.getMessage().getContentRaw().split(" ");

        boolean isAllowed = false;
        for (int i = 0; i < Bot.AllowedRoles.size(); i++) {
            if (e.getMember().getRoles().contains(e.getGuild().getRoleById(Bot.AllowedRoles.get(i))) || e.getMember().getPermissions().contains(Permission.ADMINISTRATOR)) {
                isAllowed = true;
            }
        }

        if (message.length == 1 && message[0].equalsIgnoreCase(Bot.BotPrefix + "review")) {
            if (isAllowed) {
                EmbedBuilder EmbedRules = new EmbedBuilder();
                EmbedRules.setTitle("Incorrect Format");
                EmbedRules.setColor(Color);
                EmbedRules.addField("Format:", "" + Bot.BotPrefix + "review [@User]", false);
                EmbedRules.setTimestamp(Bot.now);
                EmbedRules.setFooter(Bot.WaterMark, Bot.Logo);
                channel.sendMessage(EmbedRules.build()).queue(message1 -> {
                    e.getMessage().delete().queue();
                });
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

        if (message.length == 2 && message[0].equalsIgnoreCase(Bot.BotPrefix + "review")) {
            if (isAllowed) {
                User User = e.getMessage().getMentionedUsers().get(0);
                Member Member = e.getMessage().getMentionedMembers().get(0);


                ArrayList<ArrayList<String>> ApplicationsList = new ArrayList<>();
                ArrayList<String> ApplicationInfo = new ArrayList<>();

                String StringQuestion1 = "";
                String StringQuestion2 = "";
                String StringQuestion3 = "";
                String StringQuestion4 = "";
                String StringQuestion5 = "";
                String StringQuestion6 = "";
                String StringQuestion7 = "";
                String StringQuestion8 = "";
                String StringQuestion9 = "";
                String StringQuestion10 = "";
                String StringQuestion11 = "";
                String StringQuestion12 = "";
                String StringQuestion13 = "";
                String StringQuestion14 = "";
                String StringQuestion15 = "";
                String StringQuestion16 = "";

                if (new File("Applications.json").exists()) {
                    try {
                        JSONObject json = (JSONObject) new JSONParser().parse(new FileReader(new File("Applications.json")));
                        ApplicationsList = (ArrayList<ArrayList<String>>) json.get("ApplicationsList");
                    } catch (Exception ee) {
                        ee.printStackTrace();
                    }
                    String ApplicantID = "";
                    String part1Answers = "";
                    for (int i = 0; i < ApplicationsList.size(); i++) {
                        if (ApplicationsList.get(i).get(0).equals(Member.getId())) {
                            ApplicantID = ApplicationsList.get(i).get(0);
                            StringQuestion1 = ApplicationsList.get(i).get(1);
                            StringQuestion2 = ApplicationsList.get(i).get(2);
                            StringQuestion3 = ApplicationsList.get(i).get(3);
                            StringQuestion4 = ApplicationsList.get(i).get(4);
                            StringQuestion5 = ApplicationsList.get(i).get(5);
                            StringQuestion6 = ApplicationsList.get(i).get(6);
                            StringQuestion7 = ApplicationsList.get(i).get(7);
                            StringQuestion8 = ApplicationsList.get(i).get(8);
                            StringQuestion9 = ApplicationsList.get(i).get(9);
                            StringQuestion10 = ApplicationsList.get(i).get(10);
                            StringQuestion11 = ApplicationsList.get(i).get(10);
                            StringQuestion12 = ApplicationsList.get(i).get(11);
                            StringQuestion13 = ApplicationsList.get(i).get(12);
                            StringQuestion14 = ApplicationsList.get(i).get(13);
                            StringQuestion15 = ApplicationsList.get(i).get(14);
                            StringQuestion16 = ApplicationsList.get(i).get(15);

                        }
                    }


                    final EmbedBuilder CompletedResponses1 = new EmbedBuilder();
                    final EmbedBuilder CompletedResponses3 = new EmbedBuilder();
                    CompletedResponses1.setTitle("New Application from: " + e.getGuild().getMemberById(ApplicantID).getUser().getAsTag());
                    CompletedResponses1.setColor(Color);
                    CompletedResponses3.setColor(Color);
                    CompletedResponses1.setThumbnail(e.getGuild().getMemberById(ApplicantID).getUser().getAvatarUrl());
                    CompletedResponses1.addField("Discord: ", StringQuestion1, true);
                    CompletedResponses1.addField("IGN: ", StringQuestion2, true);
                    CompletedResponses1.addField("Timezone: ", StringQuestion3, true);
                    CompletedResponses1.addField("Hours per Day:", StringQuestion5, true);
                    CompletedResponses1.addField("Schematica: ", StringQuestion6, true);
                    CompletedResponses1.addField("Breadcrumbs: ", StringQuestion7, true);
                    CompletedResponses1.addField("Willing to ss:", StringQuestion12, true);
                    CompletedResponses1.addField("Can Water Cannon:", StringQuestion14, true);
                    CompletedResponses1.addField("Cannoning Skills", StringQuestion9, true);
                    CompletedResponses1.addField("PvP Skills", StringQuestion10, true);
                    CompletedResponses1.addField("What to do when getting raided A,B,C", StringQuestion16, true);

                    CompletedResponses3.addField("Previous Factions:", StringQuestion4, true);
                    CompletedResponses3.addField("Faction vouches:", StringQuestion15, true);
                    CompletedResponses3.addField("Main Skill-Set", StringQuestion8, true);
                    CompletedResponses3.addField("Explanation on how to patch:", StringQuestion11, true);
                    CompletedResponses3.addField("How Would Contribute", StringQuestion13, true);
                    final String Month1 = Bot.now.getMonth().toString().toLowerCase();
                    final String Monthfirst = Month1.substring(0, 1).toUpperCase();
                    final String Month2 = Monthfirst + Month1.substring(1);
                    final String Week1 = Bot.now.getDayOfWeek().toString().toLowerCase();
                    final String Weekfirst = Week1.substring(0, 1).toUpperCase();
                    final String Week2 = Weekfirst + Week1.substring(1);
                    CompletedResponses3.setFooter(Bot.WaterMark, Bot.Logo);

                    CompletedResponses1.setDescription("**Applicant: **" + e.getGuild().getMemberById(ApplicantID).getUser().getAsTag() + "\n**Time Submitted:** " + Week2 + ", " + Month2 + " " + Bot.LocalTime.getDayOfMonth() + ", " + Bot.LocalTime.getYear());
                    e.getChannel().sendMessage(CompletedResponses1.build()).queue();
                    e.getChannel().sendMessage(CompletedResponses3.build()).queue();
                    EmbedBuilder EmbedAccept = new EmbedBuilder()
                            .setTitle("Application How-To")
                            .addField("To **Accept** this application, please type: ", "`" + Bot.BotPrefix + "accept " + e.getGuild().getMemberById(ApplicantID).getUser().getAsMention() + " [@Role-To-Give]`", false)
                            .addField("To **Deny** this application, please type:  ", "`" + Bot.BotPrefix + "deny " + e.getGuild().getMemberById(ApplicantID).getUser().getAsMention() + " [Reason]`", false)
                            .setColor(Color)
                            .setTimestamp(Bot.now)
                            .setFooter(Bot.WaterMark, Bot.Logo);

                    e.getChannel().sendMessage(EmbedAccept.build()).queue();

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

    }
}
