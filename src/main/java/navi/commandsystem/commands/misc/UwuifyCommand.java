package navi.commandsystem.commands.misc;

import navi.commandsystem.Command;
import navi.commandsystem.CommandParameters;
import net.dv8tion.jda.api.entities.TextChannel;

import javax.swing.plaf.IconUIResource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Pattern;

public class UwuifyCommand implements Command {
    @Override
    public String getDescription() {
        return "Uwuifies the input.";
    }

    @Override
    public String getCategory() {
        return "misc";
    }

    @Override
    public boolean isAdminCommand() {
        return false;
    }

    private final Map<Character, String> uwuMap = Map.of(
            'r', "w",
            'l', "w",
            'R', "W",
            'L', "W",
            'u', "yu",
            'U', "yU"
    );

    private final String[] uwuSmileys = new String[] {
        "(ᵘʷᵘ)", "(ᵘﻌᵘ)", "(◡ ω ◡)", "(◡ ꒳ ◡)", "(◡ w ◡)", "(◡ ሠ ◡)", "(˘ω˘)", "(⑅˘꒳˘)", "(˘ᵕ˘)", "(˘ሠ˘)", "(˘³˘)",
        "(˘ε˘)", "(˘˘˘)", "( ᴜ ω ᴜ )", "(„ᵕᴗᵕ„)", "(ㅅꈍ ˘ ꈍ)", "(⑅˘꒳˘)", "( ｡ᵘ ᵕ ᵘ ｡)", "( ᵘ ꒳ ᵘ ✼)", "( ˘ᴗ˘ )", "(ᵕᴗ ᵕ⁎)",
        "*:･ﾟ✧(ꈍᴗꈍ)✧･ﾟ:*", "*˚*(ꈍ ω ꈍ).₊̣̇.", "(。U ω U。)", "(U ᵕ U❁)", "(U ﹏ U)", "(◦ᵕ ˘ ᵕ◦)", "ღ(U꒳Uღ)", "♥(。U ω U。)",
        "– ̗̀ (ᵕ꒳ᵕ) ̖́-", "( ͡U ω ͡U )", "( ͡o ᵕ ͡o )", "( ͡o ꒳ ͡o )", "( ˊ.ᴗˋ )", "(ᴜ‿ᴜ✿)", "~(˘▾˘~)", "(｡ᴜ‿‿ᴜ｡)", "uwu", "owo"
    };

    private final List<String> smileyTriggers = Arrays.asList("?", ";", ".", "!");

    private String uwuifyStr(String input) {
        StringBuilder sb = new StringBuilder();

        for (char currChar : input.toCharArray()) {
            String strCurrChar = Character.toString(currChar);
            sb.append(uwuMap.getOrDefault(currChar, strCurrChar));
            if (smileyTriggers.contains(strCurrChar)) {
                sb.append(" ").append(uwuSmileys[new Random().nextInt(uwuSmileys.length)]);
            }
        }
        return sb.toString();
    }

    @Override
    public void execute(CommandParameters params) {
        TextChannel channel = params.getTextChannel();

        if (!params.hasArguments()) {
            channel.sendMessage("Please provide arguments for the command.").queue();
            return;
        }

        if (params .getMessage().mentionsEveryone()){
            channel.sendMessage("Malformed input.").queue();
            return;
        }

        String rawContent = String.join(" ", params.getArgs());

        String uwuified = uwuifyStr(rawContent);

        if (uwuified.length() > 2000) {
            channel.sendMessage("Text too long.").queue();
            return;
        }

        channel.sendMessage(uwuifyStr(rawContent)).queue();
    }
}
