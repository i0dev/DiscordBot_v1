import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;

@SuppressWarnings("unused")
public class Command_Members extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {

        Color Color = java.awt.Color.decode(Bot.ColorHexCode);
        MessageChannel channel = e.getChannel();
        String[] message = e.getMessage().getContentRaw().split(" ");

        boolean isAllowed = false;
        for (int i = 0; i < Bot.AllowedRoles.size(); i++) {
            if (e.getMember().getRoles().contains(e.getGuild().getRoleById(Bot.AllowedRoles.get(i))) || e.getMember().getPermissions().contains(Permission.ADMINISTRATOR)) {
                isAllowed = true;
            }
        }

        if (message.length == 1 && message[0].equalsIgnoreCase(Bot.BotPrefix + "members")) {
            if (isAllowed) {
                EmbedBuilder EmbedRules = new EmbedBuilder();
                EmbedRules.setTitle("Incorrect Format");
                EmbedRules.setColor(Color);
                EmbedRules.addField("Format:", "" + Bot.BotPrefix + "members [@role]", false);
                EmbedRules.setTimestamp(Bot.now);
                EmbedRules.setFooter(Bot.WaterMark, Bot.Logo);
                channel.sendMessage(EmbedRules.build()).queue(message1 -> {
                    e.getMessage().delete().queue();
                });
            } else {
                EmbedBuilder EmbedRules = new EmbedBuilder();
                EmbedRules.setTitle("Insufficient Permissions");
                EmbedRules.setColor(Color);
                EmbedRules.addField("Error:", "You do not have permission to run this command!", false);
                EmbedRules.setTimestamp(Bot.now);
                EmbedRules.setFooter(Bot.WaterMark, Bot.Logo);
                channel.sendMessage(EmbedRules.build()).queue(message1 -> {
                    e.getMessage().delete().queue();
                });
            }
        }

        if (message.length == 2 && message[0].equalsIgnoreCase(Bot.BotPrefix + "members")) {
            if (isAllowed) {

                Role MentionedRole = e.getMessage().getMentionedRoles().get(0);
                int total1 = 0;
                String RolesString = "";
                for (int i = 0; i < e.getGuild().getMemberCount(); i++) {
                    if (e.getGuild()
                            .getMembers()
                            .get(i)
                            .getRoles()
                            .contains(MentionedRole)) {
                        total1++;

                        RolesString = RolesString + " " + e.getGuild().getMembers().get(i).getAsMention() + " | "
                                + e.getGuild().getMembers().get(i).getUser().getAsTag() + "\n";
                    }
                }
                if (RolesString.length() > 2000) {
                    for (int i = 1500; i < RolesString.length(); i = i + 1500) {
                        EmbedBuilder Embed = new EmbedBuilder()
                                .addField("Total users: ", total1 + "", false)
                                .setTitle("List of all users with the " + MentionedRole.getName() + " role")
                                .setColor(Color)
                                .setDescription(RolesString.substring(i - 1500, i))
                                .setTimestamp(Bot.LocalTime)
                                .setFooter(Bot.WaterMark, Bot.Logo);
                        e.getChannel().sendMessage(Embed.build()).queue(message1 -> {
                            e.getMessage().delete().queue();
                        });
                    }
                    total1 = 0;
                } else {
                    EmbedBuilder Embed = new EmbedBuilder()
                            .addField("Total users: ", total1 + "", false)
                            .setTitle("List of all users with the " + MentionedRole.getName() + " role")
                            .setColor(Color)
                            .setDescription(RolesString)
                            .setTimestamp(Bot.LocalTime)
                            .setFooter(Bot.WaterMark, Bot.Logo);
                    e.getChannel().sendMessage(Embed.build()).queue(message1 -> {
                        e.getMessage().delete().queue();
                    });
                    total1 = 0;


                }
            } else {
                EmbedBuilder EmbedRules = new EmbedBuilder();
                EmbedRules.setTitle("Insufficient Permissions");
                EmbedRules.setColor(Color);
                EmbedRules.addField("Error:", "You do not have permission to run this command!", false);
                EmbedRules.setTimestamp(Bot.now);
                EmbedRules.setFooter(Bot.WaterMark, Bot.Logo);
                channel.sendMessage(EmbedRules.build()).queue(message1 -> {
                    e.getMessage().delete().queue();
                });
            }
        }
    }
}
