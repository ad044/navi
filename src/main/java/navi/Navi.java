package navi;

import io.github.cdimascio.dotenv.Dotenv;
import navi.eventsystem.EventListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import javax.security.auth.login.LoginException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

public class Navi extends ListenerAdapter {
    public static JDA jda;

    public static RuntimeMXBean mxBean;
    public static final String prefix = "navi,";
    public static final Dotenv dotenv = Dotenv.load();
    public static final String TOKEN = dotenv.get("TOKEN");
    public static final String DEFAULT_CHANNEL = dotenv.get("DEFAULT_CHANNEL");

    public static long getUptime() {
        return mxBean.getUptime();
    }

    public static void main(String[] args) throws LoginException {
        mxBean = ManagementFactory.getRuntimeMXBean();
        jda = JDABuilder.createDefault(TOKEN)
                    .enableIntents(GatewayIntent.GUILD_MEMBERS)
                    .setMemberCachePolicy(MemberCachePolicy.ALL)
                    .build();

        jda.addEventListener(new EventListener());
    }
}
