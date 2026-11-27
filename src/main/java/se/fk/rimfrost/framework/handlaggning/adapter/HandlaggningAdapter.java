package se.fk.rimfrost.framework.handlaggning.adapter;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.client.Client;
import org.glassfish.jersey.apache5.connector.Apache5ConnectorProvider;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.glassfish.jersey.client.proxy.WebResourceFactory;
import se.fk.rimfrost.framework.handlaggning.exception.HandlaggningException;
import se.fk.rimfrost.framework.handlaggning.model.Handlaggning;
import se.fk.rimfrost.framework.handlaggning.model.HandlaggningUpdate;
import se.fk.rimfrost.jaxrsspec.controllers.generatedsource.HandlaggningControllerApi;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

@ApplicationScoped
public class HandlaggningAdapter
{
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
   }

   @PreDestroy
   void destroy()
   {
      if (this.client != null)
      {
         this.client.close();
         this.client = null;
      }
   }

   public Handlaggning readHandlaggning(String baseUrl, UUID handlaggningId) throws HandlaggningException
   {
      try
      {
         var handlaggningClient = createHandlaggningClient(baseUrl);
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

   public HandlaggningUpdate updateHandlaggning(String baseUrl, HandlaggningUpdate handlaggningUpdate)
         throws HandlaggningException
   {
      var handlaggningId = handlaggningUpdate.handlaggning().id();
      try
      {
         var handlaggningClient = createHandlaggningClient(baseUrl);
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

   private HandlaggningControllerApi createHandlaggningClient(String baseUrl) throws HandlaggningException
   {
      if (baseUrl == null)
      {
         throw new HandlaggningException(HandlaggningException.ErrorType.UNEXPECTED_ERROR,
               "Fel vid skapande av handläggningsklient: Null bas url stöds inte");
      }

      try
      {
         var baseUri = new URI(baseUrl);

         if (!("http".equalsIgnoreCase(baseUri.getScheme()) || "https".equalsIgnoreCase(baseUri.getScheme()))
               || baseUri.getHost() == null)
         {
            throw new HandlaggningException(HandlaggningException.ErrorType.UNEXPECTED_ERROR,
                  "Fel vid skapande av handläggningsklient: Bas url måste vara en absolut http(s)-url");
         }

         return WebResourceFactory.newResource(
               HandlaggningControllerApi.class,
               client.target(baseUri));
      }
      catch (IllegalStateException | URISyntaxException ex)
      {
         throw new HandlaggningException(HandlaggningException.ErrorType.UNEXPECTED_ERROR,
               "Oväntat fel vid skapande av handläggningsklient", ex);
      }
   }
}
