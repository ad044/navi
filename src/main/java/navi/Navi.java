package navi;

import io.github.cdimascio.dotenv.Dotenv;
import navi.audioplayer.AudioPlayer;
import navi.eventsystem.EventListener;
import navi.webhooks.ChatFilterWebhook;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import javax.security.auth.login.LoginException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.nio.channels.Channel;
import java.util.Map;

import static java.util.Map.entry;

public class Navi extends ListenerAdapter {
    private static AudioPlayer audioPlayer;
    private static ChatFilterWebhook chatFilterWebhook;
    private static RuntimeMXBean mxBean;

    private static final String[] prefixes = new String[] {"navi,", "!n"};

    private static final Dotenv dotenv = Dotenv.load();
    private static final String TOKEN = dotenv.get("TOKEN");
    private static final String DEFAULT_CHANNEL = dotenv.get("DEFAULT_CHANNEL");
    private static final String VOICE_CHANNEL = dotenv.get("VOICE_CHANNEL");
    private static final String SHITPOST_CHANNEL = dotenv.get("SHITPOST_CHANNEL");

    private static final Map<String, String> channelMap = Map.ofEntries(
            entry("player", VOICE_CHANNEL),
            entry("misc", SHITPOST_CHANNEL)
    );

    private static final Map<String, String> filteredWords = Map.ofEntries();

    public static void main(String[] args) throws LoginException {
        mxBean = ManagementFactory.getRuntimeMXBean();

        JDA jda = JDABuilder.createDefault(TOKEN)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .build();

        jda.addEventListener(new EventListener());

        audioPlayer = new AudioPlayer();
        chatFilterWebhook = new ChatFilterWebhook();
    }

    public static String getDefaultChannel(){
        return DEFAULT_CHANNEL;
    }

    public static long getUptime() {
        return mxBean.getUptime();
    }

    public static ChatFilterWebhook getChatFilterWebhook(){
        return chatFilterWebhook;
    }

    public static Map<String, String> getFilteredWords() {
        return filteredWords;
    }

    public static String[] getPrefixes() {
        return prefixes;
    }

    public static AudioPlayer getAudioPlayer() {
        return audioPlayer;
    }

    public static String getChannelForCategory(String category) {
        return channelMap.get(category);
    }

}
