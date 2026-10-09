package se.fk.rimfrost.framework.handlaggning;

import io.quarkus.test.component.QuarkusComponentTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import se.fk.rimfrost.framework.handlaggning.adapter.HandlaggningMapper;
import se.fk.rimfrost.framework.handlaggning.model.HandlaggningUpdate;
import se.fk.rimfrost.framework.handlaggning.model.ImmutableHandlaggning;
import se.fk.rimfrost.framework.handlaggning.model.ImmutableHandlaggningUpdate;
import se.fk.rimfrost.framework.handlaggning.model.ImmutableUppgift;
import se.fk.rimfrost.framework.handlaggning.model.ImmutableYrkande;
import se.fk.rimfrost.framework.handlaggning.model.Uppgift;
import se.fk.rimfrost.framework.handlaggning.model.Yrkande;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static se.fk.rimfrost.framework.handlaggning.TestData.createBeslut;
import static se.fk.rimfrost.framework.handlaggning.TestData.createModelHandlaggning;
import static se.fk.rimfrost.framework.handlaggning.TestData.createModelHandlaggningUpdate;
import static se.fk.rimfrost.framework.handlaggning.TestData.createModelYrkande;
import static se.fk.rimfrost.framework.handlaggning.TestData.createRollIYrkande;
import static se.fk.rimfrost.framework.handlaggning.TestData.createSakfragaStallningstagande;
import static se.fk.rimfrost.framework.handlaggning.TestData.createUppgiftsdatakopia;
import static se.fk.rimfrost.framework.handlaggning.TestData.createUppgift;
import static se.fk.rimfrost.framework.handlaggning.TestData.createUppgiftsdatakoppling;
import static se.fk.rimfrost.framework.handlaggning.TestUtils.toApiHandlaggning;
import static se.fk.rimfrost.framework.handlaggning.TestUtils.toApiHandlaggningUpdate;
import static se.fk.rimfrost.framework.handlaggning.TestUtils.toApiYrkande;

@QuarkusComponentTest
public class HandlaggningMapperTest
{
   @Inject
   HandlaggningMapper handlaggningMapper;

   private static HandlaggningUpdate updateWithYrkande(Yrkande yrkande)
   {
      var handlaggningUpdate = createModelHandlaggningUpdate();
      return ImmutableHandlaggningUpdate.builder()
            .from(handlaggningUpdate)
            .handlaggning(ImmutableHandlaggning.builder()
                  .from(handlaggningUpdate.handlaggning())
                  .yrkande(yrkande)
                  .build())
            .build();
   }

   private static HandlaggningUpdate updateWithUppgift(Uppgift uppgift)
   {
      return ImmutableHandlaggningUpdate.builder()
            .from(createModelHandlaggningUpdate())
            .uppgift(uppgift)
            .build();
   }

   // ---------------------
   //
   // Yrkande
   //
   // ---------------------

   @Test
   public void should_create_correct_api_yrkande()
   {
      var expectedYrkande = createModelYrkande();
      var apiYrkande = toApiYrkande(expectedYrkande);
      assertEquals(expectedYrkande, handlaggningMapper.toYrkande(apiYrkande));
   }

   @Test
   public void should_create_correct_api_yrkande_multiple_roller_i_yrkande()
   {
      var expectedYrkande = ImmutableYrkande.builder()
            .from(createModelYrkande())
            .addRollerIYrkande(createRollIYrkande())
            .build();
      var apiYrkande = toApiYrkande(expectedYrkande);
      assertEquals(expectedYrkande, handlaggningMapper.toYrkande(apiYrkande));
   }

   @Test
   public void should_create_correct_api_yrkande_multiple_sakfragor_stallningstaganden()
   {
      var expectedYrkande = ImmutableYrkande.builder()
            .from(createModelYrkande())
            .addSakfragorStallningstaganden(createSakfragaStallningstagande())
            .build();
      var apiYrkande = toApiYrkande(expectedYrkande);
      assertEquals(expectedYrkande, handlaggningMapper.toYrkande(apiYrkande));
   }

   @Test
   public void should_create_correct_api_yrkande_with_beslut()
   {
      var expectedYrkande = ImmutableYrkande.builder()
            .from(createModelYrkande())
            .addBeslut(createBeslut())
            .build();
      var apiYrkande = toApiYrkande(expectedYrkande);
      assertEquals(expectedYrkande, handlaggningMapper.toYrkande(apiYrkande));
   }

