package se.fk.rimfrost.framework.handlaggning.adapter.dto;

import jakarta.annotation.Nullable;
import org.immutables.value.Value;
import java.time.OffsetDateTime;
import java.util.UUID;

@Value.Immutable
public interface UpdateHandlaggningLagrum
{

   UUID id();

   String version();

   OffsetDateTime giltigFrom();

   @Nullable
   OffsetDateTime giltigTom();

   String forfattning();

   String kapitel();

   String paragraf();

   String stycke();

   String punkt();
}
