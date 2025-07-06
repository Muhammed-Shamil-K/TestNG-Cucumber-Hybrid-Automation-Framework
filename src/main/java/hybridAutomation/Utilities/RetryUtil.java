package hybridAutomation.Utilities;

import hybridAutomation.Core.Consumer;
import org.slf4j.helpers.MessageFormatter;



public class RetryUtil {
    public static <T> T retry(Consumer<T> consumer, int maxTries, long waitTime, String message, Object... args) {
        long count = 0L;
        T result = null;
        String info = MessageFormatter.arrayFormat(message, args).getMessage();
        while (count <= maxTries) {
            try {
                result = consumer.consume();
                break;
            } catch (AssertionError | Exception error) {
                if(count == maxTries) {
                    if(error instanceof AssertionError) {
                        throw new AssertionError(error);
                    } else {
                        throw new RuntimeException(error);
                    }
                } else {
                    if(error instanceof AssertionError) {
                        //need Logger correction
                        System.out.println("Assertion error" + error.getMessage());
                    } else {
                        //need Logger correction
                        System.out.println("Exception : " + error.getMessage());
                    }

                    count ++;
                    //need Logger correction
                    System.out.println("Retry # " + count + " ----->  " + info);

                    try {
                        Thread.sleep(waitTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return result;
    }

    public static <T> T retry(Consumer<T> consumer, int maxTries, long waitTime) {
        return retry(consumer, maxTries, waitTime, "");
    }

    public static <T> T retry(Consumer<T> consumer, int maxTries) {
        return retry(consumer, maxTries, 1000);
    }

    public static <T> T retry(Consumer<T> consumer, int maxTries, String message, Object... args) {
        return retry(consumer, maxTries, 10000, message, args);
    }

}
