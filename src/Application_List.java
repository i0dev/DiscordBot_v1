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
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
public class Application_List extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {

        Color Color = java.awt.Color.decode(Bot.ColorHexCode);
        MessageChannel channel = e.getChannel();
        String[] message = e.getMessage().getContentRaw().split(" ");
        if (message.length > 0 && message[0].equalsIgnoreCase(Bot.BotPrefix + "CurrentApps")) {
            ArrayList<String> BlacklistedGet = new ArrayList<>(); if (new File("BlacklistedUsers.json").exists()) { try { JSONObject json = (JSONObject) new JSONParser().parse(new FileReader(new File("BlacklistedUsers.json"))); BlacklistedGet = (ArrayList<String>) json.get("BlacklistedUsers"); } catch (Exception ee) { ee.printStackTrace(); } } boolean isBlacklisted = false; for (String s : BlacklistedGet) { if (e.getAuthor().getId().equals(s)) { isBlacklisted = true; } } if (isBlacklisted) { EmbedBuilder UserBlacklisted = new EmbedBuilder() .setTitle("Error") .setThumbnail(Bot.BotLogo) .setFooter(Bot.WaterMark, Bot.BotLogo) .setTimestamp(Bot.now) .setColor(Color.RED) .setDescription("**" + e.getAuthor().getAsTag() + "**, *You are blacklisted from using all commands, \n" + "If you think this is an error please contact a staff member!*"); e.getChannel().sendMessage(UserBlacklisted.build()).queue(message3 -> { e.getMessage().delete().queue(); message3.addReaction("❌").queue(); message3.delete().queueAfter(10, TimeUnit.SECONDS); }); return; }
        }
        boolean isAllowed = false;
        for (int i = 0; i < Bot.AllowedRoles.size(); i++) {
            if (e.getMember().getRoles().contains(e.getGuild().getRoleById(Bot.AllowedRoles.get(i))) || e.getMember().getPermissions().contains(Permission.ADMINISTRATOR)) {
                isAllowed = true;
            }
        }

        if (message.length == 1 && message[0].equalsIgnoreCase(Bot.BotPrefix + "CurrentApps")) {
            if (isAllowed) {
                EmbedBuilder EmbedRules = new EmbedBuilder();
                EmbedRules.setTitle("Pending Applications");
                EmbedRules.setColor(Color);

                ArrayList<ArrayList<String>> ApplicationsList = new ArrayList<>();
                ArrayList<String> ApplicationInfo = new ArrayList<>();
                if (new File("Applications.json").exists()) {
                    try {
                        JSONObject json = (JSONObject) new JSONParser().parse(new FileReader(new File("Applications.json")));
                        ApplicationsList = (ArrayList<ArrayList<String>>) json.get("ApplicationsList");
                    } catch (Exception ee) {
                        ee.printStackTrace();
                    }
                    if (ApplicationsList.size() < 1) {
                        EmbedRules.addField("Error", "There are currently `0` pending applications!", false);
                        EmbedRules.setTimestamp(Bot.now);
                        EmbedRules.setFooter(Bot.WaterMark, Bot.Logo);
                        channel.sendMessage(EmbedRules.build()).queue(message1 -> {
                            e.getMessage().delete().queue();
                        });
                    } else {

                        for (int i = 0; i < ApplicationsList.size(); i++) {
                            EmbedRules.addField(e.getGuild().getMemberById(ApplicationsList.get(i).get(0)).getUser().getAsTag(), "To view this app, type `" + Bot.BotPrefix + "review " + e.getGuild().getMemberById(ApplicationsList.get(i).get(0)).getUser().getAsMention() + "`", false);
                        }


                        EmbedRules.setTimestamp(Bot.now);
                        EmbedRules.setFooter(Bot.WaterMark, Bot.Logo);
                        channel.sendMessage(EmbedRules.build()).queue(message1 -> {
                            e.getMessage().delete().queue();
                        });
                    }

                } else {
                    EmbedBuilder EmbedNoPerm = new EmbedBuilder()
                            .setTitle("Insufficient Permissions")
                            .setColor(Color)
                            .addField("Error:", "You do not have permission to run this command!", false)
                            .setTimestamp(Bot.now)
                            .setFooter(Bot.WaterMark, Bot.Logo);
                    channel.sendMessage(EmbedNoPerm.build()).queue(message1 -> {
                        e.getMessage().delete().queue();
                    });
                }
            }

        }
    }
}