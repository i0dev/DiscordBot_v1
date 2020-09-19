import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Invite;
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
import java.util.List;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
public class Command_Invites extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        Color Color = java.awt.Color.decode(Bot.ColorHexCode);

        User author = e.getAuthor();
        Message msg = e.getMessage();
        MessageChannel channel = e.getChannel();
        String[] message = e.getMessage().getContentRaw().split(" ");
        int InvitesTotal = 0;
        String f = e.getMessage().getAuthor().getAsMention();
        if (message.length > 0 && message[0].equalsIgnoreCase(Bot.BotPrefix + "invites")) {
            ArrayList<String> BlacklistedGet = new ArrayList<>(); if (new File("BlacklistedUsers.json").exists()) { try { JSONObject json = (JSONObject) new JSONParser().parse(new FileReader(new File("BlacklistedUsers.json"))); BlacklistedGet = (ArrayList<String>) json.get("BlacklistedUsers"); } catch (Exception ee) { ee.printStackTrace(); } } boolean isBlacklisted = false; for (String s : BlacklistedGet) { if (e.getAuthor().getId().equals(s)) { isBlacklisted = true; } } if (isBlacklisted) { EmbedBuilder UserBlacklisted = new EmbedBuilder() .setTitle("Error") .setThumbnail(Bot.BotLogo) .setFooter(Bot.WaterMark, Bot.BotLogo) .setTimestamp(Bot.now) .setColor(Color.RED) .setDescription("**" + e.getAuthor().getAsTag() + "**, *You are blacklisted from using all commands, \n" + "If you think this is an error please contact a staff member!*"); e.getChannel().sendMessage(UserBlacklisted.build()).queue(message3 -> { e.getMessage().delete().queue(); message3.addReaction("❌").queue(); message3.delete().queueAfter(10, TimeUnit.SECONDS); }); return; }
        }
        if (message.length == 1 && message[0].equalsIgnoreCase(Bot.BotPrefix + "invites")) {
            List<Invite> invites = e.getJDA().getGuildById(e.getGuild().getId()).retrieveInvites().complete();
            String mention = e.getAuthor().getAsMention();

            int i = 0;
            for (Invite invite : invites) {

                if (invites.get(i).getInviter().getAsMention().toString().equals(f)) {
                    InvitesTotal = InvitesTotal + invites.get(i).getUses();
                }
                i++;
            }
            EmbedBuilder EmbedRules = new EmbedBuilder();
            EmbedRules.setTitle("Invites:");
            EmbedRules.setColor(Color);
            EmbedRules.setDescription("**" + mention + "**, You have ``" + InvitesTotal + "`` invites!");

            EmbedRules.setTimestamp(Bot.now);
            EmbedRules.setFooter(Bot.WaterMark, Bot.Logo);
            channel.sendMessage(EmbedRules.build()).queue(message1 -> {
                e.getMessage().delete().queueAfter(3, TimeUnit.SECONDS);
            });

        }
        if (message.length > 1 && message[0].equalsIgnoreCase(Bot.BotPrefix + "invites")) {
            String f2 = e.getMessage().getMentionedUsers().get(0).getAsMention();
            String userTag = e.getMessage().getMentionedUsers().get(0).getId();
            String userNick = e.getJDA().getGuildById(e.getGuild().getId()).getMemberById(userTag).getNickname();
            String mention = e.getMessage().getMentionedUsers().get(0).getAsMention();

            List<Invite> invites = e.getJDA().getGuildById(e.getGuild().getId()).retrieveInvites().complete();
            int i = 0;
            for (Invite invite : invites) {

                if (invites.get(i).getInviter().getAsMention().toString().equals(f2)) {
                    InvitesTotal = InvitesTotal + invites.get(i).getUses();
                }
                i++;
            }
            EmbedBuilder EmbedRules = new EmbedBuilder();
            EmbedRules.setTitle("Invites:");
            EmbedRules.setColor(Color);
            EmbedRules.setDescription("**" + mention + "**, has ``" + InvitesTotal + "`` invites!");

            EmbedRules.setTimestamp(Bot.now);
            EmbedRules.setFooter(Bot.WaterMark, Bot.Logo);
            channel.sendMessage(EmbedRules.build()).queue(message1 -> {
                e.getMessage().delete().queue();
            });

        }

    }
}