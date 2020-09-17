import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;

@SuppressWarnings("unused")
public class Command_IP extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {

        Color Color = java.awt.Color.decode(Bot.ColorHexCode);
        MessageChannel channel = e.getChannel();
        String[] message = e.getMessage().getContentRaw().split(" ");

        if (message.length == 1 && message[0].equalsIgnoreCase(Bot.BotPrefix + "ip")) {
            EmbedBuilder EmbedRules = new EmbedBuilder();
            EmbedRules.setTitle("The Current server's IP Address:");
            EmbedRules.setColor(Color);
            EmbedRules.setDescription(Bot.ServerIP);
            EmbedRules.setTimestamp(Bot.now);
            EmbedRules.setFooter(Bot.WaterMark, Bot.Logo);
            channel.sendMessage(EmbedRules.build()).queue(message1 -> {
                e.getMessage().delete().queue();
            });
        }
    }
}