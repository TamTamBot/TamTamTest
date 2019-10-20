package com.github.testbot.models.github;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class GitHubPushEvent implements GitHubEvents {
    private boolean force;
    private List<GitHubCommitModel> commits;
    private GitHubRepositoryModel repository;
    private PushAuthor pusher;
    private GitHubUserModel sender;

    @Getter
    @Setter
    public static
    class PushAuthor {
        private String name;
        private String email;
    }

    @Override
    public String toString() {
        StringBuilder toString = new StringBuilder("Push to ").append(repository.getFullName()).append( " link: ")
                .append(repository.getHtmlUrl()).append("\n\rCommits:\n\r");


        commits.forEach(c -> toString.append("message: ").append(c.getMessage()).append("\n\r"));
        toString.append("Pusher name: ").append(pusher.getName());
        return toString.toString();
    }
}



