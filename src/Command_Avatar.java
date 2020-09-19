import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
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

public class Command_Avatar extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        Color Color = java.awt.Color.decode(Bot.ColorHexCode);

        MessageChannel channelReceivedMessage = e.getChannel();

        String[] messageWithSplitting = e.getMessage().getContentRaw().split(" ");
        if (messageWithSplitting.length > 0 && messageWithSplitting[0].equalsIgnoreCase(Bot.BotPrefix + "avatar")) {
            ArrayList<String> BlacklistedGet = new ArrayList<>(); if (new File("BlacklistedUsers.json").exists()) { try { JSONObject json = (JSONObject) new JSONParser().parse(new FileReader(new File("BlacklistedUsers.json"))); BlacklistedGet = (ArrayList<String>) json.get("BlacklistedUsers"); } catch (Exception ee) { ee.printStackTrace(); } } boolean isBlacklisted = false; for (String s : BlacklistedGet) { if (e.getAuthor().getId().equals(s)) { isBlacklisted = true; } } if (isBlacklisted) { EmbedBuilder UserBlacklisted = new EmbedBuilder() .setTitle("Error") .setThumbnail(Bot.BotLogo) .setFooter(Bot.WaterMark, Bot.BotLogo) .setTimestamp(Bot.now) .setColor(Color.RED) .setDescription("**" + e.getAuthor().getAsTag() + "**, *You are blacklisted from using all commands, \n" + "If you think this is an error please contact a staff member!*"); e.getChannel().sendMessage(UserBlacklisted.build()).queue(message3 -> { e.getMessage().delete().queue(); message3.addReaction("❌").queue(); message3.delete().queueAfter(10, TimeUnit.SECONDS); }); return; }
        }
        if (messageWithSplitting.length == 1 && messageWithSplitting[0].equalsIgnoreCase(Bot.BotPrefix + "avatar")) {
            EmbedBuilder Embed = new EmbedBuilder();
            Embed.setTitle(e.getMember().getUser().getName() + "'s Avatar:");
            Embed.setColor(Color);
            Embed.setImage(e.getMember().getUser().getAvatarUrl());
            Embed.setFooter(Bot.WaterMark, Bot.Logo);

            channelReceivedMessage.sendMessage(Embed.build()).queue(message1 -> {
                e.getMessage().delete().queue();
            });
        }
        if (messageWithSplitting.length == 2 && messageWithSplitting[0].equalsIgnoreCase(Bot.BotPrefix + "avatar")) {
            net.dv8tion.jda.api.entities.Member memb = e.getGuild()
                    .getMember(e.getMessage().getMentionedUsers().get(0));
            String av = memb.getUser().getAvatarUrl();
            String name = memb.getUser().getName();
            EmbedBuilder EmbedHelp = new EmbedBuilder();
            EmbedHelp.setTitle(name + "'s Avatar:");
            EmbedHelp.setColor(Color);
            EmbedHelp.setFooter(Bot.WaterMark, Bot.Logo);

            EmbedHelp.setImage(av);
            channelReceivedMessage.sendMessage(EmbedHelp.build()).queue(message1 -> {
                e.getMessage().delete().queue();
            });

        }
    }
}