   @Test
   public void should_create_correct_api_yrkande_multiple_beslut()
   {
      var expectedYrkande = ImmutableYrkande.builder()
            .from(createModelYrkande())
            .addBeslut(createBeslut(), createBeslut())
            .build();
      var apiYrkande = toApiYrkande(expectedYrkande);
      assertEquals(expectedYrkande, handlaggningMapper.toYrkande(apiYrkande));
   }

   // ---------------------
   //
   // HandlaggningUpdate to API
   //
   // ---------------------

   @Test
   public void should_create_correct_api_handlaggning_update()
   {
      var handlaggningUpdate = createModelHandlaggningUpdate();
      assertEquals(toApiHandlaggningUpdate(handlaggningUpdate), handlaggningMapper.toApiHandlaggningUpdate(handlaggningUpdate));
   }

   @Test
   public void should_create_correct_api_handlaggning_update_null_handlaggning_id_varde()
   {
      var handlaggningUpdate = ImmutableHandlaggningUpdate.builder()
            .from(createModelHandlaggningUpdate())
            .handlaggning(ImmutableHandlaggning.builder()
                  .from(createModelHandlaggning())
                  .handlaggningIdVarde(null)
                  .build())
            .build();
      assertEquals(toApiHandlaggningUpdate(handlaggningUpdate), handlaggningMapper.toApiHandlaggningUpdate(handlaggningUpdate));
   }

   @Test
   public void should_create_correct_api_handlaggning_update_avslutad_ts_null()
   {
      var handlaggningUpdate = ImmutableHandlaggningUpdate.builder()
            .from(createModelHandlaggningUpdate())
            .handlaggning(ImmutableHandlaggning.builder()
                  .from(createModelHandlaggning())
                  .avslutadTS(null)
                  .build())
            .build();
      assertEquals(toApiHandlaggningUpdate(handlaggningUpdate), handlaggningMapper.toApiHandlaggningUpdate(handlaggningUpdate));
   }

   @Test
   public void should_create_correct_api_handlaggning_update_uppgift_null()
   {
      var handlaggningUpdate = updateWithUppgift(null);
      var apiHandlaggningUpdate = handlaggningMapper.toApiHandlaggningUpdate(handlaggningUpdate);
      assertNull(apiHandlaggningUpdate.getUppgift());
      assertEquals(toApiHandlaggningUpdate(createModelHandlaggningUpdate()).getHandlaggning(),
            apiHandlaggningUpdate.getHandlaggning());
   }

   @Test
   public void should_create_correct_api_handlaggning_update_multiple_roller_i_yrkande()
   {
      var handlaggningUpdate = updateWithYrkande(ImmutableYrkande.builder()
            .from(createModelYrkande())
            .addRollerIYrkande(createRollIYrkande())
            .build());
      assertEquals(toApiHandlaggningUpdate(handlaggningUpdate), handlaggningMapper.toApiHandlaggningUpdate(handlaggningUpdate));
   }

   @Test
   public void should_create_correct_api_handlaggning_update_multiple_sakfragor_stallningstaganden()
   {
      var handlaggningUpdate = updateWithYrkande(ImmutableYrkande.builder()
            .from(createModelYrkande())
            .addSakfragorStallningstaganden(createSakfragaStallningstagande())
            .build());
      assertEquals(toApiHandlaggningUpdate(handlaggningUpdate), handlaggningMapper.toApiHandlaggningUpdate(handlaggningUpdate));
   }

   @Test
   public void should_create_correct_api_handlaggning_update_with_beslut()
   {
      var handlaggningUpdate = updateWithYrkande(ImmutableYrkande.builder()
            .from(createModelYrkande())
            .addBeslut(createBeslut())
            .build());
      assertEquals(toApiHandlaggningUpdate(handlaggningUpdate), handlaggningMapper.toApiHandlaggningUpdate(handlaggningUpdate));
   }

   @Test
   public void should_create_correct_api_handlaggning_update_multiple_underlag()
   {
      var handlaggningUpdate = updateWithUppgift(ImmutableUppgift.builder()
            .from(createUppgift())
            .addUnderlag(createUppgiftsdatakopia())
            .build());
      assertEquals(toApiHandlaggningUpdate(handlaggningUpdate), handlaggningMapper.toApiHandlaggningUpdate(handlaggningUpdate));
   }

