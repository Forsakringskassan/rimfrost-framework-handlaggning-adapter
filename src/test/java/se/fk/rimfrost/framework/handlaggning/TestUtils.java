package se.fk.rimfrost.framework.handlaggning;

import se.fk.rimfrost.framework.handlaggning.model.Beslut;
import se.fk.rimfrost.framework.handlaggning.model.Beslutsrad;
import se.fk.rimfrost.framework.handlaggning.model.Handlaggning;
import se.fk.rimfrost.framework.handlaggning.model.HandlaggningUpdate;
import se.fk.rimfrost.framework.handlaggning.model.Idtyp;
import se.fk.rimfrost.framework.handlaggning.model.Regelutfall;
import se.fk.rimfrost.framework.handlaggning.model.RollIYrkande;
import se.fk.rimfrost.framework.handlaggning.model.SakfragaStallningstagande;
import se.fk.rimfrost.framework.handlaggning.model.SakfragaStallningstagandeRef;
import se.fk.rimfrost.framework.handlaggning.model.Uppgift;
import se.fk.rimfrost.framework.handlaggning.model.UppgiftSpecifikation;
import se.fk.rimfrost.framework.handlaggning.model.Uppgiftsdata;
import se.fk.rimfrost.framework.handlaggning.model.Uppgiftsdatakopia;
import se.fk.rimfrost.framework.handlaggning.model.Uppgiftsdatakoppling;
import se.fk.rimfrost.framework.handlaggning.model.Yrkande;
import se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.SakfragaStallningstagandeContainer;
import se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Uppgiftsdata.TypEnum;

public class TestUtils
{
   public static se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Handlaggning toApiHandlaggning(
         Handlaggning modelHandlaggning)
   {
      se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Handlaggning handlaggning = new se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Handlaggning();
      handlaggning.setId(modelHandlaggning.id());
      handlaggning.setVersion(modelHandlaggning.version());
      handlaggning.setYrkande(toApiYrkande(modelHandlaggning.yrkande()));
      handlaggning.setHandlaggningIdTyp(modelHandlaggning.handlaggningIdTyp());
      handlaggning.setHandlaggningIdVarde(modelHandlaggning.handlaggningIdVarde());
      handlaggning.setSkapadTS(modelHandlaggning.skapadTS());
      handlaggning.setAvslutadTS(modelHandlaggning.avslutadTS());
      handlaggning.setHandlaggningspecifikationId(modelHandlaggning.handlaggningspecifikationId());
      return handlaggning;
   }

   public static se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.HandlaggningUpdate toApiHandlaggningUpdate(
         HandlaggningUpdate handlaggningUpdate)
   {
      se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.HandlaggningUpdate update = new se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.HandlaggningUpdate();
      update.setHandlaggning(toApiHandlaggning(handlaggningUpdate.handlaggning()));
      update.setUppgift(toApiUppgift(handlaggningUpdate.uppgift()));
      return update;
   }

   public static se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Yrkande toApiYrkande(Yrkande modelYrkande)
   {
      se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Yrkande yrkande = new se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Yrkande();
      yrkande.setId(modelYrkande.id());
      yrkande.setVersion(modelYrkande.version());
      yrkande.setIngangtypId(modelYrkande.ingangtypId());
      yrkande.setYrkandedatum(modelYrkande.yrkandeDatum());
      yrkande.setYrkandestatus(modelYrkande.yrkandeStatus());
      yrkande.setYrkandeFrom(modelYrkande.yrkandeFrom());
      yrkande.setYrkandeTom(modelYrkande.yrkandeTom());
      yrkande.setAvsikt(modelYrkande.avsikt());
      yrkande.setRollerIYrkande(modelYrkande.rollerIYrkande().stream().map(TestUtils::toApiRollIYrkande).toList());
      yrkande.setSakfragorStallningstaganden(
            modelYrkande.sakfragorStallningstaganden().stream().map(TestUtils::toApiSakfragaStallningstagande).toList());
      yrkande.setBeslut(modelYrkande.beslut().stream().map(TestUtils::toApiBeslut).toList());
      return yrkande;
   }

