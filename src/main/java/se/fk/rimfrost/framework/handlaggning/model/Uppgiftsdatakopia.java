package se.fk.rimfrost.framework.handlaggning.model;

import org.immutables.value.Value;

@Value.Immutable
public non-sealed interface Uppgiftsdatakopia extends Uppgiftsdata
{
   String informationsobjekttyp();

   String data();
}
