package se.fk.rimfrost.framework.handlaggning.model;

import org.immutables.value.Value;

@Value.Immutable
public interface IndividYrkandeRoll
{
   Idtyp individ();

   String yrkandeRollId();
}
