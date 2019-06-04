package ru.nadin.tests.plugin.testLink.client;

import jetbrains.buildServer.agent.AgentRunningBuild;
import jetbrains.buildServer.agent.BuildProgressLogger;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import ru.nadin.tests.plugin.testLink.data.TestOccurrences;
import ru.nadin.tests.plugin.testLink.exception.InternalException;
import ru.nadin.tests.plugin.testLink.Constants;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;

public class TeamCityClient {
   private AgentRunningBuild agentRunningBuild;
   private HttpClient client;
   private final BuildProgressLogger logger;
   private String mainUrl;

   public TeamCityClient(AgentRunningBuild agentRunningBuild) throws InternalException {
      this.agentRunningBuild = agentRunningBuild;
      this.logger = agentRunningBuild.getBuildLogger();
      this.client = getTeamityRestClient();
      this.mainUrl = agentRunningBuild.getSharedBuildParameters().getEnvironmentVariables().get(Constants.TEAMCITY_URL_PARAMETER_NAME);
      if (StringUtils.isEmpty(mainUrl))
         throw new InternalException("Environment variable [env." + Constants.TEAMCITY_URL_PARAMETER_NAME + "] not set!");
   }

   public TestOccurrences getTestsResults() {
      long buildId = agentRunningBuild.getBuildId();
      HttpUriRequest request = new HttpGet(mainUrl + Constants.TEAMCITY_REST_API_TESTOCCURANCES + "(id:" + buildId + ")");
      TestOccurrences allTests = null;
      try {
         HttpResponse response = client.execute(request);
         allTests = trasnformResponse(response.getEntity());
      } catch (Exception e) {
         logger.internalError(e.getMessage(), e.getClass().getCanonicalName(), e);
         logger.error(e.getStackTrace().toString());
      }
      return allTests;
   }

   private TestOccurrences trasnformResponse(HttpEntity entity) throws IOException, JAXBException {
      JAXBContext jaxbContext = JAXBContext.newInstance(TestOccurrences.class);
      Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
      return (TestOccurrences) jaxbUnmarshaller.unmarshal(entity.getContent());
   }

   private HttpClient getTeamityRestClient() {
      String userId = agentRunningBuild.getSharedBuildParameters().getSystemProperties()
              .get(Constants.TEAMCITY_USERID_PARAMETER_NAME);
      logger.message("Connecting to TeamCity with userId = " + userId);
      String password = agentRunningBuild.getSharedBuildParameters().getSystemProperties()
              .get(Constants.TEAMCITY_PASSWORD_PARAMETER_NAME);

      CredentialsProvider provider = new BasicCredentialsProvider();
      UsernamePasswordCredentials credentials
              = new UsernamePasswordCredentials(userId, password);
      provider.setCredentials(AuthScope.ANY, credentials);

      return HttpClientBuilder.create()
              .setDefaultCredentialsProvider(provider)
              .build();
   }
}
