import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class Event_Welcome extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        Color Color = java.awt.Color.decode(Bot.ColorHexCode);

        User newUser = event.getUser();
        MessageChannel channel = event.getGuild().getTextChannelById(Bot.WelcomeMessageChannelID);
        VoiceChannel MemberCountChannel = event.getGuild().getVoiceChannelById(Bot.MemberCountVoiceChannelID);

        EmbedBuilder Embed = new EmbedBuilder();
        String newTitle = "";
        if (Bot.Welcome_Message_Title.contains("{user}")) {
            newTitle = Bot.Welcome_Message_Title.replace("{user}", newUser.getAsTag() + "");
        } else {
            newTitle = Bot.Welcome_Message_Title;
        }
        Embed.setTitle(newTitle);
        Embed.setThumbnail(newUser.getAvatarUrl());
        Embed.setColor(Color);
        Embed.setFooter(Bot.WaterMark, Bot.BotLogo);
        String newBase = "";
        if (Bot.Welcome_Message_Base.contains("{memberCount}")) {
            newBase = Bot.Welcome_Message_Base.replace("{memberCount}", event.getGuild().getMemberCount() + "");
        } else {
            newBase = Bot.Welcome_Message_Base;
        }
        String newx2Base = "";
        if (newBase.contains("{ServerIP}")) {
            newx2Base = newBase.replace("{ServerIP}", Bot.ServerIP);
        } else {
            newx2Base = newBase;
        }

        Embed.addField(Bot.Welcome_Message_Header, newx2Base, false);

        channel.sendMessage(event.getMember().getAsMention()).queue();
        channel.sendMessage(Embed.build()).queue();

        MemberCountChannel.getManager()
                .setName("Members: " + event.getGuild().getMemberCount()).queue();

        event.getGuild().addRoleToMember(newUser.getId(), event.getGuild().getRoleById(Bot.VisitorRoleID)).queueAfter(2,
                TimeUnit.SECONDS);


    }

    public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
        VoiceChannel MemberCountChannel = event.getGuild().getVoiceChannelById(Bot.MemberCountVoiceChannelID);

        int MemberCount = event.getGuild().getMemberCount();
        MemberCountChannel.getManager().setName("Members: " + MemberCount).queue();
    }

}