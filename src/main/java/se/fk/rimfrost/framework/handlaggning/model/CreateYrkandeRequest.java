package se.fk.rimfrost.framework.handlaggning.model;

import org.immutables.value.Value;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Value.Immutable
public interface CreateYrkandeRequest
{
   String erbjudandeId();

   OffsetDateTime yrkandeFrom();

   OffsetDateTime yrkandeTom();

   UUID handlaggningspecifikationId();

   List<IndividYrkandeRoll> individYrkandeRoller();

   List<ProduceratResultat> produceradeResultat();
}
