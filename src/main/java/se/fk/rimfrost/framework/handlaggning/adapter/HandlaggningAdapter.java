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

   @ConfigProperty(name = "kundbehovsflode.api.base-url")
   String kundbehovsflodeBaseUrl;

   private HandlaggningControllerApi kundbehovsClient;

   @Inject
   HandlaggningMapper handlaggningMapper;

   @PostConstruct
   void init()
   {
      ClientConfig clientConfig = new ClientConfig();
      clientConfig.connectorProvider(new ApacheConnectorProvider());
      Client client = ClientBuilder.newClient(clientConfig);

      this.kundbehovsClient = WebResourceFactory.newResource(
            HandlaggningControllerApi.class,
            client.target(this.kundbehovsflodeBaseUrl));
   }

   public HandlaggningResponse getKundbehovsflodeInfo(HandlaggningRequest handlaggningRequest)
   {
      var apiResponse = kundbehovsClient.getHandlaggning(handlaggningRequest.kundbehovsflodeId());
      return handlaggningMapper.toHandlaggningResponse(apiResponse);
   }

   public void putKundbehovsflode(PutHandlaggningUppgiftRequest request)
   {
      var apiRequest = handlaggningMapper.toPutHandlaggningRequest(request);
      LOGGER.info("putKundbehovsflode " + request);
      try
      {
         kundbehovsClient.putHandlaggning(request.kundbehovsflodeId(), apiRequest);
      }
      catch (Throwable t)
      {
         t.printStackTrace();
         throw t;
      }
      LOGGER.info("putKundbehovsflode executed");
   }

   public void patchKundbehovsflode(PatchErsattningRequest request)
   {
      var apiRequest = handlaggningMapper.toPatchHandlaggningRequest(request);
      LOGGER.info("patchKundbehovsflode " + request);
      try
      {
         kundbehovsClient.patchHandlaggning(request.kundbehovsflodeId(), apiRequest);
      }
      catch (Throwable t)
      {
         t.printStackTrace();
         throw t;
      }
      LOGGER.info("patchKundbehovsflode executed");
   }
}
