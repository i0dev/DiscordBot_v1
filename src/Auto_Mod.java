import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")

public class Auto_Mod extends ListenerAdapter {


    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        Color Color = java.awt.Color.decode(Bot.ColorHexCode);

        String channel = e.getChannel().getId();
        String message = e.getMessage().getContentRaw();

        boolean isAutoModChannel = false;
        for (int i = 0; i < Bot.AutoModChannelIDS.size(); i++) {
            if (e.getChannel().getId().equals(Bot.AutoModChannelIDS.get(i))) {

                isAutoModChannel = true;
            }
        }
        if (message.contains("discord.gg") && isAutoModChannel && !e.getMember().getPermissions().contains(Permission.ADMINISTRATOR)) {

            e.getMessage().delete().queue();
        }


        String[] messageArr = e.getMessage().getContentRaw().split(" ");
        if (e.getMessage().getMentionedUsers().size() > 5) {
            e.getMessage().delete().queue();

        }

        if ((messageArr[0].toLowerCase().contains("ticket") || messageArr[0].toLowerCase().contains("new")) && !messageArr[0].toLowerCase().contains("tickettop") && !messageArr[0].toLowerCase().contains("ticketinfo")) {
            EmbedBuilder EmbedRules = new EmbedBuilder();
            EmbedRules.setTitle(Bot.BotName + " Tickets");
            EmbedRules.setColor(Color)
                    .addField("Hey **" + e.getAuthor().getAsTag() + ",**", " Please head over to **" + e.getGuild().getTextChannelById(Bot.TicketCreateChannelID).getAsMention() + "** to create a ticket!", false);
            EmbedRules.setThumbnail(Bot.BotLogo);
            EmbedRules.setTimestamp(Bot.now)
                    .setFooter(Bot.WaterMark, Bot.BotLogo);
            e.getChannel().sendMessage(EmbedRules.build()).queue(message1 -> {
                e.getMessage().delete().queueAfter(5, TimeUnit.SECONDS);
            });

        }


        if (e.getMessage().getMentionedUsers().size() > 0) {
            if (e.getMessage().getMentionedUsers().get(0).getId().equals(e.getGuild().getSelfMember().getId())) {
                EmbedBuilder EmbedRules = new EmbedBuilder();
                EmbedRules.setTitle(e.getJDA().getSelfUser().getName());
                EmbedRules.setColor(Color);
                EmbedRules.setDescription(">>> My prefix is: " + "``" + Bot.BotPrefix + "``\nType `" + Bot.BotPrefix + "help` to see all commands\n");
                EmbedRules.setThumbnail(Bot.BotLogo);
                EmbedRules.setTimestamp(Bot.now);
                EmbedRules.setFooter("Bot created by i0dev.com", "https://cdn.discordapp.com/attachments/667207717882036234/746181096995291156/i01.png");
                e.getChannel().sendMessage(EmbedRules.build()).queue();
            }
        }


        int TemporaryBlockedCount = 0;
        for (int i = 0; i < Bot.BlockedWordsList.size(); i++) {
            if (!e.getMember().getPermissions().contains(Permission.ADMINISTRATOR) && e.getMessage().getContentRaw().toLowerCase().contains(Bot.BlockedWordsList.get(i).toLowerCase())) {
                TemporaryBlockedCount++;
            }
        }
        if (TemporaryBlockedCount > 0) {
            e.getMessage().delete().queue();
            MessageChannel channel12 = e.getGuild().getTextChannelById(Bot.LogsChannelID);

            EmbedBuilder EmbedRules = new EmbedBuilder();
            EmbedRules.setTitle(Bot.BotName + " Auto-Mod");
            EmbedRules.setDescription("**Click the black to see the message.**");
            EmbedRules.setColor(Color);
            EmbedRules.addField(e.getAuthor().getAsTag() + "'s Message:", "||" + e.getMessage().getContentRaw() + "||",
                    false);

            EmbedRules.setTimestamp(Bot.now);
            EmbedRules.setFooter("Offender: " + e.getAuthor().getAsTag(), Bot.Logo);
            channel12.sendMessage(EmbedRules.build()).queue();
        }
        TemporaryBlockedCount = 0;
    }


}