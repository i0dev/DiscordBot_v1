import net.dv8tion.jda.api.EmbedBuilder;
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
import java.util.List;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
public class Command_Roles extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        Color Color = java.awt.Color.decode(Bot.ColorHexCode);

        User author = e.getAuthor();
        Message msg = e.getMessage();
        MessageChannel channel = e.getChannel();

        String[] message = e.getMessage().getContentRaw().split(" ");
        if (message.length > 0 && message[0].equalsIgnoreCase(Bot.BotPrefix + "roles")) {
            ArrayList<String> BlacklistedGet = new ArrayList<>(); if (new File("BlacklistedUsers.json").exists()) { try { JSONObject json = (JSONObject) new JSONParser().parse(new FileReader(new File("BlacklistedUsers.json"))); BlacklistedGet = (ArrayList<String>) json.get("BlacklistedUsers"); } catch (Exception ee) { ee.printStackTrace(); } } boolean isBlacklisted = false; for (String s : BlacklistedGet) { if (e.getAuthor().getId().equals(s)) { isBlacklisted = true; } } if (isBlacklisted) { EmbedBuilder UserBlacklisted = new EmbedBuilder() .setTitle("Error") .setThumbnail(Bot.BotLogo) .setFooter(Bot.WaterMark, Bot.BotLogo) .setTimestamp(Bot.now) .setColor(Color.RED) .setDescription("**" + e.getAuthor().getAsTag() + "**, *You are blacklisted from using all commands, \n" + "If you think this is an error please contact a staff member!*"); e.getChannel().sendMessage(UserBlacklisted.build()).queue(message3 -> { e.getMessage().delete().queue(); message3.addReaction("❌").queue(); message3.delete().queueAfter(10, TimeUnit.SECONDS); }); return; }
        }
        String RolesString = " ";
        List<Role> RolesListFull = e.getGuild().getRoles();
        String[] Roles = new String[RolesListFull.size()];
        for (int i = 0; i < RolesListFull.size() - 1; i++) {
            Roles[i] = RolesListFull.get(i) + "";
            Roles[i] = Roles[i].substring(Roles[i].length() - 19, Roles[i].length() - 1);
            RolesString = RolesString + " <@&" + Roles[i] + ">\n";
        }
        RolesString = RolesString + "@everyone";

        if (message.length == 1 && message[0].equalsIgnoreCase(Bot.BotPrefix + "roles")) {
            EmbedBuilder Embed = new EmbedBuilder();

            Embed.setTitle("List of all Roles");
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