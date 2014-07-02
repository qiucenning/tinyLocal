package org.tinygroup.cepcorepcsc.impl;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.cepcore.CEPCoreRemoteInterface;
import org.tinygroup.cepcore.EventProcessor;
import org.tinygroup.event.Event;
import org.tinygroup.event.ServiceInfo;
import org.tinygroup.event.central.Node;

public class ServicePcEventProcessor implements EventProcessor {
	private Node remoteNode;
	private String nodeName;
	private List<ServiceInfo> services = new ArrayList<ServiceInfo>();
	private CEPCoreRemoteInterface remoteImpl;
	private int weight ;
	
	public ServicePcEventProcessor(String nodeName, List<ServiceInfo> list,Node remoteNode,CEPCoreRemoteInterface remoteImpl,int weight) {
		this.services = list;
		this.nodeName = nodeName;
		this.remoteNode = remoteNode;
		this.remoteImpl = remoteImpl;
		this.weight = weight;
	}
	
	public void process(Event event) {
		remoteImpl.remoteprocess(event, remoteNode);
	}

	public void setCepCore(CEPCore cepCore) {

	}

	public List<ServiceInfo> getServiceInfos() {
		return services;
	}

	public String getId() {
		return nodeName;
	}

	public int getType() {
		return EventProcessor.TYPE_REMOTE;
	}

	public int getWeight() {
		return weight;
	}

}
