import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")

public class Command_WhoIs extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        Color Color = java.awt.Color.decode(Bot.ColorHexCode);

        User author = e.getAuthor();
        Message msg = e.getMessage();
        MessageChannel channel = e.getChannel();
        String[] message = e.getMessage().getContentRaw().split(" ");
        if (message.length > 0 && message[0].equalsIgnoreCase(Bot.BotPrefix + "whois")) {
            ArrayList<String> BlacklistedGet = new ArrayList<>(); if (new File("BlacklistedUsers.json").exists()) { try { JSONObject json = (JSONObject) new JSONParser().parse(new FileReader(new File("BlacklistedUsers.json"))); BlacklistedGet = (ArrayList<String>) json.get("BlacklistedUsers"); } catch (Exception ee) { ee.printStackTrace(); } } boolean isBlacklisted = false; for (String s : BlacklistedGet) { if (e.getAuthor().getId().equals(s)) { isBlacklisted = true; } } if (isBlacklisted) { EmbedBuilder UserBlacklisted = new EmbedBuilder() .setTitle("Error") .setThumbnail(Bot.BotLogo) .setFooter(Bot.WaterMark, Bot.BotLogo) .setTimestamp(Bot.now) .setColor(Color.RED) .setDescription("**" + e.getAuthor().getAsTag() + "**, *You are blacklisted from using all commands, \n" + "If you think this is an error please contact a staff member!*"); e.getChannel().sendMessage(UserBlacklisted.build()).queue(message3 -> { e.getMessage().delete().queue(); message3.addReaction("❌").queue(); message3.delete().queueAfter(10, TimeUnit.SECONDS); }); return; }
        }

        if (message.length == 1 && message[0].equalsIgnoreCase(Bot.BotPrefix + "whois")) {
            String CreationDateLong = "" + e.getMember().getTimeCreated();
            String[] Created = CreationDateLong.split("-");
            String Year = Created[0];
            String Month = Created[1];
            String Day = Created[2];
            Day = Created[2].substring(0, 2);

            String CreationDateLong2 = "" + e.getMember().getTimeJoined();
            String[] Created2 = CreationDateLong2.split("-");
            String Year2 = Created2[0];
            String Month2 = Created2[1];
            String Day2 = Created2[2];
            Day2 = Created[2].substring(0, 2);
            EmbedBuilder EmbedHelp = new EmbedBuilder();
            EmbedHelp.setTitle(e.getMember().getUser().getAsTag() + "'s Information:");
            EmbedHelp.setColor(Color);
            EmbedHelp.addField("Joined:", "" + Month2 + " / " + Day2 + " / " + Year2, true);
            EmbedHelp.addField("Registered:", "" + Month + " / " + Day + " / " + Year + "", true);
            String RolesString = " ";

            List<Role> RolesListFull = e.getMember().getRoles();
            String[] Roles = new String[RolesListFull.size()];
            for (int i = 0; i < RolesListFull.size() - 1; i++) {
                Roles[i] = RolesListFull.get(i) + "";
                Roles[i] = Roles[i].substring(Roles[i].length() - 19, Roles[i].length() - 1);
                RolesString = RolesString + " <@&" + Roles[i] + "> ";
            }
            RolesString = RolesString + "@everyone";

            EmbedHelp.addField("Roles:", RolesString + "", false);
            EnumSet<Permission> PermsString = e.getMember().getPermissions();
            EmbedHelp.addField("Perms:", PermsString + "", false);
            EmbedHelp.setFooter("ID: " + e.getMember().getId());

            EmbedHelp.setThumbnail(e.getMember().getUser().getAvatarUrl());
            channel.sendMessage(EmbedHelp.build()).queue(message1 -> {
                e.getMessage().delete().queue();
            });
        }
        if (message.length == 2 && message[0].equalsIgnoreCase(Bot.BotPrefix + "whois")) {
            net.dv8tion.jda.api.entities.Member m = e.getMessage().getMentionedMembers().get(0);

            String CreationDateLong = "" + m.getTimeCreated();
            String[] Created = CreationDateLong.split("-");
            String Year = Created[0];
            String Month = Created[1];
            String Day = Created[2];
            Day = Created[2].substring(0, 2);

            String CreationDateLong2 = "" + m.getTimeJoined();
            String[] Created2 = CreationDateLong2.split("-");
            String Year2 = Created2[0];
            String Month2 = Created2[1];
            String Day2 = Created2[2];
            Day2 = Created[2].substring(0, 2);
            EmbedBuilder EmbedHelp = new EmbedBuilder();
            EmbedHelp.setTitle(m.getUser().getAsTag() + "'s Information:");
            EmbedHelp.setColor(Color);
            EmbedHelp.addField("Joined:", "" + Month2 + " / " + Day2 + " / " + Year2, true);
            EmbedHelp.addField("Registered:", "" + Month + " / " + Day + " / " + Year + "", true);
            String RolesString = " ";

            List<Role> RolesListFull = m.getRoles();
            String[] Roles = new String[RolesListFull.size()];
            for (int i = 0; i < RolesListFull.size() - 1; i++) {
                Roles[i] = RolesListFull.get(i) + "";
                Roles[i] = Roles[i].substring(Roles[i].length() - 19, Roles[i].length() - 1);
                RolesString = RolesString + " <@&" + Roles[i] + "> ";
            }
            RolesString = RolesString + "@everyone";

            EmbedHelp.addField("Roles:", RolesString + "", false);
            EnumSet<Permission> PermsString = m.getPermissions();
            EmbedHelp.addField("Perms:", PermsString + "", false);
            EmbedHelp.setFooter("Player ID: " + m.getId());
            EmbedHelp.setThumbnail(m.getUser().getAvatarUrl());

            channel.sendMessage(EmbedHelp.build()).queue(message1 -> {
                e.getMessage().delete().queue();
            });

        }
    }
}