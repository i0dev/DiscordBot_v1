
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
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
public class Application_Deny extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {

        Color Color = java.awt.Color.decode(Bot.ColorHexCode);
        MessageChannel channel = e.getChannel();
        String[] message = e.getMessage().getContentRaw().split(" ");
        if (message.length > 0 && message[0].equalsIgnoreCase(Bot.BotPrefix + "deny")) {
            ArrayList<String> BlacklistedGet = new ArrayList<>(); if (new File("BlacklistedUsers.json").exists()) { try { JSONObject json = (JSONObject) new JSONParser().parse(new FileReader(new File("BlacklistedUsers.json"))); BlacklistedGet = (ArrayList<String>) json.get("BlacklistedUsers"); } catch (Exception ee) { ee.printStackTrace(); } } boolean isBlacklisted = false; for (String s : BlacklistedGet) { if (e.getAuthor().getId().equals(s)) { isBlacklisted = true; } } if (isBlacklisted) { EmbedBuilder UserBlacklisted = new EmbedBuilder() .setTitle("Error") .setThumbnail(Bot.BotLogo) .setFooter(Bot.WaterMark, Bot.BotLogo) .setTimestamp(Bot.now) .setColor(Color.RED) .setDescription("**" + e.getAuthor().getAsTag() + "**, *You are blacklisted from using all commands, \n" + "If you think this is an error please contact a staff member!*"); e.getChannel().sendMessage(UserBlacklisted.build()).queue(message3 -> { e.getMessage().delete().queue(); message3.addReaction("❌").queue(); message3.delete().queueAfter(10, TimeUnit.SECONDS); }); return; }
        }
        boolean isAllowed = false;
        for (int i = 0; i < Bot.AllowedRoles.size(); i++) {
            if (e.getMember().getRoles().contains(e.getGuild().getRoleById(Bot.AllowedRoles.get(i))) || e.getMember().getPermissions().contains(Permission.ADMINISTRATOR)) {
                isAllowed = true;
            }
        }

        if (message.length == 1 && message[0].equalsIgnoreCase(Bot.BotPrefix + "deny")) {
            if (isAllowed) {
                EmbedBuilder EmbedRules = new EmbedBuilder();
                EmbedRules.setTitle("Incorrect Format");
                EmbedRules.setColor(Color);
                EmbedRules.addField("Format:", "" + Bot.BotPrefix + "deny [@User] [Reason]", false);
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
        if (message.length == 2 && message[0].equalsIgnoreCase(Bot.BotPrefix + "deny")) {
            if (isAllowed) {
                EmbedBuilder EmbedRules = new EmbedBuilder();
                EmbedRules.setTitle("Incorrect Format");
                EmbedRules.setColor(Color);
                EmbedRules.addField("Format:", "" + Bot.BotPrefix + "deny [@User] [Reason]", false);
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
        if (message.length > 2 && message[0].equalsIgnoreCase(Bot.BotPrefix + "deny")) {
            if (isAllowed) {
                User User = e.getMessage().getMentionedUsers().get(0);
                Member Member = e.getMessage().getMentionedMembers().get(0);

                String reason = "";
                for (int k = 2; k < message.length; k++) {
                    reason = reason + " " + message[k] + " ";
                }

                try {
                    e.getGuild().removeRoleFromMember(e.getGuild().getMemberById(User.getId()), e.getGuild().getRoleById(Bot.PendingRoleID)).queue();
                } catch (Exception gg) {
                    gg.printStackTrace();

                }
                EmbedBuilder EmbedRules = new EmbedBuilder();
                EmbedRules.setTitle("Successfully Rejected");
                EmbedRules.setColor(Color);
                EmbedRules.addField("Success", "**" + e.getMember().getUser().getAsTag() + "**, you have denied **" + e.getGuild().getMemberById(User.getId()).getUser().getAsTag() + "**", false);
                EmbedRules.addField("Reason", reason, false);
                EmbedRules.setTimestamp(Bot.now);
                EmbedRules.setFooter(Bot.WaterMark, Bot.Logo);
                channel.sendMessage(EmbedRules.build()).queue(message1 -> {
                    e.getMessage().delete().queue();
                });

                EmbedBuilder logs = new EmbedBuilder()
                        .setTitle("Deny Logs")
                        .setColor(Color)
                        .addField("Success", "**" + e.getMember().getUser().getAsTag() + "**, has denied **" + e.getGuild().getMemberById(User.getId()).getUser().getAsTag() + "**", false)
                        .addField("Reason", reason, false)
                        .setTimestamp(Bot.now)
                        .setFooter(Bot.WaterMark, Bot.Logo);
                e.getGuild().getTextChannelById(Bot.LogsChannelID).sendMessage(logs.build()).queue(message1 -> {
                    e.getMessage().delete().queue();
                });

                PrivateChannel DMS = e.getGuild().getMemberById(User.getId()).getUser().openPrivateChannel().complete();

                EmbedBuilder DMS1 = new EmbedBuilder()
                        .setTitle("Your application was Denied!")
                        .setColor(Color)
                        .addField(e.getMember().getUser().getAsTag(), ", has **Denied** your application!", false)
                        .addField("Reason", reason, false)
                        .setTimestamp(Bot.now)
                        .setFooter(Bot.WaterMark, Bot.Logo);
                DMS.sendMessage(DMS1.build()).queue();

                ArrayList<ArrayList<String>> ApplicationsList = new ArrayList<>();
                if (new File("Applications.json").exists()) {
                    try {
                        JSONObject json = (JSONObject) new JSONParser().parse(new FileReader(new File("Applications.json")));
                        ApplicationsList = (ArrayList<ArrayList<String>>) json.get("ApplicationsList");
                    } catch (Exception ee) {
                        ee.printStackTrace();
                    }

                    for (int i = 0; i < ApplicationsList.size(); i++) {
                        if (ApplicationsList.get(i).get(0).equals(Member.getId())) {
                            ApplicationsList.remove(i);
                        }
                    }
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
                    ApplicationsList.clear();
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
