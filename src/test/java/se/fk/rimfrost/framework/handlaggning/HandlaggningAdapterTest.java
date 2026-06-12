package se.fk.rimfrost.framework.handlaggning;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.http.Fault;
import io.quarkus.test.InjectMock;
import io.quarkus.test.component.QuarkusComponentTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import se.fk.rimfrost.framework.handlaggning.adapter.HandlaggningAdapter;
import se.fk.rimfrost.framework.handlaggning.adapter.HandlaggningMapper;
import se.fk.rimfrost.framework.handlaggning.exception.HandlaggningException;

import java.util.UUID;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static se.fk.rimfrost.framework.handlaggning.TestData.createModelCreateYrkandeRequest;
import static se.fk.rimfrost.framework.handlaggning.TestData.createModelHandlaggning;
import static se.fk.rimfrost.framework.handlaggning.TestData.createModelHandlaggningUpdate;
import static se.fk.rimfrost.framework.handlaggning.TestUtils.toApiPutHandlaggningRequest;

@QuarkusComponentTest
public class HandlaggningAdapterTest
{
   private static WireMockServer server;

   @Inject
   HandlaggningAdapter handlaggningAdapter;

   @InjectMock
   HandlaggningMapper handlaggningMapper;

   @BeforeAll
   public static void setup()
   {
      server = new WireMockServer(
            options()
                  .dynamicPort()
                  .usingFilesUnderDirectory("src/test/resources"));
      server.start();

      System.setProperty("handlaggning.api.base-url", server.baseUrl());
   }

   @AfterAll
   public static void teardown()
   {
      if (server != null)
      {
         server.stop();
      }
   }

   @BeforeEach
   void resetStubs()
   {
      server.resetToDefaultMappings();
   }

   @Test
   public void should_throw_with_error_type_bad_request_on_status_400_during_create_yrkande() throws HandlaggningException
   {
      server.stubFor(WireMock.post(WireMock.urlPathEqualTo("/yrkande"))
            .willReturn(WireMock.aResponse().withStatus(400)));
      var exception = assertThrows(HandlaggningException.class,
            () -> handlaggningAdapter.createYrkande(createModelCreateYrkandeRequest()));
      assertEquals(HandlaggningException.ErrorType.BAD_REQUEST, exception.getErrorType());
   }

   @Test
   public void should_throw_with_error_type_service_unavailable_on_connection_reset_during_create_yrkande()
         throws HandlaggningException
   {
      server.stubFor(WireMock.post(WireMock.urlPathEqualTo("/yrkande"))
            .willReturn(WireMock.aResponse().withFault(Fault.CONNECTION_RESET_BY_PEER)));
      var exception = assertThrows(HandlaggningException.class,
            () -> handlaggningAdapter.createYrkande(createModelCreateYrkandeRequest()));
      assertEquals(HandlaggningException.ErrorType.SERVICE_UNAVAILABLE, exception.getErrorType());
   }

   @Test
   public void should_throw_with_error_type_unexpected_error_on_status_500_during_create_yrkande() throws HandlaggningException
   {
      server.stubFor(WireMock.post(WireMock.urlPathEqualTo("/yrkande"))
            .willReturn(WireMock.aResponse().withStatus(500)));
      var exception = assertThrows(HandlaggningException.class,
            () -> handlaggningAdapter.createYrkande(createModelCreateYrkandeRequest()));
      assertEquals(HandlaggningException.ErrorType.UNEXPECTED_ERROR, exception.getErrorType());
   }

   @Test
   public void should_throw_with_error_type_unexpected_error_on_status_200_and_null_response_during_create_yrkande()
         throws HandlaggningException
   {
      server.stubFor(WireMock.post(WireMock.urlPathEqualTo("/yrkande"))
            .willReturn(WireMock.aResponse().withStatus(200).withBody((String) null)));
      var exception = assertThrows(HandlaggningException.class,
            () -> handlaggningAdapter.createYrkande(createModelCreateYrkandeRequest()));
      assertEquals(HandlaggningException.ErrorType.UNEXPECTED_ERROR, exception.getErrorType());
   }

