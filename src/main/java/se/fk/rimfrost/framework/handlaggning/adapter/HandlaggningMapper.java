package se.fk.rimfrost.framework.handlaggning.adapter;

import jakarta.enterprise.context.ApplicationScoped;
import se.fk.rimfrost.framework.handlaggning.model.*;
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
import se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.*;
import se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Uppgiftsdata.TypEnum;
import java.util.List;

@ApplicationScoped
public class HandlaggningMapper
{

   // ---------------------
   //
   // Yrkande
   //
   // ---------------------

   //
   // to API
   //

   private List<se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.RollIYrkande> toApiRollerIYrkande(
         List<RollIYrkande> rollerIYrkande)
   {
      return rollerIYrkande.stream()
            .map(a -> {
               var b = new se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.RollIYrkande();
               b.setId(a.id());
               b.setIndivid(toApiIdtyp(a.individ()));
               b.setYrkandeRollId(a.yrkandeRollId());
               b.setAvserYrkande(a.avserYrkande());
               return b;
            })
            .toList();
   }

   private se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Idtyp toApiIdtyp(Idtyp idtyp)
   {
      if (idtyp == null)
      {
         return null;
      }

      se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Idtyp apiIdTyp = new se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Idtyp();
      apiIdTyp.setTypId(idtyp.typId());
      apiIdTyp.setVarde(idtyp.varde());

      return apiIdTyp;
   }

   private List<SakfragaStallningstagandeContainer> toApiSakfragorStallningstaganden(
         List<SakfragaStallningstagande> sakfragorStallningstaganden)
   {
      return sakfragorStallningstaganden.stream()
            .map(a -> {
               var b = new SakfragaStallningstagandeContainer();
               b.setId(a.id());
               b.setObjektTypId(a.objektTypId());
               b.setData(a.data());
               return b;
            })
            .toList();
   }

   private List<se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.SakfragaStallningstagandeRef> toApiSakfragorStallningstagandenRef(
         List<SakfragaStallningstagandeRef> sakfragorStallningstagandenRef)
   {
      return sakfragorStallningstagandenRef.stream()
            .map(a -> {
               var b = new se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.SakfragaStallningstagandeRef();
               b.setId(a.id());
               b.setVersion(a.version());
               return b;
            })
            .toList();
   }

   private se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Beslutsrad toApiBeslutsrad(Beslutsrad beslutsrad)
   {
      var apiBeslutsrad = new se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Beslutsrad();
      apiBeslutsrad.setId(beslutsrad.id());
      apiBeslutsrad.setVersion(beslutsrad.version());
      apiBeslutsrad.setBeslutsTyp(beslutsrad.beslutsTyp());
      apiBeslutsrad.setBeslutsUtfall(beslutsrad.beslutsUtfall());
      apiBeslutsrad.setAvslutsTyp(beslutsrad.avslutsTyp());
      apiBeslutsrad.setSakfragorStallningstaganden(toApiSakfragorStallningstagandenRef(beslutsrad.sakfragorStallningstaganden()));
      return apiBeslutsrad;
   }

   private se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Beslut toApiBeslut(Beslut beslut)
   {
      var apiBeslut = new se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Beslut();
      apiBeslut.setId(beslut.id());
      apiBeslut.setVersion(beslut.version());
      apiBeslut.setBeslutsfattare(toApiIdtyp(beslut.beslutsfattare()));
      apiBeslut.setDatum(beslut.datum());
      apiBeslut.setBeslutsrader(beslut.beslutsrader().stream().map(this::toApiBeslutsrad).toList());

      return apiBeslut;
   }

   private se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Yrkande toApiYrkande(Yrkande yrkande)
   {
      var apiYrkande = new se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Yrkande();
      apiYrkande.setId(yrkande.id());
      apiYrkande.setVersion(yrkande.version());
      apiYrkande.setIngangtypId(yrkande.ingangtypId());
      apiYrkande.setYrkandedatum(yrkande.yrkandeDatum());
      apiYrkande.setYrkandestatus(yrkande.yrkandeStatus());
      apiYrkande.setYrkandeFrom(yrkande.yrkandeFrom());
      apiYrkande.setYrkandeTom(yrkande.yrkandeTom());
      apiYrkande.setAvsikt(yrkande.avsikt());
      apiYrkande.setRollerIYrkande(toApiRollerIYrkande(yrkande.rollerIYrkande()));
      apiYrkande.setSakfragorStallningstaganden(toApiSakfragorStallningstaganden(yrkande.sakfragorStallningstaganden()));
      apiYrkande.setBeslut(yrkande.beslut().stream().map(this::toApiBeslut).toList());
      return apiYrkande;
   }

