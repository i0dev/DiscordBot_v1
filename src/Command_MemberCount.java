import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;

@SuppressWarnings("unused")
public class Command_MemberCount extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        Color Color = java.awt.Color.decode(Bot.ColorHexCode);

        User author = e.getAuthor();
        Message msg = e.getMessage();
        MessageChannel channel = e.getChannel();
        String[] message = e.getMessage().getContentRaw().split(" ");

        if (message.length == 1 && message[0].equalsIgnoreCase(Bot.BotPrefix + "membercount")) {

            EmbedBuilder Embed = new EmbedBuilder();
            Embed.setTitle("Member Count:");
            Embed.addField("Members", e.getGuild().getMemberCount() + "", true);
            Embed.addField("Bots",
                    "" + e.getGuild().getMembers().stream().filter(member -> member.getUser().isBot()).count(),
                    true);
            Embed.setColor(Color);
            Embed.setFooter(Bot.WaterMark, Bot.Logo);
            channel.sendMessage(Embed.build()).queue();

        }

    }
}