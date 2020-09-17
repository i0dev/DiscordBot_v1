import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;

@SuppressWarnings("unused")

public class Command_Avatar extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        Color Color = java.awt.Color.decode(Bot.ColorHexCode);

        MessageChannel channelReceivedMessage = e.getChannel();

        String[] messageWithSplitting = e.getMessage().getContentRaw().split(" ");

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