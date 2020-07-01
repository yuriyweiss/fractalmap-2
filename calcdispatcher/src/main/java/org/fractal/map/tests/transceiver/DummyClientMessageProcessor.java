package org.fractal.map.tests.transceiver;

import java.util.UUID;

import org.fractal.map.context.Context;
import org.fractal.map.transceiver.MessageProcessor;
import org.fractal.map.transceiver.Transportable;

public class DummyClientMessageProcessor implements MessageProcessor
{
	@Override
	public void onMessageDecoded(UUID clientUUID, Transportable message)
	{
		Context.getBaseCounters().getPeriodicTransceiverMessages().getAndIncrement();
		Context.getBaseCounters().getTotalTransceiverMessages().getAndIncrement();
	}
}
