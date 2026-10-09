package se.fk.rimfrost.framework.handlaggning.model;

import jakarta.annotation.Nullable;
import org.immutables.value.Value;
import java.time.OffsetDateTime;
import java.util.UUID;

@Value.Immutable
public interface Handlaggning
{

   UUID id();

   int version();

   Yrkande yrkande();

   String handlaggningIdTyp();

   @Nullable
   String handlaggningIdVarde();

   OffsetDateTime skapadTS();

   @Nullable
   OffsetDateTime avslutadTS();

   UUID handlaggningspecifikationId();
}
