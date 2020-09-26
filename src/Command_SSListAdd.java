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
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
public class Command_SSListAdd extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {

        Color Color = java.awt.Color.decode(Bot.ColorHexCode);
        MessageChannel channel = e.getChannel();
        String[] message = e.getMessage().getContentRaw().split(" ");
        if (message.length > 0 && message[0].equalsIgnoreCase(Bot.BotPrefix + "ss")) {
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
                if (e.getAuthor().getId().equals(s)) {
                    isBlacklisted = true;
                }
            }
            if (isBlacklisted) {
                EmbedBuilder UserBlacklisted = new EmbedBuilder().setTitle("Error").setThumbnail(Bot.BotLogo).setFooter(Bot.WaterMark, Bot.BotLogo).setTimestamp(Bot.now).setColor(Color.RED).setDescription("**" + e.getAuthor().getAsTag() + "**, *You are blacklisted from using all commands, \n" + "If you think this is an error please contact a staff member!*");
                e.getChannel().sendMessage(UserBlacklisted.build()).queue(message3 -> {
                    e.getMessage().delete().queue();
                    message3.addReaction("❌").queue();
                    message3.delete().queueAfter(10, TimeUnit.SECONDS);
                });
                return;
            }
        }
        boolean isAllowed = false;
        for (int i = 0; i < Bot.AllowedRoles.size(); i++) {
            if (e.getMember().getRoles().contains(e.getGuild().getRoleById(Bot.AllowedRoles.get(i))) || e.getMember().getPermissions().contains(Permission.ADMINISTRATOR)) {
                isAllowed = true;
            }
        }
        for (int i = 0; i < Bot.LightAllowedRoleIDS.size(); i++) {
            if (e.getMember().getRoles().contains(e.getGuild().getRoleById(Bot.LightAllowedRoleIDS.get(i))) || e.getMember().getPermissions().contains(Permission.ADMINISTRATOR)) {
                isAllowed = true;
            }
        }

        if (message.length == 1 && message[0].equalsIgnoreCase(Bot.BotPrefix + "ss")) {
            if (isAllowed) {
                EmbedBuilder EmbedRules = new EmbedBuilder();
                EmbedRules.setTitle("Incorrect Format");
                EmbedRules.setColor(Color);
                EmbedRules.addField("Format:", "" + Bot.BotPrefix + "ss add [Ign] [reason]", false);
                EmbedRules.addField("Format:", "" + Bot.BotPrefix + "ss remove [Ign]", false);
                EmbedRules.addField("Format:", "" + Bot.BotPrefix + "ss list", false);
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
        if (message.length == 2 && message[0].equalsIgnoreCase(Bot.BotPrefix + "ss") && message[1].equalsIgnoreCase("add")) {
            if (isAllowed) {
                EmbedBuilder EmbedRules = new EmbedBuilder();
                EmbedRules.setTitle("Incorrect Format");
                EmbedRules.setColor(Color);
                EmbedRules.addField("Format:", "" + Bot.BotPrefix + "ss add [Ign] [reason]", false);
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
        if (message.length == 2 && message[0].equalsIgnoreCase(Bot.BotPrefix + "ss") && message[1].equalsIgnoreCase("remove")) {
            if (isAllowed) {
                EmbedBuilder EmbedRules = new EmbedBuilder();
                EmbedRules.setTitle("Incorrect Format");
                EmbedRules.setColor(Color);
                EmbedRules.addField("Format:", "" + Bot.BotPrefix + "ss remove [Ign]", false);
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
        if (message.length == 2 && message[0].equalsIgnoreCase(Bot.BotPrefix + "ss") && message[1].equalsIgnoreCase("list")) {
            if (isAllowed) {
                ArrayList<JSONObject> ArrayOfTheJSONObjects = new ArrayList<>();
                try {
                    JSONObject json = (JSONObject) new JSONParser().parse(new FileReader(new File("SSList.json")));
                    ArrayOfTheJSONObjects = (ArrayList<JSONObject>) json.get("ListOfSS");
                } catch (Exception ee) {
                    ee.printStackTrace();
                }

                EmbedBuilder EmbedRules = new EmbedBuilder();
                EmbedRules.setTitle("List of users on the SS List");
                EmbedRules.setColor(Color);

                if (ArrayOfTheJSONObjects.size() == 0) {
                    EmbedRules.addField("Error", "There is currently no one on the SS list!", false);

                }
                for (int i = 0; i < ArrayOfTheJSONObjects.size(); i++) {
                    EmbedRules.addField("", "**IGN:** `" + ArrayOfTheJSONObjects.get(i).get("SSUserIGN").toString() + "`\n**Reason:** `" + ArrayOfTheJSONObjects.get(i).get("SSUserReason").toString() + "`\n**Staff Member Who Added:** `" + ArrayOfTheJSONObjects.get(i).get("StaffMemberWhoAddedTag").toString() + "`", false);
                }

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
        if (message.length == 3 && message[0].equalsIgnoreCase(Bot.BotPrefix + "ss") && message[1].equalsIgnoreCase("add")) {
            if (isAllowed) {
                EmbedBuilder EmbedRules = new EmbedBuilder();
                EmbedRules.setTitle("Incorrect Format");
                EmbedRules.setColor(Color);
                EmbedRules.addField("Format:", "" + Bot.BotPrefix + "ss add [Ign] [Reason]", false);
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
        if (message.length > 3 && message[0].equalsIgnoreCase(Bot.BotPrefix + "ss") && message[1].equalsIgnoreCase("add")) {
            if (isAllowed) {
                String reason = " ";
                for (int k = 3; k < message.length; k++) {
                    reason = reason + message[k] + " ";
                }
                reason = reason.substring(1, reason.length() - 1);

                ArrayList<JSONObject> ArrayOfTheJSONObjects = new ArrayList<>();
                try {
                    JSONObject json = (JSONObject) new JSONParser().parse(new FileReader(new File("SSList.json")));
                    ArrayOfTheJSONObjects = (ArrayList<JSONObject>) json.get("ListOfSS");
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
                boolean isAlreadyOnList = false;
                for (int i = 0; i < ArrayOfTheJSONObjects.size(); i++) {
                    if (ArrayOfTheJSONObjects.get(i).get("SSUserIGN").toString().equalsIgnoreCase(message[2])) {
                        isAlreadyOnList = true;
                    }
                }
                if (isAlreadyOnList) {
                    EmbedBuilder EmbedRules = new EmbedBuilder();
                    EmbedRules.setTitle("Error when trying to add a IGN to the SS list");
                    EmbedRules.setColor(Color);
                    EmbedRules.addField(e.getAuthor().getAsTag(), "`" + message[2] + "` is already on the SS List! do `" + Bot.BotPrefix + "ss list` to see the current people on the SS List!", false);
                    EmbedRules.setTimestamp(Bot.now);
                    EmbedRules.setFooter(Bot.WaterMark, Bot.Logo);
                    channel.sendMessage(EmbedRules.build()).queue(message1 -> {
                        e.getMessage().delete().queue();
                    });
                } else {

                    JSONObject toFile = new JSONObject();
                    toFile.put("StaffMemberWhoAddedName", e.getAuthor().getName());
                    toFile.put("StaffMemberWhoAddedID", e.getAuthor().getId());
                    toFile.put("StaffMemberWhoAddedTag", e.getAuthor().getAsTag());
                    toFile.put("SSUserIGN", message[2]);
                    toFile.put("SSUserReason", reason);
                    ArrayOfTheJSONObjects.add(toFile);


                    JSONObject all = new JSONObject();
                    all.put("ListOfSS", ArrayOfTheJSONObjects);

                    try {
                        Files.write(Paths.get("SSList.json"), all.toJSONString().getBytes());
                    } catch (Exception ef) {
                        ef.printStackTrace();
                    }

                    EmbedBuilder EmbedRules = new EmbedBuilder();
                    EmbedRules.setTitle("Successfully Added To The SS List");
                    EmbedRules.setColor(Color);
                    EmbedRules.addField(e.getAuthor().getAsTag(), "You have added `" + message[2] + "` to the SS list, with the reason being: **" + reason + "**", false);
                    EmbedRules.setTimestamp(Bot.now);
                    EmbedRules.setFooter(Bot.WaterMark, Bot.Logo);
                    channel.sendMessage(EmbedRules.build()).queue(message1 -> {
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
        if (message.length == 3 && message[0].equalsIgnoreCase(Bot.BotPrefix + "ss") && message[1].equalsIgnoreCase("remove")) {
            if (isAllowed) {
                ArrayList<JSONObject> ArrayOfTheJSONObjects = new ArrayList<>();
                try {
                    JSONObject json = (JSONObject) new JSONParser().parse(new FileReader(new File("SSList.json")));
                    ArrayOfTheJSONObjects = (ArrayList<JSONObject>) json.get("ListOfSS");
                } catch (Exception ee) {
                    ee.printStackTrace();
                }

                boolean removedAUser = false;
                for (int i = 0; i < ArrayOfTheJSONObjects.size(); i++) {
                    String CurrentIGN = ArrayOfTheJSONObjects.get(i).get("SSUserIGN").toString();

                    if (message[2].equalsIgnoreCase(CurrentIGN)) {
                        removedAUser = true;
                        ArrayOfTheJSONObjects.remove(i);
                    }

                }

                if (removedAUser) {
                    EmbedBuilder EmbedRules = new EmbedBuilder();
                    EmbedRules.setTitle("Successfully Removed From The SS List");
                    EmbedRules.setColor(Color);
                    EmbedRules.addField(e.getAuthor().getAsTag(), "You have removed `" + message[2] + "` from the SS list!", false);
                    EmbedRules.setTimestamp(Bot.now);
                    EmbedRules.setFooter(Bot.WaterMark, Bot.Logo);
                    channel.sendMessage(EmbedRules.build()).queue(message1 -> {
                        e.getMessage().delete().queue();
                    });
                } else {
                    EmbedBuilder EmbedRules = new EmbedBuilder();
                    EmbedRules.setTitle("Error when removing a member from the SS List");
                    EmbedRules.setColor(Color);
                    EmbedRules.addField(e.getAuthor().getAsTag(), "`" + message[2] + "` is not on the SS list! do `" + Bot.BotPrefix + "ss list` to see the current people on the SS List!", false);
                    EmbedRules.setTimestamp(Bot.now);
                    EmbedRules.setFooter(Bot.WaterMark, Bot.Logo);
                    channel.sendMessage(EmbedRules.build()).queue(message1 -> {
                        e.getMessage().delete().queue();
                    });
                }


                JSONObject all = new JSONObject();
                all.put("ListOfSS", ArrayOfTheJSONObjects);

                try {
                    Files.write(Paths.get("SSList.json"), all.toJSONString().getBytes());
                } catch (Exception ef) {
                    ef.printStackTrace();
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