import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import sun.security.krb5.internal.Ticket;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")

public class Ticket_New extends ListenerAdapter {
    public String BotPrefix = Bot.BotPrefix;
    public String BotName = Bot.BotName;
    public ZonedDateTime LocalTime = ZonedDateTime.now();
    public String ColorHexCode = Bot.ColorHexCode;
    public String BotLogo = Bot.BotLogo;
    public String TicketCreateChannelID = Bot.TicketCreateChannelID;
    public String TicketCreateCategoryChannelID = Bot.TicketCreateCategoryChannelID;

    public String SupportTeamRoleID = Bot.SupportTeamRoleID;
    public String AdminRoleID = Bot.AdminRoleID;
    public String MemberRoleID = Bot.MemberRoleID;

    public boolean StaffPingEnabled = Bot.StaffPingEnabled;


    private String CurrentTicketNumber = "0";
    private ArrayList<String> TicketSubUsersIDs = new ArrayList<String>();
    public static File CurrentTicketNumberFile = new File("Tickets/CurrentTicketCount.txt");


    @Override
    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent e) {

        try {
            JSONObject json = (JSONObject) new JSONParser().parse(new FileReader(new File("Tickets/config.json")));
            BotPrefix = ((HashMap<String, String>) json.get("GeneralConfig")).get("BotPrefix");
            BotName = ((HashMap<String, String>) json.get("GeneralConfig")).get("BotName");
            BotLogo = ((HashMap<String, String>) json.get("GeneralConfig")).get("BotLogo");
            ColorHexCode = ((HashMap<String, String>) json.get("GeneralConfig")).get("ColorHexCode");
            StaffPingEnabled = ((HashMap<String, Boolean>) json.get("GeneralConfig")).get("StaffPingEnabled");

            TicketCreateChannelID = ((HashMap<String, String>) json.get("ChannelIDS")).get("TicketCreateChannelID");
            TicketCreateCategoryChannelID = ((HashMap<String, String>) json.get("ChannelIDS")).get("TicketCreateCategoryChannelID");

            MemberRoleID = ((HashMap<String, String>) json.get("RoleIDS")).get("MemberRoleID");
            SupportTeamRoleID = ((HashMap<String, String>) json.get("RoleIDS")).get("SupportTeamRoleID");
            AdminRoleID = ((HashMap<String, String>) json.get("RoleIDS")).get("AdminRoleID");

        } catch (Exception ee) {
            ee.printStackTrace();
        }
        java.awt.Color Color = java.awt.Color.decode(ColorHexCode);

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        ArrayList<JSONObject> TicketOptions = new ArrayList<>();

        try {
            JSONObject json = (JSONObject) new JSONParser().parse(new FileReader(new File("Tickets/PanelSettings.json")));
            TicketOptions = (ArrayList<JSONObject>) json.get("TicketOptions");

        } catch (Exception ee) {
            ee.printStackTrace();
        }


        for (int i = 0; i < TicketOptions.size(); i++) {

            JSONObject InsideStringTicketObject = new JSONObject();
            String Emoji = "";
            String TicketPrefix = "ticket-";
            boolean AdminOnlyDefault = false;
            boolean StaffPingEnabled = true;
            String CloseTicketTrashEmoji = "🗑️";
            String TicketPanelDescription = "General questions, concerns or any support you need\n";

            ArrayList<String> QuestionFormat = new ArrayList<>();

            InsideStringTicketObject = (JSONObject) TicketOptions.get(i);
            Emoji = (String) TicketOptions.get(i).get("Emoji");
            TicketPrefix = (String) TicketOptions.get(i).get("TicketPrefix");
            CloseTicketTrashEmoji = (String) TicketOptions.get(i).get("CloseTicketTrashEmoji");
            QuestionFormat = (ArrayList<String>) TicketOptions.get(i).get("QuestionFormat");
            AdminOnlyDefault = (boolean) TicketOptions.get(i).get("AdminOnlyDefault");
            StaffPingEnabled = (boolean) TicketOptions.get(i).get("StaffPingEnabled");
            TicketPanelDescription = (String) TicketOptions.get(i).get("TicketPanelDescription");
            if (TicketPrefix.contains("{name}")) {
                TicketPrefix = TicketPrefix.replace("{name}", e.getUser().getName());
            }
            String EmojiWithArrow = "";
            String EmojiWithoutArrow = "";
            String EmojiSimple = "";

            if (Emoji.length() < 3) {
                EmojiWithArrow = Emoji;
                EmojiWithoutArrow = Emoji;
                EmojiSimple = Emoji;
            } else {
                EmojiWithArrow = Emoji;
                EmojiWithoutArrow = Emoji.substring(0, Emoji.length() - 1);
                EmojiSimple = Emoji.substring(2, Emoji.length() - 20);
            }

            if (!e.getMember().getUser().isBot() && e.getReactionEmote().getName().equals(EmojiSimple) && e.getChannel().getId().equals(TicketCreateChannelID)) {
                ArrayList<String> BlacklistedGet = new ArrayList<>();
                if (new File("BlacklistedUsers.json").exists()) {
                    try {
                        JSONObject json = (JSONObject) new JSONParser().parse(new FileReader(new File("BlacklistedUsers.json")));
                        BlacklistedGet = (ArrayList<String>) json.get("BlacklistedUsers");
                    } catch (Exception ee) {
                        ee.printStackTrace();
                    }
                }
                boolean isBlacklisted = false;
                for (String s : BlacklistedGet) {
                    if (e.getUser().getId().equals(s)) {
                        isBlacklisted = true;
                    }
                }
                if (isBlacklisted) {

                    return;
                }
            }
            if (!e.getMember().getUser().isBot() && e.getReactionEmote().getName().equals(EmojiSimple) && e.getChannel().getId().equals(TicketCreateChannelID)) {
                e.getChannel().getHistoryFromBeginning(100).complete().getMessageById(e.getMessageId())
                        .removeReaction(EmojiWithoutArrow, e.getUser()).complete();

                TextChannel NewTicketCreated = e.getGuild().getCategoryById(TicketCreateCategoryChannelID)
                        .createTextChannel(TicketPrefix + CurrentTicketNumber).complete();
                CurrentTicketNumber = Integer.parseInt(CurrentTicketNumber) + 1 + "";
                saveFile();


                if (!new File("Tickets/Storage/" + NewTicketCreated.getId() + ".json").exists()) {
                    try {
                        new File("Tickets/Storage/" + NewTicketCreated.getId() + ".json").createNewFile();

                        JSONObject all = new JSONObject();
                        all.put("ChannelID", NewTicketCreated.getId());
                        all.put("ChannelName", NewTicketCreated.getName());
                        all.put("CategoryID", NewTicketCreated.getParent().getId());
                        all.put("UsersID", e.getUser().getId());
                        all.put("UsersTag", e.getUser().getAsTag());
                        all.put("UsersAvatarURL", e.getUser().getAvatarUrl());
                        if (AdminOnlyDefault) {
                            all.put("AdminOnlyMode", true);

                        } else {
                            all.put("AdminOnlyMode", false);
                        }
                        all.put("SubUsers", TicketSubUsersIDs);

                        try {
                            Files.write(Paths.get("Tickets/Storage/" + NewTicketCreated.getId() + ".json"), all.toJSONString().getBytes());
                        } catch (Exception ef) {
                            ef.printStackTrace();
                        }
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }

                try {
                    NewTicketCreated.putPermissionOverride(e.getMember())
                            .setAllow(Permission.MESSAGE_READ, Permission.MESSAGE_WRITE, Permission.MESSAGE_ATTACH_FILES,
                                    Permission.MESSAGE_EXT_EMOJI, Permission.MESSAGE_EMBED_LINKS, Permission.MESSAGE_HISTORY,
                                    Permission.MESSAGE_ADD_REACTION, Permission.CREATE_INSTANT_INVITE)
                            .setDeny(Permission.MESSAGE_MENTION_EVERYONE, Permission.MESSAGE_MANAGE, Permission.MESSAGE_TTS,
                                    Permission.MANAGE_WEBHOOKS, Permission.MANAGE_PERMISSIONS, Permission.MANAGE_CHANNEL)
                            .queue();
                } catch (Exception cow) {
                    cow.printStackTrace();
                }
                if (!AdminOnlyDefault) {
                    try {
                        NewTicketCreated.putPermissionOverride(e.getGuild().getRoleById(SupportTeamRoleID))
                                .setAllow(Permission.MESSAGE_READ, Permission.MESSAGE_WRITE, Permission.MESSAGE_ATTACH_FILES,
                                        Permission.MESSAGE_EXT_EMOJI, Permission.MESSAGE_EMBED_LINKS, Permission.MESSAGE_HISTORY,
                                        Permission.MESSAGE_ADD_REACTION, Permission.CREATE_INSTANT_INVITE)
                                .setDeny(Permission.MESSAGE_MENTION_EVERYONE, Permission.MESSAGE_MANAGE, Permission.MESSAGE_TTS,
                                        Permission.MANAGE_WEBHOOKS, Permission.MANAGE_PERMISSIONS, Permission.MANAGE_CHANNEL)
                                .queue();
                    } catch (Exception cow) {
                        cow.printStackTrace();
                    }
                }
                try {
                    NewTicketCreated.putPermissionOverride(e.getGuild().getRoleById(AdminRoleID))
                            .setAllow(Permission.MESSAGE_READ, Permission.MESSAGE_WRITE, Permission.MESSAGE_ATTACH_FILES,
                                    Permission.MESSAGE_EXT_EMOJI, Permission.MESSAGE_EMBED_LINKS, Permission.MESSAGE_HISTORY,
                                    Permission.MESSAGE_ADD_REACTION, Permission.CREATE_INSTANT_INVITE, Permission.MESSAGE_MENTION_EVERYONE,
                                    Permission.MANAGE_WEBHOOKS, Permission.MESSAGE_MANAGE)
                            .setDeny(Permission.MESSAGE_TTS, Permission.MANAGE_PERMISSIONS, Permission.MANAGE_CHANNEL)
                            .queue();
                } catch (Exception cow) {
                    cow.printStackTrace();
                }
                try {
                    NewTicketCreated.putPermissionOverride(e.getGuild().getPublicRole())
                            .setDeny(Permission.MESSAGE_READ, Permission.MESSAGE_WRITE, Permission.MESSAGE_ATTACH_FILES,
                                    Permission.MESSAGE_EXT_EMOJI, Permission.MESSAGE_EMBED_LINKS, Permission.MESSAGE_HISTORY,
                                    Permission.MESSAGE_ADD_REACTION, Permission.CREATE_INSTANT_INVITE,
                                    Permission.MESSAGE_MENTION_EVERYONE, Permission.MESSAGE_MANAGE, Permission.MESSAGE_TTS,
                                    Permission.MANAGE_WEBHOOKS, Permission.MANAGE_PERMISSIONS, Permission.MANAGE_CHANNEL)
                            .queue();
                } catch (Exception cow) {
                    cow.printStackTrace();
                }


                EmbedBuilder EmbedPM = new EmbedBuilder()
                        .setTitle("New Ticket from " + e.getMember().getUser().getAsTag())
                        .setThumbnail(e.getUser().getAvatarUrl())
                        .setColor(Color)
                        .setDescription("You created a new ticket in the **" + e.getGuild().getName() + "** discord!\n Click the channel to go to it! <#" + NewTicketCreated.getId() + ">")
                        .setTimestamp(LocalTime);
                PrivateChannel EmbedPMMessage = e.getUser().openPrivateChannel().complete();
                EmbedPMMessage.sendMessage(EmbedPM.build()).complete();


                String QuestionsFormatString = "";
                for (int k = 0; k < QuestionFormat.size(); k++) {
                    QuestionsFormatString += QuestionFormat.get(k) + "\n";
                }


                EmbedBuilder Embed = new EmbedBuilder()
                        .setTitle("New Ticket from " + e.getMember().getUser().getAsTag())
                        .setThumbnail(e.getUser().getAvatarUrl())
                        .setColor(Color)
                        .addField("Please follow the format below:", "```" + QuestionsFormatString + "```", false)
                        .setTimestamp(LocalTime)
                        .setFooter("Ticket From " + e.getUser().getAsTag(), e.getMember().getUser().getAvatarUrl());
                if (StaffPingEnabled) {
                    String Sing = e.getGuild().getRoleById(SupportTeamRoleID).getAsMention() + ", " + e.getMember().getAsMention();
                    NewTicketCreated.sendMessage(Sing).queue();

                } else {
                    NewTicketCreated.sendMessage(e.getMember().getAsMention()).queue();

                }
                Message TopTicket = NewTicketCreated.sendMessage(Embed.build()).complete();
                TopTicket.addReaction(CloseTicketTrashEmoji).queue();

                CloseTicketTrashEmoji = "";
            }

        }

    }


    public void saveFile() {
        try {
            Files.write(Paths.get("Tickets/CurrentTicketCount.txt"),
                    CurrentTicketNumber.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Ticket_New() {
        try {
            Scanner reader = new Scanner(CurrentTicketNumberFile);
            CurrentTicketNumber = reader.nextLine();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}