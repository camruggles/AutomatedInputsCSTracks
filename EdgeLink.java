
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by CRuggles on 6/24/2017.
 */
public class EdgeLink {


    /**
     * Necessary object information to put into file
     *
     * */
    private String track;
    private char flag;
    private int courseNum;
    private String courseSymbol;

    private EdgeLink(String track, int courseNum, String courseSymbol){
        this.track = track;
        this.courseNum = courseNum;
        if (courseSymbol.equals("S")) courseSymbol = "CS";
        this.courseSymbol = courseSymbol.toUpperCase();
        flag = 'E';

    }


/**
 * Document
 * Elements
 * Element
 * String
 * */
    public static void main(String[] args) throws IOException{

        /**
         * Initialize important variables
         * */
        Scanner bryce;
        ArrayList<EdgeLink> list = new ArrayList<EdgeLink>();
        File f = new File("output.txt");
        BufferedWriter bw = new BufferedWriter(new FileWriter(f));
        // https://www.cs.purdue.edu/undergraduate/curriculum/track_softengr.html

        //https://www.cs.purdue.edu/undergraduate/curriculum/bachelor.html
        Document doc = Jsoup.connect("https://www.cs.purdue.edu/undergraduate/curriculum/bachelor.html").get(); //main CS page
        Elements links = doc.select("a[href]"); // the Elements objects with all of the CS track links
        /**
         * iterates through all of the web links and filters them out based on track criteria
        * */

        for (Element link : links) {

            String s = link.attr("href"); // the actual link
            String t = link.text(); // the text highlighted with the link
            String symbol; // MATH OR CS OR STAT OR EPCS
            String number; // the course number
            int num; // the parsed course number

            if ((s.contains("track") || s.contains("database")) && !t.equals("")) { // ensures its a track page

                    /**
                     * prints out information to console and file about the hyperlink and the text
                     *
                     * */
                    System.out.println("\nlink : " + link.attr("href"));
                    System.out.println("text : " + link.text());
                    bw.write("\n\nlink : " + link.attr("href"));
                    bw.write("\ntext : " + link.text());
                    bw.write("\n");

                        String track = link.attr("href");
                        String address = "https://www.cs.purdue.edu/undergraduate/curriculum/" + track; //runs another JSoup operation on the individual track pages
                        Document doc2 = Jsoup.connect(address).get(); //the new document object
                        Elements links2 = doc2.select("a[href]"); //all of the course based links to be used during track analysis
                        System.out.printf("%s\n", link.attr("href")); //helpful for debugging and monitoring

                        if (track.contains("track_")){
                            int i = track.indexOf('_') +1;
                            int j = track.indexOf(".");
                            track = track.substring(i, j);
                            System.out.println(track);
                            track = track.toUpperCase();

                        }
                        else{
                            track = "DBIS";
                        }

                        for (Element link2 : links2) { // iterates through a track page to look for course information

                            s = link2.attr("href");  //reassigns s to see if it leades to a course catalog
                            t = link2.text(); // reasssigns to to the course text, if it contains CS/MATH/STAT/EPCS and a five digit number, we parse it.

                            if (t.contains("00") && t.contains(" ")){ // determines if it's a course
                                System.out.println("text : " + link2.text());
                                bryce = new Scanner(t);
                                /**uses a scanner to separate CS/MATH/STAT from the course number.*/
                                bryce.useDelimiter(" ");
                                symbol = bryce.next();
                                number = bryce.next();
                                number = number.substring(0, 3);
                                num = Integer.parseInt(number);

                                list.add(new EdgeLink(track, num, symbol));
                                //end of parsing


                                //writes information to file
                                bw.write("\n\t" + symbol + " " + num);
                                bw.flush();

                            }



                        }
            }

        }

        printAll(list);
        printToFile(list, "edges.txt");

        bw.close();
     //   bryce.close();


    }

    public String toString(){
        String r = String.format("%s:%c:%s:%d", this.track, this.flag, this.courseSymbol, this.courseNum);
        return r;

    }

    private static void printAll(ArrayList<EdgeLink> arr){
        for (EdgeLink e : arr)
            System.out.println(e.toString());



    }

    private static void printToFile(ArrayList<EdgeLink> arr, String fileName) throws IOException{
        File f = new File(fileName);
        BufferedWriter zach = new BufferedWriter(new FileWriter(f));
        for (EdgeLink e : arr)
                zach.write(e.toString() + "\n");
        zach.flush();
        zach.close();


    }

}
