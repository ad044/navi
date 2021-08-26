package navi.commandsystem.commands.audioplayer;

import navi.Navi;
import navi.commandsystem.Command;
import navi.commandsystem.CommandParameters;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.util.Arrays;
import java.util.Map;

public final class PlayCommand implements Command {
    @Override
    public String getDescription() {
        return "Adds a track to the queue.";
    }

    @Override
    public String getCategory() {
        return "player";
    }

    @Override
    public boolean isAdminCommand() {
        return false;
    }

    private final Map<String, String> queryParams = Map.of(
            "yt", "ytsearch:",
            "sc", "scsearch:"
    );

    private boolean isUrl(String str) {
        return str.matches("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");
    }

    private static <T> T[] addToBeginningOfArray(T[] elements, T element)
    {
        T[] newArray = Arrays.copyOf(elements, elements.length + 1);
        newArray[0] = element;
        System.arraycopy(elements, 0, newArray, 1, elements.length);

        return newArray;
    }

    private String[] getSearchQuery(String[] args) {
        String fstArg = args[0];

        if (fstArg.equals("yt") || fstArg.equals("sc")) {
            if (args.length < 2) {
                return null;
            }

            args[0] = queryParams.get(fstArg);

            return args;
        } else if (!isUrl(fstArg)){
            return addToBeginningOfArray(args, "ytsearch:");
        }

        return args;
    }

    @Override
    public void execute(CommandParameters params) {
        String[] args = params.getArgs();
        TextChannel channel = params.getTextChannel();
        VoiceChannel voiceChannel = params.getAuthor().getVoiceState().getChannel();

        if (!params.hasArguments()) {
            channel.sendMessage("Insufficient arguments.").queue();
            return;
        }

        String[] query = getSearchQuery(args);

        if (query == null){
            channel.sendMessage("Insufficient arguments.").queue();
            return;
        }

        Navi.getAudioPlayer().loadAndPlay(params.getTextChannel(), voiceChannel, String.join(" ", query));
    }
}
