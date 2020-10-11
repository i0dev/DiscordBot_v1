import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
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
public class Command_GetMuted extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        Color Color = java.awt.Color.decode(Bot.ColorHexCode);

        User author = e.getAuthor();
        String RolesString = "";
        Message msg = e.getMessage();
        MessageChannel channel = e.getChannel();
        int total1 = 0;

        String[] message = e.getMessage().getContentRaw().split(" ");
        if (message.length > 0 && message[0].equalsIgnoreCase(Bot.BotPrefix + "getmuted")) {
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


        if (message.length == 1 && message[0].equalsIgnoreCase(Bot.BotPrefix + "getMuted")) {
            boolean isAllowed = false;
            for (int i = 0; i < Bot.AllowedRoles.size(); i++) {
                if (e.getMember().getRoles().contains(e.getGuild().getRoleById(Bot.AllowedRoles.get(i))) || e.getMember().getPermissions().contains(Permission.ADMINISTRATOR)) {
                    isAllowed = true;
                }
            }
            for (int i = 0; i < Bot.LightAllowedRoleIDS.size(); i++) {
                if (e.getMember().getRoles().contains(e.getGuild().getRoleById(Bot.LightAllowedRoleIDS.get(i))) || e.getMember().getPermissions().contains(Permission.ADMINISTRATOR)) {
                    isAllowed = true;
                }
            }
            if (!isAllowed) {
                EmbedBuilder EmbedRules = new EmbedBuilder();
                EmbedRules.setTitle("Insufficient Permissions");
                EmbedRules.setColor(Color);
                EmbedRules.addField("Error:", "You do not have permission to run this command!", false);
                EmbedRules.setTimestamp(Bot.now);
                EmbedRules.setFooter(Bot.WaterMark, Bot.Logo);
                e.getChannel().sendMessage(EmbedRules.build()).queue(message1 -> {
                    e.getMessage().delete().queue();
                });
            } else {
                for (int i = 0; i < e.getGuild().getMemberCount(); i++) {
                    if (e.getGuild().getMembers().get(i).getRoles().contains(e.getGuild().getRolesByName("Muted", false).get(0))) {
                        RolesString = RolesString + " " + e.getGuild().getMembers().get(i).getAsMention() + " | "
                                + e.getGuild().getMembers().get(i).getUser().getAsTag() + "\n";
                        total1++;
                    }
                }

                EmbedBuilder Embed = new EmbedBuilder();

                Embed.addField("Total users: ", total1 + "", false);
                Embed.setTitle("List of all Muted Users");
                Embed.setColor(Color);
                Embed.setDescription(RolesString);
                Embed.setTimestamp(Bot.now);
                Embed.setFooter(Bot.WaterMark, Bot.Logo);
                channel.sendMessage(Embed.build()).queue(message1 -> {
                    e.getMessage().delete().queue();

                });
            }
        }

    }
}