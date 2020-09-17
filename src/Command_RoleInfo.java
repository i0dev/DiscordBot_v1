import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.List;

@SuppressWarnings("unused")
public class Command_RoleInfo extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        Color Color = java.awt.Color.decode(Bot.ColorHexCode);

        User author = e.getAuthor();
        Message msg = e.getMessage();
        MessageChannel channel = e.getChannel();

        String[] message = e.getMessage().getContentRaw().split(" ");
        for (int i = 0; i < message.length; i++) {
            message[i] = message[i].toLowerCase();
        }
        if (message.length == 1 && message[0].equalsIgnoreCase(Bot.BotPrefix + "roleinfo")) {
            EmbedBuilder Embed = new EmbedBuilder();
            Embed.setTitle("Incorrect Format");
            Embed.addField("Format:",
                    "**" + Bot.BotPrefix + "roleinfo [Role] - No mention, just name**", false);
            Embed.setColor(Color);
            Embed.setTimestamp(Bot.now);
            Embed.setFooter(Bot.WaterMark, Bot.Logo);
            channel.sendMessage(Embed.build()).queue(message1 -> {
                e.getMessage().delete().queue();
            });
        } else if (message.length > 1 && message[0].equalsIgnoreCase(Bot.BotPrefix + "roleinfo")) {

            String RoleInput = "";
            if (message.length == 2) {
                RoleInput = message[1];
            } else if (message.length == 3) {
                RoleInput = message[1] + " " + message[message.length - 1] + "";

            } else if (message.length == 4) {
                RoleInput = message[1] + " " + message[2] + " " + message[3];
            } else if (message.length == 5) {
                RoleInput = message[1] + " " + message[message.length - 3] + " " + message[message.length - 2] + " "
                        + message[message.length - 1];

            } else if (message.length > 5) {
                EmbedBuilder Embed = new EmbedBuilder();
                Embed.setTitle("Error");
                Embed.addField(":small_red_triangle_down: Size Error :small_red_triangle_down:",
                        "**That role name is to large.**", false);
                Embed.setColor(Color);
                Embed.setTimestamp(Bot.now);
                Embed.setFooter("Request From " + e.getAuthor().getAsTag(), Bot.Logo);
                channel.sendMessage(Embed.build()).queue(message1 -> {
                    e.getMessage().delete().queue();
                });
            }

            List<net.dv8tion.jda.api.entities.Role> Roles = e.getGuild().getRoles();
            int Position = 9999;
            int IDposition = 0;
            String[] Roles11 = new String[Roles.size()];
            String[] Roles12 = new String[Roles.size()];

            for (int i = 0; i < Roles.size(); i++) {
                Roles11[i] = Roles.get(i) + "";
                Roles11[i] = Roles11[i].substring(2, Roles11[i].length() - 20);
                Roles11[i] = Roles11[i].toLowerCase();

                if (Roles11[i].equals(RoleInput)) {
                    Position = i;
                }
            }
            if (Position == 9999) {
                EmbedBuilder Embed = new EmbedBuilder();
                Embed.setTitle("Unkown Role");
                Embed.addField(":small_red_triangle_down: Unkown Role :small_red_triangle_down: ",
                        "**f you need the list of roles, Type " + Bot.BotPrefix + "roles**", false);
                Embed.setColor(Color);
                Embed.setTimestamp(Bot.now);
                Embed.setFooter("Request From " + e.getAuthor().getAsTag(), Bot.Logo);
                channel.sendMessage(Embed.build()).queue(message1 -> {
                    e.getMessage().delete().queue();
                });
            }
            for (int i = 0; i < Roles.size(); i++) {
                Roles12[i] = Roles.get(i) + "";
                Roles12[i] = Roles12[i].substring(Roles12[i].length() - 19, Roles12[i].length() - 1);
            }

            String RoleID = Roles12[Position];

            EmbedBuilder Embed = new EmbedBuilder();

            String RoleColor = "" + e.getGuild().getRoleById(RoleID).getColor();
            String RoleName = "" + e.getGuild().getRoleById(RoleID).getName();
            String RoleMention = "" + e.getGuild().getRoleById(RoleID).getAsMention();
            String RoleID1 = "" + RoleID;
            String RolePosition = "" + Position;
            String RoleIsMention = "";
            if (e.getGuild().getRoleById(RoleID).isMentionable() == true) {
                RoleIsMention = "Yes";
            } else {
                RoleIsMention = "No";
            }
            String RoleIsHoisted = "";
            if (e.getGuild().getRoleById(RoleID).isHoisted() == true) {
                RoleIsHoisted = "Yes";
            } else {
                RoleIsHoisted = "No";
            }

            String Red = "0";
            String Blue = "0";
            String Green = "0";
            String[] RawColor = RoleColor.split("=");
            if (RawColor[0].equals("null")) {

            } else {
                Red = RawColor[1].substring(0, RawColor[1].length() - 2);
                Blue = RawColor[2].substring(0, RawColor[2].length() - 2);
                Green = RawColor[3].substring(0, RawColor[3].length() - 1);
            }

            Embed.setTitle("Role Information [ " + RoleInput + " ]");
            Embed.setColor(Color);
            Embed.addField("ID", "" + RoleID1, true);
            Embed.addField("Name", "" + RoleName, true);
            Embed.addField("Color", "[Red:" + Red + "] [Blue:" + Blue + "] [Green:" + Green + "]", true);
            Embed.addField("Mention", "" + RoleMention, true);
            Embed.addField("Position", "" + RolePosition, true);
            Embed.addField("Mentionable", "" + RoleIsMention, true);
            Embed.addField("Hoisted", "" + RoleIsHoisted, true);

            Embed.setTimestamp(Bot.now);
            Embed.setFooter(Bot.WaterMark, Bot.Logo);
            channel.sendMessage(Embed.build()).queue(message1 -> {
                e.getMessage().delete().queue();
            });

        }
    }
}