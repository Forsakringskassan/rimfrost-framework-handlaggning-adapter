package se.fk.rimfrost.framework.handlaggning.adapter;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.client.Client;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.glassfish.jersey.apache5.connector.Apache5ConnectorProvider;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.glassfish.jersey.client.proxy.WebResourceFactory;
import se.fk.rimfrost.framework.handlaggning.exception.HandlaggningException;
import se.fk.rimfrost.framework.handlaggning.model.Handlaggning;
import se.fk.rimfrost.framework.handlaggning.model.HandlaggningUpdate;
import se.fk.rimfrost.jaxrsspec.controllers.generatedsource.HandlaggningControllerApi;
import java.util.UUID;

@ApplicationScoped
public class HandlaggningAdapter
{

   @ConfigProperty(name = "handlaggning.api.base-url")
   String handlaggningBaseUrl;

   private HandlaggningControllerApi handlaggningClient;

   private Client client;

   @Inject
   HandlaggningMapper handlaggningMapper;

   @PostConstruct
   void init()
   {
      ClientConfig clientConfig = new ClientConfig();
      clientConfig.connectorProvider(new Apache5ConnectorProvider());
      clientConfig.register(AdapterObjectMapperProvider.class);
      this.client = JerseyClientBuilder.createClient(clientConfig);
      this.handlaggningClient = WebResourceFactory.newResource(
            HandlaggningControllerApi.class,
            client.target(this.handlaggningBaseUrl));
   }

   @PreDestroy
   void destroy()
   {
      this.handlaggningClient = null;

      if (this.client != null)
      {
         this.client.close();
         this.client = null;
      }
   }

   public Handlaggning readHandlaggning(UUID handlaggningId) throws HandlaggningException
   {
      try
      {
         var getHandlaggningResponse = handlaggningClient.getHandlaggning(handlaggningId);
         if (getHandlaggningResponse == null)
         {
            throw new HandlaggningException(HandlaggningException.ErrorType.UNEXPECTED_ERROR,
                  "Oväntat fel vid hämtande av handläggning, response är null för handläggningId: " + handlaggningId);
         }
         return handlaggningMapper.toHandlaggning(getHandlaggningResponse.getHandlaggning());
      }
      catch (ProcessingException e)
      {
         throw new HandlaggningException(HandlaggningException.ErrorType.SERVICE_UNAVAILABLE,
               "Kunde inte nå handläggningstjänsten vid hämtning av handläggning", e);
      }
      catch (WebApplicationException e)
      {
         var status = e.getResponse().getStatus();
         switch (status)
         {
            case 404:
               throw new HandlaggningException(HandlaggningException.ErrorType.NOT_FOUND,
                     "Hittade ingen handläggning med id: " + handlaggningId, e);
            case 400:
               throw new HandlaggningException(HandlaggningException.ErrorType.BAD_REQUEST,
                     "Felaktig förfrågan vid hämtning av handläggning med id: " + handlaggningId, e);
            default:
               throw new HandlaggningException(HandlaggningException.ErrorType.UNEXPECTED_ERROR,
                     "Oväntat fel vid hämtning av handläggning med id: " + handlaggningId + ", status: " + status, e);
         }
      }
   }

   public HandlaggningUpdate updateHandlaggning(HandlaggningUpdate handlaggningUpdate) throws HandlaggningException
   {
      var handlaggningId = handlaggningUpdate.handlaggning().id();
      try
      {
         var apiHandlaggningUpdate = handlaggningMapper.toApiHandlaggningUpdate(handlaggningUpdate);
         var putHandlaggningResponse = handlaggningClient.putHandlaggning(handlaggningId, apiHandlaggningUpdate);
         if (putHandlaggningResponse == null)
         {
            throw new HandlaggningException(HandlaggningException.ErrorType.UNEXPECTED_ERROR,
                  "Oväntat fel vid uppdatering av handläggning, response är null för handläggningId: " + handlaggningId);
         }
         return handlaggningMapper.toHandlaggningUpdate(putHandlaggningResponse);
      }
      catch (ProcessingException e)
      {
         throw new HandlaggningException(HandlaggningException.ErrorType.SERVICE_UNAVAILABLE,
               "Kunde inte nå handläggningstjänsten vid uppdatering av handläggning", e);
      }
      catch (WebApplicationException e)
      {
         var status = e.getResponse().getStatus();
         switch (status)
         {
            case 404:
               throw new HandlaggningException(HandlaggningException.ErrorType.NOT_FOUND,
                     "Ingen handläggning hittades med id: " + handlaggningId, e);
            case 400:
               throw new HandlaggningException(HandlaggningException.ErrorType.BAD_REQUEST,
                     "Felaktig förfrågan vid uppdatering av handläggning med id: " + handlaggningId, e);
            case 409:
               throw new HandlaggningException(HandlaggningException.ErrorType.CONFLICT,
                     "Konflikt vid uppdatering av handläggning med id: " + handlaggningId, e);
            default:
               throw new HandlaggningException(HandlaggningException.ErrorType.UNEXPECTED_ERROR,
                     "Oväntat fel vid uppdatering av handläggning med id: " + handlaggningId + ", status: " + status, e);
         }
      }
   }

}
