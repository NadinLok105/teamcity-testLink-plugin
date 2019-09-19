package ru.nadin.tests.plugin.testLink.data;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "testOccurrences")
@XmlAccessorType(XmlAccessType.FIELD)
public class TestOccurrences {
   @XmlElement(name = "testOccurrence")
   private List<TestOccurrence> testOccurrences = null;
   @XmlAttribute
   private String nextHref;

   public List<TestOccurrence> getTestOccurrences() {
      return testOccurrences;
   }

   public void setTestOccurrences(List<TestOccurrence> testOccurrences) {
      this.testOccurrences = testOccurrences;
   }

   @Override
   public String toString() {
      StringBuffer buffer = new StringBuffer("tests = ");
      getTestOccurrences().stream().forEach(test -> buffer.append("[").append(test.toString()).append("]"));
      return buffer.toString();
   }

   public String getNextHref() {
      return nextHref;
   }

   public void setNextHref(String nextHref) {
      this.nextHref = nextHref;
   }
}
