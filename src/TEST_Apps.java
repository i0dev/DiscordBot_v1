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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;

@SuppressWarnings("unused")
public class TEST_Apps extends ListenerAdapter {
    public String BotPrefix = Bot.BotPrefix;
    public String BotName = Bot.BotName;
    public ZonedDateTime LocalTime = Bot.LocalTime;
    public String ColorHexCode = Bot.ColorHexCode;
    public String BotLogo = Bot.BotLogo;
    public String LogsChannelID = Bot.LogsChannelID;
    public static ArrayList<String> AllowedRoles = new ArrayList<String>();
    public String AdminRoleID = Bot.AdminRoleID;

    public static ArrayList<Object> AllWarnings = new ArrayList<Object>();

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        try {
            JSONObject json = (JSONObject) new JSONParser().parse(new FileReader(new File("Tickets/config.json")));
            BotPrefix = ((HashMap<String, String>) json.get("GeneralConfig")).get("BotPrefix");
            BotName = ((HashMap<String, String>) json.get("GeneralConfig")).get("BotName");
            BotLogo = ((HashMap<String, String>) json.get("GeneralConfig")).get("BotLogo");
            ColorHexCode = ((HashMap<String, String>) json.get("GeneralConfig")).get("ColorHexCode");
            AllowedRoles = ((HashMap<String, ArrayList>) json.get("Extras")).get("AllowedRoles");
            LogsChannelID = ((HashMap<String, String>) json.get("Extras")).get("LogsChannelID");
            AdminRoleID = ((HashMap<String, String>) json.get("RoleIDS")).get("AdminRoleID");

        } catch (Exception ee) {
            ee.printStackTrace();
        }
        Color Color = java.awt.Color.decode(Bot.ColorHexCode);
        MessageChannel channel = e.getChannel();
        String[] message = e.getMessage().getContentRaw().split(" ");


        if (message.length == 1 && message[0].equalsIgnoreCase(Bot.BotPrefix + "epply")) {
            EmbedBuilder EmbedRules = new EmbedBuilder();
            EmbedRules.setTitle("App Started");
            EmbedRules.setColor(Color);
            EmbedRules.addField("gg:", "App started in dms", false);
            EmbedRules.setTimestamp(LocalTime);
            EmbedRules.setFooter(Bot.WaterMark, BotLogo);
            channel.sendMessage(EmbedRules.build()).queue(message1 -> {
                e.getMessage().delete().queue();
            });

        }
        ArrayList<JSONObject> ApplicaitonOptions = new ArrayList<>();

        try {
            JSONObject json = (JSONObject) new JSONParser().parse(new FileReader(new File("Tickets/PanelSettings.json")));
            ApplicaitonOptions = (ArrayList<JSONObject>) json.get("TicketOptions");

        } catch (Exception ee) {
            ee.printStackTrace();
        }


    }
}