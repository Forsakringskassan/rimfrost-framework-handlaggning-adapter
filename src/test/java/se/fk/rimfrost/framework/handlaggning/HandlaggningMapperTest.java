package se.fk.rimfrost.framework.handlaggning;

import io.quarkus.test.component.QuarkusComponentTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import se.fk.rimfrost.framework.handlaggning.adapter.HandlaggningMapper;
import se.fk.rimfrost.framework.handlaggning.model.ImmutableHandlaggning;
import se.fk.rimfrost.framework.handlaggning.model.ImmutableHandlaggningUpdate;
import se.fk.rimfrost.framework.handlaggning.model.ImmutableUppgift;
import se.fk.rimfrost.framework.handlaggning.model.ImmutableYrkande;
import se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.PutHandlaggningRequest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static se.fk.rimfrost.framework.handlaggning.TestData.createIndividYrkandeRoll;
import static se.fk.rimfrost.framework.handlaggning.TestData.createModelHandlaggning;
import static se.fk.rimfrost.framework.handlaggning.TestData.createModelHandlaggningUpdate;
import static se.fk.rimfrost.framework.handlaggning.TestData.createModelYrkande;
import static se.fk.rimfrost.framework.handlaggning.TestData.createProduceratResultat;
import static se.fk.rimfrost.framework.handlaggning.TestData.createUnderlag;
import static se.fk.rimfrost.framework.handlaggning.TestData.createUppgift;
import static se.fk.rimfrost.framework.handlaggning.TestUtils.toApiHandlaggning;
import static se.fk.rimfrost.framework.handlaggning.TestUtils.toApiHandlaggningUpdate;
import static se.fk.rimfrost.framework.handlaggning.TestUtils.toApiPostYrkandeRequest;
import static se.fk.rimfrost.framework.handlaggning.TestUtils.toApiYrkande;

@QuarkusComponentTest
public class HandlaggningMapperTest
{
   @Inject
   HandlaggningMapper handlaggningMapper;

   @Test
   public void should_create_correct_api_yrkande()
   {
      var expectedYrkande = createModelYrkande();
      var apiYrkande = toApiYrkande(expectedYrkande);
      assertEquals(expectedYrkande, handlaggningMapper.toYrkande(apiYrkande));
   }

   @Test
   public void should_create_correct_api_yrkande_multiple_individ_roller()
   {
      var expectedYrkande = ImmutableYrkande.builder()
            .from(createModelYrkande())
            .addIndividYrkandeRoller(createIndividYrkandeRoll())
            .build();
      var apiYrkande = toApiYrkande(expectedYrkande);
      assertEquals(expectedYrkande, handlaggningMapper.toYrkande(apiYrkande));
   }

   @Test
   public void should_create_correct_api_yrkande_multiple_producerade_resultat()
   {
      var expectedYrkande = ImmutableYrkande.builder()
            .from(createModelYrkande())
            .addProduceradeResultat(createProduceratResultat())
            .build();
      var apiYrkande = toApiYrkande(expectedYrkande);
      assertEquals(expectedYrkande, handlaggningMapper.toYrkande(apiYrkande));
   }

   @Test
   public void should_create_correct_api_yrkande_null_beslut()
   {
      var expectedYrkande = ImmutableYrkande.builder()
            .from(createModelYrkande())
            .beslut(null)
            .build();
      var apiYrkande = toApiYrkande(expectedYrkande);
      assertEquals(expectedYrkande, handlaggningMapper.toYrkande(apiYrkande));
   }

   @Test
   public void should_create_correct_api_post_yrkande()
   {
      var yrkande = createModelYrkande();
      assertEquals(toApiPostYrkandeRequest(yrkande), handlaggningMapper.toPostYrkandeRequest(yrkande));
   }

   @Test
   public void should_create_correct_api_post_yrkande_multiple_individ_roller()
   {
      var yrkande = ImmutableYrkande.builder()
            .from(createModelYrkande())
            .addIndividYrkandeRoller(createIndividYrkandeRoll())
            .build();
      assertEquals(toApiPostYrkandeRequest(yrkande), handlaggningMapper.toPostYrkandeRequest(yrkande));
   }

   @Test
   public void should_create_correct_api_post_yrkande_multiple_producerade_resultat()
   {
      var yrkande = ImmutableYrkande.builder()
            .from(createModelYrkande())
            .addProduceradeResultat(createProduceratResultat())
            .build();
      assertEquals(toApiPostYrkandeRequest(yrkande), handlaggningMapper.toPostYrkandeRequest(yrkande));
   }

