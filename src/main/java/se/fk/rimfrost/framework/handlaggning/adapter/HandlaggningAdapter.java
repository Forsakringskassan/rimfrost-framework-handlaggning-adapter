package se.fk.rimfrost.framework.handlaggning.adapter;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.glassfish.jersey.apache.connector.ApacheConnectorProvider;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.proxy.WebResourceFactory;
import se.fk.rimfrost.framework.handlaggning.adapter.dto.HandlaggningRequest;
import se.fk.rimfrost.framework.handlaggning.adapter.dto.HandlaggningResponse;
import se.fk.rimfrost.framework.handlaggning.adapter.dto.PatchErsattningRequest;
import se.fk.rimfrost.framework.handlaggning.adapter.dto.PutHandlaggningUppgiftRequest;
import se.fk.rimfrost.jaxrsspec.controllers.generatedsource.HandlaggningControllerApi;
import static io.quarkus.arc.impl.UncaughtExceptions.LOGGER;

@SuppressWarnings("unused")
@ApplicationScoped
public class HandlaggningAdapter
{

   @ConfigProperty(name = "handlaggning.api.base-url")
   String handlaggningBaseUrl;

   private HandlaggningControllerApi handlaggningClient;

   @Inject
   HandlaggningMapper handlaggningMapper;

   @PostConstruct
   void init()
   {
      ClientConfig clientConfig = new ClientConfig();
      clientConfig.connectorProvider(new ApacheConnectorProvider());
      Client client = ClientBuilder.newClient(clientConfig);

      this.handlaggningClient = WebResourceFactory.newResource(
            HandlaggningControllerApi.class,
            client.target(this.handlaggningBaseUrl));
   }

   public HandlaggningResponse getHandlaggningInfo(HandlaggningRequest handlaggningRequest)
   {
      var apiResponse = handlaggningClient.getHandlaggning(handlaggningRequest.handlaggningId());
      return handlaggningMapper.toHandlaggningResponse(apiResponse);
   }

   public void putHandlaggning(PutHandlaggningUppgiftRequest request)
   {
      var apiRequest = handlaggningMapper.toPutHandlaggningRequest(request);
      LOGGER.info("putHandlaggning " + request);
      try
      {
         handlaggningClient.putHandlaggning(request.handlaggningId(), apiRequest);
      }
      catch (Throwable t)
      {
         t.printStackTrace();
         throw t;
      }
      LOGGER.info("putHandlaggning executed");
   }

   public void patchHandlaggning(PatchErsattningRequest request)
   {
      var apiRequest = handlaggningMapper.toPatchHandlaggningRequest(request);
      LOGGER.info("patchHandlaggning " + request);
      try
      {
         handlaggningClient.patchHandlaggning(request.handlaggningId(), apiRequest);
      }
      catch (Throwable t)
      {
         t.printStackTrace();
         throw t;
      }
      LOGGER.info("patchHandlaggning executed");
   }
}
