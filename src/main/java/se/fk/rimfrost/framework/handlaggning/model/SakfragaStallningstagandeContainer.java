package se.fk.rimfrost.framework.handlaggning.model;

import org.immutables.value.Value;
import java.util.UUID;

@Value.Immutable
public interface SakfragaStallningstagandeContainer
{
   UUID id();

   String objektTypId();

   String data();
}
