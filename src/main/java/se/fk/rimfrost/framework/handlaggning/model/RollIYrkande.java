package se.fk.rimfrost.framework.handlaggning.model;

import org.immutables.value.Value;
import java.util.UUID;

@Value.Immutable
public interface RollIYrkande
{
   UUID id();

   Idtyp individ();

   String yrkandeRollId();

   UUID avserYrkande();
}
