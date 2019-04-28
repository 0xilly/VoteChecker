package info.mdhs.votechecker;


import info.mdhs.votechecker.config.ConfigUtil;
import info.mdhs.votechecker.poll.PollData;

import java.util.Arrays;

public class VoteChecker
{

    public static void main(String[] args)
    {

        var config = new ConfigUtil().getConfig();
        Arrays.asList(config.issues).forEach(i ->
        {
            var pd =new PollData(config.user, config.token, i.title, i.owner, i.repo, i.id);
            pd.writeData();
            System.out.println(pd.getName());
            System.out.println("===============================");
            System.out.println(pd.getYes() + ": Voted in favor.");
            System.out.println(pd.getNo() + ": Voted in opposition.");
            System.out.println(pd.getAbstain() + ": abstained from voting.");
            System.out.println(pd.getTotal() + ": Total votes.");
            System.out.println("===============================");
            System.out.println();
        });
    }
}
