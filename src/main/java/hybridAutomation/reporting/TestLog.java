package hybridAutomation.reporting;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class TestLog {

    public static Logger log() {

        return LogManager.getLogger(Thread.currentThread().getStackTrace()[2].getClassName());
    }
}
