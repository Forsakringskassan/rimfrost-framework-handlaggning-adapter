package se.fk.rimfrost.framework.handlaggning.adapter.dto;

import jakarta.annotation.Nullable;
import org.immutables.value.Value;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Value.Immutable
public interface HandlaggningResponse
{

   UUID handlaggningId();

   String personnummer();

   String formanstyp();

   List<Ersattning> ersattning();

   @Value.Immutable
   interface Ersattning
   {

      UUID ersattningsId();

      String ersattningsTyp();

      int omfattningsProcent();

      int belopp();

      int berakningsgrund();

      @Nullable
      String beslutsutfall();

      LocalDate franOchMed();

      LocalDate tillOchMed();
   }

}
