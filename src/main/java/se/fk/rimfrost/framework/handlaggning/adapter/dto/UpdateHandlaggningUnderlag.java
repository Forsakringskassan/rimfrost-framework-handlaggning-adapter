package se.fk.rimfrost.framework.handlaggning.adapter.dto;

import org.immutables.value.Value;

@Value.Immutable
public interface UpdateHandlaggningUnderlag
{

   String typ();

   String version();

   String data();

}
