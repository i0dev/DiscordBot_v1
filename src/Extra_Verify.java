import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class Extra_Verify extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {

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

            e.getChannel().sendMessage(Embed.build()).queue(message -> {
                e.getMessage().delete().queue();
                message.pin().queue();
                message.addReaction("✅").queue();

            });
        }
    }

    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent e) {
        Color Color = java.awt.Color.decode(Bot.ColorHexCode);

        if (!e.getMember().getUser().isBot()) {
            if (e.getReactionEmote().getName().equals("✅")
                    && e.getChannel().getId().equals(Bot.Verify_ChannelID)) {
                e.getChannel().getHistoryFromBeginning(100).complete().getMessageById(e.getMessageId())
                        .removeReaction("✅", e.getUser()).complete();

                e.getGuild().addRoleToMember(e.getMember().getId(), e.getGuild().getRoleById(Bot.Verify_RoleToGiveID)).queue();

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