   @Test
   public void should_create_yrkande_with_handlaggning() throws HandlaggningException
   {
      var expectedHandlaggning = createModelHandlaggning();
      var request = createModelCreateYrkandeRequest();
      Mockito.when(handlaggningMapper.toHandlaggning(Mockito.any())).thenReturn(expectedHandlaggning);
      var handlaggning = handlaggningAdapter.createYrkande(request);
      assertEquals(expectedHandlaggning, handlaggning);
   }

   @Test
   public void should_throw_with_error_type_bad_request_on_status_400_during_read_handlaggning() throws HandlaggningException
   {
      var handlaggningId = UUID.fromString("cf4691e6-f7e6-4db2-a409-8fc736d13931");
      server.stubFor(WireMock.get(WireMock.urlPathMatching("/handlaggning/.+"))
            .willReturn(WireMock.aResponse().withStatus(400)));
      var exception = assertThrows(HandlaggningException.class, () -> handlaggningAdapter.readHandlaggning(handlaggningId));
      assertEquals(HandlaggningException.ErrorType.BAD_REQUEST, exception.getErrorType());
   }

   @Test
   public void should_throw_with_error_type_service_unavailable_on_connection_reset_during_read_handlaggning()
         throws HandlaggningException
   {
      var handlaggningId = UUID.fromString("cf4691e6-f7e6-4db2-a409-8fc736d13931");
      server.stubFor(WireMock.get(WireMock.urlPathMatching("/handlaggning/.+"))
            .willReturn(WireMock.aResponse().withFault(Fault.CONNECTION_RESET_BY_PEER)));
      var exception = assertThrows(HandlaggningException.class, () -> handlaggningAdapter.readHandlaggning(handlaggningId));
      assertEquals(HandlaggningException.ErrorType.SERVICE_UNAVAILABLE, exception.getErrorType());
   }

   @Test
   public void should_throw_with_error_type_unexpected_error_on_status_500_during_read_handlaggning() throws HandlaggningException
   {
      var handlaggningId = UUID.fromString("cf4691e6-f7e6-4db2-a409-8fc736d13931");
      server.stubFor(WireMock.get(WireMock.urlPathMatching("/handlaggning/.+"))
            .willReturn(WireMock.aResponse().withStatus(500)));
      var exception = assertThrows(HandlaggningException.class, () -> handlaggningAdapter.readHandlaggning(handlaggningId));
      assertEquals(HandlaggningException.ErrorType.UNEXPECTED_ERROR, exception.getErrorType());
   }

   @Test
   public void should_throw_with_error_type_unexpected_error_on_status_200_and_null_response_during_read_handlaggning()
         throws HandlaggningException
   {
      var handlaggningId = UUID.fromString("cf4691e6-f7e6-4db2-a409-8fc736d13931");
      server.stubFor(WireMock.get(WireMock.urlPathMatching("/handlaggning/.+"))
            .willReturn(WireMock.aResponse().withStatus(200).withBody((String) null)));
      var exception = assertThrows(HandlaggningException.class, () -> handlaggningAdapter.readHandlaggning(handlaggningId));
      assertEquals(HandlaggningException.ErrorType.UNEXPECTED_ERROR, exception.getErrorType());
   }

   @Test
   public void should_read_handlaggning() throws HandlaggningException
   {
      var handlaggningId = UUID.fromString("cf4691e6-f7e6-4db2-a409-8fc736d11234");
      var expectedHandlaggning = TestData.createModelHandlaggning();
      Mockito.when(handlaggningMapper.toHandlaggning(Mockito.any())).thenReturn(expectedHandlaggning);
      var handlaggning = handlaggningAdapter.readHandlaggning(handlaggningId);
      assertEquals(expectedHandlaggning, handlaggning);
   }

