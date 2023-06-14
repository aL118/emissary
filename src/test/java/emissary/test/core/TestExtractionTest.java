package emissary.test.core;

import emissary.place.IServiceProviderPlace;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaders;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

class TestExtractionTest extends UnitTest {

    @Test
    void testCheckStringValueForCollection() throws JDOMException, IOException {
        SAXBuilder builder = new SAXBuilder(XMLReaders.NONVALIDATING);
        String resourceName = "/emissary/test/core/TestExtractionTest.xml";
        InputStream inputStream = TestExtractionTest.class.getResourceAsStream(resourceName);
        assertNotNull(inputStream, "Could not locate: " + resourceName);
        Document answerDoc = builder.build(inputStream);
        inputStream.close();

        WhyDoYouMakeMeDoThisExtractionTest test = new WhyDoYouMakeMeDoThisExtractionTest("nonsense");

        Element meta = answerDoc.getRootElement().getChild("answers").getChild("meta");
        test.checkStringValue(meta, "1;2;3;4;5;6;7", "testCheckStringValueForCollection");
        test.checkStringValue(meta, "1;3;4;2;5;6;7", "testCheckStringValueForCollection");
        test.checkStringValue(meta, "1;3;2;4;7;5;6", "testCheckStringValueForCollection");
        test.checkStringValue(meta, "7;6;5;4;3;2;1", "testCheckStringValueForCollection");
    }

    @Test
    void testCheckStringValueForCollectionFailure() throws JDOMException, IOException {
        SAXBuilder builder = new SAXBuilder(XMLReaders.NONVALIDATING);
        String resourceName = "/emissary/test/core/TestExtractionTest.xml";
        InputStream inputStream = TestExtractionTest.class.getResourceAsStream(resourceName);
        assertNotNull(inputStream, "Could not locate: " + resourceName);
        Document answerDoc = builder.build(inputStream);
        inputStream.close();

        WhyDoYouMakeMeDoThisExtractionTest test = new WhyDoYouMakeMeDoThisExtractionTest("nonsense");

        Element meta = answerDoc.getRootElement().getChild("answers").getChild("meta");

        assertThrows(AssertionError.class, () -> test.checkStringValue(meta, "7;0;0;0;2;1", "testCheckStringValueForCollection"));
    }

    @Test
    void testCheckStringValueBangIndex() throws IOException, JDOMException {
        SAXBuilder builder = new SAXBuilder(XMLReaders.NONVALIDATING);
        String resourceName = "/emissary/test/core/TestExtractionTest.xml";
        InputStream inputStream = TestExtractionTest.class.getResourceAsStream(resourceName);
        assertNotNull(inputStream, "Could not locate: " + resourceName);
        Document answerDoc = builder.build(inputStream);
        inputStream.close();
        String matchMode = "!index";

        WhyDoYouMakeMeDoThisExtractionTest test = new WhyDoYouMakeMeDoThisExtractionTest("nonsense");

        List<Element> dataList = answerDoc.getRootElement().getChild("answers").getChildren("data");
        Element bangIndexData = getAttributeFromDataChild(dataList, matchMode);

        test.checkStringValue(bangIndexData, "time:20221229", "testCheckStringValue!IndexTrue");
        assertThrows(AssertionError.class, () -> test.checkStringValue(bangIndexData, "timestamp:20221229", "testCheckStringValue!IndexFalse"));

        matchMode = "!contains";

        Element bangContainsData = getAttributeFromDataChild(dataList, matchMode);

        test.checkStringValue(bangContainsData, "time:20221229", "testCheckStringValue!ContainsTrue");
        assertThrows(AssertionError.class, () -> test.checkStringValue(bangContainsData, "timestamp:20221229", "testCheckStringValue!ContainsFalse"));

    }

    private Element getAttributeFromDataChild(List<Element> dataList, String matchMode) {
        Element data = null;
        // Having different matchModes in the same data necessitates having to go through each child and filter for the
        // correct
        // one
        try {
            data = dataList.stream().filter(item -> item.getAttribute("matchMode").getValue().equals(matchMode)).findFirst().get();
        } catch (NoSuchElementException e) {
            fail("Attribute " + matchMode + " does not exist in data");
        }
        return data;
    }

    public static class WhyDoYouMakeMeDoThisExtractionTest extends ExtractionTest {

        public WhyDoYouMakeMeDoThisExtractionTest(String crazy) throws IOException {
            super(crazy);
        }

        @Override
        public IServiceProviderPlace createPlace() throws IOException {
            return null;
        }
    }
}
