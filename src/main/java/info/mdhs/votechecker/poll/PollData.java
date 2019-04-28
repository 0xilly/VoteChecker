package info.mdhs.votechecker.poll;

import org.kohsuke.github.GitHub;
import org.kohsuke.github.ReactionContent;

import java.util.ArrayList;
import java.util.List;

public class PollData
{
    private String name;
    private int yes;
    private int no;
    private int abstain;
    private List<String> votedYes;
    private List<String> votedNo;
    private List<String> abstained;

    public PollData(String username, String token, String name, String owner, String repo, int projectId)
    {
        votedYes = new ArrayList<>();
        votedNo = new ArrayList<>();
        abstained = new ArrayList<>();
        this.name = name;
        try
        {
            GitHub github;
            if (username != null || token !=null)
            {
                github = GitHub.connect(username, token);
            } else
            {
                github = GitHub.connectAnonymously();
            }

            var organization = github.getOrganization(owner);
            var ghrepo = organization.getRepository(repo);
            var reactions = ghrepo.getIssue(projectId).listReactions();

            reactions.forEach(i ->
            {
                try
                {
                } catch (Exception e)
                {
                    e.printStackTrace();
                }

                if (i.getContent() == ReactionContent.PLUS_ONE)
                {

                    votedYes.add(i.getUser().getLogin());
                    yes++;
                }

                if (i.getContent() == ReactionContent.MINUS_ONE)
                {
                    votedNo.add(i.getUser().getLogin());
                    no++;
                }

                if (i.getContent() == ReactionContent.HEART)
                {
                    abstained.add(i.getUser().getLogin());
                    abstain++;
                }
            });
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public void writeData()
    {
        new PollWriter(this);
    }

    public int getNo()
    {
        return no;
    }

    public int getAbstain()
    {
        return abstain;
    }

    public int getYes()
    {
        return yes;
    }

    public int getTotal()
    {
        return this.yes + this.no + this.abstain;
    }

    public String getName()
    {
        return this.name;
    }
}