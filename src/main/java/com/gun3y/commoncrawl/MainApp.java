package com.gun3y.commoncrawl;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.io.FileSystemUtils;
import org.apache.commons.io.FileUtils;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.ParserProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainApp.class);

    @Option(name = "-data-html", usage = "data path for html")
    private String dataPathHtml = "PR-DATA/data_htmlpage";

    @Option(name = "-data", usage = "data path")
    private String dataPath = "PR-DATA/data_prdm";

    @Option(name = "-thread", usage = "thread size")
    private int thread = 5;

    @Option(name = "-seed", usage = "seed list")
    private File seedFile = new File(".");

    public static void main(String[] args) throws IOException {
        //        MainApp app = new MainApp();
        //        app.doMain(args);

        List<String> urls = FileUtils.readLines(new File(MainApp.class.getClassLoader().getResource("url.txt").getPath()));

        String prefix = "https://aws-publicdatasets.s3.amazonaws.com/";

        String dir = "data";

        for (String url : urls) {
            URL urlObj = new URL(prefix + url);

            LOGGER.info("URL: " + urlObj.toExternalForm());
            long freeSpaceKb = FileSystemUtils.freeSpaceKb();
            if (freeSpaceKb < 10000000) {
                LOGGER.error("Not Enough Space:" + freeSpaceKb + " KB");
                return;
            }
            else {
                FileUtils.copyURLToFile(urlObj, new File(dir + url.substring(url.lastIndexOf('/'))));
            }
        }

    }

    public void doMain(String[] args) throws IOException {
        System.out.println(Calendar.getInstance().getTime());
        ParserProperties defaults = ParserProperties.defaults().withUsageWidth(100);

        CmdLineParser parser = new CmdLineParser(this, defaults);

        try {
            parser.parseArgument(args);
        }
        catch (CmdLineException e) {
            System.err.println(e.getMessage());
            parser.printUsage(System.out);
            return;
        }

        System.out.println("Data     : " + this.dataPath);
        System.out.println("Data-Html: " + this.dataPathHtml);
        System.out.println("Thread   : " + this.thread);
        System.out.println("SeedFile : " + this.seedFile.getAbsolutePath());

        System.out.println(Calendar.getInstance().getTime());
    }
}
