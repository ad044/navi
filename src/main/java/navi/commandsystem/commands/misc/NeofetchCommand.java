package navi.commandsystem.commands.misc;

import navi.Navi;
import navi.commandsystem.Command;
import navi.commandsystem.CommandParameters;
import navi.time.TimeParser;

public final class NeofetchCommand implements Command {
    @Override
    public final String getDescription() {
        return "Prints out current host machine information of NAVI.";
    }

    @Override
    public String getManual() {
        return "Takes no arguments.\nExample: navi, neofetch";
    }

    @Override
    public String getCategory() {
        return "misc";
    }

    @Override
    public final boolean isAdminCommand() {
        return false;
    }

    private static int getRandomInRange(int max, int min){
        return (int) ((Math.random() * (max - min)) + min);
    }

    private static double getRandomInRange(double max, double min){
        return ((Math.random() * (max - min)) + min);
    }

    private static int getRamUsage() {
        return getRandomInRange(1087423, 3440);
    }

    private static class DiskUsage {
        private final double gbUsed;
        private final double percentageUsed;

        public DiskUsage(double maxUsage, double minUsage, double maxSize) {
            gbUsed = getRandomInRange(maxUsage, minUsage);
            percentageUsed = gbUsed / maxSize * 100;
        }

        public double getGbUsed() {
            return gbUsed;
        }

        public double getPercentageUsed() {
            return percentageUsed;
        }
    }

    private static DiskUsage getAdamDiskUsage() {
        return new DiskUsage(48.5, 44.3, 50);
    }

    private static DiskUsage getLilithDiskUsage() {
        return new DiskUsage(348.26, 347.1, 1000);
    }

    @Override
    public final void execute(CommandParameters params) {
        String uptime = TimeParser.millisToHMS(Navi.getUptime());
        int ramUsage = getRamUsage();
        DiskUsage adam = getAdamDiskUsage();
        DiskUsage lilith = getLilithDiskUsage();

        String neofetchStr = "```\n                  .+/\t                    user@lainland\n" +
                "                  sNm-\t\t\t\t\t   ----\n" +
                "               .-/+so+:.'\t\t\t\t\tOS: CoplandOS\n" +
                "      '-os*'.ohNNNNNNNNNmy:'.ss/.\t\t\tHost: Navi v2\n" +
                "    '+dNNdssNMNds+///+oymNNdohmNmy-'\t\t Kernel: 6.6.98-copland\n" +
                String.format("  .omNds:-hMMh/://++++//:oNMN+.+hmNh:\t\tUptime: %s \n", uptime) +
                " +mNd/'  sMMd-//odNNNmh//:+NMN-  -sNNy.\t  Packages: 114357\n" +
                ":NNN.   'dMM+///NMMMMMMy//-NMM/    sNNh\t  Shell: Speakell\n" +
                ".mNN/'  'hMMy://hNMMMMmo//:NMN:   .dNNo\t  CPU: Tachibana Xeon E-2350 (256) @ 8.500GHz\n" +
                " .smNh+. :NMNo://oyhys+//:dMMy'':smNd/\t   GPU: Tachibana Graphics 5000Xi\n" +
                String.format("  ':smNmy++mMNh+/:::::/+smMNy/odNNho-\t    Memory: %sMiB / 2097152MiB\n", ramUsage) +
                String.format("  ymd./ymm+.odNNmd/''ymNNmy:.hmdo-omm-\t   Disk: Adam:   %.2fGiB / 50GiB (%.2f%%)\n",
                        adam.getGbUsed(),
                        adam.getPercentageUsed()) +
                String.format("  ./:   '.   '-yMMy -NMm/.   '.'  '/:'\t\t     Lilith: %.2fTiB / 1000TiB (%.2f%%)\n",
                        lilith.getGbUsed(),
                        lilith.getPercentageUsed()) +
                "               :MMh :MMh'\t\t\t\t\tPsyche Processor: V0.44\n" +
                "        /+-    .MMy .MMy    '/+.\n" +
                "       'dMNs/-:yMN/ 'mMN+::+dNN/\n" +
                "        '/ymNNNmy:   .odNNNmho-\n```";
        params.getTextChannel().sendMessage(neofetchStr).queue();
    }
}