   @Test
   public void should_create_correct_api_handlaggning_update_underlag_with_koppling()
   {
      var handlaggningUpdate = updateWithUppgift(ImmutableUppgift.builder()
            .from(createUppgift())
            .addUnderlag(createUppgiftsdatakoppling())
            .build());
      assertEquals(toApiHandlaggningUpdate(handlaggningUpdate), handlaggningMapper.toApiHandlaggningUpdate(handlaggningUpdate));
   }

   @Test
   public void should_create_correct_api_handlaggning_update_multiple_resultat()
   {
      var handlaggningUpdate = updateWithUppgift(ImmutableUppgift.builder()
            .from(createUppgift())
            .addResultat(createUppgiftsdatakoppling())
            .build());
      assertEquals(toApiHandlaggningUpdate(handlaggningUpdate), handlaggningMapper.toApiHandlaggningUpdate(handlaggningUpdate));
   }

   @Test
   public void should_create_correct_api_handlaggning_update_uppgift_utford_ts_null()
   {
      var handlaggningUpdate = updateWithUppgift(ImmutableUppgift.builder()
            .from(createUppgift())
            .utfordTS(null)
            .build());
      assertEquals(toApiHandlaggningUpdate(handlaggningUpdate), handlaggningMapper.toApiHandlaggningUpdate(handlaggningUpdate));
   }

   @Test
   public void should_create_correct_api_handlaggning_update_uppgift_planerad_till_ts_null()
   {
      var handlaggningUpdate = updateWithUppgift(ImmutableUppgift.builder()
            .from(createUppgift())
            .planeradTillTS(null)
            .build());
      assertEquals(toApiHandlaggningUpdate(handlaggningUpdate), handlaggningMapper.toApiHandlaggningUpdate(handlaggningUpdate));
   }

   @Test
   public void should_create_correct_api_handlaggning_update_uppgift_utforare_null()
   {
      var handlaggningUpdate = updateWithUppgift(ImmutableUppgift.builder()
            .from(createUppgift())
            .utforare(null)
            .build());
      assertEquals(toApiHandlaggningUpdate(handlaggningUpdate), handlaggningMapper.toApiHandlaggningUpdate(handlaggningUpdate));
   }

   @Test
   public void should_create_correct_api_handlaggning_update_uppgift_uppgift_status_null()
   {
      var handlaggningUpdate = updateWithUppgift(ImmutableUppgift.builder()
            .from(createUppgift())
            .uppgiftStatus(null)
            .build());
      assertEquals(toApiHandlaggningUpdate(handlaggningUpdate), handlaggningMapper.toApiHandlaggningUpdate(handlaggningUpdate));
   }

   @Test
   public void should_create_correct_api_handlaggning_update_uppgift_kommentar_null()
   {
      var handlaggningUpdate = updateWithUppgift(ImmutableUppgift.builder()
            .from(createUppgift())
            .kommentar(null)
            .build());
      assertEquals(toApiHandlaggningUpdate(handlaggningUpdate), handlaggningMapper.toApiHandlaggningUpdate(handlaggningUpdate));
   }

   @Test
   public void should_create_correct_api_handlaggning_update_uppgift_regelutfall_null()
   {
      var handlaggningUpdate = updateWithUppgift(ImmutableUppgift.builder()
            .from(createUppgift())
            .regelutfall(null)
            .build());
      assertEquals(toApiHandlaggningUpdate(handlaggningUpdate), handlaggningMapper.toApiHandlaggningUpdate(handlaggningUpdate));
   }

   // ---------------------
   //
   // HandlaggningUpdate to model
   //
   // ---------------------

   @Test
   public void should_create_correct_model_handlaggning_update()
   {
      var handlaggningUpdate = createModelHandlaggningUpdate();
      assertEquals(handlaggningUpdate, handlaggningMapper.toHandlaggningUpdate(toApiHandlaggningUpdate(handlaggningUpdate)));
   }

   @Test
   public void should_create_correct_model_handlaggning_update_null_handlaggning_id_varde()
   {
      var handlaggningUpdate = ImmutableHandlaggningUpdate.builder()
            .from(createModelHandlaggningUpdate())
            .handlaggning(ImmutableHandlaggning.builder()
                  .from(createModelHandlaggning())
                  .handlaggningIdVarde(null)
                  .build())
            .build();
      assertEquals(handlaggningUpdate, handlaggningMapper.toHandlaggningUpdate(toApiHandlaggningUpdate(handlaggningUpdate)));
   }

