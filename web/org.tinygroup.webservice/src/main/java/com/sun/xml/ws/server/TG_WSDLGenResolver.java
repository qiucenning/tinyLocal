/**
 *  Copyright (c) 1997-2013, www.tinygroup.org (luo_guo@icloud.com).
 *
 *  Licensed under the GPL, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.gnu.org/licenses/gpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.sun.xml.ws.server;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.transform.Result;
import javax.xml.ws.Holder;
import javax.xml.ws.WebServiceException;

import com.sun.istack.NotNull;
import com.sun.xml.stream.buffer.MutableXMLStreamBuffer;
import com.sun.xml.stream.buffer.XMLStreamBufferResult;
import com.sun.xml.ws.api.server.SDDocument;
import com.sun.xml.ws.api.server.SDDocumentSource;
import com.sun.xml.ws.wsdl.writer.WSDLResolver;

/**
 * WSDLGenerator uses WSDLResolver while creating WSDL artifacts. WSDLResolver
 * is used to control the file names and which artifact to be generated or not.
 *
 * @author Jitendra Kotamraju
 */
final class TG_WSDLGenResolver implements WSDLResolver {
    
    private final List<TG_SDDocumentImpl> docs;
    private final List<SDDocumentSource> newDocs = new ArrayList<SDDocumentSource>();
    private SDDocumentSource concreteWsdlSource;
    
    private TG_SDDocumentImpl abstractWsdl;
    private TG_SDDocumentImpl concreteWsdl;

    /**
     * targetNS -> schema documents.
     */
    private final Map<String, List<TG_SDDocumentImpl>> nsMapping = new HashMap<String,List<TG_SDDocumentImpl>>();

    private final QName serviceName;
    private final QName portTypeName;

    public TG_WSDLGenResolver(@NotNull List<TG_SDDocumentImpl> docs,QName serviceName,QName portTypeName) {
        this.docs = docs;
        this.serviceName = serviceName;
        this.portTypeName = portTypeName;

        for (TG_SDDocumentImpl doc : docs) {
            if(doc.isWSDL()) {
                SDDocument.WSDL wsdl = (SDDocument.WSDL) doc;
                if(wsdl.hasPortType())
                    abstractWsdl = doc;
            }
            if(doc.isSchema()) {
                SDDocument.Schema schema = (SDDocument.Schema) doc;
                List<TG_SDDocumentImpl> sysIds = nsMapping.get(schema.getTargetNamespace());
                if (sysIds == null) {
                    sysIds = new ArrayList<TG_SDDocumentImpl>();
                    nsMapping.put(schema.getTargetNamespace(), sysIds);
                }
                sysIds.add(doc);
            }
        }
    }
    
    /**
     * Generates the concrete WSDL that contains service element.
     *
     * @return Result the generated concrete WSDL
     */
    public Result getWSDL(String filename) {
        URL url = createURL(filename);
        MutableXMLStreamBuffer xsb = new MutableXMLStreamBuffer();
        xsb.setSystemId(url.toExternalForm());
        concreteWsdlSource = SDDocumentSource.create(url,xsb);
        newDocs.add(concreteWsdlSource);
        XMLStreamBufferResult r = new XMLStreamBufferResult(xsb);
        r.setSystemId(filename);
        return r;
    }

    /**
     * At present, it returns file URL scheme eventhough there is no resource
     * in the filesystem.
     *
     * @return URL of the generated document
     *
     */
    private URL createURL(String filename) {
        try {
            return new URL("file:///"+filename);
        } catch (MalformedURLException e) {
            // TODO: I really don't think this is the right way to handle this error,
            // WSDLResolver needs to be documented carefully.
            throw new WebServiceException(e);
        }
    }

    /**
     * Updates filename if the suggested filename need to be changed in
     * wsdl:import. If the metadata already contains abstract wsdl(i.e. a WSDL
     * which has the porttype), then the abstract wsdl shouldn't be generated
     *
     * return null if abstract WSDL need not be generated
     *        Result the abstract WSDL
     */
    public Result getAbstractWSDL(Holder<String> filename) {
        if (abstractWsdl != null) {
            filename.value = abstractWsdl.getURL().toString();
            return null;                // Don't generate abstract WSDL
        }
        URL url = createURL(filename.value);
        MutableXMLStreamBuffer xsb = new MutableXMLStreamBuffer();
        xsb.setSystemId(url.toExternalForm());
        SDDocumentSource abstractWsdlSource = SDDocumentSource.create(url,xsb);
        newDocs.add(abstractWsdlSource);
        XMLStreamBufferResult r = new XMLStreamBufferResult(xsb);
        r.setSystemId(filename.value);
        return r;
    }

    /**
     * Updates filename if the suggested filename need to be changed in
     * xsd:import. If there is already a schema document for the namespace
     * in the metadata, then it is not generated.
     *
     * return null if schema need not be generated
     *        Result the generated schema document
     */
    public Result getSchemaOutput(String namespace, Holder<String> filename) {
        List<TG_SDDocumentImpl> schemas = nsMapping.get(namespace);
        if (schemas != null) {
            if (schemas.size() > 1) {
                throw new ServerRtException("server.rt.err",
                    "More than one schema for the target namespace "+namespace);
            }
            filename.value = schemas.get(0).getURL().toExternalForm();
            return null;            // Don't generate schema
        }

        URL url = createURL(filename.value);
        MutableXMLStreamBuffer xsb = new MutableXMLStreamBuffer();
        xsb.setSystemId(url.toExternalForm());
        SDDocumentSource sd = SDDocumentSource.create(url,xsb);
        newDocs.add(sd);

        XMLStreamBufferResult r = new XMLStreamBufferResult(xsb);
        r.setSystemId(filename.value);
        return r;
    }
    
    /**
     * Converts SDDocumentSource to SDDocumentImpl and updates original docs. It
     * categories the generated documents into WSDL, Schema types.
     *
     * @return the primary WSDL
     *         null if it is not there in the generated documents
     *
     */
    public TG_SDDocumentImpl updateDocs() {
        for (SDDocumentSource doc : newDocs) {
        	TG_SDDocumentImpl docImpl = TG_SDDocumentImpl.create(doc,serviceName,portTypeName);
            if (doc == concreteWsdlSource) {
                concreteWsdl = docImpl;
            }
            docs.add(docImpl);
        }
        return concreteWsdl;
    }
    
}
