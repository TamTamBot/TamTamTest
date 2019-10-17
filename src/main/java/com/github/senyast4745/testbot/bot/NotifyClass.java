package com.github.senyast4745.testbot.bot;

import chat.tamtam.botapi.TamTamBotAPI;
import com.github.senyast4745.testbot.bot.impl.TestTamTamBot;
import com.github.senyast4745.testbot.models.GitHubEvents;
import com.github.senyast4745.testbot.models.GitHubCommitCommentEvent;
import com.github.senyast4745.testbot.models.GitHubPullRequestEvent;
import com.github.senyast4745.testbot.models.GitHubPushEvent;

public abstract class NotifyClass {
    protected TamTamBotAPI bot = TestTamTamBot.getBot();

    public void defaultEvent(GitHubEvents event){}

    public void onCommitComment(GitHubCommitCommentEvent comment){}

    public void onPush(GitHubPushEvent push){}

    public void onPullRequest(GitHubPullRequestEvent pullRequest){}
}
