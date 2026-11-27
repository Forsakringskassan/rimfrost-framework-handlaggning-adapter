package se.fk.rimfrost.framework.handlaggning;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.tomakehurst.wiremock.WireMockServer;
import io.quarkus.test.component.QuarkusComponentTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.fk.rimfrost.framework.handlaggning.adapter.HandlaggningAdapter;
import se.fk.rimfrost.framework.handlaggning.adapter.HandlaggningMapper;
import se.fk.rimfrost.framework.handlaggning.exception.HandlaggningException;
import se.fk.rimfrost.framework.handlaggning.model.Handlaggning;
import se.fk.rimfrost.framework.handlaggning.model.ImmutableHandlaggning;
import se.fk.rimfrost.framework.handlaggning.model.ImmutableIdtyp;
import se.fk.rimfrost.framework.handlaggning.model.ImmutableRollIYrkande;
import se.fk.rimfrost.framework.handlaggning.model.ImmutableSakfragaStallningstagandeContainer;
import se.fk.rimfrost.framework.handlaggning.model.ImmutableYrkande;
import se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.GetHandlaggningResponse;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.anyRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.anyUrl;
import static com.github.tomakehurst.wiremock.client.WireMock.exactly;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Testar att bas-url:en som skickas in per anrop respekteras. Två WireMock-servrar svarar med
 * olika handläggningId i svaret, och varje anrop ska hämta svaret från rätt server utan att
 * anropen påverkar varandra.
 */
@QuarkusComponentTest(value = HandlaggningMapper.class)
public class HandlaggningAdapterBaseUrlTest
{
   private static final UUID REQUESTED_HANDLAGGNING_ID = UUID.fromString("5367f6b8-cc4a-11f0-8de9-199901011234");

   private static final UUID SERVER_A_HANDLAGGNING_ID = UUID.fromString("aaaaaaaa-cc4a-11f0-8de9-199901011234");

   private static final UUID SERVER_B_HANDLAGGNING_ID = UUID.fromString("bbbbbbbb-cc4a-11f0-8de9-199901011234");

   private static final UUID YRKANDE_ID = UUID.fromString("81234d56-7890-4abc-1234-56789abcdef0");

   private static final String HANDLAGGNING_PATH = "/handlaggning/" + REQUESTED_HANDLAGGNING_ID;

   private static final ObjectMapper OBJECT_MAPPER = JsonMapper.builder()
         .addModule(new JavaTimeModule())
         .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
         .build();

   private static WireMockServer serverA;

   private static WireMockServer serverB;

   @Inject
   HandlaggningAdapter handlaggningAdapter;

   @BeforeAll
   public static void setup()
   {
      serverA = new WireMockServer(options().dynamicPort());
      serverA.start();
      serverB = new WireMockServer(options().dynamicPort());
      serverB.start();
   }

   @AfterAll
   public static void teardown()
   {
      if (serverA != null)
      {
         serverA.stop();
      }
      if (serverB != null)
      {
         serverB.stop();
      }
   }

   @BeforeEach
   void resetStubs() throws JsonProcessingException
   {
      serverA.resetAll();
      serverB.resetAll();
      stubGetHandlaggning(serverA, SERVER_A_HANDLAGGNING_ID);
      stubGetHandlaggning(serverB, SERVER_B_HANDLAGGNING_ID);
   }

   @Test
   public void should_read_handlaggning_from_server_given_by_base_url() throws HandlaggningException
   {
      var handlaggningFromA = handlaggningAdapter.readHandlaggning(serverA.baseUrl(), REQUESTED_HANDLAGGNING_ID);
      var handlaggningFromB = handlaggningAdapter.readHandlaggning(serverB.baseUrl(), REQUESTED_HANDLAGGNING_ID);

      assertEquals(SERVER_A_HANDLAGGNING_ID, handlaggningFromA.id());
      assertEquals(SERVER_B_HANDLAGGNING_ID, handlaggningFromB.id());
      serverA.verify(exactly(1), getRequestedFor(urlPathEqualTo(HANDLAGGNING_PATH)));
      serverA.verify(exactly(1), anyRequestedFor(anyUrl()));
      serverB.verify(exactly(1), getRequestedFor(urlPathEqualTo(HANDLAGGNING_PATH)));
      serverB.verify(exactly(1), anyRequestedFor(anyUrl()));
   }

   private static void stubGetHandlaggning(WireMockServer server, UUID handlaggningIdInResponse)
         throws JsonProcessingException
   {
      var response = new GetHandlaggningResponse(TestUtils.toApiHandlaggning(createHandlaggning(handlaggningIdInResponse)));
      server.stubFor(get(urlPathEqualTo(HANDLAGGNING_PATH))
            .willReturn(aResponse()
                  .withStatus(200)
                  .withHeader("Content-Type", "application/json")
                  .withBody(OBJECT_MAPPER.writeValueAsString(response))));
   }

   private static Handlaggning createHandlaggning(UUID handlaggningId)
   {
      var rollIYrkande = ImmutableRollIYrkande.builder()
            .id(UUID.fromString("3c1a9d2e-6f4b-4e8a-9b7c-1d2e3f4a5b6c"))
            .individ(ImmutableIdtyp.builder()
                  .typId("123456789")
                  .varde("19901010-1234")
                  .build())
            .yrkandeRollId("d1b6721d-a3d8-4fd1-aae8-6ba11849e9bc")
            .avserYrkande(YRKANDE_ID)
            .build();

      var sakfragaStallningstagande = ImmutableSakfragaStallningstagandeContainer.builder()
            .id(UUID.fromString("bc6e5150-29be-4758-a2ee-e241f1d7ccf7"))
            .objektTypId("ersattning")
            .data("{\"belopp\":40000,\"berakningsgrund\":100,\"ersattningstyp\":{\"id\":\"361779e2-cc93-471a-815b-50bad66cd427\",\"namn\":\"HUNDBIDRAG\"},\"omfattningProcent\":100,\"beslutsutfall\":\"FU\"}")
            .build();

      var yrkande = ImmutableYrkande.builder()
            .id(YRKANDE_ID)
            .version(1)
            .ingangtypId("f35c574d-e2a3-42ac-9ccb-835a24e692fe")
            .yrkandeDatum(OffsetDateTime.parse("2026-01-22T13:30:11.155427+01:00"))
            .yrkandeStatus("YRKAT")
            .yrkandeFrom(OffsetDateTime.parse("2025-01-10T16:15:50Z"))
            .yrkandeTom(OffsetDateTime.parse("2025-01-10T16:15:50Z"))
            .avsikt("NY")
            .rollerIYrkande(List.of(rollIYrkande))
            .sakfragorStallningstaganden(List.of(sakfragaStallningstagande))
            .beslut(List.of())
            .build();

      return ImmutableHandlaggning.builder()
            .id(handlaggningId)
            .version(1)
            .yrkande(yrkande)
            .handlaggningIdTyp("e7f3c5a2-1234-4abc-9876-56789abcdef0")
            .skapadTS(OffsetDateTime.parse("2026-01-22T13:31:00.8742683+01:00"))
            .handlaggningspecifikationId(UUID.fromString("71234567-89ab-4cde-9012-3456789abcde"))
            .build();
   }
}
