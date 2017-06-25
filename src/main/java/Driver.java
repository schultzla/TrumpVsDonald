import net.dean.jraw.RedditClient;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.http.oauth.Credentials;
import net.dean.jraw.http.oauth.OAuthData;
import net.dean.jraw.http.oauth.OAuthException;
import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;
import net.dean.jraw.paginators.Sorting;
import net.dean.jraw.paginators.SubredditPaginator;
import net.dean.jraw.paginators.TimePeriod;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Logan on 6/25/2017.
 */
public class Driver {

    public static void main(String[] args) {

        Twitter twitter = TwitterFactory.getSingleton();

        UserAgent myUserAgent = UserAgent.of("desktop", "me.logamos.donbot", "v0.1", "TrumpVsDonald");
        RedditClient redditClient = new RedditClient(myUserAgent);
        Credentials credentials = Credentials.script("TrumpVsDonald", "****", "YyUpR65F_VRuVw", "*****");
        OAuthData authData = null;
        try {
            authData = redditClient.getOAuthHelper().easyAuth(credentials);
        } catch (OAuthException e) {
            e.printStackTrace();
        }
        redditClient.authenticate(authData);

        SubredditPaginator sp = new SubredditPaginator(redditClient);
        sp.setLimit(100);
        sp.setSorting(Sorting.NEW);
        sp.setTimePeriod(TimePeriod.WEEK);
        sp.setSubreddit("TrumpCriticizesTrump");
        sp.next(true);
        Listing<Submission> list = sp.getCurrentListing();


        ArrayList<Long> tweetId = new ArrayList<Long>();
        for(Submission submission : list) {
            String temp = submission.getUrl();
            CharSequence charSequence = "twitter";
            if(temp.contains(charSequence)) {
                System.out.println(temp);
                temp = temp.replaceAll("\\D+", "");
                try {
                    temp = temp.substring(0, 18);
                } catch (StringIndexOutOfBoundsException e) {
                    continue;
                }
            } else {
                continue;
            }
            System.out.println(temp);
            Long id = Long.valueOf(temp);
            try {
                twitter.retweetStatus(id);
            } catch (TwitterException e) {
                //e.printStackTrace();
            }
        }
    }

}
