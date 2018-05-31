package chapter4.operations;

import java.util.Map;

import org.apache.storm.trident.operation.BaseFunction;
import org.apache.storm.trident.operation.TridentCollector;
import org.apache.storm.trident.operation.TridentOperationContext;
import org.apache.storm.trident.tuple.TridentTuple;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import chapter4.MessageMapper;

public class XMPPFunction extends BaseFunction {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(XMPPFunction.class);

	private MessageMapper mapper;
	private XMPP xmpp;

	public XMPPFunction(MessageMapper mapper) {
		this.mapper = mapper;
	}

	@Override
	public void prepare(Map conf, TridentOperationContext context) {
		LOG.debug("Prepare: {}", conf);
		super.prepare(conf, context);
		xmpp = new XMPPMock();
		xmpp.connect(null);
	}

	@Override
	public void execute(TridentTuple tuple, TridentCollector collector) {
		String message = this.mapper.toMessageBody(tuple);
		this.xmpp.send(message);
	}

	private static interface XMPP {
		public void connect(Map conf);

		public void send(String message);
	}

	public static class XMPPMock implements XMPP {
		public void connect(Map conf) {

		}

		@Override
		public void send(String message) {
			LOG.info("got xmpp message:{}", message);

		}

	}

	public static class XMPPReal implements XMPP {

		private XMPPConnection xmppConnection;
		private String to;

		XMPPReal() {

		}

		public void connect(Map conf) {

			String user = "storm@budreau.local";
			String pass = "storm";
			String server = "budreau.local";
			String to = "tgoetz@budreau.local";

			this.to = to;
			ConnectionConfiguration config = new ConnectionConfiguration(server);
			this.xmppConnection = new XMPPConnection(config);
			try {
				this.xmppConnection.connect();
				this.xmppConnection.login(user, pass);
			} catch (XMPPException e) {
				LOG.warn("Error initializing XMPP Channel", e);
			}
		}

		@Override
		public void send(String message) {
			Message msg = new Message(this.to, Type.normal);
			msg.setBody(message);
			this.xmppConnection.sendPacket(msg);

		}

	}
}