   @Test
   public void should_create_correct_model_handlaggning_update_avslutad_ts_null()
   {
      var handlaggningUpdate = ImmutableHandlaggningUpdate.builder()
            .from(createModelHandlaggningUpdate())
            .handlaggning(ImmutableHandlaggning.builder()
                  .from(createModelHandlaggning())
                  .avslutadTS(null)
                  .build())
            .build();
      assertEquals(handlaggningUpdate, handlaggningMapper.toHandlaggningUpdate(toApiHandlaggningUpdate(handlaggningUpdate)));
   }

   @Test
   public void should_create_correct_model_handlaggning_update_multiple_roller_i_yrkande()
   {
      var handlaggningUpdate = updateWithYrkande(ImmutableYrkande.builder()
            .from(createModelYrkande())
            .addRollerIYrkande(createRollIYrkande())
            .build());
      assertEquals(handlaggningUpdate, handlaggningMapper.toHandlaggningUpdate(toApiHandlaggningUpdate(handlaggningUpdate)));
   }

   @Test
   public void should_create_correct_model_handlaggning_update_multiple_sakfragor_stallningstaganden()
   {
      var handlaggningUpdate = updateWithYrkande(ImmutableYrkande.builder()
            .from(createModelYrkande())
            .addSakfragorStallningstaganden(createSakfragaStallningstagande())
            .build());
      assertEquals(handlaggningUpdate, handlaggningMapper.toHandlaggningUpdate(toApiHandlaggningUpdate(handlaggningUpdate)));
   }

   @Test
   public void should_create_correct_model_handlaggning_update_with_beslut()
   {
      var handlaggningUpdate = updateWithYrkande(ImmutableYrkande.builder()
            .from(createModelYrkande())
            .addBeslut(createBeslut())
            .build());
      assertEquals(handlaggningUpdate, handlaggningMapper.toHandlaggningUpdate(toApiHandlaggningUpdate(handlaggningUpdate)));
   }

   @Test
   public void should_create_correct_model_handlaggning_update_multiple_underlag()
   {
      var handlaggningUpdate = updateWithUppgift(ImmutableUppgift.builder()
            .from(createUppgift())
            .addUnderlag(createUppgiftsdatakopia())
            .build());
      assertEquals(handlaggningUpdate, handlaggningMapper.toHandlaggningUpdate(toApiHandlaggningUpdate(handlaggningUpdate)));
   }

   @Test
   public void should_create_correct_model_handlaggning_update_underlag_with_koppling()
   {
      var handlaggningUpdate = updateWithUppgift(ImmutableUppgift.builder()
            .from(createUppgift())
            .addUnderlag(createUppgiftsdatakoppling())
            .build());
      assertEquals(handlaggningUpdate, handlaggningMapper.toHandlaggningUpdate(toApiHandlaggningUpdate(handlaggningUpdate)));
   }

   @Test
   public void should_create_correct_model_handlaggning_update_multiple_resultat()
   {
      var handlaggningUpdate = updateWithUppgift(ImmutableUppgift.builder()
            .from(createUppgift())
            .addResultat(createUppgiftsdatakoppling())
            .build());
      assertEquals(handlaggningUpdate, handlaggningMapper.toHandlaggningUpdate(toApiHandlaggningUpdate(handlaggningUpdate)));
   }

   @Test
   public void should_create_correct_model_handlaggning_update_missing_underlag_and_resultat()
   {
      var expectedHandlaggningUpdate = updateWithUppgift(ImmutableUppgift.builder()
            .from(createUppgift())
            .underlag(List.of())
            .resultat(List.of())
            .build());
      var apiHandlaggningUpdate = toApiHandlaggningUpdate(expectedHandlaggningUpdate);
      apiHandlaggningUpdate.getUppgift().setUnderlag(null);
      apiHandlaggningUpdate.getUppgift().setResultat(null);
      assertEquals(expectedHandlaggningUpdate, handlaggningMapper.toHandlaggningUpdate(apiHandlaggningUpdate));
   }

   @Test
   public void should_create_correct_model_handlaggning_update_uppgift_utford_ts_null()
   {
      var handlaggningUpdate = updateWithUppgift(ImmutableUppgift.builder()
            .from(createUppgift())
            .utfordTS(null)
            .build());
      assertEquals(handlaggningUpdate, handlaggningMapper.toHandlaggningUpdate(toApiHandlaggningUpdate(handlaggningUpdate)));
   }

