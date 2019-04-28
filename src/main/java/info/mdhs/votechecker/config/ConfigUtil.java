package info.mdhs.votechecker.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConfigUtil
{
    private Gson gson;
    private Config config;
    private Path confFile = Paths.get("config.json");

    public ConfigUtil()
    {
        if (!Files.exists(confFile))
        {
            var gb = new GsonBuilder();
            var conf = new Config();
            var issues = new Issue();
            conf.token = null;
            conf.user = null;

            issues.title = "Some poll";
            issues.id = 123;
            issues.owner = "user/org";
            issues.repo = "repo";

            conf.issues = new Issue[] {issues};
            gson = gb.setPrettyPrinting().serializeNulls().create();

            var confStr = gson.toJson(conf);

            try
            {
                Files.write(confFile, confStr.getBytes());
            } catch (IOException e)
            {
                e.printStackTrace();
            }

            System.out.println("please fill in user information in config.json");
            System.exit(2);
        }

        try (var reader = Files.newBufferedReader(confFile)) {
            gson = new Gson();
            config = gson.fromJson(reader, Config.class);
        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    public Config getConfig()
    {
        return config;
    }
}
