package se.fk.rimfrost.framework.handlaggning.model;

import org.immutables.value.Value;

@Value.Immutable
public interface Uppgiftsdata
{
   String informationsobjektId();

   int informationsobjektversion();

   String informationsobjekttyp();

   String data();
}
