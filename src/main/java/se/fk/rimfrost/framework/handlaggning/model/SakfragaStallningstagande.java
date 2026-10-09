package se.fk.rimfrost.framework.handlaggning.model;

import org.immutables.value.Value;
import java.util.UUID;

@Value.Immutable
public interface SakfragaStallningstagande
{
   UUID id();

   String objektTypId();

   String data();
}
