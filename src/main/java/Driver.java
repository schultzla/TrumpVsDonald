import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

/**
 * Created by Logan on 6/25/2017.
 */
public class Driver {

    public static void main(String[] args) {
        Twitter twitter = TwitterFactory.getSingleton();
        




        long tweetId = 498008486551506945L;
        try {
            twitter.retweetStatus(tweetId);
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }

}