   @Test
   public void should_create_correct_api_post_handlaggning_request()
   {
      var yrkandeId = UUID.randomUUID();
      var handlaggningSpecifikationId = UUID.randomUUID();

      se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.PostHandlaggningRequest expectedRequest = new se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.PostHandlaggningRequest();
      expectedRequest.setYrkandeId(yrkandeId);
      expectedRequest.setHandlaggningspecifikationId(handlaggningSpecifikationId);

      assertEquals(expectedRequest, handlaggningMapper.toPostHandlaggningRequest(yrkandeId, handlaggningSpecifikationId));
   }

   @Test
   public void should_create_correct_api_put_handlaggning_request()
   {
      var handlaggningUpdate = createModelHandlaggningUpdate();
      PutHandlaggningRequest expectedRequest = new PutHandlaggningRequest();
      expectedRequest.setHandlaggning(toApiHandlaggningUpdate(handlaggningUpdate));

      assertEquals(expectedRequest, handlaggningMapper.toPutHandlaggningRequest(handlaggningUpdate));
   }

   @Test
   public void should_create_correct_api_handlaggning_update()
   {
      var handlaggningUpdate = createModelHandlaggningUpdate();
      ;
      assertEquals(toApiHandlaggningUpdate(handlaggningUpdate), handlaggningMapper.toApiHandlaggningUpdate(handlaggningUpdate));
   }

   @Test
   public void should_create_correct_api_handlaggning_update_multiple_individ_roller()
   {
      var handlaggningUpdate = createModelHandlaggningUpdate();
      var updatedYrkande = ImmutableYrkande.builder()
            .from(handlaggningUpdate.yrkande())
            .addIndividYrkandeRoller(createIndividYrkandeRoll())
            .build();
      var updatedHandlaggningUpdate = ImmutableHandlaggningUpdate.builder()
            .from(handlaggningUpdate)
            .yrkande(updatedYrkande)
            .build();

      assertEquals(toApiHandlaggningUpdate(updatedHandlaggningUpdate),
            handlaggningMapper.toApiHandlaggningUpdate(updatedHandlaggningUpdate));
   }

   @Test
   public void should_create_correct_api_handlaggning_update_multiple_producerade_resultat()
   {
      var handlaggningUpdate = createModelHandlaggningUpdate();
      var updatedYrkande = ImmutableYrkande.builder()
            .from(handlaggningUpdate.yrkande())
            .addProduceradeResultat(createProduceratResultat())
            .build();
      var updatedHandlaggningUpdate = ImmutableHandlaggningUpdate.builder()
            .from(handlaggningUpdate)
            .yrkande(updatedYrkande)
            .build();

      assertEquals(toApiHandlaggningUpdate(updatedHandlaggningUpdate),
            handlaggningMapper.toApiHandlaggningUpdate(updatedHandlaggningUpdate));
   }

   @Test
   public void should_create_correct_api_handlaggning_update_null_beslut()
   {
      var handlaggningUpdate = createModelHandlaggningUpdate();
      var updatedYrkande = ImmutableYrkande.builder()
            .from(handlaggningUpdate.yrkande())
            .beslut(null)
            .build();
      var updatedHandlaggningUpdate = ImmutableHandlaggningUpdate.builder()
            .from(handlaggningUpdate)
            .yrkande(updatedYrkande)
            .build();

      assertEquals(toApiHandlaggningUpdate(updatedHandlaggningUpdate),
            handlaggningMapper.toApiHandlaggningUpdate(updatedHandlaggningUpdate));
   }

   @Test
   public void should_create_correct_api_handlaggning_update_multiple_underlag()
   {
      var handlaggningUpdate = ImmutableHandlaggningUpdate.builder()
            .from(createModelHandlaggningUpdate())
            .addUnderlag(createUnderlag())
            .build();

      assertEquals(toApiHandlaggningUpdate(handlaggningUpdate), handlaggningMapper.toApiHandlaggningUpdate(handlaggningUpdate));
   }

   @Test
   public void should_create_correct_api_handlaggning_update_avslutad_ts_null()
   {
      var handlaggningUpdate = ImmutableHandlaggningUpdate.builder()
            .from(createModelHandlaggningUpdate())
            .avslutadTS(null)
            .build();

      assertEquals(toApiHandlaggningUpdate(handlaggningUpdate), handlaggningMapper.toApiHandlaggningUpdate(handlaggningUpdate));
   }

   @Test
   public void should_create_correct_api_handlaggning_update_uppgift_utford_ts_null()
   {
      var uppgift = ImmutableUppgift.builder()
            .from(createUppgift())
            .utfordTs(null)
            .build();

      var handlaggningUpdate = ImmutableHandlaggningUpdate.builder()
            .from(createModelHandlaggningUpdate())
            .uppgift(uppgift)
            .build();

      assertEquals(toApiHandlaggningUpdate(handlaggningUpdate), handlaggningMapper.toApiHandlaggningUpdate(handlaggningUpdate));
   }

