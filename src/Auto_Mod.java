import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;

@SuppressWarnings("unused")

public class Auto_Mod extends ListenerAdapter {


    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        Color Color = java.awt.Color.decode(Bot.ColorHexCode);

        String channel = e.getChannel().getId();
        String message = e.getMessage().getContentRaw();

        if (message.contains("discord.gg") && !e.getMember().getPermissions().contains(Permission.ADMINISTRATOR)) {

            e.getMessage().delete().queue();
        }
        String[] messageArr = e.getMessage().getContentRaw().split(" ");
        if (e.getMessage().getMentionedUsers().size() > 5) {
            e.getMessage().delete().queue();

        }


        if (e.getMessage().getMentionedUsers().size() > 0) {
            if (e.getMessage().getMentionedUsers().get(0).getId().equals(e.getGuild().getSelfMember().getId())) {
                EmbedBuilder EmbedRules = new EmbedBuilder();
                EmbedRules.setTitle("Bot Prefix");
                EmbedRules.setColor(Color);
                EmbedRules.addField("My prefix is:", "``" + Bot.BotPrefix + "``", false);

                EmbedRules.setTimestamp(Bot.now);
                EmbedRules.setFooter("Requested By: " + e.getAuthor().getAsTag(), Bot.Logo);
                e.getChannel().sendMessage(EmbedRules.build()).queue();
            }
        }

        String[] BlockedWordsList = new String[4];

        int TemporaryBlockedCount = 0;
        for (int i = 0; i < Bot.BlockedWordsList.length; i++) {
            if (!e.getMember().getPermissions().contains(Permission.ADMINISTRATOR) && e.getMessage().getContentRaw().toLowerCase().contains(Bot.BlockedWordsList[i])) {
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