   // to model

   public Yrkande toYrkande(se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Yrkande apiYrkande)
   {
      return ImmutableYrkande.builder()
            .id(apiYrkande.getId())
            .version(apiYrkande.getVersion())
            .ingangtypId(apiYrkande.getIngangtypId())
            .yrkandeDatum(apiYrkande.getYrkandedatum())
            .yrkandeStatus(apiYrkande.getYrkandestatus())
            .yrkandeFrom(apiYrkande.getYrkandeFrom())
            .yrkandeTom(apiYrkande.getYrkandeTom())
            .avsikt(apiYrkande.getAvsikt())
            .rollerIYrkande(toRollerIYrkande(apiYrkande.getRollerIYrkande()))
            .sakfragorStallningstaganden(toSakfragorStallningstaganden(apiYrkande.getSakfragorStallningstaganden()))
            .beslut(apiYrkande.getBeslut().stream().map(this::toBeslut).toList())
            .build();
   }

   private Beslut toBeslut(se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Beslut apiBeslut)
   {
      return ImmutableBeslut.builder()
            .id(apiBeslut.getId())
            .version(apiBeslut.getVersion())
            .datum(apiBeslut.getDatum())
            .beslutsfattare(toIdtyp(apiBeslut.getBeslutsfattare()))
            .beslutsrader(apiBeslut.getBeslutsrader().stream().map(this::toBeslutsrad).toList())
            .build();
   }

   private Beslutsrad toBeslutsrad(se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Beslutsrad apiBeslutsrad)
   {
      return ImmutableBeslutsrad.builder()
            .id(apiBeslutsrad.getId())
            .version(apiBeslutsrad.getVersion())
            .beslutsTyp(apiBeslutsrad.getBeslutsTyp())
            .beslutsUtfall(apiBeslutsrad.getBeslutsUtfall())
            .avslutsTyp(apiBeslutsrad.getAvslutsTyp())
            .sakfragorStallningstaganden(toSakfragorStallningstagandenRef(apiBeslutsrad.getSakfragorStallningstaganden()))
            .build();
   }

   private List<SakfragaStallningstagandeRef> toSakfragorStallningstagandenRef(
         List<se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.SakfragaStallningstagandeRef> apiSakfragorStallningstagandenRef)
   {
      return apiSakfragorStallningstagandenRef.stream()
            .map(a -> (SakfragaStallningstagandeRef) ImmutableSakfragaStallningstagandeRef.builder()
                  .id(a.getId())
                  .version(a.getVersion())
                  .build())
            .toList();
   }

   private List<RollIYrkande> toRollerIYrkande(
         List<se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.RollIYrkande> apiRollerIYrkande)
   {
      return apiRollerIYrkande.stream()
            .map(a -> (RollIYrkande) ImmutableRollIYrkande.builder()
                  .id(a.getId())
                  .individ(toIdtyp(a.getIndivid()))
                  .yrkandeRollId(a.getYrkandeRollId())
                  .avserYrkande(a.getAvserYrkande())
                  .build())
            .toList();
   }

   private Idtyp toIdtyp(se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Idtyp apiIdTyp)
   {
      if (apiIdTyp == null)
      {
         return null;
      }

      return ImmutableIdtyp.builder()
            .typId(apiIdTyp.getTypId())
            .varde(apiIdTyp.getVarde())
            .build();
   }

   private List<SakfragaStallningstagande> toSakfragorStallningstaganden(
         List<SakfragaStallningstagandeContainer> apiSakfragorStallningstaganden)
   {
      return apiSakfragorStallningstaganden.stream()
            .map(a -> (SakfragaStallningstagande) ImmutableSakfragaStallningstagande.builder()
                  .id(a.getId())
                  .objektTypId(a.getObjektTypId())
                  .data(a.getData())
                  .build())
            .toList();
   }

   // ---------------------
   //
   // Handläggning
   //
   // ---------------------

   //
   // to API
   //

   public se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.HandlaggningUpdate toApiHandlaggningUpdate(
         HandlaggningUpdate handlaggningUpdate)
   {
      var apiHandlaggningUpdate = new se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.HandlaggningUpdate();
      apiHandlaggningUpdate.setHandlaggning(toApiHandlaggning(handlaggningUpdate.handlaggning()));
      apiHandlaggningUpdate.setUppgift(toApiUppgift(handlaggningUpdate.uppgift()));
      return apiHandlaggningUpdate;
   }

