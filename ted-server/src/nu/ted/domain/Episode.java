package nu.ted.domain;

import java.util.Date;

// TODO: this will eventually replace SeasonEpisode

public interface Episode
{
    public String getTitle();
    public int getSeason();
    public int getNumber();
    public Date getFirstAired();

}
