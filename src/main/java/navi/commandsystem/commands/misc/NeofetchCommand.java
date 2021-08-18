package navi.commandsystem.commands.misc;

import navi.commandsystem.Command;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public final class NeofetchCommand implements Command {
    @Override
    public final String getDescription() {
        return "Prints out current host machine information of NAVI.";
    }

    @Override
    public final boolean isAdminCommand() {
        return false;
    }

    @Override
    public final void execute(GuildMessageReceivedEvent event, String[] args) {
        event.getChannel().sendMessage(
        "```fix\n                  .+/\n" +
                "                  sNm-\n" +
                "               .-/+so+:.'\n" +
                "      '-os*'.ohNNNNNNNNNmy:'.ss/.\n" +
                "    '+dNNdssNMNds+///+oymNNdohmNmy-'\n" +
                "  .omNds:-hMMh/://++++//:oNMN+.+hmNh:\n" +
                " +mNd/'  sMMd-//odNNNmh//:+NMN-  -sNNy.\n" +
                ":NNN.   'dMM+///NMMMMMMy//-NMM/    sNNh\n" +
                ".mNN/'  'hMMy://hNMMMMmo//:NMN:   .dNNo\n" +
                " .smNh+. :NMNo://oyhys+//:dMMy'':smNd/\n" +
                "  ':smNmy++mMNh+/:::::/+smMNy/odNNho-\n" +
                "  ymd./ymm+.odNNmd/''ymNNmy:.hmdo-omm-\n" +
                "  ./:   '.   '-yMMy -NMm/.   '.'  '/:'\n" +
                "               :MMh :MMh'\n" +
                "        /+-    .MMy .MMy    '/+.\n" +
                "       'dMNs/-:yMN/ 'mMN+::+dNN/\n" +
                "        '/ymNNNmy:   .odNNNmho-\n" +
                "            '''          ''\n```").queue();
    }
}
