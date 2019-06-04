package ru.nadin.tests.plugin.testLink.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "testOccurrence")
@XmlAccessorType(XmlAccessType.FIELD)
public class TestOccurrence {
   @XmlAttribute
   private String id;
   @XmlAttribute
   private String name;
   @XmlAttribute
   private String status;

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getStatus() {
      return status;
   }

   public void setStatus(String status) {
      this.status = status;
   }

   @Override
   public String toString() {
      return "test id=" + getId() + "; name=" + getName() + "; status" + getStatus();
   }
}
