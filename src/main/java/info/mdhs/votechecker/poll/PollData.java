package info.mdhs.votechecker.poll;

import org.kohsuke.github.GitHub;
import org.kohsuke.github.ReactionContent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PollData
{
    private String name;
    private int yes;
    private int no;
    private int indifferent;
    private List<User> votedYes;
    private List<User> votedNo;
    private List<User> votedIndifferent;

    public PollData(String username, String token, String name, String owner, String repo, int projectId)
    {
        votedYes = new ArrayList<>();
        votedNo = new ArrayList<>();
        votedIndifferent = new ArrayList<>();
        this.name = name;
        try
        {
            GitHub github;
            if (username != null && token != null)
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

                if (i.getContent() == ReactionContent.PLUS_ONE)
                {
                    votedYes.add(new User(i, i.getUser()));
//                    votedYes.add(i.getUser().getLogin());
                    yes++;
                }

                if (i.getContent() == ReactionContent.MINUS_ONE)
                {
                    votedNo.add(new User(i, i.getUser()));
                    no++;
                }
            });
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        votedYes.forEach(vy->votedNo.forEach(vn->
        {
            if (vy.name.equals(vn.getName()))
            {
                votedIndifferent.add(vy);
            }
        }));


        votedIndifferent.forEach(a->
        {
            votedNo.remove(a);
            votedYes.remove(a);
            yes--;
            no--;
        });

        this.indifferent = votedIndifferent.size();

    }

    public void writeData()
    {
        new PollWriter(this);
    }

    public int getNo()
    {
        return no;
    }

    public int getIndifferent()
    {
        return this.indifferent;
    }

    public int getYes()
    {
        return yes;
    }

    public int getTotal()
    {
        return this.yes + this.no + this.indifferent;
    }

    public String getName()
    {
        return this.name;
    }

    public List<User> getLessThanAweek()
    {
        var combo = new ArrayList<User>();

        combo.addAll(votedNo);
        combo.addAll(votedYes);
        combo.addAll(votedIndifferent);

        var retlist = new ArrayList<User>();
        combo.forEach(i->
        {
            if (i.daysBetween <= 7)
            {
                retlist.add(i);
            }
        });

        return retlist;
    }
}