   private se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Handlaggning toApiHandlaggning(
         Handlaggning handlaggning)
   {
      var apiHandlaggning = new se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Handlaggning();
      apiHandlaggning.setId(handlaggning.id());
      apiHandlaggning.setVersion(handlaggning.version());
      apiHandlaggning.setYrkande(toApiYrkande(handlaggning.yrkande()));
      apiHandlaggning.setHandlaggningIdTyp(handlaggning.handlaggningIdTyp());
      apiHandlaggning.setHandlaggningIdVarde(handlaggning.handlaggningIdVarde());
      apiHandlaggning.setSkapadTS(handlaggning.skapadTS());
      apiHandlaggning.setAvslutadTS(handlaggning.avslutadTS());
      apiHandlaggning.setHandlaggningspecifikationId(handlaggning.handlaggningspecifikationId());
      return apiHandlaggning;
   }

   private List<se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Uppgiftsdata> toApiUnderlag(
         List<Uppgiftsdata> underlag)
   {
      return underlag.stream()
            .map(this::toApiUppgiftsdata)
            .toList();
   }

   private List<se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Uppgiftsdata> toApiResultat(
         List<Uppgiftsdatakoppling> resultat)
   {
      return resultat.stream()
            .map(this::toApiUppgiftsdata)
            .toList();
   }

   private se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Uppgiftsdata toApiUppgiftsdata(Uppgiftsdata uppgiftsdata)
   {
      return switch (uppgiftsdata)
      {
         case Uppgiftsdatakopia kopia -> {
            var apiKopia = new se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Uppgiftsdatakopia();
            apiKopia.setTyp(TypEnum.KOPIA);
            apiKopia.setInformationsobjektId(kopia.informationsobjektId());
            apiKopia.setInformationsobjektversion(kopia.informationsobjektversion());
            apiKopia.setInformationsobjekttyp(kopia.informationsobjekttyp());
            apiKopia.setData(kopia.data());
            yield apiKopia;
         }
         case Uppgiftsdatakoppling koppling -> {
            var apiKoppling = new se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Uppgiftsdatakoppling();
            apiKoppling.setTyp(TypEnum.KOPPLING);
            apiKoppling.setInformationsobjektId(koppling.informationsobjektId());
            apiKoppling.setInformationsobjektversion(koppling.informationsobjektversion());
            yield apiKoppling;
         }
      };
   }

   private se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Regelutfall toApiRegelutfall(Regelutfall regelutfall)
   {
      if (regelutfall == null)
      {
         return null;
      }

      var apiRegelutfall = new se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Regelutfall();
      apiRegelutfall.setVarde(regelutfall.varde());
      return apiRegelutfall;
   }

   private se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Uppgift toApiUppgift(Uppgift uppgift)
   {
      if (uppgift == null)
      {
         return null;
      }

      var apiUppgift = new se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Uppgift();
      apiUppgift.setId(uppgift.id());
      apiUppgift.setVersion(uppgift.version());
      apiUppgift.setSkapadTS(uppgift.skapadTS());
      apiUppgift.setUtfordTS(uppgift.utfordTS());
      apiUppgift.setPlaneradTillTS(uppgift.planeradTillTS());
      apiUppgift.setUtforare(toApiIdtyp(uppgift.utforare()));
      apiUppgift.setAktivitetId(uppgift.aktivitetId());
      apiUppgift.setUppgiftspecifikation(toApiUppgiftSpecifikation(uppgift.uppgiftSpecifikation()));
      apiUppgift.setUppgiftStatus(uppgift.uppgiftStatus());
      apiUppgift.setKommentar(uppgift.kommentar());
      apiUppgift.setFsSAinformation(uppgift.fSSAinformation());
      apiUppgift.setRegelutfall(toApiRegelutfall(uppgift.regelutfall()));
      apiUppgift.setUnderlag(toApiUnderlag(uppgift.underlag()));
      apiUppgift.setResultat(toApiResultat(uppgift.resultat()));
      return apiUppgift;
   }

   private se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.UppgiftSpecifikation toApiUppgiftSpecifikation(
         UppgiftSpecifikation uppgiftSpecifikation)
   {
      var apiUppgiftSpecifikation = new se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.UppgiftSpecifikation();
      apiUppgiftSpecifikation.setId(uppgiftSpecifikation.id());
      apiUppgiftSpecifikation.setVersion(uppgiftSpecifikation.version());
      return apiUppgiftSpecifikation;
   }

   //
   // to model
   //

   private List<Uppgiftsdata> toUnderlag(
         List<se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Uppgiftsdata> apiUnderlag)
   {
      if (apiUnderlag == null)
      {
         return List.of();
      }

      return apiUnderlag.stream()
            .map(HandlaggningMapper::toUppgiftsdata)
            .toList();
   }