   @Test
   public void should_throw_with_error_type_bad_request_on_status_400_during_update_handlaggning() throws HandlaggningException
   {
      server.stubFor(WireMock.put(WireMock.urlPathMatching("/handlaggning/.+"))
            .willReturn(WireMock.aResponse().withStatus(400)));
      var handlaggningUpdate = createModelHandlaggningUpdate();
      Mockito.when(handlaggningMapper.toPutHandlaggningRequest(handlaggningUpdate))
            .thenReturn(toApiPutHandlaggningRequest(handlaggningUpdate));
      var exception = assertThrows(HandlaggningException.class, () -> handlaggningAdapter.updateHandlaggning(handlaggningUpdate));
      assertEquals(HandlaggningException.ErrorType.BAD_REQUEST, exception.getErrorType());
   }

   @Test
   public void should_throw_with_error_type_service_unavailable_on_connection_reset_during_update_handlaggning()
         throws HandlaggningException
   {
      server.stubFor(WireMock.put(WireMock.urlPathMatching("/handlaggning/.+"))
            .willReturn(WireMock.aResponse().withFault(Fault.CONNECTION_RESET_BY_PEER)));
      var handlaggningUpdate = createModelHandlaggningUpdate();
      Mockito.when(handlaggningMapper.toPutHandlaggningRequest(handlaggningUpdate))
            .thenReturn(toApiPutHandlaggningRequest(handlaggningUpdate));
      var exception = assertThrows(HandlaggningException.class, () -> handlaggningAdapter.updateHandlaggning(handlaggningUpdate));
      assertEquals(HandlaggningException.ErrorType.SERVICE_UNAVAILABLE, exception.getErrorType());
   }

   @Test
   public void should_throw_with_error_type_unexpected_error_on_status_500_during_update_handlaggning()
         throws HandlaggningException
   {
      server.stubFor(WireMock.put(WireMock.urlPathMatching("/handlaggning/.+"))
            .willReturn(WireMock.aResponse().withStatus(500)));
      var handlaggningUpdate = createModelHandlaggningUpdate();
      Mockito.when(handlaggningMapper.toPutHandlaggningRequest(handlaggningUpdate))
            .thenReturn(toApiPutHandlaggningRequest(handlaggningUpdate));
      var exception = assertThrows(HandlaggningException.class, () -> handlaggningAdapter.updateHandlaggning(handlaggningUpdate));
      assertEquals(HandlaggningException.ErrorType.UNEXPECTED_ERROR, exception.getErrorType());
   }

   @Test
   public void should_throw_with_error_type_unexpected_error_on_status_200_and_null_response_during_update_handlaggning()
         throws HandlaggningException
   {
      server.stubFor(WireMock.put(WireMock.urlPathMatching("/handlaggning/.+"))
            .willReturn(WireMock.aResponse().withStatus(200).withBody((String) null)));
      var handlaggningUpdate = createModelHandlaggningUpdate();
      Mockito.when(handlaggningMapper.toPutHandlaggningRequest(handlaggningUpdate))
            .thenReturn(toApiPutHandlaggningRequest(handlaggningUpdate));
      var exception = assertThrows(HandlaggningException.class, () -> handlaggningAdapter.updateHandlaggning(handlaggningUpdate));
      assertEquals(HandlaggningException.ErrorType.UNEXPECTED_ERROR, exception.getErrorType());
   }

   @Test
   public void should_update_handlaggning() throws HandlaggningException
   {
      var handlaggningId = UUID.fromString("cf4691e6-f7e6-4db2-a409-8fc736d11234");
      var expectedHandlaggningUpdate = createModelHandlaggningUpdate(createModelHandlaggning(handlaggningId));
      Mockito.when(handlaggningMapper.toPutHandlaggningRequest(expectedHandlaggningUpdate))
            .thenReturn(toApiPutHandlaggningRequest(expectedHandlaggningUpdate));
      Mockito.when(handlaggningMapper.toHandlaggningUpdate(Mockito.any())).thenReturn(expectedHandlaggningUpdate);
      var handlaggningUpdate = handlaggningAdapter.updateHandlaggning(expectedHandlaggningUpdate);
      assertEquals(expectedHandlaggningUpdate, handlaggningUpdate);
   }
}
