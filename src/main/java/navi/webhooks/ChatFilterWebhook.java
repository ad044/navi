package navi.webhooks;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import navi.Navi;
import net.dv8tion.jda.api.entities.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ChatFilterWebhook {
    private final Map<TextChannel, WebhookClient> webhooksForChannels;

    public ChatFilterWebhook() {
        this.webhooksForChannels = new HashMap<>();
    }

    private WebhookClient getWebhookForChannel(TextChannel channel) {
        WebhookClient client = webhooksForChannels.get(channel);
        if (client == null) {
            Optional<Webhook> channelWebhook = channel.retrieveWebhooks()
                    .complete()
                    .stream()
                    .filter(webhook -> webhook.getName().equals("chat_filterer"))
                    .findFirst();

            String url = channelWebhook.map(Webhook::getUrl)
                    .orElseGet(() -> channel.createWebhook("chat_filterer").complete().getUrl());

            client = WebhookClient.withUrl(url);

            webhooksForChannels.put(channel, client);
        }
        return client;
    }

    private String filterMessage(String message) {
        Map<String, String> filteredWords = Navi.getFilteredWords();
        String filteredMessage = message;
        for (String word : filteredWords.keySet()) {
            filteredMessage = filteredMessage.replaceAll(word, filteredWords.get(word));
        }

        return filteredMessage;
    }

    public void sendFilteredMessageAsUser(Guild guild, TextChannel channel, User user, String originalMessage) {
        Member userAsMember = guild.getMember(user);

        if (userAsMember == null) {
            return;
        }

        WebhookMessageBuilder builder = new WebhookMessageBuilder()
                .setUsername(userAsMember.getEffectiveName())
                .setAvatarUrl(user.getEffectiveAvatarUrl().replaceFirst("gif", "png") + "?size=512")
                .setContent(filterMessage(originalMessage));

        getWebhookForChannel(channel).send(builder.build());
    }
}
