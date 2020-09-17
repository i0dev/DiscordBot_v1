import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;

@SuppressWarnings("unused")
public class Ticket_Panel extends ListenerAdapter {
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
    public String TicketPanelOverAllDescription = "";
    public boolean StaffPingEnabled = Bot.StaffPingEnabled;

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
            TicketPanelOverAllDescription = ((HashMap<String, String>) json.get("GeneralConfig")).get("TicketPanelOverAllDescription");

        } catch (Exception ee) {
            ee.printStackTrace();
        }
        java.awt.Color Color = java.awt.Color.decode(ColorHexCode);

        if (e.getMessage().getContentRaw().equalsIgnoreCase(BotPrefix + "ticket_panel") &&
                e.getMember().getPermissions().contains(Permission.ADMINISTRATOR)) {
            EmbedBuilder Embed = new EmbedBuilder();


            ArrayList<JSONObject> TicketOptions = new ArrayList<>();

            try {
                JSONObject json = (JSONObject) new JSONParser().parse(new FileReader(new File("Tickets/PanelSettings.json")));
                TicketOptions = (ArrayList<JSONObject>) json.get("TicketOptions");

            } catch (Exception ee) {
                ee.printStackTrace();
            }

            MessageChannel channel = e.getChannel();

            if (TicketPanelOverAllDescription.length() > 0) {
                Embed.setDescription(TicketPanelOverAllDescription);
            }
            Embed.setTitle(BotName + " Tickets")
                    .setThumbnail(BotLogo)
                    .setColor(Color)
                    .setFooter(BotName + " Tickets", BotLogo)
                    .setTimestamp(LocalTime);

            for (int i = 0; i < TicketOptions.size(); i++) {
                String TicketPanelDescription = "General questions, concerns or any support you need";

                JSONObject InsideStringTicketObject = new JSONObject();
                String TicketPanelTitle = "General Ticket";

                String Emoji = (String) TicketOptions.get(i).get("Emoji");
                TicketPanelDescription = (String) TicketOptions.get(i).get("TicketPanelDescription");
                TicketPanelTitle = (String) TicketOptions.get(i).get("TicketPanelTitle");

                String EmojiWithArrow = Emoji;
                Embed.addField(EmojiWithArrow + " " + TicketPanelTitle, TicketPanelDescription, false);


            }

            Message embeds = channel.sendMessage(Embed.build()).complete();
            e.getMessage().delete().queue();
            embeds.pin().queue();
            for (int i = 0; i < TicketOptions.size(); i++) {

                JSONObject InsideStringTicketObject = new JSONObject();

                String Emoji = (String) TicketOptions.get(i).get("Emoji");
                String EmojiWithoutArrow = "";
                if (Emoji.length() < 3) {
                    EmojiWithoutArrow = Emoji;
                } else {
                    EmojiWithoutArrow = Emoji.substring(0, Emoji.length() - 1);

                }

                embeds.addReaction(EmojiWithoutArrow).queue();
                Emoji = "";
                EmojiWithoutArrow = "";
            }


        }
    }

}