import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.net.IDN;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")

public class Ticket_Rename extends ListenerAdapter {

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

    public String ChannelID = "Default";
    public String ChannelName = "Default";
    public String CategoryID = "Default";
    public String UsersID = "Default";
    public String UsersTag = "Default";
    public String UsersAvatarURL = "Default";
    public boolean AdminOnlyMode = false;
    public ArrayList<String> SubUsers = new ArrayList<String>();

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
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
        MessageChannel channel = e.getChannel();
        GuildChannel permChan = e.getChannel();
        Role supportRole = e.getGuild().getRoleById(SupportTeamRoleID);
        String[] messageWithSplit = e.getMessage().getContentRaw().split(" ");
        if (new File("Tickets/Storage/" + e.getChannel().getId() + ".json").exists()) {

            try {
                JSONObject json = (JSONObject) new JSONParser().parse(new FileReader(new File("Tickets/Storage/" + e.getChannel().getId() + ".json")));
                ChannelID = (String) json.get("ChannelID");
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        }

        if (messageWithSplit[0].equalsIgnoreCase(BotPrefix + "rename") && ChannelID.length() < 9) {

            EmbedBuilder EmbedRules = new EmbedBuilder();

            EmbedRules.setTitle("Error");
            EmbedRules.setDescription("You can only use this command in a ticket");

            EmbedRules.setColor(Color);
            EmbedRules.setTimestamp(LocalTime);
            EmbedRules.setFooter("Request From " + e.getAuthor().getAsTag(), BotLogo);
            channel.sendMessage(EmbedRules.build()).queue(message1 -> {
                e.getMessage().delete().queueAfter(3, TimeUnit.SECONDS);
            });

        } else {


            if (messageWithSplit.length == 1 && messageWithSplit[0].equalsIgnoreCase(BotPrefix + "rename")) {
                EmbedBuilder Embed = new EmbedBuilder();
                Embed.setTitle("Incorrect Format");
                Embed.addField("Format:", "**" + BotPrefix + "rename [name]**", false);
                Embed.setColor(Color);
                Embed.setTimestamp(LocalTime);
                Embed.setFooter("Request From " + e.getAuthor().getAsTag(), BotLogo);
                channel.sendMessage(Embed.build()).queue(message1 -> {
                    e.getMessage().delete().queue();
                });
            }
            if (messageWithSplit.length > 1 && messageWithSplit[0].equalsIgnoreCase(BotPrefix + "rename")) {
                try {
                    JSONObject json = (JSONObject) new JSONParser().parse(new FileReader(new File("Tickets/Storage/" + e.getChannel().getId() + ".json")));
                    ChannelID = (String) json.get("ChannelID");
                    ChannelName = (String) json.get("ChannelName");
                    CategoryID = (String) json.get("CategoryID");
                    UsersID = (String) json.get("UsersID");
                    UsersTag = (String) json.get("UsersTag");
                    UsersAvatarURL = (String) json.get("UsersAvatarURL");
                    AdminOnlyMode = (boolean) json.get("AdminOnlyMode");
                    SubUsers = (ArrayList<String>) json.get("SubUsers");

                } catch (Exception ee) {
                    ee.printStackTrace();
                }
                String newTicketName = "";


                for (int i = 1; i < messageWithSplit.length; i++) {
                    newTicketName += messageWithSplit[i] + "-";
                }
                newTicketName = newTicketName.substring(0, newTicketName.length() - 1);

                String oldChannelName = channel.getName();
                String ticketID = oldChannelName;
                String IDNUMBER = "";
                String TicketNameNoNum = "";
                for (int k = 0; k < oldChannelName.length(); k++) {
                    char Char = oldChannelName.charAt(k);
                    String SChar = Char + "";
                    ArrayList myIntArray = new ArrayList();
                    myIntArray.add("0");
                    myIntArray.add("1");
                    myIntArray.add("2");
                    myIntArray.add("3");
                    myIntArray.add("4");
                    myIntArray.add("5");
                    myIntArray.add("6");
                    myIntArray.add("7");
                    myIntArray.add("8");
                    myIntArray.add("9");

                    if (myIntArray.contains(SChar)) {
                        IDNUMBER = IDNUMBER + SChar;
                    } else {
                        TicketNameNoNum = TicketNameNoNum + SChar;
                    }
                }

                String FinalTicketOutPut = newTicketName + "-" + IDNUMBER;
                FinalTicketOutPut = FinalTicketOutPut.toLowerCase();

                EmbedBuilder EmbedRules = new EmbedBuilder();
                EmbedRules.setTitle("Ticket Rename");
                EmbedRules.setColor(Color);
                EmbedRules.setThumbnail(UsersAvatarURL);
                EmbedRules.addField("Ticket ID:",
                        "**" + e.getChannel().getName() + " was changed to " + FinalTicketOutPut + "**",
                        false);
                EmbedRules.setTimestamp(LocalTime);
                EmbedRules.setFooter("Changed By " + e.getAuthor().getAsTag(), e.getMember().getUser().getAvatarUrl());
                channel.sendMessage(EmbedRules.build()).queue(message1 -> {
                    e.getMessage().delete().queue();
                });
                System.out.println(newTicketName + "-" + ticketID);
                e.getChannel().getManager().setName(FinalTicketOutPut).queue();

                JSONObject all = new JSONObject();
                all.put("ChannelID", ChannelID);
                all.put("ChannelName", FinalTicketOutPut);
                all.put("CategoryID", CategoryID);
                all.put("UsersID", UsersID);
                all.put("UsersTag", UsersTag);
                all.put("UsersAvatarURL", UsersAvatarURL);
                all.put("AdminOnlyMode", AdminOnlyMode);
                all.put("SubUsers", SubUsers);


                try {
                    Files.write(Paths.get("Tickets/Storage/" + ChannelID + ".json"), all.toJSONString().getBytes());
                } catch (Exception ef) {
                    ef.printStackTrace();
                }

            }
        }

    }
}