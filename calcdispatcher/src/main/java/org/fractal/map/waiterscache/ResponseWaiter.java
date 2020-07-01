package org.fractal.map.waiterscache;

import org.fractal.map.message.ServletMessage;

public interface ResponseWaiter
{
	public void setResponse(ServletMessage response);

	public void setResponseTimeout();
}
