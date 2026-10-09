package se.fk.rimfrost.framework.handlaggning.model;

public sealed interface Uppgiftsdata permits Uppgiftsdatakopia, Uppgiftsdatakoppling
{
   String informationsobjektId();

   int informationsobjektversion();
}
