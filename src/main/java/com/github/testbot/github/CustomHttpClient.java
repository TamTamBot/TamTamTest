package com.github.testbot.github;

import chat.tamtam.botapi.client.impl.JacksonSerializer;
import chat.tamtam.botapi.exceptions.SerializationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.testbot.models.database.UserModel;
import com.github.testbot.models.github.GitHubCreateWebhook;
import com.github.testbot.models.github.GitHubRepositoryModel;
import com.github.testbot.models.github.GitHubWebhookConfig;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;

import static com.github.testbot.constans.GitHubConstants.*;

@Slf4j
@Component
public class CustomHttpClient {

    @Value("${server.bot.url}")
    private String serverUrl;

    private final JacksonSerializer serializer;

    private final OkHttpClient client = new OkHttpClient();

    public CustomHttpClient(JacksonSerializer serializer) {
        this.serializer = serializer;
    }

    public GitHubRepositoryModel pingGithubRepo(final String fullRepoName) throws IOException, SerializationException {

        final Request request = new Request.Builder().url(GIT_HUB_ROOT_API_URL + GIT_HUB_REPOS_URL + fullRepoName).get().build();

        Response response = client.newCall(request).execute();
        log.info(response.toString());
        if (response.code() == HttpStatus.OK.value()) {
            if (response.body() != null) {
                return serializer.deserialize(response.body().string(), GitHubRepositoryModel.class);
            }
        }
        throw new IllegalStateException("Incorrect response code in " + response.toString());
    }

    public boolean checkAccessTokenForWebhooksOperations(UserModel userModel) throws IOException {
        String credential = Credentials.basic(userModel.getGithubUserName(), userModel.getAccessToken());
        final Request request = new Request.Builder().url(GIT_HUB_ROOT_API_URL + "user")
                .get().header("Authorization", credential).build();
        Response response = client.newCall(request).execute();
        log.info(response.toString());
        if (response.code() == HttpStatus.OK.value()) {
            return true;
        }
        else {
            log.warn("Status code: {}", response.code());
            return false;
        }
    }

    public boolean addWebhookToRepo(UserModel userModel, String fullRepoName) throws IOException, SerializationException {
        String apiUrl = GIT_HUB_ROOT_API_URL + GIT_HUB_REPOS_URL + fullRepoName + "/hooks";

        GitHubWebhookConfig webhookConfig = new GitHubWebhookConfig(serverUrl + "/github","json", "0" );
        GitHubCreateWebhook createWebhook = new GitHubCreateWebhook("web", true,
                Arrays.asList("push", "pull_request", "commit_comment"), webhookConfig);
        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        try {
            json = mapper.writeValueAsString(createWebhook);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), json);
        String credential = Credentials.basic(userModel.getGithubUserName(), userModel.getAccessToken());
        final Request request = new Request.Builder().url(apiUrl)
                .post(body).header("Authorization", credential).build();
        Response response = client.newCall(request).execute();
        log.info(response.toString());
        if (response.code() == HttpStatus.CREATED.value()) {
            log.info("Webhook created successfully.");
            return true;
        } else if (response.code() == HttpStatus.UNPROCESSABLE_ENTITY.value()) {
            log.info("Webhook already created.");
            return true;
        } else {
            log.warn("Status code: {}", response.code());
            return false;
        }
    }
}

