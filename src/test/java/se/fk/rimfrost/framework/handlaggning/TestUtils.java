package se.fk.rimfrost.framework.handlaggning;

import se.fk.rimfrost.framework.handlaggning.model.Beslut;
import se.fk.rimfrost.framework.handlaggning.model.Beslutsrad;
import se.fk.rimfrost.framework.handlaggning.model.Handlaggning;
import se.fk.rimfrost.framework.handlaggning.model.HandlaggningUpdate;
import se.fk.rimfrost.framework.handlaggning.model.Idtyp;
import se.fk.rimfrost.framework.handlaggning.model.IndividYrkandeRoll;
import se.fk.rimfrost.framework.handlaggning.model.ProduceratResultat;
import se.fk.rimfrost.framework.handlaggning.model.ProduceratResultatRef;
import se.fk.rimfrost.framework.handlaggning.model.Underlag;
import se.fk.rimfrost.framework.handlaggning.model.Uppgift;
import se.fk.rimfrost.framework.handlaggning.model.UppgiftSpecifikation;
import se.fk.rimfrost.framework.handlaggning.model.Yrkande;
import se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.PutHandlaggningRequest;

public class TestUtils
{
   public static PutHandlaggningRequest toApiPutHandlaggningRequest(HandlaggningUpdate handlaggningUpdate)
   {
      PutHandlaggningRequest request = new PutHandlaggningRequest();
      request.setHandlaggning(toApiHandlaggningUpdate(handlaggningUpdate));
      return request;
   }

   public static se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Handlaggning toApiHandlaggning(
         Handlaggning modelHandlaggning)
   {
      se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Handlaggning handlaggning = new se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Handlaggning();
      handlaggning.setId(modelHandlaggning.id());
      handlaggning.setVersion(modelHandlaggning.version());
      handlaggning.setYrkande(toApiYrkande(modelHandlaggning.yrkande()));
      handlaggning.setSkapadTS(modelHandlaggning.skapadTS());
      handlaggning.setAvslutadTS(modelHandlaggning.avslutadTS());
      handlaggning.setProcessinstansId(modelHandlaggning.processInstansId());
      handlaggning.setHandlaggningspecifikationId(modelHandlaggning.handlaggningspecifikationId());
      return handlaggning;
   }

   public static se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.HandlaggningUpdate toApiHandlaggningUpdate(
         HandlaggningUpdate handlaggningUpdate)
   {
      se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.HandlaggningUpdate update = new se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.HandlaggningUpdate();
      update.setId(handlaggningUpdate.id());
      update.setVersion(handlaggningUpdate.version());
      update.setYrkande(toApiYrkande(handlaggningUpdate.yrkande()));
      update.setProcessinstansId(handlaggningUpdate.processInstansId());
      update.setSkapadTS(handlaggningUpdate.skapadTS());
      update.setAvslutadTS(handlaggningUpdate.avslutadTS());
      update.setHandlaggningspecifikationId(handlaggningUpdate.handlaggningspecifikationId());
      update.setUnderlag(handlaggningUpdate.underlag().stream().map(TestUtils::toApiUnderlag).toList());
      update.setUppgift(toApiUppgift(handlaggningUpdate.uppgift()));
      return update;
   }

   public static se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Yrkande toApiYrkande(Yrkande modelYrkande)
   {
      se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Yrkande yrkande = new se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Yrkande();
      yrkande.setId(modelYrkande.id());
      yrkande.setVersion(modelYrkande.version());
      yrkande.setErbjudandeId(modelYrkande.erbjudandeId());
      yrkande.setYrkandedatum(modelYrkande.yrkandeDatum());
      yrkande.setYrkandestatus(modelYrkande.yrkandeStatus());
      yrkande.setYrkandeFrom(modelYrkande.yrkandeFrom());
      yrkande.setYrkandeTom(modelYrkande.yrkandeTom());
      yrkande.setAvsikt(modelYrkande.avsikt());
      yrkande.setIndividYrkandeRoller(
            modelYrkande.individYrkandeRoller().stream().map(TestUtils::toApiIndividYrkandeRoll).toList());
      yrkande
            .setProduceradeResultat(modelYrkande.produceradeResultat().stream().map(TestUtils::toApiProduceratResultat).toList());
      yrkande.setBeslut(toApiBeslut(modelYrkande.beslut()));
      return yrkande;
   }

   private static se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.IndividYrkandeRoll toApiIndividYrkandeRoll(
         IndividYrkandeRoll modelIndividYrkandeRoll)
   {
      se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.IndividYrkandeRoll individYrkandeRoll = new se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.IndividYrkandeRoll();
      individYrkandeRoll.setIndivid(toApiIdTyp(modelIndividYrkandeRoll.individ()));
      individYrkandeRoll.setYrkandeRollId(modelIndividYrkandeRoll.yrkandeRollId());
      return individYrkandeRoll;
   }

