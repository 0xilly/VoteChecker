package info.mdhs.votechecker.poll;

import org.kohsuke.github.GHReaction;
import org.kohsuke.github.GHUser;

import java.io.IOException;
import java.time.temporal.ChronoUnit;


public class User
{
    String name;
    String dateVoted;
    String accountCreated;
    long daysBetween;

    public User(GHReaction reaction, GHUser user)
    {
        this.name = user.getLogin();
        try
        {
            var created = reaction.getCreatedAt();
            var voted = user.getCreatedAt();
            this.dateVoted = created.toString();
            this.accountCreated = voted.toString();

            this.daysBetween = ChronoUnit.DAYS.between(voted.toInstant(), created.toInstant());

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public String getName()
    {
        return name;
    }

    public String getDateVoted()
    {
        return dateVoted;
    }

    public String getAccountCreated()
    {
        return accountCreated;
    }
}
