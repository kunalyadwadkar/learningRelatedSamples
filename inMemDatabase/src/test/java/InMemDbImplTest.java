import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class InMemDbImplTest {

    private InMemDb<String, Integer> underTest;

    @BeforeMethod
    public void setUp() throws Exception {
        underTest = new InMemDbImpl<>();

    }

    @Test
    public void testCase1() throws Exception {

        underTest.set("a", 10);
        assertThat(underTest.get("a"), is(10));

        underTest.delete("a");

        assertThat(underTest.get("a"), is(nullValue()));

    }


    @Test
    public void testCase2() throws Exception {

        underTest.set("a", 10);
        underTest.set("b", 10);
        assertThat(underTest.count(10), is(2));
        assertThat(underTest.count(20), is(0));

        underTest.delete("a");
        assertThat(underTest.count(10), is(1));

        underTest.set("b", 30);
        assertThat(underTest.count(10), is(0));
    }


    @Test
    public void testCase3() throws Exception {
        underTest.begin();

        underTest.set("a", 10);
        assertThat(underTest.get("a"), is(10));
        underTest.begin();
        underTest.set("a", 20);
        assertThat(underTest.get("a"), is(20));
        underTest.rollback();
        assertThat(underTest.get("a"), is(10));
        underTest.rollback();
        assertThat(underTest.get("a"), is(nullValue()));
    }

    @Test
    public void testCase4() throws Exception {
        underTest.begin();
        underTest.set("a", 30);
        underTest.begin();
        underTest.set("a", 40);
        underTest.commit();
        assertThat(underTest.get("a"), is(40));


    }


    @Test(expectedExceptions = NoSuchTransactionException.class)
    public void testCase5() throws Exception {
        underTest.begin();
        underTest.set("a", 30);
        underTest.begin();
        underTest.set("a", 40);
        underTest.commit();
        assertThat(underTest.get("a"), is(40));
        underTest.rollback();

    }

    @Test
    public void testCase6() throws Exception {

        underTest.set("a", 10);
        underTest.begin();
        assertThat(underTest.count(10), is(1));
        underTest.begin();
        underTest.delete("a");
        assertThat(underTest.count(10), is(0));
        underTest.rollback();
        assertThat(underTest.count(10), is(1));
    }
}