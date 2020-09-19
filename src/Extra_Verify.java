import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Extra_Verify extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        String[] message = e.getMessage().getContentRaw().split(" ");
        if (message.length > 0 && message[0].equalsIgnoreCase(Bot.BotPrefix + "Verify_Panel")) {
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
        if (e.getMessage().getContentRaw().equalsIgnoreCase(Bot.BotPrefix + "Verify_Panel")
                && e.getMember().getPermissions().contains(Permission.ADMINISTRATOR)) {
            Color Color = java.awt.Color.decode(Bot.ColorHexCode);

            EmbedBuilder Embed = new EmbedBuilder()
                    .setTitle(Bot.BotName + " Verification")
                    .setThumbnail(Bot.BotLogo)
                    .setColor(Color)
                    .setDescription(Bot.Verify_EmbedDescription)
                    .setFooter(Bot.BotName + " Verification", Bot.BotLogo)
                    .setTimestamp(Bot.LocalTime);

            e.getChannel().sendMessage(Embed.build()).queue(message32 -> {
                e.getMessage().delete().queue();
                message32.pin().queue();
                message32.addReaction("✅").queue();

            });
        }
    }

    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent e) {
        if (!e.getMember().getUser().isBot() && e.getChannel().getId().equals(Bot.Verify_ChannelID)) {
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
                if (e.getUser().getId().equals(s)) {
                    isBlacklisted = true;
                }
            }
            if (isBlacklisted) {

                return;
            }
        }
        Color Color = java.awt.Color.decode(Bot.ColorHexCode);

        if (!e.getMember().getUser().isBot()) {
            if (e.getReactionEmote().getName().equals("✅")
                    && e.getChannel().getId().equals(Bot.Verify_ChannelID)) {
                e.getChannel().getHistoryFromBeginning(100).complete().getMessageById(e.getMessageId())
                        .removeReaction("✅", e.getUser()).complete();

                e.getGuild().addRoleToMember(e.getMember(), e.getGuild().getRoleById(Bot.Verify_RoleToGiveID)).queue();
                e.getGuild().removeRoleFromMember(e.getMember(), e.getGuild().getRoleById(Bot.VisitorRoleID)).queue();
                MessageChannel privateDM = e.getMember().getUser().openPrivateChannel().complete();
                EmbedBuilder Embed = new EmbedBuilder()
                        .setTitle(Bot.BotName + " Verification")
                        .addField("Success!",
                                "Thank you for **Verifying** in the " + Bot.BotName + " Discord!", false)
                        .setColor(Color)
                        .setTimestamp(Bot.LocalTime)
                        .setThumbnail(Bot.BotLogo);
                privateDM.sendMessage(Embed.build()).queue();

            }
        }
    }
}