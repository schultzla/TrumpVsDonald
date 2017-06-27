import net.dean.jraw.models.Submission;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

/**
 * Created by Logan on 6/25/2017
 */
public class Driver {

    public static void main(String[] args) {
        //Authorize Twitter account
        Twitter twitter = TwitterFactory.getSingleton();

        //Authorize Reddit Account
        RedditAuth.getCredentials();

        //Go through last 20 new submissions on /r/TrumpCriticizesTrump
        for(Submission submission : RedditAuth.list) {
            //Get the URL of the current submission
            String temp = submission.getUrl();

            /*If the URL is a Twitter link, replace all of the letters and take the first 18 digits which would be the
               status ID. If the status is old and shorter than 18 just skip it, same if it is not a Twitter link
             */
            CharSequence charSequence = "twitter";
            if(temp.contains(charSequence)) {
                temp = temp.replaceAll("\\D+", "");
                try {
                    temp = temp.substring(0, 18);
                } catch (StringIndexOutOfBoundsException e) {
                    log("Bad link");
                    continue;
                }
            } else {
                continue;
            }

            //Store the status ID as a long
            Long id = Long.valueOf(temp);
            Status status;
            try {
                /*If the status was already retweeted stop the search as the bot has reached the last tweet it tweeted
                and any subsequent tweets will have already been retweeted. Otherwise, retweet the new link and move on
                 */
                status = twitter.showStatus(id);
                if(!status.isRetweetedByMe()) {
                    twitter.retweetStatus(id);
                    System.out.println("Retweeted: " + status.getText());
                } else {
                    System.out.println("Duplicate Tweet");
                    break;
                }
            } catch (TwitterException e) {
                log("Unable to reach Twitter");
            }
        }
    }

    //Shortened print method
    private static void log(String s) {
        System.out.println(s);
    }
}