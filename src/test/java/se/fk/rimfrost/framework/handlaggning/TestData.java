package se.fk.rimfrost.framework.handlaggning;

import se.fk.rimfrost.framework.handlaggning.model.Handlaggning;
import se.fk.rimfrost.framework.handlaggning.model.HandlaggningUpdate;
import se.fk.rimfrost.framework.handlaggning.model.ImmutableHandlaggning;
import se.fk.rimfrost.framework.handlaggning.model.ImmutableHandlaggningUpdate;
import se.fk.rimfrost.framework.handlaggning.model.ImmutableIdtyp;
import se.fk.rimfrost.framework.handlaggning.model.ImmutableIndividYrkandeRoll;
import se.fk.rimfrost.framework.handlaggning.model.ImmutableProduceratResultat;
import se.fk.rimfrost.framework.handlaggning.model.ImmutableUnderlag;
import se.fk.rimfrost.framework.handlaggning.model.ImmutableUppgift;
import se.fk.rimfrost.framework.handlaggning.model.ImmutableUppgiftSpecifikation;
import se.fk.rimfrost.framework.handlaggning.model.ImmutableYrkande;
import se.fk.rimfrost.framework.handlaggning.model.ProduceratResultat;
import se.fk.rimfrost.framework.handlaggning.model.Underlag;
import se.fk.rimfrost.framework.handlaggning.model.Uppgift;
import se.fk.rimfrost.framework.handlaggning.model.Yrkande;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class TestData
{
   public static Yrkande.IndividYrkandeRoll createIndividYrkandeRoll()
   {
      var individ = ImmutableIdtyp.builder()
            .typId("ec00ec43-ed93-4e71-b533-88e74417fc53")
            .varde("199901011234")
            .build();

      return ImmutableIndividYrkandeRoll.builder()
            .individ(individ)
            .yrkandeRollId("5f7b256a-9ca9-41c7-9e64-6a587fb35cc1")
            .build();
   }

   public static ProduceratResultat createProduceratResultat()
   {
      return ImmutableProduceratResultat.builder()
            .id(UUID.fromString("cf188fb1-f217-42e5-b22b-2ff758c79e76"))
            .version(1)
            .resultatFrom(OffsetDateTime.parse("2026-04-15T08:00:00+00"))
            .resultatTom(OffsetDateTime.parse("2026-04-18T17:00:00+00"))
            .yrkandeStatus("NY")
            .typ("ersattning")
            .data("{}")
            .build();
   }

   public static Yrkande createModelYrkande()
   {
      return ImmutableYrkande.builder()
            .id(UUID.fromString("cb9537db-0660-4281-a99e-a33bdf7ec412"))
            .version(1)
            .erbjudandeId("59b2a5c2-f102-47ce-98ad-3bac1ab420a8")
            .yrkandeDatum(OffsetDateTime.parse("2026-04-23T10:15:55+00"))
            .yrkandeStatus("8f278a1d-0821-468f-9f64-7f8e1cd99f86")
            .yrkandeFrom(OffsetDateTime.parse("2026-04-15T08:00:00+00"))
            .yrkandeTom(OffsetDateTime.parse("2026-04-18T17:00:00+00"))
            .avsikt("NY")
            .individYrkandeRoller(List.of(createIndividYrkandeRoll()))
            .produceradeResultat(List.of(createProduceratResultat()))
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
            .processInstansId(UUID.fromString("0f710437-f846-45f9-8ba1-483e1247e975"))
            .skapadTS(OffsetDateTime.parse("2026-04-23T10:15:55+00"))
            .avslutadTS(OffsetDateTime.parse("2026-04-23T15:25:15+00"))
            .handlaggningspecifikationId(UUID.fromString("d85805ed-13d7-4315-a3f5-e85c67fa4bcb"))
            .build();
   }

   public static HandlaggningUpdate createModelHandlaggningUpdate()
   {
      return createModelHandlaggningUpdate(createModelHandlaggning());
   }

   public static Underlag createUnderlag()
   {
      return ImmutableUnderlag.builder()
            .typ("folkbokforing")
            .version(1)
            .data("")
            .build();
   }

   public static Uppgift createUppgift()
   {
      var utforarId = ImmutableIdtyp.builder()
            .typId("ec00ec43-ed93-4e71-b533-88e74417fc53")
            .varde("199001015555")
            .build();

      var uppgiftSpecifikation = ImmutableUppgiftSpecifikation.builder()
            .id(UUID.fromString("e856b685-6330-4767-a8c2-5ec9aca8bcba"))
            .version(1)
            .build();

      return ImmutableUppgift.builder()
            .id(UUID.fromString("425a97bb-7279-4562-ad01-f77d0db0ae4c"))
            .version(1)
            .skapadTs(OffsetDateTime.parse("2026-04-23T10:17:25+00"))
            .planeradTs(OffsetDateTime.parse("2026-04-23T11:40:00+00"))
            .utforarId(utforarId)
            .uppgiftStatus("NY")
            .aktivitetId(UUID.fromString("576fab63-205b-4004-87db-40793bb75209"))
            .fSSAinformation("ebdadec4-c126-4cbd-a2d7-bf71676211e9")
            .uppgiftSpecifikation(uppgiftSpecifikation)
            .build();
   }

   public static HandlaggningUpdate createModelHandlaggningUpdate(Handlaggning handlaggning)
   {
      return ImmutableHandlaggningUpdate.builder()
            .id(handlaggning.id())
            .version(handlaggning.version())
            .yrkande(handlaggning.yrkande())
            .processInstansId(Objects.requireNonNull(handlaggning.processInstansId()))
            .skapadTS(handlaggning.skapadTS())
            .avslutadTS(handlaggning.avslutadTS())
            .handlaggningspecifikationId(handlaggning.handlaggningspecifikationId())
            .underlag(List.of(createUnderlag()))
            .uppgift(createUppgift())
            .build();

   }
}