   @Test
   public void should_create_correct_api_handlaggning_update_uppgift_planerad_ts_null()
   {
      var uppgift = ImmutableUppgift.builder()
            .from(createUppgift())
            .planeradTs(null)
            .build();

      var handlaggningUpdate = ImmutableHandlaggningUpdate.builder()
            .from(createModelHandlaggningUpdate())
            .uppgift(uppgift)
            .build();

      assertEquals(toApiHandlaggningUpdate(handlaggningUpdate), handlaggningMapper.toApiHandlaggningUpdate(handlaggningUpdate));
   }

   @Test
   public void should_create_correct_api_handlaggning_update_uppgift_utforar_id_null()
   {
      var uppgift = ImmutableUppgift.builder()
            .from(createUppgift())
            .utforarId(null)
            .build();

      var handlaggningUpdate = ImmutableHandlaggningUpdate.builder()
            .from(createModelHandlaggningUpdate())
            .uppgift(uppgift)
            .build();

      assertEquals(toApiHandlaggningUpdate(handlaggningUpdate), handlaggningMapper.toApiHandlaggningUpdate(handlaggningUpdate));
   }

   @Test
   public void should_create_correct_api_handlaggning_update_uppgift_uppgift_status_null()
   {
      var uppgift = ImmutableUppgift.builder()
            .from(createUppgift())
            .uppgiftStatus(null)
            .build();

      var handlaggningUpdate = ImmutableHandlaggningUpdate.builder()
            .from(createModelHandlaggningUpdate())
            .uppgift(uppgift)
            .build();

      assertEquals(toApiHandlaggningUpdate(handlaggningUpdate), handlaggningMapper.toApiHandlaggningUpdate(handlaggningUpdate));
   }

   @Test
   public void should_create_correct_model_handlaggning_update()
   {
      var handlaggningUpdate = createModelHandlaggningUpdate();
      ;
      assertEquals(handlaggningUpdate, handlaggningMapper.toHandlaggningUpdate(toApiHandlaggningUpdate(handlaggningUpdate)));
   }

   @Test
   public void should_create_correct_model_handlaggning_update_multiple_individ_roller()
   {
      var handlaggningUpdate = createModelHandlaggningUpdate();
      var updatedYrkande = ImmutableYrkande.builder()
            .from(handlaggningUpdate.yrkande())
            .addIndividYrkandeRoller(createIndividYrkandeRoll())
            .build();
      var updatedHandlaggningUpdate = ImmutableHandlaggningUpdate.builder()
            .from(handlaggningUpdate)
            .yrkande(updatedYrkande)
            .build();

      assertEquals(updatedHandlaggningUpdate,
            handlaggningMapper.toHandlaggningUpdate(toApiHandlaggningUpdate(updatedHandlaggningUpdate)));
   }

   @Test
   public void should_create_correct_model_handlaggning_update_multiple_producerade_resultat()
   {
      var handlaggningUpdate = createModelHandlaggningUpdate();
      var updatedYrkande = ImmutableYrkande.builder()
            .from(handlaggningUpdate.yrkande())
            .addProduceradeResultat(createProduceratResultat())
            .build();
      var updatedHandlaggningUpdate = ImmutableHandlaggningUpdate.builder()
            .from(handlaggningUpdate)
            .yrkande(updatedYrkande)
            .build();

      assertEquals(updatedHandlaggningUpdate,
            handlaggningMapper.toHandlaggningUpdate(toApiHandlaggningUpdate(updatedHandlaggningUpdate)));
   }

   @Test
   public void should_create_correct_model_handlaggning_update_null_beslut()
   {
      var handlaggningUpdate = createModelHandlaggningUpdate();
      var updatedYrkande = ImmutableYrkande.builder()
            .from(handlaggningUpdate.yrkande())
            .beslut(null)
            .build();
      var updatedHandlaggningUpdate = ImmutableHandlaggningUpdate.builder()
            .from(handlaggningUpdate)
            .yrkande(updatedYrkande)
            .build();

      assertEquals(updatedHandlaggningUpdate,
            handlaggningMapper.toHandlaggningUpdate(toApiHandlaggningUpdate(updatedHandlaggningUpdate)));
   }

   @Test
   public void should_create_correct_model_handlaggning_update_multiple_underlag()
   {
      var handlaggningUpdate = ImmutableHandlaggningUpdate.builder()
            .from(createModelHandlaggningUpdate())
            .addUnderlag(createUnderlag())
            .build();

      assertEquals(handlaggningUpdate, handlaggningMapper.toHandlaggningUpdate(toApiHandlaggningUpdate(handlaggningUpdate)));
   }

   @Test
   public void should_create_correct_model_handlaggning_update_avslutad_ts_null()
   {
      var handlaggningUpdate = ImmutableHandlaggningUpdate.builder()
            .from(createModelHandlaggningUpdate())
            .avslutadTS(null)
            .build();

      assertEquals(handlaggningUpdate, handlaggningMapper.toHandlaggningUpdate(toApiHandlaggningUpdate(handlaggningUpdate)));
   }

