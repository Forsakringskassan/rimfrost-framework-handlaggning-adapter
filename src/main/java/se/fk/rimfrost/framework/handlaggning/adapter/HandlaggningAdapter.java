package se.fk.rimfrost.framework.handlaggning.adapter;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.glassfish.jersey.apache5.connector.Apache5ConnectorProvider;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.proxy.WebResourceFactory;
import se.fk.rimfrost.framework.handlaggning.exception.HandlaggningException;
import se.fk.rimfrost.framework.handlaggning.model.Handlaggning;
import se.fk.rimfrost.framework.handlaggning.model.HandlaggningUpdate;
import se.fk.rimfrost.jaxrsspec.controllers.generatedsource.HandlaggningControllerApi;
import java.util.UUID;

@SuppressWarnings("unused")
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
      this.client = ClientBuilder.newClient(clientConfig);
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
      catch (NotFoundException e)
      {
         throw new HandlaggningException(HandlaggningException.ErrorType.NOT_FOUND,
               "Hittade ingen handläggning med id: " + handlaggningId, e);
      }
      catch (BadRequestException e)
      {
         throw new HandlaggningException(HandlaggningException.ErrorType.BAD_REQUEST,
               "Felaktig förfrågan vid hämtning av handläggning med id: " + handlaggningId, e);
      }
      catch (ProcessingException e)
      {
         throw new HandlaggningException(HandlaggningException.ErrorType.SERVICE_UNAVAILABLE,
               "Kunde inte nå handläggningstjänsten vid hämtning av handläggning", e);
      }
      catch (WebApplicationException e)
      {
         throw new HandlaggningException(HandlaggningException.ErrorType.UNEXPECTED_ERROR,
               "Oväntat fel vid hämtning av handläggning med id: " + handlaggningId + ", status: " + e.getResponse().getStatus(),
               e);
      }
   }

   public HandlaggningUpdate updateHandlaggning(HandlaggningUpdate handlaggningUpdate) throws HandlaggningException
   {
      try
      {
         var putHandlaggningRequest = handlaggningMapper.toPutHandlaggningRequest(handlaggningUpdate);
         var putHandlaggningResponse = handlaggningClient.putHandlaggning(handlaggningUpdate.id(), putHandlaggningRequest);
         if (putHandlaggningResponse == null)
         {
            throw new HandlaggningException(HandlaggningException.ErrorType.UNEXPECTED_ERROR,
                  "Oväntat fel vid uppdatering av handläggning, response är null för handläggningId: " + handlaggningUpdate.id());
         }
         return handlaggningMapper.toHandlaggningUpdate(putHandlaggningResponse.getHandlaggning());
      }
      catch (NotFoundException e)
      {
         throw new HandlaggningException(HandlaggningException.ErrorType.NOT_FOUND,
               "Ingen handläggning hittades med id: " + handlaggningUpdate.id(), e);
      }
      catch (BadRequestException e)
      {
         throw new HandlaggningException(HandlaggningException.ErrorType.BAD_REQUEST,
               "Felaktig förfrågan vid uppdatering av handläggning med id: " + handlaggningUpdate.id(), e);
      }
      catch (ProcessingException e)
      {
         throw new HandlaggningException(HandlaggningException.ErrorType.SERVICE_UNAVAILABLE,
               "Kunde inte nå handläggningstjänsten vid uppdatering av handläggning", e);
      }
      catch (WebApplicationException e)
      {
         throw new HandlaggningException(HandlaggningException.ErrorType.UNEXPECTED_ERROR,
               "Oväntat fel vid uppdatering av handläggning med id: " + handlaggningUpdate.id() + ", status: "
                     + e.getResponse().getStatus(),
               e);
      }
   }

}
