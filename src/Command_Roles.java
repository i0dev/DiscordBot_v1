import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.List;

@SuppressWarnings("unused")
public class Command_Roles extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        Color Color = java.awt.Color.decode(Bot.ColorHexCode);

        User author = e.getAuthor();
        Message msg = e.getMessage();
        MessageChannel channel = e.getChannel();

        String[] message = e.getMessage().getContentRaw().split(" ");
        String RolesString = " ";
        List<Role> RolesListFull = e.getGuild().getRoles();
        String[] Roles = new String[RolesListFull.size()];
        for (int i = 0; i < RolesListFull.size() - 1; i++) {
            Roles[i] = RolesListFull.get(i) + "";
            Roles[i] = Roles[i].substring(Roles[i].length() - 19, Roles[i].length() - 1);
            RolesString = RolesString + " <@&" + Roles[i] + ">\n";
        }
        RolesString = RolesString + "@everyone";

        if (message.length == 1 && message[0].equalsIgnoreCase(Bot.BotPrefix + "roles")) {
            EmbedBuilder Embed = new EmbedBuilder();

            Embed.setTitle("List of all Roles");
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