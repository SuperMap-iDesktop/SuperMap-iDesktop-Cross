package com.supermap.desktop.process.graphics.events;

import com.supermap.desktop.process.graphics.connection.IConnection;
import com.supermap.desktop.process.graphics.storage.IConnectionManager;

import java.util.EventObject;

/**
 * Created by highsad on 2017/5/8.
 */
public class ConnectionRemovedEvent extends EventObject {
	private IConnectionManager connectionManager;
	private IConnection connection;

	/**
	 * Constructs a prototypical Event.
	 *
	 * @param connectionManager The object on which the Event initially occurred.
	 * @throws IllegalArgumentException if source is null.
	 */
	public ConnectionRemovedEvent(IConnectionManager connectionManager, IConnection connection) {
		super(connectionManager);
		this.connectionManager = connectionManager;
		this.connection = connection;
	}

	public IConnectionManager getConnectionManager() {
		return connectionManager;
	}

	public IConnection getConnection() {
		return connection;
	}
}
