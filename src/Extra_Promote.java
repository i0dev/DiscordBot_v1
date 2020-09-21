import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
public class Extra_Promote extends ListenerAdapter {
    public String BotPrefix = Bot.BotPrefix;
    public String BotName = Bot.BotName;
    public ZonedDateTime LocalTime = ZonedDateTime.now();
    public ZonedDateTime now = ZonedDateTime.now();
    public String ColorHexCode = Bot.ColorHexCode;
    public String BotLogo = Bot.BotLogo;
    public String TicketCreateChannelID = Bot.TicketCreateChannelID;
    public String TicketCreateCategoryChannelID = Bot.TicketCreateCategoryChannelID;
    public String StaffMovementsChannelID = "";
    public ArrayList<String> SuggestionGameModes = new ArrayList<String>();

    public String SupportTeamRoleID = Bot.SupportTeamRoleID;
    public String AdminRoleID = Bot.AdminRoleID;
    public String MemberRoleID = Bot.MemberRoleID;

    public boolean StaffPingEnabled = Bot.StaffPingEnabled;

    public String ChannelID = "";
    public String ChannelName = "";
    public String CategoryID = "";
    public String UsersID = "";
    public String UsersTag = "";
    public String UsersAvatarURL = "";
    public boolean AdminOnlyMode;
    public ArrayList<String> StaffRoleIDS = new ArrayList<String>();

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        try {
            JSONObject json = (JSONObject) new JSONParser().parse(new FileReader(new File("Tickets/config.json")));
            BotPrefix = ((HashMap<String, String>) json.get("GeneralConfig")).get("BotPrefix");
            BotName = ((HashMap<String, String>) json.get("GeneralConfig")).get("BotName");
            BotLogo = ((HashMap<String, String>) json.get("GeneralConfig")).get("BotLogo");
            ColorHexCode = ((HashMap<String, String>) json.get("GeneralConfig")).get("ColorHexCode");
            StaffMovementsChannelID = ((HashMap<String, String>) json.get("ChannelIDS")).get("StaffMovementsChannelID");
            AdminRoleID = ((HashMap<String, String>) json.get("RoleIDS")).get("AdminRoleID");
            StaffRoleIDS = ((HashMap<String, ArrayList>) json.get("GeneralConfig")).get("StaffRoleIDS");
        } catch (Exception ee) {
            ee.printStackTrace();
        }

