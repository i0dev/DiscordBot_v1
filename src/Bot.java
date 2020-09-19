import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.security.auth.login.LoginException;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class Bot {

    public static JDA jda;

    public static long ApplicationTimeoutTimeSECONDS = 300;
    public static String ConfirmedFactionChannelID = "";
    public static String ColorHexCode = "#d66058";
    public static String BotLogo = "";
    public static String BotToken = "";
    public static String WaterMark = "";
    public static String BotActivity = "";
    public static String BotName = "";
    public static OnlineStatus BotStatus = OnlineStatus.DO_NOT_DISTURB;
    public static String BotPrefix = "";
    public static ZonedDateTime LocalTime = ZonedDateTime.now();
    public static String ServerIP = "";
    public static ArrayList<String> AllowedRoles = new ArrayList<String>();
    public static ArrayList<String> AllowedRolesDEFAULT = new ArrayList<String>();
    public static String LogsChannelID = "";
    public static String VisitorRoleID = "";
    public static String MutedRoleID = "";
    public static String MemberCountVoiceChannelID = "";
    public static String WelcomeMessageChannelID = "";
    public static String GiveawayChannelID = "";
    public static ZonedDateTime now = LocalTime;
    public static String Logo = BotLogo;
    public static String PendingRoleID = "";
    public static String GuildID = "";
    public static String ApplicationChannelID = "";
    public static String Welcome_Message_Header = "";
    public static String Welcome_Message_Base = "";
    public static String Welcome_Message_Title = "";

    public static String Verify_ChannelID = "";
    public static String Verify_EmbedDescription = "";
    public static String Verify_RoleToGiveID = "";

    public static boolean StaffPingEnabled;
    public static String TicketCreateChannelID = "";
    public static String TicketCreateCategoryChannelID = "";
    public static String SuggestionChannelID = "";

    public static String SupportTeamRoleID = "";
    public static String AdminRoleID = "";
    public static ArrayList<String> ApplicationQuestions = new ArrayList<>();
    public static String MemberRoleID = "";

    public static String[] BlockedWordsList = {"nigger", "Faggot", "n1gger", "n1g3r", "nigga", "n1gga", "niga"};

    public static void main(String args[]) throws Exception {

        System.out.println("");
        System.out.println("");
        System.out.println("-------------------------------");
        System.out.println("|    Now Loading i0dev.com    |");
        System.out.println("| Done! i0dev.com Operational |");
        System.out.println("-------------------------------");
        System.out.println("");
        System.out.println("");

        if (!new File("Applications.json").exists()) {
            new File("Applications.json").createNewFile();
        }
        if (!new File("BlacklistedUsers.json").exists()) {
            new File("BlacklistedUsers.json").createNewFile();
        }
        if (!new File("config.json").exists()) {
            new File("config.json").createNewFile();
            loadDefaults();
            loadStorage();
        } else {
            loadStorage();
        }
        if (!new File("Tickets").exists()) {
            new File("Tickets").mkdirs();
            loadDefaults();
        }
        if (!new File("Tickets/Storage").exists()) {
            new File("Tickets/Storage").mkdirs();
        }
        if (!new File("Warnings.json").exists()) {
            new File("Warnings.json").createNewFile();
        }
        if (!new File("Tickets/Logs").exists()) {
            new File("Tickets/Logs").mkdirs();
        }
        if (!new File("Tickets/PanelSettings.json").exists()) {
            new File("Tickets/PanelSettings.json").createNewFile();
            loadPanelDefaults();
        }

        if (!new File("Tickets/config.json").exists()) {
            new File("Tickets/config.json").createNewFile();
            loadTicketDefaults();
        }


        if (!new File("Tickets/CurrentTicketCount.txt").exists()) {
            new File("Tickets/CurrentTicketCount.txt").createNewFile();
            try {
                String CurrentTicketNumber = "0";
                Files.write(Paths.get("Tickets/CurrentTicketCount.txt"),
                        CurrentTicketNumber.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            JFrame frame = new JFrame(BotName);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
            frame.setBounds(500, 200, 500, 500);
            frame.setBackground(Color.BLUE);
            frame.setTitle(BotName + " Bot");
            frame.add(new JLabel(BotName + " Bot"), JLabel.CENTER);
        } catch (Exception e) {
            System.out.println("");
            System.out.println("");
            System.out.println("-------------------------------");
            System.out.println("|      J-Frame disabling!     |");
            System.out.println("|  This version of OS doesn't |");
            System.out.println("|   support a java j-frame!   |");
            System.out.println("-------------------------------");
            System.out.println("");
            System.out.println("");
        }
        try {
            jda = new JDABuilder(BotToken).setActivity(Activity.playing(BotActivity)).setStatus(BotStatus).build();
        } catch (LoginException e) {
            e.printStackTrace();
        }

        //  jda.addEventListener(new Applications_New());
        jda.addEventListener(new Application_Accept());
        jda.addEventListener(new Application_Deny());
        jda.addEventListener(new Application_Review());
        jda.addEventListener(new Application_List());

        jda.addEventListener(new Event_Welcome());

        jda.addEventListener(new Command_Announcements());
        jda.addEventListener(new Command_IP());

        jda.addEventListener(new Command_Mute());
        // jda.addEventListener(new Command_TempMute());
        jda.addEventListener(new Command_UnMute());

        jda.addEventListener(new Command_Ban());
        jda.addEventListener(new Command_Kick());
        jda.addEventListener(new Command_Unban());


        //eXTRAS
        jda.addEventListener(new Auto_Mod());
        jda.addEventListener(new Command_MemberCount());
        jda.addEventListener(new Command_Prune());
        jda.addEventListener(new Command_Say());
        jda.addEventListener(new Command_RoleInfo());
        jda.addEventListener(new Command_ServerInfo());
        jda.addEventListener(new Command_WhoIs());
        jda.addEventListener(new Command_Roles());
        jda.addEventListener(new Command_Avatar());

        jda.addEventListener(new Command_GetMuted());
        //   jda.addEventListener(new Command_Help());
        jda.addEventListener(new Command_Invites());
        jda.addEventListener(new Command_Members());

        jda.addEventListener(new Extra_Verify());

        jda.addEventListener(new Ticket_Panel());
        jda.addEventListener(new Ticket_New());
        jda.addEventListener(new Ticket_AdminOnly());
        jda.addEventListener(new Ticket_Rename());
        jda.addEventListener(new Ticket_Help());
        jda.addEventListener(new Ticket_Add());
        jda.addEventListener(new Ticket_Close());
        jda.addEventListener(new Ticket_Remove());
        jda.addEventListener(new Ticket_Info());
        jda.addEventListener(new Extra_Staff());

        jda.addEventListener(new Extra_GetWarns());
        jda.addEventListener(new Extra_Warn());

        jda.addEventListener(new Extra_Promote());
        jda.addEventListener(new Extra_Demote());
        jda.addEventListener(new Extra_Resign());

        jda.addEventListener(new Extra_Suggest());
        jda.addEventListener(new Extra_FacLEader());
        jda.addEventListener(new Extra_BugReport());

        jda.addEventListener(new TEST_Apps());
        jda.addEventListener(new TEST_Help());
        jda.addEventListener(new Extra_Assign());
        jda.addEventListener(new Extra_StaffClear());
        jda.addEventListener(new Extra_Confirm());

        jda.addEventListener(new Fun_Slap());
        jda.addEventListener(new Fun_8Ball());
        jda.addEventListener(new Fun_CoinFlip());
        jda.addEventListener(new Fun_Dice());
        jda.addEventListener(new Fun_Hi());
        jda.addEventListener(new Fun_Pat());
        jda.addEventListener(new TEST_Polls());

        jda.addEventListener(new Extra_UnBlacklist());
        jda.addEventListener(new Extra_GetBlacklisted());
        jda.addEventListener(new Extra_Blacklist());
        jda.addEventListener(new Command_Clear());

    }

    public static void loadPanelDefaults() {

        ArrayList<JSONObject> TicketOptions = new ArrayList<JSONObject>();
        ArrayList<String> QuestionFormat = new ArrayList<>();
        ArrayList<String> QuestionFormat2 = new ArrayList<>();

        JSONObject InsideStringTicketObject = new JSONObject();
        InsideStringTicketObject.put("Emoji", "<:ffff:747892948187873431>");
        InsideStringTicketObject.put("TicketPrefix", "general-");
        InsideStringTicketObject.put("CloseTicketTrashEmoji", "\uD83D\uDDD1"); //🗑
        QuestionFormat.add("What is your question?");
        QuestionFormat.add("What is your IGN");
        InsideStringTicketObject.put("QuestionFormat", QuestionFormat);
        InsideStringTicketObject.put("AdminOnlyDefault", false);
        InsideStringTicketObject.put("StaffPingEnabled", true);
        InsideStringTicketObject.put("TicketPanelDescription", "General questions, concerns or any support you need");
        InsideStringTicketObject.put("TicketPanelTitle", "General Ticket");
        JSONObject InsideStringTicketObject2 = new JSONObject();
        InsideStringTicketObject2.put("Emoji", "<:i01:750090634278338610>");
        InsideStringTicketObject2.put("TicketPrefix", "admin-");
        InsideStringTicketObject2.put("CloseTicketTrashEmoji", "\uD83D\uDDD1"); //🗑
        QuestionFormat2.add("What is your question?");
        QuestionFormat2.add("What is your IGN");
        InsideStringTicketObject2.put("QuestionFormat", QuestionFormat2);
        InsideStringTicketObject2.put("AdminOnlyDefault", true);
        InsideStringTicketObject2.put("StaffPingEnabled", true);
        InsideStringTicketObject2.put("TicketPanelDescription", "Report Players for malicious intent here.");
        InsideStringTicketObject2.put("TicketPanelTitle", "Admin Ticket");
        TicketOptions.add(InsideStringTicketObject);
        TicketOptions.add(InsideStringTicketObject2);
        JSONObject all = new JSONObject();
        all.put("TicketOptions", TicketOptions);
        try {
            Files.write(Paths.get("Tickets/PanelSettings.json"), all.toJSONString().getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        TicketOptions.clear();
    }

    public static void loadDefaults() {
        JSONObject all = new JSONObject();
        all.put("ColorHexCode", "#4287f5");
        all.put("BotPrefix", ".");
        all.put("BotLogo", "https://cdn.discordapp.com/attachments/667207717882036234/746181096995291156/i01.png");
        all.put("BotToken", "");
        all.put("BotName", "i0dev");
        all.put("BotActivity", "On Vacation");
        all.put("ServerIP", "play.i01.com");
        all.put("LogsChannelID", "748915292767518830");
        AllowedRolesDEFAULT.add("748918917854134404");
        AllowedRolesDEFAULT.add("748918918889865267");
        all.put("AllowedRoles", AllowedRolesDEFAULT);
        all.put("MutedRoleID", "748916437527167097");
        all.put("VisitorRoleID", "748916438017769583");
        all.put("MemberCountVoiceChannelID", "748916637289414676");
        all.put("WelcomeMessageChannelID", "748916684366020669");
        all.put("GiveawayChannelID", "748919695184363601");
        all.put("ApplicationChannelID", "748991769076695231");
        all.put("GuildID", "743277096327053316");
        all.put("PendingRoleID", "749014680613290058");
        all.put("WaterMark", "© i0dev.com");
        all.put("Welcome_Message_Header", "Use .apply in #apply-here");
        all.put("Welcome_Message_Base", "**Current Server: {ServerIP}\nMember Count: {memberCount}**");
        all.put("ApplicationTimeoutTimeSECONDS", 300);
        all.put("Welcome_Message_Title", "hey {user}, Welcome to the smoke discord");
        all.put("Verify_EmbedDescription", "To ensure a safe and mutually beneficial experience," +
                " all users are required to verify themselves as an actual human. " +
                "It is your responsibility as a client to read all the rules. Once you agree " +
                "to them you will be bound by them for as long as you are in this server. " +
                "When you are done reading them, select the reaction at the bottom to acknowledge" +
                " and agree to these terms, which will grant you access to the rest of the server.");
        all.put("Verify_ChannelID", "749014680613290058");
        all.put("Verify_RoleToGiveID", "749014680613290058");

        try {
            Files.write(Paths.get("config.json"), all.toJSONString().getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadTicketDefaults() {
        JSONObject RoleIDS = new JSONObject();
        RoleIDS.put("SupportTeamRoleID", "744039663995584562");
        RoleIDS.put("AdminRoleID", "747864895483019324");
        RoleIDS.put("MemberRoleID", "744039668546535544");
        RoleIDS.put("FacLeaderRoleID", "755805707881938965");
        JSONObject ChannelIDS = new JSONObject();
        ChannelIDS.put("TicketLogsChannelID", "744580370044813362");
        ChannelIDS.put("SuggestionChannelID", "751860128055165008");
        ChannelIDS.put("StaffMovementsChannelID", "751873062026346506");
        ChannelIDS.put("AdminLogsChannelID", "744580384423149734");
        ChannelIDS.put("ConfirmedFactionChannelID", "756166636402245763");
        ChannelIDS.put("BugReportChannelID", "751876990763794593");
        ChannelIDS.put("TicketCreateChannelID", "743324430067040396");
        ChannelIDS.put("TicketCreateCategoryChannelID", "744580337010475099");
        JSONObject GeneralConfig = new JSONObject();
        ArrayList<String> StaffRoleIDS = new ArrayList<String>();
        StaffRoleIDS.add("751874809314541748");
        StaffRoleIDS.add("751874807087235124");
        GeneralConfig.put("StaffRoleIDS", StaffRoleIDS);
        ArrayList<String> SuggestionGameModesDEFAULT = new ArrayList<String>();
        SuggestionGameModesDEFAULT.add("Factions");
        SuggestionGameModesDEFAULT.add("SkyBlock");
        GeneralConfig.put("SuggestionGameModes", SuggestionGameModesDEFAULT);
        GeneralConfig.put("StaffPingEnabled", true);
        GeneralConfig.put("BotPrefix", ".");
        GeneralConfig.put("TicketPanelOverAllDescription", "Click to one of the emojis to create a corresponding ticket!");
        GeneralConfig.put("BotToken", "");
        GeneralConfig.put("BotName", "i0dev");
        GeneralConfig.put("BotActivity", "i0dev.com | .help");
        GeneralConfig.put("BotLogo", "https://cdn.discordapp.com/attachments/667207717882036234/746181096995291156/i01.png");
        GeneralConfig.put("ColorHexCode", "#4287f5");
        GeneralConfig.put("TicketCount", 0);
        ArrayList<String> AppQuestionsDefault = new ArrayList<String>();
        AppQuestionsDefault.add("What is your age");
        AppQuestionsDefault.add("What is your name");
        AppQuestionsDefault.add("What is your IGN");
        AppQuestionsDefault.add("What is your Favorite Candy");
        GeneralConfig.put("StaffRoleIDS", AppQuestionsDefault);
        ArrayList<String> AllowedRolesDEFAULT = new ArrayList<String>();

        JSONObject Extras = new JSONObject();
        AllowedRolesDEFAULT.add("748918917854134404");
        AllowedRolesDEFAULT.add("748918918889865267");
        Extras.put("AllowedRoles", AllowedRolesDEFAULT);
        Extras.put("LogsChannelID", "748915292767518830");

        JSONObject all = new JSONObject();
        all.put("Extras", Extras);
        all.put("GeneralConfig", GeneralConfig);
        all.put("RoleIDS", RoleIDS);
        all.put("ChannelIDS", ChannelIDS);

        try {
            Files.write(Paths.get("Tickets/config.json"), all.toJSONString().getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadStorage() {
        try {
            JSONObject json = (JSONObject) new JSONParser().parse(new FileReader(new File("config.json")));

            BotPrefix = (String) json.get("BotPrefix");
            BotActivity = (String) json.get("BotActivity");
            BotLogo = (String) json.get("BotLogo");
            Logo = (String) json.get("BotLogo");
            BotName = (String) json.get("BotName");
            BotToken = (String) json.get("BotToken");
            ServerIP = (String) json.get("ServerIP");
            LogsChannelID = (String) json.get("LogsChannelID");
            AllowedRoles = (ArrayList<String>) json.get("AllowedRoles");
            MutedRoleID = (String) json.get("MutedRoleID");
            VisitorRoleID = (String) json.get("VisitorRoleID");
            MemberCountVoiceChannelID = (String) json.get("MemberCountVoiceChannelID");
            WelcomeMessageChannelID = (String) json.get("WelcomeMessageChannelID");
            ColorHexCode = (String) json.get("ColorHexCode");
            GiveawayChannelID = (String) json.get("GiveawayChannelID");
            ApplicationChannelID = (String) json.get("ApplicationChannelID");
            GuildID = (String) json.get("GuildID");
            PendingRoleID = (String) json.get("PendingRoleID");
            WaterMark = (String) json.get("WaterMark");
            Welcome_Message_Base = (String) json.get("Welcome_Message_Base");
            Welcome_Message_Header = (String) json.get("Welcome_Message_Header");
            Welcome_Message_Title = (String) json.get("Welcome_Message_Title");
            ApplicationTimeoutTimeSECONDS = (long) json.get("ApplicationTimeoutTimeSECONDS");
            Verify_RoleToGiveID = (String) json.get("Verify_RoleToGiveID");
            Verify_ChannelID = (String) json.get("Verify_ChannelID");
            Verify_EmbedDescription = (String) json.get("Verify_EmbedDescription");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            JSONObject json = (JSONObject) new JSONParser().parse(new FileReader(new File("Tickets/config.json")));

            SupportTeamRoleID = ((HashMap<String, String>) json.get("RoleIDS")).get("SupportTeamRoleID");
            ConfirmedFactionChannelID = ((HashMap<String, String>) json.get("ChannelIDS")).get("ConfirmedFactionChannelID");
            ApplicationQuestions = ((HashMap<String, ArrayList>) json.get("GeneralConfig")).get("ApplicationQuestions");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}