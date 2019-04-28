package info.mdhs.votechecker.poll;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PollWriter
{
    private Gson gson;

    public PollWriter(PollData pollData)
    {
        var gb = new GsonBuilder();
        var files = Paths.get(pollData.getName() + ".json");

        gson = gb.setPrettyPrinting().create();

        var confStr = gson.toJson(pollData);

        try
        {
            Files.write(files, confStr.getBytes());
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
