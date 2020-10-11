
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
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
public class Application_Review extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {

        Color Color = java.awt.Color.decode(Bot.ColorHexCode);
        MessageChannel channel = e.getChannel();
        String[] message = e.getMessage().getContentRaw().split(" ");
        if (message.length > 0 && message[0].equalsIgnoreCase(Bot.BotPrefix + "review")) {
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
                if (new File("Applications.json").exists()) {
                    try {
                        JSONObject json = (JSONObject) new JSONParser().parse(new FileReader(new File("Applications.json")));
                        ApplicationsList = (ArrayList<ArrayList<String>>) json.get("ApplicationsList");
                    } catch (Exception ee) {
                        ee.printStackTrace();
                    }
                }
                for (int j = 0; j < ApplicationsList.size(); j++) {
                    if (ApplicationsList.get(j).get(0).equals(Member.getId())) {
                        ArrayList<String> responses = ApplicationsList.get(j);
                        ArrayList<String> Questions = Bot.ApplicationQuestions;
                        ZonedDateTime LocalTime = Bot.now;
                        String ResponsesStringLong = "";
                        User applicant = e.getGuild().getMemberById(responses.get(0)).getUser();
                        responses.remove(0);

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
                        Embed.setDescription("**Applicant: **" + applicant.getAsTag() + "\n**Time Submitted:** " + Week2 + ", " + Month2 + " " + Bot.LocalTime.getDayOfMonth() + ", " + Bot.LocalTime.getYear());
                        if (isBig) {
                            e.getChannel().sendMessage(Embed.build()).queue();
                            e.getChannel().sendMessage(EmbedBIG.build()).queue();
                        } else {
                            e.getChannel().sendMessage(Embed.build()).complete();
                        }
                        EmbedBuilder EmbedAccept = new EmbedBuilder()
                                .setTitle("Application How-To")
                                .addField("To **Accept** this application, please type: ", "`" + Bot.BotPrefix + "accept " + applicant.getAsMention() + " [@Role-To-Give]`", false)
                                .addField("To **Deny** this application, please type:  ", "`" + Bot.BotPrefix + "deny " + applicant.getAsMention() + " [Reason]`", false)
                                .setColor(Color)
                                .setTimestamp(Bot.now)
                                .setFooter(Bot.WaterMark, Bot.Logo);
                        Message INFO = e.getJDA().getGuildById(Bot.GuildID).getTextChannelById(Bot.ApplicationChannelID).sendMessage(EmbedAccept.build()).complete();
                        return;
                    }
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
