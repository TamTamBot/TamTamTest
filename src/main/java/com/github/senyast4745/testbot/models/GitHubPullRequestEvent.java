package com.github.senyast4745.testbot.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class GitHubPullRequestEvent implements GitHubEvents {
    private String action; // assigned, unassigned, review_requested,
    // review_request_removed, labeled, unlabeled, opened, edited,
    // closed, ready_for_review, locked, unlocked, or reopened
    private Integer number;
    @JsonProperty("pull_request")
    private GitHubPullRequestModel pullRequest;
    private GitHubRepositoryModel repository;
    private GitHubUserModel sender;

    @Override
    public String toString() {
        return "Pull request\n\rAction " + action + "\n\r" + pullRequest.toString() + "\n\rBy user: "
                + sender.getLogin() +"\n\rTo repository: " + repository.toString();
    }
}
