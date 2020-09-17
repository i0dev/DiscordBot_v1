import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;

@SuppressWarnings("unused")
public class Command_Mute extends ListenerAdapter {

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

        if (message.length == 1 && message[0].equalsIgnoreCase(Bot.BotPrefix + "mute")) {
            if (isAllowed) {
                EmbedBuilder EmbedRules = new EmbedBuilder();
                EmbedRules.setTitle("Incorrect Format");
                EmbedRules.setColor(Color);
                EmbedRules.addField("Format:", "" + Bot.BotPrefix + "mute [@user] [reason]", false);
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
        if (message.length == 2 && message[0].equalsIgnoreCase(Bot.BotPrefix + "mute")) {
            if (isAllowed) {
                EmbedBuilder EmbedRules = new EmbedBuilder();
                EmbedRules.setTitle("Incorrect Format");
                EmbedRules.setColor(Color);
                EmbedRules.addField("Format:", "" + Bot.BotPrefix + "mute [@user] [reason]", false);
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
        if (message.length > 2 && message[0].equalsIgnoreCase(Bot.BotPrefix + "mute")) {
            if (isAllowed) {
                User MentionedUser = e.getMessage().getMentionedUsers().get(0);
                Member MentionedMember = e.getMessage().getMentionedMembers().get(0);
                Role mutedRole = null;
                try {
                    e.getGuild().getRolesByName("Muted", false).get(0);

                    mutedRole = e.getGuild().getRolesByName("Muted", false).get(0);

                } catch (Exception f) {
                    f.printStackTrace();
                    mutedRole = e.getGuild().createRole().setName("Muted").setColor(java.awt.Color.darkGray).complete();
                    for (int i = 0; i < e.getGuild().getTextChannels().size(); i++) {
                        e.getGuild().getTextChannelById(e.getGuild().getTextChannels().get(i).getId()).putPermissionOverride(mutedRole).setDeny(Permission.MESSAGE_WRITE).complete();
                    }

                    for (int k = 0; k < e.getGuild().getVoiceChannels().size(); k++) {
                        e.getGuild().getVoiceChannelById(e.getGuild().getVoiceChannels().get(k).getId()).putPermissionOverride(mutedRole).setDeny(Permission.VOICE_SPEAK).complete();
                    }
                }

                if (e.getGuild().getMember(MentionedUser).getRoles().contains(mutedRole)) {
                    EmbedBuilder EmbedRules = new EmbedBuilder();
                    EmbedRules.setTitle("Error");
                    EmbedRules.setColor(Color);
                    EmbedRules.addField("Error", e.getMember().getUser().getAsTag() + ", " + MentionedUser.getAsTag() + " is already muted!", false);
                    EmbedRules.setTimestamp(Bot.now);
                    EmbedRules.setFooter(Bot.WaterMark, Bot.Logo);
                    channel.sendMessage(EmbedRules.build()).queue(message1 -> {
                        e.getMessage().delete().queue();
                    });
                } else {

                    e.getGuild().addRoleToMember(MentionedMember, mutedRole).queue();

                    String reason = "";
                    for (int k = 2; k < message.length; k++) {
                        reason = reason + " " + message[k] + " ";
                    }

                    EmbedBuilder EmbedRules = new EmbedBuilder();
                    EmbedRules.setTitle("Successfully Muted");
                    EmbedRules.setColor(Color);
                    EmbedRules.addField("Success", e.getMember().getUser().getAsTag() + ", you have muted " + MentionedUser.getAsTag(), false);
                    EmbedRules.addField("Reason", reason, false);
                    EmbedRules.setTimestamp(Bot.now);
                    EmbedRules.setFooter(Bot.WaterMark, Bot.Logo);
                    channel.sendMessage(EmbedRules.build()).queue(message1 -> {
                        e.getMessage().delete().queue();
                    });

                    EmbedBuilder logs = new EmbedBuilder()
                            .setTitle("Mute Logs")
                            .setColor(Color)
                            .addField("User", e.getMember().getUser().getAsTag() + ", has muted " + MentionedUser.getAsTag(), false)
                            .addField("Reason", reason, false)
                            .setTimestamp(Bot.now)
                            .setFooter(Bot.WaterMark, Bot.Logo);
                    e.getGuild().getTextChannelById(Bot.LogsChannelID).sendMessage(logs.build()).queue(message1 -> {
                        e.getMessage().delete().queue();
                    });
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