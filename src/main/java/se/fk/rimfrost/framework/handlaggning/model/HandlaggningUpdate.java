package se.fk.rimfrost.framework.handlaggning.model;

import jakarta.annotation.Nullable;
import org.immutables.value.Value;

@Value.Immutable
public interface HandlaggningUpdate
{

   Handlaggning handlaggning();

   @Nullable
   Uppgift uppgift();
}