   private List<Uppgiftsdatakoppling> toResultat(
         List<se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Uppgiftsdata> apiResultat)
   {
      if (apiResultat == null)
      {
         return List.of();
      }

      return apiResultat.stream()
            .map(HandlaggningMapper::toUppgiftsdata)
            .map(HandlaggningMapper::toUppgiftsdatakoppling)
            .toList();
   }

   private static Uppgiftsdata toUppgiftsdata(
         se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Uppgiftsdata apiUppgiftsdata)
   {
      if (apiUppgiftsdata instanceof se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Uppgiftsdatakopia apiKopia)
      {
         return ImmutableUppgiftsdatakopia.builder()
               .informationsobjektId(apiKopia.getInformationsobjektId())
               .informationsobjektversion(apiKopia.getInformationsobjektversion())
               .informationsobjekttyp(apiKopia.getInformationsobjekttyp())
               .data(apiKopia.getData())
               .build();
      }
      if (apiUppgiftsdata instanceof se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Uppgiftsdatakoppling apiKoppling)
      {
         return ImmutableUppgiftsdatakoppling.builder()
               .informationsobjektId(apiKoppling.getInformationsobjektId())
               .informationsobjektversion(apiKoppling.getInformationsobjektversion())
               .build();
      }
      throw new IllegalArgumentException(
            "Uppgiftsdata av typ " + apiUppgiftsdata.getTyp() + " stöds inte");
   }

   private static Uppgiftsdatakoppling toUppgiftsdatakoppling(Uppgiftsdata uppgiftsdata)
   {
      if (uppgiftsdata instanceof Uppgiftsdatakoppling koppling)
      {
         return koppling;
      }
      throw new IllegalArgumentException("Resultat måste vara av typ KOPPLING");
   }

   private Regelutfall toRegelutfall(
         se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Regelutfall apiRegelutfall)
   {
      if (apiRegelutfall == null)
      {
         return null;
      }

      return ImmutableRegelutfall.builder()
            .varde(apiRegelutfall.getVarde())
            .build();
   }

   private UppgiftSpecifikation toUppgiftSpecifikation(
         se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.UppgiftSpecifikation apiUppgiftSpecifikation)
   {
      return ImmutableUppgiftSpecifikation.builder()
            .id(apiUppgiftSpecifikation.getId())
            .version(apiUppgiftSpecifikation.getVersion()).build();
   }

   private Uppgift toUppgift(se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Uppgift apiUppgift)
   {
      if (apiUppgift == null)
      {
         return null;
      }

      return ImmutableUppgift.builder()
            .id(apiUppgift.getId())
            .version(apiUppgift.getVersion())
            .skapadTS(apiUppgift.getSkapadTS())
            .utfordTS(apiUppgift.getUtfordTS())
            .planeradTillTS(apiUppgift.getPlaneradTillTS())
            .utforare(toIdtyp(apiUppgift.getUtforare()))
            .aktivitetId(apiUppgift.getAktivitetId())
            .uppgiftSpecifikation(toUppgiftSpecifikation(apiUppgift.getUppgiftspecifikation()))
            .uppgiftStatus(apiUppgift.getUppgiftStatus())
            .kommentar(apiUppgift.getKommentar())
            .fSSAinformation(apiUppgift.getFsSAinformation())
            .regelutfall(toRegelutfall(apiUppgift.getRegelutfall()))
            .underlag(toUnderlag(apiUppgift.getUnderlag()))
            .resultat(toResultat(apiUppgift.getResultat()))
            .build();
   }

   public HandlaggningUpdate toHandlaggningUpdate(
         se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.HandlaggningUpdate apiHandlaggningUpdate)
   {
      return ImmutableHandlaggningUpdate.builder()
            .handlaggning(toHandlaggning(apiHandlaggningUpdate.getHandlaggning()))
            .uppgift(toUppgift(apiHandlaggningUpdate.getUppgift()))
            .build();
   }

   public Handlaggning toHandlaggning(
         se.fk.rimfrost.jaxrsspec.controllers.generatedsource.model.Handlaggning apiHandlaggning)
   {
      return ImmutableHandlaggning.builder()
            .id(apiHandlaggning.getId())
            .version(apiHandlaggning.getVersion())
            .yrkande(toYrkande(apiHandlaggning.getYrkande()))
            .handlaggningIdTyp(apiHandlaggning.getHandlaggningIdTyp())
            .handlaggningIdVarde(apiHandlaggning.getHandlaggningIdVarde())
            .skapadTS(apiHandlaggning.getSkapadTS())
            .avslutadTS(apiHandlaggning.getAvslutadTS())
            .handlaggningspecifikationId(apiHandlaggning.getHandlaggningspecifikationId())
            .build();
   }

}
