package navi.commandsystem;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.Member;

import java.util.List;

public final class CommandParameters {
    public CommandParameters(Guild guild, Message message, TextChannel textChannel, Member author, List<Member> mentions, String[] args) {
        this.guild = guild;
        this.textChannel = textChannel;
        this.author = author;
        this.mentions = mentions;
        this.args = args;
        this.message = message;
    }

    private final Guild guild;
    private final TextChannel textChannel;
    private final Member author;
    private final String[] args;
    private final List<Member> mentions;
    private final Message message;

    public final TextChannel getTextChannel() {
        return textChannel;
    }

    public final Member getAuthor() {
        return author;
    }

    public final String[] getArgs() {
        return args;
    }

    public final List<Member> getMentions() {
        return mentions;
    }

    public final boolean hasMentions() {
        return mentions.size() > 0;
    }

    public final boolean hasArguments() {
        return args.length > 0;
    }

    public final Guild getGuild() {
        return guild;
    }

    public Message getMessage() {
        return message;
    }
}