        Color Color = java.awt.Color.decode(ColorHexCode);
        MessageChannel channel = e.getChannel();
        String[] message = e.getMessage().getContentRaw().split(" ");
        if (message.length > 0 && message[0].equalsIgnoreCase(Bot.BotPrefix + "promote")) {
            ArrayList<String> BlacklistedGet = new ArrayList<>(); if (new File("BlacklistedUsers.json").exists()) { try { JSONObject json = (JSONObject) new JSONParser().parse(new FileReader(new File("BlacklistedUsers.json"))); BlacklistedGet = (ArrayList<String>) json.get("BlacklistedUsers"); } catch (Exception ee) { ee.printStackTrace(); } } boolean isBlacklisted = false; for (String s : BlacklistedGet) { if (e.getAuthor().getId().equals(s)) { isBlacklisted = true; } } if (isBlacklisted) { EmbedBuilder UserBlacklisted = new EmbedBuilder() .setTitle("Error") .setThumbnail(Bot.BotLogo) .setFooter(Bot.WaterMark, Bot.BotLogo) .setTimestamp(Bot.now) .setColor(Color.RED) .setDescription("**" + e.getAuthor().getAsTag() + "**, *You are blacklisted from using all commands, \n" + "If you think this is an error please contact a staff member!*"); e.getChannel().sendMessage(UserBlacklisted.build()).queue(message3 -> { e.getMessage().delete().queue(); message3.addReaction("❌").queue(); message3.delete().queueAfter(10, TimeUnit.SECONDS); }); return; }
        }
        boolean isAllowed = false;
        for (int i = 0; i < Bot.AllowedRoles.size(); i++) {
            if (e.getMember().getRoles().contains(e.getGuild().getRoleById(Bot.AllowedRoles.get(i))) || e.getMember().getPermissions().contains(Permission.ADMINISTRATOR)) {
                isAllowed = true;
            }
        }
        if (message.length == 1 && message[0].equalsIgnoreCase(Bot.BotPrefix + "promote")) {
            if (isAllowed) {
                EmbedBuilder EmbedRules = new EmbedBuilder();
                EmbedRules.setTitle("Incorrect Format");
                EmbedRules.setColor(Color);
                EmbedRules.addField("Format:", "" + Bot.BotPrefix + "promote [@User] <-s>", false);
                EmbedRules.setTimestamp(now);
                EmbedRules.setFooter(Bot.WaterMark, BotLogo);
                channel.sendMessage(EmbedRules.build()).queue(message1 -> {
                    e.getMessage().delete().queue();
                });
            } else {
                EmbedBuilder EmbedRules = new EmbedBuilder();
                EmbedRules.setTitle("Insufficient Permissions");
                EmbedRules.setColor(Color);
                EmbedRules.addField("Error:", "You do not have permission to run this command!", false);
                EmbedRules.setTimestamp(now);
                EmbedRules.setFooter(Bot.WaterMark, BotLogo);
                channel.sendMessage(EmbedRules.build()).queue(message1 -> {
                    e.getMessage().delete().queue();
                });
            }
        }
        if (message.length == 999 && message[0].equalsIgnoreCase(Bot.BotPrefix + "promote")) {
            if (isAllowed) {
                EmbedBuilder EmbedRules = new EmbedBuilder();
                EmbedRules.setTitle("Incorrect Format");
                EmbedRules.setColor(Color);
                EmbedRules.addField("Format:", "" + Bot.BotPrefix + "promote [@User] <-s>", false);
                EmbedRules.setTimestamp(now);
                EmbedRules.setFooter(Bot.WaterMark, BotLogo);
                channel.sendMessage(EmbedRules.build()).queue(message1 -> {
                    e.getMessage().delete().queue();
                });
            } else {
                EmbedBuilder EmbedRules = new EmbedBuilder();
                EmbedRules.setTitle("Insufficient Permissions");
                EmbedRules.setColor(Color);
                EmbedRules.addField("Error:", "You do not have permission to run this command!", false);
                EmbedRules.setTimestamp(now);
                EmbedRules.setFooter(Bot.WaterMark, BotLogo);
                channel.sendMessage(EmbedRules.build()).queue(message1 -> {
                    e.getMessage().delete().queue();
                });
            }
        }
        if ((message.length == 2 || message.length == 3) && message[0].equalsIgnoreCase(Bot.BotPrefix + "promote") && e.getMessage().getMentionedRoles().size() == 0) {
            if (isAllowed) {
                if (e.getMessage().getContentRaw().contains(" -s")) {
                    User MentionedUser = e.getMessage().getMentionedUsers().get(0);
                    Member MentionedMember = e.getMessage().getMentionedMembers().get(0);
                    Role Position = null;
                    boolean isCurrentlyStaff = false;
                    for (int i = 0; i < StaffRoleIDS.size(); i++) {
                        if (MentionedMember.getRoles().contains(e.getGuild().getRoleById(StaffRoleIDS.get(i)))) {
                            Position = e.getGuild().getRoleById(StaffRoleIDS.get(i + 1));
                            isCurrentlyStaff = true;
                            e.getGuild().addRoleToMember(MentionedMember, e.getGuild().getRoleById(StaffRoleIDS.get(i + 1))).queue();
                            e.getGuild().removeRoleFromMember(MentionedMember, e.getGuild().getRoleById(StaffRoleIDS.get(i))).queue();
                        }
                    }
                    if (!isCurrentlyStaff) {
                        e.getGuild().addRoleToMember(MentionedMember, e.getGuild().getRoleById(StaffRoleIDS.get(0))).queue();
                        e.getGuild().addRoleToMember(MentionedMember, e.getGuild().getRoleById(SupportTeamRoleID)).queue();
                        Position = e.getGuild().getRoleById(StaffRoleIDS.get(0));
                    }


                    EmbedBuilder EmbedRules = new EmbedBuilder();
                    EmbedRules.setTitle("Successfully Promoted " + MentionedUser.getAsTag());
                    EmbedRules.setColor(Color);
                    EmbedRules.addField("Success", e.getMember().getUser().getAsTag() + ", you have __*Silently*__ promoted **" + MentionedMember.getUser().getAsTag() + "** to **" + Position.getAsMention() + "**", false);
                    EmbedRules.setTimestamp(now);
                    EmbedRules.setFooter(Bot.WaterMark, BotLogo);
                    channel.sendMessage(EmbedRules.build()).queue(message1 -> {
                        e.getMessage().delete().queue();
                    });
                } else {

                    User MentionedUser = e.getMessage().getMentionedUsers().get(0);
                    Member MentionedMember = e.getMessage().getMentionedMembers().get(0);
                    Role Position = null;
                    boolean isCurrentlyStaff = false;
                    for (int i = 0; i < StaffRoleIDS.size(); i++) {
                        if (MentionedMember.getRoles().contains(e.getGuild().getRoleById(StaffRoleIDS.get(i)))) {
                            Position = e.getGuild().getRoleById(StaffRoleIDS.get(i + 1));
                            isCurrentlyStaff = true;
                            e.getGuild().addRoleToMember(MentionedMember, e.getGuild().getRoleById(StaffRoleIDS.get(i + 1))).queue();
                            e.getGuild().removeRoleFromMember(MentionedMember, e.getGuild().getRoleById(StaffRoleIDS.get(i))).queue();
                        }
                    }
                    if (!isCurrentlyStaff) {
                        e.getGuild().addRoleToMember(MentionedMember, e.getGuild().getRoleById(StaffRoleIDS.get(0))).queue();
                        e.getGuild().addRoleToMember(MentionedMember, e.getGuild().getRoleById(SupportTeamRoleID)).queue();
                        Position = e.getGuild().getRoleById(StaffRoleIDS.get(0));
                    }
                    EmbedBuilder EmbedRules = new EmbedBuilder();
                    EmbedRules.setTitle("Successfully Promoted " + MentionedUser.getAsTag());
                    EmbedRules.setColor(Color);
                    EmbedRules.addField("Success", e.getMember().getUser().getAsTag() + ", you have promoted **" + MentionedMember.getUser().getAsTag() + "** to **" + Position.getAsMention() + "**", false);

                    EmbedRules.setTimestamp(now);
                    EmbedRules.setFooter(Bot.WaterMark, BotLogo);
                    channel.sendMessage(EmbedRules.build()).queue();


                    EmbedBuilder Embed = new EmbedBuilder()
                            .setTitle("Incoming Staff Movement!")
                            .setColor(Color)
                            .addField("Promotion", MentionedMember.getUser().getAsTag() + " has been promoted to **" + Position.getAsMention() + "** by " + e.getAuthor().getAsTag(), false)
                            .setTimestamp(now)
                            .setFooter(Bot.WaterMark, BotLogo);
                    e.getGuild().getTextChannelById(StaffMovementsChannelID).sendMessage(Embed.build()).queue(message1 -> {
                        e.getMessage().delete().queue();
                    });

                }
            } else {
                EmbedBuilder EmbedRules = new EmbedBuilder();
                EmbedRules.setTitle("Insufficient Permissions");
                EmbedRules.setColor(Color);
                EmbedRules.addField("Error:", "You do not have permission to run this command!", false);
                EmbedRules.setTimestamp(now);
                EmbedRules.setFooter(Bot.WaterMark, BotLogo);
                channel.sendMessage(EmbedRules.build()).queue(message1 -> {
                    e.getMessage().delete().queue();
                });
            }

        }

    }
}