   @Test
   public void should_create_correct_model_handlaggning_update_uppgift_planerad_till_ts_null()
   {
      var handlaggningUpdate = updateWithUppgift(ImmutableUppgift.builder()
            .from(createUppgift())
            .planeradTillTS(null)
            .build());
      assertEquals(handlaggningUpdate, handlaggningMapper.toHandlaggningUpdate(toApiHandlaggningUpdate(handlaggningUpdate)));
   }

   @Test
   public void should_create_correct_model_handlaggning_update_uppgift_utforare_null()
   {
      var handlaggningUpdate = updateWithUppgift(ImmutableUppgift.builder()
            .from(createUppgift())
            .utforare(null)
            .build());
      assertEquals(handlaggningUpdate, handlaggningMapper.toHandlaggningUpdate(toApiHandlaggningUpdate(handlaggningUpdate)));
   }

   @Test
   public void should_create_correct_model_handlaggning_update_uppgift_uppgift_status_null()
   {
      var handlaggningUpdate = updateWithUppgift(ImmutableUppgift.builder()
            .from(createUppgift())
            .uppgiftStatus(null)
            .build());
      assertEquals(handlaggningUpdate, handlaggningMapper.toHandlaggningUpdate(toApiHandlaggningUpdate(handlaggningUpdate)));
   }

   @Test
   public void should_create_correct_model_handlaggning_update_uppgift_kommentar_null()
   {
      var handlaggningUpdate = updateWithUppgift(ImmutableUppgift.builder()
            .from(createUppgift())
            .kommentar(null)
            .build());
      assertEquals(handlaggningUpdate, handlaggningMapper.toHandlaggningUpdate(toApiHandlaggningUpdate(handlaggningUpdate)));
   }

   @Test
   public void should_create_correct_model_handlaggning_update_uppgift_regelutfall_null()
   {
      var handlaggningUpdate = updateWithUppgift(ImmutableUppgift.builder()
            .from(createUppgift())
            .regelutfall(null)
            .build());
      assertEquals(handlaggningUpdate, handlaggningMapper.toHandlaggningUpdate(toApiHandlaggningUpdate(handlaggningUpdate)));
   }

   // ---------------------
   //
   // Handlaggning to model
   //
   // ---------------------

   @Test
   public void should_create_correct_model_handlaggning()
   {
      var handlaggning = createModelHandlaggning();
      assertEquals(handlaggning, handlaggningMapper.toHandlaggning(toApiHandlaggning(handlaggning)));
   }

   @Test
   public void should_create_correct_model_handlaggning_multiple_roller_i_yrkande()
   {
      var handlaggning = createModelHandlaggning();
      var updatedHandlaggning = ImmutableHandlaggning.builder()
            .from(handlaggning)
            .yrkande(ImmutableYrkande.builder()
                  .from(handlaggning.yrkande())
                  .addRollerIYrkande(createRollIYrkande())
                  .build())
            .build();
      assertEquals(updatedHandlaggning, handlaggningMapper.toHandlaggning(toApiHandlaggning(updatedHandlaggning)));
   }

   @Test
   public void should_create_correct_model_handlaggning_multiple_sakfragor_stallningstaganden()
   {
      var handlaggning = createModelHandlaggning();
      var updatedHandlaggning = ImmutableHandlaggning.builder()
            .from(handlaggning)
            .yrkande(ImmutableYrkande.builder()
                  .from(handlaggning.yrkande())
                  .addSakfragorStallningstaganden(createSakfragaStallningstagande())
                  .build())
            .build();
      assertEquals(updatedHandlaggning, handlaggningMapper.toHandlaggning(toApiHandlaggning(updatedHandlaggning)));
   }

   @Test
   public void should_create_correct_model_handlaggning_with_beslut()
   {
      var handlaggning = createModelHandlaggning();
      var updatedHandlaggning = ImmutableHandlaggning.builder()
            .from(handlaggning)
            .yrkande(ImmutableYrkande.builder()
                  .from(handlaggning.yrkande())
                  .addBeslut(createBeslut())
                  .build())
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

   @Test
   public void should_create_correct_model_handlaggning_handlaggning_id_varde_null()
   {
      var handlaggning = ImmutableHandlaggning.builder()
            .from(createModelHandlaggning())
            .handlaggningIdVarde(null)
            .build();
      assertEquals(handlaggning, handlaggningMapper.toHandlaggning(toApiHandlaggning(handlaggning)));
   }
}
