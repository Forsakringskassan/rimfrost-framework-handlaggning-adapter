package se.fk.rimfrost.framework.handlaggning.adapter.dto;

import org.immutables.value.Value;
import java.util.List;
import java.util.UUID;

@Value.Immutable
public interface PutHandlaggningUppgiftRequest
{
   UUID kundbehovsflodeId();

   UpdateHandlaggningUppgift uppgift();

   List<UpdateHandlaggningUnderlag> underlag();

}