   private static se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Idtyp toApiIdTyp(Idtyp modelIdtyp)
   {
      if (modelIdtyp == null)
      {
         return null;
      }

      se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Idtyp idTyp = new se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Idtyp();
      idTyp.setTypId(modelIdtyp.typId());
      idTyp.setVarde(modelIdtyp.varde());
      return idTyp;
   }

   private static se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.ProduceratResultat toApiProduceratResultat(
         ProduceratResultat modelProduceratResultat)
   {
      se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.ProduceratResultat produceratResultat = new se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.ProduceratResultat();
      produceratResultat.setId(modelProduceratResultat.id());
      produceratResultat.setVersion(modelProduceratResultat.version());
      produceratResultat.setFrom(modelProduceratResultat.resultatFrom());
      produceratResultat.setTom(modelProduceratResultat.resultatTom());
      produceratResultat.setYrkandestatus(modelProduceratResultat.yrkandeStatus());
      produceratResultat.setAvslagsanledning(modelProduceratResultat.avslagsanledning());
      produceratResultat.setTyp(modelProduceratResultat.typ());
      produceratResultat.setData(modelProduceratResultat.data());
      return produceratResultat;
   }

   private static se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Beslut toApiBeslut(Beslut modelBeslut)
   {
      if (modelBeslut == null)
      {
         return null;
      }

      se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Beslut beslut = new se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Beslut();
      beslut.setId(modelBeslut.id());
      beslut.setVersion(modelBeslut.version());
      beslut.setDatum(modelBeslut.datum());
      beslut.setBeslutsfattare(toApiIdTyp(modelBeslut.beslutsfattare()));
      beslut.setBeslutsrader(modelBeslut.beslutsrader().stream().map(TestUtils::toApiBeslutsrad).toList());
      return beslut;
   }

   private static se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Beslutsrad toApiBeslutsrad(
         Beslutsrad modelBeslutsrad)
   {
      se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Beslutsrad beslutsrad = new se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Beslutsrad();
      beslutsrad.setId(modelBeslutsrad.id());
      beslutsrad.setVersion(modelBeslutsrad.version());
      beslutsrad.setAvslutsTyp(modelBeslutsrad.avslutsTyp());
      beslutsrad.setBeslutsTyp(modelBeslutsrad.beslutsTyp());
      beslutsrad.setBeslutsUtfall(modelBeslutsrad.beslutsUtfall());
      beslutsrad.setProduceradeResultatRef(
            modelBeslutsrad.produceradeResultatRef().stream().map(TestUtils::toApiProduceratResultatRef).toList());
      return beslutsrad;
   }

   private static se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.ProduceratResultatRef toApiProduceratResultatRef(
         ProduceratResultatRef modelProduceratResultatRef)
   {
      se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.ProduceratResultatRef produceratResultatRef = new se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.ProduceratResultatRef();
      produceratResultatRef.setId(modelProduceratResultatRef.id());
      produceratResultatRef.setVersion(modelProduceratResultatRef.version());
      return produceratResultatRef;
   }

   private static se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Underlag toApiUnderlag(Underlag modelUnderlag)
   {
      se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Underlag underlag = new se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Underlag();
      underlag.setTyp(modelUnderlag.typ());
      underlag.setData(modelUnderlag.data());
      underlag.setVersion(modelUnderlag.version());
      return underlag;
   }

   private static se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Uppgift toApiUppgift(Uppgift modelUppgift)
   {
      if (modelUppgift == null)
      {
         return null;
      }

      se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Uppgift uppgift = new se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Uppgift();
      uppgift.setId(modelUppgift.id());
      uppgift.setVersion(modelUppgift.version());
      uppgift.setSkapadTs(modelUppgift.skapadTs());
      uppgift.setUtfordTs(modelUppgift.utfordTs());
      uppgift.setPlaneradTs(modelUppgift.planeradTs());
      uppgift.setUtforarId(toApiIdTyp(modelUppgift.utforarId()));
      uppgift.setAktivitetId(modelUppgift.aktivitetId());
      uppgift.setUppgiftspecifikation(toApiUppgiftSpecifikation(modelUppgift.uppgiftSpecifikation()));
      uppgift.setUppgiftStatus(modelUppgift.uppgiftStatus());
      uppgift.setFsSAinformation(modelUppgift.fSSAinformation());
      return uppgift;
   }

   private static se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.UppgiftSpecifikation toApiUppgiftSpecifikation(
         UppgiftSpecifikation modelUppgiftSpecifikation)
   {
      se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.UppgiftSpecifikation uppgiftSpecifikation = new se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.UppgiftSpecifikation();
      uppgiftSpecifikation.setId(modelUppgiftSpecifikation.id());
      uppgiftSpecifikation.setVersion(modelUppgiftSpecifikation.version());
      return uppgiftSpecifikation;
   }
}