   @Test
   public void should_create_correct_model_handlaggning_update_uppgift_utford_ts_null()
   {
      var uppgift = ImmutableUppgift.builder()
            .from(createUppgift())
            .utfordTs(null)
            .build();

      var handlaggningUpdate = ImmutableHandlaggningUpdate.builder()
            .from(createModelHandlaggningUpdate())
            .uppgift(uppgift)
            .build();

      assertEquals(handlaggningUpdate, handlaggningMapper.toHandlaggningUpdate(toApiHandlaggningUpdate(handlaggningUpdate)));
   }

   @Test
   public void should_create_correct_model_handlaggning_update_uppgift_planerad_ts_null()
   {
      var uppgift = ImmutableUppgift.builder()
            .from(createUppgift())
            .planeradTs(null)
            .build();

      var handlaggningUpdate = ImmutableHandlaggningUpdate.builder()
            .from(createModelHandlaggningUpdate())
            .uppgift(uppgift)
            .build();

      assertEquals(handlaggningUpdate, handlaggningMapper.toHandlaggningUpdate(toApiHandlaggningUpdate(handlaggningUpdate)));
   }

   @Test
   public void should_create_correct_model_handlaggning_update_uppgift_utforar_id_null()
   {
      var uppgift = ImmutableUppgift.builder()
            .from(createUppgift())
            .utforarId(null)
            .build();

      var handlaggningUpdate = ImmutableHandlaggningUpdate.builder()
            .from(createModelHandlaggningUpdate())
            .uppgift(uppgift)
            .build();

      assertEquals(handlaggningUpdate, handlaggningMapper.toHandlaggningUpdate(toApiHandlaggningUpdate(handlaggningUpdate)));
   }

   @Test
   public void should_create_correct_model_handlaggning_update_uppgift_uppgift_status_null()
   {
      var uppgift = ImmutableUppgift.builder()
            .from(createUppgift())
            .uppgiftStatus(null)
            .build();

      var handlaggningUpdate = ImmutableHandlaggningUpdate.builder()
            .from(createModelHandlaggningUpdate())
            .uppgift(uppgift)
            .build();

      assertEquals(handlaggningUpdate, handlaggningMapper.toHandlaggningUpdate(toApiHandlaggningUpdate(handlaggningUpdate)));
   }

   @Test
   public void should_create_correct_model_handlaggning()
   {
      var handlaggning = createModelHandlaggning();
      ;
      assertEquals(handlaggning, handlaggningMapper.toHandlaggning(toApiHandlaggning(handlaggning)));
   }

   @Test
   public void should_create_correct_model_handlaggning_multiple_individ_roller()
   {
      var handlaggning = createModelHandlaggning();
      var updatedYrkande = ImmutableYrkande.builder()
            .from(handlaggning.yrkande())
            .addIndividYrkandeRoller(createIndividYrkandeRoll())
            .build();
      var updatedHandlaggning = ImmutableHandlaggning.builder()
            .from(handlaggning)
            .yrkande(updatedYrkande)
            .build();

      assertEquals(updatedHandlaggning, handlaggningMapper.toHandlaggning(toApiHandlaggning(updatedHandlaggning)));
   }

   @Test
   public void should_create_correct_model_handlaggning_multiple_producerade_resultat()
   {
      var handlaggning = createModelHandlaggning();
      var updatedYrkande = ImmutableYrkande.builder()
            .from(handlaggning.yrkande())
            .addProduceradeResultat(createProduceratResultat())
            .build();
      var updatedHandlaggning = ImmutableHandlaggning.builder()
            .from(handlaggning)
            .yrkande(updatedYrkande)
            .build();

      assertEquals(updatedHandlaggning, handlaggningMapper.toHandlaggning(toApiHandlaggning(updatedHandlaggning)));
   }

   @Test
   public void should_create_correct_model_handlaggning_null_beslut()
   {
      var handlaggning = createModelHandlaggning();
      var updatedYrkande = ImmutableYrkande.builder()
            .from(handlaggning.yrkande())
            .beslut(null)
            .build();
      var updatedHandlaggning = ImmutableHandlaggning.builder()
            .from(handlaggning)
            .yrkande(updatedYrkande)
            .build();

      assertEquals(updatedHandlaggning, handlaggningMapper.toHandlaggning(toApiHandlaggning(updatedHandlaggning)));
   }

   @Test
   public void should_create_correct_model_handlaggning_avslutad_ts_null()
   {
      var handlaggning = ImmutableHandlaggning.builder()
            .from(createModelHandlaggning())
            .avslutadTS(null)
            .build();

      assertEquals(handlaggning, handlaggningMapper.toHandlaggning(toApiHandlaggning(handlaggning)));
   }
}
