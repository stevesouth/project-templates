package com.caplin.template.channel;

import com.caplin.datasource.DataSource;
import com.caplin.datasource.channel.Channel;
import com.caplin.datasource.channel.ChannelListener;
import com.caplin.datasource.messaging.record.RecordMessage;
import com.caplin.datasource.namespace.PrefixNamespace;

/**
 * Created by chsitter on 27/04/2016.
 */
public class ChannelTemplate implements ChannelListener {
    public ChannelTemplate(DataSource dataSource) {
        dataSource.addChannelListener(new PrefixNamespace("/TEMPLATE/CHANNEL"), this);
    }

    @Override
    public boolean onChannelOpen(Channel channel) {
        return false;
    }

    @Override
    public void onChannelClose(Channel channel) {

    }

    @Override
    public void onMessageReceived(Channel channel, RecordMessage recordMessage) {

    }
}
