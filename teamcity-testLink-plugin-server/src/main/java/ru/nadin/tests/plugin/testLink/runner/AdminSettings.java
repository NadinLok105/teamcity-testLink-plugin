package ru.nadin.tests.plugin.testLink.runner;

import jetbrains.buildServer.serverSide.ServerPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nadin.tests.plugin.testLink.Constants;

import java.io.*;
import java.util.Properties;

public class AdminSettings {

   private Logger logger = LoggerFactory.getLogger(AdminSettings.class);

   public ServerPaths serverPaths;
   private String testLinkUserKey = "";
   private String testLinkCustomField = "";
   private String testLinkUrl = "";

   public AdminSettings(ServerPaths serverPaths) {
      this.serverPaths = serverPaths;
   }

   public void init() {
      loadProperties();
   }

   public void saveProperties() throws IOException {
      File keyFile = propFile();
      if (keyFile == null) {
         throw new RuntimeException("Property file not found");
      }

      Properties prop = new Properties();
      FileOutputStream fos = new FileOutputStream(keyFile);
      prop.put(Constants.TESTLINK_USER_KEY, this.testLinkUserKey);
      prop.put(Constants.TESTLINK_CUSTOM_FIELD, this.testLinkCustomField);
      prop.put(Constants.TESTLINK_URL, this.testLinkUrl);
      prop.store(fos, null);
      fos.close();
   }

   public void loadProperties() {
      File keyFile = propFile();
      if (keyFile == null) {
         return;
      }

      FileInputStream inFile = null;
      try {
         inFile = new FileInputStream(keyFile);
         Properties prop = new Properties();
         prop.load(inFile);
         this.testLinkUserKey = prop.getProperty(Constants.TESTLINK_USER_KEY);
         this.testLinkCustomField = prop.getProperty(Constants.TESTLINK_CUSTOM_FIELD);
         this.testLinkUrl = prop.getProperty(Constants.TESTLINK_URL);
         inFile.close();
      } catch (IOException e) {
         logger.error("Cannot load configuration: " + e.getMessage());
      } finally {
         try {
            if (inFile != null) {
               inFile.close();
            }
         } catch (IOException e) {
            logger.error("Cannot close " + keyFile.getAbsolutePath() + ": " + e.getMessage());
         }
      }
   }

   private File propFile() {
      File keyFile = new File(serverPaths.getConfigDir() + Constants.PROPERTIES_FILE);
      if (!keyFile.exists()) {
         try {
            boolean created = keyFile.createNewFile();
            if (!created) {
               logger.error("Cannot create configuration file, file already exists!");
            }
         } catch (IOException e) {
            logger.error("Cannot create configuration file! Error is: " + e.getMessage());
            return null;
         }
      }
      return keyFile;
   }


   public String getTestLinkUserKey() {
      return testLinkUserKey;
   }

   public void setTestLinkUserKey(String testLinkUserKey) {
      this.testLinkUserKey = testLinkUserKey;
   }

   public String getTestLinkCustomField() {
      return testLinkCustomField;
   }

   public void setTestLinkCustomField(String testLinkCustomField) {
      this.testLinkCustomField = testLinkCustomField;
   }

   public String getTestLinkUrl() {
      return testLinkUrl;
   }

   public void setTestLinkUrl(String testLinkUrl) {
      this.testLinkUrl = testLinkUrl;
   }
}
