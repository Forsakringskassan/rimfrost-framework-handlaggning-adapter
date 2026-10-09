package se.fk.rimfrost.framework.handlaggning;

import se.fk.rimfrost.framework.handlaggning.model.Beslut;
import se.fk.rimfrost.framework.handlaggning.model.Handlaggning;
import se.fk.rimfrost.framework.handlaggning.model.HandlaggningUpdate;
import se.fk.rimfrost.framework.handlaggning.model.ImmutableBeslut;
import se.fk.rimfrost.framework.handlaggning.model.ImmutableBeslutsrad;
import se.fk.rimfrost.framework.handlaggning.model.ImmutableHandlaggning;
import se.fk.rimfrost.framework.handlaggning.model.ImmutableHandlaggningUpdate;
import se.fk.rimfrost.framework.handlaggning.model.ImmutableIdtyp;
import se.fk.rimfrost.framework.handlaggning.model.ImmutableRegelutfall;
import se.fk.rimfrost.framework.handlaggning.model.ImmutableRollIYrkande;
import se.fk.rimfrost.framework.handlaggning.model.ImmutableSakfragaStallningstagande;
import se.fk.rimfrost.framework.handlaggning.model.ImmutableSakfragaStallningstagandeRef;
import se.fk.rimfrost.framework.handlaggning.model.ImmutableUppgift;
import se.fk.rimfrost.framework.handlaggning.model.ImmutableUppgiftSpecifikation;
import se.fk.rimfrost.framework.handlaggning.model.ImmutableUppgiftsdata;
import se.fk.rimfrost.framework.handlaggning.model.ImmutableUppgiftsdatakoppling;
import se.fk.rimfrost.framework.handlaggning.model.ImmutableYrkande;
import se.fk.rimfrost.framework.handlaggning.model.RollIYrkande;
import se.fk.rimfrost.framework.handlaggning.model.SakfragaStallningstagande;
import se.fk.rimfrost.framework.handlaggning.model.Uppgift;
import se.fk.rimfrost.framework.handlaggning.model.Uppgiftsdata;
import se.fk.rimfrost.framework.handlaggning.model.Uppgiftsdatakoppling;
import se.fk.rimfrost.framework.handlaggning.model.Yrkande;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public class TestData
{
   private static final UUID YRKANDE_ID = UUID.fromString("cb9537db-0660-4281-a99e-a33bdf7ec412");

   private static final UUID SAKFRAGA_STALLNINGSTAGANDE_ID = UUID.fromString("cf188fb1-f217-42e5-b22b-2ff758c79e76");

   public static RollIYrkande createRollIYrkande()
   {
      var individ = ImmutableIdtyp.builder()
            .typId("ec00ec43-ed93-4e71-b533-88e74417fc53")
            .varde("199901011234")
            .build();

      return ImmutableRollIYrkande.builder()
            .id(UUID.fromString("3c1a9d2e-6f4b-4e8a-9b7c-1d2e3f4a5b6c"))
            .individ(individ)
            .yrkandeRollId("5f7b256a-9ca9-41c7-9e64-6a587fb35cc1")
            .avserYrkande(YRKANDE_ID)
            .build();
   }

   public static SakfragaStallningstagande createSakfragaStallningstagande()
   {
      return ImmutableSakfragaStallningstagande.builder()
            .id(SAKFRAGA_STALLNINGSTAGANDE_ID)
            .objektTypId("ersattning")
            .data("{}")
            .build();
   }

   public static Beslut createBeslut()
   {
      var beslutsfattare = ImmutableIdtyp.builder()
            .typId("ec00ec43-ed93-4e71-b533-88e74417fc53")
            .varde("91234567-89ab-4cde-9012-3456789abcde")
            .build();

      var sakfragaStallningstagandeRef = ImmutableSakfragaStallningstagandeRef.builder()
            .id(SAKFRAGA_STALLNINGSTAGANDE_ID)
            .version(1)
            .build();

      var beslutsrad = ImmutableBeslutsrad.builder()
            .id(UUID.fromString("a1b2c3d4-0000-0000-0000-000000000012"))
            .version(1)
            .beslutsTyp("e44d5e15-5231-4857-b992-5b8d9c34e36f")
            .beslutsUtfall("f011934d-d05e-404d-95ab-24571eec241b")
            .avslutsTyp("b1364919-6002-4e37-a759-556bd2ec4bfd")
            .addSakfragorStallningstaganden(sakfragaStallningstagandeRef)
            .build();

      return ImmutableBeslut.builder()
            .id(UUID.fromString("a1b2c3d4-0000-0000-0000-000000000011"))
            .version(1)
            .datum(OffsetDateTime.parse("2026-04-23T10:15:55+00"))
            .beslutsfattare(beslutsfattare)
            .addBeslutsrader(beslutsrad)
            .build();
   }

   public static Yrkande createModelYrkande()
   {
      return ImmutableYrkande.builder()
            .id(YRKANDE_ID)
            .version(1)
            .ingangtypId("59b2a5c2-f102-47ce-98ad-3bac1ab420a8")
            .yrkandeDatum(OffsetDateTime.parse("2026-04-23T10:15:55+00"))
            .yrkandeStatus("8f278a1d-0821-468f-9f64-7f8e1cd99f86")
            .yrkandeFrom(OffsetDateTime.parse("2026-04-15T08:00:00+00"))
            .yrkandeTom(OffsetDateTime.parse("2026-04-18T17:00:00+00"))
            .avsikt("NY")
            .rollerIYrkande(List.of(createRollIYrkande()))
            .sakfragorStallningstaganden(List.of(createSakfragaStallningstagande()))
            .build();
   }

   public static Handlaggning createModelHandlaggning()
   {
      return createModelHandlaggning(UUID.fromString("2e2040e4-ad4a-4bec-856b-2e39fa5f7133"));
   }

   public static Handlaggning createModelHandlaggning(UUID handlaggningId)
   {
      return ImmutableHandlaggning.builder()
            .id(handlaggningId)
            .version(1)
            .yrkande(createModelYrkande())
            .handlaggningIdTyp("0f710437-f846-45f9-8ba1-483e1247e975")
            .handlaggningIdVarde("HL-2026-0001")
            .skapadTS(OffsetDateTime.parse("2026-04-23T10:15:55+00"))
            .avslutadTS(OffsetDateTime.parse("2026-04-23T15:25:15+00"))
            .handlaggningspecifikationId(UUID.fromString("d85805ed-13d7-4315-a3f5-e85c67fa4bcb"))
            .build();
   }

   public static HandlaggningUpdate createModelHandlaggningUpdate()
   {
      return createModelHandlaggningUpdate(createModelHandlaggning());
   }

   public static Uppgiftsdata createUppgiftsdata()
   {
      return ImmutableUppgiftsdata.builder()
            .informationsobjektId("3f8b2c1d-6e4a-4d7b-9c5e-1a2b3c4d5e6f")
            .informationsobjektversion(1)
            .informationsobjekttyp("folkbokforing")
            .data("{}")
            .build();
   }

   public static Uppgiftsdatakoppling createUppgiftsdatakoppling()
   {
      return ImmutableUppgiftsdatakoppling.builder()
            .informationsobjektId("7d3e1c2b-8a9f-4b6e-a5d4-c3b2a1f0e9d8")
            .informationsobjektversion(1)
            .build();
   }

   public static Uppgift createUppgift()
   {
      var utforare = ImmutableIdtyp.builder()
            .typId("ec00ec43-ed93-4e71-b533-88e74417fc53")
            .varde("199001015555")
            .build();

      var uppgiftSpecifikation = ImmutableUppgiftSpecifikation.builder()
            .id(UUID.fromString("e856b685-6330-4767-a8c2-5ec9aca8bcba"))
            .version(1)
            .build();

      var regelutfall = ImmutableRegelutfall.builder()
            .varde("UPPFYLLT")
            .build();

      return ImmutableUppgift.builder()
            .id(UUID.fromString("425a97bb-7279-4562-ad01-f77d0db0ae4c"))
            .version(1)
            .skapadTS(OffsetDateTime.parse("2026-04-23T10:17:25+00"))
            .utfordTS(OffsetDateTime.parse("2026-04-23T11:52:10+00"))
            .planeradTillTS(OffsetDateTime.parse("2026-04-23T11:40:00+00"))
            .utforare(utforare)
            .uppgiftStatus("NY")
            .kommentar("Kommentar till uppgiften")
            .aktivitetId(UUID.fromString("576fab63-205b-4004-87db-40793bb75209"))
            .fSSAinformation("ebdadec4-c126-4cbd-a2d7-bf71676211e9")
            .uppgiftSpecifikation(uppgiftSpecifikation)
            .regelutfall(regelutfall)
            .underlag(List.of(createUppgiftsdata()))
            .resultat(List.of(createUppgiftsdatakoppling()))
            .build();
   }

   public static HandlaggningUpdate createModelHandlaggningUpdate(Handlaggning handlaggning)
   {
      return ImmutableHandlaggningUpdate.builder()
            .handlaggning(handlaggning)
            .uppgift(createUppgift())
            .build();
   }
}
