import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@SuppressWarnings("unused")
public class Command_Prune extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        Color Color = java.awt.Color.decode(Bot.ColorHexCode);

        User author = e.getAuthor();
        Message msg = e.getMessage();
        MessageChannel channel = e.getChannel();
        String[] message = e.getMessage().getContentRaw().split(" ");

        if (message.length == 1 && message[0].equalsIgnoreCase(Bot.BotPrefix + "prune")) {

            channel.sendMessage("Usage: " + Bot.BotPrefix + "prune <user> [messages]").queue();
            ;
        }
        if (message.length == 2 && message[0].equalsIgnoreCase(Bot.BotPrefix + "prune")
                && e.getMember().hasPermission(Permission.MESSAGE_MANAGE)) {

            int pruneNum = Integer.parseInt(message[1]);
            pruneNum++;

            List<Message> messages = e.getChannel().getHistory().retrievePast(pruneNum).complete();
            OffsetDateTime twoWeeksAgo = OffsetDateTime.now().minus(2, ChronoUnit.WEEKS);

            channel.purgeMessages(messages);
        }
        if (message.length == 3 && message[0].equalsIgnoreCase(Bot.BotPrefix + "prune")
                && e.getMember().hasPermission(Permission.MESSAGE_MANAGE)) {

            User Mentioneduser = e.getMessage().getMentionedUsers().get(0);


            int pruneNum = Integer.parseInt(message[2]);
            pruneNum++;

            List<Message> messages = e.getChannel().getHistory().retrievePast(pruneNum).complete();
            OffsetDateTime twoWeeksAgo = OffsetDateTime.now().minus(2, ChronoUnit.WEEKS);
            for (int i = 0; i < message.length; i++) {
                if (messages.get(i).getAuthor().equals(Mentioneduser))
                    channel.deleteMessageById(messages.get(i).getId()).queue();
            }

        }
        if (message.length > 1 && message[0].equalsIgnoreCase(Bot.BotPrefix + "prune")
                && !e.getMember().hasPermission(Permission.MESSAGE_MANAGE)) {
            EmbedBuilder EmbedRules = new EmbedBuilder();
            EmbedRules.setTitle("Incorrect Format");
            EmbedRules.setColor(Color);
            EmbedRules.addField("Format:", "" + Bot.BotPrefix + "demote [@User] <-s>", false);
            EmbedRules.setTimestamp(Bot.now);
            EmbedRules.setFooter(Bot.WaterMark, Bot.BotLogo);
            channel.sendMessage(EmbedRules.build()).queue(message1 -> {
                e.getMessage().delete().queue();
            });

        }


    }
}