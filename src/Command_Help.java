import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")

public class Command_Help extends ListenerAdapter {


    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        Color Color = java.awt.Color.decode(Bot.ColorHexCode);

        MessageChannel channel = e.getChannel();


        String[] message = e.getMessage().getContentRaw().split(" ");
        if (message.length > 0 && message[0].equalsIgnoreCase(Bot.BotPrefix + "help")) {
            ArrayList<String> BlacklistedGet = new ArrayList<>(); if (new File("BlacklistedUsers.json").exists()) { try { JSONObject json = (JSONObject) new JSONParser().parse(new FileReader(new File("BlacklistedUsers.json"))); BlacklistedGet = (ArrayList<String>) json.get("BlacklistedUsers"); } catch (Exception ee) { ee.printStackTrace(); } } boolean isBlacklisted = false; for (String s : BlacklistedGet) { if (e.getAuthor().getId().equals(s)) { isBlacklisted = true; } } if (isBlacklisted) { EmbedBuilder UserBlacklisted = new EmbedBuilder() .setTitle("Error") .setThumbnail(Bot.BotLogo) .setFooter(Bot.WaterMark, Bot.BotLogo) .setTimestamp(Bot.now) .setColor(Color.RED) .setDescription("**" + e.getAuthor().getAsTag() + "**, *You are blacklisted from using all commands, \n" + "If you think this is an error please contact a staff member!*"); e.getChannel().sendMessage(UserBlacklisted.build()).queue(message3 -> { e.getMessage().delete().queue(); message3.addReaction("❌").queue(); message3.delete().queueAfter(10, TimeUnit.SECONDS); }); return; }
        }

        boolean isAllowed = false;
        for (int i = 0; i < Bot.AllowedRoles.size(); i++) {
            if (e.getMember().getRoles().contains(e.getGuild().getRoleById(Bot.AllowedRoles.get(i))) || e.getMember().getPermissions().contains(Permission.ADMINISTRATOR)) {
                isAllowed = true;
            }
        }
        if (message.length == 1 && message[0].equalsIgnoreCase(Bot.BotPrefix + "help")) {
            if (isAllowed) {

                EmbedBuilder Embed = new EmbedBuilder();
                Embed.setTitle(Bot.BotName + " Help Page");
                Embed.setColor(Color);
                Embed.setDescription("Please check your DM's for a list of commands!");
                Embed.setTimestamp(Bot.now);
                Embed.setFooter(Bot.WaterMark, Bot.Logo);
                channel.sendMessage(Embed.build()).queue();

                EmbedBuilder EmbedHelp = new EmbedBuilder();
                EmbedHelp.setTitle(Bot.BotName + " Help Page");
                EmbedHelp.setColor(Color);
                EmbedHelp.setDescription("[ ] = Required || < > = Optional");
                EmbedHelp.addField("General",
                        "**" + Bot.BotPrefix + "help** > Sends the help page \n" +
                                "**" + Bot.BotPrefix + "roles** > Display all the server roles.\n" +
                                "**" + Bot.BotPrefix + "roleinfo** [role] > Sends in depth information about that role.\n" +
                                "**" + Bot.BotPrefix + "serverinfo** > Sends in depth information about the server.\n" +
                                "**" + Bot.BotPrefix + "avatar** <user> > Gets the avatar of the mentioned user\n" +
                                "**" + Bot.BotPrefix + "invites** <user> > Gets their or yours invites to the server \n" +
                                "**" + Bot.BotPrefix + "whois** [user] > Gets a list of that users info\n" +
                                "**" + Bot.BotPrefix + "membercount** > Sends the member count\n" +
                                "**" + Bot.BotPrefix + "ip** > Sends the current servers IP.\n" +
                                "**" + Bot.BotPrefix + "apply** > Apply to join the faction"
                        , false);

                EmbedHelp.addField("Moderation",

                        "**" + Bot.BotPrefix + "ban** [user] > Bans the user.\n" +
                                "**" + Bot.BotPrefix + "kick** [user] > Kicks the user.\n" +
                                "**" + Bot.BotPrefix + "members** [@Role] > Lists all members with that role.\n" +
                                "**" + Bot.BotPrefix + "unban** [user] > Un-Bans the user.\n" +
                                "**" + Bot.BotPrefix + "prune** <user> [messages] > Deletes that many amount of messages in that channel\n" +
                                "**" + Bot.BotPrefix + "mute** [user] [reason] > mutes the user.\n" +
                                "**" + Bot.BotPrefix + "unmute** [user] [reason] > unmutes the user.\n" +
                                "**" + Bot.BotPrefix + "announce** [#channel] [announcement] > sends an announcement to that channel\n" +
                                "**" + Bot.BotPrefix + "say** [#channel] [announcement] > sends a silent-announcement to that channel\n" +
                                "**" + Bot.BotPrefix + "getMuted** > Gets all muted users\n" +
                                "**" + Bot.BotPrefix + "accept** [@user] [Role-To-Give] > Accepts an application\n" +
                                "**" + Bot.BotPrefix + "deny** [@user] [Reason] > Denies an applcation\n" +
                                "**" + Bot.BotPrefix + "review** [@user] > Sends their application\n" +
                                "**" + Bot.BotPrefix + "CurrentApps** > Sends a list of all pending applications\n" +
                                "**" + Bot.BotPrefix + "CancelApps** > Cancels all current applications"

                        , false);

                EmbedHelp.setTimestamp(Bot.now);
                EmbedHelp.setFooter("Created by i0dev.com", Bot.Logo);
                MessageChannel openDmsWithUser = e.getAuthor().openPrivateChannel().complete();
                openDmsWithUser.sendMessage(EmbedHelp.build()).queue(message1 -> {
                    e.getMessage().delete().queue();
                });


            } else {
                EmbedBuilder EmbedHelp = new EmbedBuilder();
                EmbedHelp.setTitle(Bot.BotName + " Help Page");
                EmbedHelp.setColor(Color);
                EmbedHelp.setDescription("[ ] = Required || < > = Optional");
                EmbedHelp.addField("General",
                        "**" + Bot.BotPrefix + "help** > Sends the help page \n" +
                                "**" + Bot.BotPrefix + "roles** > Display all the server roles.\n" +
                                "**" + Bot.BotPrefix + "roleinfo** [role] > Sends in depth information about that role.\n" +
                                "**" + Bot.BotPrefix + "serverinfo** > Sends in depth information about the server.\n" +
                                "**" + Bot.BotPrefix + "avatar** <user> > Gets the avatar of the mentioned user\n" +
                                "**" + Bot.BotPrefix + "invites** <user> > Gets their or yours invites to the server \n" +
                                "**" + Bot.BotPrefix + "whois** [user] > Gets a list of that users info\n" +
                                "**" + Bot.BotPrefix + "membercount** > Sends the member count\n" +
                                "**" + Bot.BotPrefix + "ip** > Sends the current servers IP."
                        , false);
                EmbedHelp.addField("Credit", "Bot was created by i01, ad i0dev.com", false);

                EmbedHelp.setTimestamp(Bot.now);
                EmbedHelp.setFooter(Bot.WaterMark, Bot.Logo);
                MessageChannel openDmsWithUser = e.getAuthor().openPrivateChannel().complete();
                openDmsWithUser.sendMessage(EmbedHelp.build()).queue(message1 -> {
                    e.getMessage().delete().queue();
                });

            }
        }

    }
}