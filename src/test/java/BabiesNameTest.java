import edu.duke.FileResource;
import org.junit.Assert;
import org.junit.Test;

public class BabiesNameTest {
    BabiesName babiesName = new BabiesName();

    @Test
    public void testTotalBirth() {
       String result = babiesName.totalBirth(new FileResource("us_babynames/us_babynames_by_year/yob1990.csv"));
        Assert.assertEquals("Boys: 2052543\n" +
                "Girls: 1944175\n" +
                "Total Birth: 3996718\n" +
                "Girls names count: 15232\n" +
                "Boys names count: 9482\n" +
                "Names count: 24714",result);
    }

    @Test
    public void testGetRank() {
        int result = babiesName.getRank(1986, "Alexander", "M");
        Assert.assertNotEquals(-1,result);
        Assert.assertEquals(41,result);
    }

    @Test
    public void testGetName() {
        String result = babiesName.getName(1982, 111, "f");
        Assert.assertEquals("Alison", result);
        Assert.assertNotNull(result);
    }

    @Test
    public void testWhatIsNameInYear() {
       String result = babiesName.whatIsNameInYear("Isabella", 2012, 2014, "F");
       Assert.assertEquals("Your name in 2014 year is: Sophia", result);
       //Assert.assertEquals("NOT FOUND!", result);
    }

    /**
     * For successful passing this test you should at the same time select all year files in:
     * src/main/resources/us_babynames/us_babynames_by_year directory.
     */
    @Test
    public void testYearOfHighestRank() {
        int result = babiesName.yearOfHighestRank("Mason", "m");
        Assert.assertEquals(2012, result);
    }
    /**
     * For successful passing this test you should at the same time select 2012-2014 years in:
     * src/main/resources/us_babynames/us_babynames_by_year directory.
     */
    @Test
    public void testGetAverageRank() {
        double result = babiesName.getAverageRank("Mason", "m");
        Assert.assertEquals(3.0, result, 0.0);
    }

    @Test
    public void testGetTotalBirthsRankedHigher() {
        int result = babiesName.getTotalBirthsRankedHigher(2012, "Ethan", "m");
        Assert.assertEquals(15, result);
    }
}

