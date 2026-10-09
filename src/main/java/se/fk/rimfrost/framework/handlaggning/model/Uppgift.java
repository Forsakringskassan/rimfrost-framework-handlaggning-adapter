package se.fk.rimfrost.framework.handlaggning.model;

import jakarta.annotation.Nullable;
import org.immutables.value.Value;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Value.Immutable
public interface Uppgift
{

   UUID id();

   int version();

   OffsetDateTime skapadTS();

   @Nullable
   OffsetDateTime utfordTS();

   @Nullable
   OffsetDateTime planeradTillTS();

   @Nullable
   Idtyp utforare();

   @Nullable
   String uppgiftStatus();

   @Nullable
   String kommentar();

   UUID aktivitetId();

   String fSSAinformation();

   UppgiftSpecifikation uppgiftSpecifikation();

   @Nullable
   Regelutfall regelutfall();

   List<Uppgiftsdata> underlag();

   List<Uppgiftsdatakoppling> resultat();

}