   private static se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.RollIYrkande toApiRollIYrkande(
         RollIYrkande modelRollIYrkande)
   {
      se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.RollIYrkande rollIYrkande = new se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.RollIYrkande();
      rollIYrkande.setId(modelRollIYrkande.id());
      rollIYrkande.setIndivid(toApiIdTyp(modelRollIYrkande.individ()));
      rollIYrkande.setYrkandeRollId(modelRollIYrkande.yrkandeRollId());
      rollIYrkande.setAvserYrkande(modelRollIYrkande.avserYrkande());
      return rollIYrkande;
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

   private static SakfragaStallningstagandeContainer toApiSakfragaStallningstagande(
         SakfragaStallningstagande modelSakfragaStallningstagande)
   {
      SakfragaStallningstagandeContainer sakfragaStallningstagande = new SakfragaStallningstagandeContainer();
      sakfragaStallningstagande.setId(modelSakfragaStallningstagande.id());
      sakfragaStallningstagande.setObjektTypId(modelSakfragaStallningstagande.objektTypId());
      sakfragaStallningstagande.setData(modelSakfragaStallningstagande.data());
      return sakfragaStallningstagande;
   }

   private static se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Beslut toApiBeslut(Beslut modelBeslut)
   {
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
      beslutsrad.setSakfragorStallningstaganden(
            modelBeslutsrad.sakfragorStallningstaganden().stream().map(TestUtils::toApiSakfragaStallningstagandeRef).toList());
      return beslutsrad;
   }

   private static se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.SakfragaStallningstagandeRef toApiSakfragaStallningstagandeRef(
         SakfragaStallningstagandeRef modelSakfragaStallningstagandeRef)
   {
      se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.SakfragaStallningstagandeRef sakfragaStallningstagandeRef = new se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.SakfragaStallningstagandeRef();
      sakfragaStallningstagandeRef.setId(modelSakfragaStallningstagandeRef.id());
      sakfragaStallningstagandeRef.setVersion(modelSakfragaStallningstagandeRef.version());
      return sakfragaStallningstagandeRef;
   }

   private static se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Uppgiftsdata toApiUppgiftsdata(
         Uppgiftsdata modelUppgiftsdata)
   {
      return switch (modelUppgiftsdata)
      {
         case Uppgiftsdatakopia modelKopia -> toApiUppgiftsdatakopia(modelKopia);
         case Uppgiftsdatakoppling modelKoppling -> toApiUppgiftsdatakoppling(modelKoppling);
      };
   }

   private static se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Uppgiftsdata toApiUppgiftsdatakopia(
         Uppgiftsdatakopia modelUppgiftsdatakopia)
   {
      se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Uppgiftsdatakopia kopia = new se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Uppgiftsdatakopia();
      kopia.setTyp(TypEnum.KOPIA);
      kopia.setInformationsobjektId(modelUppgiftsdatakopia.informationsobjektId());
      kopia.setInformationsobjektversion(modelUppgiftsdatakopia.informationsobjektversion());
      kopia.setInformationsobjekttyp(modelUppgiftsdatakopia.informationsobjekttyp());
      kopia.setData(modelUppgiftsdatakopia.data());
      return kopia;
   }

   private static se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Uppgiftsdata toApiUppgiftsdatakoppling(
         Uppgiftsdatakoppling modelUppgiftsdatakoppling)
   {
      se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Uppgiftsdatakoppling uppgiftsdatakoppling = new se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Uppgiftsdatakoppling();
      uppgiftsdatakoppling.setTyp(TypEnum.KOPPLING);
      uppgiftsdatakoppling.setInformationsobjektId(modelUppgiftsdatakoppling.informationsobjektId());
      uppgiftsdatakoppling.setInformationsobjektversion(modelUppgiftsdatakoppling.informationsobjektversion());
      return uppgiftsdatakoppling;
   }

   private static se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Regelutfall toApiRegelutfall(
         Regelutfall modelRegelutfall)
   {
      if (modelRegelutfall == null)
      {
         return null;
      }

      se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Regelutfall regelutfall = new se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Regelutfall();
      regelutfall.setVarde(modelRegelutfall.varde());
      return regelutfall;
   }

   public static se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Uppgift toApiUppgift(Uppgift modelUppgift)
   {
      se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Uppgift uppgift = new se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Uppgift();
      uppgift.setId(modelUppgift.id());
      uppgift.setVersion(modelUppgift.version());
      uppgift.setSkapadTS(modelUppgift.skapadTS());
      uppgift.setUtfordTS(modelUppgift.utfordTS());
      uppgift.setPlaneradTillTS(modelUppgift.planeradTillTS());
      uppgift.setUtforare(toApiIdTyp(modelUppgift.utforare()));
      uppgift.setAktivitetId(modelUppgift.aktivitetId());
      uppgift.setUppgiftspecifikation(toApiUppgiftSpecifikation(modelUppgift.uppgiftSpecifikation()));
      uppgift.setUppgiftStatus(modelUppgift.uppgiftStatus());
      uppgift.setKommentar(modelUppgift.kommentar());
      uppgift.setFsSAinformation(modelUppgift.fSSAinformation());
      uppgift.setRegelutfall(toApiRegelutfall(modelUppgift.regelutfall()));
      uppgift.setUnderlag(modelUppgift.underlag().stream().map(TestUtils::toApiUppgiftsdata).toList());
      uppgift.setResultat(modelUppgift.resultat().stream().map(TestUtils::toApiUppgiftsdatakoppling).toList());
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
