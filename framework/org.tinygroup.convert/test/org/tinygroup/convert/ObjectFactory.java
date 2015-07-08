//
// ���ļ����� JavaTM Architecture for XML Binding (JAXB) ����ʵ�� v2.2.5-2 ���ɵ�
// ����� <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �����±���Դģʽʱ, �Դ��ļ��������޸Ķ�����ʧ��
// ����ʱ��: 2015.07.07 ʱ�� 11:40:34 AM GMT+08:00 
//


package org.tinygroup.convert;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.tinygroup.convert package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.tinygroup.convert
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Librarys }
     * 
     */
    public Librarys createLibrarys() {
        return new Librarys();
    }

    /**
     * Create an instance of {@link Librarys.Library }
     * 
     */
    public Librarys.Library createLibrarysLibrary() {
        return new Librarys.Library();
    }

}
