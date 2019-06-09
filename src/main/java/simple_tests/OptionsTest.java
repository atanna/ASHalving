package simple_tests;
import org.apache.commons.cli.*;


public class OptionsTest {



        public static void main(String[] args) throws Exception {

            Options options = new Options();


            options
                    .addOption("i", "input", true, "input file path")
                    .addOption("o", "output", true, "output file");


            CommandLineParser parser = new DefaultParser();
            HelpFormatter formatter = new HelpFormatter();
            CommandLine cmd;

            try {
                cmd = parser.parse(options, args);


                String inputFilePath = cmd.getOptionValue("input");
                String outputFilePath = cmd.getOptionValue("output");

                System.out.println(inputFilePath);
                System.out.println(outputFilePath);
            } catch (ParseException e) {
                System.out.println(e.getMessage());
                formatter.printHelp("utility-name", options);

                System.exit(1);
            }

        }


}
