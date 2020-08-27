import edu.duke.DirectoryResource;
import edu.duke.FileResource;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.util.*;

public class BabiesName {

    /**
     * Method totalBirths to also print the number of girls names , the number of
     * boys names and the total names in the file.
     */
    public String totalBirth(FileResource fr) {

        int totalBirth = 0;
        int totalGirls = 0;
        int totalBoys = 0;
        int countOfBoysNames = 0;
        int countOfGirlsNames = 0;
        int countOfNames = 0;

        for (CSVRecord record : fr.getCSVParser(false)) {
            int count = Integer.parseInt(record.get(2));
            totalBirth += count;
            countOfNames++;
            if (record.get(1).equals("F")) {
                countOfGirlsNames++;
                totalGirls += count;
            } else {
                countOfBoysNames++;
                totalBoys += count;
            }
        }
        return "Boys: " + totalBoys + "\n" +
                "Girls: " + totalGirls + "\n" +
                "Total Birth: " + totalBirth + "\n" +
                "Girls names count: " + countOfGirlsNames + "\n" +
                "Boys names count: " + countOfBoysNames + "\n" +
                "Names count: " + countOfNames;
    }

    /**
     * Method named getRank that has three parameters: an integer named year, a string named name, and a string named
     * gender (F for female and M for male). This method returns the rank of the name in the file for the given gender,
     * where rank 1 is the name with the largest number of births. If the name is not in the file, then -1 is returned.
     * For example, in the file "yob2012short.csv", given the name Mason, the year 2012 and the gender ‘M’, the number
     * returned is 2, as Mason is the boys name with the second highest number of births. Given the name Mason, the
     * year 2012 and the gender ‘F’, the number returned is -1 as Mason does not appear with an F in that file.
     */
    public int getRank(int nameYear, String name, String gender) {
        FileResource fr = new FileResource("us_babynames/us_babynames_by_year/yob" + nameYear + ".csv");
        int rank = 0;

        for (CSVRecord rec : fr.getCSVParser(false)) {
            if (rec.get(1).equals(gender.toUpperCase())) {
                rank++;
            }
            if (rec.get(0).equals(name) && rec.get(1).equals(gender)) {
                return rank;
            }
        }
        return -1;
    }

    /**
     * Method named getName that has three parameters: an integer named year, an integer named rank, and a string named
     * gender (F for female and M for male). This method returns the name of the person in the file at this rank, for
     * the given gender, where rank 1 is the name with the largest number of births. If the rank does not exist in the
     * file, then “NO NAME” is returned.
     */
    public String getName(int year, int rank, String gender) {
        if (year >= 1800 && year <= 2014) {
            FileResource fr = new FileResource("us_babynames/us_babynames_by_year/yob" + year + ".csv");
            int currentCount = 0;
            int temp = 0;
            Map<Integer, String> mapOfNames = new LinkedHashMap<>();
            for (CSVRecord record : fr.getCSVParser(false)) {
                String name = record.get(0);
                int countOfBirth = Integer.parseInt(record.get(2));
                String gen = record.get(1);
                if (gen.equals(gender.toUpperCase())) {
                    if (countOfBirth < temp) {
                        currentCount++;
                    } else {
                        temp = countOfBirth;
                    }
                    mapOfNames.put(currentCount, name);
                }
            }

            return mapOfNames.get(rank - 1);
        }
        return "NOT FOUND!";
    }

    /**
     * Method named whatIsNameInYear that has four parameters: a string name, an integer named year representing
     * the year that name was born, an integer named newYear and a string named gender (F for female and M for male).
     * This method determines what name would have been named if they were born in a different year, based on the same
     * popularity. That is, you should determine the rank of name in the year they were born, and then print the name
     * born in newYear that is at the same rank and same gender. For example, using the files "yob2012short.csv" and
     * "yob2014short.csv", notice that in 2012 Isabella is the third most popular girl's name. If Isabella was born in
     * 2014 instead, she would have been named Sophia, the third most popular girl's name that year. The output might
     * look like this:
     * Your name in 2014 year is: Sophia.
     */
    public String whatIsNameInYear(String name, int year, int newYear, String gender) {
        try {
            String newName = getName(newYear, getRank(year, name, gender), gender);
            String result = "Your name in " + newYear + " year is: " + newName;
            System.out.println(result);
            return result;
        } catch (edu.duke.ResourceException notFound) {
            System.out.println("NOT FOUND!");
        }
        return "NOT FOUND!";
    }

