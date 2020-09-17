import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;

@SuppressWarnings("unused")
public class Command_GetMuted extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        Color Color = java.awt.Color.decode(Bot.ColorHexCode);

        User author = e.getAuthor();
        String RolesString = "";
        Message msg = e.getMessage();
        MessageChannel channel = e.getChannel();
        int total1 = 0;

        String[] message = e.getMessage().getContentRaw().split(" ");

        for (int i = 0; i < e.getGuild().getMemberCount(); i++) {
            if (e.getGuild().getMembers().get(i).getRoles().contains(e.getGuild().getRolesByName("Muted", false).get(0))) {
                RolesString = RolesString + " " + e.getGuild().getMembers().get(i).getAsMention() + " | "
                        + e.getGuild().getMembers().get(i).getUser().getAsTag() + "\n";
                total1++;
            }
        }

        if (message.length == 1 && message[0].equalsIgnoreCase(Bot.BotPrefix + "getMuted")) {
            EmbedBuilder Embed = new EmbedBuilder();

            Embed.addField("Total users: ", total1 + "", false);
            Embed.setTitle("List of all Muted Users");
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