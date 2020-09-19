import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.OnlineStatus;
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
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
public class Command_ServerInfo extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        Color Color = java.awt.Color.decode(Bot.ColorHexCode);

        User author = e.getAuthor();
        Message msg = e.getMessage();
        MessageChannel channel = e.getChannel();

        String[] message = e.getMessage().getContentRaw().split(" ");
        if (message.length > 0 && message[0].equalsIgnoreCase(Bot.BotPrefix + "serverinfo")) {
            ArrayList<String> BlacklistedGet = new ArrayList<>(); if (new File("BlacklistedUsers.json").exists()) { try { JSONObject json = (JSONObject) new JSONParser().parse(new FileReader(new File("BlacklistedUsers.json"))); BlacklistedGet = (ArrayList<String>) json.get("BlacklistedUsers"); } catch (Exception ee) { ee.printStackTrace(); } } boolean isBlacklisted = false; for (String s : BlacklistedGet) { if (e.getAuthor().getId().equals(s)) { isBlacklisted = true; } } if (isBlacklisted) { EmbedBuilder UserBlacklisted = new EmbedBuilder() .setTitle("Error") .setThumbnail(Bot.BotLogo) .setFooter(Bot.WaterMark, Bot.BotLogo) .setTimestamp(Bot.now) .setColor(Color.RED) .setDescription("**" + e.getAuthor().getAsTag() + "**, *You are blacklisted from using all commands, \n" + "If you think this is an error please contact a staff member!*"); e.getChannel().sendMessage(UserBlacklisted.build()).queue(message3 -> { e.getMessage().delete().queue(); message3.addReaction("❌").queue(); message3.delete().queueAfter(10, TimeUnit.SECONDS); }); return; }
        }
        String CreationDateLong = "" + e.getGuild().getTimeCreated();
        String[] Created = CreationDateLong.split("-");
        String Year = Created[0];
        String Month = Created[1];
        String Day = Created[2];
        Day = Created[2].substring(0, 2);

        int online = 0;
        for (net.dv8tion.jda.api.entities.Member member : e.getGuild().getMembers()) {
            if (member.getOnlineStatus().equals(OnlineStatus.ONLINE)) {
                online++;
            }
        }
        int Idle = 0;
        for (net.dv8tion.jda.api.entities.Member member : e.getGuild().getMembers()) {
            if (member.getOnlineStatus().equals(OnlineStatus.IDLE)) {
                Idle++;
            }
        }
        int DND = 0;
        for (net.dv8tion.jda.api.entities.Member member : e.getGuild().getMembers()) {
            if (member.getOnlineStatus().equals(OnlineStatus.DO_NOT_DISTURB)) {
                DND++;
            }
        }
        int Offline = 0;
        for (net.dv8tion.jda.api.entities.Member member : e.getGuild().getMembers()) {
            if (member.getOnlineStatus().equals(OnlineStatus.OFFLINE)) {
                Offline++;
            }
        }

        if (message.length == 1 && message[0].equalsIgnoreCase(Bot.BotPrefix + "serverinfo")) {
            EmbedBuilder Embed = new EmbedBuilder();
            Embed.setTitle(e.getGuild().getName() + " Server Stats");
            Embed.setColor(Color);
            long OwnerID = e.getGuild().getOwnerIdLong();
            Embed.addField("Server Owner", ":crown: <@" + OwnerID + ">", true);
            Embed.addField("Server Region", ":globe_with_meridians: " + e.getGuild().getRegion(), true);
            Embed.addField("Member Count", ":clipboard: " + e.getGuild().getMemberCount(), true);
            Embed.addField("Channel Categories", ":dividers: " + e.getGuild().getCategories().size(), true);
            Embed.addField("Text Channels", ":speech_balloon: " + e.getGuild().getTextChannels().size(), true);
            Embed.addField("Voice Channels", ":loud_sound: " + e.getGuild().getVoiceChannels().size(), true);
            Embed.addField("Role Count", ":crystal_ball: " + e.getGuild().getRoles().size(), true);
            Embed.addField("Server Creation Date", ":calendar_spiral: " + Month + " / " + Day + " / " + Year, true);
            Embed.addField(Bot.BotName + " Bot Prefix", ":grey_question: [ " + Bot.BotPrefix + " ]", true);
            Embed.addBlankField(false);
            Embed.addField("Bot Count ",
                    ":robot: " + e.getGuild().getMembers().stream().filter(member -> member.getUser().isBot()).count(),
                    true);
            Embed.addField("Human Count ",
                    ":man: " + e.getGuild().getMembers().stream().filter(member -> !member.getUser().isBot()).count(),
                    true);
            Embed.addField("Online  ", ":green_circle: " + online, true);
            Embed.addField("Do Not Disturb  ", ":red_circle: " + DND, true);
            Embed.addField("Idle  ", ":yellow_circle: " + Idle, true);
            Embed.addField("Offline  ", ":white_circle: " + Offline, true);
            Invite invite = e.getGuild().getDefaultChannel().createInvite().complete();
            Embed.addField("Server Invite Link", ":link: " + invite.getUrl(), false);
            Embed.setTimestamp(Bot.now);
            Embed.setFooter(Bot.WaterMark, Bot.Logo);
            Embed.setThumbnail(Bot.Logo);
            channel.sendMessage(Embed.build()).queue(message1 -> {
                e.getMessage().delete().queue();
            });
        }

    }

}
