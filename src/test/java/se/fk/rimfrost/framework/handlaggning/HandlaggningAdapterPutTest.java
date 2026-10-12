package se.fk.rimfrost.framework.handlaggning;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
import se.fk.rimfrost.framework.handlaggning.model.HandlaggningUpdate;
import se.fk.rimfrost.framework.handlaggning.model.ImmutableHandlaggning;
import se.fk.rimfrost.framework.handlaggning.model.ImmutableHandlaggningUpdate;
import se.fk.rimfrost.framework.handlaggning.model.ImmutableIdtyp;
import se.fk.rimfrost.framework.handlaggning.model.ImmutableRollIYrkande;
import se.fk.rimfrost.framework.handlaggning.model.ImmutableSakfragaStallningstagandeContainer;
import se.fk.rimfrost.framework.handlaggning.model.ImmutableUppgift;
import se.fk.rimfrost.framework.handlaggning.model.ImmutableUppgiftSpecifikation;
import se.fk.rimfrost.framework.handlaggning.model.ImmutableUppgiftsdatakopia;
import se.fk.rimfrost.framework.handlaggning.model.ImmutableUppgiftsdatakoppling;
import se.fk.rimfrost.framework.handlaggning.model.ImmutableYrkande;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.putRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Testar PUT av handläggning med riktig mapper. Requesten som skickas ska motsvara
 * jsonBody i put-handlaggning.json.
 */
@QuarkusComponentTest(value = HandlaggningMapper.class, useSystemConfigSources = true)
public class HandlaggningAdapterPutTest
{
   private static final UUID HANDLAGGNING_ID = UUID.fromString("5367f6b8-cc4a-11f0-8de9-199901011234");

   private static final UUID YRKANDE_ID = UUID.fromString("81234d56-7890-4abc-1234-56789abcdef0");

   private static final String INFORMATIONSOBJEKT_ID = "3f8b2c1d-6e4a-4d7b-9c5e-1a2b3c4d5e6f";

   private static final int UNDERLAG_VERSION = 1;

   private static final Set<String> DATUMFALT = Set.of("yrkandedatum", "yrkandeFrom", "yrkandeTom", "skapadTS", "avslutadTS",
         "utfordTS", "planeradTillTS");

   private static WireMockServer server;

   @Inject
   HandlaggningAdapter handlaggningAdapter;

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
      server.resetRequests();
   }

   @Test
   public void should_update_handlaggning_with_underlag_and_resultat() throws HandlaggningException, IOException
   {
      var handlaggningUpdate = createHandlaggningUpdate();

      var response = handlaggningAdapter.updateHandlaggning(handlaggningUpdate);

      // Datum i handläggningen normaliseras till UTC vid deserialisering, så endast uppgiften jämförs i sin helhet
      assertEquals(handlaggningUpdate.uppgift(), response.uppgift());
      server.verify(putRequestedFor(urlPathEqualTo("/handlaggning/" + HANDLAGGNING_ID))
            .withRequestBody(equalToJson(expectedRequestBody())));
   }

   private static String expectedRequestBody() throws IOException
   {
      var mapping = new ObjectMapper()
            .readTree(Files.readString(Path.of("src/test/resources/mappings/put-handlaggning.json")));
      var expected = mapping.get("response").get("jsonBody").deepCopy();
      ignoreDatum(expected);
      return expected.toString();
   }

   /**
    * REST-klienten serialiserar OffsetDateTime som epoch-tal i komponenttestet, medan put-handlaggning.json
    * har ISO-strängar. Datumvärden ignoreras därför i jämförelsen.
    */
   private static void ignoreDatum(JsonNode node)
   {
      if (node instanceof ObjectNode objectNode)
      {
         DATUMFALT.stream()
               .filter(falt -> objectNode.hasNonNull(falt))
               .forEach(falt -> objectNode.put(falt, "${json-unit.ignore}"));
      }
      node.forEach(HandlaggningAdapterPutTest::ignoreDatum);
   }

   private static HandlaggningUpdate createHandlaggningUpdate()
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

      var handlaggning = ImmutableHandlaggning.builder()
            .id(HANDLAGGNING_ID)
            .version(1)
            .yrkande(yrkande)
            .handlaggningIdTyp("e7f3c5a2-1234-4abc-9876-56789abcdef0")
            .skapadTS(OffsetDateTime.parse("2026-01-22T13:31:00.8742683+01:00"))
            .handlaggningspecifikationId(UUID.fromString("71234567-89ab-4cde-9012-3456789abcde"))
            .build();

      var kopia = ImmutableUppgiftsdatakopia.builder()
            .informationsobjekttyp("folkbokforing")
            .data("{}")
            .build();

      var underlagKoppling = ImmutableUppgiftsdatakoppling.builder()
            .informationsobjektId(INFORMATIONSOBJEKT_ID)
            .informationsobjektversion(UNDERLAG_VERSION)
            .build();

      var resultatKoppling = ImmutableUppgiftsdatakoppling.builder()
            .informationsobjektId(INFORMATIONSOBJEKT_ID)
            .informationsobjektversion(UNDERLAG_VERSION + 1)
            .build();

      var uppgift = ImmutableUppgift.builder()
            .id(UUID.fromString("425a97bb-7279-4562-ad01-f77d0db0ae4c"))
            .version(1)
            .skapadTS(OffsetDateTime.parse("2026-01-22T13:32:00Z"))
            .aktivitetId(UUID.fromString("576fab63-205b-4004-87db-40793bb75209"))
            .uppgiftSpecifikation(ImmutableUppgiftSpecifikation.builder()
                  .id(UUID.fromString("e856b685-6330-4767-a8c2-5ec9aca8bcba"))
                  .version(1)
                  .build())
            .uppgiftStatus("NY")
            .fSSAinformation("ebdadec4-c126-4cbd-a2d7-bf71676211e9")
            .underlag(List.of(kopia, underlagKoppling))
            .resultat(List.of(resultatKoppling))
            .build();

      return ImmutableHandlaggningUpdate.builder()
            .handlaggning(handlaggning)
            .uppgift(uppgift)
            .build();
   }
}