    /**
     * Method yearOfHighestRank that has two parameters: a string name, and a string named gender (F for female and M
     * for male). This method selects a range of files to process and returns an integer, the year with the highest
     * rank for the name and gender. If the name and gender are not in any of the selected files, it should return -1.
     * For example, calling yearOfHighestRank with name Mason and gender ‘M’ and selecting the three test files above
     * results in returning the year 2012. That is because Mason was ranked the 2nd most popular name in 2012, ranked
     * 4th in 2013 and ranked 3rd in 2014. His highest ranking was in 2012.
     */
    public int yearOfHighestRank(String name, String gender) {
        Map<Integer, Integer> map = new LinkedHashMap<>();
        DirectoryResource dr = new DirectoryResource();
        for (File file : dr.selectedFiles()) {
            String nameOfFile = file.getName();
            nameOfFile = nameOfFile.replaceAll("[^0-9]+", "");
            int currRank = getRank(Integer.parseInt(nameOfFile), name, gender.toUpperCase());
            if (currRank != -1) {
                map.put(currRank, Integer.parseInt(nameOfFile));
            }
        }

        return map.get(Collections.min(map.keySet()));
    }

    /**
     * Method getAverageRank that has two parameters: a string name, and a string named gender (F for female and M for
     * male). This method selects a range of files to process and returns a double representing the average rank of the
     * name and gender over the selected files. It should return -1.0 if the name is not ranked in any of the selected
     * files. For example calling getAverageRank with name Mason and gender ‘M’ and selecting the three test files
     * above results in returning 3.0, as he is rank 2 in the year 2012, rank 4 in 2013 and rank 3 in 2014. As another
     * example, calling getAverageRank with name Jacob and gender ‘M’ and selecting the three test files above results
     * in returning 2.66.
     */
    public double getAverageRank(String name, String gender) {
        DirectoryResource dr = new DirectoryResource();
        List<Double> list = new ArrayList<>();
        for (File file : dr.selectedFiles()) {
            String nameOfFile = file.getName();
            nameOfFile = nameOfFile.replaceAll("[^0-9]+", "");
            list.add((double) getRank(Integer.parseInt(nameOfFile), name, gender.toUpperCase()));
        }
        double sum = 0;

        for (Double d : list) {
            sum = sum + d;
        }

        return sum / list.size();
    }

    /**
     * method getTotalBirthsRankedHigher that has three parameters: an integer named year, a string named name, and a
     * string named gender (F for female and M for male). This method returns an integer, the total number of births of
     * those names with the same gender and same year who are ranked higher than name. For example, if
     * getTotalBirthsRankedHigher accesses the "yob2012short.csv" file with name set to “Ethan”, gender set to “M”, and
     * year set to 2012, then this method should return 15, since Jacob has 8 births and Mason has 7 births, and those
     * are the only two ranked higher than Ethan.
     */
    public int getTotalBirthsRankedHigher(int year, String name, String gender) {
        FileResource fr = new FileResource("us_babynames/us_babynames_test/yob" + year + "short.csv");
        List<Integer> list = new ArrayList<>();
        int rank = getRank(year, name, gender.toUpperCase());
        int count = 0;

        for (CSVRecord record : fr.getCSVParser(false)) {
            if (record.get(1).equals(gender.toUpperCase()) && count != rank - 1) {
                count++;
                int numOfBirth = Integer.parseInt(record.get(2));
                list.add(numOfBirth);
            }
        }
        int sum = 0;
        for (Integer i : list) {
            sum += i;
        }
        return sum;
    }
}
