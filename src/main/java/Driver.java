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
 * Created by Logan on 6/25/2017
 */
public class Driver {

    public static void main(String[] args) {

        Twitter twitter = TwitterFactory.getSingleton();

        UserAgent myUserAgent = UserAgent.of("desktop", "me.logamos.donbot", "v0.1", "TrumpVsDonald");
        RedditClient redditClient = new RedditClient(myUserAgent);
        //no credentials for you
        OAuthData authData = null;
        try {
            authData = redditClient.getOAuthHelper().easyAuth(credentials);
        } catch (OAuthException e) {
            e.printStackTrace();
        }
        redditClient.authenticate(authData);

        SubredditPaginator pages = new SubredditPaginator(redditClient);
        pages.setLimit(20);
        pages.setSorting(Sorting.NEW);
        pages.setTimePeriod(TimePeriod.WEEK);
        pages.setSubreddit("TrumpCriticizesTrump");
        pages.next(true);
        Listing<Submission> list = pages.getCurrentListing();



        ArrayList<Long> tweetId = new ArrayList<Long>();
        for(Submission submission : list) {
            String temp = submission.getUrl();
            CharSequence charSequence = "twitter";
            if(temp.contains(charSequence)) {
                temp = temp.replaceAll("\\D+", "");
                try {
                    temp = temp.substring(0, 18);
                } catch (StringIndexOutOfBoundsException e) {
                    continue;
                }
            } else {
                continue;
            }
            Long id = Long.valueOf(temp);
            Status status = null;
            try {
                status = twitter.showStatus(id);
            } catch (TwitterException e) {
                e.printStackTrace();
            }
            try {
                if(!status.isRetweetedByMe()) {
                    twitter.retweetStatus(id);
                    System.out.println("Retweeted: " + status.getText());
                } else {
                    System.out.println("Duplicate Tweet");
                    break;
                }
            } catch (TwitterException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean parseFile(String fileName,String searchStr) throws FileNotFoundException{
        Scanner scan = new Scanner(new File(fileName));
        while(scan.hasNext()){
            String line = scan.nextLine().toLowerCase().toString();
            return line.contains(searchStr);
        }
        return false;
    }

}