import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
public class Command_Invites extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        Color Color = java.awt.Color.decode(Bot.ColorHexCode);

        User author = e.getAuthor();
        Message msg = e.getMessage();
        MessageChannel channel = e.getChannel();
        String[] message = e.getMessage().getContentRaw().split(" ");
        int InvitesTotal = 0;
        String f = e.getMessage().getAuthor().getAsMention();

        if (message.length == 1 && message[0].equalsIgnoreCase(Bot.BotPrefix + "invites")) {
            List<Invite> invites = e.getJDA().getGuildById(e.getGuild().getId()).retrieveInvites().complete();
            String mention = e.getAuthor().getAsMention();

            int i = 0;
            for (Invite invite : invites) {

                if (invites.get(i).getInviter().getAsMention().toString().equals(f)) {
                    InvitesTotal = InvitesTotal + invites.get(i).getUses();
                }
                i++;
            }
            EmbedBuilder EmbedRules = new EmbedBuilder();
            EmbedRules.setTitle("Invites:");
            EmbedRules.setColor(Color);
            EmbedRules.setDescription("**" + mention + "**, You have ``" + InvitesTotal + "`` invites!");

            EmbedRules.setTimestamp(Bot.now);
            EmbedRules.setFooter(Bot.WaterMark, Bot.Logo);
            channel.sendMessage(EmbedRules.build()).queue(message1 -> {
                e.getMessage().delete().queueAfter(3, TimeUnit.SECONDS);
            });

        }
        if (message.length > 1 && message[0].equalsIgnoreCase(Bot.BotPrefix + "invites")) {
            String f2 = e.getMessage().getMentionedUsers().get(0).getAsMention();
            String userTag = e.getMessage().getMentionedUsers().get(0).getId();
            String userNick = e.getJDA().getGuildById(e.getGuild().getId()).getMemberById(userTag).getNickname();
            String mention = e.getMessage().getMentionedUsers().get(0).getAsMention();

            List<Invite> invites = e.getJDA().getGuildById(e.getGuild().getId()).retrieveInvites().complete();
            int i = 0;
            for (Invite invite : invites) {

                if (invites.get(i).getInviter().getAsMention().toString().equals(f2)) {
                    InvitesTotal = InvitesTotal + invites.get(i).getUses();
                }
                i++;
            }
            EmbedBuilder EmbedRules = new EmbedBuilder();
            EmbedRules.setTitle("Invites:");
            EmbedRules.setColor(Color);
            EmbedRules.setDescription("**" + mention + "**, has ``" + InvitesTotal + "`` invites!");

            EmbedRules.setTimestamp(Bot.now);
            EmbedRules.setFooter(Bot.WaterMark, Bot.Logo);
            channel.sendMessage(EmbedRules.build()).queue(message1 -> {
                e.getMessage().delete().queue();
            });

        }

    }
}